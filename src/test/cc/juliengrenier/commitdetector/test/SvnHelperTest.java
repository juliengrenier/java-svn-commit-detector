package cc.juliengrenier.commitdetector.test;
import org.tmatesoft.svn.core.io.SVNRepository;

import cc.juliengrenier.commitdetector.SvnHelper;
import cc.juliengrenier.commitdetector.exception.CommitDetectorException;
import junit.framework.TestCase;

public class SvnHelperTest extends TestCase {
	public void testGetRepository(){
		String url = "file:///svn/repo";
		try {
			SVNRepository repos = SvnHelper.getRepository(url);
			assertNotNull(repos);
			assertEquals(repos.getLocation().toString(),url);
		} catch (CommitDetectorException e) {
			fail(e.getMessage());
		}
	}
	
	public void testInvalidGetRepository(){
		String url = "http://FALIDA/svn/ERROR";
		try {
			SvnHelper.getRepository(url);
			fail("repository is invalid");
		} catch (CommitDetectorException e) {
		}
	}
}
