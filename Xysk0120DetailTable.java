 /*
 *
 *  修正履歴
 *  L001  2006/07/04 K.Sasaki   SQLのチューニング
 *  L002  2006/12/01 Y.Okamura  発売日２重表示対応
 */
 package xysk;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;


public class Xysk0120DetailTable extends XysfljTableTag {

	private int cnt = 0;
	public int maxday; //最大経過日
	private int rowspan; //rowspan設定用 連結させる行の数
	private int index[] = new int[3]; //入力された項目
	//　index[]={x,y,z};
	/*
	 * index[] 
	 * 1:仕入数
	 * 2:販売数
	 * 3:販売率
	 * 4:配本数
	 * 5:売切店数
	 * 6:売切店発生率
	 * 7:返品数
	 * 8:欠品数
	 * 9:仕入PSA
	 * 10:販売PSA 
	 */
	private int all; //項目コンボBOXで選択された場合
	private int line1; //コンボBOXで１行目に選択されたもの
	private int line2; //コンボBOXで２行目に選択されたもの
	private int line3; //コンボBOXで３行目に選択されたもの

	private InputDataCol kencdCol;
	private InputDataCol kennmCol;
	private InputDataCol saledateCol;
	private InputDataCol titleCol;
	private InputDataCol sumCol;
	private InputDataCol day1Col;
	private Xysklj_01201_Params param;
	private DetailTableData totalData = null;
//	private int cursor = 0;

	public String select_Brandform; //選択された銘柄
	public String select_Saledate; //選択された発売日
	public String select_publish; //選択された出版社
	private String ken_cd = ""; //県コード

	public String allitem; //項目コンボで選択された値 0:項目選択　1:前項目
	public String selectline1; //1行目コンボで選択された値
	public String selectline2; //2行目コンボで選択された値
	public String selectline3; //3行目コンボで選択された値
	private int ct = 0;
//L002 CHGS
//  private int intSaledate = 0;
    private String strSaledate = "";
    private String strJancode = "";
    private String Zflg = "";
//L002 CHGE
    private int intSaledate = 0;

	public Xysk0120DetailTable(Xysklj_01201_Params p) {
		super("Xysk0120DetailTable", p);
		param = p;
		int st = param.getStatus();
		ct = param.getCommandType();

		dataRow = new DataRow();
		//県コードから合計までのヘッダー
		kencdCol = new InputDataCol("県コード", 76, "A");
		kennmCol = new InputDataCol("県名", 76, "B");
		saledateCol = new InputDataCol("発売日", 76, "C");
		titleCol = new InputDataCol("", 76, "D");
		sumCol = new InputDataCol("合計", 76, "E");

		addHeader(kencdCol);
		addHeader(kennmCol);
		addHeader(saledateCol);
		addHeader(titleCol);
		addHeader(sumCol);

		//コンボBOXで選択された値をgetする
		select_Brandform = param.getValue(XyskljPUBLISHBRANDCombo.NAME);
		select_Saledate = param.getValue(XyskljPUBLISHSALEDATECombo.NAME);
		select_publish = param.getValue(XyskljPUBLISHCOMCombo.NAME);

		//検索以外の場合は処理を行わない
		if ((Xysklj_01201_Params.COMMAND_REFERENCE == ct)
			|| (Xysklj_01201_Params.COMMAND_DOWNLOAD == ct)) {
			try {
				//発売日を数値型に変換
//L002 CHGS
//				intSaledate = Integer.parseInt(select_Saledate.substring(0, 4)) * 10000;
//				intSaledate = intSaledate + (Integer.parseInt(select_Saledate.substring(5, 7)) * 100);
//				intSaledate = intSaledate + (Integer.parseInt(select_Saledate.substring(8)) * 1);
				strSaledate = select_Saledate.substring(0, 4);
				strSaledate = strSaledate + select_Saledate.substring(5, 7);
				strSaledate = strSaledate + select_Saledate.substring(8, 10);
				Zflg = "";
				if(select_Saledate.length() > 10){
					strJancode  = select_Saledate.substring(12, 17);
					strJancode  = strJancode + select_Saledate.substring(18);
				Zflg = "1";
				}
//L002 CHGE
				//DBからの値を取得
				getDbData();
			} catch (XysfljDB.DBException e) {
				//データ取得でのDBエラー
				param.addMessageByCode(1000);
				param.executeErrorRoutine(e);
			}
		}

	}

