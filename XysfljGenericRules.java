package xysk;

import java.util.Calendar;
import java.text.*;
import java.util.*;

final public class XysfljGenericRules{
	private final static int DEBUG_OFF = 0;
	private final static int DEBUG_ON = 1;
	private final static int DEBUG_LEVEL = DEBUG_OFF;
	
	public final static String CONT_PATH_XYSK = Xysflj_Common.CONT_PATH;
	public final static String DOWNLOAD_PATH_XYSK = Xysflj_Common.SERVLET_PATH;
	public final static String FORM_STRING_CODE = "iso-8859-1";
	public final static String SOURCE_STRING_CODE = "EUC";
	public final static String DB_STRING_CODE = "Shift_JIS";
	public final static String CLIENT_STRING_CODE = "Shift_JIS";
	public final static String DB_DRIVER = Xysflj_Common.DRIVER_NAME;
	public final static String DB_URL = Xysflj_Common.THIN_DRIVER_URL;
	public final static String DB_USER = Xysflj_Common.DB_USER;
	public final static String DB_PASS = Xysflj_Common.DB_PASS;
	public final static int CHANGE_HOUR = 6;
	public final static int CHANGE_MINUTE = 0;
	public final static String LINE_BG_COLOR_2 = "#E0FFFF";

	public final static String FIND_SLAVE_SINGLE = "";		//非多重の識別文字列(コンボボックス作成など)
	public final static String FIND_NO_ORG = "______";		//全国検索時の識別文字列
//	public final static String DB_URL_SLAVE_SINGLE = "jdbc:oracle:thin:@10.64.8.40:1521:LE4A";
//	public final static String DB_URL_SLAVE_SINGLE = "jdbc:oracle:thin:@10.173.6.107:1521:ORCL";
	public final static String DB_URL_SLAVE_SINGLE = "jdbc:oracle:thin:@10.64.8.29:1523:LE6B";
	public final static String DB_USER_SLAVE_SINGLE = "hxy022";
	public final static String DB_PASS_SLAVE_SINGLE = "hxy022";

	public final static String LOG_FUNC_ID_SEARCH = "1";
	public final static String LOG_FUNC_ID_DOWNLOAD = "2";
	public final static String LOG_FUNC_ID_PRINT = "3";
	public final static String LOG_FUNC_ID_INSERT = "4";
	public final static String LOG_FUNC_ID_UPDATE = "5";
	public final static String LOG_FUNC_ID_DELETE = "6";
	
	public final static String SCREEN_NM_120 = "120 出版社−発行形態";
	public final static String SCREEN_NM_130 = "130 出版社　銘柄／発売日 (全国）別";
	public final static String SCREEN_NM_220 = "220 新聞社−発行形態";
	public final static String SCREEN_NM_230 = "230 新聞社　商品 (全国）別";

	final static public String exchangeOutString(String value){
		String ret = value;
		if(null != ret && false == DB_STRING_CODE.equals(SOURCE_STRING_CODE)){
			try{
				ret = new String(value.toString().getBytes(DB_STRING_CODE),SOURCE_STRING_CODE);
			}
			catch(java.io.UnsupportedEncodingException ex){
			}
		}
		return convertParticularChar(ret);
	}
	final static public String exchangeOutString(StringBuffer value){
		return exchangeOutString(value.toString());
	}
	
	final static public String convertFormString(String s){
		
		String x = s;
		if(null != x && false == FORM_STRING_CODE.equals(SOURCE_STRING_CODE)){
			try{
				x = new String(s.getBytes(XysfljGenericRules.FORM_STRING_CODE), XysfljGenericRules.SOURCE_STRING_CODE);
				x = convertParticularChar(x);
			}
			catch(Exception e){
			}
		}
		
		return x;
	}

	final static public String getRequestParameter(javax.servlet.http.HttpServletRequest req, String param, String def) {
		String ret = def;
		String s = (String)req.getParameter(param);
		if(null != s){
			ret = XysfljGenericRules.convertFormString(s);
		}
		return ret;
	}
	
