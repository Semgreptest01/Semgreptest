package xysk;

import javax.servlet.http.*;

public class Xysklj_02301_Params extends XysfljParameters{

    //ÇÛÃÖ¤¹¤ë£Ã£ï£í£â£ï£Â£ï£ø
	public XyskljNewsPUBLISHCOMCombo publishcomcombo;
    public String status = null ;
    public String allitem = null ;
    public String selectline1 = null ;
    public String selectline2 = null ;
    public String selectline3 = null ;
    public String inout = null;
	public int NO_DATA_FLG = 0;
    final public static int COMMAND_NO_DATA	= 1;
	final public static int COMMAND_DATA		= 0;
	final public static int printDays		= 14;


	public Xysklj_02301_Params(HttpServletRequest request){
		super("Sxysk02301",request);
		

		//½ÐÈÇ¼Ò
		publishcomcombo = new XyskljNewsPUBLISHCOMCombo(this);				
		setControl(publishcomcombo);

    	allitem = getRequestParameter("allitem");
    	selectline1 = getRequestParameter("selectline1");
    	selectline2 = getRequestParameter("selectline2");
    	selectline3 = getRequestParameter("selectline3");
    	inout = getRequestParameter("inout");
    	
    	//¸¡º÷Á°¤Ï¥Ç¡¼¥¿Ìµ¤ËÀßÄê		
		NO_DATA_FLG = COMMAND_NO_DATA;  

	}
}
