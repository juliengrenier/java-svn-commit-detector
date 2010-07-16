package cc.juliengrenier.commitdetector.listener;

import cc.juliengrenier.commitdetector.OutputConfiguration;
import cc.juliengrenier.commitdetector.exception.CommitDetectorException;

public final class PrintListenerFactory {
	/**
	 * available output type 
	 * @author julien.grenier
	 */
	public static enum OUTPUT_TYPE {CONSOLE(ConsoleStreamListener.class),RSS(RSSListener.class),FILE(FileStreamListener.class);
		private Class<? extends AbstractPrintListener> clazz = null;
		private OUTPUT_TYPE(Class<? extends AbstractPrintListener> clazz) {
			this.clazz = clazz;
		}
		
		public AbstractPrintListener newInstance(OutputConfiguration oc) throws InstantiationException, IllegalAccessException{
			AbstractPrintListener newInstance = clazz.newInstance();
			newInstance.setOutputConfiguration(oc); 
			return newInstance;
		}
	};
	/**
	 * There is no instance of that class
	 */
	private PrintListenerFactory(){}
	/**
	 * Get the default implementation of the {@link CommitListener}
	 * @param url
	 * @param path
	 * @return
	 * @throws CommitDetectorException
	 */
	public static AbstractPrintListener getDefault() throws CommitDetectorException{
		OutputConfiguration oc = new OutputConfiguration();
		oc.setVerbose(false);
		return getImplementation(OUTPUT_TYPE.CONSOLE, oc); 
	}
	
	public static AbstractPrintListener getImplementation(OUTPUT_TYPE type, OutputConfiguration oc) throws CommitDetectorException{
		try {
			return type.newInstance(oc);
		} catch (InstantiationException e) {
			throw new CommitDetectorException(e.getMessage());
		} catch (IllegalAccessException e) {
			throw new CommitDetectorException(e.getMessage());
		}
	}
}
