/*
 *
 *  修正履歴
 *  L001  2006/12/01 S.Sawada   発売日２重表示対応（メソッド追加）
 *
 */
package xysk;

import java.sql.*;
import javax.servlet.http.HttpServletRequest;
import xysk.Xysflj_Common;

/**
 * @author user
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class XysfljCommonCombo extends XysfljComboTag{
	final static public int TYPE = XysfljParameters.ESSENTIAL;

	final static String TOTAL = "";
	public XysfljCommonCombo (XysfljParameters p,String comboName){
		super(comboName,p);
	}
	
	protected void getData(String sql,String code,String name)
		throws XysfljDB.DBException{
		try{
			XysfljDB tdb = param.getDbConnection();

			ResultSet rs = tdb.selectTable(sql);
			while(null != rs && rs.next()){
//				comboElements.put(rs,code,name);
				comboElements.put(rs,code,Xysflj_Common.getStrCvt(name));
			}
			if ( null != rs ) {
				rs.close();
			}
		}
		catch(SQLException e){
			//rs.nextのエラー
			throw new XysfljDB.ResultAnalyzeException(e);
		}
	}

//L001 ADDS
	protected void getsaleData(String sql,String code,String codeZ)
		throws XysfljDB.DBException{
		try{
			XysfljDB tdb = param.getDbConnection();

			ResultSet rs = tdb.selectTable(sql);
			while(null != rs && rs.next()){
				if((rs.getString("jan").substring(0,3)).equals("491")){
					comboElements.put(rs,codeZ,Xysflj_Common.getStrCvt(codeZ));
				}else{
					comboElements.put(rs,code,Xysflj_Common.getStrCvt(code));
				}
			}
			if ( null != rs ) {
				rs.close();
			}
		}
		catch(SQLException e){
			//rs.nextのエラー
			throw new XysfljDB.ResultAnalyzeException(e);
		}
	}
//L001 ADDE

	protected void getData_New(String sql,String code,String name)
		throws XysfljDB.DBException{
		try{
			XysfljDB tdb = param.getDbConnection();

			ResultSet rs = tdb.selectTable(sql);
			String CD_Name = "";
			while(null != rs && rs.next()){
				comboElements.putCodeName(rs,code,name);
//				comboElements.put(rs,code,Xysflj_Common.getStrCvt(name));
			}
			if ( null != rs ) {
				rs.close();
			}
		}
		catch(SQLException e){
			//rs.nextのエラー
			throw new XysfljDB.ResultAnalyzeException(e);
		}
	}

	protected void getDataCodeName(String sql,String code,String name)
		throws XysfljDB.DBException{
		try{
			XysfljDB tdb = param.getDbConnection();

			ResultSet rs = tdb.selectTable(sql);
			while(null != rs && rs.next()){
				comboElements.putCodeName(rs,code,name);
			}
			if ( null != rs ) {
				rs.close();
			}
		}
		catch(SQLException e){
			//rs.nextのエラー
			throw new XysfljDB.ResultAnalyzeException(e);
		}
	}


}

