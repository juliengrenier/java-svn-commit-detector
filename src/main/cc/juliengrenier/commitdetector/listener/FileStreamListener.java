package cc.juliengrenier.commitdetector.listener;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import cc.juliengrenier.commitdetector.OutputConfiguration;
import cc.juliengrenier.commitdetector.exception.CommitDetectorException;
import cc.juliengrenier.commitdetector.listener.PrintListenerFactory.OUTPUT_TYPE;

public class FileStreamListener extends PrintStreamListener {
	private PrintStream outputStream;

	/**
	 * Create a {@link PrintStreamListener} that will prints the output to file located at <code>outputLocation</code>
	 * @throws CommitDetectorException
	 */
	protected FileStreamListener() {
		super(OUTPUT_TYPE.FILE);

	}

	@Override
	public void setOutputConfiguration(OutputConfiguration outputConfig) {
		try {
			outputStream = new PrintStream(outputConfig.getLocation());
			log.debug("The output is located at "+outputConfig);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("File "+outputConfig+" doesn't exist");
		}
	}

	@Override
	protected PrintStream getOutputStream() {
		return outputStream;
	}
}
