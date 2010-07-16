package cc.juliengrenier.commitdetector.listener;

import viecili.jrss.generator.RSSFeedGenerator;
import viecili.jrss.generator.RSSFeedGeneratorFactory;
import viecili.jrss.generator.elem.Channel;
import viecili.jrss.generator.elem.Item;
import viecili.jrss.generator.elem.RSS;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNLogEntry;

import cc.juliengrenier.commitdetector.OutputConfiguration;
import cc.juliengrenier.commitdetector.exception.CommitDetectorException;
import cc.juliengrenier.commitdetector.listener.PrintListenerFactory.OUTPUT_TYPE;


/**
 * This class produce a RSS feed that publish a entry everything
 * a commit occurs.
 * @author julien.grenier
 *
 */
public final class RSSListener extends AbstractPrintListener {
	private static Logger log = Logger.getLogger(RSSListener.class);
	private Channel channel;
	private RSSFeedGenerator feedGenerator = RSSFeedGeneratorFactory.getDefault();
	private RSS feed;
	private File outputFile;
	/**
	 * 
	 * @param url The repository URL
	 * @param outputLocation The location of the rss file.
	 * @throws CommitDetectorException
	 */
	protected RSSListener(){
		super(OUTPUT_TYPE.RSS);
	}

	private void initChannel(String url) {
		channel = new Channel("title",url,"RSS feed");
		channel.setTtl(2);
		feed = new RSS();
		feed.addChannel(channel);
		
	}

	/**
	 * This method generate the feed into the output file.
	 */
	protected void printLogEntry(SVNLogEntry logEntry) {
		channel.addItem(new Item(logEntry.getMessage(),"",logEntry.getMessage()));
		try {
			feedGenerator.generateToFile(feed,outputFile);
		} catch (IOException e) {
			log.error(e);
		}
	}

	@Override
	public void setOutputConfiguration(OutputConfiguration outputConfig) {
		String location = outputConfig.getLocation();
		outputFile = new File(location);
		initChannel(location);
	}
}
