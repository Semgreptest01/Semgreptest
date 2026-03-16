/*
 *
 *  修正履歴
 *  L001  2006/05/12 K.Sasaki   ログ出力対応　ログ出力メソッドのコメントアウトと新規追加
 *  L002  2006/06/12 O.Ogawara  サービス時間外の場合、コネクションクローズ処理追加
 */
package xysk;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Calendar;
import javax.servlet.http.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;
import javax.servlet.RequestDispatcher;

/**
 */
abstract public class XysfljParameters extends Hashtable{
	final public String ID;
	
	//ステータス(ページ状態)
	//INIT(0)以外のものは、継承先のクラスで定義する
	final public static int INIT		=  0;	//ステータス

	final static public int ESSENTIAL = 1;
	final static public int OPTIONAL = 0;
	
	final static public int DATA_COMPLETE = 0;
	final static public int DATA_MISSING  = 2^0;
	final static public int DATA_ERROR    = 2^1;
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	
	private XysfljMasterDB	masterDbConnection		= null;
	private XysfljSlaveDB slaveDbConnection = null;

	//ステータス(ページ状態)
	private int status;
	private int oldStatus;
	private int commandTypeStatus;
	final public static String COMMAND_TYPE = "CommandType";

	/**
	 * コマンドタイプ
	 */
	final public static int COMMAND_INIT			=  0;	//初期状態
	final public static int COMMAND_INSERT		=  1;	//DB挿入
	final public static int COMMAND_UPDATE		=  2;	//DB更新
	final public static int COMMAND_REFERENCE	=  3;	//検索
	final public static int COMMAND_PAGE_UP		=  4;	//上ページ
	final public static int COMMAND_PAGE_DOWN	=  5;	//下ページ
	final public static int COMMAND_PAGE_LEFT	=  6;	//左ページ
	final public static int COMMAND_PAGE_RIGHT	=  7;	//右ページ
	final public static int COMMAND_PRINT		=  8;	//印刷
	final public static int COMMAND_DOWNLOAD		=  9;	//ダウンロード
	final public static int COMMAND_PAGE_MOVE	= 10;	//その他ページ移動
	
	/**
	 * ログインユーザのタイプ
	 */
	private int loginUserType = LOGIN_USER_TYPE_INIT;
	final static public int LOGIN_USER_TYPE_INIT		= 0;
	final static public int LOGIN_USER_TYPE_MS		= 1;
	final static public int LOGIN_USER_TYPE_DR		= 2;
	final static public int LOGIN_USER_TYPE_OTHER	= 3;

	/**
	 * ログインユーザのID
	 */
	private String loginUserID = "";

	/**
	 * ログインユーザの組織
	 */
	private String loginUserSection = "";
	
	/**
	 * ログインユーザの運営部(確定していない場合は空文字)
	 */
	private String loginUserMS = "";

	/**
	 * ログインユーザのDR(確定していない場合は空文字)
	 */
	private String loginUserDR = "";
	
	private String findSoshikiCode = "";

	/**
	 * ログインユーザの取引相手先コード(確定していない場合は空文字)
	 */
	private Vector loginUserTorihikisakiCD = new Vector();
	
//	//出力メッセージ格納
//	private Vector messages = new Vector();
	
	/**
	 * 共通メッセージボックス。
	 */
	private Vector msg_box = new	Vector();		// 画面Msg変数インスタンス生成
	
	/**
	 * 共通メッセージボックスに追加されたキーの一覧。
	 */
	private Vector msg_keys = new Vector();		//メッセージの重複チェックに利用

