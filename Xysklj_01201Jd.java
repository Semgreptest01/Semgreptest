/*
 *
 *  修正履歴
 *  L001  2006/05/12 K.Sasaki   ログ出力対応　ログ出力メソッドの呼び出し追加
 *
 */
package xysk;

import java.io.IOException;
import java.util.Vector;

import java.io.*;
import java.text.*;
import java.util.*;


import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * (ダウンロード)用クラス
 */
public class Xysklj_01201Jd extends HttpServlet {

	private String getLineSeparator() {
		return "\n";
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public void service(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {

		Vector csv1 = new Vector();
		Vector csv2 = new Vector();
		Vector csv3 = new Vector();
		int checkintsize = 0;
		int headintsize = 0;
		int csvintsize = 0;

		//フォームデータ解析
		Xysklj_01201_Params param = null;

		try {
			param = new Xysklj_01201_Params(req);

			Xysk0120CheckTable checkTable = new Xysk0120CheckTable(param);
			Vector vcheckdata = checkTable.csvdata();
			if (vcheckdata != null) {
				checkintsize = 1;
			}
	
			//データ明細
			Xysk0120DetailTable detailTable = new Xysk0120DetailTable(param);
			Vector csvheaddata = detailTable.csvHeaddata();
			if (csvheaddata != null) {
				headintsize = 5 + detailTable.maxday;
			}
	
			//データ明細
			Vector csvdata = detailTable.csvdata();
//			if (csvdata != null) {
			if ( detailTable.maxday != 0 ) {
				// CSV形式に編集(ヘッダー部)
				if (Xysflj_Common.csv_Out(checkintsize, vcheckdata, csv1) != true) {
				}
				// CSV形式に編集
				if (Xysflj_Common.csv_Out(headintsize, csvheaddata, csv2) != true) {
				}
				csvintsize = 5 + detailTable.maxday;
				// CSV形式に編集
				if (Xysflj_Common.csv_Out(csvintsize, csvdata, csv3) != true) {
				}
			}

// 2006/05/12変更(E10K移行対応)\
		// ***** ﾛｸﾞ出力処理 START *****************************************************************************
			param.putLog( 9,			 											// 状態（初期表示時:0 ﾀﾞｳﾝﾛｰﾄﾞ:9）
						(String)req.getParameter("screenId"), 						// 画面ＩＤ
						XysfljGenericRules.SCREEN_NM_120, 							// 画面名称
						XysfljGenericRules.SCREEN_NM_120);							// 画面MSG
		// ***** ﾛｸﾞ出力処理  END  ****************************************************************************

			// CSVファイルを出力
//			csvDownload(res, "Xysk1201.csv", csv1,csv2,csv3, getLineSeparator());
			csvDownload(res, "Xysk1201.csv", csv1,csv2,csv3, getLineSeparator(),detailTable.maxday);

		}
		catch(Exception e){
			XysfljParameters.redirectErrorPage(req,res,e);
		}
		finally {
			//DB CLOSE
			if (null != param) {
				param.exit();
			}
		}

	}

	/*
	********************************************************************************
	* メソッド名		csvDownload
	* @deprecated		ＣＳＶファイル形式ダウンロード
	* @param		HttpServletResponse	dbcom
	* @param		String	fileName
	* @param		Vector	csvData
	* @param		String	kaigyo_cd
	* @return		boolean ( true:OK false:ERR )
	********************************************************************************
	*/
	public static boolean csvDownload(HttpServletResponse response,
							String	fileName,
							Vector	csvData1,
							Vector	csvData2,
							Vector	csvData3,
							String	kaigyo_cd,
							int	maxday) {
		try{																		// try

//			response.setContentType("application/octet-stream;charset=Shift_JIS");
//			response.setHeader("Content-Disposition","attachmentl; filename=" + fileName);
			response.setContentType("application/data-download;charset=Shift_JIS");
			response.setHeader("Content-Disposition"," filename=" + fileName);

			PrintWriter out=response.getWriter();

			if (maxday != 0){
				out.print(",,県、銘柄別販売実績" + kaigyo_cd );
			}
			Enumeration data1 = csvData1.elements();
			while(data1.hasMoreElements()){
				out.print( Xysflj_Common.getStrCvt((String)data1.nextElement()) + kaigyo_cd );
			}
			Enumeration data2 = csvData2.elements();
			while(data2.hasMoreElements()){
				out.print( Xysflj_Common.getStrCvt((String)data2.nextElement()) + kaigyo_cd );
			}
			Enumeration data3 = csvData3.elements();
			while(data3.hasMoreElements()){
				out.print( Xysflj_Common.getStrCvt((String)data3.nextElement()) + kaigyo_cd );
			}
			out.close();

			return true;															// Normal return

		}catch(Exception e){														// catch
			return false;															// Abnormal return
		}																			//
	}



}
