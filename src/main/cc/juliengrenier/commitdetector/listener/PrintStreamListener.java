package cc.juliengrenier.commitdetector.listener;

import java.io.PrintStream;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;

import cc.juliengrenier.commitdetector.listener.PrintListenerFactory.OUTPUT_TYPE;


/**
 * this class is an implementation of the {@link AbstractPrintListener}.
 * This class will print basic information about each changes.
 * @author julien.grenier
 *
 */
public abstract class PrintStreamListener extends AbstractPrintListener{
	protected static Logger log = Logger.getLogger(PrintStreamListener.class);
	


	
	public PrintStreamListener(OUTPUT_TYPE type){
		super(type);
	}

	@SuppressWarnings("unchecked")
	protected void printLogEntry(SVNLogEntry logEntry) {
		getOutputStream().println("\nAuthor : "+logEntry.getAuthor());
		getOutputStream().println("Message : "+logEntry.getMessage());
		getOutputStream().println("Revision : "+logEntry.getRevision());
		getOutputStream().println("Date : "+logEntry.getDate());
		getOutputStream().println();
		if(isVerbose()){
			printChangedPath(logEntry.getChangedPaths());
		}
		
	}

	abstract protected PrintStream getOutputStream();

	/**
	 * Print detailed informations (each files affected by the commit)
	 * @param changedPaths a map of changed paths
	 */
	private void printChangedPath(Map<String, SVNLogEntryPath> changedPaths) {
		 if (changedPaths.size() > 0) {
			 getOutputStream().println("Changed paths:\n");
             Set<Map.Entry<String, SVNLogEntryPath>> changedPathsSet = changedPaths.entrySet();
             
             for (Map.Entry<String, SVNLogEntryPath> me : changedPathsSet) {
            	 SVNLogEntryPath entryPath = me.getValue();
            	 getOutputStream().println("\t"
                         + entryPath.getType()
                         + "\t"
                         + entryPath.getPath()
                         + ((entryPath.getCopyPath() != null) ? " (from "
                                 + entryPath.getCopyPath() + " revision "
                                 + entryPath.getCopyRevision() + ")" : ""));
             }
         }
	}
}
