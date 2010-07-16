package cc.juliengrenier.commitdetector.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tmatesoft.svn.core.SVNLogEntry;

public class StatsCommitListener implements CommitListener {
	private CommitListener listener;
	private Map<String, Integer> commitsPerUser = new HashMap<String, Integer>();
	private Map<String, Integer> filesPerUser = new HashMap<String, Integer>();
	
	public StatsCommitListener(CommitListener listener){
		this.listener = listener;
	}
	public long getLastSeenRevision() {
		return this.listener.getLastSeenRevision();
	}

	public String getName() {
		return this.listener.getName();
	}

	public boolean isVerbose() {
		return this.listener.isVerbose();
	}

	public void onCommit(CommitEvent event) {
		this.listener.onCommit(event);
		List<SVNLogEntry> logEntries = event.getLogEntries();
		for(SVNLogEntry entry : logEntries){
			String user = entry.getAuthor();
			if(commitsPerUser.containsKey(user)){
				Integer commits = commitsPerUser.get(user);
				commitsPerUser.put(user, commits+1);
			}else{
				commitsPerUser.put(user, 1);
			}
			Integer commitsCount = commitsPerUser.get(user);
			
			if(isVerbose()){
				int commitedFiles = entry.getChangedPaths().size();
				if(filesPerUser.containsKey(user)){
					Integer numbFiles = filesPerUser.get(user);
					commitsPerUser.put(user, numbFiles+commitedFiles);
				}else{
					commitsPerUser.put(user, commitedFiles);
				}	
			}
		}
	}

	public void setLastSeenRevision(long lastSeenRevision) {
		this.listener.setLastSeenRevision(lastSeenRevision);
	}

}