	private void getDbData() throws XysfljDB.DBException {
		//ここから
		param = param;
		allitem = param.getRequestParameter("allitem");
		selectline1 = param.getRequestParameter("selectline1");
		selectline2 = param.getRequestParameter("selectline2");
		selectline3 = param.getRequestParameter("selectline3");

		all = Integer.valueOf(allitem).intValue();
		line1 = Integer.valueOf(selectline1).intValue();
		line2 = Integer.valueOf(selectline2).intValue();
		line3 = Integer.valueOf(selectline3).intValue();

		if (all != 0 || line1 != 0) {
			//全項目選択の場合
			if (all == 1) {
				line1 = 11;
				rowspan = 10; //連結するrowspan
			} else if (all == 0) {
				//1行目のみ選択
				if (line1 != 0 && line2 == 0 && line3 == 0) {
					rowspan = 1; //連結するrowspan
				}
				//2行目のみ選択
				else if (line1 != 0 && line2 != 0 && line3 == 0) {
					rowspan = 2; //連結するrowspan

				} //3行目のみ選択
				else if (line1 != 0 && line2 != 0 && line3 != 0) {
					rowspan = 3; //連結するrowspan

				}
			}
		} else {
			//項目がひとつも選択されていない状態	
		}

		getTotalDataFromDB();
	}

	public Vector getDetailTableData() {
		return tableElements;
	}

	public DetailTableData getTotalData() {
		return totalData;
	}

