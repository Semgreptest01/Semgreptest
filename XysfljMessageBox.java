package xysk;

final public class XysfljMessageBox{
	private String code = "";
	private String message = "";
	public XysfljMessageBox(String c,String m){
		code = c;
		message = m;
	}
	public String getCode(){
		return code;
	}
	public String getMessage(){
		return message;
	}
}
