package cc.juliengrenier.commitdetector.test;

import cc.juliengrenier.commitdetector.OutputConfiguration;
import cc.juliengrenier.commitdetector.listener.AbstractPrintListener;
import cc.juliengrenier.commitdetector.listener.PrintListenerFactory;
import cc.juliengrenier.commitdetector.listener.PrintStreamListener;
import cc.juliengrenier.commitdetector.listener.RSSListener;
import cc.juliengrenier.commitdetector.listener.PrintListenerFactory.OUTPUT_TYPE;
import junit.framework.TestCase;

public class PrintListenerFactoryTest extends TestCase {
	public void testGetDefault(){
		try {
			AbstractPrintListener listener = PrintListenerFactory.getDefault();
			assertTrue(listener instanceof PrintStreamListener);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	public void testGetRSSImplementation(){
		try{
			OutputConfiguration oc = new OutputConfiguration();
			oc.setLocation( "c:\\test.rss");
			AbstractPrintListener listener = PrintListenerFactory.getImplementation(OUTPUT_TYPE.RSS,oc);
			assertNotNull(listener);
			assertTrue(listener instanceof RSSListener);
		}catch(Exception e){
			fail(e.getMessage());
		}
	}
	
	public void testGetFileImplementation(){
		try{
			OutputConfiguration oc = new OutputConfiguration();
			oc.setLocation( "c:\\test.txt");
			AbstractPrintListener listener = PrintListenerFactory.getImplementation(OUTPUT_TYPE.FILE,oc);
			assertNotNull(listener);
			assertTrue(listener instanceof PrintStreamListener);
		}catch(Exception e){
			fail(e.getMessage());
		}
	}
}
