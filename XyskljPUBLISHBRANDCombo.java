package xysk;

import javax.servlet.http.*;
import java.util.Vector;
import java.util.Enumeration;
import java.sql.ResultSet;
import java.sql.SQLException;

public class XyskljPUBLISHBRANDCombo extends XysfljCommonCombo{
	static public String NAME = "select_brand";
	public XyskljPUBLISHBRANDCombo(XysfljParameters p){
		super(p,NAME);
		// 2003/12/18 miura:発売日ｸﾘｱとボタン制御を追加
		setOnChange("clear3(document.form1);brand_saledate_search(1);");
		setStyle("width : 340px;");

		comboElements.put("000000","");
		try{
			String inout = null;
			inout = param.getRequestParameter("inout");
			String selectedPublishcom = param.getValue(XyskljPUBLISHCOMCombo.NAME);
			String sql = "";
			if ( inout == null || inout.equals("") || inout.equals("0") ) {
//					System.out.println("ローソンユーザー =" + inout);
					sql = " SELECT  銘柄コード brancd_cd         ";
					sql += "        ,銘柄名 brancd_nn             ";
					sql += " FROM    UPM_出版社別銘柄              ";
					sql += " WHERE   出版社コード = '" + selectedPublishcom + "' ";
					sql += " GROUP BY  銘柄コード            ";
					sql += "        ,銘柄名            ";
					getDataCodeName(sql,"brancd_cd","brancd_nn");

			} else {
				Vector TorihikiCD = param.getLoginUserTorihikisakiCD();
				for( int i=0; i<= (TorihikiCD.size()-1); i++ ) {

					if ( TorihikiCD.elementAt(i+1) == null || TorihikiCD.elementAt(i+1).equals("") || TorihikiCD.elementAt(i+1).equals("1") ) {
//					System.out.println("種別1 = " + TorihikiCD.elementAt(i+1));
						sql = " SELECT  銘柄コード brancd_cd         ";
						sql += "        ,銘柄名 brancd_nn             ";
						sql += " FROM    UPM_出版社別銘柄              ";
						sql += " WHERE   出版社コード = '" + selectedPublishcom + "' ";
						sql += " GROUP BY  銘柄コード            ";
						sql += "        ,銘柄名            ";
						getDataCodeName(sql,"brancd_cd","brancd_nn");
					}else{
//					System.out.println("種別2 = " + TorihikiCD.elementAt(i+1));
//						System.out.println("XyskljPUBLISHBRANDCombo=" + TorihikiCD.elementAt(i));
						sql = " SELECT A.商品コード brancd_cd ";
						sql += "      , B.銘柄名 brancd_nn ";
						sql += " FROM   UPT_取引先_商品 A ";
						sql += "      , UPM_出版社別銘柄 B ";
						sql += " WHERE A.取引先コード='" + TorihikiCD.elementAt(i) + "'";
						sql += "   AND B.出版社コード = '" + selectedPublishcom + "' ";
//						System.out.println(selectedPublishcom);
						sql += "   AND A.商品コード =B.銘柄コード ";
						sql += " GROUP BY  A.商品コード ";
						sql += "         , B.銘柄名 ";
//						System.out.println("1回目 = " + i);
						getDataCodeName(sql,"brancd_cd","brancd_nn");
//						System.out.println("2回目 = " + i);
					}
					i=i+1;
				}
			}

/*		try{
			String selectedPublishcom = param.getValue(XyskljPUBLISHCOMCombo.NAME);
			String sql = "";
			sql += " SELECT  銘柄コード brancd_cd         ";
			sql += "        ,銘柄名 brancd_nn             ";
			sql += " FROM    UPM_出版社別銘柄              ";
			sql += " WHERE   出版社コード = '" + selectedPublishcom + "' ";
			sql += " GROUP BY  銘柄コード            ";
			sql += "        ,銘柄名            ";
			getDataCodeName(sql,"brancd_cd","brancd_nn"); */
		}
		catch(XysfljDB.DBException e){
			//データ取得でのDBエラー
			param.addMessageByCode(1000);
			param.executeErrorRoutine(e);
		}
		//comboElements.put("","その他");
	}
}
