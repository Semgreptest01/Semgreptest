package xysk;

import java.sql.*;
import java.util.*;
import java.io.*;
import javax.servlet.*;
import java.util.*;
import java.text.*;

abstract public class XysfljDBCore extends Xysflj_Dbcom
{
	static public String[] SQL_MAIN_KEYWORDS = {"SELECT","FROM","WHERE","JOIN","UNION","ORDER","GROUP"};
	static public String[] SQL_MAIN_KEYWORDS2 = {"BY"};
	static public String[] SQL_SUB_KEYWORDS = {"AND","OR"};
	static private XysfljDBCore instanceObject = null;
	protected Connection conn;
	protected Statement statmt;
	protected String systemName = "";
	
	static protected Hashtable CONNECTION_SET = null;
//	static protected Hashtable createConnectionSet(){
//		Hashtable ht = new Hashtable();
//		InputStream propertyReader = null;
//		try{
//			ClassLoader cl = ClassLoader.getSystemClassLoader();
//			propertyReader = new FileInputStream("C:\\Db.properties");
//			Properties property = new Properties();
//			property.load(propertyReader);
//			
//			ht.put("SYSTEM",new ConnectionProperty(property,"SYSTEM"));
//			ht.put("XBHS",new ConnectionProperty(property,"XBHS"));
//			ht.put("XBKK",new ConnectionProperty(property,"XBKK"));
//			ht.put("XBKM",new ConnectionProperty(property,"XBKM"));
//			ht.put("XBTY",new ConnectionProperty(property,"XBTY"));
//			ht.put("XYSK",new ConnectionProperty(property,"XYSK"));
//			ht.put("XYDL",new ConnectionProperty(property,"XYDL"));
//		}
//		catch(IOException e){
//		}
//		finally{
//			try{
//				propertyReader.close();
//			}
//			catch(Exception e){
//			}
//		}
//	
//		return ht;
//	}
	
	/*
	//シングルトンでこのクラスを使うとき
	static public XysfljDBCore getInstance()
		throws DbConnectException{
		if(null == instanceObject){
			synchronized(XysfljDBCore.class){
				if(null == instanceObject){
					instanceObject = new XysfljDBCore();
				}
			}
		}
		return instanceObject;
	}
	*/
	/**
	 * DB接続のオブジェクトを作成する。
	 * @see java.lang.Object#Object()
	 */
	public XysfljDBCore(String n)
		throws DbConnectException{
		setSystemName(n);
	}
	
	private void setSystemName(String n){
		systemName = createSystemName(n);
	}
	
	protected String createSystemName(String n){
		String s = "";
		try{
			if(n.equals("SYSTEM")){
				s = n;
			}
			else{
				s = n.substring(1,5).toUpperCase();	//システム名(例:XYDL)
			}
		}
		catch(Exception e){
			s = "ERROR";
		}
		return s;
	}
	
	public Connection DBConnect(String DB_User, String DB_Password)
			throws SQLException {
		try{
			connectDB();
		}
		catch(DbConnectException e){
			throw (SQLException)e.getException();
		}
		return conn;
	}
    public void DBDisconnect(Connection con)
        throws SQLException {
        return;
    }
    
	public Connection DBConnectByDbcom(String DB_User, String DB_Password)
			throws SQLException {
		return super.DBConnect(DB_User,DB_Password);
	}
	
//	**
//	* DBに接続する。(本番用)
//	 * URL、ユーザ名、パスワードは、
//	 * XysfljGenericRulesクラスで定義されている値を使用する。
//	 * @throws DbConnectException
//	 */
	private void connectDB()
		throws DbConnectException
		{
		try{
			if(null == conn || null == statmt){
		XysfljGenericRules.outputMessage("DB接続開始[Xysflj_Dbcom.DBConnect]");
				conn = super.DBConnect(XysfljGenericRules.DB_USER,XysfljGenericRules.DB_PASS);
				conn.setAutoCommit(false);
				statmt = conn.createStatement();
		XysfljGenericRules.outputMessage("DB接続に成功しました。\r\nSYSTEM : " + systemName);
			}
		}
		catch(SQLException e){
			XysfljGenericRules.outputMessage("DB接続に失敗しました。\r\nSYSTEM : " + systemName + "\r\n" + e.toString());
			throw new DbConnectException(e);
		}
	}
	
//	/**
//	 * DBに接続する。(単体テスト用)
//	 * URL、ユーザ名、パスワードは、
//	 * XysfljGenericRulesクラスで定義されている値を使用する。
//	 * @throws DbConnectException
//	 */
//	protected void connectDB()
//		throws DbConnectException
//	{
//		if(null == conn || null == statmt){
//			ConnectionProperty cp = getConnectionProperty();
//			connectDB(cp);
//		}
//	}
	
	protected ConnectionProperty getConnectionProperty(){
		return getConnectionProperty(systemName);
	}
	