	public void getTotalDataFromDB() throws XysfljDB.DBException {
		try {

			XysfljDB tdb = param.getDbConnection();

			// MAX経過日数
			String sqlMax = ""; //SQL
			sqlMax += "SELECT ";
			sqlMax += "MAX(A.経過日数) AS MAXDATE ";
			sqlMax += "FROM UPT_形態別販売実績 A";
			sqlMax += "    ,UPM_出版社別銘柄 B   ";
//			sqlMax += "WHERE A.銘柄コード =" + select_Brandform ;
//	*********** E10K移行時追加 06/07/04 START *******************************
//			sqlMax += "WHERE B.銘柄コード =" + select_Brandform ;
			sqlMax += "WHERE B.銘柄コード = '" + select_Brandform + "'";
//	************************** 06/07/04  END  *******************************
// 
			sqlMax += " AND B.発売日 = to_date("
//L002 CHGS
//				+ intSaledate
				+ strSaledate
				+ ",'YYYYMMDD')";
			sqlMax += " AND A.銘柄コード = B.銘柄コード ";
			sqlMax += " AND A.ＪＡＮコード = B.ＪＡＮコード ";
			if(Zflg.equals("1")){
			sqlMax += " AND substr(B.ＪＡＮコード, 5,7) = '"
				+ strJancode 
				+ "'";
			}
//L002 CHGE
//			sqlMax += " AND to_char(B.発売日,'YYYY/MM/DD') = '" + select_Saledate + "'" ;
				
			ResultSet rsMax = tdb.selectTable(sqlMax);
			//MAX（最大）経過日数の取得
			while ( rsMax.next()) {
				maxday = rsMax.getInt("MAXDATE");
				//データが存在した場合は表示する
				if (rsMax.getString("MAXDATE") != null) {
					param.NO_DATA_FLG = param.COMMAND_DATA;
				} else {
					//データが存在しない場合はメッセージ出力	
					param.addMessageByCode(5);
				}
			}
			rsMax.close();

			//1日目から最大経過日数までのヘッダー 
			char a;
			a = 'E';
			cnt = 0;

			while (cnt < maxday) {
				cnt++;
				String day = Integer.toString(cnt);

				day1Col = new InputDataCol(day + "日目", 76, String.valueOf(a++));
				addHeader(day1Col);
			}

			String sql = ""; //SQL

			sql += "SELECT A.出版社コード ";
			sql += ",A.県コード ";
			sql += ",A.県名 ";
			sql += ",A.銘柄コード ";
			sql += ",to_char(A.発売日,'YYYY/MM/DD') saledate   ";
			sql += ",A.経過日数 ";
			sql += ",A.仕入数 ";
			sql += ",A.販売数 ";
			sql += ",A.販売率 ";
			sql += ",A.配本店 ";
			sql += ",A.売切店数 ";
			sql += ",A.売切店発生率 ";
			sql += ",A.返品数 ";
			sql += ",A.欠品数 ";
			sql += ",A.仕入ＰＳＡ ";
			sql += ",A.販売ＰＳＡ ";
			sql += "FROM UPT_形態別販売実績 A ";
			sql += "    ,UPM_出版社別銘柄 B   ";
//	*********** E10K移行時追加 06/07/04 START *******************************
//			sql += "WHERE A.銘柄コード =" + select_Brandform;
			sql += "WHERE A.銘柄コード = '" + select_Brandform + "'";
//	************************** 06/07/04  END  *******************************
//L002 CHGS
//			sql += " AND to_char(B.発売日,'YYYY/MM/DD') = '" + select_Saledate + "'";
			sql += " AND to_char(B.発売日,'YYYYMMDD') = '"
				+ strSaledate
				+ "'";
			sql += " AND A.銘柄コード = B.銘柄コード ";
			sql += " AND A.ＪＡＮコード = B.ＪＡＮコード ";
			if(Zflg.equals("1")){
			sql += " AND substr(B.ＪＡＮコード, 5,7) = '"
				+ strJancode 
				+ "'";
			}
//L002 CHGE
			sql += " ORDER BY A.県コード ASC ";
			sql += " ,A.経過日数 ASC ";

			ResultSet rs = tdb.selectTable(sql);

			index[0] = line1;
			index[1] = line2;
			index[2] = line3;

			DetailTableData d = new DetailTableData();
			DetailTableData d2 = new DetailTableData();
			DetailTableData d3 = new DetailTableData();
			DetailTableData d4 = new DetailTableData();
			DetailTableData d5 = new DetailTableData();
			DetailTableData d6 = new DetailTableData();
			DetailTableData d7 = new DetailTableData();
			DetailTableData d8 = new DetailTableData();
			DetailTableData d9 = new DetailTableData();
			DetailTableData d10 = new DetailTableData();

			long cnt = 0;
			
			//パフォーマンスチューニング
			rs.setFetchSize(1000);

			while ( rs.next()) {

				//先頭データのみセットする
				if (!(ken_cd.equals(rs.getString("県コード")))) {
					cnt = 0;

					//最初の場合はaddしない。県コードが切り替わったときaddする。
					if (!(ken_cd.equals(""))) {

						//全項目選択時	
						if (index[0] == 11) {
							tableElements.add(d);
							tableElements.add(d2);
							tableElements.add(d3);
							tableElements.add(d4);
							tableElements.add(d5);
							tableElements.add(d6);
							tableElements.add(d7);
							tableElements.add(d8);
							tableElements.add(d9);
							tableElements.add(d10);
							//選択した項目のみ
						} else {
							for (int i = 0; i < 3; i++) {
								if (index[i] == 1) {
									tableElements.add(d);
								}
								if (index[i] == 2) {
									tableElements.add(d2);
								}
								if (index[i] == 3) {
									tableElements.add(d3);
								}
								if (index[i] == 4) {
									tableElements.add(d4);
								}
								if (index[i] == 5) {
									tableElements.add(d5);
								}
								if (index[i] == 6) {
									tableElements.add(d6);
								}
								if (index[i] == 7) {
									tableElements.add(d7);
								}
								if (index[i] == 8) {
									tableElements.add(d8);
								}
								if (index[i] == 9) {
									tableElements.add(d9);
								}
								if (index[i] == 10) {
									tableElements.add(d10);
								}
							}

						}
					}

					d = new DetailTableData();
					d2 = new DetailTableData();
					d3 = new DetailTableData();
					d4 = new DetailTableData();
					d5 = new DetailTableData();
					d6 = new DetailTableData();
					d7 = new DetailTableData();
					d8 = new DetailTableData();
					d9 = new DetailTableData();
					d10 = new DetailTableData();
					//全項目取得時
					if (index[0] == 11) {
						d.setData(rs);
						d2.setData2(rs);
						d3.setData3(rs);
						d4.setData4(rs);
						d5.setData5(rs);
						d6.setData6(rs);
						d7.setData7(rs);
						d8.setData8(rs);
						d9.setData9(rs);
						d10.setData10(rs);
						//選択した項目のみセット
					} else {
						for (int i = 0; i < 3; i++) {
							if (index[i] == 1) {
								d.setData(rs);
							}
							if (index[i] == 2) {
								d2.setData2(rs);
							}
							if (index[i] == 3) {
								d3.setData3(rs);
							}
							if (index[i] == 4) {
								d4.setData4(rs);
							}
							if (index[i] == 5) {
								d5.setData5(rs);
							}
							if (index[i] == 6) {
								d6.setData6(rs);
							}
							if (index[i] == 7) {
								d7.setData7(rs);
							}
							if (index[i] == 8) {
								d8.setData8(rs);
							}
							if (index[i] == 9) {
								d9.setData9(rs);
							}
							if (index[i] == 10) {
								d10.setData10(rs);
							}
						}
					}
					ken_cd = rs.getString("県コード");
				}
				//同一県コードの場合は同一行にセットする

				if (ken_cd.equals(rs.getString("県コード"))) {

					//全項目取得時
					if (index[0] == 11) {
						d.setData(rs, cnt);
						d2.setData2(rs, cnt);
						d3.setData3(rs, cnt);
						d4.setData4(rs, cnt);
						d5.setData5(rs, cnt);
						d6.setData6(rs, cnt);
						d7.setData7(rs, cnt);
						d8.setData8(rs, cnt);
						d9.setData9(rs, cnt);
						d10.setData10(rs, cnt);

						cnt++;
						//選択した項目のみセット
					} else {
						for (int i = 0; i < 3; i++) {
							if (index[i] == 1) {
								d.setData(rs, cnt);
							}
							if (index[i] == 2) {
								d2.setData2(rs, cnt);
							}
							if (index[i] == 3) {
								d3.setData3(rs, cnt);
							}
							if (index[i] == 4) {
								d4.setData4(rs, cnt);
							}
							if (index[i] == 5) {
								d5.setData5(rs, cnt);
							}
							if (index[i] == 6) {
								d6.setData6(rs, cnt);
							}
							if (index[i] == 7) {
								d7.setData7(rs, cnt);
							}
							if (index[i] == 8) {
								d8.setData8(rs, cnt);
							}
							if (index[i] == 9) {
								d9.setData9(rs, cnt);
							}
							if (index[i] == 10) {
								d10.setData10(rs, cnt);
							}

						}
						cnt++;
					}
				}
			}
			rs.close();

			//全項目取得時	
			if (index[0] == 11) {
				tableElements.add(d);
				tableElements.add(d2);
				tableElements.add(d3);
				tableElements.add(d4);
				tableElements.add(d5);
				tableElements.add(d6);
				tableElements.add(d7);
				tableElements.add(d8);
				tableElements.add(d9);
				tableElements.add(d10);
				//選択した項目のみ
			} else {
				for (int i = 0; i < 3; i++) {
					if (index[i] == 1) {
						tableElements.add(d);
					}
					if (index[i] == 2) {
						tableElements.add(d2);
					}
					if (index[i] == 3) {
						tableElements.add(d3);
					}
					if (index[i] == 4) {
						tableElements.add(d4);
					}
					if (index[i] == 5) {
						tableElements.add(d5);
					}
					if (index[i] == 6) {
						tableElements.add(d6);
					}
					if (index[i] == 7) {
						tableElements.add(d7);
					}
					if (index[i] == 8) {
						tableElements.add(d8);
					}
					if (index[i] == 9) {
						tableElements.add(d9);
					}
					if (index[i] == 10) {
						tableElements.add(d10);
					}
				}
			}
			//				}	
		} catch (SQLException e) {
			//rs.nextのエラー
			throw new XysfljDB.ResultAnalyzeException(e);
		}
	}

