package cc.juliengrenier.commitdetector.listener;

import java.util.List;

import org.tmatesoft.svn.core.SVNLogEntry;

public class CommitEvent {
	private long oldRevision;
	private long newRevision;
	private List<SVNLogEntry> logEntries;
	
	public CommitEvent(long oldRevision, long newRevision) {
		super();
		this.oldRevision = oldRevision;
		this.newRevision = newRevision;
	}

	public long getOldRevision() {
		return oldRevision;
	}
	
	public long getNewRevision() {
		return newRevision;
	}

	public void setLogEntries(List<SVNLogEntry> logEntries) {
		this.logEntries = logEntries;
	}
	public List<SVNLogEntry> getLogEntries(){
		return this.logEntries;
	}
}