	protected ConnectionProperty getConnectionProperty(String n){
		ConnectionProperty cp = (ConnectionProperty)CONNECTION_SET.get(n);
		if(null == cp){
			cp = new ConnectionProperty(XysfljGenericRules.DB_URL,XysfljGenericRules.DB_USER,XysfljGenericRules.DB_PASS);
		}
		return cp;
	}
	
	/**
	 * DBに接続する。(単体テスト用)
	 * URL、ユーザ名、パスワードは、
	 * XysfljGenericRulesクラスで定義されている値を使用する。
	 * @throws DbConnectException
	 */
//	abstract protected void connectDB(ConnectionProperty cp)
//		throws DbConnectException;
//
//	public ResultSet querySQL(Connection con, String sql)
//		throws SQLException {
//		Calendar startCal = Calendar.getInstance();
//		XysfljGenericRules.outputMessage("SQL実行(" + systemName + ")\r\n" + parseSQL(sql), startCal);
//		try{
//			connectDB();
//		}
//		catch(DbConnectException e){
//			throw new SQLException();
//		}
//		Rs  = super.querySQL(con, sql);
//		Calendar endCal = Calendar.getInstance();
//		long l = endCal.getTime().getTime() - startCal.getTime().getTime();
//		double d = (double)l/1000;
//		DecimalFormat df = new DecimalFormat("0.###");
//		String t = df.format(d);
//		XysfljGenericRules.outputMessage("SQL終了 " + t + "秒かかりました", endCal);
//      return Rs;
//    }

	/**
	 * 単一の ResultSet オブジェクトを返す SQL 文(SELECT文)を実行する。
	 * @param sql 通常静的 SQL SELECT 文
	 * @return 指定されたクエリーによって作成されたデータを含む ResultSet オブジェクト。null にはならない。
	 * @throws DbConnectException
	 * @throws ExecuteSQLException
	 */
	synchronized public ResultSet selectTable(String sql)
		throws DbConnectException,ExecuteSQLException
	{
		try{
			Calendar startCal = Calendar.getInstance();
			XysfljGenericRules.outputMessage("SQL実行(" + systemName + ")\r\n" + parseSQL(sql), startCal);
			connectDB();
			ResultSet result = null;
			result = statmt.executeQuery(sql);
			Calendar endCal = Calendar.getInstance();
			long l = endCal.getTime().getTime() - startCal.getTime().getTime();
			double d = (double)l/1000;
			DecimalFormat df = new DecimalFormat("0.###");
			String t = df.format(d);
			XysfljGenericRules.outputMessage("SQL終了 " + t + "秒かかりました", endCal);
			return result;
		}
		catch(SQLException e){
			throw new ExecuteSQLException(e);
		}
	}
	
	/**
	 * SQL INSERT 文、UPDATE 文、または DELETE 文を実行する。
	 * さらに、SQL DDL 文のような何も返さない SQL 文も実行できる。
	 * @param sql SQL INSERT 文、UPDATE 文、または DELETE 文、あるいは何も返さない SQL 文
	 * @return INSERT 文、UPDATE 文、DELETE 文の場合は行数。何も返さない SQL 文の場合は 0
	 * @throws DbConnectException
	 * @throws ExecuteSQLException
	 */
	synchronized public int updateTable(String sql)
		throws DbConnectException,ExecuteSQLException
	{
		try{
			connectDB();
			int result = 0;
			result = statmt.executeUpdate(sql);
			return result;
		}
		catch(SQLException e){
			throw new ExecuteSQLException(e);
		}
	}
	
	/**
	 * 直前のコミット/ロールバック以降に行われた変更をすべて有効とし、
	 * Connection が現在保持するデータベースロックをすべて解除する。
	 * @throws ExecuteSQLException
	 */
	synchronized public void commit()
		throws ExecuteSQLException
	{
		try{
			if(null != conn){
				conn.commit();
			}
		}
		catch(SQLException e){
			throw new ExecuteSQLException(e);
		}
	}
	
	/**
	 * 直前のコミット/ロールバック以降に行われた変更をすべて無効とし、
	 * この Connection が現在保持するデータベースロックをすべて解除する。
	 * @throws ExecuteSQLException
	 */
	synchronized public void rollback()
		throws ExecuteSQLException
	{
		try{
			if(null != conn){
				conn.rollback();
			}
		}
		catch(SQLException e){
			throw new ExecuteSQLException(e);
		}
	}
	
	/**
	 * 自動的な解除を待たずに、ただちに Connection のデータベースと 
	 * JDBC リソースを解除する。
	 */
	synchronized public void connectionClose(){
		statmentClose();
		try{
			if(null != conn){
XysfljGenericRules.outputMessage("DB(Connection)切断開始[Xysflj_Dbcom.DBConnect] SYSTEM : " + systemName);
				conn.close();
				conn = null;
XysfljGenericRules.outputMessage("  DB(Connection)切断に成功しました。 SYSTEM : " + systemName);
			}
		}
		catch(SQLException e){
XysfljGenericRules.outputMessage("  DB(Connection)切断に失敗しました。\r\nSYSTEM : " + systemName + "\r\n" + e.toString());
//			throw new ExecuteSQLException(e);
		}
	}
	