	private class DataRow extends LinePatern {
		public DataRow() {
			super("データ", 50);
		}

		public String getOptionTag(String value) throws SpanException {
			return " align=\"right\"";
		}
	}

	public String createTag() throws NoDataException, SpanException {

		//検索以外の場合は処理を行わない（データが存在する場合のみ有効）
		if (((Xysklj_01201_Params.COMMAND_REFERENCE == ct)
			|| (Xysklj_01201_Params.COMMAND_DOWNLOAD == ct))
			&& (param.NO_DATA_FLG == param.COMMAND_DATA)) {
			return super.createTag();
		} else {
			return "";
		}
	}
	public String createStartTagWithFrame(String f) {
		if (((Xysklj_01201_Params.COMMAND_REFERENCE == ct)
			|| (Xysklj_01201_Params.COMMAND_DOWNLOAD == ct))
			&& (param.NO_DATA_FLG == param.COMMAND_DATA)) {

			StringBuffer tag = new StringBuffer();
			/*		tag.append("<INPUT type='hidden' name='");
					tag.append(NAME);
					tag.append("Page' value='");
					tag.append(String.valueOf(pageNum));
					tag.append("'>\r\n");
			*/
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
			tag.append("\t<COL width=\"50\">\r\n");
			tag.append("\t<COL width=\"70\">\r\n");
			tag.append("\t<COL width=\"65\">\r\n");
			tag.append("\t<COL width=\"85\">\r\n");
			tag.append("\t<COL width=\"70\">\r\n");
			int cnt = 0;

			while (cnt < maxday) {
				tag.append("\t<COL width=\"70\">\r\n");
				cnt++;
			}
			tag.append("\t<!--THEAD END-->\r\n");
			tag.append("\t<TBODY>\r\n");
			return tag.toString();
		} else {
			return "";
		}
	}
	public String createHeadTag() {
		StringBuffer tag = new StringBuffer();
		Enumeration headers = tableHeaders.elements();
		tag.append("\t\t<TR bgcolor=\"#C0C0C0\" align=\"center\">\r\n");

		while (headers.hasMoreElements()) {
			LinePatern l = (LinePatern) headers.nextElement();
			tag.append("\t\t\t<TD");
			tag.append(l.getHeadOptionTag());
			tag.append(">");
			tag.append(l.getLabel());
			tag.append("<BR></TD>\r\n");
			cnt++;
		}
		cnt = 0;

		tag.append("\t\t</TR>\r\n");
		return XysfljGenericRules.exchangeOutString(tag);
	}