	/**
	 * メッセージの格納場所。
	 */
	public final static Hashtable messageSet = createMessageSet();
	private static Hashtable createMessageSet(){
		Hashtable messageHash = new Hashtable();
		messageHash.put("1000","9999 システム障害です。");
		messageHash.put("1001","不正なデータが存在します。");
		return messageHash;
	}
	static public String getMessage(int key){
		String m = messageSet.get(String.valueOf(key)).toString();
		if(null == m){
			m = "9999 システム障害です。";
		}
		return m;
	}

// *****************************************************************************************************
// ******** No.L001-1 2006/05/12変更(E10K移行対応) START ***********************************************
// *****************************************************************************************************
    /**
     * ログ出力
     * @param commandType 処理種別
     */
/*
    public void putLog(int commandType) {
        if (commandType != COMMAND_INIT) {
            try {
                // 処理種別から関数IDを決定
                String funcId = "";
                switch (commandType) {
                case COMMAND_REFERENCE:     // 検索
                case COMMAND_PAGE_UP:       // 上ページ
                case COMMAND_PAGE_DOWN:     // 下ページ
                case COMMAND_PAGE_LEFT:     // 左ページ
                case COMMAND_PAGE_RIGHT:    // 右ページ
                case COMMAND_PAGE_MOVE:     // その他ページ移動
                    funcId = XysfljGenericRules.LOG_FUNC_ID_SEARCH;
                    break;
                case COMMAND_PRINT:         // 印刷
                    funcId = XysfljGenericRules.LOG_FUNC_ID_PRINT;
                    break;
                case COMMAND_DOWNLOAD:      // ダウンロード
                    funcId = XysfljGenericRules.LOG_FUNC_ID_DOWNLOAD;
                    break;
                case COMMAND_INSERT:        // 登録
                    funcId = XysfljGenericRules.LOG_FUNC_ID_INSERT;
                    break;
                case COMMAND_UPDATE:        // 更新
                    funcId = XysfljGenericRules.LOG_FUNC_ID_UPDATE;
                    break;
                default:                    // 上記以外
                	return;					//ログ処理を実施しない
                }

				// 組織コードを取得
				String sosikiCode = getLoginUserSection();

				String id = "";
				id = ID.substring(0,5).toUpperCase() + ID.substring(6,9);
				
                // ユーザIDを取得
                String nameCode = (String)getSessionParameter("namecd");

XysfljGenericRules.outputMessage("操作ログ\r\n  組織コード : \"" + sosikiCode + "\"\r\n  氏名コード : \"" + nameCode + "\"\r\n  MSI画面ID : \"" + id + "\"\r\n  MSI機能ID : \"" + funcId + "\"");
                if (sosikiCode == null || sosikiCode.equals("") ||
                		nameCode == null || nameCode.equals("")) {
					addMessage("ユーザ情報が正しくありません。");
	                executeErrorRoutine();
                }
                
                // ログを出力
                Connection con = masterDbConnection.getConnection();
                masterDbConnection.Log_out(con, sosikiCode, nameCode, id, funcId);
            } catch (SQLException ex) {
                // ログ出力に失敗した場合
            }
        }
    }
*/
	/**
	********************************************************************************
	* メソッド名		putLog		ログメソッドの呼び出し
	* @param		String		commandType   (状態) 初期画面時:0 以外:1
	* @param		String		SCREEN_ID     (画面ＩＤ)
	* @param		String		SCREEN_NM     (画面名称)
	* @param		String		OBJECT_NM     (ﾛｸﾞﾒｯｾｰｼﾞ必要情報) jsp名称
	* @return		なし
	********************************************************************************
	*/
	public void putLog( int commandType, String SCREEN_ID, String SCREEN_NM, String OBJECT_NM ) {
		if (commandType != COMMAND_INIT) {
			try {
				// 機能IDの取得
				String funcId = "";
				switch (commandType) {
					case COMMAND_REFERENCE:     // 検索
					case COMMAND_PAGE_UP:       // 上ページ
					case COMMAND_PAGE_DOWN:     // 下ページ
					case COMMAND_PAGE_LEFT:     // 左ページ
					case COMMAND_PAGE_RIGHT:    // 右ページ
					case COMMAND_PAGE_MOVE:		// その他ページ移動
						funcId = XysfljGenericRules.LOG_FUNC_ID_SEARCH;
						break;
					case COMMAND_PRINT:			// 印刷
						funcId = XysfljGenericRules.LOG_FUNC_ID_PRINT;
						break;
					case COMMAND_DOWNLOAD:      // ﾀﾞｳﾝﾛｰﾄﾞ
						funcId = XysfljGenericRules.LOG_FUNC_ID_DOWNLOAD;
						break;
					case COMMAND_INSERT:		// 登録
						funcId = XysfljGenericRules.LOG_FUNC_ID_INSERT;
						break;
					case COMMAND_UPDATE:		// 更新
						funcId = XysfljGenericRules.LOG_FUNC_ID_UPDATE;
						break;
					default:					// 上記以外
						return;					// ﾛｸﾞ処理を実施しない
				}
				// セッション情報の取得
				String sosikiCode = (String)getSessionParameter("sosiki");
				String nameCode = (String)getSessionParameter("namecd");
				String syokuiCode = (String)getSessionParameter("syokui");

				if (sosikiCode == null || sosikiCode.equals("") ||
						nameCode == null || nameCode.equals("") ||
						syokuiCode == null || syokuiCode.equals("")) {
					addMessage("ユーザ情報が正しくありません。");
					executeErrorRoutine();
				}

				// ﾛｸﾞ出力 ( 項目 ) 画面ID,画面名称,PGM名称,機能ID,氏名ｺｰﾄﾞ,職位ｺｰﾄﾞ,組織ｺｰﾄﾞ
				Connection con = masterDbConnection.getConnection();
				masterDbConnection.printLog(SCREEN_ID,SCREEN_NM,OBJECT_NM, funcId, nameCode, sosikiCode, syokuiCode);
				masterDbConnection.DBDisconnect(con);

			} catch (Exception e) {
				// ﾛｸﾞ出力失敗
				addMessage("ログの出力に失敗しました。");
				executeErrorRoutine();
			}
		}
	}
// ******** No.L001-1 ****************************  END  ***********************************************
	/**
	 * コンストラクタ
	 * @param request
	 */
	public XysfljParameters(String id,HttpServletRequest req,HttpServletResponse res){
		ID = id;
		session = req.getSession();
		
		request = req;
		
		response = res;
		
		//認証チェック
//		Xysflj_Dbcom	dbcom		=	new	Xysflj_Dbcom();						// RDB Common Class instance生成
		try{
			masterDbConnection = new XysfljMasterDB(ID);						// RDB Common Class instance生成
		}
		catch(XysfljDB.DBException e){
			addMessage("DBの接続に失敗しました。");
			executeErrorRoutine(e);
		}
  		
  		commandTypeStatus = 0;
  		String ct = request.getParameter("CommandType");
  		try{
	  		commandTypeStatus = Integer.parseInt(ct);
  		}
  		catch(Exception e){
  			commandTypeStatus = 0;
  		}

		boolean sessionSetFlg = Xysflj_Common.session_Set( masterDbConnection, request, session, msg_box );

		String namecd = null;
		namecd = (String)session.getAttribute("namecd");
		
		boolean pageStartFlg = false;
		if(false == ID.substring(6,9).equals("000")){
			pageStartFlg = true;
		}

		if(false == sessionSetFlg && null == ct && true == pageStartFlg){
			//セッションがない場合はエラー
			executeErrorRoutine();												// 　同 Exception
		}

XysfljGenericRules.outputMessage(" [ R : " + (String)getSessionParameter("renban") + " ] - [ S : " + (String)getSessionParameter("sosiki") + "]");
		msg_box.removeAllElements();									// MSG CLEAR
		if(!Xysflj_Common.nin_Chk( session, msg_box )) {
			Xysflj_Common.debug_Msg( request, "nin_Chk NG", msg_box );		// *** DEBUG MSG ***
			addMessage("認証 Check でエラーになりました。");
			executeErrorRoutine(new Exception("認証チェックエクセプション"));											// 認証 Check
		}
		if(!Xysflj_Common.time_Chk( masterDbConnection, (String)getSessionParameter("renban"), (String)request.getParameter("inout"), msg_box )) {		// サービス時間チェック
//			Xysflj_Common.debug_Msg( request, "time_Chk NG", msg_box );		// *** DEBUG MSG ***
//	*********** E10K移行時追加(2006/06/12) L002 ************************************
			exit();															//	コネクションクローズ処理
//	*********** E10K移行時追加 END         *****************************************
			msg_box.addElement( "******** time check ********" );
			executeErrorRoutine(new Exception("サービス時間外エクセプション"));											//   同 Exception
		}
		String menu_inout = (String)request.getParameter("inout");
		if ( menu_inout != null && menu_inout.equals("1") ) {			
			loginUserTorihikisakiCD = Xysflj_Common.getTorihikisakiCD(
														 masterDbConnection,
														 (String)getSessionParameter("namecd"),
														 (String)getSessionParameter("logdate"),
														  msg_box );
//System.out.println("Parameters"+loginUserTorihikisakiCD);
//			if ( loginUserTorihikisakiCD == null ||  loginUserTorihikisakiCD.equals("") ){
			if ( loginUserTorihikisakiCD.size() == 0	||	loginUserTorihikisakiCD.elementAt(0) == ""){
				addMessage("利用者コードが未登録です。");
				executeErrorRoutine(new Exception("取引相手先コード取得エクセプション"));	
			}
		}

		//ログインユーザのタイプをインスタンスにセットする
		try{
			setLoginUserInfo(masterDbConnection);
		}
		catch(Exception e){
			addMessageByCode(1000);
			executeErrorRoutine(e);
		}
		
		//ページのステータスを初期表示状態にする
  		setStatus(INIT);
  		try{
  			String s = request.getParameter("Status");
	  		oldStatus = Integer.parseInt(s);
  		}
  		catch(Exception e){
  			oldStatus = 0;
  		}

// *****************************************************************************************************
// ******** No.L001-2 2006/05/12変更(E10K移行対応) START ***********************************************
// *****************************************************************************************************
//		putLog(commandTypeStatus);
		try{
			Hashtable hash_data = new Hashtable();
			if( !Xysflj_Common.getParam( request, hash_data, msg_box ) )
				 throw new Exception();
			if( COMMAND_DOWNLOAD != commandTypeStatus ){
				String ScreenNm = "";
				if( null != request.getParameter("screenNm") ){
					switch( new Integer( request.getParameter("screenNm") ).intValue() ){
						case 120: ScreenNm = XysfljGenericRules.SCREEN_NM_120; break;
						case 130: ScreenNm = XysfljGenericRules.SCREEN_NM_130; break;
						case 220: ScreenNm = XysfljGenericRules.SCREEN_NM_220; break;
						case 230: ScreenNm = XysfljGenericRules.SCREEN_NM_230; break;
						default : return;
					}
					putLog( commandTypeStatus, 												// 状態（初期表示時:0）
							(String)request.getParameter("screenId"), 						// 画面ＩＤ
							ScreenNm,
							ScreenNm
					);
				}
			}

		}catch(Exception e){
			XysfljParameters.redirectErrorPage(req,res,e);
		}
// ******** No.L001-2 ****************************  END  ***********************************************
	}

