/*
 *
 *  修正履歴
 *  L001  2006/06/08 K.Sasaki   文字化け対応 名称関連取得時にメソッドXysflj_Common.getStrCvt()を介す
 *
 */
 package xysk;

import javax.servlet.http.*;
import java.util.Vector;
import java.util.Enumeration;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Xysklj_NewsPublishProduct extends XysfljParameters{
	static Vector TorihikisakiCD = new Vector();
	static String inout = null;
	public Xysklj_NewsPublishProduct(HttpServletRequest request){
		super("Sxysk00000",request);
		TorihikisakiCD = super.getLoginUserTorihikisakiCD();
		inout = super.getRequestParameter("inout");
	}
	
	public String createPublishProductJS() throws XysfljDB.DbConnectException {
		String selectedpublishcom = getRequestParameter("publishcom_cd");

		Vector productName = new Vector();
		Vector productCode = new Vector();

        XysfljDB dbCom = getDbConnection();

		publishProductList(dbCom,"",selectedpublishcom,productCode,productName);
		
		StringBuffer sb = new StringBuffer();
		
		for(int i = 0;i<productCode.size();i++){
			String n = productName.elementAt(i).toString();
			String c = productCode.elementAt(i).toString();
			if(0 != i){
				sb.append(",");
			}
			sb.append("\"('");
			sb.append(n);
			sb.append("','");
			sb.append(c);
			sb.append("')\"\r\n");
		}
		return XysfljGenericRules.exchangeOutString(sb.toString());
	}
	/**
	********************************************************************************
	* メソッド名		publishProductList
	* @param		Xysflj_Dbcom	dbcom
	* @param		String	sosiki_cd
	* @param		String	publishcom_cd
	* @param		Vector	product_codes
	* @param		Vector	product_names
	* @return		boolean ( true:OK false:ERR )
	********************************************************************************
	*/
	public static boolean publishProductList(XysfljDB dbcom,
							String	sosiki_cd,
							String	publishcom_cd,
							Vector	product_codes,
							Vector	product_names	)	{
		try{																		// try
			String sql = "";
			product_codes.removeAllElements();
			product_names.removeAllElements();
			product_codes.addElement("000000");
			product_names.addElement("");
			if ( inout == null || inout.equals("") || inout.equals("0") ) {
			sql += " SELECT  商品コード product_cd         ";
			sql += "        ,商品名 product_nn             ";
			sql += " FROM    UPM_新聞社別商品              ";
			sql += " WHERE   新聞社コード = '" + publishcom_cd + "' ";
			sql += " GROUP BY  商品コード            ";
			sql += "        ,商品名            ";
				try{																	// try
					// 検索データ件数取得
//					System.out.println( sql );											// *** DEBUG ***
					ResultSet rs = dbcom.selectTable(sql);								// Execute Query
					while(rs.next()){													// +++ rs.next +++
						product_codes.addElement(rs.getString("product_cd"));
						// **** E10K移行時変更対応(2006/06/08) ***
						product_names.addElement( Xysflj_Common.getStrCvt( rs.getString("product_nn") ));
//						product_names.addElement(rs.getString("product_nn"));
						// **** END ******************************
//						System.out.println("publishProductList1 = " + product_codes + "," + product_names);
					}																	// +++ next End +++
					rs.close();															// Close
					dbcom.connectionClose();											// Disconnect
				}catch(SQLException e){													// SQL Error
					throw new Exception();												// throw Exception
				}
			}else{
			for( int i=0; i<= (TorihikisakiCD.size()-1); i++ ) {
				if ( TorihikisakiCD.elementAt(i+1) == null || TorihikisakiCD.elementAt(i+1).equals("") || TorihikisakiCD.elementAt(i+1).equals("1") ) {
//					System.out.println("種別1 = " + TorihikisakiCD.elementAt(i+1));
					sql += " SELECT  商品コード product_cd         ";
					sql += "        ,商品名 product_nn             ";
					sql += " FROM    UPM_新聞社別商品              ";
					sql += " WHERE   新聞社コード = '" + publishcom_cd + "' ";
					sql += " GROUP BY  商品コード            ";
					sql += "        ,商品名            ";
					try{																	// try
						// 検索データ件数取得
//						System.out.println( sql );											// *** DEBUG ***
						ResultSet rs = dbcom.selectTable(sql);								// Execute Query
						while(rs.next()){													// +++ rs.next +++
							product_codes.addElement(rs.getString("product_cd"));
							// **** E10K移行時変更対応(2006/06/08) ***
							product_names.addElement( Xysflj_Common.getStrCvt( rs.getString("product_nn") ));
//							product_names.addElement(rs.getString("product_nn"));
							// **** END ******************************
//							System.out.println("publishProductList1 = " + product_codes + "," + product_names);
						}																	// +++ next End +++
						rs.close();															// Close
						dbcom.connectionClose();											// Disconnect
					}catch(SQLException e){													// SQL Error
						throw new Exception();												// throw Exception
					}
				} else {
//					System.out.println("Xysklj_PUBLISHBRAND=" + TorihikisakiCD.elementAt(i));
//					System.out.println("種別2 = " + TorihikisakiCD.elementAt(i+1));
					sql = " SELECT A.商品コード product_cd ";
					sql += "      , B.商品名 product_nn ";
					sql += " FROM   UPT_取引先_商品 A ";
					sql += "      , UPM_新聞社別商品 B ";
					sql += " WHERE A.取引先コード='" + TorihikisakiCD.elementAt(i) + "'";
					sql += "   AND B.新聞社コード = '" + publishcom_cd + "' ";
//					System.out.println(publishcom_cd);
					sql += "   AND A.商品コード =B.商品コード ";
					sql += " GROUP BY  A.商品コード ";
					sql += "         , B.商品名 ";
					try{																	// try
						// 検索データ件数取得
//						System.out.println( sql );											// *** DEBUG ***
						ResultSet rs = dbcom.selectTable(sql);								// Execute Query
						while(rs.next()){													// +++ rs.next +++
							product_codes.addElement(rs.getString("product_cd"));
							// **** E10K移行時変更対応(2006/06/08) ***
							product_names.addElement( Xysflj_Common.getStrCvt( rs.getString("product_nn") ));
//							product_names.addElement(rs.getString("product_nn"));
							// **** END ******************************
//							System.out.println("publishProductList2 = " + product_codes + "," + product_names);
						}																	// +++ next End +++
							rs.close();															// Close
							dbcom.connectionClose();											// Disconnect
					}catch(SQLException e){													// SQL Error
						throw new Exception();												// throw Exception
					}
				}
					i=i+1;
			}
		}

			// *************************************
			
			// * 読み込みデータをバッファに設定する
			// *************************************
//			try{																	// try
//				// 検索データ件数取得
//				ResultSet rs = dbcom.selectTable(sql);								// Execute Query

//				dbcom.connectionClose();											// Disconnect

//			}catch(SQLException e){													// SQL Error
//				throw new Exception();												// throw Exception
//			}

			return true;															// Normal return

		}catch(Exception e){														// catch
			return false;															// Abnormal return
		}																			//
	}


}