	public Vector csvHeaddata() {
		Vector csv = new Vector();

		Enumeration headers = tableHeaders.elements();

		while (headers.hasMoreElements()) {
			LinePatern l = (LinePatern) headers.nextElement();
			csv.add(l.getLabel());
		}
		cnt = 0;
		return csv;
	}

	public Vector csvdata() {
		Vector csv = new Vector();

		try {

			Enumeration records = tableElements.elements();
			while (records.hasMoreElements()) {
				TableData data = (TableData) records.nextElement();
				Enumeration column = tableHeaders.elements();
				while (column.hasMoreElements()) {
					LinePatern l = (LinePatern) column.nextElement();
					csv.add(data.get(l.getName()));
				}
			}
		} catch (Exception e) {
		}
		return csv;
	}

	//ここから
	/**
		 * テーブルのデータ行を出力するタグを返す。
		 * &lt;TR&gt;から&lt;/TR&gt;まで(1行)のタグをテーブルの要素(tableElements)
		 * の数、出力する。
		 * @return データ部分のタグ
		 * @throws NoDataException
		 * @throws SpanException
		 */
	public String createDataTag() throws NoDataException, SpanException {
		StringBuffer tag = new StringBuffer();

		int cursor = 0;

		Enumeration records = tableElements.elements();
		int i = 0; // rouwspan判定用

		TableData data = null;
		while (records.hasMoreElements()) {
			data = (TableData) records.nextElement();
			tag.append("\t\t<TR");
			//		tag.append("align = center");
			tag.append(dataRow.getOptionTag(""));
			tag.append(">\r\n");

			Enumeration column = tableHeaders.elements();
			cnt = 0;
			i++; // 行数カウント
			if (rowspan < i) { // 行数がrowspan値を超えた場合
				i = 1; // 行数に1(行目)を設定
			}
			while (cnt < maxday + 5) {
				//			while(column.hasMoreElements()){
				LinePatern l = (LinePatern) column.nextElement();
				//				boolean rowSpanFlg = l.rowspanCheck();
				//				boolean colSpanFlg = dataRow.colspanCheck();
				//				if(false == rowSpanFlg && false == colSpanFlg){
				if (i == 1) { // 行数が1行目の場合のみrowspanを設定
					//3列目まで縦方向にセルを連結
					if (cnt < 3) {
						String value = data.get(l.getName());
						tag.append(
							"\t\t\t<TD align=\"center\" rowspan=" + rowspan);
						tag.append(" " + l.getOptionTag(value,cursor,data));
						tag.append(">\r\n");
						tag.append("\t\t\t\t" + l.editValueWithFontTag(value,cursor,data));
						tag.append("<BR>\r\n\t\t\t</TD>\r\n");
					}
				}
				if (3 <= cnt && cnt < maxday + 5) {
					String value = data.get(l.getName());
					if (cnt == 3) {
						tag.append("\t\t\t<TD align=\"center\"");
					} else {
						tag.append("\t\t\t<TD ");
					}
					tag.append(" " + l.getOptionTag(value,cursor,data));
					tag.append(">\r\n");
					tag.append("\t\t\t\t" + l.editValueWithFontTag(value,cursor,data));
					tag.append("<BR>\r\n\t\t\t</TD>\r\n");
				}
				cnt++;
			}

		}

		tag.append("\t\t</TR>\r\n");
		cursor++;

		while (cursor < minLines) {
			tag.append("\t\t<TR");
			tag.append(dataRow.getOptionTag("",cursor,data));
			tag.append(">\r\n");

			Enumeration column = tableHeaders.elements();
			while (column.hasMoreElements()) {
				LinePatern l = (LinePatern) column.nextElement();
				if (false == l.rowspanCheck()) {
					tag.append("\t\t\t<TD><BR></TD>\r\n");
				}
			}
			tag.append("\t\t</TR>\r\n");
			cursor++;
		}

		return tag.toString();
	}

