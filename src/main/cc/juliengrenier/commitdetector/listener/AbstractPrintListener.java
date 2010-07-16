package cc.juliengrenier.commitdetector.listener;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.io.SVNRepository;

import cc.juliengrenier.commitdetector.CommitDetectorService;
import cc.juliengrenier.commitdetector.OutputConfiguration;
import cc.juliengrenier.commitdetector.SvnHelper;
import cc.juliengrenier.commitdetector.exception.CommitDetectorException;
import cc.juliengrenier.commitdetector.listener.PrintListenerFactory.OUTPUT_TYPE;

/**
 * <p>
 * Abstract implementation of {@link CommitListener}.
 * This class provide common code that is necessary to print 
 * information everytime a commit occur.</p>
 * Implementation of that class must implements the printLogEntry method.
 * @author julien.grenier
 *
 */
public abstract class AbstractPrintListener implements CommitListener {
	private Logger log = Logger.getLogger(AbstractPrintListener.class);
	private String name = "";
	private boolean verbose = false;
	private long lastSeenRevision;
	public void setLastSeenRevision(long lastSeenRevision){
		this.lastSeenRevision = lastSeenRevision;
	}
	
	public long getLastSeenRevision(){
		return lastSeenRevision;
	}
	
	protected AbstractPrintListener(OUTPUT_TYPE outputType){
		name = outputType.name();
	}
	public String getName(){
		return name;
	}


	public void onCommit(CommitEvent event) {
		List<SVNLogEntry> logEntries = event.getLogEntries();
		for(SVNLogEntry logEntry:logEntries){
			printLogEntry(logEntry);
		}
	}

	/**
	 * This method produce the output for a <code>logEntry</code>
	 * @param logEntry The {@link SVNLogEntry} to print
	 */
	abstract protected void printLogEntry(SVNLogEntry logEntry);

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean showChangedPath) {
		this.verbose = showChangedPath;
	}

	public abstract void setOutputConfiguration(OutputConfiguration outputConfig) ;
}