	/**
	 * 自動的にクローズされるときに Statement オブジェクト
	 * のデータベースと JDBC リソースが解放されるのを待つのではなく、
	 * ただちにそれらを解放する。
	 */
	synchronized public void statmentClose(){
		try{
			if(null != statmt){
XysfljGenericRules.outputMessage("DB(Statment)切断開始[Xysflj_Dbcom.DBConnect] SYSTEM : " + systemName);
				statmt.close();
				statmt = null;
XysfljGenericRules.outputMessage("  DB(Statment)切断に成功しました。 SYSTEM : " + systemName);
			}
		}
		catch(SQLException e){
XysfljGenericRules.outputMessage("  DB(Statment)切断に失敗しました。\r\nSYSTEM : " + systemName + "\r\n" + e.toString());
//			throw new ExecuteSQLException(e);
		}
	}
	
	/**
	 * java.sql.Connectionを返す
	 */
	public Connection  getConnection(){
		try{
			connectDB();
		}
		catch(DbConnectException e){
			return null;
		}

		return conn;
	}
	
	/**
	 * java.sql.Statementを返す
	 */
	public Statement getStatement(){
		try{
			connectDB();
		}
		catch(DbConnectException e){
			return null;
		}

		return statmt;
	}
	
	public String parseSQL(String sql){
		String tmp = new String(sql);
		
		StringTokenizer st = new StringTokenizer(tmp);
		
		boolean firstFlg = true;
		StringBuffer sb = new StringBuffer();
		while (st.hasMoreTokens()) {
			String t = st.nextToken();
			t = replaceKeyword(t);
			if(false == firstFlg){
				sb.append(" ");
			}
			else{
				firstFlg = false;
			}
			sb.append(t);
		}

		tmp = sb.toString();
		tmp = XysfljGenericRules.replaceString(tmp,",","\r\n\t,");

		return tmp;
	}
	
	public String replaceKeyword(String s){
		String ret = "";
		int i = 0;
		while(i < SQL_MAIN_KEYWORDS.length){
			if(s.equalsIgnoreCase(SQL_MAIN_KEYWORDS[i])){
				return "\r\n" + SQL_MAIN_KEYWORDS[i] + "\r\n";
			}
			i++;
		}

//		i = 0;
//		while(i < SQL_MAIN_KEYWORDS2.length){
//			if(s.equalsIgnoreCase(SQL_MAIN_KEYWORDS2[i])){
//				return SQL_MAIN_KEYWORDS2[i];
//			}
//			i++;
//		}

		i = 0;
		while(i < SQL_SUB_KEYWORDS.length){
			if(s.equalsIgnoreCase(SQL_SUB_KEYWORDS[i])){
				return "\r\n\t" + SQL_SUB_KEYWORDS[i];
			}
			i++;
		}
		return s;
	}
	
	public static String createInSentence(Vector v,boolean quotation){
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		
		boolean first = true;
		Iterator i = v.iterator();
		while(i.hasNext()){
			if(false == first){
				sb.append(",");
			}
			else{
				first = false;
			}
			String s = (String)i.next();
			if(quotation){
				sb.append("'");
			}
			sb.append(s);
			if(quotation){
				sb.append("'");
			}
		}
		
		sb.append(")");
		return sb.toString();
	}
	
	static public class ConnectionProperty{
		String url;
		String user;
		String pass;
		
		public ConnectionProperty(Properties p,String key){
			url = p.getProperty(key + ".URL",XysfljGenericRules.DB_URL);
			user = p.getProperty(key + ".USER",XysfljGenericRules.DB_USER);
			pass = p.getProperty(key + ".PASS",XysfljGenericRules.DB_PASS);
		}
		public ConnectionProperty(String l,String u,String p){
			url = l;
			user = u;
			pass = p;
		}
		
		/**
		 * Returns the pass.
		 * @return String
		 */
		public String getPass() {
			return pass;
		}

		/**
		 * Returns the url.
		 * @return String
		 */
		public String getUrl() {
			return url;
		}

		/**
		 * Returns the user.
		 * @return String
		 */
		public String getUser() {
			return user;
		}

		/**
		 * Sets the pass.
		 * @param pass The pass to set
		 */
		public void setPass(String pass) {
			this.pass = pass;
		}

		/**
		 * Sets the url.
		 * @param url The url to set
		 */
		public void setUrl(String url) {
			this.url = url;
		}

		/**
		 * Sets the user.
		 * @param user The user to set
		 */
		public void setUser(String user) {
			this.user = user;
		}

	}
	
	static public class DBException extends XysfljException{
		public DBException(SQLException e){
			super(e);
		}
	}
	
	static public class DbConnectException extends DBException{
		public DbConnectException(SQLException e){
			super(e);
		}
	}
	
	static public class ExecuteSQLException extends DBException{
		public ExecuteSQLException(SQLException e){
			super(e);
		}
	}
	
	static public class ResultAnalyzeException extends DBException{
		public ResultAnalyzeException(SQLException e){
			super(e);
		}
	}
}
