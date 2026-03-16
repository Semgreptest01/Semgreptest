package xysk;

import java.sql.*;
import java.util.*;
import java.io.*;
import javax.servlet.*;
import java.util.*;
import java.text.*;

public class XysfljDB extends XysfljDBCore
{
	{
		CONNECTION_SET = createConnectionSet();
	}
	static protected Hashtable createConnectionSet(){
		Hashtable ht = new Hashtable();
		ht.put("XYSK",new ConnectionProperty("jdbc:oracle:thin:@10.64.8.29:1523:LE6B","hxy022","hxy022"));
//		ht.put("XYSK",new ConnectionProperty("jdbc:oracle:thin:@10.173.6.107:1521:ORCL","hxy022","hxy022"));
//		ht.put("XYSK",new ConnectionProperty("jdbc:oracle:thin:@10.64.8.40:1521:LE4A","hxy022","hxy022"));
		return ht;
	}

	/**
	 * DB接続のオブジェクトを作成する。
	 * @see java.lang.Object#Object()
	 */
	public XysfljDB(String n)
		throws DbConnectException{
		super(n);
	}

	/**
	 * DBに接続する。(単体テスト用)
	 * URL、ユーザ名、パスワードは、
	 * XysfljGenericRulesクラスで定義されている値を使用する。
	 * @throws DbConnectException
	 */
	protected void connectDB(ConnectionProperty cp)
		throws DbConnectException
	{
		if(null == conn || null == statmt){
			try{
//XysfljGenericRules.outputMessage("DB接続開始[Xysflj_Dbcom.DBConnect]");
//				if(Xysflj_Common.CON_SW.equals("2")){
//XysfljGenericRules.outputMessage("DB接続を行います。\r\nSYSTEM : " + systemName + "\r\nURL : " + cp.getUrl() + "\r\nUSER : " + cp.getUser() + "\r\nP//ASS : " + cp.getPass());
//				}
				Xysflj_Common.THIN_DRIVER_URL = cp.getUrl();
				conn = DBConnectByDbcom(cp.getUser(),cp.getPass());
				conn.setAutoCommit(false);
				statmt = conn.createStatement();
//XysfljGenericRules.outputMessage("DB接続に成功しました。\r\nSYSTEM : " + systemName);
			}
			catch(java.sql.SQLException e)
			{
				//SQLエラー
				XysfljGenericRules.outputMessage("DB接続に失敗しました。\r\nSYSTEM : " + systemName + "\r\n" + e.toString());
				throw new DbConnectException(e);
			}
		}
	}
}
