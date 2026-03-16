/**
*--------------------------------------------------------------------------------
* クラス名		Xysflj_Dbcom.class
* システム　　名称	ＬＥＴＳＳ店舗開発システム
* 名称			データベース・共通クラス
* 会社名or所属名	ＣＶＳシステム事業部
* 作成日		2003/06/10 00:00:00
* @author		A.Konishi
* @since		1.0
* @version		1.1
* *** 修正履歴 ***
* No.  Date 	   Aouther		Description
* 01   2006/06/12  O.Ogawara	ログ処理時のconnection close処理追加
*--------------------------------------------------------------------------------
*/
package xysk;
import java.io.*;
import java.sql.*;
import javax.sql.*;
import javax.naming.InitialContext;
import java.util.Calendar;
import java.util.TimeZone;
import xysk.Xysflj_Common;

public class Xysflj_Dbcom extends Object{
	// **************************************************************************
	// * **** 重要事項 ****		**** This Class is Not Change ! ****			*
	// * ・ Application Server 環境の切り替えは、con_sw の値で行う。			*
	// * （ Xysflj_Common 側から全制御されますから、一切の変更は不要です）		*
	// *	con_sw	1:JNDI (iAS)	2:PrimalyPacket (Tomcat)	3:J2EE (J2SDKEE)*
	// * ・iAS 設定																*
	// *	JNDI Data Source 登録は、iAS マシン上で（redReg -[File.xml]）		*
	// *	を実行して、detasourceのプロパティを登録してください。				*
	// * ・J2SDKEE 設定															*
	// *	DataSource名 の変更は、J2EEエンジンconfig配下の						*
	// *	default.propertiesを変更して下さい。								*
	// * 【注意】																*
	// * ・改造する場合のDEBUG時には、msg_boxの使用はしないほうが良いでしょう。	*
	// * System.out.println のログでDEBUGすれば基本構造を変更しないで済みます。	*
	// **************************************************************************
	static	private	String	drivername	=	Xysflj_Common.DRIVER_NAME;				// Driver For Tomcat
	Statement	Stmt	= null;														// SQL Statement
	ResultSet	Rs	= null;															// SQL ResultSet
	String		con_sw	= Xysflj_Common.CON_SW;										// DB Connection Switch
	/**
	********************************************************************************
	* メソッド名		DBConnect		DB Connection Process

	* @return		con(DB Connection Object)
	********************************************************************************
	*/
	public Connection DBConnect(String DB_User, String DB_Password)
													 throws SQLException {			// *** DBConnect Method ***
		String		dsName	= Xysflj_Common.JNDI_NAME;								// DATA Source JNDI Name
		InitialContext	ctx	= null;													// Initial Context
		DataSource		ds	= null;													// Data Source For JNDI
		Connection		con	= null;													// RDB Connection
		if(con_sw.equals("1")){														// ------ iAS ? ------
			try{																	// try
				if ( Xysflj_Common.DEBUG_SW.equals("1") ) {
					System.out.println( "Dbcom.class:iAS Connect dsName:"+dsName );
				}
				ctx	= new InitialContext();											// Initial Context new Instance
				ds	= (DataSource)ctx.lookup(dsName);								// Data Source Lookup
				con	= ds.getConnection();											// DataBase Connection
				con.setAutoCommit(false);											// Auto Commit False
			} catch (javax.naming.NamingException e) {								// catch NamingException
				con	=	null;														// Connection null clear
				throw new SQLException("Dbcom.class:DataSource not found! " + dsName );
		} catch (Exception ex){														// catch Exception
				con	=	null;														// Connection null clear
				throw new SQLException("Dbcom.class:" + ex.toString());				// *** throw SQLException ***
		}																			//	
		} else if (con_sw.equals("3")) {											// ------ J2SDKEE ? ------
			try{																	// try
				ctx				= new InitialContext();								// Initial Context new Instance
				ds				= (DataSource)ctx.lookup(dsName);					// Data Source For J2SDKEE
				con				= ds.getConnection(DB_User, DB_Password);			// DataBase Connection
				con.setAutoCommit(false);											// Auto Commit False
			} catch (javax.naming.NamingException e) {								// catch NamingException
				throw new SQLException("Dbcom.class:DataSource not found!");		// *** throw SQLException ***
			}																		//	
		} else if (con_sw.equals("2")) {											// ----- TOMCAT ? -----
			String	urlString	= Xysflj_Common.THIN_DRIVER_URL;					// Thin Driver Name
			try{																	// try
				Class.forName(drivername);											// JDBC Driver Loading
				con = DriverManager.getConnection(urlString, DB_User, DB_Password);			// DataBase Connection
				con.setAutoCommit(false);											// Auto Commit False
			} catch (ClassNotFoundException e){										// catch ClassNotFoundException
				con = null;															// Connection null clear
				throw new SQLException("Dbcom.class:JDBC Driver not found!");		// *** throw SQLException ***
			} catch (Exception ex) {												// catch Exception
				con = null;															// Connection null clear
				throw new SQLException("Dbcom.class:" + ex.toString());				// *** throw SQLException ***
			}																		//	
		}																			// **************
		return con;																	// *** Return ***
	}																				// **************
	/**
	********************************************************************************
	* メソッド名		DBDisconnect		DB切断
	* @param		Connection	con(DB 接続 Object)
	* @return		nothing
	********************************************************************************
	*/
	public void DBDisconnect(Connection con)
		throws SQLException {
		con.close();
		if ( Xysflj_Common.DEBUG_SW.equals("1") ) {
			System.out.println( "Dbcom.class:DB Disconnect");
		}
		con = null;
	}
	/**
	********************************************************************************
	* メソッド名		querySQL		クエリーＳＱＬ実行
	* @param		Connection	con（ DB 接続 Object ）
	* @param		String		sql
	* @return		Rs	 ( SQLの実行結果 )
	********************************************************************************
	*/
	public ResultSet querySQL(Connection con, String sql)
		throws SQLException {
		Stmt = con.createStatement();
		Rs	= Stmt.executeQuery(sql);
		return Rs;
	}
	/**
	********************************************************************************
	* メソッド名		updateSQL		更新系ＳＱＬ実行
	* @param		Connection	con（ DB 接続 Object ）
	* @param		String		sql
	* @return		nothing
	********************************************************************************
	*/
	public void updateSQL(Connection con, String sql)
		throws SQLException {
		Statement stmt = null;
		stmt = con.createStatement();
		stmt.executeUpdate(sql);
		stmt.close();
		stmt = null;
	}
	/**
	********************************************************************************
	* メソッド名		DBCommit		Transaction Commit
	* @param		Connection	con（ DB 接続 Object ）
	* @return		nothing
	********************************************************************************
	*/
	public void DBCommit(Connection con)
		throws SQLException {
		con.commit();
	}
	/**
	********************************************************************************
	* メソッド名		DBRollback		Transaction RollBack
	* @param		Connection	con（ DB 接続 Object ）
	* @return		nothing
	********************************************************************************
	*/
	public void DBRollback(Connection con)
		throws SQLException {
		con.rollback();
	}
	/**
	********************************************************************************
	* メソッド名		SQLExceptionPrint		ＪＤＢＣ実行例外メッセージ出力
	* @param		SQLException	ex(SQLExceptionのオブジェクト参照変数)
	* @param		String		funcname
	* @return		nothing
	********************************************************************************
	*/
	public void SQLExceptionPrint(SQLException ex, String funcname){
		System.out.println("["+funcname+"] SQL Status : ["+ex.getSQLState()+"], ["+ex.toString()+"]");
	}
	/**
	********************************************************************************
	* メソッド名		ExceptionPrint		例外発生メッセージ出力
	* @param		Exception	ex(SQLExceptionのオブジェクト参照変数)
	* @return		nothing
	********************************************************************************
	*/
	public void ExceptionPrint(Exception e){
		System.out.println(e.toString());
	}

