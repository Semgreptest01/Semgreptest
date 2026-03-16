/*
 *
 *  修正履歴
 *  L001  2006/06/08 K.Sasaki   文字化け対応 名称関連取得時にメソッドXysflj_Common.getStrCvt()を介す
 *
 */
 package xysk;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.sql.SQLException;
import java.sql.ResultSet;
import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;

abstract public class XysfljComboTag extends XysfljControlTag{
	/**
	 * コンボボックスの役割。
	 * 運営部でも、DR出もない場合は値をXysfljParameters.LOGIN_USER_TYPE_INITとする。
	 */
	private int role = XysfljParameters.LOGIN_USER_TYPE_INIT;
	
	/**
	 * コンボボックスの要素を格納するオブジェクト。
	 */
	public ComboData comboElements;
	
	/**
	 * 名前を指定してオブジェクトを作成。
	 * 初期値は、INITになる。
	 * @param name 登録するコンボの名前
	 * @see xysf.XysfljControlTag#XysfljControlTag(String)
	 */
	public XysfljComboTag(String name){
		this(name,INIT);
	}
	
	/**
	 * 名前と初期値を指定してオブジェクトを作成。
	 * @param name 登録するコンボの名前
	 * @param def コンボの初期値とするvalue値
	 */
	public XysfljComboTag(String name,String def){
		this(name,def,new ComboData());
	}
	
	/**
	 * 名前とComboDataを指定してオブジェクトを作成。
	 * @param name 登録するコンボの名前
	 * @param p ページのパラメータ郡(初期値の解析に使用)
	 */
	public XysfljComboTag(String name,XysfljParameters p){
		this(name,p.getRequestParameter(name),new ComboData());
		param = p;
		setOldValue(p.getRequestParameter("initial" + name));
	}
	
	/**
	 * 名前とページパラメータとComboDataを指定してオブジェクトを作成。
	 * @param name 登録するコンボの名前
	 * @param p ページのパラメータ郡(初期値の解析に使用)
	 * @param d このコンボの要素を格納するComboDataオブジェクト
	 */
	public XysfljComboTag(String name,XysfljParameters p,ComboData d){
		this(name,p.getRequestParameter(name),d);
		param = p;
		setOldValue(p.getRequestParameter("initial" + name));
	}
	
	/**
	 * 名前とフォームリクエストとComboDataを指定してオブジェクトを作成。
	 * @param name 登録するコンボの名前
	 * @param r ブラウザから渡されるリクエスト(初期値の解析に使用)
	 * @param d このコンボの要素を格納するComboDataオブジェクト
	 */
	public XysfljComboTag(String name,HttpServletRequest r,ComboData d){
		this(name,r.getParameter(name),d);
		setOldValue(r.getParameter("initial" + name));
	}
	
	/**
	 * 名前とフォームリクエストを指定してオブジェクトを作成。
	 * @param name 登録するコンボの名前
	 * @param r ブラウザから渡されるリクエスト(初期値の解析に使用)
	 */
	public XysfljComboTag(String name,HttpServletRequest r){
		this(name,r.getParameter(name),new ComboData());
		setOldValue(r.getParameter("initial" + name));
	}
	
	/**
	 * 名前と初期値とComboDataを指定してオブジェクトを作成。
	 * @param name 登録するコンボの名前
	 * @param def コンボの初期値とするvalue値
	 * @param d このコンボの要素を格納するComboDataオブジェクト
	 */
	public XysfljComboTag(String name,String def,ComboData d){
		super(name,def);
		comboElements = d;
	}
	
	/**
	 * セットされているvalue値の名前を返す。
	 * @return String
	 */
	public String getName(){
		return comboElements.getName(getValue());
	}
	
	/**
	 * コンボのタグを出力する。
	 * &lt;SELECT&gt; ・・・&lt;/SELECT&gt;の部分(SELECTのタグを含む)が出力される。
	 * @see xysf.XysfljControlTag#createTag()
	 */
	public String createTag(){
		StringBuffer tag = new StringBuffer();
		int s = comboElements.size();
		if(null != param &&
			XysfljParameters.COMMAND_PRINT == param.getCommandType()){
			tag.append("<TABLE border=\"0\" cellpadding=\"0\" cellspacing=\"0\"");
			tag.append(">\r\n");
			tag.append("\t<TR>");
			tag.append("<TD>");
			tag.append(getName());
			tag.append("<BR></TD>");
			tag.append("</TR>\r\n");
			tag.append("</TABLE>\r\n");
		}
		else{
			tag.append("<SELECT");
			tag.append(" name=\"" + CONTROL_NAME + "\"");
			tag.append(getTabIndex());
			tag.append(getJavaScript());
			tag.append(getStyle());
			tag.append(">\r\n");

			String userDefinedCode = getUserDefinedCode();
			if(false == userDefinedCode.equals("")){
				if(false == comboElements.contains(userDefinedCode)){
					param.addMessageByCode(1000);
					param.executeErrorRoutine();
				}
				else{
					tag.append("\t<OPTION value=\"" + userDefinedCode + "\"");
					tag.append(" selected");
					tag.append(">");
					tag.append(comboElements.getName(userDefinedCode) + "\r\n");
				}
			}
			else{
				Enumeration records = comboElements.elements();
				while(records.hasMoreElements()){
					String key = records.nextElement().toString();
					tag.append("\t<OPTION value=\"" + key + "\"");
					if(true == getValue().equals(key)){
						tag.append(" selected");
					}
					tag.append(">");
					tag.append(comboElements.getName(key) + "\r\n");
				}
			}

			tag.append("</SELECT>\r\n");
		}
		tag.append(createInitialParamTag());
		return XysfljGenericRules.exchangeOutString(tag);
	}
	
