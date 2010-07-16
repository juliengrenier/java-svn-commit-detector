package cc.juliengrenier.commitdetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.io.SVNRepository;

import cc.juliengrenier.commitdetector.exception.CommitDetectorException;
import cc.juliengrenier.commitdetector.listener.CommitEvent;
import cc.juliengrenier.commitdetector.listener.CommitListener;

/**
 * <p>
 * This class is a service that {@link CommitListener} can 
 * register to listen repository changes.
 * 
 * </p>
 * @author julien.grenier
 *
 */
public final class CommitDetectorService{
	private static Logger log = Logger.getLogger(CommitDetectorService.class);
	private static Map<String, CommitDetectorService> serviceMap = new HashMap<String, CommitDetectorService>();
	private static enum DAEMON_STATE {RUNNING,STOPPED};
	private DAEMON_STATE state = DAEMON_STATE.STOPPED;
	private List<CommitListener> listeners = new ArrayList<CommitListener>(); 
	private Thread daemon = null;
	private String url;
	private SVNRepository repository;
	private static String[] paths = {"/"};
	
	
	/**
	 * there is no instance of that class
	 *
	 */
	private CommitDetectorService() {
		
	}

	public static CommitDetectorService getCommitDetectorFor(String url) throws CommitDetectorException{
		if(serviceMap.containsKey(url)){
			return serviceMap.get(url);
		}else{
			CommitDetectorService cds = new CommitDetectorService();
			cds.initRepository(url);
			serviceMap.put(url, cds);
			return cds;
		}
	}
	/**
	 * Start the service
	 * @throws CommitDetectorException 
	 */
	public void start() {

		state = DAEMON_STATE.RUNNING;
		if(daemon == null){
			daemon = new Thread(new SvnChangeDetectorDaemon());
			daemon.setDaemon(false);
			daemon.start();
		}
		log.debug("SvnChangeDetector is now : " + state.toString());
	}

	private void initRepository(String url) throws CommitDetectorException {
		if(StringUtils.isEmpty(url)){
			throw new IllegalArgumentException("you must provide a valid url");
		}
		this.url = url;
		repository = SvnHelper.getRepository(this.url);
	}
	
	/**
	 * Stop the service
	 */
	public void stop(){
		state = DAEMON_STATE.STOPPED;
		log.debug("SvnChangeDetector is now : " + state.toString());
	}
	
	/**
	 * Check if the service is already started
	 * @return a boolean
	 */
	public boolean isStarted(){
		return state.equals(DAEMON_STATE.RUNNING);
	}
	
	/**
	 * This method allows a <code>listener</code> to register.
	 * Every time a change is done in the repository the listener wants to listen the service
	 * will call the <code>listener.onNewRevision()</code> method. 
	 * @param listener an implementation of {@link CommitListener}
	 * @throws CommitDetectorException
	 */
	public void register(CommitListener listener) throws CommitDetectorException{
		if(!listeners.contains(listener)){
			log.info("adding "+listener.getName()+" to the listeners");
			listener.setLastSeenRevision(getYoungestRevision());
			listeners.add(listener);
		}
	}
	private long getYoungestRevision() throws CommitDetectorException{
		try {
			return repository.getLatestRevision();
		} catch (SVNException e) {
			throw new CommitDetectorException("unable to get the youngest revision", e);
		}
	}


	/**
	 * This method allows a listener to unregister itself.
	 * @param listener an implementation of {@link CommitListener}
	 */
	public synchronized void unregister(CommitListener listener){
		log.info("removing "+listener.getName()+" to the listeners");
		listeners.remove(listener);
	}
	
	/**
	 * An inner class that do the actual works of checking the state of the repositories
	 * every 30 seconds.
	 * @author julien.grenier
	 */
	private class SvnChangeDetectorDaemon implements Runnable{
		public void run() {
			try {
				while(state == DAEMON_STATE.RUNNING){
					for(Iterator<CommitListener> iter = listeners.listIterator();iter.hasNext();){
						CommitListener listener = iter.next();
						long lastSeenRev = listener.getLastSeenRevision();
						long latestRev = getYoungestRevision();
						if(lastSeenRev != latestRev){
							CommitEvent commitEvent = new CommitEvent(lastSeenRev,latestRev);
							List<SVNLogEntry> logEntries = new ArrayList<SVNLogEntry>();
							repository.log(CommitDetectorService.paths, logEntries, commitEvent.getOldRevision()+1,commitEvent.getNewRevision(),listener.isVerbose(),false);
							commitEvent.setLogEntries(logEntries);
							listener.onCommit(commitEvent);
							listener.setLastSeenRevision(latestRev);
						}
					}
					pause(30);
				}
				
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		/**
		 * Small method that pause the daemon for <code>seconds</code> seconds.
		 * @param seconds
		 */
		private void pause(int seconds) {
			try{
				Thread.sleep(seconds*1000);
			}catch(InterruptedException e){
				//Do nothing
			}
		}	
	}
}
