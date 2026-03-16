package xysk;

import javax.servlet.http.*;

public class Xysklj_01301_Params extends XysfljParameters{

    //配置するＣｏｍｂｏＢｏｘ
	public XyskljPUBLISHCOMCombo publishcomcombo;
    public String status = null ;
    public String allitem = null ;
    public String selectline1 = null ;
    public String selectline2 = null ;
    public String selectline3 = null ;
    public String inout = null;								// 2003/12/18 kimura:ユーザー制御追加
	public int NO_DATA_FLG = 0;
    final public static int COMMAND_NO_DATA	= 1;
	final public static int COMMAND_DATA		= 0;


	public Xysklj_01301_Params(HttpServletRequest request){
		super("Sxysk01301",request);
		

		//出版社
		publishcomcombo = new XyskljPUBLISHCOMCombo(this);				
		setControl(publishcomcombo);

    	allitem = getRequestParameter("allitem");
    	selectline1 = getRequestParameter("selectline1");
    	selectline2 = getRequestParameter("selectline2");
    	selectline3 = getRequestParameter("selectline3");
    	inout = getRequestParameter("inout");				// 2003/12/18 kimura:ユーザー制御追加
    	
    	//検索前はデータ無に設定		
		NO_DATA_FLG = COMMAND_NO_DATA;  

	}
}
