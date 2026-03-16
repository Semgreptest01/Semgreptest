package xysk;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.Vector;

public class Xysk0220CheckTable extends XysfljTableTag {
	//コンボBOXより選択

	public String select_Productform ;//選択された商品
	public String select_publish  ;//選択された新聞社
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
	

	public Xysklj_02201_Params param = null;
	public XysfljTableTag tag = null;
	//-----表の項目数分作成----->
	public InputDataCol labelCol; // label
	public InputDataCol2 fromCol; // from
	public InputDataCol toCol; // to
	//<-------------------------
	private Vector tableHeaders = new Vector();
	private int ct = 0;	

	public Xysk0220CheckTable(Xysklj_02201_Params p) {
		super("Xysk0220CheckTable", p);
		param = p;
	 	ct = param.getCommandType();


		dataRow = new DataRow();
		//-----表の項目数分作成----->
		labelCol = new InputDataCol("Label", 145, "A");
		fromCol = new InputDataCol2("From", 135, "B");
		toCol = new InputDataCol("To", 115, "C");
		addHeader(labelCol); // label
		addHeader(fromCol); // from
		//<-------------------------
		
	//コンボBOXで選択された値をgetする

		 select_Productform = param.getValue(XyskljNewsPublishProductCombo.NAME);
		 select_publish = param.getValue(XyskljNewsPUBLISHCOMCombo.NAME);
	    //検索以外の場合は処理を行わない
		if ((Xysklj_02201_Params.COMMAND_REFERENCE == ct)  ||
			(Xysklj_02201_Params.COMMAND_DOWNLOAD == ct)){
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
			//          XysfljDB tdb = XysfljDB.getInstance();
			XysfljDB tdb = param.getDbConnection();

			// SQL文作成<予算実績データ読込>
			String sql = "";				//SQL

			sql += " SELECT  to_char(A.発売日,'YYYY/MM/DD') saledate   ";
			sql += ",A.商品コード ";
			sql += ",A.商品名 ";
			sql += ",A.ＪＡＮコード ";
			sql += ",A.新聞社コード ";
			sql += ",B.新聞社名 ";
			sql += "FROM UPM_新聞社別商品 A ";
			sql += ",UPM_新聞社 B ";
			sql += "WHERE A.新聞社コード = B.新聞社コード ";
			sql += " AND A.新聞社コード =" + select_publish  ;
			sql += " AND A.商品コード =" + select_Productform  ;

			ResultSet rs = tdb.selectTable(sql); // SQL実行
			//SQLにて取得したレコード分tableElementsにセットする
			if ( rs.next()) {
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
		tag.append(">\r\n");
		tag.append("\t<!--THEAD START-->\r\n");
		Enumeration headers = tableHeaders.elements();
		tag.append("\t<COL width=\"145\">\r\n");
		tag.append("\t<COL width=\"200\" >\r\n");
		tag.append("\t<!--THEAD END-->\r\n");
		tag.append("\t<TBODY>\r\n");
		return tag.toString();
	}
	

	public String createTag() throws NoDataException, SpanException {

	    //検索以外の場合は処理を行わない
		if (((Xysklj_02201_Params.COMMAND_REFERENCE == ct)  ||
			(Xysklj_02201_Params.COMMAND_DOWNLOAD == ct))	&&			
			(param.NO_DATA_FLG == param.COMMAND_DATA)){			    
			StringBuffer tag = new StringBuffer();
			tag.append(createStartTag());
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
	            	csv.add("新聞社：" + (String)data.get("From"));
				} else {
	            	csv.add((String)data.get("Label") + " " + (String)data.get("From"));
				}
				intcnt++;
			}
	  	} catch(Exception e){
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
				// **** E10K移行時変更対応(2006/06/08) ***
				chk2_uriage_type = Xysflj_Common.getStrCvt( rs.getString("新聞社名") );
//				chk2_uriage_type = rs.getString("新聞社名");
				// **** END ******************************
				setLabelCol("ＪＡＮコード：" + chk1_uriage_type );
				setFromCol( chk2_uriage_type);
			} catch (SQLException e) {
				//エクセプション無視
			}
		}
		public void setData2(ResultSet rs) {
			try {

				// ２行目
				chk1_loss_type = rs.getString("商品コード");
				// **** E10K移行時変更対応(2006/06/08) ***
				chk2_loss_type = Xysflj_Common.getStrCvt( rs.getString("商品名") );
//				chk2_loss_type = rs.getString("商品名");
				// **** END ******************************
				setLabelCol("商品コード：" + chk1_loss_type );
				setFromCol(chk2_loss_type);

			} catch (SQLException e) {
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