	/**
	 * コンストラクタ
	 * @param request
	 */
	public XysfljParameters(String id,HttpServletRequest req){
		this(id , req, null);
	}
	
	/**
	 * ステータスをセットする
	 * @param s セットするステータスの値
	 */
	protected void setStatus(String s){
		try{
			status = Integer.parseInt(s);
		}
		catch(NumberFormatException e){
			status = INIT;
		}
	}
	
	/**
	 * ステータスをセットする
	 * @param s セットするステータスの値
	 */
	protected void setStatus(int s){
		status = s;
	}
	
	/**
	 * ステータスを返す
	 * @return int ステータスの値
	 */
	public int getStatus(){
		return status;
	}

	/**
	 * Method setControl.
	 * フォーム項目を自オブジェクトに登録
	 * @param v 登録するフォーム項目
	 * @return String 登録したフォーム項目のvalue値(value値がセットされていない場合はnullが帰る)
	 */
	protected String setControl(XysfljControlTag v){
		super.put(v.CONTROL_NAME,v);
		
		//コンボボックスの場合、Value値がセットされていない場合は、1番目の要素をValue値にセットする
		if(v instanceof XysfljComboTag){
			String val = v.getValue();
			if(null == val || val.equals(XysfljComboTag.INIT)){
				((XysfljComboTag)v).setValueOfFirstElement();
			}
		}
		String ret = v.getValueAllowNull();
		return ret;
	}
	
