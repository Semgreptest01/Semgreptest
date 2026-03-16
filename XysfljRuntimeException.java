package xysk;

import java.util.*;

public class XysfljRuntimeException extends RuntimeException{
	private Exception exception = null;
	private Vector messages = null;
	public XysfljRuntimeException(Exception e,Vector m){
		super(e.getMessage());
		exception = e;
		messages = m;
	}
//	public XysfljRuntimeException(){
//		messages = new Vector();
//		messages.add("9999 システム障害です。");
//	}
	
	/**
	 * 内部で保持しているエクセプションを返す。
	 * @return Exception
	 */
	public Exception getException(){
		return exception;
	}
	
	/**
	 * @see java.lang.Throwable#getMessage()
	 */
	public Vector getMessages(){
		return messages;
	}
}
