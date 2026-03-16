package xysk;

import java.sql.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Vector;
import java.util.Enumeration;

/**
 * @author user
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class XyskljNewsPUBLISHCOMCombo extends XysfljCommonCombo{
	final static public String NAME = "select_publishcom";
	final static public int TYPE = XysfljParameters.ESSENTIAL;

	public XyskljNewsPUBLISHCOMCombo (XysfljParameters p){
		super(p, NAME);
		setOnChange("change_publishcom(document.form1);button_sts_chg();news_publishcom_product_search(1);");
		setStyle("width : 240px;");

		comboElements.put("000000","");

		try{
			String inout = null;
			String p_count = null;
			inout = param.getRequestParameter("inout");
			String sql = "";
			if ( inout == null || inout.equals("") || inout.equals("0") ) {
				sql += " SELECT   新聞社コード publishcom_cd ";
				sql += "         ,新聞社名 publishcom_nn ";
				sql += " FROM     UPM_新聞社          ";
				sql += " GROUP BY 新聞社カナ          ";
				sql += "         ,新聞社コード          ";
				sql += "         ,新聞社名           ";
				sql += " ORDER BY 新聞社カナ              ";
				sql += "         ,新聞社コード              ";
				getData(sql,"publishcom_cd","publishcom_nn");
			} else {
				Vector TorihikiCD = param.getLoginUserTorihikisakiCD();
				for( int i=0; i<= (TorihikiCD.size()-1); i++ ) {
				sql =	"SELECT A.新聞社コード publishcom_cd "
					+			",B.新聞社名 publishcom_nn "
					+	" FROM UPT_利用者_CTL A,"
					+		" UPM_新聞社 B"
					+	" WHERE A.取引先コード ='" + TorihikiCD.elementAt(i) + "'"
					+		" AND A.種別区分 ='" + TorihikiCD.elementAt(i+1) + "'"
					+		" AND A.新聞社コード = B.新聞社コード "
					+	" ORDER BY B.新聞社カナ, B.新聞社コード";
				getData(sql,"publishcom_cd","publishcom_nn");
				i=i+1;
				}
// 「データが見つかりません!!」のスマートなやり方↓
				Enumeration records = comboElements.elements();
				while(records.hasMoreElements()){
					p_count =  records.nextElement().toString();
				}
				if(p_count== null || p_count == "000000" || p_count == ""){
					param.addMessageByCode(5);
				}
/* 「データが見つかりません!!」の技術者にわかりやすいやり方↓
(int t = 0;　Vector p_count1 = new Vector();　を宣言して使うこと)

				Enumeration records = comboElements.elements();
				while(records.hasMoreElements()){
					p_count =  records.nextElement().toString();
				System.out.println("p_count=" + p_count);
					p_count1.addElement(p_count);
				System.out.println("p_count1=" + p_count1);
				System.out.println("t=" + t);
					t++;
				System.out.println("t=" + t);
				}		
				System.out.println("p_count2=" + p_count1.elementAt(t-1));
				System.out.println("p_count3=" + p_count1.size());
				if(p_count1.size() == 0 || p_count1.elementAt(t-1) == "000000" || p_count1.elementAt(t-1) == ""){
				System.out.println("???????????????????????");
					param.addMessageByCode(5);
				}
*/

/*				sql =	"SELECT A.新聞社コード publishcom_cd "
					+			",B.新聞社名 publishcom_nn "
					+	" FROM UPT_利用者_CTL A,"
					+		" UPM_新聞社 B"
					+	" WHERE A.取引先コード ='" + TorihikisakiCD + "'"
					+		" AND A.新聞社コード = B.新聞社コード "
					+	" ORDER BY B.新聞社カナ, B.新聞社コード";
*/
			}
//			getData(sql,"publishcom_cd","publishcom_nn");
		}
		catch(XysfljDB.DBException e){
			//データ取得でのDBエラー
			param.addMessageByCode(1000);
			param.executeErrorRoutine(e);
		}
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
//			if (  comboElements.size() < 3 ) {
//				tag.append(" disabled");
//			}
			tag.append(">\r\n");

/*			if (  comboElements.size() < 3 ) {
				int i=0;
				String value = "";
				Enumeration records = comboElements.elements();
				while(records.hasMoreElements()){
					String key = records.nextElement().toString();
					tag.append("\t<OPTION value=\"" + key + "\"");
					if( 1 == i++ ){
						tag.append(" selected");
						value = key;
					}
					tag.append(">");
					tag.append(comboElements.getName(key) + "\r\n");
				}
//				tag.append("<INPUT type =\"hidden\" name=\"" + CONTROL_NAME
//												 + "\" value=\"" + value + "\">");
			} else {*/
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
//			}

			tag.append("</SELECT>\r\n");
		}
		tag.append(createInitialParamTag());
		return XysfljGenericRules.exchangeOutString(tag);
	}

}

