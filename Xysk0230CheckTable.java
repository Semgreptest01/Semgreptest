package xysk;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.Vector;

public class Xysk0230CheckTable extends XysfljTableTag {
	//コンボBOXより選択


	public String select_publish  ;//選択された新聞社コード
	
	public String publishcom = "";

	public Xysklj_02301_Params param = null;
	public XysfljTableTag tag = null;
	//-----表の項目数分作成----->
	public InputDataCol labelCol; // label

	//<-------------------------
	private Vector tableHeaders = new Vector();
	private int ct = 0;	

	public Xysk0230CheckTable(Xysklj_02301_Params p) {
		super("Xysk0230CheckTable", p);
		param = p;
	 	ct = param.getCommandType();

		dataRow = new DataRow();
		//-----表の項目数分作成----->
		labelCol = new InputDataCol("Label", 250, "A");

		addHeader(labelCol); // label

		//<-------------------------
		
	//コンボBOXで選択された値をgetする

		 select_publish = param.getValue(XyskljNewsPUBLISHCOMCombo.NAME);//出版コード
	    //検索以外の場合は処理を行わない
		if ((Xysklj_02301_Params.COMMAND_REFERENCE == ct)  
			|| (Xysklj_02301_Params.COMMAND_DOWNLOAD == ct)) {
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
			XysfljDB tdb = param.getDbConnection();

			// SQL文作成<予算実績データ読込>
			String sql = "";				//SQL

			sql += "SELECT 新聞社名 ";
			sql += "FROM UPM_新聞社 ";
			sql += "WHERE 新聞社コード =" + select_publish ;

			ResultSet rs = tdb.selectTable(sql); // SQL実行
			//SQLにて取得したレコード分tableElementsにセットする
			if ( rs.next()) {
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

	public String createTag() throws NoDataException, SpanException {

	    //検索以外の場合は処理を行わない
		if (((Xysklj_02301_Params.COMMAND_REFERENCE == ct)||
			(Xysklj_02301_Params.COMMAND_DOWNLOAD == ct))	&&			
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
            	String strLabel =  (String)data.get("Label");
	            csv.add("新聞社：" + strLabel );
//	            csv.add((String)data.get("To"));

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
		public void setData2(String labelCol, String fromCol, String toCol) {
			// 空値をセット
			setLabelCol(labelCol);
		}


		public String dataFormat(long data) {
			DecimalFormat DFpercent = new DecimalFormat("#,##0.00");
			return String.valueOf(DFpercent.format(data));
		}


		public void setData(ResultSet rs) {
			try {
				// **** E10K移行時変更対応(2006/06/08) ***
				publishcom = Xysflj_Common.getStrCvt( rs.getString("新聞社名") );
//				publishcom = rs.getString("新聞社名");
				// **** END ******************************
				setLabelCol( publishcom );
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