	final static public void outputMessage(String s){
		outputMessage(s, null);
	}
	final static public void outputMessage(String s, Calendar cal){
		if(DEBUG_OFF < DEBUG_LEVEL){
			synchronized(System.out){
				String d = "";
				try{
					if(null == cal){
						cal = Calendar.getInstance();
					}
					SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss.SSS");
					d = df.format(cal.getTime());
				}
				catch(Exception e){
					d = "                       ";
				}
				
				boolean firstLine = true;
				StringTokenizer st = new StringTokenizer(s,"\r\n");
				while (st.hasMoreTokens()) {
					StringBuffer sb = new StringBuffer();
					if(true == firstLine){
						sb.append("[" + d + "] ");
						firstLine = false;
					}
					else{
						sb.append("                          ");
					}
					String tmp = st.nextToken();
					sb.append(tmp);
//					System.out.println(sb.toString());
				}
			}
		}
	}

	/**
	 * 文字列を変換する
	 * @return 置換後の文字列
	 * @param inputString 元の文字列
	 * @param serchString 置換元文字列
	 * @param replaceString 置換文字列
	 */
	static public String replaceString(String inputString, String serchString, String replaceString) {
		int serchStringIndex = 0;
		String t = inputString;
		StringBuffer stringBuffer = new StringBuffer(inputString);
		int serchStringLength = serchString.length();
		int replaceStringLength = replaceString.length();
		while(-1 != (serchStringIndex = stringBuffer.toString().indexOf(serchString,serchStringIndex))){
			stringBuffer.replace(serchStringIndex,serchStringIndex+serchStringLength,replaceString);
			t = stringBuffer.toString();
			serchStringIndex += replaceStringLength;
		}
		return stringBuffer.toString();
	}

	
	/**
	 * 特殊文字を変換する
	 * @param s
	 * @return String
	 */
	public static String convertParticularChar(String s){
		String value = s;
		if(null != value){
			if(SOURCE_STRING_CODE.equals("EUC")){	//ソースの文字コードがEUCのとき
				value = toJIS(value);
			}
			else if(SOURCE_STRING_CODE.equals("Shift_JIS")){	//ソースの文字コードがシフトJISのとき
				value = toCp932(value);
			}
		}
		return value;
	}
	
	private static String toJIS(String s) {
		StringBuffer sb = new StringBuffer();
		char c;
		for (int i = 0; i < s.length(); i++) {
			c  = s.charAt(i);
			switch (c) {
				case 0xff3c:	// FULLWIDTH REVERSE SOLIDUS ->
				c = 0x005c; // REVERSE SOLIDUS
				break;
				case 0xff5e:	// FULLWIDTH TILDE ->
				c = 0x301c; // WAVE DASH
				break;
				case 0x2225:	// PARALLEL TO ->
				c = 0x2016; // DOUBLE VERTICAL LINE
				break;
				case 0xff0d:	// FULLWIDTH HYPHEN-MINUS ->
				c = 0x2212; // MINUS SIGN
				break;
				case 0xffe0:	// FULLWIDTH CENT SIGN ->
				c = 0x00a2; // CENT SIGN
				break;
				case 0xffe1:	// FULLWIDTH POUND SIGN ->
				c = 0x00a3; // POUND SIGN
				break;
				case 0xffe2:	// FULLWIDTH NOT SIGN ->
				c = 0x00ac; // NOT SIGN
				break;
			}
			sb.append(c);
		}
		return new String(sb);
	}


	private static String toCp932(String s) {
		StringBuffer sb = new StringBuffer();
		char c;
		for (int i = 0; i < s.length(); i++) {
			c  = s.charAt(i);
			switch (c) {
				case 0x005c:	// REVERSE SOLIDUS ->
				c = 0xff3c; // FULLWIDTH REVERSE SOLIDUS
				break;
				case 0x301c:	// WAVE DASH ->
				c = 0xff5e; // FULLWIDTH TILDE
				break;
				case 0x2016:	// DOUBLE VERTICAL LINE ->
				c = 0x2225; // PARALLEL TO
				break;
				case 0x2212:	// MINUS SIGN ->
				c = 0xff0d; // FULLWIDTH HYPHEN-MINUS
				break;
				case 0x00a2:	// CENT SIGN ->
				c = 0xffe0; // FULLWIDTH CENT SIGN
				break;
				case 0x00a3:	// POUND SIGN ->
				c = 0xffe1; // FULLWIDTH POUND SIGN
				break;
				case 0x00ac:	// NOT SIGN ->
				c = 0xffe2; // FULLWIDTH NOT SIGN
				break;
			}
			sb.append(c);
		}
		return new String(sb);
	}
}