	/**
	 * Method getControl.
	 * 指定されたキーに関連づけられたフォーム項目を返す
	 * @param key	キー
	 * @return XysfljControlTag	指定されたキーに関連づけられたフォーム項目
	 */
	public XysfljControlTag getControl(String key){
		return (XysfljControlTag)super.get(key);
	}
	
	/**
	 * 指定されたキーに関連づけられたフォーム項目のvalue値を返す
	 * @param key キー
	 * @return String キーに関連づけられたフォーム項目のvalue値
	 */
	public String getValue(String key){
		return getControl(key).getValue();
	}
	
	/**
	 * HttpServletRequestを返す
	 * @return HttpServletRequest
	 */
	public HttpServletRequest getRequest(){
		return request;
	}
	/**
	 * リクエスト情報からキーに関連づけられたパラメータを返す
	 * @param key
	 * @return String
	 */
	public String getRequestParameter(String key){
		return request.getParameter(key);
	}
	
	public String createHiddenTag(String n,String v){
		StringBuffer tag = new StringBuffer();
		tag.append("\t\t<INPUT type='hidden' name='");
		tag.append(n);
		tag.append("' value='");
		tag.append(v);
		tag.append("'>\r\n");
		return tag.toString();
	}
	
	public String createTagOfStatus(){
		return createHiddenTag("Status",String.valueOf(status));
	}
	