	private String getUserDefinedCode(){
		String r = "";
		if(XysfljParameters.LOGIN_USER_TYPE_MS == role){
			r = param.getLoginUserMS();
		}
		else if(XysfljParameters.LOGIN_USER_TYPE_DR == role){
			r = param.getLoginUserDR();
		}
		
		return r;
	}
	
	public void setValueOfFirstElement(){
		setValue(comboElements.firstKey());
	}
	
	/**
	 * 運営部のコンボボックスとして扱う
	 */
	public void asManagementSection(){
		role = XysfljParameters.LOGIN_USER_TYPE_MS;
		String ms = param.getLoginUserMS();
		if(false == ms.equals("")){
			setValue(ms);
		}
	}
	
	
	/**
	 * DRのコンボボックスとして扱う
	 */
	public void asDrSection(){
		role = XysfljParameters.LOGIN_USER_TYPE_DR;
		String dr = param.getLoginUserDR();
		if(false == dr.equals("")){
			setValue(dr);
		}
	}
	
	/**
	 * コンボボックスの要素を定義するクラス。
	 * XysfljComboTagクラスで1つオブジェクトが作成される。
	 * @author user
	 */
	static public class ComboData extends Hashtable{
		protected Vector keyList = new Vector();

		/**
		 * コンストラクタ
		 * @see java.lang.Object#Object()
		 */
		public ComboData(){}
		
		/**
		 * 要素キーのリストを返す。
		 * @see java.util.Dictionary#elements()
		 */
		public Enumeration elements(){
			return keyList.elements();
		}
		
		/**
		 * 項目の最初の要素 (インデックス 0 の項目) を返す。
		 * @return String
		 */
		public String firstElement(){
			String s = getName(firstKey());
			if(null != s){
				return s;
			}
			return "";
		}
		
		/**
		 * キーリストの最初の要素 (インデックス 0 の項目) を返す。
		 * @return String
		 */
		public String firstKey(){
			String ret = "";
			try{
				Object s = keyList.firstElement();
				if(null != s){
					ret = s.toString();
				}
			}
			catch(NoSuchElementException e){
				ret = "";
			}
			return ret;
		}
		
		public boolean contains(Object o){
			return keyList.contains(o);
		}
		
		/**
		 * 登録されている要素の数を返すす。
		 * @see java.util.Dictionary#size()
		 */
		public int size(){
			return keyList.size();
		}
		
		/**
		 * SQLのResultSetより、値を要素に追加する。
		 * @param rs 実行したSQLのResultSet
		 */
		public void put(ResultSet rs){
			put(rs,"value","name");
		}
		
		/**
		 * SQLのResultSetよりカラム名を指定して、値を要素に追加する。
		 * @param rs 実行したSQLのResultSet
		 * @param c コードのカラム名
		 * @param n 名前のカラム名
		 */
		public void put(ResultSet rs,String c,String n){
			try{
				String code = rs.getString(c);
				// **** E10K移行時変更対応(2006/06/08) ***
				String name = Xysflj_Common.getStrCvt( rs.getString(n) );
//				String name = rs.getString(n);
				// **** END ******************************
				put(code,name);
//				put(code,Xysflj_Common.getStrCvt(name));
			}
			catch(SQLException e){
			}
		}
		
		/**
		 * SQLのResultSetよりカラム名を指定して、値を要素に追加する。
		 * コンボに「コード　+　名称」を表示
		 * @param rs 実行したSQLのResultSet
		 * @param c コードのカラム名
		 * @param n 名前のカラム名
		 */
		public void putCodeName(ResultSet rs,String c,String n){
			try{
				String code = rs.getString(c);
				// **** E10K移行時変更対応(2006/06/08) ***
				String name = Xysflj_Common.getStrCvt( rs.getString(n) );
//				String name = rs.getString(n);
				// **** END ******************************
				put(code,code + " " + name);
//				put(code,code + " " + Xysflj_Common.getStrCvt(name));
// System.out.println("putCodeName="+code+","+name);
			}
			catch(SQLException e){
			}
		}

		/**
		 * 要素に文字列を登録する。
		 * @param c コード
		 * @param n 名前
		 */
		public void put(String c,String n){
			if(null == n){
				n = "";
			}
			put(c,(Object)n);
		}
		
		/**
		 * 要素にオブジェクトを登録する。
		 * @param c コード
		 * @param v コードに対応させる値(継承先で作成したクラス)
		 */
		public void put(String c,Object v){
			//要素を追加
			try{
				super.put(c,v);
			}
			catch(NullPointerException e){
				e.printStackTrace();
			}

			//登録されていないキーの場合は新規登録する
			//(すでに登録されている場合は無視)
			if(-1 == keyList.indexOf(c)){
				keyList.add(c);
			}
		}
		
		/**
		 * 指定されたキーの要素に対応する名前を返す。
		 * @param v キー
		 * @return String 指定されたキーの要素に対応する名前
		 */
		public String getName(String v){
			String ret = (String)get(v);
			if(null == ret){
				ret = "";
			}
			return ret;
		}
	}
}
