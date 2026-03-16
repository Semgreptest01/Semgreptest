/*
 *
 *  修正履歴
 *  L001  2006/06/08 K.Sasaki   文字化け対応 名称関連取得時にメソッドXysflj_Common.getStrCvt()を介す
 *  L002  2006/12/01 Y.Okamura  発売日２重表示対応
 *
 */
 package xysk;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.Vector;

public class Xysk0120CheckTable extends XysfljTableTag {
	//コンボBOXより選択

	public String select_Brandform ;//選択された銘柄
	public String select_Saledate ;//選択された発売日
	public String select_publish  ;//選択された出版社
	public String Saledate;//SQL比較用　選択した年月
	
	// 表一行目
	public long chk1_uriage_val = 0;
	public long chk2_uriage_val = 0;
	public String chk1_uriage_type = "";
	public String chk2_uriage_type = "";
	public String chk3_uriage_type = "";
	public String chk4_uriage_type = "";
	// 表二行目
	public long chk1_loss_val = 0;
	public long chk2_loss_val = 0;
	public String chk1_loss_type = "";
	public String chk2_loss_type = "";
	

	public Xysklj_01201_Params param = null;
	public XysfljTableTag tag = null;
	//-----表の項目数分作成----->
	public InputDataCol labelCol; // label
	public InputDataCol2 fromCol; // from
	public InputDataCol toCol; // to
	//<-------------------------
	private Vector tableHeaders = new Vector();
	private int ct = 0;	
//L002 ADDS
	private String strSaledate2 = "";
	private String strJancode2 = "";
	private String Zflg2 = "";
//L002 ADDE
	
	public Xysk0120CheckTable(Xysklj_01201_Params p) {
		super("Xysk0120CheckTable", p);
		param = p;
	 	ct = param.getCommandType();


		dataRow = new DataRow();
		//-----表の項目数分作成----->
		labelCol = new InputDataCol("Label", 145, "A");
		fromCol = new InputDataCol2("From", 135, "B");
		toCol = new InputDataCol("To", 115, "C");
		addHeader(labelCol); // label
		addHeader(fromCol); // from
		addHeader(toCol); // to
		//<-------------------------
		
	//コンボBOXで選択された値をgetする

		 select_Brandform = param.getValue(XyskljPUBLISHBRANDCombo.NAME);
		 select_Saledate = param.getValue(XyskljPUBLISHSALEDATECombo.NAME);
		 select_publish = param.getValue(XyskljPUBLISHCOMCombo.NAME);
	    //検索以外の場合は処理を行わない
		if ((Xysklj_01201_Params.COMMAND_REFERENCE == ct)  ||
			(Xysklj_01201_Params.COMMAND_DOWNLOAD == ct)){
			try {
				getDbData(); // DB値取得
			}
			catch (XysfljDB.DBException e) {
				// データ取得でのDBエラー
			}
		}
	}

	private void getDbData() throws XysfljDB.DBException {
		getTotalDataFromDB(); // ComboBoxの選択値でSQL文振分け
	}

	public Vector getDetailTableData() {
		return tableElements;
	}

