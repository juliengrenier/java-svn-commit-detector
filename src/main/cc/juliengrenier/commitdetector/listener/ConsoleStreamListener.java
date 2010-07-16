package cc.juliengrenier.commitdetector.listener;

import java.io.PrintStream;

import cc.juliengrenier.commitdetector.OutputConfiguration;
import cc.juliengrenier.commitdetector.exception.CommitDetectorException;
import cc.juliengrenier.commitdetector.listener.PrintListenerFactory.OUTPUT_TYPE;

public class ConsoleStreamListener extends PrintStreamListener {
	private PrintStream outputStream;

	/**
	 * Create a {@link PrintStreamListener} that will prints the output to the console
	 * @throws CommitDetectorException 
	 */
	protected ConsoleStreamListener(){
		super(OUTPUT_TYPE.CONSOLE);
		outputStream = System.out;
		log.debug("output is the console");
	}

	@Override
	/**
	 * There is no outputLocation since we are outputting the logs into the console
	 */
	public void setOutputConfiguration(OutputConfiguration outputConfig) {
		//On purpose empty
	}

	@Override
	protected PrintStream getOutputStream() {
		return outputStream;
	}
}
