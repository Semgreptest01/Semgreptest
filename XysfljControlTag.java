package xysk;

import java.util.Calendar;

abstract public class XysfljControlTag{
	/**
	 * バリューの初期値。
	 */
	final static public String INIT = "";

	final protected String CONTROL_NAME;
	private String value = "";
	private String oldValue = "";
	
	private String onClick = "";		//JavaScriptのOnClickイベント
	private String onChange = "";		//JavaScriptのOnChangeイベント
	private String onBlur = "";		//JavaScriptのOnBlurイベント
	private int tabIndex = 0;			//タブインデックスの指定
	private String style = "";			//スタイルシートの指定

	/**
	 * 画面のパラメータ管理クラス。
	 * <BR>
	 * 継承先でページのパラメータにアクセスする場合、このオブジェクトから操作を行う。<BR>
	 * このオブジェクトは、存在しない場合があるので、使用するときは注意が必要。<BR>
	 * 継承先で使用するときは、コンストラクタの引数で渡す必要がある。
	 */
	protected XysfljParameters param = null;
	
	/**
	 * コンストラクタ
	 * @param n コントロールの名前
	 */
	public XysfljControlTag(String n){
		this(n,"");
	}

	/**
	 * コンストラクタ
	 * @param n コントロールの名前
	 * @param v コントロールの値
	 */
	public XysfljControlTag(String n, String v){
		CONTROL_NAME = n;
		setValue(v);
	}

	public void setValue(String v){
		value = v;
	}
	public String getValue(){
		if(null == value){
			return "";
		}
		return value;
	}
	public String getValueAllowNull(){
		return value;
	}
	
	public void setOldValue(String v){
		oldValue = v;
	}
	public String getOldValue(){
		if(null == oldValue){
			return "";
		}
		return oldValue;
	}
	public String getOldValueAllowNull(){
		return oldValue;
	}
	
	/**
	 * リクエストされたフォーム画面で初期表示に使用した値をvalueにセットする。
	 * 値がない場合は、INITがセットされる。
	 * @param request
	 */
	public void setOld(){
		String v = null;
		if(null != param){
			v = param.getRequestParameter("initial" + CONTROL_NAME);
		}
		if(null == v){
			v = INIT;
		}
		setValue(v);
	}
	
	/**
	 * Method createInitialParamTag.
	 * このコントロールのvalue値をhiddenにして返す(nameはコントロール名の頭ににinitialがつく)
	 * @return String
	 */
	protected String createInitialParamTag(){
		String v = value;
		if(null == v){
			v = "";
		}
		return "<INPUT type='hidden' name='initial" + CONTROL_NAME + "' value='" + v + "'>\r\n";
	}
	
	public void setStyle(String v){
		style = v;
	}
	public void setTabIndex(int v){
		tabIndex = v;
	}
	public void setOnClick(String v){
		onClick = v;
	}
	public void setOnChange(String v){
		onChange = v;
	}
	public void setOnBlur(String v){
		onBlur = v;
	}
	public String getStyle(){
		if(false == style.equals("")){
			return " style=\"" + style + "\"";
		}
		return "";
	}
	public String getTabIndex(){
		if(0 < tabIndex){
			return " tabindex=\"" + tabIndex + "\"";
		}
		return "";
	}
	public String getJavaScript(){
		StringBuffer sb = new StringBuffer();
		if(false == onClick.equals("")){
			sb.append(" onclick=\"" + onClick + "\"");
		}
		if(false == onChange.equals("")){
			sb.append(" onchange=\"" + onChange + "\"");
		}
		if(false == onBlur.equals("")){
			sb.append(" onblur=\"" + onBlur + "\"");
		}
		return XysfljGenericRules.exchangeOutString(sb);
	}
	/**
	 * Method getDbData.
	 * DBから値を取得する(処理は、継承先で記述する)
	 * @throws DBException
	 */
	private void getDbData()
		throws XysfljDB.DBException{
	}
	/**
	 * Method changePeriod.
	 * プログラム日付の切り替えをチェック
	 * @param t 比較したい日付を持ったカレンダー
	 * @return boolean true:tとプログラム日付が一致していない、false:tとプログラム日付が一致している
	 */
	protected boolean changePeriod(Calendar t){
		Calendar now = Calendar.getInstance();
		Calendar changeTime = Calendar.getInstance();
		int y = now.get(Calendar.YEAR);
		int m = now.get(Calendar.MONTH);
		int d = now.get(Calendar.DATE);
		changeTime.set(y,m,d,XysfljGenericRules.CHANGE_HOUR,XysfljGenericRules.CHANGE_MINUTE);

		if(null == t ||
			(true == t.before(changeTime) &&
			true == changeTime.before(now))){
			return true;
		}
		return false;
	}
	/**
	 * Method createTag.
	 * コントロールのタグを作成
	 * @return String 作成したタグ
	 */
	abstract public String createTag();
}