	public void getTotalDataFromDB() throws XysfljDB.DBException {
		try {
//L002 CHGS
			strSaledate2 = select_Saledate.substring(0, 10);
			Zflg2 = "";
			if(select_Saledate.length() > 10){
				strJancode2  = select_Saledate.substring(12, 17);
				strJancode2  = strJancode2 + select_Saledate.substring(18);
			Zflg2 = "1";
			}
//L002 CHGE
			
//          XysfljDB tdb = XysfljDB.getInstance();
			XysfljDB tdb = param.getDbConnection();

			// SQL文作成<予算実績データ読込>
			String sql = "";				//SQL

			sql += " SELECT  to_char(A.発売日,'YYYY/MM/DD') saledate   ";
			sql += ",A.銘柄コード ";
			sql += ",A.銘柄名 ";
			sql += ",A.ＪＡＮコード ";
			sql += ",A.出版社コード ";
			sql += ",B.出版社名 ";
			sql += "FROM UPM_出版社別銘柄 A ";
			sql += ",UPM_出版社 B ";
			sql += "WHERE A.出版社コード = B.出版社コード ";
			sql += " AND A.出版社コード =" + select_publish  ;
			sql += " AND A.銘柄コード =" + select_Brandform  ;
//L002 CHGS
//			sql += " AND to_char(A.発売日,'YYYY/MM/DD') = '" + select_Saledate + "'" ;	//テスト用 後で修正
			sql += " AND to_char(A.発売日,'YYYY/MM/DD') = '" + strSaledate2 + "'" ;	//テスト用 後で修正
			if(Zflg2.equals("1")){
			sql += " AND substr(A.ＪＡＮコード, 5,7) = '" + strJancode2 + "'" ;
			}
//L002 CHGS

			ResultSet rs = tdb.selectTable(sql); // SQL実行
			//SQLにて取得したレコード分tableElementsにセットする
			while ( rs.next()) {
				//表示の表要素作成

					InputPlan d = new InputPlan();
					InputPlan d2 = new InputPlan();

					d.setData(rs);
					d2.setData2(rs);
					tableElements.add(d);
					tableElements.add(d2);

			}
			rs.close();

		}
		catch (SQLException e) {
			//rs.nextのエラー
			throw new XysfljDB.ResultAnalyzeException(e);
		}
	}

	private class DataRow extends LinePatern {
		public DataRow() {
			super("データ", 50);
		}
	}

	public String createStartTagWithFrame(String f) {
		StringBuffer tag = new StringBuffer();
		border = 1;
		tag.append("\r\n");
		tag.append("<TABLE");
		tag.append(" border=\"" + border + "\"");
		tag.append(" cellpadding=\"" + cellpadding + "\"");
		tag.append(" cellspacing=\"" + cellspacing + "\"");
		tag.append(" bgcolor=\"" + bgcolor + "\"");
		tag.append(getStyle());
		if (false == f.equals("")) {
			tag.append(" FRAME=\"" + f + "\"");
		}
		tag.append("\t<!--THEAD START-->\r\n");
		Enumeration headers = tableHeaders.elements();
		tag.append("\t<COL width=\"145\">\r\n");
		tag.append("\t<COL width=\"135\" >\r\n");
		tag.append("\t<COL width=\"115\" >\r\n");
		tag.append("\t<!--THEAD END-->\r\n");
		tag.append("\t<TBODY>\r\n");
		return tag.toString();
	}
	

	public String createTag() throws NoDataException, SpanException {

	    //検索以外の場合は処理を行わない
		if (((Xysklj_01201_Params.COMMAND_REFERENCE == ct)  ||
			(Xysklj_01201_Params.COMMAND_DOWNLOAD == ct))	&&			
			(param.NO_DATA_FLG == param.COMMAND_DATA)){			    
			StringBuffer tag = new StringBuffer();
			tag.append(createStartTag());
			//		tag.append(createHeadTag());
			tag.append(createDataTag());
			tag.append(createEndTag());
			return XysfljGenericRules.exchangeOutString(tag);
		}
		else{
			return "";
		}

	}


	public Vector csvdata(){
        Vector csv = new Vector();
		int intcnt = 0;
		try{
			Enumeration records = tableElements.elements();
			while(records.hasMoreElements()){
				TableData data = (TableData)records.nextElement();
				Enumeration column = tableHeaders.elements();
				if (intcnt == 0 ) {
	            	csv.add((String)data.get("Label"));
	            	String strfrom =  (String)data.get("From");
	            	csv.add("出版社：" + strfrom.substring(0,6) + " " + strfrom.substring(7));
	            	csv.add((String)data.get("To"));
				} else {
	            	csv.add((String)data.get("Label") + (String)data.get("From"));
				}
				intcnt++;
			}
	  	}
	  	catch(Exception e){
	  	}
		return csv;
	}	


	