	public String getOptionTag(String value) throws SpanException {
		return "";
	}

	public class InputDataCol extends DataCol {
		final private String COL_ID;

		public String editValueWithFontTag(String val) {
			//			if (param.getTransactionType() == 3) {
			return val;
			//			} else {
			//				return "<input type=\"text\" name=\"Spread_"
			//					+ COL_ID
			//					+ "\" style=\"text-align:right;\" size=\"12\" maxlength=\"10\" value=\""
			//					+ val
			//					+ "\">";
			//			}
		}

		public InputDataCol(String s, int n, String t) {
			super(s, n);
			COL_ID = t;
		}
	}

	public class DataCol extends LinePatern {
		public DataCol(String s, int i) {
			super(s, i);
		}
	}

	public class DetailTableData extends TableData {
		public DetailTableData() {
			//全項目取得時　
			if (index[0] == 0) {
				setData(maxday);
				setData2(maxday);
				setData3(maxday);
				setData4(maxday);
				setData5(maxday);
				setData6(maxday);
				setData7(maxday);
				setData8(maxday);
				setData9(maxday);
				setData10(maxday);
			}
			//選択した項目のみ
			else {
				for (int i = 0; i < 3; i++) {
					if (index[i] == 1) {
						setData(maxday);
					}
					if (index[i] == 2) {
						setData2(maxday);
					}
					if (index[i] == 3) {
						setData3(maxday);
					}
					if (index[i] == 4) {
						setData4(maxday);
					}
					if (index[i] == 5) {
						setData5(maxday);
					}
					if (index[i] == 6) {
						setData6(maxday);
					}
					if (index[i] == 7) {
						setData7(maxday);
					}
					if (index[i] == 8) {
						setData8(maxday);
					}
					if (index[i] == 9) {
						setData9(maxday);
					}
					if (index[i] == 10) {
						setData10(maxday);
					}
				}
			}
		}

		/**
		 * フォーマット編集を行います。
		 * @param data 編集する値
		 * @return 編集された値
		 */
		private String dataFormat(long data) {
			DecimalFormat DFcomma = new DecimalFormat("###,###,###");
			//		int wk_data;
			//		if (data >= 0) {
			//			wk_data= (int) ((data + 500) / 1000);
			//		} else {
			//			wk_data= (int) ((data - 500) / 1000);
			//		}
			//		return String.valueOf(DFcomma.format(wk_data));
			return String.valueOf(DFcomma.format(data));
		}

		/**
		 * フォーマット編集を行います。
		 * @param data 編集する値
		 * @return 編集された値
		 */
		private String dataFormat_percent(double data)
		{
			DecimalFormat DFpercent= new DecimalFormat("#,##0.00");
			return String.valueOf(DFpercent.format(data));
		}

		//空の値をセット
		private void setData(int i) {

			setKencd("");
			setKennm("");
			setSaleDate("");
			setTitle("");
			setSum("");

			cnt = 0;
			while (cnt < i) {
				cnt++;
				setDay("", cnt);
			}

		}
		private void setData2(int i) {

			setKencd("");
			setKennm("");
			setSaleDate("");
			setTitle("");
			setSum("");

			cnt = 0;
			while (cnt < i) {
				cnt++;
				setDay("", cnt);
			}

		}
		private void setData3(int i) {

			setKencd("");
			setKennm("");
			setSaleDate("");
			setTitle("");
			setSum("");

			cnt = 0;
			while (cnt < i) {
				cnt++;
				setDay("", cnt);
			}

		}
		private void setData4(int i) {

			setKencd("");
			setKennm("");
			setSaleDate("");
			setTitle("");
			setSum("");

			cnt = 0;
			while (cnt < i) {
				cnt++;
				setDay("", cnt);
			}

		}
		private void setData5(int i) {

			setKencd("");
			setKennm("");
			setSaleDate("");
			setTitle("");
			setSum("");
			;

			cnt = 0;
			while (cnt < i) {
				cnt++;
				setDay("", cnt);
			}

		}
		private void setData6(int i) {

			setKencd("");
			setKennm("");
			setSaleDate("");
			setTitle("");
			setSum("");

			cnt = 0;
			while (cnt < i) {
				cnt++;
				setDay("", cnt);
			}

		}
		private void setData7(int i) {

			setKencd("");
			setKennm("");
			setSaleDate("");
			setTitle("");
			setSum("");

			cnt = 0;
			while (cnt < i) {
				cnt++;
				setDay("", cnt);
			}

		}
		private void setData8(int i) {

			setKencd("");
			setKennm("");
			setSaleDate("");
			setTitle("");
			setSum("");

			cnt = 0;
			while (cnt < i) {
				cnt++;
				setDay("", cnt);
			}

		}
		private void setData9(int i) {

			setKencd("");
			setKennm("");
			setSaleDate("");
			setTitle("");
			setSum("");

			cnt = 0;
			while (cnt < i) {
				cnt++;
				setDay("", cnt);
			}

		}
		private void setData10(int i) {

			setKencd("");
			setKennm("");
			setSaleDate("");
			setTitle("");
			setSum("");

			cnt = 0;
			while (cnt < i) {
				cnt++;
				setDay("", cnt);
			}

		}

