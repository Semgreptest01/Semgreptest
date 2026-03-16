package xysk;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.Vector;

public class Xysk0220Renewday extends XysfljTableTag {
	//コンボBOXより選択
	
	// 表一行目
	public String Renewday = "";

 

	public Xysklj_02201_Params param = null;
	public XysfljTableTag tag = null;
	//-----表の項目数分作成----->
	public InputDataCol labelCol; // label

	//<-------------------------
	private Vector tableHeaders = new Vector();

	public Xysk0220Renewday(Xysklj_02201_Params p) {
		super("Xysk0220Renewday", p);
		param = p;

		dataRow = new DataRow();
		//-----表の項目数分作成----->
		labelCol = new InputDataCol("Label", 145, "A");

		addHeader(labelCol); // label

		//<-------------------------
		

	 

			try {
				getDbData(); // DB値取得
			}
			catch (XysfljDB.DBException e) {
				// データ取得でのDBエラー
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

			sql += " SELECT  to_char(作成日時,'YYYY/MM/DD') renewday   ";
			sql += "FROM UPC_新聞_コントロール ";
			sql += "WHERE 制御種別 = '01'";

			ResultSet rs = tdb.selectTable(sql); // SQL実行
			//SQLにて取得したレコード分tableElementsにセットする
			while ( rs.next()) {
				//表示の表要素作成
				InputPlan d = new InputPlan();
				d.setData(rs);
				tableElements.add(d);
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
		border = 0;
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
		tag.append("\t<!--THEAD END-->\r\n");
		tag.append("\t<TBODY>\r\n");
		return tag.toString();
	}
	
	
	public String createTag() throws NoDataException, SpanException {


			StringBuffer tag = new StringBuffer();
			tag.append(createStartTag());
			tag.append(createDataTag());
			tag.append(createEndTag());
			return XysfljGenericRules.exchangeOutString(tag);


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
			setData("");

		}

		public void setData(String labelCol) {
			// 空値をセット
			setLabelCol(labelCol);

		}

		public void setData(ResultSet rs) {
			try {

						 Renewday = rs.getString("renewday");
				  		setLabelCol("更新日：" + Renewday);

						}
	
			catch (SQLException e) {
				//エクセプション無視
			}
		}

		//Setter(表にて使用する項目分定義しておく)
		public void setLabelCol(String s) {
			put(labelCol.getName(), s);
		}

		//Getter(表にて使用する項目分定義しておく)
		public String getLabelCol() throws NoDataException {
			return get(labelCol.getName());
		}

	}
}