	/**
	********************************************************************************
	* メソッド名		Log_out		ログデータをログテーブルへ出力する。
	* @param		Connection	con（ DB 接続 Object ）
	* @param		String		org_cd(組織コード)
	* @param		String		usr_nm_cd(氏名コード)
	* @param		String		msi_scrn_id(ＭＳＩ画面ＩＤ）S0S1+B1B2+３桁連番
	* @param		String		msi_func_id(ＭＳＩ機能ＩＤ) 1:検索 2:ﾀﾞｳﾝﾛｰﾄﾞ 3:印刷 4:登録 5:更新 6:削除
	* @return		nothing
	********************************************************************************
	*/
/*
	public void Log_out(Connection	con,
						String		org_cd,
						String		usr_nm_cd,
						String		msi_scrn_id,
						String		msi_func_id)
													throws SQLException {			// *** DBConnect Method **
		String bak_THIN_DRIVER_URL = "";

		try {
			if ( Xysflj_Common.DEBUG_SW.equals("1") ) {
				System.out.println("*********************************************************");
				System.out.println("* Log_out:組織=" + org_cd + " 氏名=" + usr_nm_cd + " 画面=" + msi_scrn_id + " 機能=" + msi_func_id);
				System.out.println("*********************************************************");
			}

			int gmt = 9;
			int yyyy, mm, dd, hh, ms, ss;
			String	yyyymmdd, st_yyyy, st_mm, st_dd, hhmmss, st_hh, st_ms, st_ss;
			String	tz[]=TimeZone.getAvailableIDs(gmt * 60 * 60 * 1000);
			Calendar d=Calendar.getInstance(TimeZone.getTimeZone(tz[0]));
			String sql;

			try {
//				if (con_sw.equals("2")) {											// ----- TOMCAT ? -----
//					bak_THIN_DRIVER_URL = Xysflj_Common.THIN_DRIVER_URL;
//					Xysflj_Common.THIN_DRIVER_URL =
//										"jdbc:oracle:thin:@10.173.6.107:1521:ORCL";	// IAS_NT		
//					con = DBConnect("otg001", "otg001");
//				} else { // IAS
					con = DBConnect("hxy022", "hxy022");
//				}

				ResultSet rs = null;
				while ( true ) {
					yyyy	= d.get(d.YEAR); st_yyyy = "";st_yyyy += yyyy;
					mm	= d.get(d.MONTH)+1; st_mm = "";st_mm += mm;
					dd	= d.get(d.DATE); st_dd = "";st_dd += dd;
					if( yyyy < 1000 ){ st_yyyy = "0" + yyyy; }
					if( yyyy < 100	){ st_yyyy = "00" + yyyy; }
					if( yyyy < 10	){ st_yyyy = "000" + yyyy; }
					if( mm < 10 ){ st_mm = "0" + mm; }
					if( dd < 10 ){ st_dd = "0" + dd; }
					yyyymmdd = st_yyyy + st_mm + st_dd;

					hh	= d.get(d.HOUR_OF_DAY); st_hh = "";
					st_hh += hh;
					ms	= d.get(d.MINUTE); st_ms = "";st_ms += ms;
					ss	= d.get(d.SECOND); st_ss = "";st_ss += ss;
					if( hh < 10 ){ st_hh = "0" + hh; }
					if( ms < 10 ){ st_ms = "0" + ms; }
					if( ss < 10 ){ st_ss = "0" + ss; }
					hhmmss = st_hh + st_ms + st_ss;

					sql = "SELECT CLNDR_DT"
						+	" FROM WT010M "
						+		" WHERE CLNDR_DT = "		+ "'" + yyyymmdd + "'"
						+			" AND PROC_HOUR = " 	+ "'" + hhmmss + "'"
						+			" AND ORG_CD = "		+ "'" + org_cd + "'"
						+			" AND USR_NM_CD = "		+ "'" + usr_nm_cd + "'"
						+			" AND MSI_SCRN_ID = "	+ "'" + msi_scrn_id + "'";

					rs = querySQL(con, sql);
					if ( !rs.next() ) {
						break;
					}
					d.add(d.SECOND, 1);
				}
				rs.close();

				sql = "INSERT INTO WT010M ("
							+	"CLNDR_DT,"
							+	"PROC_HOUR,"
							+	"ORG_CD,"
							+	"USR_NM_CD,"
							+	"MSI_SCRN_ID,"
							+	"MSI_FUNC_ID )"
							+	" VALUES ("
							+	"'"+ yyyymmdd + "',"
							+	"'"+ hhmmss + "',"
							+	"'"+ org_cd + "',"
							+	"'"+ usr_nm_cd + "',"
							+	"'"+ msi_scrn_id + "',"
							+	"'"+ msi_func_id + "')";

//				System.out.println(sql);

				Statement stmt = con.createStatement();
				stmt.executeUpdate(sql);
				stmt.close();

				con.commit();

				DBDisconnect(con);													// DB Disconnect

//				if (con_sw.equals("2")) {											// ----- TOMCAT ? -----
//					Xysflj_Common.THIN_DRIVER_URL = bak_THIN_DRIVER_URL;
//				}

			}catch(SQLException e){ 												// SQL Error
				throw new Exception("SQLException:" + e.getMessage());				// throw Exception
			}																		// 

		} catch (Exception ex) {													// catch Exception
			throw new SQLException("Dbcom.Log_out:" + ex.toString()); 				// *** throw SQLException ***
		}																			//	
	}
*/
//	*********** E10K移行時追加(2006/05/12) *****************************************
	/**
	********************************************************************************
	* メソッド名	printLog	ログ出力前準備
	* @param		String		layoutId (レイアウトID)
	* @param		String		objectNm (機能対象物名称) 照会画面名称、印刷画面名称、ﾀﾞｳﾝﾛｰﾄﾞ名称
	* @param		String		funcId   (機能ID) 1:検索 2:ﾀﾞｳﾝﾛｰﾄﾞ 3:印刷4:登録 5:更新 6:削除
	* @param		String		usrCd    (氏名コード)
	* @param		String		sosikiCd (組織コード)
	* @param		String		syokuiCd (職位コード)
	* @return		なし
	********************************************************************************
	*/
	public void printLog(String layoutId, String scrnNm, String objectNm, String funcId,
						 String usrCd, String sosikiCd, String syokuiCd) {
		try {
			// *** メッセージの生成 ***
			String msgId = "";
			String msg = "";
			int FUNC = Integer.parseInt( funcId );
			switch( FUNC ){
				case 1:
					msgId = "WQGDWM0001I";						// リテラル値
					msg = "画面「" + objectNm + "(検索)」を閲覧";
					break;
				case 2:
					msgId = "WQGDWM0009I";						// リテラル値
					msg = "ファイル「" + objectNm + "」をダウンロード";
					break;
				case 3:
					msgId = "WQGDWM0006I";						// リテラル値
					msg = "帳票「" + objectNm + "」を発行";
					break;
				case 4:
					msgId = "WQGDWM0001I";						// リテラル値
					msg = "画面「" + objectNm + "(登録)」を閲覧";
					break;
				case 5:
					msgId = "WQGDWM0001I";						// リテラル値
					msg = "画面「" + objectNm + "(更新)」を閲覧";
					break;
				case 6:
					msgId = "WQGDWM0001I";						// リテラル値
					msg = "画面「" + objectNm + "(削除)」を閲覧";
					break;
				default:
			}
			// ログの出力
			Log_out(	  scrnNm,							// 画面名
						  layoutId,							// レイアウトID
						  msgId,							// メッセージID
						  msg,								// メッセージ
						  usrCd,							// 氏名コード
						  sosikiCd,							// 組織コード
						  syokuiCd);						// 職位コード

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
		}
	}
//	*********** E10K移行時追加(2006/05/12) *****************************************
	/**
	********************************************************************************
	* メソッド名		Log_out		ログデータをログテーブルへ出力する。
	* @param		Connection	con（ DB 接続 Object ）
	* @param		String		scrnNm   (画面名称)
	* @param		String		scrnId   (レイアウトＩＤ) S0S1+B1B2+３桁連番
	* @param		String		msgId    (メッセージＩＤ)
	* @param		String		msg      (メッセージ)
	* @param		String		usrCd    (氏名コード)
	* @param		String		sosikiCd (組織コード)
	* @param		String		syokuiCd (職位コード)
	* @param		String		errSyu   (エラー種別) ACCESS固定
	* @return		なし
	********************************************************************************
	*/
	public void Log_out(	String		scrnNm,
							String		layoutId,
							String		msgId,
							String		msg,
							String		usrCd,
							String		sosikiCd,
							String		syokuiCd	) throws SQLException {
//	*********** E10K移行時追加(2006/06/12) 04 **************************************
		Connection con = null;
		try {
			int gmt = 9;
			String now;
			String	tz[]=TimeZone.getAvailableIDs(gmt * 60 * 60 * 1000);
			Calendar d=Calendar.getInstance(TimeZone.getTimeZone(tz[0]));
			String sql = "";
			try {
				con = DBConnect(Xysflj_Common.DB_USER, Xysflj_Common.DB_PASS);
				ResultSet rs = null;
				now = getYMDString( d );
				sql = "INSERT INTO WT010M ("
							+	"出力日時,"
							+	"画面名,"
							+	"レイアウトＩＤ,"
							+	"メッセージＩＤ,"
							+	"メッセージ,"
							+	"ユーザーＩＤ,"
							+	"組織コード,"
							+	"職位コード,"
							+	"エラー種別 )"
							+	" VALUES ("
							+	"TO_DATE('" + now + "','YYYY/MM/DD HH24:MI:SS'),"
							+	"'"+ scrnNm + "',"
							+	"'"+ layoutId + "',"
							+	"'"+ msgId + "',"
							+	"'"+ msg + "',"
							+	"'"+ usrCd + "',"
							+	"'"+ sosikiCd + "',"
							+	"'"+ syokuiCd + "',"
							+	"'ACCESS')";

				if ( Xysflj_Common.DEBUG_SW.equals("1") ) {
					System.out.println("*** ログTBLへの出力SQL *** \n" + sql);
				}
				Statement stmt = con.createStatement();
				stmt.executeUpdate(sql);
				stmt.close();
				con.commit();
				con.close();con = null;
//				DBDisconnect(con);													// DB Disconnect

			}catch(SQLException e){ 												// SQL Error
				throw new Exception("SQLException:" + e.getMessage());				// throw Exception
			}																		// 
		} catch (Exception ex) {													// catch Exception
			DBRollback(con);
			try{
				if(con != null){
					con.close();con = null;
					System.out.println( "***finally*** RDB Disconnected !" );
				}
			}catch(Exception ex2){
				System.out.println( "***finally*** RDB Disconnected Error !" );
			}
//	*********** E10K移行時追加(2006/06/12) END *************************************
			throw new SQLException("Dbcom.Log_out:" + ex.toString()); 				// *** throw SQLException ***
		}																			//	
	}
//	*********** E10K移行時追加(2006/05/12) *****************************************
	/**
	********************************************************************************
	* メソッド名	getYMDString				指定カレンダーから日付を文字列で取得する。
	* @param		Calendar					d
	* @return		String						文字列日付（"YYYYMMDD HH:MM:SS"）
	********************************************************************************
	*/
	public static String getYMDString(Calendar d) throws Exception
	{
		try {
			int yyyy, mm, dd, hh, ms, ss;
			String	yyyymmdd, st_yyyy, st_mm, st_dd, hhmmss, st_hh, st_ms, st_ss, now;

			yyyy	= d.get(d.YEAR); st_yyyy = "";st_yyyy += yyyy;
			mm	= d.get(d.MONTH)+1; st_mm = "";st_mm += mm;
			dd	= d.get(d.DATE); st_dd = "";st_dd += dd;
			if( yyyy < 1000 ){ st_yyyy = "0" + yyyy; }
			if( yyyy < 100	){ st_yyyy = "00" + yyyy; }
			if( yyyy < 10	){ st_yyyy = "000" + yyyy; }
			if( mm < 10 ){ st_mm = "0" + mm; }
			if( dd < 10 ){ st_dd = "0" + dd; }
			yyyymmdd = st_yyyy + st_mm + st_dd;

			hh	= d.get(d.HOUR_OF_DAY); st_hh = "";
			st_hh += hh;
			ms	= d.get(d.MINUTE); st_ms = "";st_ms += ms;
			ss	= d.get(d.SECOND); st_ss = "";st_ss += ss;
			if( hh < 10 ){ st_hh = "0" + hh; }
			if( ms < 10 ){ st_ms = "0" + ms; }
			if( ss < 10 ){ st_ss = "0" + ss; }
			hhmmss = st_hh + ":" + st_ms + ":" + st_ss;
			now = yyyymmdd + " " + hhmmss;
			return now;

		} catch (Exception ex) {
			throw new Exception("getYYYYMMDD:" + ex.toString());
		}
	}	// ----------------------- End of Method -----------------------------------
}