	public String createTagOfCommandType(){
		return createHiddenTag("CommandType",String.valueOf(commandTypeStatus));
	}
	
	/**
	 * ログインユーザの情報をインスタンスにセットする
	 * 
	 */
	private void setLoginUserInfo(XysfljDB dbcom)
		throws ParameterException{
		//ログインユーザのIDをセット
		Object o = getSessionParameter("namecd");
		loginUserID = (String)o;
		
		//ログインユーザの組織コードをセット
		loginUserSection = (String)getSessionParameter("sosiki");
/*	
		String t = Xysflj_Common.sosiki_Chk(dbcom,loginUserSection);
		
		if(null == loginUserID || loginUserID.equals("") ||
				null == loginUserSection || loginUserSection.equals("") ||
				null == t || t.equals("")){
			throw new ParameterException();
		}
		
		if(t.equals("0")){
			//その他
			loginUserType = LOGIN_USER_TYPE_OTHER;
			loginUserMS = "";
			loginUserDR = "";
		}
		else if(t.equals("1")){
			//運営部
			loginUserType = LOGIN_USER_TYPE_MS;
			loginUserMS = loginUserSection;
			loginUserDR = "";
		}
		else if(t.equals("2")){
			//DR
			loginUserType = LOGIN_USER_TYPE_DR;
			loginUserMS = getMsCodeFromDr(dbcom,loginUserSection);
			loginUserDR = loginUserSection;
		}
		
		//ログ出力
		String tmpUserType = "";
		if(LOGIN_USER_TYPE_MS == loginUserType){
			tmpUserType = "運営部";
		}
		else if(LOGIN_USER_TYPE_DR == loginUserType){
			tmpUserType = "DR";
		}
		else{
			tmpUserType = "その他";
		}
		XysfljGenericRules.outputMessage("ログイン情報\r\n  ログインユーザタイプ : \"" + tmpUserType + "\"\r\n  運営部コード : \"" + loginUserMS + "\"\r\n  DRコード : \"" + loginUserDR + "\"");
*/
	}
	
	/**
	 * ログインユーザのIDを返す。
	 * @return ログインユーザID
	 */
	public String getLoginUserID(){
		if(null == loginUserID){
			return "";
		}
		return loginUserID;
	}
	
	/**
	 * ログインユーザのログイン組織コードを返す。
	 * @return ログイン組織コード
	 */
	public String getLoginUserSection(){
		if(null == loginUserSection){
			return "";
		}
		return loginUserSection;
	}

	/**
	 * ログインユーザのログイン組織コードを返す。
	 * @return ログイン組織コード
	 */
	public Vector getLoginUserTorihikisakiCD(){
		if(null == loginUserTorihikisakiCD){
			return loginUserTorihikisakiCD;
		}
//System.out.println("Parameters2"+loginUserTorihikisakiCD);
		return loginUserTorihikisakiCD;
	}
	
	/**
	 * DRコードより、所属の運営部コードを取得する。
	 * @param dr DRコード
	 * @return 運営部コード
	 * @throws ParameterException
	 */
	public String getMsCodeFromDr(XysfljDB dbcom,String dr)
			throws ParameterException{
		String ms = null;
		ResultSet rs = null;
		try{
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT UNEI_DIV ");
			sb.append(" FROM   UY007M ");
			sb.append(" WHERE  DR = '");
			sb.append(dr);
			sb.append("' ");
			sb.append("        AND TEN = '000000'");
			
			String sql = sb.toString();
			rs = dbcom.selectTable(sql);
		}
		catch(XysfljDB.DBException e){
			throw new ParameterException(e);
		}
		
		try{
			while(null != rs && rs.next() && null == ms){
				ms = rs.getString("UNEI_DIV");
			}
		}
		catch(SQLException e){
			throw new ParameterException(e);
		}
		return ms;
	}
	
