package cc.juliengrenier.commitdetector.exception;

public class CommitDetectorException extends Exception {
	private static final long serialVersionUID = 5750569995815036642L;

	public CommitDetectorException(){
		super();
	}
	
	public CommitDetectorException(String message){
		super(message);
	}
	public CommitDetectorException(String message,Throwable trace){
		super(message,trace);
	}
}
