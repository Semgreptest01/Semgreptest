package xysk;

public class XysfljException extends Exception{
	private Exception exception = null;
	public XysfljException(Exception e){
		super(e.getMessage());
		exception = e;
	}
	public XysfljException(){
	}
	public Exception getException(){
		return exception;
	}
}
