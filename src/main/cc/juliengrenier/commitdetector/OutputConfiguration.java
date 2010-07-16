package cc.juliengrenier.commitdetector;

import cc.juliengrenier.commitdetector.listener.PrintListenerFactory.OUTPUT_TYPE;

public class OutputConfiguration {
	private boolean verbose = true;
	private String location = null;
	public boolean isVerbose() {
		return verbose;
	}
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String outputLocation) {
		this.location = outputLocation;
	}
	
}
