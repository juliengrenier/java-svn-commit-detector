package cc.juliengrenier.commitdetector.listener;

import cc.juliengrenier.commitdetector.exception.CommitDetectorException;

public interface CommitListener {
	/**
	 * Method that will be call everytime there's a commit
	 * @param oldRevision
	 * @param newRevision
	 */
	public void onCommit(CommitEvent event);
	

	/**
	 * The name of the listener
	 * @return
	 */
	public String getName();
	
	/**
	 * The last seen revision
	 * @return
	 */
	public long getLastSeenRevision();
	/**
	 * To update the last seen revision
	 * @param lastSeenRevision
	 */
	public void setLastSeenRevision(long lastSeenRevision);
	
	public boolean isVerbose();
}
