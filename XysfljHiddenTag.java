package xysk;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.sql.SQLException;
import java.sql.ResultSet;
import javax.servlet.http.HttpServletRequest;

abstract public class XysfljHiddenTag extends XysfljControlTag{
	final static public String INIT = "";

	/**
	 * @see xysf.XysfljControlTag#XysfljControlTag(String)
	 */
	public XysfljHiddenTag(String name){
		this(name,"");
	}
	/**
	 * 名前とComboDataを指定してオブジェクトを作成
	 * @param name 登録するコンボの名前
	 * @param p ページのパラメータ郡(初期値の解析に使用)
	 */
	public XysfljHiddenTag(String name,XysfljParameters p){
		this(name,p.getRequestParameter(name));
	}
	/**
	 * 名前とフォームリクエストを指定してオブジェクトを作成
	 * @param name 登録するコンボの名前
	 * @param r ブラウザから渡されるリクエスト(初期値の解析に使用)
	 */
	public XysfljHiddenTag(String name,HttpServletRequest r){
		this(name,r.getParameter(name));
	}
	/**
	 * 名前と初期値とComboDataを指定してオブジェクトを作成
	 * @param name 登録するコンボの名前
	 * @param def コンボの初期値とするvalue値
	 * @param d このコンボの要素を格納するComboDataオブジェクト
	 */
	public XysfljHiddenTag(String name,String def){
		super(name);
		setValue(def);
	}
	
	/**
	 * コンボのタグを出力する
	 * @see xysf.XysfljControlTag#createTag()
	 */
	public String createTag(){
		StringBuffer tag = new StringBuffer();
		tag.append("<INPUT type='hidden' name='");
		tag.append(CONTROL_NAME);
		tag.append("' value='");
		tag.append(getValue());
		tag.append("'>\r\n");
		tag.append(createInitialParamTag());
		return XysfljGenericRules.exchangeOutString(tag);
	}
}