	public class InputDataCol extends DataCol {
		final private String COL_ID;

		public String editValueWithFontTag(String val) {
			return val;
		}

		public InputDataCol(String s, int n, String t) {
			super(s, n);
			COL_ID = t;
		}
	}

	public class InputDataCol2 extends DataCol {
		final private String COL_ID;

		public String editValueWithFontTag(String val) {
			return val;
		}

		public InputDataCol2(String s, int n, String t) {
			super(s, n);
			COL_ID = t;
		}
		public String getOptionTag(String value){
			try{
				int l = getCursor();
				if(1 == l){
					dataRow.setColspan(2);
					return " colspan=\"2\"";
				}
				else{
					return "";
				}
			}
			catch(SpanException e){
				return "";
			}
		}
	}

	public class DataCol extends LinePatern {
		public DataCol(String s, int i) {
			super(s, i);
		}
	}

	public class InputPlan extends TableData {
		public InputPlan() {
			// 表に合わせ定義
			setData("", "", "");
			setData2("", "", "");
		}

		public void setData(String labelCol, String fromCol, String toCol) {
			// 空値をセット
			setLabelCol(labelCol);
			setFromCol(fromCol);
			setToCol(toCol);
		}
		public void setData2(String labelCol, String fromCol, String toCol) {
			// 空値をセット
			setLabelCol(labelCol);
			setFromCol(fromCol);
			setToCol(toCol);
		}


		public String dataFormat(long data) {
			DecimalFormat DFpercent = new DecimalFormat("#,##0.00");
			return String.valueOf(DFpercent.format(data));
		}


		public void setData(ResultSet rs) {
			try {
						// １行目
						chk1_uriage_type = rs.getString("ＪＡＮコード");
						chk2_uriage_type = rs.getString("出版社コード");
						// **** E10K移行時変更対応(2006/06/08) ***
						chk3_uriage_type = Xysflj_Common.getStrCvt( rs.getString("出版社名") );
//						chk3_uriage_type = rs.getString("出版社名");
						// **** END ******************************


						setLabelCol("ＪＡＮコード：" + chk1_uriage_type );
						setFromCol( chk2_uriage_type + "："+ chk3_uriage_type );
//L002 CHGS
//						setToCol("発売日：" + select_Saledate );
						setToCol("発売日：" + strSaledate2 );
//L002 CHGE

						}

			catch (SQLException e) {
				//エクセプション無視
			}
		}
		public void setData2(ResultSet rs) {
			try {

						// ２行目
						chk1_loss_type = rs.getString("銘柄コード");
						// **** E10K移行時変更対応(2006/06/08) ***
						chk2_loss_type = Xysflj_Common.getStrCvt( rs.getString("銘柄名") );
//						chk2_loss_type = rs.getString("銘柄名");
						// **** END ******************************


						setLabelCol("銘柄コード：" + chk1_loss_type );
						// **** E10K移行時変更対応(2006/06/08) ***
						setFromCol( Xysflj_Common.getStrCvt( rs.getString("銘柄名") ));
//						setFromCol(rs.getString("銘柄名"));
						// **** END ******************************
						setToCol(rs.getString(""));

						}
	//		}
			catch (SQLException e) {
				//エクセプション無視
			}
		}
		//Setter(表にて使用する項目分定義しておく)
		public void setLabelCol(String s) {
			put(labelCol.getName(), s);
		}
		public void setFromCol(String s) {
			put(fromCol.getName(), s);
		}
		public void setToCol(String s) {
			put(toCol.getName(), s);
		}

		//Getter(表にて使用する項目分定義しておく)
		public String getLabelCol() throws NoDataException {
			return get(labelCol.getName());
		}
		public String getFromCol() throws NoDataException {
			return get(fromCol.getName());
		}
		public String getToCol() throws NoDataException {
			return get(toCol.getName());
		}
	}
}
