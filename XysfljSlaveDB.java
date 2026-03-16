package xysk;

/**
 * @author user
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class XysfljSlaveDB extends XysfljDB {
	public final String ID;
	private ConnectionProperty connectionProperty = null;

	/**
	 * Constructor for XysfljSlaveDB.
	 * @param n
	 * @throws DbConnectException
	 */
	public XysfljSlaveDB(String n) throws XysfljDB.DbConnectException {
		this(n,null);
	}

	public XysfljSlaveDB(String n, ConnectionProperty cp) throws XysfljDB.DbConnectException {
		super(n);
		ID = n;
		connectionProperty = cp;
	}

	/**
	 * DBに接続する。
	 * URL、ユーザ名、パスワードは、
	 * XysfljGenericRulesクラスで定義されている値を使用する。
	 * @throws DbConnectException
	 */
	protected void connectDB()
		throws DbConnectException
	{
		if(null == conn || null == statmt){
			connectDB(connectionProperty);
		}
	}
}
