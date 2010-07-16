package cc.juliengrenier.commitdetector;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import cc.juliengrenier.commitdetector.exception.CommitDetectorException;
import cc.juliengrenier.commitdetector.listener.AbstractPrintListener;
import cc.juliengrenier.commitdetector.listener.PrintListenerFactory;
import cc.juliengrenier.commitdetector.listener.PrintListenerFactory.OUTPUT_TYPE;

/**
 * This is the main entry point for the CommitDetector application
 * @author julien.grenier
 *
 */
public class CommitDetector {
	private static Logger log = Logger.getLogger(CommitDetector.class);
	public CommitDetector(String url,OUTPUT_TYPE type, OutputConfiguration oc) throws CommitDetectorException{
		log.debug("url : "+ url+ " output type "+ type.name() + " verbose "+ oc.isVerbose());
		AbstractPrintListener printListener = PrintListenerFactory.getImplementation(type, oc);
		printListener.setVerbose(oc.isVerbose());
		CommitDetectorService cds = CommitDetectorService.getCommitDetectorFor(url);
		cds.register(printListener);
		if(!cds.isStarted()){
			cds.start();
		}
	}
	public static void main(String[] args) {
		String url = "";
		OUTPUT_TYPE type = OUTPUT_TYPE.CONSOLE;
		String outputLocation = null;
		boolean verbose = false;
		List<String> argumentsList = Arrays.asList(args);
		if(argumentsList.isEmpty()){
			showHelp();
		}
		
		for(String argument : argumentsList){
			if(argument.startsWith("-h")){
				showHelp();
			}else if(argument.startsWith("-u:")){
				url = argument.substring(3);
			}else if(argument.startsWith("-c")){
				type = OUTPUT_TYPE.CONSOLE;
			}else if(argument.startsWith("-r:")){
				type = OUTPUT_TYPE.RSS;
				outputLocation = argument.substring(3);
			}else if(argument.startsWith("-f:")){
				type = OUTPUT_TYPE.FILE;
				outputLocation = argument.substring(3);
			}else if(argument.startsWith("-v")){
				verbose = true;
			}else{
				showHelp();
			}
		}
		OutputConfiguration oc = new OutputConfiguration();
		oc.setLocation(outputLocation);
		oc.setVerbose(verbose);
		try {
			new CommitDetector(url,type, oc);
		} catch (CommitDetectorException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}

	private static void showHelp() {
		System.err.println("usage : CommitDetector -[c|r|f]:[fileName] -u:[repository] -v\n-c:Console output(filename is ignored)\n-r:RSS output\n-f:File Output\n-v:Verbose(Show changed files)");
		System.exit(0);
	}

}