		private void setData(ResultSet rs) {
			try {

				setKencd(rs.getString("県コード"));
				setKennm(rs.getString("県名"));
				setSaleDate(rs.getString("saledate"));
				setTitle("仕入数");
				setSum(dataFormat(rs.getLong("仕入数"))); //合計
				//				setSum(rs.getString("仕入数")); //合計
			} catch (SQLException e) {
				//エクセプション無視
			}
		}
		private void setData2(ResultSet rs) {
			try {

				setKencd(rs.getString("県コード"));
				setKennm(rs.getString("県名"));
				setSaleDate(rs.getString("saledate"));

				setTitle("販売数");
				setSum(rs.getString("販売数")); //合計
				setSum(dataFormat(rs.getLong("販売数"))); //合計
				//				setSum(rs.getString("販売数")); //合計
			} catch (SQLException e) {
				//エクセプション無視
			}
		}
		private void setData3(ResultSet rs) {
			try {

				setKencd(rs.getString("県コード"));
				setKennm(rs.getString("県名"));
				setSaleDate(rs.getString("saledate"));

				setTitle("販売率");
				setSum(dataFormat_percent(rs.getDouble("販売率"))); //合計
				//				setSum(rs.getString("販売率")); //合計
			} catch (SQLException e) {
				//エクセプション無視
			}
		}
		private void setData4(ResultSet rs) {
			try {

				setKencd(rs.getString("県コード"));
				setKennm(rs.getString("県名"));
				setSaleDate(rs.getString("saledate"));

				setTitle("配本店");
				setSum(dataFormat(rs.getLong("配本店"))); //合計
				//				setSum(rs.getString("配本店")); //合計
			} catch (SQLException e) {
				//エクセプション無視
			}
		}
		private void setData5(ResultSet rs) {
			try {

				setKencd(rs.getString("県コード"));
				setKennm(rs.getString("県名"));
				setSaleDate(rs.getString("saledate"));

				setTitle("売切店数");
				setSum(dataFormat(rs.getLong("売切店数"))); //合計
				//				setSum(rs.getString("売切店数")); //合計
			} catch (SQLException e) {
				//エクセプション無視
			}
		}
		private void setData6(ResultSet rs) {
			try {

				setKencd(rs.getString("県コード"));
				setKennm(rs.getString("県名"));
				setSaleDate(rs.getString("saledate"));

				setTitle("売切店発生率");
				setSum(dataFormat_percent(rs.getDouble("売切店発生率"))); //合計
				//				setSum(rs.getString("売切店発生率")); //合計
			} catch (SQLException e) {
				//エクセプション無視
			}
		}
		private void setData7(ResultSet rs) {
			try {

				setKencd(rs.getString("県コード"));
				setKennm(rs.getString("県名"));
				setSaleDate(rs.getString("saledate"));

				setTitle("返品数");
				setSum(dataFormat(rs.getLong("返品数"))); //合計
				//				setSum(rs.getString("返品数")); //合計
			} catch (SQLException e) {
				//エクセプション無視
			}
		}
		private void setData8(ResultSet rs) {
			try {

				setKencd(rs.getString("県コード"));
				setKennm(rs.getString("県名"));
				setSaleDate(rs.getString("saledate"));

				setTitle("欠品数");
				setSum(dataFormat(rs.getLong("欠品数"))); //合計
				//				setSum(rs.getString("欠品数")); //合計
			} catch (SQLException e) {
				//エクセプション無視
			}
		}
		private void setData9(ResultSet rs) {
			try {

				setKencd(rs.getString("県コード"));
				setKennm(rs.getString("県名"));
				setSaleDate(rs.getString("saledate"));

				setTitle("仕入ＰＳＡ");
				setSum(dataFormat_percent(rs.getDouble("仕入ＰＳＡ"))); //合計
				//				setSum(rs.getString("仕入ＰＳＡ")); //合計
			} catch (SQLException e) {
				//エクセプション無視
			}
		}
		private void setData10(ResultSet rs) {
			try {

				setKencd(rs.getString("県コード"));
				setKennm(rs.getString("県名"));
				setSaleDate(rs.getString("saledate"));

				setTitle("販売ＰＳＡ");
				setSum(dataFormat_percent(rs.getDouble("販売ＰＳＡ"))); //合計
				//				setSum(rs.getString("販売ＰＳＡ")); //合計
			} catch (SQLException e) {
				//エクセプション無視
			}
		}
		private void setData(ResultSet rs, long day) {
			try {
				cnt = 0;
				setDay(dataFormat(rs.getLong("仕入数")), day);
				//				setDay(rs.getString("仕入数"), day);
			} catch (SQLException e) {
				//エクセプション無視
			}
		}
		private void setData2(ResultSet rs, long day) {
			try {
				cnt = 0;
				setDay(dataFormat(rs.getLong("販売数")), day);
				//				setDay(rs.getString("販売数"), day);
			} catch (SQLException e) {
				//エクセプション無視
			}
		}
		private void setData3(ResultSet rs, long day) {
			try {
				cnt = 0;
				setDay(dataFormat_percent(rs.getDouble("販売率")), day);
				//				setDay(rs.getString("販売率"), day);
			} catch (SQLException e) {
				//エクセプション無視
			}
		}
		private void setData4(ResultSet rs, long day) {
			try {
				cnt = 0;
				setDay(dataFormat(rs.getLong("配本店")), day);
				//				setDay(rs.getString("配本店"), day);
			} catch (SQLException e) {
				//エクセプション無視
			}
		}
		private void setData5(ResultSet rs, long day) {
			try {
				cnt = 0;
				setDay(dataFormat(rs.getLong("売切店数")), day);
				//				setDay(rs.getString("売切店数"), day);
			} catch (SQLException e) {
				//エクセプション無視
			}
		}
		private void setData6(ResultSet rs, long day) {
			try {
				cnt = 0;
				setDay(dataFormat_percent(rs.getDouble("売切店発生率")), day);
				//				setDay(rs.getString("売切店発生率"), day);
			} catch (SQLException e) {
				//エクセプション無視
			}
		}
		private void setData7(ResultSet rs, long day) {
			try {
				cnt = 0;
				setDay(dataFormat(rs.getLong("返品数")), day);
				//				setDay(rs.getString("返品数"), day);
			} catch (SQLException e) {
				//エクセプション無視
			}
		}
		private void setData8(ResultSet rs, long day) {
			try {
				cnt = 0;
				setDay(dataFormat(rs.getLong("欠品数")), day);
				//				setDay(rs.getString("欠品数"), day);
			} catch (SQLException e) {
				//エクセプション無視
			}
		}
		private void setData9(ResultSet rs, long day) {
			try {
				cnt = 0;
				setDay(dataFormat_percent(rs.getDouble("仕入ＰＳＡ")), day);
				//				setDay(rs.getString("仕入ＰＳＡ"), day);
			} catch (SQLException e) {
				//エクセプション無視
			}
		}
		private void setData10(ResultSet rs, long day) {
			try {
				cnt = 0;
				setDay(dataFormat_percent(rs.getDouble("販売ＰＳＡ")), day);
				//				setDay(rs.getString("販売ＰＳＡ"), day);
			} catch (SQLException e) {
				//エクセプション無視
			}
		}

		//setter
		private void setKencd(String v) {
			put(kencdCol.getName(), v);
		}
		private void setKennm(String v) {
			put(kennmCol.getName(), v);
		}
		private void setSaleDate(String v) {

			put(saledateCol.getName(), v);
		}
		private void setTitle(String v) {
			put(titleCol.getName(), v);
		}
		private void setSum(String v) {
			put(sumCol.getName(), v);
		}

		private void setDay(String v, long day) {
			char a;
			a = 'E';
			day1Col = new InputDataCol(day + "日目", 76, String.valueOf(a + day));
			put(day1Col.getName(), v);
		}

		//getter
		public String getKencd() throws NoDataException {
			return get(kencdCol.getName());
		}
		public String getKennm() throws NoDataException {
			return get(kennmCol.getName());
		}
		public String getSaleDate() throws NoDataException {
			return get(saledateCol.getName());
		}
		public String getTitle() throws NoDataException {
			return get(titleCol.getName());
		}
		public String getSum() throws NoDataException {
			return get(sumCol.getName());
		}
		public String getDay() throws NoDataException {
			return get(day1Col.getName());
		}
	}
}
