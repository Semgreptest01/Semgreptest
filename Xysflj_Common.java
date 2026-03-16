/**
*-------------------------------------------------------------------------------
* クラス名			Xysflj_Common.class
* システム名称		ＭＳＩシステム
* 名称				システム・共通クラス
* 会社名or所属名	ＣＶＳシステム事業部
* 作成日			2003/06/10 00:00:00
* @author			A.Konishi
* @since			1.0
* @version			1.0
* *** 修正履歴 ***
* No. Date		 Aouther		Description
* 01  2003/09/19 A.Konishi		ダウンロード時、開くを二度表示させない修正
* 02  2006/04/24 K.Sasaki		OAS用設定追加
* 03  2006/04/26 K.Sasaki		文字化け対応メソッド修正   public static String getStrCvt(  
* 04  2006/06/08 K.Sasaki		文字化け対応 名称関連取得時にメソッドXysflj_Common.getStrCvt()を介す
* 05  2006/06/13 O.Ogawara		サービス時間外のコネクションクローズ処理追加
*-------------------------------------------------------------------------------
*/
package xysk;
import java.io.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Xysflj_Common extends Object {
	// *****************************************************************************
	// * 基本的には、以下に続く共通変数を変更することで、全CLASSの遠隔コントロール
	// * を行うように心がけること。つまり、プラットフォームに依存する部分は、ここで
	// * 集中管理するようにすること。それ以外のビジネス・ロジック部を各Servletクラス
	// * にコード実装するようにすること。ビジネス・ロジックに関係する共通ロジックは、
	// * ここに実装すると混乱することになります。このCLASSがFAT_CLASS（肥大）になら
	// * ないように注意すべきです。
	// * それらのビジネス・ロジックは、ＥＪＢ化すべきです。
	// * （または、さらなる共通CLASS実装ですが、それならＥＪＢ化するのが王道ですが
	// * 　もし共通CLASS実装する場合は、 Bcommon.class という名前にします。）
	// * この Common.class と Bcommon.class は、
	// * Application ServerのStatic Fileとして外部配置するのが良いでしょう。
	// * 静的ファイルの位置は、Document ROOTからの相対パスは、../../contdata/xx 固定に
	// * しておくのが無難ですが下記変数を修正すれば、他の階層構造も柔軟に対応可能です。
	// *****************************************************************************
	//	public final static String DRIVER_NAME		= "org.gjt.mm.mysql.Driver";	// Driver Name
	//	public final static String THIN_DRIVER_URL	= "jdbc:mysql://localhost:3306/testdb";
	// *****************************************************************************

	// **** OAS(汎用社外用) ***
	public final static String DEBUG_SW			= "0";								// 0:本番 1:DEBUG中
	public final static String CON_SW			= "1";								// 1:JNDI(iAS)　2:Tomcat　3:J2SDKEE
	public final static String CHAR_SET			= "EUC-JP";							// Shift_Jis or x-euc-jp
	public final static String CONT_PATH		= "/XYSK/contdata/XYSK/";			// "/CONTDATA/XY/" or "../CONTDATA/XY/"
	public final static String JSP_PATH			= "/";								// JSP PATH
	public final static String SERVLET_PATH		= "/XYSK/servlet/xysk.";			// "/NASApp/XETEST/" or "/XETEST/servlet/xetest."
	public final static String JNDI_NAME		= "jdbc/XYSK_ORACLE022";			// JNDI Name
	public static String THIN_DRIVER_URL		= "";
	public final static String DRIVER_NAME		= "oracle.jdbc.driver.OracleDriver";// Driver Name
	public static String DB_USER				= "";								// DB User ID
	public static String DB_PASS				= "";								// DB Password
	public static String DB_IP					= "";								// DB Machine IP
/*
	// **** OAS ***
	public final static String DEBUG_SW			= "0";								// 0:本番 1:DEBUG中
	public final static String CON_SW			= "1";								// 1:JNDI(iAS)　2:Tomcat　3:J2SDKEE
	public final static String CHAR_SET			= "EUC-JP";							// Shift_Jis or x-euc-jp
	public final static String CONT_PATH		= "/NASApp/XYSK/contdata/XYSK/";	// "/CONTDATA/XY/" or "../CONTDATA/XY/"
	public final static String JSP_PATH			= "/";								// JSP PATH
	public final static String SERVLET_PATH		= "/NASApp/XYSK/servlet/xysk.";		// "/NASApp/XETEST/" or "/XETEST/servlet/xetest."
	public final static String JNDI_NAME		= "jdbc/XYSK_ORACLE022";			// JNDI Name
	public static String THIN_DRIVER_URL		= "";
	public final static String DRIVER_NAME		= "oracle.jdbc.driver.OracleDriver";// Driver Name
	public static String DB_USER				= "";								// DB User ID
	public static String DB_PASS				= "";								// DB Password
	public static String DB_IP					= "";								// DB Machine IP
*/
	// **** iAS ***
//	public final static String DEBUG_SW			= "0";								// 0:本番 1:DEBUG中
//	public final static String CON_SW			= "1";								// 1:JNDI(iAS)　2:Tomcat　3:J2SDKEE
//	public final static String CHAR_SET			= "EUC-JP";							// Shift_Jis or x-euc-jp
//	public final static String CONT_PATH		= "/contdata/XYSK/";				// "/CONTDATA/XY/" or "../CONTDATA/XY/"
//	public final static String JSP_PATH			= "/";								// JSP PATH
//	public final static String SERVLET_PATH		= "/NASApp/XYSK/";				// "/NASApp/XETEST/" or "/XETEST/servlet/xetest."
//	public final static String JNDI_NAME		= "jdbc/XYSK_ORACLE022";			// JNDI Name( 192.20.31.126 LDAP )
//	public static String THIN_DRIVER_URL		= " ";
//	public final static String DRIVER_NAME		= "oracle.jdbc.driver.OracleDriver";// Driver Name
//	public static String DB_USER				= "hxy022";							// DB User ID
//	public static String DB_PASS				= "hxy022";							// DB Password
//	public static String DB_IP					= "10.64.8.29";						// DB Machine IP

	// **** Tomcat ***
/*
	public final static String DEBUG_SW			= "1";								// 0:本番 1:DEBUG中
	public final static String CON_SW			= "2";								// 1:JNDI(iAS)　2:Tomcat　3:J2SDKEE
	public final static String CHAR_SET			= "Shift_Jis";						// Shift_Jis or x-euc-jp
	public final static String CONT_PATH		= "/XYSK/contdata/XYSK/";			// "/contdata/XE/" or "../../contdata/XE/"
	public final static String JSP_PATH			= "/";								// JSP PATH
	public final static String SERVLET_PATH		= "/XYSK/servlet/xysk.";			// "/NASApp/XY/" or "/XYDL/servlet/xydl."
	public final static String JNDI_NAME		= "jdbc/XYSK_ORACLE022";			// JNDI Name( 192.20.31.126 LDAP
//	public final static String THIN_DRIVER_URL	= "jdbc:oracle:thin:@Dosv_2027:1521:MSI";
//	public static String THIN_DRIVER_URL		= "jdbc:oracle:thin:@192.254.6.23:1521:lora01";
//	public static String THIN_DRIVER_URL		= "jdbc:oracle:thin:@10.64.8.40:1521:LE4A"; // EP4
	public static String THIN_DRIVER_URL		= "jdbc:oracle:thin:@10.64.8.29:1523:LE6B"; // EP4

	public final static String DRIVER_NAME		= "oracle.jdbc.driver.OracleDriver";// Driver Name
	public static String DB_USER				= "hxy022";							// DB User ID
	public static String DB_PASS				= "hxy022";							// DB Password
	public static String DB_IP					= "10.64.8.29";					// DB Machine IP
*/

//	*********** E10K移行時追加(2006/04/26) *****************************************
	public static String WAVE_DASH 			= "\u301c";				// 〜 iASをデフォルト値とする。
	public static String DOUBLE_VERTICAL	= "\u2016";				// ‖ iASをデフォルト値とする。
	public static String MINUS_SIGN			= "\u2212";				// − iASをデフォルト値とする。
	public static String EM_DASH			= "\u2014";				// ―
//	********************************************************************************
	/**
	********************************************************************************
	* メソッド名		debug_Msg
	* @param		HttpServletRequest	request
	* @param		String			wk_msg
	* @param		Vector			msg_box
	* @return		nothing
	********************************************************************************
	*/
	public static void debug_Msg(HttpServletRequest request, String wk_msg, Vector msg_box) {
		if( DEBUG_SW.equals("1") ){
			String d = curr_Date();
			String s = curr_Time();
			String r = request.getRemoteAddr();
//			System.out.println( d + " " + s + " " + r + " "	 + request.getServletPath() + " " + wk_msg);
			msg_box.addElement( d + " " + s + " "
			 + request.getServletPath() + " " + wk_msg);
		}
	}
	/**
	********************************************************************************
	* メソッド名		curr_Time
	* @return		HH:MM:SS
	********************************************************************************
	*/
	public static String curr_Time() {
		int gmt = 9;
		int hh, ms, ss;
		String	s, st_hh, st_ms, st_ss;
		String	tz[]=TimeZone.getAvailableIDs(gmt * 60 * 60 * 1000);
		Calendar d=Calendar.getInstance(TimeZone.getTimeZone(tz[0]));
		hh	= d.get(d.HOUR_OF_DAY); st_hh = "";
		st_hh += hh;
		ms	= d.get(d.MINUTE); st_ms = "";st_ms += ms;
		ss	= d.get(d.SECOND); st_ss = "";st_ss += ss;
		if( hh < 10 ){ st_hh = "0" + hh; }
		if( ms < 10 ){ st_ms = "0" + ms; }
		if( ss < 10 ){ st_ss = "0" + ss; }
		s = st_hh + ":" + st_ms + ":" + st_ss;
		return s;
	}
	/**
	********************************************************************************
	* メソッド名		curr_Date
	* @return		YYYY/MM/DD
	********************************************************************************
	*/
	public static String curr_Date() {
		int gmt = 9;
		int yyyy, mm, dd;
		String	s, st_yyyy, st_mm, st_dd;
		String	tz[]=TimeZone.getAvailableIDs(gmt * 60 * 60 * 1000);
		Calendar d=Calendar.getInstance(TimeZone.getTimeZone(tz[0]));
		yyyy	= d.get(d.YEAR); st_yyyy = "";st_yyyy += yyyy;
		mm	= d.get(d.MONTH)+1; st_mm = "";st_mm += mm;
		dd	= d.get(d.DATE); st_dd = "";st_dd += dd;
		if( yyyy < 1000 ){ st_yyyy = "0" + yyyy; }
		if( yyyy < 100	){ st_yyyy = "00" + yyyy; }
		if( yyyy < 10	){ st_yyyy = "000" + yyyy; }
		if( mm < 10 ){ st_mm = "0" + mm; }
		if( dd < 10 ){ st_dd = "0" + dd; }
		s = st_yyyy + "/" + st_mm + "/" + st_dd;
		return s;
	}
	/**
	********************************************************************************
	* メソッド名		session_Set
	* @param		Xysflj_Dbcom		dbcom
	* @param		HttpServletRequest	request
	* @param		HttpSession 	session
	* @param		Vector			msg_box
	* @return		boolean ( true:OK false:ERR )
	********************************************************************************
	*/
	public static boolean session_Set(	Xysflj_Dbcom		dbcom,
										HttpServletRequest	request,
										HttpSession 	session,
										Vector			msg_box) {
		try{																		// try
			String s		= "";													//	
			String namecd		= (String)request.getParameter("namecd");			// Security Infomation Get - User Code
			if(namecd == null){ 													// 共通セキュリティ以外から遷移してきた
				return false;														// 場合は、すでにセッションは存在している
			}																		// として returnする。
			String logdate		= (String)request.getParameter("logdate");			// Security Infomation Get - Login Date
			String sosiki		= (String)request.getParameter("sosiki");			// Security Infomation Get - 組織コード
			String sosiki_sec	= (String)request.getParameter("sosiki_sec");		// Security Infomation Get - 組織セキュリティ
			String sosiki_grp	= (String)request.getParameter("sosiki_grp");		// Security Infomation Get - 組織グループ
			String syokui		= (String)request.getParameter("syokui");			// Security Infomation Get - 職位コード
			String syokui_sec	= (String)request.getParameter("syokui_sec");		// Security Infomation Get - 職位セキュリティ
			String syokui_grp	= (String)request.getParameter("syokui_grp");		// Security Infomation Get - 職位グループ
			String syoribi		= (String)request.getParameter("syoribi");			// Security Infomation Get - 現セキュリティ日付
			String kyumenu		= (String)request.getParameter("kyumenu");			// Security Infomation Get - 新旧フラグ
			String kennin		= (String)request.getParameter("kennin");			// Security Infomation Get - 兼任フラグ
			String kyudatetime	= (String)request.getParameter("kyudatetime");		// Security Infomation Get - 旧組織終了日
			String renban		= (String)request.getParameter("renban");			// Security Infomation Get - メニュー連番
			String inout_cls	= (String)request.getParameter("inout_cls");		// Security Infomation Get - 内外フラグ
			if( DEBUG_SW.equals("1") ){
				msg_box.addElement( "***DEBUG*** 利用者コード　　　：" + namecd );
				msg_box.addElement( "***DEBUG*** ログイン日　　　　：" + logdate );
				msg_box.addElement( "***DEBUG*** 組織コード　　　　：" + sosiki );
				msg_box.addElement( "***DEBUG*** 組織セキュリティ　：" + sosiki_sec );
				msg_box.addElement( "***DEBUG*** 組織グループ　　　：" + sosiki_grp );
				msg_box.addElement( "***DEBUG*** 職位コード　　　　：" + syokui );
				msg_box.addElement( "***DEBUG*** 職位セキュリティ　：" + syokui_sec );
				msg_box.addElement( "***DEBUG*** 職位グループ　　　：" + syokui_grp );
				msg_box.addElement( "***DEBUG*** 現セキュリティ日付：" + syoribi );
				msg_box.addElement( "***DEBUG*** 新旧フラグ　　　　：" + kyumenu );
				msg_box.addElement( "***DEBUG*** 兼任フラグ　　　　：" + kennin );
				msg_box.addElement( "***DEBUG*** 旧組織終了日　　　：" + kyudatetime );
				msg_box.addElement( "***DEBUG*** メニュー連番　　　：" + renban );
				msg_box.addElement( "***DEBUG*** 内外フラグ　　　　：" + inout_cls );
			}
			session.setAttribute("namecd", namecd); 								// セッション情報の出力・利用者コード
			session.setAttribute("logdate", logdate);								// セッション情報の出力・ログイン日
			session.setAttribute("sosiki", sosiki); 								// セッション情報の出力・組織コード
			session.setAttribute("sosiki_sec", sosiki_sec); 						// セッション情報の出力・組織セキュリティ
			session.setAttribute("sosiki_grp", sosiki_grp); 						// セッション情報の出力・組織グループ
			session.setAttribute("syokui", syokui); 								// セッション情報の出力・職位コード
			session.setAttribute("syokui_sec", syokui_sec); 						// セッション情報の出力・職位セキュリティ
			session.setAttribute("syokui_grp", syokui_grp); 						// セッション情報の出力・職位グループ
			session.setAttribute("syoribi", syoribi);								// セッション情報の出力・現セキュリティ日付
			session.setAttribute("kyumenu", kyumenu);								// セッション情報の出力・新旧フラグ
			session.setAttribute("kennin", kennin); 								// セッション情報の出力・兼任フラグ
			session.setAttribute("kyudatetime", kyudatetime);						// セッション情報の出力・旧組織終了日
			session.setAttribute("renban", renban); 								// セッション情報の出力・メニュー連番
			session.setAttribute("inout_cls", inout_cls);							// セッション情報の出力・内外フラグ
			return true;															// Return OK
		}catch(Exception e){														//	
			msg_Out( 2, msg_box );													// セッション情報・書き込みエラーです。
			return false;															//	
		}																			// Return ERR
	}																				//	
	/**
	********************************************************************************
	* メソッド名		today_Get
	* @param		Xysflj_Dbcom	dbcom
	* @param		String	sosiki_cd
	* @param		Vector	msg_box
	* @return		String ( NOT null:OK null:ERR )
	* ****注意*** 現在、日付コントロールは、XEM970としていますが、ＤＢ実装完了後、
	*			  テーブルネームが確定すれば、修正すること。
	********************************************************************************
	*/
	public static String today_Get( Xysflj_Dbcom	dbcom,
									String		sosiki_cd,
									Vector		msg_box ) { 						// *Start of today_Get
//		return curr_Date(); 														// *** DEBUG ***
		try{																		// try
			// *********************************************************************
			// *** 日付コントロールより当日日付を取得
			// *********************************************************************
			String sql	=	"SELECT DSPLC_DT_1" 									// SELECT String
					+	"  FROM XEM970" 											//
					+	" WHERE DT_CLS      = '0'"									//
					+	"   AND DT_DSPLC_NO = 0";									//
			String s = "";															//
			if (!get_Connect_user(dbcom, sosiki_cd)) {								// 接続ユーザID/PASS取得
				throw new Exception();												// throw Exception
			}
			try{																	// try
				msg_box.addElement( "*** DB Connection Call ***" ); 				// *** DEBUG ***
				Connection connection = dbcom.DBConnect(DB_USER, DB_PASS);			// DB connection
				msg_box.addElement( "*** SQL Quesry ***" ); 						// *** DEBUG ***
				ResultSet rs = dbcom.querySQL(connection, sql); 					// Execute Query
				if(rs.next()){														// Loop
					s = rs.getDate("DSPLC_DT_1").toString().replace('-','/');		// 日付ＧＥＴ
				}																	//	
				rs.close(); 														// Close
				dbcom.DBDisconnect(connection); 									// Disconnect
				connection = null;													// Connection = null
			}catch(SQLException e){ 												// SQL Error
				msg_box.addElement( "today_Get:SQLEception:" + e.getMessage() );	// *** ERR ***
				throw new Exception();												// throw Exception
			}																		//	
			if(s == null || s.equals("")){											// Get Data null ?
				msg_Out( 6, msg_box );												// 日付コントロールに例外が発生しました。
				throw new Exception();												// throw Exception
			}																		//	
			return s;																// Normal return
		}catch(Exception e){														// catch
			msg_box.addElement( "today_Get:Exception:" + e.getMessage() );			// *** ERR ***
			return null;															// Abnormal return
		}																			//
	}																				// *End of today_Get
	/**
	********************************************************************************
	* メソッド名		get_Connect_user
	* @param		Xysflj_Dbcom	dbcom
	* @param		String	sosiki_code
	* @return		boolean ( true:OK false:ERR )
	* ****注意*** 現在、固定にしているが仕様が確定すれば修正すること。、
	********************************************************************************
	*/
	public static boolean get_Connect_user( Xysflj_Dbcom	dbcom,
									String		sosiki_code 	) { 				//
//		return true;

		try{
			String user_sw = sosiki_Chk(dbcom, sosiki_code);
			String sql = "SELECT RMT_IP,ORACLE_USER,ORACLE_PASSWD"
						+ " FROM WT002C";

			if ( user_sw.equals("1") ) {
				sql += " WHERE SOSIKI_KBN = '2'"
					+	" AND SOSIKI_CD = '" + sosiki_code + "'";
			} else if ( user_sw.equals("2") ) {
				sql += " WHERE SOSIKI_KBN = '4'"
					+	" AND SOSIKI_CD = '" + sosiki_code + "'";
			} else {
				sql += " WHERE SOSIKI_KBN = '0'"
					+	" AND SOSIKI_CD = '0'";
			}
			try{																	// try

				Connection connection = dbcom.DBConnect(DB_USER, DB_PASS);			// DB connection

				ResultSet rs = dbcom.querySQL(connection, sql); 						// Execute Query
				if(rs.next()){															// +++ rs.next +++
				  DB_IP = rs.getString("RMT_IP");
//				  System.out.println( DB_IP );
				  if ( DB_IP.equals("192.254.6.22") ) {
//					  THIN_DRIVER_URL = "jdbc:oracle:thin:@192.254.6.22:1521:LORA02"; // EP2
//					  System.out.println("THIN_DRIVER_URL:"+THIN_DRIVER_URL);
				  } else if ( DB_IP.equals("192.254.6.23") ) {
//					  THIN_DRIVER_URL = "jdbc:oracle:thin:@192.254.6.23:1521:LORA01"; // EP3
//					  System.out.println("THIN_DRIVER_URL:"+THIN_DRIVER_URL);
				  } else if ( DB_IP.equals("192.254.6.21") ) {
//					  THIN_DRIVER_URL = "jdbc:oracle:thin:@192.254.6.21:1521:LORA04"; // EP1
//					  System.out.println("THIN_DRIVER_URL:"+THIN_DRIVER_URL);
				  } else{
//					  THIN_DRIVER_URL = "jdbc:oracle:thin:@192.254.6.23:1521:LORA01"; // EP3
//					  System.out.println("THIN_DRIVER_URL:"+THIN_DRIVER_URL);
				  }
					DB_USER = rs.getString("ORACLE_USER");
					DB_PASS = rs.getString("ORACLE_PASSWD");
//					System.out.println("DB_USER:"+DB_USER);
//					System.out.println("DB_PASS:"+DB_PASS);
				}																		//	
				rs.close(); 															// Close

				dbcom.DBDisconnect(connection); 									// Disconnect
				connection = null;													// Connection = null
			}catch(SQLException e){ 												// SQL Error
				throw new Exception("SQLEception:" + e.getMessage());				// throw Exception
			}																		//	

			return true;															// Normal return
		}catch(Exception e){														// catch
			System.out.println( "get_Connect_user:Exception:" + e.getMessage() );// *** ERR ***
			return false;															// Abnormal return
		}																			//

	}																				// *End of get_Connect_user
	/**
	********************************************************************************
	* メソッド名		Get_Connect_No
	* @param			Xysflj_Dbcom	dbcom
	* @param			Connection		con
	* @param			String			sosiki_code
	* @return			int ( 1〜20:OK 0:ERR )
	********************************************************************************
	*/
	public static int Get_Connect_No(	Xysflj_Dbcom	dbcom,
											Connection		con,
											String			sosiki_code	) {
		try{
			int i = 0;
			String user_sw = sosiki_Chk(dbcom, sosiki_code);
			String sql = "SELECT RMT_IP,ORACLE_USER,ORACLE_PASSWD"
						+ " FROM WT002C";

			if ( user_sw.equals("1") ) {
				sql += " WHERE SOSIKI_KBN = '2'"
					+	" AND SOSIKI_CD = '" + sosiki_code + "'";
			} else if ( user_sw.equals("2") ) {
				sql += " WHERE SOSIKI_KBN = '4'"
					+	" AND SOSIKI_CD = '" + sosiki_code + "'";
			} else {
				sql += " WHERE SOSIKI_KBN = '0'"
					+	" AND SOSIKI_CD = '0'";
			}
//			System.out.println( sql );
			try{																// try
				con = dbcom.DBConnect(DB_USER, DB_USER);						// DB connection

				ResultSet rs = dbcom.querySQL(con, sql); 						// Execute Query
				if(rs.next()){													// +++ rs.next +++
				  DB_IP = rs.getString("RMT_IP");
//				  System.out.println( DB_IP );
				  if ( DB_IP.equals("192.254.6.22") ) {
//					  THIN_DRIVER_URL = "jdbc:oracle:thin:@192.254.6.22:1521:LORA02"; // EP2
//					  System.out.println("THIN_DRIVER_URL:"+THIN_DRIVER_URL);
				  } else if ( DB_IP.equals("192.254.6.23") ) {
//					  THIN_DRIVER_URL = "jdbc:oracle:thin:@192.254.6.23:1521:LORA01"; // EP3
//					  System.out.println("THIN_DRIVER_URL:"+THIN_DRIVER_URL);
				  } else if ( DB_IP.equals("192.254.6.21") ) {
//					  THIN_DRIVER_URL = "jdbc:oracle:thin:@192.254.6.21:1521:LORA04"; // EP1
//					  System.out.println("THIN_DRIVER_URL:"+THIN_DRIVER_URL);
				  } else{
//					  THIN_DRIVER_URL = "jdbc:oracle:thin:@192.254.6.23:1521:LORA01"; // EP3
//					  System.out.println("THIN_DRIVER_URL:"+THIN_DRIVER_URL);
				  }
//					System.out.println("ORACLE_USER:"+rs.getString("ORACLE_USER"));
					i = (new Integer(rs.getString("ORACLE_USER").substring(3,6))).intValue();
//					System.out.println("多重Ｎｏ．:"+i);
				}																	//	
				rs.close(); 														// Close
				dbcom.DBDisconnect(con); 											// Disconnect
				con = null;															// Connection = null
			}catch(SQLException e){ 												// SQL Error
				throw new Exception("SQLEception:" + e.getMessage());				// throw Exception
			}																		//	
			return i;																// Normal return
		}catch(Exception e){														// catch
			System.out.println( "get_Connect_user:Exception:" + e.getMessage() );// *** ERR ***
			return 0;																// Abnormal return
		}																			//
	}																				// *End of get_Connect_user
	/**
	********************************************************************************
	* メソッド名		sosiki_Chk
	* @param		Xysflj_Dbcom	dbcom
	* @param		String	sosiki_code
	* @return		sosiki_sw (1:運営部 2:DR 0:その他)
	********************************************************************************
	*/
	public static String sosiki_Chk(	Xysflj_Dbcom	dbcom,
									String		sosiki_cd		) { 				// *Start of sosiki_Chk
		try{																		// try
			String sql	=	"";
			try{																	// try
//				if (!get_Connect_user(dbcom, sosiki_cd)) {							// 接続ユーザID/PASS取得
//					throw new Exception();											// throw Exception
//				}
//				System.out.println( "*** DB Connection Call ***" ); 				// *** DEBUG ***
				Connection connection = dbcom.DBConnect(DB_USER, DB_PASS);			// DB connection
//				System.out.println( "*** SQL Quesry ***" ); 						// *** DEBUG ***

				sql = "SELECT DR,UNEI_DIV"											// SELECT String
					+ " FROM UY007M"
					+ " WHERE DR='" + sosiki_cd + "'"
					+ "  AND SV='0000000'"
					+ "  AND NOHINA='000'"
					+ "  AND TEN='000000'"
					+ "  AND SYORI_KBN < '7'";

//				System.out.println( sql );											// *** DEBUG ***
				ResultSet rs = dbcom.querySQL(connection, sql); 					// Execute Query
				if(rs.next()){														// +++ rs.next +++
					// DRデータがみつかった
					rs.close(); 													// Close
					dbcom.DBDisconnect(connection); 								// Disconnect
					connection = null;												// Connection = null
					return "2";
				} else {
					rs.close(); 													// Close
					// DRデータがみつからなかった
					sql = "SELECT UNEI_DIV" 										// SELECT String
						+ " FROM UY007M"
						+ " WHERE UNEI_DIV='" + sosiki_cd + "'"
						+ "  AND DR='000000'"
						+ "  AND SV='0000000'"
						+ "  AND NOHINA='000'"
						+ "  AND TEN='000000'"
						+ "  AND SYORI_KBN < '7'";

//					System.out.println( sql );										// *** DEBUG ***
					rs = dbcom.querySQL(connection, sql);							// Execute Query

					if(rs.next()){													// +++ rs.next +++
						// DIVデータがみつかった
						rs.close(); 													// Close
						dbcom.DBDisconnect(connection); 							// Disconnect
						connection = null;											// Connection = null
						return "1";
					}
				}																	//	
				rs.close(); 														// Close

				dbcom.DBDisconnect(connection); 									// Disconnect
				connection = null;													// Connection = null
			}catch(SQLException e){ 												// SQL Error
				throw new Exception("SQLEception:" + e.getMessage());				// throw Exception
			}																		//	

			return "0"; 															// Normal return
		}catch(Exception e){														// catch
			System.out.println( "sosiki_Chk:Exception:" + e.getMessage() ); 	// *** ERR ***
			return "0"; 															// Abnormal return
		}																			//
	}																				// *End of sosiki_Chk
	/**
	********************************************************************************
	* メソッド名		time_Chk
	* @param		Xysflj_Dbcom	dbcom
	* @param		String			renban
	* @param		String			inout_cls
	* @param		Vector			msg_box
	* @return		boolean ( true:OK false:ERR )
	* ****注意***
	* ストアド・ファンクション：smzzop001をＤＢサーバーに実装してください。
	* DEBUG_SW = 1 のときは、サービス時間チェックはしないで false でリターンする。
	********************************************************************************
	*/
	public static boolean time_Chk( Xysflj_Dbcom	dbcom,
									String		renban,
									String		inout,
									Vector		msg_box ) { 						// *Start of time_Chk
//		return true;

		if( DEBUG_SW.equals("1") ) return true;										// DEBUG 中はチェックしない。
		String	err_sw		=	""; 												// エラー・フラグ
		Connection connection = null;
		String bak_THIN_DRIVER_URL ="";
		if ( CON_SW.equals("2") ) {
			bak_THIN_DRIVER_URL = THIN_DRIVER_URL;
		}

// **************** E10K (2006/06/13) 04 *****************************************
		try {																		// *try*
			connection = dbcom.DBConnect("hxy022", "hxy022");						// connection
		}catch(Exception e){														// catch Exception
			try{
				if(connection != null){
					dbcom.DBDisconnect(connection);connection = null;
					if( DEBUG_SW.equals("1") ){
						System.out.println( "***finally*** RDB Disconnected !" );
					}
				}
			}catch(Exception ex){
				if( DEBUG_SW.equals("1") ){
					System.out.println( "***finally*** RDB Disconnected Error !" );
				}
			}
			msg_box.addElement( e.getMessage() );	
			return false;														// Return ERR
		}																		// *catch end*

		try{																		// *try*
			CallableStatement cstmt = null; 										// SQL Statement
			if ( inout == null || inout.equals("") || inout.equals("0")) {
				cstmt = connection.prepareCall("begin sfzzop001.main(?, ?, ?); end;");	// Oracle Stored Function Call
			} else {
				cstmt = connection.prepareCall("begin smzzop001.main(?, ?, ?); end;");	// Oracle Stored Function Call
			}
			cstmt.setString (1, renban );											//	
			cstmt.registerOutParameter (2, Types.CHAR); 							//	
			cstmt.registerOutParameter (3, Types.CHAR); 							//	
			cstmt.execute();														// 実行
			err_sw = cstmt.getString(2);											// 結果取得
			cstmt.close();															// Statement Close
			dbcom.DBDisconnect(connection); 										// ＤＢ接続解除
			if ( CON_SW.equals("2") ) {
				THIN_DRIVER_URL = bak_THIN_DRIVER_URL;
			}
		}catch (SQLException se) {													// *catch SQLException*
			if ( CON_SW.equals("2") ) {
				THIN_DRIVER_URL = bak_THIN_DRIVER_URL;
			}
			msg_box.addElement( se.getMessage() );									// *** ERR ***
			msg_Out( 12, msg_box ); 												// サービス時間チェック（SQL Exception）
			return false;															// Return ERR
		}catch(Exception e){														// *catch Exception*
			if ( CON_SW.equals("2") ) {
				THIN_DRIVER_URL = bak_THIN_DRIVER_URL;
			}
			msg_box.addElement( e.getMessage() );									// *** ERR ***
			msg_Out( 13, msg_box ); 												// サービス時間チェック（Exception）
			return false;															// Return ERR
		}																			// *catch end*
		finally {
			try{
				if(connection != null){
					dbcom.DBDisconnect(connection);connection = null;
					if( DEBUG_SW.equals("1") ){
						System.out.println( "***finally*** RDB Disconnected !" );
					}
				}
			}catch(Exception ex){
				if( DEBUG_SW.equals("1") ){
					System.out.println( "***finally*** RDB Disconnected Error !" );
				}
			}
		}
// **************** E10K (2006/06/13) END 04 *************************************
		if(err_sw.equals("00")) return true;										// Status=00 OK?
		if(err_sw.equals("01")){													// Status=01 ?
			msg_Out( 14, msg_box ); 												// サービス時間外です。
			return false;															// Return ERR
		}																			//	
		if(err_sw.equals("02")){													// Status=02 ?
			msg_Out( 15, msg_box ); 												// バッチ処理中のためサービス停止中です。
			return false;															// Return ERR
		}																			//	
		if(err_sw.equals("03")){													// Status=03 ?
			msg_Out( 16, msg_box ); 												// トラブルのためサービス停止中です。
			return false;															// Return ERR
		}																			//	
		if(err_sw.equals("04")){													// Status=04 ?
			msg_Out( 17, msg_box ); 												// マシン保守のためサービス停止中です。
			return false;															// Return ERR
		}																			//	
		if(err_sw.equals("08")){													// Status=08 ?
			msg_Out( 18, msg_box ); 												// サービス時間チェック（NO DATA FOUND）
			return false;															// Return ERR
		}																			//	
		if(err_sw.equals("09")){													// Status=09 ?
			msg_Out( 19, msg_box ); 												// サービス時間チェック（データ取得エラー）
			return false;															// Return ERR
		}																			//	
		if(err_sw.equals("21")){													// Status=21 ?
			msg_Out( 35, msg_box ); 												// 月次確定処理中のためサービス停止中です。
			return false;															// Return ERR
		}																			//	
		msg_Out( 20, msg_box ); 													// サービス時間チェック（その他）
		return false;																// Return ERR
	}																				// *End of Method

	/**
	********************************************************************************
	* メソッド名		getTorihikisakiCD
	* @param		Xysflj_Dbcom	dbcom
	* @param		String			userid
	* @param		String			yukodate(YYYY/MM/DD)
	* @param		Vector			msg_box
	* @return		boolean ( true:OK false:ERR )
	* ****注意***
	* ストアド・ファンクション：smzzop001をＤＢサーバーに実装してください。
	* DEBUG_SW = 1 のときは、サービス時間チェックはしないで ""; でリターンする。
	********************************************************************************
	*/
/*	public static String getTorihikisakiCD( Xysflj_Dbcom	dbcom,
									String		userid,
									String		yukodate,
									Vector		msg_box ) { 						// *Start of time_Chk
//		return "113000";
*/

public static Vector getTorihikisakiCD ( Xysflj_Dbcom	dbcom,
							String		userid,
							String		yukodate,
							Vector		msg_box ) { 						// *Start of time_Chk
		String sql	=	"";
		Vector TorihikiCD = new Vector();
		try{
			Connection connection = dbcom.DBConnect(DB_USER, DB_PASS);		// DB connection
				sql	=	"SELECT 取引先コード"
						+	" ,種別区分"
						+	" FROM UPM_利用者_取引"
						+	" WHERE 利用者コード='" + userid + "'"
						+	" ORDER BY 取引先コード"
						+	" ,種別区分";
			TorihikiCD.removeAllElements();
			ResultSet rs = dbcom.querySQL(connection, sql); 					// Execute Query
			while(rs.next()){													// +++ rs.next +++
				TorihikiCD.addElement(rs.getString("取引先コード"));				//	
				TorihikiCD.addElement(rs.getString("種別区分")); 				//	
			}																	//	
//			System.out.println(TorihikiCD);
			rs.close(); 														// Close
			dbcom.DBDisconnect(connection); 									// Disconnect
			connection = null;													// Connection = null
		}catch(SQLException e){ 												// SQL Error
			msg_Out( 19, msg_box );											// 取引相手先コード取得（データ取得エラー）
		}																		// 
		return TorihikiCD;

/*		if( DEBUG_SW.equals("1") ) return "";										// DEBUG 中はチェックしない。
		String	err_sw		=	""; 												// エラー・フラグ
		String	torihikiCD	=	""; 												// 取引相手先コード
		Connection connection = null;
		String bak_THIN_DRIVER_URL ="";
		String YYYYMMDD = yukodate.substring(0,4) + yukodate.substring(5,7) + yukodate.substring(8); 
		if ( CON_SW.equals("2") ) {
			bak_THIN_DRIVER_URL = THIN_DRIVER_URL;
		}

		try {																		// *try*
			connection = dbcom.DBConnect("hxy022", "hxy022");						// connection
		}catch(Exception e){														// catch Exception
			msg_box.addElement( e.getMessage() );	
			return "";																// Return ERR
		}																			// *catch end*

		try{																		// *try*
			CallableStatement cstmt = null; 										// SQL Statement
			cstmt = connection.prepareCall("begin smzzop101.main(?, ?, ?, ?); end;");	// Oracle Stored Function Call
			cstmt.setString (1, userid );											//	
			cstmt.setString (2, YYYYMMDD );											//	
			cstmt.registerOutParameter (3, Types.CHAR); 							//	
			cstmt.registerOutParameter (4, Types.CHAR); 							//	
			cstmt.execute();														// 実行
			err_sw = cstmt.getString(3);											// 結果取得
			torihikiCD = cstmt.getString(4);										// 取引相手先コード取得
			if ( torihikiCD == null ) {
				torihikiCD = "";
			}
			cstmt.close();															// Statement Close
			dbcom.DBDisconnect(connection); 										// ＤＢ接続解除
			if ( CON_SW.equals("2") ) {
				THIN_DRIVER_URL = bak_THIN_DRIVER_URL;
			}
		}catch (SQLException se) {													// *catch SQLException*
			if ( CON_SW.equals("2") ) {
				THIN_DRIVER_URL = bak_THIN_DRIVER_URL;
			}
			msg_box.addElement( se.getMessage() );									// *** ERR ***
			msg_Out( 12, msg_box ); 												// 取引相手先コード取得（SQL Exception）
			return "";																// Return ERR
		}catch(Exception e){														// *catch Exception*
			if ( CON_SW.equals("2") ) {
				THIN_DRIVER_URL = bak_THIN_DRIVER_URL;
			}
			msg_box.addElement( e.getMessage() );									// *** ERR ***
			msg_Out( 13, msg_box ); 												// 取引相手先コード取得（Exception）
			return "";																// Return ERR
		}																			// *catch end*
		if(err_sw.equals("00")) return torihikiCD;									// Status=00 OK?
		if(err_sw.equals("08")){													// Status=08 ?
			msg_Out( 18, msg_box ); 												// 取引相手先コード取得（NO DATA FOUND）
			return "";																// Return ERR
		}																			//	
		if(err_sw.equals("09")){													// Status=09 ?
			msg_Out( 19, msg_box ); 												// 取引相手先コード取得（データ取得エラー）
			return "";																// Return ERR
		}																			//	
		if(err_sw.equals("13")){													// Status=21 ?
			msg_Out( 35, msg_box ); 												// セキュリティシステムトラブルのためサービス停止中です。
			return "";																// Return ERR
		}																			//	
		if(err_sw.equals("14")){													// Status=21 ?
			msg_Out( 35, msg_box ); 												// セキュリティシステムマシン保守のためサービス停止中です。
			return "";																// Return ERR
		}																			//	
		msg_Out( 20, msg_box ); 													// 取引相手先コード取得（その他）
		return "";																	// Return ERR
*/
	}																				// *End of Method

	/**
	********************************************************************************
	* メソッド名		nin_Chk
	* @param		HttpSession 	session
	* @param		Vector			msg_box
	* @return		boolean ( true:OK false:ERR )
	********************************************************************************
	*/
	public static boolean nin_Chk( HttpSession session, Vector msg_box) {			// *Start of Method
		try{																		// try
			if((String)session.getAttribute("namecd") == null)						// 不正アクセス?
				throw new Exception();												//	
			return true;															// Normal
		}catch(Exception e){														// catch
			msg_Out( 21, msg_box ); 												// 認証チェック・エラーです。（またはタイムアウトです。）
			return false;															// Abnormal return
		}																			// Return ERR
	}																				// *End of Method


	/**
	********************************************************************************
	* メソッド名		getParam （Ｔｏｍｃａｔのみ）
	* @param		HttpServletRequest	request
	* @param		Hashtable		hash_data
	* @param		Vector			msg_box
	* @return		boolean ( true:OK false:ERR )
	* 【説明】　 コール方法
	*			  Hashtable hash_data = new Hashtable();
	*			  if(Xewtlj_common.getParam( request, hash_data, msg_box ))
	*				 throw new Exception();
	*			  in_data001 = (String)hash_data.get("data001");
	********************************************************************************
	*/
	public static boolean getParam( HttpServletRequest	request,
					Hashtable		hash_data,
					Vector			msg_box ) {
		try{
			String s;
			String as[];
			for(	Enumeration enum = request.getParameterNames();
				enum.hasMoreElements();
				hash_data.put(s, new String(as[0].getBytes("ISO8859_1"), "SJIS"))
//				hash_data.put(s, new String(as[0].getBytes("8859_1"), "JISAutoDetect"))
			)
			{
				s = (String)enum.nextElement();
				as = request.getParameterValues(s);
			}
			return true;
		}catch(Exception e){
			msg_Out( 22, msg_box ); // ＧＥＴパラメータ＆文字列変換・エラーです。
			return false;
		}
	}
	/**
	********************************************************************************
	* メソッド名		msg_Out
	* @param		int 	msg_id
	* @param		Vector		msg_box
	* @return		boolean ( true:OK false:ERR )
	********************************************************************************
	*/
	public static boolean msg_Out(	int 	msg_id,
									Vector		msg_box ) { 						// *Start of Method
		String msg_tbl[] = new String[100];
		int i = 0;
		try{																		// try
			msg_tbl[0]	= "次の入力をしてください。";
			msg_tbl[1]	= "日付コントロール日付が null です。";
			msg_tbl[2]	= "セッション情報・書き込みエラーです。";
			msg_tbl[3]	= "RDB Disconnected !";
			msg_tbl[4]	= "RDB Disconnection Error !";
			msg_tbl[5]	= "データが見つかりません !!";
			msg_tbl[6]	= "日付コントロールに例外が発生しました。";
			msg_tbl[7]	= "利用者名・取得エラーです。";
			msg_tbl[8]	= "組織名・取得エラーです。";
			msg_tbl[9]	= "エラーがあります。";
			msg_tbl[10] = "職位名・取得エラーです。";
			msg_tbl[11] = "サービス時間チェック（DB Connection ERR）";
			msg_tbl[12] = "サービス時間チェック（SQL Exception）";
			msg_tbl[13] = "サービス時間チェック（Exception）";
			msg_tbl[14] = "サービス時間外です。";
			msg_tbl[15] = "バッチ処理中のためサービス停止中です。";
			msg_tbl[16] = "トラブルのためサービス停止中です。";
			msg_tbl[17] = "マシン保守のためサービス停止中です。";
			msg_tbl[18] = "サービス時間チェック（NO DATA FOUND）";
			msg_tbl[19] = "サービス時間チェック（データ取得エラー）";
			msg_tbl[20] = "サービス時間チェック（その他）";
			msg_tbl[21] = "認証チェック・エラーです。（またはタイムアウトです。）";
			msg_tbl[22] = "ＧＥＴパラメータ＆文字列変換・エラーです。";
			msg_tbl[23] = "処理権限の取得が出来ませんでした。";
			msg_tbl[24] = "登録権限がありません。";
			msg_tbl[25] = "変更権限がありません。";
			msg_tbl[26] = "ダウンロード権限がありません。";
			msg_tbl[27] = "削除権限がありません。";
			msg_tbl[28] = "照会権限がありません。";
			msg_tbl[29] = "特権の権限がありません。";
			msg_tbl[30] = "データベースのレコード登録に失敗しました。";
			msg_tbl[31] = "データベースのレコード更新に失敗しました。";
			msg_tbl[32] = "データベースのROLL BACKに失敗しました。";
			msg_tbl[33] = "データベースのROLL BACKをしました。";
			msg_tbl[34] = "データベースのレコード削除に失敗しました。";

			msg_box.addElement( msg_tbl[msg_id] );
			return true;															//
		}catch(Exception e){														// catch
			msg_box.addElement( "msg_Out:Exception:" + e.getMessage() );			// *** ERR ***
			return false;															// Abnormal return
		}																			//	
	}																				// * End of Method
	/**
	********************************************************************************
	* メソッド名		csv_Out
	* @param		int 	retu
	* @param		Vector		in_data
	* @param		Vector		out_data
	* @return		boolean ( true:OK false:ERR )
	********************************************************************************
	*/
	public static boolean csv_Out(	int 	retu,
									Vector		in_data,
									Vector		out_data	) { 					// *Start of Method
		try{																		// try
			String str_csv="";
			int ele_max = in_data.size();
//			System.out.println("ele_max=" +ele_max);
			for (int i=0; i< ele_max; i++) {
				str_csv += "\"" + in_data.elementAt(i) + "\"";
				if ( ((i+1) % retu) == 0 ) {
					out_data.addElement(str_csv);
					str_csv = "";
				} else {
					str_csv += ",";
				}
			}
			return true;															//
		}catch(Exception e){														// catch
//			msg_box.addElement( "csv_Out:Exception:" + e.getMessage() );			// *** ERR ***
			return false;															// Abnormal return
		}																			//	
	}																				// * End of Method

	/**
	********************************************************************************
	* メソッド名		c_Edit
	* @param		long		in_data
	* @param		String		out_data
	* @return		String ( Not "":OK "":ERR )
	********************************************************************************
	*/
	public static String c_Edit(	long		in_data ) { 						// *Start of Method
		try{																		// try
			String wk_str=(new Long(in_data)).toString();
			return c_Edit(wk_str);													//
		}catch(Exception e){														// catch
//			msg_box.addElement( "c_Edit:Exception:" + e.getMessage() ); 			// *** ERR ***
			return "";																// Abnormal return
		}																			//	
	}																				// * End of Method

	/**
	********************************************************************************
	* メソッド名		c_Edit
	* @param		double		in_data
	* @param		int 		syousu_keta
	* @return		String ( Not "":OK "":ERR )
	********************************************************************************
	*/
	public static String c_Edit(	double		in_data,
									int 		syousu_keta ) { 					// *Start of Method
		try{																		// try
			double	wk_double=in_data*Math.pow(10,syousu_keta);
			wk_double = Math.round(wk_double);
			wk_double=wk_double/(double)Math.pow(10,syousu_keta);
			String	wk_str=(new Double(wk_double)).toString();
			int 	wk_piri_pos = wk_str.indexOf('.');
			String	wk_syosu = wk_str.substring(wk_piri_pos);
			if ( wk_syosu.length() <= syousu_keta ) {
				while ( wk_syosu.length() <= syousu_keta ) {
					wk_syosu += "0";
				}
			} else {
				wk_syosu = wk_syosu.substring(0, syousu_keta+1);
			}
			return c_Edit(wk_str.substring(0,wk_piri_pos))+wk_syosu;		//
		}catch(Exception e){														// catch
//			msg_box.addElement( "c_Edit:Exception:" + e.getMessage() ); 			// *** ERR ***
			return "";																// Abnormal return
		}																			//	
	}																				// * End of Method


	/**
	********************************************************************************
	* メソッド名		c_Edit
	* @param		String		in_data
	* @return		String ( Not "":OK "":ERR )
	********************************************************************************
	*/
	public static String c_Edit(	String		in_data ) { 					// *Start of Method
		try{																		// try
			String wk_str=in_data;
			String out_data = "";
			String fugou = "";
			if ( wk_str.indexOf("-") >=0 ) {
				fugou = "-";
				wk_str = wk_str.substring(1);
			}

			while (wk_str.length() > 3) {
				out_data = "," + wk_str.substring(wk_str.length()-3) + out_data;
				wk_str = wk_str.substring(0, wk_str.length()-3);
			}
			out_data = wk_str + out_data;
			return fugou+out_data;														  //
		}catch(Exception e){														// catch
			return "";																// Abnormal return
		}																			//	
	}																				// * End of Method

	/**
	********************************************************************************
	* メソッド名		c_EditCsv
	* @param		double		in_data
	* @param		int 		syousu_keta
	* @return		String ( Not "":OK "":ERR )
	********************************************************************************
	*/
	public static String c_EditCsv( double		in_data,
									int 		syousu_keta ) { 					// *Start of Method
		try{																		// try
			double	wk_double=in_data*Math.pow(10,syousu_keta);
			wk_double = Math.round(wk_double);
			wk_double=wk_double/(double)Math.pow(10,syousu_keta);
			String	wk_str=(new Double(wk_double)).toString();
			int 	wk_piri_pos = wk_str.indexOf('.');
			String	wk_syosu = wk_str.substring(wk_piri_pos);
			if ( wk_syosu.length() <= syousu_keta ) {
				while ( wk_syosu.length() <= syousu_keta ) {
					wk_syosu += "0";
				}
			} else {
				wk_syosu = wk_syosu.substring(0, syousu_keta+1);
			}
			return wk_str.substring(0,wk_piri_pos)+wk_syosu;						//
		}catch(Exception e){														// catch
//			msg_box.addElement( "c_Edit:Exception:" + e.getMessage() ); 			// *** ERR ***
			return "";																// Abnormal return
		}																			//	
	}																				// * End of Method

	/**
	********************************************************************************
	* メソッド名		ymd_Format
	* @param		int 		format_sw
	* @param		String		in_data
	* @return		Calendar ( Not null:OK null:ERR )
	********************************************************************************
	*/
	public static Calendar ymd_Format(	int 		format_sw,
										String		in_data) {						// *Start of Method
		try{																		// try
			Calendar out_data = Calendar.getInstance();

			switch (format_sw) {
				case 1:
				case 3:
					out_data.set((new Integer(in_data.substring(0,4))).intValue(),
									((new Integer(in_data.substring(5,7))).intValue()-1),
									(new Integer(in_data.substring(8,10))).intValue());
					break;
				case 2:
//					System.out.println("year=" +(new Integer(in_data.substring(0,4))).intValue());
//					System.out.println("month=" +((new Integer(in_data.substring(4,6))).intValue()-1));
//					System.out.println("day=" +(new Integer(in_data.substring(6,8))).intValue());
					out_data.set((new Integer(in_data.substring(0,4))).intValue(),
									((new Integer(in_data.substring(4,6))).intValue()-1),
									(new Integer(in_data.substring(6,8))).intValue());
					break;
				default:
					throw new Exception();											//	
			}
			return out_data;															//
		}catch(Exception e){														// catch
			System.out.println( "ymd_Format:Exception:" + e.getMessage() ); 		// *** ERR ***
			return null;															//	
		}																			//	
	}																				// * End of Method

	/**
	********************************************************************************
	* メソッド名		sosiki_Chiiki
	* @param		Xysflj_Dbcom	dbcom
	* @param		String	sosiki_cd
	* @param		Vector	JS_datas
	* @return		boolean ( true:OK false:ERR )
	********************************************************************************
	*/
	public static boolean sosiki_Chiiki(	Xysflj_Dbcom	dbcom,
									String		sosiki_cd,
									Vector		JS_datas	)	 {					// *Start of sosiki_Chiiki
		try{																		// try
			Vector sosiki_cds = new Vector();
			JS_datas.addElement("<SCRIPT language=\"JavaScript\">");
			JS_datas.addElement("<!--");
			JS_datas.addElement("var dummy_chiiki   =  new Array(\"('','00',true,true)\");");
			JS_datas.addElement("var Array_chiiki00 =  new Array(\"('','00',true,true)\");");

			// *********************************************************************
			// *** 商品部組織を取得
			// *********************************************************************
			String sql	=	"SELECT SKUBU_SOSIKI"									// SELECT String
						+	"  FROM UY006M" 										//
						+	" WHERE SKUBU_SOSIKI <> '90'"							//90はダミーのため
						+	"  AND SKUBU_TIIKI = '00'"								//
						+	" ORDER BY SKUBU_SOSIKI";								//
			try{																	// try
//				if ( sosiki_cd != null ) {
//					if (!get_Connect_user(dbcom, sosiki_cd)) {						// 接続ユーザID/PASS取得
//						throw new Exception();										// throw Exception
//					}
//				}
//				System.out.println( "*** DB Connection Call ***" ); 				// *** DEBUG ***
				Connection connection = dbcom.DBConnect(DB_USER, DB_USER);			// DB connection
//				System.out.println( "*** SQL Quesry ***" ); 						// *** DEBUG ***
//				System.out.println( sql );											// *** DEBUG ***
				ResultSet rs = dbcom.querySQL(connection, sql); 					// Execute Query
				while(rs.next()){													// +++ rs.next +++
					sosiki_cds.addElement(rs.getString("SKUBU_SOSIKI"));			// 商品部組織コード
				}																	//	
				rs.close(); 														// Close

				for ( int i=0; i < sosiki_cds.size(); i++) {
					JS_datas.addElement("var Array_chiiki"
										+ sosiki_cds.elementAt(i)
										+ " =  new Array(");
					JS_datas.addElement("\"('','00')\"");
					sql =	"SELECT SKUBU_TIIKI,KEY_KOMOKU_NN"						// SELECT String
						+	"  FROM UY006M" 										//
						+	" WHERE SKUBU_SOSIKI = '" + sosiki_cds.elementAt(i) +"'"//
						+	"  AND SKUBU_TIIKI <> '00'" 							//
						+	" ORDER BY SKUBU_TIIKI";								//
//					System.out.println( "*** SQL Quesry ***" ); 						// *** DEBUG ***
//					System.out.println( sql );											// *** DEBUG ***
					rs = dbcom.querySQL(connection, sql);					// Execute Query
					while(rs.next()){												// +++ rs.next +++
						JS_datas.addElement(",\"('"
									// **** E10K移行時変更対応(2006/06/08) ***
									+ Xysflj_Common.getStrCvt( rs.getString("KEY_KOMOKU_NN") ) 	// 商品部組織コード
//									+ rs.getString("KEY_KOMOKU_NN") 							// 商品部組織コード
									// **** END ******************************
									+ "','"
									+ rs.getString("SKUBU_TIIKI")					//
									+ "')\"");
					}																//	
					rs.close(); 													// Close
					JS_datas.addElement(");");
				}																	//	
					
				dbcom.DBDisconnect(connection); 									// Disconnect
				connection = null;													// Connection = null
			}catch(SQLException e){ 												// SQL Error
//				System.out.println( "sosiki_Chiiki:SQLEception:" + e.getMessage() );// *** ERR ***
				throw new Exception();												// throw Exception
			}																		//	
			JS_datas.addElement("-->");
			JS_datas.addElement("</SCRIPT>");
			return true;															// Normal return
		}catch(Exception e){														// catch
//			System.out.println( "sosiki_Chiiki:Exception:" + e.getMessage() );		// *** ERR ***
			return false;															// Abnormal return
		}																			//
	}																				// *End of today_Get

	/**
	********************************************************************************
	* メソッド名		div_Dr
	* @param		Xysflj_Dbcom	dbcom
	* @param		String	sosiki_cd
	* @param		Vector	JS_datas
	* @return		boolean ( true:OK false:ERR )
	********************************************************************************
	*/
	public static boolean div_Dr(	Xysflj_Dbcom	dbcom,
									String		sosiki_cd,
									Vector		JS_datas	)	 {					// *Start of div_Dr
		try{																		// try
			Vector div_cds = new Vector();
			JS_datas.addElement("<SCRIPT language=\"JavaScript\">");
			JS_datas.addElement("<!--");
			JS_datas.addElement("var dummy_dr   =  new Array(\"('','000000',true,true)\");");
			JS_datas.addElement("var Array_dr000000 =  new Array(\"('','000000',true,true)\");");

			// *********************************************************************
			// *** 商品部組織を取得
			// *********************************************************************
			String sql	=	"SELECT UNEI_DIV"										// SELECT String
						+	"  FROM UY011M" 										//
						+	" WHERE DR = '000000'"									//
						+	" ORDER BY OUT_SEQ";									//
			try{																	// try
//				if ( sosiki_cd != null ) {
//					if (!get_Connect_user(dbcom, sosiki_cd)) {						// 接続ユーザID/PASS取得
//						throw new Exception();										// throw Exception
//					}
//				}
//				System.out.println( "*** DB Connection Call ***" ); 				// *** DEBUG ***
				Connection connection = dbcom.DBConnect(DB_USER, DB_USER);			// DB connection
//				System.out.println( "*** SQL Quesry ***" ); 						// *** DEBUG ***
//				System.out.println( sql );											// *** DEBUG ***
				ResultSet rs = dbcom.querySQL(connection, sql); 					// Execute Query
				while(rs.next()){													// +++ rs.next +++
					div_cds.addElement(rs.getString("UNEI_DIV"));					//	
				}																	//	
				rs.close(); 														// Close

				for ( int i=0; i < div_cds.size(); i++) {
					JS_datas.addElement("var Array_dr"
										+ div_cds.elementAt(i)
										+ " =  new Array(");
					JS_datas.addElement("\"('','000000')\"");
					sql =	"SELECT DR,KEY_KOMOKU_NN"								// SELECT String
						+	"  FROM UY011M" 										//
						+	" WHERE UNEI_DIV = '" + div_cds.elementAt(i) +"'"		//
						+	"  AND DR <> '000000'"									//
						+	" ORDER BY OUT_SEQ";									//
//					System.out.println( "*** SQL Quesry ***" ); 					// *** DEBUG ***
//					System.out.println( sql );										// *** DEBUG ***
					rs = dbcom.querySQL(connection, sql);							// Execute Query
					while(rs.next()){												// +++ rs.next +++
						JS_datas.addElement(",\"('"
									// **** E10K移行時変更対応(2006/06/08) ***
									+ Xysflj_Common.getStrCvt( rs.getString("KEY_KOMOKU_NN") ) 				//	
//									+ rs.getString("KEY_KOMOKU_NN") 				//	
									// **** END ******************************
									+ "','"
									+ rs.getString("DR")							//
									+ "')\"");
					}																//	
					rs.close(); 													// Close
					JS_datas.addElement(");");
				}																	//	
					
				dbcom.DBDisconnect(connection); 									// Disconnect
				connection = null;													// Connection = null
			}catch(SQLException e){ 												// SQL Error
//				System.out.println( "div_Dr:SQLEception:" + e.getMessage() );	// *** ERR ***
				throw new Exception();												// throw Exception
			}																		//	
			JS_datas.addElement("-->");
			JS_datas.addElement("</SCRIPT>");
			return true;															// Normal return
		}catch(Exception e){														// catch
//			System.out.println( "div_Dr:Exception:" + e.getMessage() ); 			// *** ERR ***
			return false;															// Abnormal return
		}																			//
	}																				// *End of today_Get

	/**
	********************************************************************************
	* メソッド名		kikaku_Kenshu
	* @param		Xysflj_Dbcom	dbcom
	* @param		String	sosiki_cd
	* @param		Vector	JS_datas
	* @return		boolean ( true:OK false:ERR )
	********************************************************************************
	*/
	public static boolean kikaku_Kenshu(	Xysflj_Dbcom	dbcom,
									String		sosiki_cd,
									String		user_sw,
									String		sdate,
									String		edate,
									Vector		JS_datas	)	 {					// *Start of div_Dr
		try{																		// try
			Vector kikaku_cds = new Vector();
			JS_datas.addElement("<SCRIPT language=\"JavaScript\">");
			JS_datas.addElement("<!--");
			JS_datas.addElement("var dummy_kenshu       =  new Array(\"('','0000000000000',true,true)\");");
			JS_datas.addElement("var Array_kenshu00000  =  new Array(\"('','0000000000000',true,true)\");");

			// *********************************************************************
			// *** 商品部組織を取得
			// *********************************************************************
			String sql = "";

			switch ((new Integer(user_sw)).intValue()) {
				case 0:
					sql = "SELECT A.KIKAKU ITEM1"									// SELECT String
						+ " FROM tw005m A"											//
//						+ " WHERE A.YUKO_SDATE <= '" + sdate + "'"					//
//						+  " AND A.YUKO_EDATE >= '" + edate + "'"					//
						+ " ORDER BY A.KIKAKU"; 									//
					break;
				case 1:
					sql = "SELECT A.KIKAKU ITEM1"
						+  " FROM tw005m A"
						+  " WHERE"
//						+  " A.YUKO_SDATE <= '" + sdate + "'"
//						+	" AND A.YUKO_EDATE >= '" + edate + "'"
						+	" A.KIKAKU IN"
						+	 " (SELECT B.KIKAKU "
						+	   " FROM TW038T B"
						+		" WHERE"
						+		" B.EGY_DATE <= '" + sdate + "'"
						+		 " AND B.EGY_DATE >= '" + edate + "'"
						+		 " AND B.UNEI_DIV = '" + sosiki_cd +"')"
						+  " ORDER BY A.KIKAKU";
					break;
				case 2:
					sql = "SELECT A.KIKAKU ITEM1"
						+  " FROM tw005m A"
						+  " WHERE"
//						+  " A.YUKO_SDATE <= '" + sdate + "'"
//						+	" AND A.YUKO_EDATE >= '" + edate + "'"
						+	" A.KIKAKU IN"
						+	 " (SELECT B.KIKAKU "
						+	   " FROM TW037T B"
						+		" WHERE"
						+		" B.EGY_DATE <= '" + sdate + "'"
						+		 " AND B.EGY_DATE >= '" + edate + "'"
						+		 " AND B.DR = '" + sosiki_cd +"')"
						+  " ORDER BY A.KIKAKU";
					break;
				default:
					System.out.println( "kikaku_Kenshu:不正なuser_swです。");		// *** ERR ***
					throw new Exception();											// throw Exception
			}

			try{																	// try
//				if ( sosiki_cd != null ) {
//					if (!get_Connect_user(dbcom, sosiki_cd)) {						// 接続ユーザID/PASS取得
//						throw new Exception();										// throw Exception
//					}
//				}
//				System.out.println( "*** DB Connection Call ***" ); 				// *** DEBUG ***
				Connection connection = dbcom.DBConnect(DB_USER, DB_USER);			// DB connection
//				System.out.println( "*** SQL Quesry ***" ); 						// *** DEBUG ***
//				System.out.println( sql );											// *** DEBUG ***
				ResultSet rs = dbcom.querySQL(connection, sql); 					// Execute Query
				while(rs.next()){													// +++ rs.next +++
					kikaku_cds.addElement(rs.getString("ITEM1"));					//	
				}																	//	
				rs.close(); 														// Close

				for ( int i=0; i < kikaku_cds.size(); i++) {
					JS_datas.addElement("var Array_kenshu"
										+ kikaku_cds.elementAt(i)
										+ " =  new Array(");
					JS_datas.addElement("\"('','0000000000000')\"");
					sql =	"SELECT COUPON_KENSY,COUPON_KENSY_NN"					// SELECT String
						+	 " FROM TW006M" 										//
						+	  " WHERE KIKAKU = '" + kikaku_cds.elementAt(i) +"'"	//
						+	  " ORDER BY COUPON_KENSY"; 							//
//					System.out.println( "*** SQL Quesry ***" ); 					// *** DEBUG ***
//					System.out.println( sql );										// *** DEBUG ***
					rs = dbcom.querySQL(connection, sql);							// Execute Query
					while(rs.next()){												// +++ rs.next +++
						JS_datas.addElement(",\"('"
									// **** E10K移行時変更対応(2006/06/08) ***
									+ Xysflj_Common.getStrCvt( rs.getString("COUPON_KENSY_NN") )				//	
//									+ rs.getString("COUPON_KENSY_NN")				//	
									// **** END ******************************
									+ "','"
									+ rs.getString("COUPON_KENSY")					//
									+ "')\"");
					}																//	
					rs.close(); 													// Close
					JS_datas.addElement(");");
				}																	//	
					
				dbcom.DBDisconnect(connection); 									// Disconnect
				connection = null;													// Connection = null
			}catch(SQLException e){ 												// SQL Error
				System.out.println( "kikaku_Kenshu:SQLEception:" + e.getMessage() );// *** ERR ***
				throw new Exception();												// throw Exception
			}																		//	
			JS_datas.addElement("-->");
			JS_datas.addElement("</SCRIPT>");
			return true;															// Normal return
		}catch(Exception e){														// catch
			System.out.println( "kikaku_Kenshu:Exception:" + e.getMessage() );		// *** ERR ***
			return false;															// Abnormal return
		}																			//
	}																				// *End of today_Get

	/**
	********************************************************************************
	* メソッド名		sosoki_List
	* @param		Xysflj_Dbcom	dbcom
	* @param		String	sosiki_cd
	* @param		Vector	sosiki_codes
	* @param		Vector	sosiki_names
	* @return		boolean ( true:OK false:ERR )
	********************************************************************************
	*/
	public static boolean sosiki_List(	Xysflj_Dbcom	dbcom,
									String		sosiki_cd,
									Vector		sosiki_codes,
									Vector		sosiki_names	)	 {				// *Start of sosiki_List
		try{																		// try
			// *********************************************************************
			// *** 商品部組織を取得
			// *********************************************************************
			String sql	=	"SELECT SKUBU_SOSIKI, KEY_KOMOKU_NN"					// SELECT String
						+	"  FROM UY006M" 										//
						+	" WHERE SKUBU_SOSIKI <> '90'"							//90はダミーのため
						+	"  AND SKUBU_TIIKI = '00'"								//
						+	" ORDER BY SKUBU_SOSIKI";								//
			sosiki_codes.removeAllElements();
			sosiki_names.removeAllElements();
			try{																	// try
//				if ( sosiki_cd != null ) {
//					if (!get_Connect_user(dbcom, sosiki_cd)) {						// 接続ユーザID/PASS取得
//						throw new Exception();										// throw Exception
//					}
//				}
//				System.out.println( "*** DB Connection Call ***" ); 				// *** DEBUG ***
				Connection connection = dbcom.DBConnect(DB_USER, DB_USER);			// DB connection
//				System.out.println( "*** SQL Quesry ***" ); 						// *** DEBUG ***
//				System.out.println( sql );											// *** DEBUG ***
				ResultSet rs = dbcom.querySQL(connection, sql); 					// Execute Query
				while(rs.next()){													// +++ rs.next +++
					sosiki_codes.addElement(rs.getString("SKUBU_SOSIKI"));			//	
					// **** E10K移行時変更対応(2006/06/08) ***
					sosiki_names.addElement( Xysflj_Common.getStrCvt( rs.getString("KEY_KOMOKU_NN") )); 		//	
//					sosiki_names.addElement(rs.getString("KEY_KOMOKU_NN")); 		//	
					// **** END ******************************
				}																	//	
				rs.close(); 														// Close
//				System.out.println( "count:"+sosiki_codes.size() ); 				// *** DEBUG ***
					
				dbcom.DBDisconnect(connection); 									// Disconnect
				connection = null;													// Connection = null
			}catch(SQLException e){ 												// SQL Error
				System.out.println( "sosiki_List:SQLEception:" + e.getMessage() );	// *** ERR ***
				throw new Exception();												// throw Exception
			}																		// 
//			return sosiki_dt;														// Normal return
			return true;														// Normal return
		}catch(Exception e){														// catch
			System.out.println( "sosiki_List:Exception:" + e.getMessage() );				// *** ERR ***
//			return null;															// Abnormal return
			return false;															// Abnormal return
		}																			//
	}																				// *End of today_Get

	/**
	********************************************************************************
	* メソッド名		chiikiList
	* @param		Xysflj_Dbcom	dbcom
	* @param		String	sosiki_cd
	* @param		String	sosiki
	* @param		Vector	chiiki_codes
	* @param		Vector	chiiki_names
	* @return		boolean ( true:OK false:ERR )
	********************************************************************************
	*/
	public static boolean chiikiList(	Xysflj_Dbcom	dbcom,
									String		sosiki_cd,
									String		sosiki,
									Vector		chiiki_codes,
									Vector		chiiki_names	)	 {				// *Start of sosiki_List
		try{																		// try
			// *********************************************************************
			// *** 商品部組織を取得
			// *********************************************************************
			String	sql =	"SELECT SKUBU_TIIKI,KEY_KOMOKU_NN"						// SELECT String
						+	"  FROM UY006M" 										//
						+	" WHERE SKUBU_SOSIKI = '" + sosiki +"'" 				//
						+	"  AND SKUBU_TIIKI <> '00'" 							//
						+	" ORDER BY SKUBU_TIIKI";								//

			chiiki_codes.removeAllElements();
			chiiki_names.removeAllElements();
			try{																	// try
//				if ( sosiki_cd != null ) {
//					if (!get_Connect_user(dbcom, sosiki_cd)) {						// 接続ユーザID/PASS取得
//						throw new Exception();										// throw Exception
//					}
//				}
//				System.out.println( "*** DB Connection Call ***" ); 				// *** DEBUG ***
				Connection connection = dbcom.DBConnect(DB_USER, DB_USER);			// DB connection
//				System.out.println( "*** SQL Quesry ***" ); 						// *** DEBUG ***
//				System.out.println( sql );											// *** DEBUG ***
				ResultSet rs = dbcom.querySQL(connection, sql); 					// Execute Query
				while(rs.next()){													// +++ rs.next +++
					chiiki_codes.addElement(rs.getString("SKUBU_TIIKI"));			//	
					// **** E10K移行時変更対応(2006/06/08) ***
					chiiki_names.addElement( Xysflj_Common.getStrCvt( rs.getString("KEY_KOMOKU_NN") )); 		//	
//					chiiki_names.addElement(rs.getString("KEY_KOMOKU_NN")); 		//	
					// **** END ******************************
				}																	//	
				rs.close(); 														// Close
//				System.out.println( "count:"+sosiki_codes.size() ); 				// *** DEBUG ***
					
				dbcom.DBDisconnect(connection); 									// Disconnect
				connection = null;													// Connection = null
			}catch(SQLException e){ 												// SQL Error
				System.out.println( "sosiki_List:SQLEception:" + e.getMessage() );	// *** ERR ***
				throw new Exception();												// throw Exception
			}																		// 
//			return sosiki_dt;														// Normal return
			return true;														// Normal return
		}catch(Exception e){														// catch
			System.out.println( "sosiki_List:Exception:" + e.getMessage() );				// *** ERR ***
//			return null;															// Abnormal return
			return false;															// Abnormal return
		}																			//
	}																				// *End of today_Get

	/**
	********************************************************************************
	* メソッド名		div_List
	* @param		Xysflj_Dbcom	dbcom
	* @param		String	sosiki_cd
	* @param		Vector	div_codes
	* @param		Vector	div_names
	* @return		boolean ( true:OK false:ERR )
	********************************************************************************
	*/
	public static boolean div_List( Xysflj_Dbcom	dbcom,
									String		sosiki_cd,
									Vector		div_codes,
									Vector		div_names)	 {						// *Start of div_List
		try{																		// try
			// *********************************************************************
			// *** 商品部組織を取得
			// *********************************************************************
			String sql	=	"SELECT UNEI_DIV, KEY_KOMOKU_NN"						// SELECT String
						+	"  FROM UY011M" 										//
						+	" WHERE DR = '000000'"									//
						+	" ORDER BY OUT_SEQ";									//
			div_codes.removeAllElements();
			div_names.removeAllElements();
			try{																	// try
//				if ( sosiki_cd != null ) {
//					if (!get_Connect_user(dbcom, sosiki_cd)) {						// 接続ユーザID/PASS取得
//						throw new Exception();										// throw Exception
//					}
//				}
//				System.out.println( "*** DB Connection Call ***" ); 				// *** DEBUG ***
				Connection connection = dbcom.DBConnect(DB_USER, DB_USER);			// DB connection
//				System.out.println( "*** SQL Quesry ***" ); 						// *** DEBUG ***
//				System.out.println( sql );											// *** DEBUG ***
				ResultSet rs = dbcom.querySQL(connection, sql); 					// Execute Query
				while(rs.next()){													// +++ rs.next +++
					div_codes.addElement(rs.getString("UNEI_DIV")); 				//	
					// **** E10K移行時変更対応(2006/06/08) ***
					div_names.addElement( Xysflj_Common.getStrCvt( rs.getString("KEY_KOMOKU_NN") ));	//	
//					div_names.addElement(rs.getString("KEY_KOMOKU_NN"));								//	
					// **** END ******************************
				}																	//	
				rs.close(); 														// Close
					
				dbcom.DBDisconnect(connection); 									// Disconnect
				connection = null;													// Connection = null
			}catch(SQLException e){ 												// SQL Error
//				System.out.println( "div_List:SQLEception:" + e.getMessage() ); // *** ERR ***
				throw new Exception();												// throw Exception
			}																		//	
			return true;															// Normal return
		}catch(Exception e){														// catch
//			System.out.println( "div_List:Exception:" + e.getMessage() );				// *** ERR ***
			return false;															// Abnormal return
		}																			//
	}																				// *End of today_Get

	/**
	********************************************************************************
	* メソッド名		drList
	* @param		Xysflj_Dbcom	dbcom
	* @param		String	sosiki_cd
	* @param		String	div_cd
	* @param		Vector	dr_codes
	* @param		Vector	dr_names
	* @return		boolean ( true:OK false:ERR )
	********************************************************************************
	*/
	public static boolean drList(	Xysflj_Dbcom	dbcom,
									String		sosiki_cd,
									String		div_cd,
									Vector		dr_codes,
									Vector		dr_names)	 {						// *Start of drList
		try{																		// try
			// *********************************************************************
			// *** 商品部組織を取得
			// *********************************************************************
			String	sql =	"SELECT DR,KEY_KOMOKU_NN"								// SELECT String
						+	"  FROM UY011M" 										//
						+	" WHERE UNEI_DIV = '" + div_cd +"'" 					//
						+	"  AND DR <> '000000'"									//
						+	" ORDER BY OUT_SEQ";									//
			dr_codes.removeAllElements();
			dr_names.removeAllElements();
			try{																	// try
//				if ( sosiki_cd != null ) {
//					if (!get_Connect_user(dbcom, sosiki_cd)) {						// 接続ユーザID/PASS取得
//						throw new Exception();										// throw Exception
//					}
//				}
//				System.out.println( "*** DB Connection Call ***" ); 				// *** DEBUG ***
				Connection connection = dbcom.DBConnect(DB_USER, DB_USER);			// DB connection
//				System.out.println( "*** SQL Quesry ***" ); 						// *** DEBUG ***
//				System.out.println( sql );											// *** DEBUG ***
				ResultSet rs = dbcom.querySQL(connection, sql); 					// Execute Query
				while(rs.next()){													// +++ rs.next +++
					dr_codes.addElement(rs.getString("DR"));						//	
					// **** E10K移行時変更対応(2006/06/08) ***
					dr_names.addElement( Xysflj_Common.getStrCvt( rs.getString("KEY_KOMOKU_NN") )); 			//	
//					dr_names.addElement(rs.getString("KEY_KOMOKU_NN")); 			//	
					// **** END ******************************
				}																	//	
				rs.close(); 														// Close
					
				dbcom.DBDisconnect(connection); 									// Disconnect
				connection = null;													// Connection = null
			}catch(SQLException e){ 												// SQL Error
//				System.out.println( "drList:SQLEception:" + e.getMessage() );		// *** ERR ***
				throw new Exception();												// throw Exception
			}																		//	
			return true;															// Normal return
		}catch(Exception e){														// catch
//			System.out.println( "drList:Exception:" + e.getMessage() ); 			// *** ERR ***
			return false;															// Abnormal return
		}																			//
	}																				// *End of today_Get

	/**
	********************************************************************************
	* メソッド名		svList
	* @param		Xysflj_Dbcom	dbcom
	* @param		String	sosiki_cd
	* @param		String	dr_cd
	* @param		Vector	sv_codes
	* @param		Vector	sv_names
	* @return		boolean ( true:OK false:ERR )
	********************************************************************************
	*/
	public static boolean svList(Xysflj_Dbcom dbcom,
							String	sosiki_cd,
							String	dr_cd,
							Vector	sv_codes,
							Vector	sv_names	)	{
		try{																		// try
			String sql	=	"SELECT KEY_KOMOKU_NN,SV"								// SELECT String
						+	"  FROM UY007M" 										//
						+	" WHERE DR = '" + dr_cd + "'"
						+	"   AND SV > '0000000'"
						+	"   AND TEN = '000000'"
						+	"   AND NOHINA = '000'"
						+	"  ORDER BY SV";

			// *************************************
			// * 読み込みデータをバッファに設定する
			// *************************************
			try{																	// try
//				if ( sosiki_cd != null ) {
//					if (!get_Connect_user(dbcom, sosiki_cd)) {						// 接続ユーザID/PASS取得
//						throw new Exception();										// throw Exception
//					}
//				}
//				System.out.println( "*** DB Connection Call ***" ); 				// *** DEBUG ***
				Connection connection = dbcom.DBConnect(DB_USER, DB_USER);			// DB connection
				// 検索データ件数取得
//				System.out.println( "*** SQL Quesry ***" ); 						// *** DEBUG ***
//				System.out.println( sql );											// *** DEBUG ***
				ResultSet rs = dbcom.querySQL(connection, sql); 					// Execute Query
				sv_codes.removeAllElements();
				sv_names.removeAllElements();
				while(rs.next()){													// +++ rs.next +++
					sv_codes.addElement(rs.getString("SV"));
					// **** E10K移行時変更対応(2006/06/08) ***
					sv_names.addElement( Xysflj_Common.getStrCvt( rs.getString("KEY_KOMOKU_NN") ));
//					sv_names.addElement(rs.getString("KEY_KOMOKU_NN"));
					// **** END ******************************
				}																	// +++ next End +++
				rs.close(); 														// Close

				dbcom.DBDisconnect(connection); 									// Disconnect
				connection = null;													// Connection = null
			}catch(SQLException e){ 												// SQL Error
				throw new Exception();												// throw Exception
			}

			return true;															// Normal return

		}catch(Exception e){														// catch
			return false;															// Abnormal return
		}																			//
	}

	/**
	********************************************************************************
	* メソッド名		tenList
	* @param		Xysflj_Dbcom	dbcom
	* @param		String	sosiki_cd
	* @param		String	sv_cd
	* @param		Vector	ten_codes
	* @param		Vector	ten_names
	* @return		boolean ( true:OK false:ERR )
	********************************************************************************
	*/
	public static boolean tenList(Xysflj_Dbcom dbcom,
							String	sosiki_cd,
							String	sv_cd,
							Vector	ten_codes,
							Vector	ten_names	)	{
		try{																		// try
			String sql	=	"SELECT KEY_KOMOKU_NN,TEN"								// SELECT String
						+	"  FROM UY007M" 										//
						+	" WHERE SV = '" + sv_cd + "'"
						+	"   AND TEN > '000000'"
						+	"  ORDER BY TEN";

			// *************************************
			// * 読み込みデータをバッファに設定する
			// *************************************
			try{																	// try
//				if ( sosiki_cd != null ) {
//					if (!get_Connect_user(dbcom, sosiki_cd)) {						// 接続ユーザID/PASS取得
//						throw new Exception();										// throw Exception
//					}
//				}
//				System.out.println( "*** DB Connection Call ***" ); 				// *** DEBUG ***
				Connection connection = dbcom.DBConnect(DB_USER, DB_USER);			// DB connection
				// 検索データ件数取得
//				System.out.println( "*** SQL Quesry ***" ); 						// *** DEBUG ***
//				System.out.println( sql );											// *** DEBUG ***
				ResultSet rs = dbcom.querySQL(connection, sql); 					// Execute Query
				ten_codes.removeAllElements();
				ten_names.removeAllElements();
				while(rs.next()){													// +++ rs.next +++
					ten_codes.addElement(rs.getString("TEN"));
					// **** E10K移行時変更対応(2006/06/08) ***
					ten_names.addElement( Xysflj_Common.getStrCvt( rs.getString("KEY_KOMOKU_NN") ));
//					ten_names.addElement(rs.getString("KEY_KOMOKU_NN"));
					// **** END ******************************
				}																	// +++ next End +++
				rs.close(); 														// Close

				dbcom.DBDisconnect(connection); 									// Disconnect
				connection = null;													// Connection = null
			}catch(SQLException e){ 												// SQL Error
				throw new Exception();												// throw Exception
			}

			return true;															// Normal return

		}catch(Exception e){														// catch
			return false;															// Abnormal return
		}																			//
	}

	/**
	********************************************************************************
	* メソッド名		kikakuList
	* @param		Xysflj_Dbcom	dbcom
	* @param		String	sosiki_cd
	* @param		String	user_sw
	* @param		String	sdate
	* @param		String	edate
	* @param		Vector	kikaku_codes
	* @param		Vector	kikaku_names
	* @return		boolean ( true:OK false:ERR )
	********************************************************************************
	*/
	public static boolean kikakuList(Xysflj_Dbcom dbcom,
									String	sosiki_cd,
									String	user_sw,
									String	sdate,
									String	edate,
									Vector	kikaku_codes,
									Vector	kikaku_names	)	{
		try{																		// try
			String sql	="";

			switch ((new Integer(user_sw)).intValue()) {
				case 0:
					sql = "SELECT A.KIKAKU, A.KIKAKU_NN"							// SELECT String
						+ " FROM tw005m A"											//
						+ " WHERE YUKO_SDATE <= '" + sdate + "'"					//
						+  " AND YUKO_EDATE >= '" + edate + "'" 					//
						+ " ORDER BY KIKAKU";										//
					break;
				case 1:
					sql = "SELECT A.KIKAKU, A.KIKAKU_NN"
						+  " FROM tw005m A"
						+  " WHERE A.YUKO_SDATE <= '" + sdate + "'"
						+	" AND A.YUKO_EDATE >= '" + edate + "'"
						+	" AND A.KIKAKU IN"
						+	 " (SELECT B.KIKAKU "
						+	   " FROM TW038T B"
						+		" WHERE B.EGY_DATE <= '" + sdate + "'"
						+		 " AND B.EGY_DATE >= '" + edate + "'"
						+		 " AND B.UNEI_DIV = '" + sosiki_cd +"'"
						+  " ORDER BY A.KIKAKU";
					break;
				case 2:
					sql = "SELECT A.KIKAKU, A.KIKAKU_NN"
						+  " FROM tw005m A"
						+  " WHERE A.YUKO_SDATE <= '" + sdate + "'"
						+	" AND A.YUKO_EDATE >= '" + edate + "'"
						+	" AND A.KIKAKU IN"
						+	 " (SELECT B.KIKAKU "
						+	   " FROM TW037T B"
						+		" WHERE B.EGY_DATE <= '" + sdate + "'"
						+		 " AND B.EGY_DATE >= '" + edate + "'"
						+		 " AND B.UNEI_DIV = '" + sosiki_cd +"'"
						+  " ORDER BY A.KIKAKU";
					break;
				default:
					System.out.println( "kikakuList:不正なuser_swです。");			// *** ERR ***
					throw new Exception();											// throw Exception
			}

			// *************************************
			// * 読み込みデータをバッファに設定する
			// *************************************
			try{																	// try
				if ( sosiki_cd != null ) {
					if (!get_Connect_user(dbcom, sosiki_cd)) {						// 接続ユーザID/PASS取得
						throw new Exception();										// throw Exception
					}
				}
//				System.out.println( "*** DB Connection Call ***" ); 				// *** DEBUG ***
				Connection connection = dbcom.DBConnect("uuv001", "uuv001");		// DB connection
				// 検索データ件数取得
//				System.out.println( "*** SQL Quesry ***" ); 						// *** DEBUG ***
//				System.out.println( sql );											// *** DEBUG ***
				ResultSet rs = dbcom.querySQL(connection, sql); 					// Execute Query
				kikaku_codes.removeAllElements();
				kikaku_names.removeAllElements();
				while(rs.next()){													// +++ rs.next +++
					kikaku_codes.addElement(rs.getString("A.KIKAKU"));
					// **** E10K移行時変更対応(2006/06/08) ***
					kikaku_names.addElement( Xysflj_Common.getStrCvt( rs.getString("A.KIKAKU_NN") ));
//					kikaku_names.addElement(rs.getString("A.KIKAKU_NN"));
					// **** END ******************************
				}																	// +++ next End +++
				rs.close(); 														// Close

				dbcom.DBDisconnect(connection); 									// Disconnect
				connection = null;													// Connection = null
			}catch(SQLException e){ 												// SQL Error
				throw new Exception("SQLException:"+ e.getMessage());				// throw Exception
			}

			return true;															// Normal return

		}catch(Exception e){														// catch
			System.out.println( "kikakuList:Exception:" + e.getMessage() ); 		// *** ERR ***
			return false;															// Abnormal return
		}																			//
	}

	/**
	********************************************************************************
	* メソッド名		kenshuList
	* @param		Xysflj_Dbcom	dbcom
	* @param		String	sosiki_cd
	* @param		String	kikaku_cd
	* @param		String	sdate
	* @param		String	edate
	* @param		Vector	kenshu_codes
	* @param		Vector	kenshu_names
	* @return		boolean ( true:OK false:ERR )
	********************************************************************************
	*/
	public static boolean kenshuList(Xysflj_Dbcom dbcom,
									String	sosiki_cd,
									String	kikaku_cd,
									String	sdate,
									String	edate,
									Vector	kenshu_codes,
									Vector	kenshu_names	)	{
		try{																		// try
			String sql	=	"SELECT COUPON_KENSY,COUPON_KENSY_NN"					// SELECT String
						+	 " FROM TW006M" 										//
						+	  " WHERE KIKAKU = '" + kikaku_cd +"'"					//
						+	  " ORDER BY COUPON_KENSY"; 							//

			// *************************************
			// * 読み込みデータをバッファに設定する
			// *************************************
			try{																	// try
				if ( sosiki_cd != null ) {
					if (!get_Connect_user(dbcom, sosiki_cd)) {						// 接続ユーザID/PASS取得
						throw new Exception();										// throw Exception
					}
				}
//				System.out.println( "*** DB Connection Call ***" ); 				// *** DEBUG ***
				Connection connection = dbcom.DBConnect("uuv001", "uuv001");		// DB connection
				// 検索データ件数取得
//				System.out.println( "*** SQL Quesry ***" ); 						// *** DEBUG ***
//				System.out.println( sql );											// *** DEBUG ***
				ResultSet rs = dbcom.querySQL(connection, sql); 					// Execute Query
				kenshu_codes.removeAllElements();
				kenshu_names.removeAllElements();
				while(rs.next()){													// +++ rs.next +++
					kenshu_codes.addElement(rs.getString("COUPON_KENSY"));
					// **** E10K移行時変更対応(2006/06/08) ***
					kenshu_names.addElement( Xysflj_Common.getStrCvt( rs.getString("COUPON_KENSY_NN") ));
//					kenshu_names.addElement(rs.getString("COUPON_KENSY_NN"));
					// **** END ******************************
				}																	// +++ next End +++
				rs.close(); 														// Close

				dbcom.DBDisconnect(connection); 									// Disconnect
				connection = null;													// Connection = null
			}catch(SQLException e){ 												// SQL Error
				throw new Exception("SQLException:"+ e.getMessage());				// throw Exception
			}

			return true;															// Normal return

		}catch(Exception e){														// catch
			System.out.println( "kenshuList:Exception:" + e.getMessage() ); 		// *** ERR ***
			return false;															// Abnormal return
		}																			//
	}

	/**
	********************************************************************************
	* メソッド名		kinshuList
	* @param		Xysflj_Dbcom	dbcom
	* @param		String	sosiki_cd
	* @param		String	kenshu_cd
	* @param		Vector	kinshu_codes
	* @param		Vector	kinshu_names
	* @return		boolean ( true:OK false:ERR )
	********************************************************************************
	*/
	public static boolean kinshuList(Xysflj_Dbcom dbcom,
									String	sosiki_cd,
									String	kenshu_cd,
									Vector	kinshu_codes,
									Vector	kinshu_names	)	{
		try{																		// try
			String sql	=	"SELECT A.COUPON_KINSY,A.COUPON_KINSY_NN"				// SELECT String
						+	 " FROM TW007M A,TW006M B"								//
						+	  " WHERE B.COUPON_KENSY = = '" + kenshu_cd +"'"		//
						+	   " AND B.COUPON_KINSY = A.COUPON_KINSY"
						+	 " ORDER BY A.COUPON_KINSY";							//

			// *************************************
			// * 読み込みデータをバッファに設定する
			// *************************************
			try{																	// try
				if ( sosiki_cd != null ) {
					if (!get_Connect_user(dbcom, sosiki_cd)) {						// 接続ユーザID/PASS取得
						throw new Exception();										// throw Exception
					}
				}
//				System.out.println( "*** DB Connection Call ***" ); 				// *** DEBUG ***
				Connection connection = dbcom.DBConnect("uuv001", "uuv001");		// DB connection
				// 検索データ件数取得
//				System.out.println( "*** SQL Quesry ***" ); 						// *** DEBUG ***
//				System.out.println( sql );											// *** DEBUG ***
				ResultSet rs = dbcom.querySQL(connection, sql); 					// Execute Query
				kinshu_codes.removeAllElements();
				kinshu_names.removeAllElements();
				while(rs.next()){													// +++ rs.next +++
					kinshu_codes.addElement(rs.getString("COUPON_KINSY"));
					// **** E10K移行時変更対応(2006/06/08) ***
					kinshu_names.addElement( Xysflj_Common.getStrCvt( rs.getString("COUPON_KINSY_NN") ));
//					kinshu_names.addElement(rs.getString("COUPON_KINSY_NN"));
					// **** END ******************************
				}																	// +++ next End +++
				rs.close(); 														// Close

				dbcom.DBDisconnect(connection); 									// Disconnect
				connection = null;													// Connection = null
			}catch(SQLException e){ 												// SQL Error
				throw new Exception("SQLException:"+ e.getMessage());				// throw Exception
			}

			return true;															// Normal return

		}catch(Exception e){														// catch
			System.out.println( "kinshuList:Exception:" + e.getMessage() ); 		// *** ERR ***
			return false;															// Abnormal return
		}																			//
	}

	/**
	********************************************************************************
	* メソッド名		getYoubiMoji
	* @param		String	youbi_cd
	* @return		String ( Not "":OK "":ERR )
	********************************************************************************
	*/
	public static String getYoubiMoji( String youbi_cd )	 {						// *Start of youbi_Moji
		try{																		// try
			if ( youbi_cd == null || youbi_cd.equals("") ) {
				return "";
			}

			switch ((new Integer(youbi_cd)).intValue()) {
				case 1:
					return "月";
				case 2:
					return "火";
				case 3:
					return "水";
				case 4:
					return "木";
				case 5:
					return "金";
				case 6:
					return "土";
				case 7:
					return "日";
			}
			return "";
		}catch(Exception e){														// catch
//			System.out.println( "getYoubiMoji:Exception:" + e.getMessage() );		// *** ERR ***
			return "";																// Abnormal return
		}																			//
	}																				// *End of youbi_Moji

	/**
	********************************************************************************
	* メソッド名		getTodate
	* @param		String	ymd_data(YYYYMMDD)
	* @param		int 	keika_nissu
	* @return		String ( Not "":OK "":ERR )
	********************************************************************************
	*/
	public static String getTodate( String ymd_data,
										int keika_nissu )	 {						// *Start of getTodate
		try{
			String ret_str = "";																		// try
			if ( ymd_data == null || ymd_data.equals("") ) {
				return "";
			}

			Calendar cal = ymd_Format(2, ymd_data);

			cal.add(Calendar.DATE, keika_nissu);

			Integer wk_year = new Integer(cal.get(Calendar.YEAR));	
			Integer wk_month = new Integer(cal.get(Calendar.MONTH) + 1);	
			Integer wk_day = new Integer(cal.get(Calendar.DAY_OF_MONTH));

			ret_str = wk_year.toString();
			if ( wk_month.intValue() < 10 ) {
				ret_str += "0";
			}
			ret_str += wk_month.toString();
			if ( wk_day.intValue() < 10 ) {
				ret_str += "0";
			}
			ret_str += wk_day.toString();
	
			return ret_str;

		}catch(Exception e){														// catch
//			System.out.println( "getTodate:Exception:" + e.getMessage() );			// *** ERR ***
			return "";																// Abnormal return
		}																			//
	}																				// *End of getTodate

	/**
	********************************************************************************
	* メソッド名		ymd_Print
	* @param		String	ymd_data(yyyymmdd or yyyymm)	
	* @return		String ( Not "":OK "":ERR )
	********************************************************************************
	*/
	public static String ymd_Print( int format_sw,
									String ymd_data )	 {							// *Start of ymd_print
		try{																		// try
			if ( ymd_data == null || ymd_data.equals("") ) {
				return "";
			}

			if ( ymd_data.length() == 8 ) {
				//YYYYMMDD
				switch ( format_sw ) {
					case 1:
						return ymd_data.substring(0,4) + "/"
							 + ymd_data.substring(4,6) + "/"
							 + ymd_data.substring(6,8);
					case 2:
						return ymd_data.substring(0,4) + "年"
							 + ymd_data.substring(4,6) + "月"
							 + ymd_data.substring(6,8);
					case 3:
						return ymd_data.substring(4,6) + "月"
							 + ymd_data.substring(6,8) + "日";
					case 4:
						return ymd_data.substring(4,6) + "/"
							 + ymd_data.substring(6,8);
				}
			} else {
				//YYYYMMDD
				switch ( format_sw ) {
 					case 1:
						return ymd_data.substring(0,4) + "/"
							 + ymd_data.substring(4,6);
					case 2:
						return ymd_data.substring(0,4) + "年"
							 + ymd_data.substring(4,6) + "月";
//					case 3:
				}
			}

			return "";																// Abnormal return

		}catch(Exception e){														// catch
//			System.out.println( "ymd_print:Exception:" + e.getMessage() );			// *** ERR ***
			return "";																// Abnormal return
		}																			//
	}																				// *End of ymd_print

	/**
	********************************************************************************
	* メソッド名		ymd_GetYoubi
	* @param		String	yyyymmdd
	* @return		int ( Not 0:OK 0:ERR )
	********************************************************************************
	*/
	public static int ymd_GetYoubi( String ymd_data )	 {							// *Start of ymd_print
		try{																		// try
			if ( ymd_data == null || ymd_data.equals("") ) {
				return 0;
			}

			Calendar cal;															//

			if ( ymd_data.length() == 8 ) {
				cal = ymd_Format(2, ymd_data);
			} else {
				cal = ymd_Format(1, ymd_data);
			}

			int youbi = cal.get(Calendar.DAY_OF_WEEK);

			if (youbi == Calendar.MONDAY)		return 1;
			if (youbi == Calendar.TUESDAY)		return 2;
			if (youbi == Calendar.WEDNESDAY)	return 3;
			if (youbi == Calendar.THURSDAY) 	return 4;
			if (youbi == Calendar.FRIDAY)		return 5;
			if (youbi == Calendar.SATURDAY) 	return 6;
			if (youbi == Calendar.SUNDAY)		return 7;

			return 0;																// Abnormal return

		}catch(Exception e){														// catch
			System.out.println( "ymd_GetYoubi:Exception:" + e.getMessage() );			// *** ERR ***
			return 0;																// Abnormal return
		}																			//
	}																				// *End of ymd_print

	/**
	********************************************************************************
	* メソッド名		getDivFromDr
	* @param		Xysflj_Dbcom	dbcom
	* @param		String	sosiki_code
	* @return		String ( Not null:OK null:ERR )
	********************************************************************************
	*/
	public static String getDivFromDr(	Xysflj_Dbcom	dbcom,
										String		sosiki_cd,
										String		dr_code 	) { 				// *Start of sosiki_Chk
		try{																		// try
			String	sql = "SELECT DR,UNEI_DIV"										// SELECT String
						+ " FROM UY007M"
						+ " WHERE DR='" + dr_code + "'"
						+ "  AND SV='0000000'"
						+ "  AND NOHINA='000'"
						+ "  AND TEN='000000'"
						+ "  AND SYORI_KBN < '7'";
			String div_code =	"";
			try{																	// try
				if ( sosiki_cd != null ) {
					if (!get_Connect_user(dbcom, sosiki_cd)) {						// 接続ユーザID/PASS取得
						throw new Exception();										// throw Exception
					}
				}
				Connection connection = dbcom.DBConnect(DB_USER, DB_USER);			// DB connection
//				System.out.println( "*** SQL Quesry ***" ); 						// *** DEBUG ***

//				System.out.println( sql );											// *** DEBUG ***
				ResultSet rs = dbcom.querySQL(connection, sql); 					// Execute Query
				if(rs.next()){														// +++ rs.next +++
					div_code = rs.getString("UNEI_DIV");
				}
				rs.close(); 														// Close

				dbcom.DBDisconnect(connection); 									// Disconnect
				connection = null;													// Connection = null
			}catch(SQLException e){ 												// SQL Error
				System.out.println( "getDivFromDr:SQLEception:" + e.getMessage() );// *** ERR ***
				throw new Exception();												// throw Exception
			}																		//	

//			System.out.println( "getDivFromDr:div_code:" + div_code );
			return div_code;														// Normal return
		}catch(Exception e){														// catch
			System.out.println( "getDivFromDr:Exception:" + e.getMessage() );		// *** ERR ***
			return "";																// Abnormal return
		}																			//
	}																				// *End of sosiki_Chk

	/**
	********************************************************************************
	* メソッド名		tenName
	* @param		Xysflj_Dbcom	dbcom
	* @param		String	sosiki_cd
	* @param		String	ten_cd
	* @return		String ( Not null:OK null:ERR )
	********************************************************************************
	*/
	public static String tenName(Xysflj_Dbcom dbcom,
							String	sosiki_cd,
							String	ten_cd) {
		try{																		// try
			String sql	=	"SELECT KEY_KOMOKU_NN"									// SELECT String
						+	"  FROM UY007M" 										//
						+	" WHERE TEN = '" + ten_cd + "'"
						+	"  ORDER BY TEN";
			String ten_nm = "";

			// *************************************
			// * 読み込みデータをバッファに設定する
			// *************************************
			try{																	// try
				if ( sosiki_cd != null ) {
					if (!get_Connect_user(dbcom, sosiki_cd)) {						// 接続ユーザID/PASS取得
						throw new Exception();										// throw Exception
					}
				}
//				System.out.println( "*** DB Connection Call ***" ); 				// *** DEBUG ***
				Connection connection = dbcom.DBConnect(DB_USER, DB_USER);			// DB connection
				// 検索データ件数取得
//				System.out.println( "*** SQL Quesry ***" ); 						// *** DEBUG ***
//				System.out.println( sql );											// *** DEBUG ***
				ResultSet rs = dbcom.querySQL(connection, sql); 					// Execute Query
				if ( rs.next() ) {
					// **** E10K移行時変更対応(2006/06/08) ***
					ten_nm = Xysflj_Common.getStrCvt( rs.getString("KEY_KOMOKU_NN") );
//					ten_nm = rs.getString("KEY_KOMOKU_NN");
					// **** END ******************************
				}																	// +++ next End +++
				rs.close(); 														// Close

				dbcom.DBDisconnect(connection); 									// Disconnect
				connection = null;													// Connection = null
			}catch(SQLException e){ 												// SQL Error
				System.out.println( "tenName:SQLEception:" + e.getMessage() );		// *** ERR ***
				throw new Exception();												// throw Exception
			}

			return ten_nm;															// Normal return

		}catch(Exception e){														// catch
			return null;															// Abnormal return
		}																			//
	}

	/**
	********************************************************************************
	* メソッド名		csvDownload
	* @param		HttpServletResponse dbcom
	* @param		String	fileName
	* @param		Vector	csvData
	* @param		String	kaigyo_cd
	* @return		boolean ( true:OK false:ERR )
	********************************************************************************
	*/
	public static boolean csvDownload(HttpServletResponse response,
							String	fileName,
							Vector	csvData,
							String	kaigyo_cd) {
		try{																		// try

//			response.setContentType("application/octet-stream;charset=Shift_JIS");
			response.setContentType("application/data-download;charset=Shift_JIS");

//			response.setHeader("Content-Disposition","attachmentl; filename=" + fileName);
			response.setHeader("Content-Disposition","filename=" + fileName);

			PrintWriter out=response.getWriter();

			Enumeration data = csvData.elements();
			while(data.hasMoreElements()){
				out.print( Xysflj_Common.getStrCvt((String)data.nextElement()) + kaigyo_cd );
			}
			out.close();

			return true;															// Normal return

		}catch(Exception e){														// catch
			return false;															// Abnormal return
		}																			//
	}

	/**
	********************************************************************************
	* メソッド名		getString	文字列の指定文字分切り出し
	* @param		String	in_data
	* @param		int		out_length
	* @return		String ( Not "":OK "":ERR )
	********************************************************************************
	*/
	public static String getString(	String			in_data,
									int				out_length) {
		try{																		// try
			String out_data = "";

			out_data = in_data;
			if ( in_data.length() > out_length ) {
				out_data = in_data.substring(0, out_length);
			}

			return out_data;														// Normal return

		}catch(Exception e){														// catch
			return "";																// Abnormal return
		}																			//
	}
	/**
	********************************************************************************
	* メソッド名		getStrCvt	文字列化け対応（〜、‖、−）
	* @param		String	in_data
	* @return		String ( Not "":OK "":ERR )
	********************************************************************************
	*/
/*	public static String getStrCvt(	String			in_data) {
		try{																		// try
			String out_data = "";

			if ( CON_SW.equals("2") ) {												// Tomcat
				out_data = in_data.replace('〜', '\uff5e') ;
				out_data = out_data.replace('‖', '\u2225') ;
				out_data = out_data.replace('−', '\uff0d') ;
			} else {																// iAS
				out_data = in_data.replace('〜', '\u301c') ;
				out_data = out_data.replace('‖', '\u2016') ;
				out_data = out_data.replace('−', '\u2212') ;
			}
			return out_data;														// Normal return

		}catch(Exception e){														// catch
			return "";																// Abnormal return
		}																			//
	}
*/
//	*********** E10K移行時追加(2006/04/26) *****************************************
	/**
	********************************************************************************
	* メソッド名	getStrCvt					文字列化け対応（〜、‖、−）
	* @param		String						in_data
	* @return		String						( Not "":OK "":ERR )
	********************************************************************************
	*/
	public static String getStrCvt(	String			in_data) {
		try{																		// try
			String out_data = "";
			if( in_data == null ){
				return "";
			}
			if ( !CON_SW.equals("2") ) {
				out_data = in_data.replace('\u301c', WAVE_DASH.charAt(0));
				out_data = out_data.replace('\uff5e', WAVE_DASH.charAt(0));
				out_data = out_data.replace('\u2225', DOUBLE_VERTICAL.charAt(0));
				out_data = out_data.replace('\uff0d', MINUS_SIGN.charAt(0));
				out_data = out_data.replace('\u2015', EM_DASH.charAt(0));
				return out_data;														// Normal return
			}
			out_data = in_data.replace('\u301c', WAVE_DASH.charAt(0));
			out_data = out_data.replace('\u2016', DOUBLE_VERTICAL.charAt(0));
			out_data = out_data.replace('\u2212', MINUS_SIGN.charAt(0));
			out_data = out_data.replace('〜', WAVE_DASH.charAt(0));
			out_data = out_data.replace('‖', DOUBLE_VERTICAL.charAt(0));
			out_data = out_data.replace('−', MINUS_SIGN.charAt(0));
			out_data = out_data.replace('―', EM_DASH.charAt(0));
			return out_data;														// Normal return

		}catch(Exception e){														// catch
			return "";																// Abnormal return
		}																			//
	}	// ----------------------- End of Method -----------------------------------
}																					// * End of Common Class *