	/**
	 * リクエスト元のページでの初期ステータス(今回のActionで変更になる前)を返す
	 * @return int
	 */
	public int getOldStatus(){
		return oldStatus;
	}
	
	/**
	 * リクエスト元のページでの初期ステータス(今回のActionで変更になる前)を返す
	 * @return int
	 */
	public void setOldStatusForAll(){
		Enumeration e = elements();
		while(e.hasMoreElements()){
			XysfljControlTag control = (XysfljControlTag)e.nextElement();
			control.setOld();
		}
	}
	/**
	 * リクエスト元のページでの初期ステータス(今回のActionで変更になる前)を返す
	 * @return int
	 */
	public boolean emptyAllControl(){
		boolean ret = true;
		Enumeration e = elements();
		while(e.hasMoreElements() && true == ret){
			XysfljControlTag control = (XysfljControlTag)e.nextElement();
			if(false == control.getValue().equals("")){
				ret = false;
			}
		}
		return ret;
	}
	
	/**
	 * 
	 * @return int
	 */
	public int getCommandType(){
		return commandTypeStatus;
	}

	/**
	 * 
	 */
	private void setCommandType(int v){
		commandTypeStatus = v;
	}
	
	/**
	 * セッションにパラメータをセットする
	 * @param key
	 * @param o
	 */
	public void setSessionParameter(String key,Object o){
		session.setAttribute(key,o);
	}
	
	/**
	 * セッション情報のパラメータを返す
	 * @param key
	 * @return Object
	 */
	public Object getSessionParameter(String key){
		return session.getAttribute(key);
	}
	
	/**
	 * セッション情報のパラメータを削除する
	 * @param key
	 */
	public void removeSessionParameter(String key){
		session.removeAttribute(key);
	}
	
	/**
	 * ログインユーザのタイプを返す
	 * @return int
	 */
	public int getLoginUserType(){
		return loginUserType;
	}
	
	/**
	 * ログインユーザの運営部を返す
	 * @return String
	 */
	public String getLoginUserMS(){
		return loginUserMS;
	}
	
	/**
	 * ログインユーザのDRを返す
	 * @return String
	 */
	public String getLoginUserDR(){
		return loginUserDR;
	}
	
	public XysfljDB getDbConnection()
		throws XysfljDB.DbConnectException{
		slaveDbConnection = masterDbConnection.createSlaveConnection();
		return slaveDbConnection;
	}
	
	public void setFindSoshikiCode(String s){
		masterDbConnection.setSlaveOrganizationCode(s);
	}

//	public Calendar getApplicationDate(){
//		Calendar cal = null;
//		try{
//			String d = "";
//			XysfljDB tdb = getDbConnection();
//			final String sql = "SELECT HENI_DATE1 FROM TW001M WHERE DATE_KBN = '0' AND DATE_HENI = 0";
//			ResultSet rs = tdb.selectTable(sql);
//			while(null != rs && rs.next()){
//				d = rs.getString("HENI_DATE1");
//			}
//			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
//			ParsePosition  pos = new ParsePosition(0);
//			java.util.Date ym = df.parse(d, pos);
//			if(null != ym){
//				cal = Calendar.getInstance();
//				cal.setTime(ym);
//			}
//		}
//		catch(Exception e){
//			return null;
//		}
//		return cal;
//	}
	
	/**
	 * ページで出力するメッセージの登録を行う
	 * @param msg
	 */
	public void addMessage(String msg){
		if(false == msg_keys.contains(msg)){	//指定されたメッセージが追加済みの場合は新たにメッセージを追加しない
			msg_box.add(msg);
		}
	}
	
	public Vector getMessages(){
		return msg_box;
	}
	
	/**
	 * ページで出力するメッセージの登録を行う
	 * @param key
	 */
	public void addMessageByCode(int key){
		//メッセージ重複チェック
		String k = String.valueOf(key);
		if(false == msg_keys.contains(k)){	//指定されたキーのメッセージが追加済みの場合は新たにメッセージを追加しない
			if(key < 1000){
				Xysflj_Common.msg_Out(key,msg_box);
			}
			else{
				msg_box.add(getMessage(key));
			}
			msg_keys.add(k);
		}
	}

