 /*
 *
 *  修正履歴
 *  L001  2006/07/04 K.Sasaki   SQLのチューニング
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


public class Xysk0230DetailTable extends XysfljTableTag {

	private int cnt = 0;
	public String renewday ="";
	public String saledate ="";
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
	private InputDataCol sumCol;
	private InputDataCol brandCol;
	private InputDataCol titleCol;
	private InputDataCol day1Col;
	private Xysklj_02301_Params param;
	private DetailTableData totalData = null;
//	private int cursor = 0;

	public String select_publish; //選択された新聞社
	private String brand = ""; //商品コード+商品名
    public String inout = null;	
	public Vector TorihikiCD =new Vector(); //取引先コード
	public String torihiki_list;
	public String syouhin_list;

	public String allitem; //項目コンボで選択された値 0:項目選択　1:前項目
	public String selectline1; //1行目コンボで選択された値
	public String selectline2; //2行目コンボで選択された値
	public String selectline3; //3行目コンボで選択された値

	private int ct = 0;

	public Xysk0230DetailTable(Xysklj_02301_Params p) {
		super("Xysk0230DetailTable", p);
		param = p;
		ct = param.getCommandType();

		int st = param.getStatus();
		dataRow = new DataRow();
		//県コードから合計までのヘッダー
		brandCol = new InputDataCol("商品", 130, "A");
		titleCol = new InputDataCol("", 98, "C");
		sumCol = new InputDataCol("合計", 76, "D");

		addHeader(brandCol);
		addHeader(titleCol);
		addHeader(sumCol);

		//コンボBOXで選択された値をgetする
		select_publish = param.getValue(XyskljNewsPUBLISHCOMCombo.NAME);

		inout = param.getRequestParameter("inout");
//		System.out.println("ioutフラグは"+ inout);

		//取引先コードを取得
		TorihikiCD = param.getLoginUserTorihikisakiCD();
//			System.out.println("取引先コードは" + TorihikiCD);

		//検索以外の場合は処理を行わない
		if ((Xysklj_02301_Params.COMMAND_REFERENCE == ct)
			|| (Xysklj_02301_Params.COMMAND_DOWNLOAD == ct)) {

			try {
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

		if (((inout == null || inout.equals("") || inout.equals("0"))) || ((TorihikiCD.elementAt(cnt+1)).equals("1"))){
			getTotalDataFromDB();
		}
		else if((TorihikiCD.elementAt(cnt+1)).equals("2")){
			getNewTotalDataFromDB();
		}
		else{
		} 
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

			// SQL文作成<予算実績データ読込>
			String sql = "";				//SQL

			sql = " SELECT  to_char(作成日時,'YYYYMMDD') renewday   "
				+	"FROM UPC_新聞_コントロール "
				+	"WHERE 制御種別 = '01'";

			ResultSet rs = tdb.selectTable(sql); // SQL実行
			//SQLにて取得したレコード分tableElementsにセットする
			if ( rs.next()) {
				renewday = rs.getString("renewday");
				//データが存在した場合は表示する
				if (rs.getString("renewday") != null) {
//					param.NO_DATA_FLG = param.COMMAND_DATA;
				} else {
					//データが存在しない場合はメッセージ出力	
					param.addMessageByCode(5);
				}

			}
			rs.close();

			//更新日から１４日後までのヘッダー 
			char a;
			a = 'D';
			cnt = 0;

			String day = "";
			while (cnt < param.printDays) {
				day = Xysflj_Common.getTodate(renewday, -1 - (cnt));
				day1Col = new InputDataCol( day, 76, String.valueOf(a++));
				addHeader(day1Col);
				cnt++;
			}

			day = Xysflj_Common.getTodate(renewday, 0 - (cnt));

			String sql2 = "";
			sql2 = "SELECT MAX(to_char(A.発売日,'YYYYMMDD')) saledate "
				+ "FROM UPT_新聞_形態別販売実績 A "
				+ "    ,UPM_新聞社別商品 B   "
//	*********** E10K移行時追加 06/07/04 START *******************************
//				+ "WHERE A.新聞社コード =" + select_publish
//				+	" AND B.新聞社コード =" + select_publish
				+ "WHERE A.新聞社コード = '" + select_publish + "'"
				+	" AND B.新聞社コード = '" + select_publish + "'"
//	************************** 06/07/04  END  *******************************
				+	" AND A.商品コード = B.商品コード "
				+	" AND A.県コード = 00 "
				+	" AND to_char(B.発売日,'YYYYMMDD') >= '" + day + "'";


			ResultSet rs2 = tdb.selectTable(sql2); // SQL実行
			//SQLにて取得したレコード分tableElementsにセットする
			if ( rs2.next()) {
				saledate = rs2.getString("saledate");
				//データが存在した場合は表示する
				if (rs2.getString("saledate") != null) {
					param.NO_DATA_FLG = param.COMMAND_DATA;
				} else {
					//データが存在しない場合はメッセージ出力	
					param.addMessageByCode(5);
				}
			}

			rs2.close();

			sql = "SELECT A.新聞社コード "
				+		",A.県コード "
				+		",A.県名 "
				+		",A.商品コード "
				+		",A.商品コード || "
				+		" B.商品名 商品名"
				+		",to_char(A.発売日,'YYYYMMDD') saledate "
				+		",A.経過日数 "
				+		",A.仕入数 "
				+		",A.販売数 "
				+		",A.販売率 "
				+		",A.配本店 "
				+		",A.売切店数 "
				+		",A.売切店発生率 "
				+		",A.返品数 "
				+		",A.欠品数 "
				+		",A.仕入ＰＳＡ "
				+		",A.販売ＰＳＡ "
				+ "FROM UPT_新聞_形態別販売実績 A "
				+ "    ,UPM_新聞社別商品 B   "
//	*********** E10K移行時追加 06/07/04 START *******************************
//				+ "WHERE A.新聞社コード =" + select_publish
//				+	" AND B.新聞社コード =" + select_publish
				+ "WHERE A.新聞社コード = '" + select_publish + "'"
				+	" AND B.新聞社コード = '" + select_publish + "'"
//	************************** 06/07/04  END  *******************************
				+	" AND A.商品コード = B.商品コード "
				+	" AND A.県コード = 00 "
				+	" AND to_char(B.発売日,'YYYYMMDD') >= '" + day + "'"
				+ "ORDER BY A.商品コード ASC "
				+			",A.発売日 DESC ";

			rs = tdb.selectTable(sql);

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
				if ( !(brand.equals(rs.getString("商品コード"))) ) {
					cnt = 0;

					//最初の場合はaddしない。商品が切り替わったときaddする。
					if (!(brand.equals(""))) {

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
					brand = rs.getString("商品コード");
				}

				//同一商品の場合は同一行にセットする
				if (brand.equals(rs.getString("商品コード"))) {

					long w_date = rs.getLong("saledate");
					//全項目取得時
					if (index[0] == 11) {
						d.setData(rs, w_date);
						d2.setData2(rs, w_date);
						d3.setData3(rs, w_date);
						d4.setData4(rs, w_date);
						d5.setData5(rs, w_date);
						d6.setData6(rs, w_date);
						d7.setData7(rs, w_date);
						d8.setData8(rs, w_date);
						d9.setData9(rs, w_date);
						d10.setData10(rs, w_date);

						//選択した項目のみセット
					} else {
						for (int i = 0; i < 3; i++) {
							if (index[i] == 1) {
								d.setData(rs, w_date);
							}
							if (index[i] == 2) {
								d2.setData2(rs, w_date);
							}
							if (index[i] == 3) {
								d3.setData3(rs, w_date);
							}
							if (index[i] == 4) {
								d4.setData4(rs, w_date);
							}
							if (index[i] == 5) {
								d5.setData5(rs, w_date);
							}
							if (index[i] == 6) {
								d6.setData6(rs, w_date);
							}
							if (index[i] == 7) {
								d7.setData7(rs, w_date);
							}
							if (index[i] == 8) {
								d8.setData8(rs, w_date);
							}
							if (index[i] == 9) {
								d9.setData9(rs, w_date);
							}
							if (index[i] == 10) {
								d10.setData10(rs, w_date);
							}

						}
					}
					cnt++;
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
		} catch (SQLException e) {
			//rs.nextのエラー
			throw new XysfljDB.ResultAnalyzeException(e);
		}
	}

	public void getNewTotalDataFromDB() throws XysfljDB.DBException {
		try {
				StringBuffer mk_list = new StringBuffer();
				mk_list.append("\'");
				mk_list.append(TorihikiCD.elementAt(0).toString());
//				System.out.println(TorihikiCD.elementAt(0));
				for(cnt=1; cnt< (TorihikiCD.size()-1); cnt++ ) {
					mk_list.append("\'"+","+"\'");
					mk_list.append(TorihikiCD.elementAt(cnt+1).toString());
					cnt=cnt+1;
				}
				mk_list.append("\'");

				torihiki_list = mk_list.toString();

			XysfljDB tdb = param.getDbConnection();

				String sql2 = "";		//SQL
				sql2 = "SELECT DISTINCT A.商品コード select_product_cd ";			//商品を取引先が扱っているものに絞る
				sql2 += "FROM	UPM_新聞社別商品 A ";							
			    sql2 += "		,UPT_取引先_商品 B ";
				sql2 += "WHERE A.新聞社コード = '"+ select_publish +"' ";
				sql2 +=	"AND B.取引先コード in ("+( torihiki_list )+") ";
				sql2 += 	"AND A.商品コード = B.商品コード ";											
							
				ResultSet rs2 = tdb.selectTable(sql2); // SQL実行
				//SQLにて取得したレコード分取得する
			
				Vector pro =new Vector();
				while(rs2.next()){
					pro.addElement(rs2.getString("select_product_cd"));
				}
				if ( pro.size() == 0	||	pro.elementAt(0) == ""){
					pro.addElement("000000");
				}
				rs2.close();

//				System.out.println("pro=" + pro);

				StringBuffer select_product_list =new StringBuffer();
				select_product_list.append("\'");
				select_product_list.append(pro.elementAt(0).toString());
//				System.out.println(pro.elementAt(0));
				for(int i=1;i<(pro.size());i++){
						select_product_list.append("\'"+","+"\'");
						select_product_list.append(pro.elementAt(i).toString());
				}	
						select_product_list.append("\'");
				syouhin_list = select_product_list.toString();


			// SQL文作成<予算実績データ読込>
			String sql = "";				//SQL

			sql = " SELECT  to_char(作成日時,'YYYYMMDD') renewday   "
				+	"FROM UPC_新聞_コントロール "
				+	"WHERE 制御種別 = '01'";

			ResultSet rs = tdb.selectTable(sql); // SQL実行
			//SQLにて取得したレコード分tableElementsにセットする
			if ( rs.next()) {
				renewday = rs.getString("renewday");
				//データが存在した場合は表示する
				if (rs.getString("renewday") != null) {
//					param.NO_DATA_FLG = param.COMMAND_DATA;
				} else {
					//データが存在しない場合はメッセージ出力	
					param.addMessageByCode(5);
				}

			}
			rs.close();

			//更新日から１４日後までのヘッダー 
			char a;
			a = 'D';
			cnt = 0;

			String day = "";
			while (cnt < param.printDays) {
				day = Xysflj_Common.getTodate(renewday, -1 - (cnt));
				day1Col = new InputDataCol( day, 76, String.valueOf(a++));
				addHeader(day1Col);
				cnt++;
			}

			day = Xysflj_Common.getTodate(renewday, 0 - (cnt));

			String sql3 = "";
			sql3 = "SELECT MAX(to_char(A.発売日,'YYYYMMDD')) saledate "
				+ "FROM UPT_新聞_形態別販売実績 A "
				+ "    ,UPM_新聞社別商品 B   "
				+ "    ,UPT_取引先_商品 C   "
//	*********** E10K移行時追加 06/07/04 START *******************************
//				+ "WHERE A.新聞社コード =" + select_publish
//				+	" AND B.新聞社コード =" + select_publish
				+ "WHERE A.新聞社コード = '" + select_publish + "'"
				+	" AND B.新聞社コード = '" + select_publish + "'"
//	************************** 06/07/04  END  *******************************
				+   " AND C.取引先コード in ("+( torihiki_list )+") "
				+	" AND A.商品コード = B.商品コード "
				+	" AND A.商品コード = C.商品コード "
				+	" AND A.県コード = 00 "
				+	" AND to_char(B.発売日,'YYYYMMDD') > '" + day + "'";


			ResultSet rs3 = tdb.selectTable(sql3); // SQL実行
			//SQLにて取得したレコード分tableElementsにセットする
			if ( rs3.next()) {
				saledate = rs3.getString("saledate");
				//データが存在した場合は表示する
				if (rs3.getString("saledate") != null) {
					param.NO_DATA_FLG = param.COMMAND_DATA;
				} else {
					//データが存在しない場合はメッセージ出力	
					param.addMessageByCode(5);
				}
			}

			rs3.close();

			sql = "SELECT A.商品コード || B.商品名  商品名 ";
			sql	+=	" ,A.商品コード ";
			sql	+=	" ,to_char(A.発売日,'YYYYMMDD') saledate ";
			sql	+=	" ,A.経過日数 ";
			sql	+=	" ,A.仕入数 ";
			sql	+=	" ,A.販売数 ";
			sql	+=	" ,A.販売率 ";
			sql	+=	" ,A.配本店 ";
			sql	+=	" ,A.売切店数 ";
			sql	+=	" ,A.売切店発生率 ";
			sql	+=	" ,A.返品数 ";
			sql	+=	" ,A.欠品数	";
			sql	+=	" ,A.仕入ＰＳＡ ";
			sql	+=	" ,A.販売ＰＳＡ  " ;
			sql	+= "FROM UPT_新聞_形態別販売実績 A,UPM_新聞社別商品 B ";	
			sql	+= "WHERE A.新聞社コード = '" + select_publish +"'";
			sql +=  " AND A.新聞社コード = B.新聞社コード";
			sql	+=	" AND A.商品コード in ("+( syouhin_list )+") ";
			sql +=  " AND A.商品コード = B.商品コード";
			sql	+=   " AND A.県コード = '00'";
			sql	+=   " AND to_char(B.発売日,'YYYYMMDD') > '" + day + "' ";
			sql	+= "ORDER BY A.商品コード ASC ";
			sql	+=	" ,A.発売日 DESC ";

			rs = tdb.selectTable(sql);

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
				if ( !(brand.equals(rs.getString("商品コード"))) ) {
					cnt = 0;

					//最初の場合はaddしない。商品が切り替わったときaddする。
					if (!(brand.equals(""))) {

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
					brand = rs.getString("商品コード");
				}

				//同一商品の場合は同一行にセットする
				if (brand.equals(rs.getString("商品コード"))) {

					long w_date = rs.getLong("saledate");
					//全項目取得時
					if (index[0] == 11) {
						d.setData(rs, w_date);
						d2.setData2(rs, w_date);
						d3.setData3(rs, w_date);
						d4.setData4(rs, w_date);
						d5.setData5(rs, w_date);
						d6.setData6(rs, w_date);
						d7.setData7(rs, w_date);
						d8.setData8(rs, w_date);
						d9.setData9(rs, w_date);
						d10.setData10(rs, w_date);

						//選択した項目のみセット
					} else {
						for (int i = 0; i < 3; i++) {
							if (index[i] == 1) {
								d.setData(rs, w_date);
							}
							if (index[i] == 2) {
								d2.setData2(rs, w_date);
							}
							if (index[i] == 3) {
								d3.setData3(rs, w_date);
							}
							if (index[i] == 4) {
								d4.setData4(rs, w_date);
							}
							if (index[i] == 5) {
								d5.setData5(rs, w_date);
							}
							if (index[i] == 6) {
								d6.setData6(rs, w_date);
							}
							if (index[i] == 7) {
								d7.setData7(rs, w_date);
							}
							if (index[i] == 8) {
								d8.setData8(rs, w_date);
							}
							if (index[i] == 9) {
								d9.setData9(rs, w_date);
							}
							if (index[i] == 10) {
								d10.setData10(rs, w_date);
							}

						}
					}
					cnt++;
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
		//検索以外の場合は処理を行わない
		if (((Xysklj_02301_Params.COMMAND_REFERENCE == ct)
			|| (Xysklj_02301_Params.COMMAND_DOWNLOAD == ct))
			&& (param.NO_DATA_FLG == param.COMMAND_DATA)) {
			StringBuffer tag = new StringBuffer();

			if ( Xysklj_02301_Params.COMMAND_REFERENCE == ct ) {
				tag.append(createStartTag());
				tag.append(createHeadTag());
				tag.append(createEndTag());
				tag.append("<DIV id=\"BODY1\" style=\"height: 410px; overflow:auto;\">");
				tag.append(createStartTag());
				tag.append(createDataTag());
				tag.append(createEndTag());
				tag.append("</DIV>");
			} else {
				tag.append(super.createTag());
			}

			return XysfljGenericRules.exchangeOutString(tag);
		} else {
			return "";
		}
	}
	public String createStartTagWithFrame(String f) {

		StringBuffer tag = new StringBuffer();
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
		tag.append("\t<COL width=\"110\">\r\n");
		tag.append("\t<COL width=\"85\">\r\n");
		tag.append("\t<COL width=\"70\">\r\n");
		int cnt = 0;

		while (cnt < param.printDays) {
			tag.append("\t<COL width=\"50\">\r\n");
			cnt++;
		}
		tag.append("\t<!--THEAD END-->\r\n");
		tag.append("\t<TBODY>\r\n");
		return tag.toString();
	}
	public String createHeadTag() {
		StringBuffer tag = new StringBuffer();
		Enumeration headers = tableHeaders.elements();
		tag.append("\t\t<TR bgcolor=\"#C0C0C0\" align=\"center\">\r\n");

		cnt = 0;
		while (headers.hasMoreElements()) {
			LinePatern l = (LinePatern) headers.nextElement();
			tag.append("\t\t\t<TD");
			tag.append(l.getHeadOptionTag());
			tag.append(">");
			if ( cnt > 2 ) {
				String day = l.getLabel();
				String youbi = "";
				switch ( Xysflj_Common.ymd_GetYoubi(day)) {
					case 1:
						youbi = "(月)";
						break;
					case 2:
						youbi = "(火)";
						break;
					case 3:
						youbi = "(水)";
						break;
					case 4:
						youbi = "(木)";
						break;
					case 5:
						youbi = "(金)";
						break;
					case 6:
						youbi = "<SPAN style=\"color:#0000FF\">(土)</SPAN>";
						break;
					case 7:
						youbi = "<SPAN style=\"color:#FF0000\">(日)</SPAN>";
						break;
				}
				tag.append(Xysflj_Common.ymd_Print(4, day) + "<BR>" + youbi);
			} else {
				tag.append(l.getLabel());
			}
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
		Vector vYoubi = new Vector();

		cnt = 0;
		while (headers.hasMoreElements()) {
			LinePatern l = (LinePatern) headers.nextElement();
			if ( cnt > 2 ) {
				String day = l.getLabel();
				String youbi = "";
				switch ( Xysflj_Common.ymd_GetYoubi(day)) {
					case 1:
						youbi = "(月)";
						break;
					case 2:
						youbi = "(火)";
						break;
					case 3:
						youbi = "(水)";
						break;
					case 4:
						youbi = "(木)";
						break;
					case 5:
						youbi = "(金)";
						break;
					case 6:
						youbi = "(土)";
						break;
					case 7:
						youbi = "(日)";
						break;
				}
				csv.add(Xysflj_Common.ymd_Print(4, day));
				vYoubi.add(youbi);
			} else {
				csv.add(l.getLabel());
			}
			cnt++;
		}
//XysfljGenericRules.outputMessage("vYoubi.size()="+ vYoubi.size());
		csv.add("");
		csv.add("");
		csv.add("");
		for ( int i=0; i < vYoubi.size(); i++ ) {
			csv.add((String)vYoubi.elementAt(i));
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
			int pageLineCnt = 0;
		while (records.hasMoreElements()) {
			data = (TableData) records.nextElement();

			Enumeration column = tableHeaders.elements();
			cnt = 0;
			i++; // 行数カウント
			if (rowspan < i) { // 行数がrowspan値を超えた場合
				i = 1; // 行数に1(行目)を設定
				pageLineCnt++;
			}
			tag.append("\t\t<TR");
			if ( (pageLineCnt % 2 ) != 0 ) {
				tag.append(" bgcolor=\"#e0ffff\"" );
			}
			tag.append(dataRow.getOptionTag("",cursor,data));
			tag.append(">\r\n");
			int max = 3 + param.printDays;
			while (cnt < max) {
				LinePatern l = (LinePatern) column.nextElement();
				if (i == 1) { // 行数が1行目の場合のみrowspanを設定
					//2列目まで縦方向にセルを連結
					if (cnt < 1) {
						String value = data.get(l.getName());
						tag.append(
							"\t\t\t<TD align=\"center\" rowspan=" + rowspan);
						tag.append(" " + l.getOptionTag(value,cursor,data));
						tag.append(">\r\n");
						tag.append("\t\t\t\t" + l.editValueWithFontTag(value,cursor,data));
						tag.append("<BR>\r\n\t\t\t</TD>\r\n");
					} else {
						String value = data.get(l.getName());
						if (cnt == 1) {
							tag.append("\t\t\t<TD align=\"center\"");
						} else {
							tag.append("\t\t\t<TD ");
						}
						tag.append(" " + l.getOptionTag(value,cursor,data));
						tag.append(">\r\n");
						tag.append("\t\t\t\t" + l.editValueWithFontTag(value,cursor,data));
						tag.append("<BR>\r\n\t\t\t</TD>\r\n");
					}
				} else {
					if (cnt > 0) {
						String value = data.get(l.getName());
						if (cnt == 1) {
							tag.append("\t\t\t<TD align=\"center\"");
						} else {
							tag.append("\t\t\t<TD ");
						}
						tag.append(" " + l.getOptionTag(value,cursor,data));
						tag.append(">\r\n");
						tag.append("\t\t\t\t" + l.editValueWithFontTag(value,cursor,data));
						tag.append("<BR>\r\n\t\t\t</TD>\r\n");
					}
				}
				cnt++;
			}
			tag.append("\t\t</TR>\r\n");
		}

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
			return val;
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
				setData(param.printDays);
				setData2(param.printDays);
				setData3(param.printDays);
				setData4(param.printDays);
				setData5(param.printDays);
				setData6(param.printDays);
				setData7(param.printDays);
				setData8(param.printDays);
				setData9(param.printDays);
				setData10(param.printDays);
			}
			//選択した項目のみ
			else {
				for (int i = 0; i < 3; i++) {
					if (index[i] == 1) {
						setData(param.printDays);
					}
					if (index[i] == 2) {
						setData2(param.printDays);
					}
					if (index[i] == 3) {
						setData3(param.printDays);
					}
					if (index[i] == 4) {
						setData4(param.printDays);
					}
					if (index[i] == 5) {
						setData5(param.printDays);
					}
					if (index[i] == 6) {
						setData6(param.printDays);
					}
					if (index[i] == 7) {
						setData7(param.printDays);
					}
					if (index[i] == 8) {
						setData8(param.printDays);
					}
					if (index[i] == 9) {
						setData9(param.printDays);
					}
					if (index[i] == 10) {
						setData10(param.printDays);
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

			setBrand("");
			setTitle("");
			setSum("");

			cnt = 0;
			while (cnt < i) {
				cnt++;
				setDay("", cnt);
			}

		}
		private void setData2(int i) {

			setBrand("");
			setTitle("");
			setSum("");

			cnt = 0;
			while (cnt < i) {
				cnt++;
				setDay("", cnt);
			}

		}
		private void setData3(int i) {

			setBrand("");
			setTitle("");
			setSum("");

			cnt = 0;
			while (cnt < i) {
				cnt++;
				setDay("", cnt);
			}

		}
		private void setData4(int i) {

			setBrand("");
			setTitle("");
			setSum("");

			cnt = 0;
			while (cnt < i) {
				cnt++;
				setDay("", cnt);
			}

		}
		private void setData5(int i) {

			setBrand("");
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

			setBrand("");
			setTitle("");
			setSum("");

			cnt = 0;
			while (cnt < i) {
				cnt++;
				setDay("", cnt);
			}

		}
		private void setData7(int i) {

			setBrand("");
			setTitle("");
			setSum("");

			cnt = 0;
			while (cnt < i) {
				cnt++;
				setDay("", cnt);
			}

		}
		private void setData8(int i) {

			setBrand("");
			setTitle("");
			setSum("");

			cnt = 0;
			while (cnt < i) {
				cnt++;
				setDay("", cnt);
			}

		}
		private void setData9(int i) {

			setBrand("");
			setTitle("");
			setSum("");

			cnt = 0;
			while (cnt < i) {
				cnt++;
				setDay("", cnt);
			}

		}
		private void setData10(int i) {

			setBrand("");
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
				// **** E10K移行時変更対応(2006/06/08) ***
				setBrand( Xysflj_Common.getStrCvt( rs.getString("商品名") ));
//				setBrand(rs.getString("商品名"));
				// **** END ******************************
				setTitle("仕入数");
				setSum(dataFormat(rs.getLong("仕入数"))); //合計
				//				setSum(rs.getString("仕入数")); //合計
			} catch (SQLException e) {
				//エクセプション無視
			}
		}
		private void setData2(ResultSet rs) {
			try {
				// **** E10K移行時変更対応(2006/06/08) ***
				setBrand( Xysflj_Common.getStrCvt( rs.getString("商品名") ));
//				setBrand(rs.getString("商品名"));
				// **** END ******************************
				setTitle("販売数");
				setSum(dataFormat(rs.getLong("販売数"))); //合計
				//				setSum(rs.getString("販売数")); //合計
			} catch (SQLException e) {
				//エクセプション無視
			}
		}
		private void setData3(ResultSet rs) {
			try {
				// **** E10K移行時変更対応(2006/06/08) ***
				setBrand( Xysflj_Common.getStrCvt( rs.getString("商品名") ));
//				setBrand(rs.getString("商品名"));
				// **** END ******************************
				setTitle("販売率");
				setSum(dataFormat_percent(rs.getDouble("販売率"))); //合計
				//				setSum(rs.getString("販売率")); //合計
			} catch (SQLException e) {
				//エクセプション無視
			}
		}
		private void setData4(ResultSet rs) {
			try {
				// **** E10K移行時変更対応(2006/06/08) ***
				setBrand( Xysflj_Common.getStrCvt( rs.getString("商品名") ));
//				setBrand(rs.getString("商品名"));
				// **** END ******************************
				setTitle("配本店");
				setSum(dataFormat(rs.getLong("配本店"))); //合計
				//				setSum(rs.getString("配本店")); //合計
			} catch (SQLException e) {
				//エクセプション無視
			}
		}
		private void setData5(ResultSet rs) {
			try {
				// **** E10K移行時変更対応(2006/06/08) ***
				setBrand( Xysflj_Common.getStrCvt( rs.getString("商品名") ));
//				setBrand(rs.getString("商品名"));
				// **** END ******************************
				setTitle("売切店数");
				setSum(dataFormat(rs.getLong("売切店数"))); //合計
				//				setSum(rs.getString("売切店数")); //合計
			} catch (SQLException e) {
				//エクセプション無視
			}
		}
		private void setData6(ResultSet rs) {
			try {
				// **** E10K移行時変更対応(2006/06/08) ***
				setBrand( Xysflj_Common.getStrCvt( rs.getString("商品名") ));
//				setBrand(rs.getString("商品名"));
				// **** END ******************************
				setTitle("売切店発生率");
				setSum(dataFormat_percent(rs.getDouble("売切店発生率"))); //合計
				//				setSum(rs.getString("売切店発生率")); //合計
			} catch (SQLException e) {
				//エクセプション無視
			}
		}
		private void setData7(ResultSet rs) {
			try {
				// **** E10K移行時変更対応(2006/06/08) ***
				setBrand( Xysflj_Common.getStrCvt( rs.getString("商品名") ));
//				setBrand(rs.getString("商品名"));
				// **** END ******************************
				setTitle("返品数");
				setSum(dataFormat(rs.getLong("返品数"))); //合計
				//				setSum(rs.getString("返品数")); //合計
			} catch (SQLException e) {
				//エクセプション無視
			}
		}
		private void setData8(ResultSet rs) {
			try {
				// **** E10K移行時変更対応(2006/06/08) ***
				setBrand( Xysflj_Common.getStrCvt( rs.getString("商品名") ));
//				setBrand(rs.getString("商品名"));
				// **** END ******************************
				setTitle("欠品数");
				setSum(dataFormat(rs.getLong("欠品数"))); //合計
				//				setSum(rs.getString("欠品数")); //合計
			} catch (SQLException e) {
				//エクセプション無視
			}
		}
		private void setData9(ResultSet rs) {
			try {
				// **** E10K移行時変更対応(2006/06/08) ***
				setBrand( Xysflj_Common.getStrCvt( rs.getString("商品名") ));
//				setBrand(rs.getString("商品名"));
				// **** END ******************************
				setTitle("仕入ＰＳＡ");
				setSum(dataFormat_percent(rs.getDouble("仕入ＰＳＡ"))); //合計
				//				setSum(rs.getString("仕入ＰＳＡ")); //合計
			} catch (SQLException e) {
				//エクセプション無視
			}
		}
		private void setData10(ResultSet rs) {
			try {
				// **** E10K移行時変更対応(2006/06/08) ***
				setBrand( Xysflj_Common.getStrCvt( rs.getString("商品名") ));
//				setBrand(rs.getString("商品名"));
				// **** END ******************************
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

		private void setBrand(String v) {
			put(brandCol.getName(), v);
		}
		private void setTitle(String v) {
			put(titleCol.getName(), v);
		}
		private void setSum(String v) {
			put(sumCol.getName(), v);
		}
		private void setDay(String v, long day) {
			char a;
			a = 'D';
			day1Col = new InputDataCol(day + "", 76, String.valueOf(a + day));
			put(day1Col.getName(), v);

		}

		//getter

		public String getBrand() throws NoDataException {
			return get(brandCol.getName());
		}
		public String gettitle() throws NoDataException {
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
