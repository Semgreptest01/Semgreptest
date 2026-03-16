package xysk;

import java.text.DecimalFormat;

/**
 * @author user
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class XysfljMasterDB extends XysfljDB {
	public static final String MASTER_ID = "SYSTEM";
//	private static final String SYSTEM_DB_URL = "";
//	private static final String SYSTEM_DB_USER = "";
//	private static final String SYSTEM_DB_PASS = "";
	
	private String slaveId = "";
	private String slaveOrganizationCode = XysfljGenericRules.FIND_SLAVE_SINGLE;
	private XysfljSlaveDB slaveDbConnection = null;
	private boolean multiplexFlg = false;
	
	/**
	 * Constructor for XysfljSystemDB.
	 * @param n
	 * @throws DbConnectException
	 */
	public XysfljMasterDB(String sId) throws XysfljDB.DbConnectException {
		this(sId,false);
	}
	public XysfljMasterDB(String sId, boolean b) throws DbConnectException {
		super(MASTER_ID);
		slaveId = sId;
		multiplexFlg = b;
	}
	
	/**
	 * スレーブのDBコネクションを作成する。
	 * @param 接続対象の組織コード
	 * @return スレーブのDBコネクション
	 * @throws DbConnectException
	 */
	public XysfljSlaveDB createSlaveConnection() throws XysfljDB.DbConnectException {
		if(null == slaveDbConnection){
			ConnectionProperty x = createSlaveConnectionProperty();
			slaveDbConnection = new XysfljSlaveDB(slaveId,x);
		}
		return slaveDbConnection;
	}
	
	/**
	 * 検索対象の多重番号(スキーマの番号)を返す。
	 * @param 検索対象の組織コード
	 * @return 多重番号(000)
	 */
	private String getMultiplexNo(String code){
		if(null == code){
			code = XysfljGenericRules.FIND_SLAVE_SINGLE;
		}

//		if(code.equals(XysfljGenericRules.FIND_SLAVE_SINGLE)){
//			return null;
//		}

		int n = Xysflj_Common.Get_Connect_No(this,conn,code);
		DecimalFormat df = new DecimalFormat("000");
		String s = df.format(n);
		return s;
	}
	
	/**
	 * スレーブ接続のConnectionPropertyを作成する。
	 * @param 接続する組織コード
	 * @return ConnectionProperty
	 */
	private ConnectionProperty createSlaveConnectionProperty(){
		ConnectionProperty cp = null;
		if(null != slaveOrganizationCode && slaveOrganizationCode.equals(XysfljGenericRules.FIND_NO_ORG)){
			cp = new ConnectionProperty(XysfljGenericRules.DB_URL_SLAVE_SINGLE,
											XysfljGenericRules.DB_USER_SLAVE_SINGLE,
											XysfljGenericRules.DB_PASS_SLAVE_SINGLE);
		}
		else{		//初期接続で多重と関係がないコンボボックス作成にも使用する(XysfljGenericRules.FIND_SLAVE_SINGLE)
			String s = createSystemName(slaveId);
			cp = getConnectionProperty(s);
			if(false == slaveOrganizationCode.equals(XysfljGenericRules.FIND_SLAVE_SINGLE)){
				synchronized(Xysflj_Common.THIN_DRIVER_URL){
					String multiplexCode = getMultiplexNo(slaveOrganizationCode);
					if(null != multiplexCode){
						StringBuffer u = new StringBuffer(cp.getUser());
						StringBuffer p = new StringBuffer(cp.getPass());
						
						int l0 = multiplexCode.length();
						int l1 = u.length();
						int l2 = p.length();
						u.replace(l1-l0,l1,multiplexCode);
						p.replace(l2-l0,l2,multiplexCode);
						
						String sUrl = Xysflj_Common.THIN_DRIVER_URL;
						String sUser = u.toString();
						String sPass = p.toString();
						
						cp.setUrl(sUrl);
						cp.setUser(sUser);
						cp.setPass(sPass);
					}
				}
			}
		}
		return cp;
	}

	
	/**
	 * 検索対象の組織コードをセットする。
	 * @param 組織コード
	 */
	public void setSlaveOrganizationCode(String s){
		if(null == s){
			s = XysfljGenericRules.FIND_SLAVE_SINGLE;
		}
		if(false == slaveOrganizationCode.equals(s)){
			slaveOrganizationCode = s;
			disconnectSlave();
		}
	}
	
	/**
	 * スレーブ接続を切断する。
	 */
	private void disconnectSlave(){
		if(null != slaveDbConnection){
			slaveDbConnection.statmentClose();
			slaveDbConnection.connectionClose();
			slaveDbConnection = null;
		}
	}
}