	/**
	 * ページへ出力するメッセージのJavaScriptを返す
	 * @return String
	 */
	public String createMessagePrintJS(){
		StringBuffer sb = new StringBuffer();
//		Enumeration enum = messages.elements();
//		while(enum.hasMoreElements()){
//			XysfljMessageBox m = (XysfljMessageBox)enum.nextElement();
//			sb.append("msg_print(\"");
//			sb.append(m.getCode());
//			sb.append("\",\"");
//			sb.append(m.getMessage());
//			sb.append("\")\r\n");
//		}

		Enumeration enum = msg_box.elements();
		while(enum.hasMoreElements()){
			String m = (String)enum.nextElement();
			sb.append("msg_print(\"");
			sb.append("\",\"");
			sb.append(m);
			sb.append("\")\r\n");
		}
		return XysfljGenericRules.exchangeOutString(sb);
	}
	
	/**
	 * エクセプションを新規に発生させ、エラールーチンを実行する。
	 */
	public void executeErrorRoutine(){
		if(null == msg_box){
			msg_box = new Vector();
		}
		if(0 == msg_box.size()){
			msg_box.add("9999 システム障害です。");
		}
		executeErrorRoutine(new Exception("システム障害"));
	}
	
	/**
	 * エクセプションを元にエラールーチンを実行する。
	 * @param e
	 */
	public void executeErrorRoutine(Exception e){
//		e.printStackTrace();
		XysfljRuntimeException ex = new XysfljRuntimeException(e,getMessages());
		throw ex;
	}
	
	/**
	 * エラーページへリダイレクトする。
	 * エラーメッセージはeより取得する(eがXysfljRuntimeExceptionのとき)。
	 * @param request
	 * @param response
	 * @param e
	 */
	public static void redirectErrorPage(HttpServletRequest request, HttpServletResponse response, Exception e){
		Vector v = null;
		if(e instanceof XysfljRuntimeException){
			v = ((XysfljRuntimeException)e).getMessages();
		}
		redirectErrorPage(request,response,v);
	}
	
	/**
	 * エラーページへリダイレクトする。
	 * @param request
	 * @param response
	 * @param messages エラーメッセージ
	 */
	public static void redirectErrorPage(HttpServletRequest request, HttpServletResponse response, Vector messages){
		if(null == messages){
			messages = new Vector();
		}
		if(0 == messages.size()){
			messages.add("9999 システム障害です。");
		}
		// ****************************************************************************
		// ******* サービス時間チェックとエラー画面の分岐処理 **** START **************
		// ****************************************************************************
		int time_sw = 0;
		for (int i=0; i< messages.size(); i++ ) {
			if ( messages.elementAt(i).equals("******** time check ********") ){
				messages.removeElementAt(i);
				time_sw = 1;
			}
		}
		request.setAttribute("msg_box", messages);							// Msg Box set
		RequestDispatcher dispatcher = request.getRequestDispatcher("/Xysflj_99999J.jsp");
		if ( time_sw == 1 ){	// サービス時間チェック？
			dispatcher = request.getRequestDispatcher("/Xysflj_99991J.jsp");
		}
		// ****************************************************************************
		// ******* サービス時間チェックとエラー画面の分岐処理 **** END ****************
		// ****************************************************************************
		
		try{
			dispatcher.forward(request, response);								// include
		}
		catch(Exception e){
			//転送失敗
			messages.add("表示内容が正しくない可能性があります。");
		}
	}
	
	/**
	 * 終了処理。
	 * DBの接続をクローズする。
	 */
	public void exit(){
		
		if(null != masterDbConnection){
			try{
				masterDbConnection.statmentClose();
				masterDbConnection.connectionClose();
				masterDbConnection = null;
			}
			catch(Exception e){
			}
		}

		if(null != slaveDbConnection){
			try{
				slaveDbConnection.statmentClose();
				slaveDbConnection.connectionClose();
				slaveDbConnection = null;
			}
			catch(Exception e){
			}
		}
	}
	
	/**
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() throws Throwable {
		exit();
		super.finalize();
	}

	static public class ParameterException extends XysfljException{
		public ParameterException(){}
		public ParameterException(Exception e){
			super(e);
		}
	}

	static public class EssentialException extends ParameterException{
	}

	static public class KeyNullPointerException extends ParameterException{
	}

}
