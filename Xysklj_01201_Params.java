package xysk;

import javax.servlet.http.*;

public class Xysklj_01201_Params extends XysfljParameters{

    //配置するＣｏｍｂｏＢｏｘ
	public 
	XyskljPUBLISHBRANDCombo BrandformCombo = null;
	XyskljPUBLISHSALEDATECombo SaledateCombo = null;
	XyskljPUBLISHCOMCombo publishcomcombo;
    public String status = null ;
    public String allitem = null ;
    public String inout = null;							// 2003/12/18 miura:ユーザー制御
    public String selectline1 = null ;
    public String selectline2 = null ;
    public String selectline3 = null ;
    	public int NO_DATA_FLG = 0;
    final public static int COMMAND_NO_DATA	= 1;
	final public static int COMMAND_DATA		= 0;

	public Xysklj_01201_Params(HttpServletRequest request){
		super("Sxysk01201",request);
		

		//出版社
		publishcomcombo = new XyskljPUBLISHCOMCombo(this);				
		setControl(publishcomcombo);
		
		//銘柄
		BrandformCombo = new XyskljPUBLISHBRANDCombo(this);
		setControl(BrandformCombo);

		//発売日
		SaledateCombo = new XyskljPUBLISHSALEDATECombo(this);
		setControl(SaledateCombo);
		


    	allitem = getRequestParameter("allitem");
    	selectline1 = getRequestParameter("selectline1");
    	selectline2 = getRequestParameter("selectline2");
    	selectline3 = getRequestParameter("selectline3");
     	inout = getRequestParameter("inout");			// 2003/12/18 miura:ユーザー制御
    	
    	
    	//検索前はデータ無に設定		
		NO_DATA_FLG = COMMAND_NO_DATA;  

	}
}
