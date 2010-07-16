package cc.juliengrenier.commitdetector;

import java.util.Collection;
import java.util.Iterator;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;

import cc.juliengrenier.commitdetector.exception.CommitDetectorException;

/**
 * This class wraps SVN functionality
 * @author julien.grenier
 *
 */
public final class SvnHelper{
	
	private SvnHelper(){

	}

	/**
	 * This method lists the entries of a {@link SVNRepository}
	 * @param repository
	 * @param path
	 * @throws SVNException
	 */
    @SuppressWarnings("unchecked")
	public static void listEntries(SVNRepository repository, String path)
    throws SVNException {
		Collection<SVNDirEntry> entries = repository.getDir(path, -1, null,
		        (Collection) null);
		Iterator<SVNDirEntry> iterator = entries.iterator();
		while (iterator.hasNext()) {
		    SVNDirEntry entry = iterator.next();
		    System.out.println("/" + (path.equals("") ? "" : path + "/")
		            + entry.getName() + " (author: '" + entry.getAuthor()
		            + "'; revision: " + entry.getRevision() + "; date: " + entry.getDate() + ")");
		    /*
		     * Checking up if the entry is a directory.
		     */
		    if (entry.getKind() == SVNNodeKind.DIR) {
		        listEntries(repository, (path.equals("")) ? entry.getName()
		                : path + "/" + entry.getName());
		    }
		}
    }
	
    /**
     * This method build a {@link SVNRepository} from the <code>url</code>
     * @param url The repository URL
     * @return a {@link SVNRepository} 
     * @throws CommitDetectorException
     */
	public static SVNRepository getRepository(String url) throws CommitDetectorException {
		setupLibrary();
		SVNRepository repository = null;
	    try {
	        repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
	        repository.testConnection();
	        
	    } catch (SVNException svne) {
	    	throw new CommitDetectorException("error while creating an SVNRepository for location :" + url,svne);
	    }
	    return repository;
	}
    
	/*
     * Initializes the library to work with a repository via 
     * different protocols.
     */
    private static void setupLibrary() {
         // For using over http:// and https://
        DAVRepositoryFactory.setup();
         // For using over svn:// and svn+xxx://
        SVNRepositoryFactoryImpl.setup();
         // For using over file:///
        FSRepositoryFactory.setup();
    }
}
