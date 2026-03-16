package xysk;

import java.util.Calendar;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.text.DecimalFormat;
import java.io.IOException;
import java.io.Writer;

public class XysfljTableTag{
	protected Writer out = null;
	protected int border = 1;
	protected int cellpadding = 0;
	protected int cellspacing = 0;
	protected String bgcolor = "#F0FFFF";
	protected String essentialStyle = "table-layout:fixed";
	protected String style = essentialStyle;
	protected String headBgColor = "#C0C0C0";
//	protected int cursor = 0;
//	protected TableData currentTableData = null;
	
	//テーブル表示の表示領域の設定(どちらかに0が入っていた場合は表示領域をセットしない)
	private int divWidth = 0;
	private int divHeight = 0;
	
	/**
	 * テーブルの最低出力行数。<BR>
	 * 要素データがこの値に満たないときは、残りを空行で出力される。
	 */
	protected int minLines = 0;
	
	/**
	 * 印刷時のテーブルカラム幅倍率
	 */
	protected double printReduction = 0.68;
	
	/**
	 * テーブルのカラム定義オブジェクト(LinePaternのサブクラス)を登録するVector。
	 * タグ出力前に必ずオブジェクトを追加しなければならない。
	 */
	protected Vector tableHeaders = new Vector();
	
	/**
	 * テーブルの要素データを登録するVector。
	 */
	protected Vector tableElements = new Vector();
	
	/**
	 * テーブルの行定義オブジェクト(LinePaternのサブクラス)を登録する変数。
	 * タグ出力前に必ずオブジェクトを代入しなければならない。
	 */
	public LinePatern headRow = new LinePatern();
	
	/**
	 * テーブルの行定義オブジェクト(LinePaternのサブクラス)を登録する変数。
	 * タグ出力前に必ずオブジェクトを代入しなければならない。
	 */
	public LinePatern dataRow;
	
	/**
	 * ページ番号(0から始まる)。
	 */
	private int pageNum = 0;
	
	/**
	 * 最大データ件数。ページ計算に使用される。
	 */
	private int maxData = 0;
	
	/**
	 * テーブルの名前。
	 */
	private final String NAME;
	
	/**
	 * ページのParametersオブジェクトを保存する変数。
	 */
	protected XysfljParameters parameters;
	
	/**
	 * 1ページに印刷する行数。
	 */
	private int printLines = 0;					//1ページに印刷する行数
	private int nowPrintPage = 0;
	private int printStartLine = 0;
	/**
	 * コンストラクタ
	 * @param n テーブルの名前
	 * @param p ページのPrametersオブジェクト
	 */
	protected XysfljTableTag(String n,XysfljParameters p){
		this(n,p,null);
	}

	/**
	 * Constructor XysfljTableTag.
	 * @param n
	 * @param p
	 * @param o
	 */
	public XysfljTableTag(String n, XysfljParameters p, Writer o) {
		initPrintStartLine();	//印刷ページの初期化
		NAME = n;
		parameters = p;
		String pageString = p.getRequestParameter(NAME+"Page");
		try{
			pageNum=Integer.parseInt(pageString);
		}
		catch(Exception e){
			pageNum=0;
		}
		out = o;
	}

	
	/**
	 * テーブルのデータ件数をセットする。<BR>
	 * ここでセットした値は、前次ページの存在チェックに使用される。
	 * @param v レコードの件数
	 */
	protected void setMaxData(int v){
		maxData = v;
	}
	
	/**
	 * ページを1つ進める。
	 */
	protected void pageNext(){
		pageNum++;
	}

	/**
	 * 次のページが存在するかチェックする。<BR>
	 * setMinLinesメソッドでセットされた値と、setMaxDataメソッドでセットされた値を元に演算を行う。
	 * @return 次のページが存在する場合はtrue、存在しない場合はfalseを返す
	 */
	public boolean hasPageNext(){
		try{
			int d = 0;
			d = ((maxData-1)/minLines);
			if(pageNum < d){
				return true;
			}
		}
		catch(Exception e){
			return false;
		}
		return false;
	}
	
	/**
	 * ページを1つ前に戻す。
	 */
	protected void pagePrev(){
		pageNum--;
	}
	
	/**
	 * 1つ前のページが存在するかチェックする。
	 * @return 1つ前のページが存在する場合はtrue、存在しない場合はfalseを返す
	 */
	public boolean hasPagePrev(){
		try{
			if(0 < pageNum){
				return true;
			}
		}
		catch(Exception e){
			return false;
		}
		return false;
	}
	
	/**
	 * ページ番号を戻す。
	 * @return 現在のページ番号
	 */
	protected int getPageNum(){
		return pageNum;
	}
	
	/**
	 * ページ番号をセットする。
	 * @param p ページ番号に設定する値
	 */
	protected void setPageNum(int p){
		pageNum = p;
	}
	protected int getMinLines(){
		return minLines;
	}
	
	/**
	 * 表示させるデータ部分の最大行数。<BR>
	 * サブクラスで、データ取得SQL実行時の件数を指定するときに使われる値をセットする。
	 * 
	 * @param l 表示させる行数
	 */
	public void setMinLines(int l){
		minLines = l;
	}
	
//	/**
//	 * createDataTagで操作している行を返す。<BR>
//	 * 主に、継承先クラスのLinePaternクラスで行によって出力タグを変更する時、使用します。
//	 * @return 現在、操作されている行の行番号
//	 */
//	protected int getCursor(){
//		return cursor;
//	}

	public void setStyle(String v){
		style = essentialStyle + ";" + v;
	}
	protected String getStyle(){
		return " style=\"" + style + "\"";
	}
	
	public String createStartTagWithFrame(String f){
		StringBuffer tag = new StringBuffer();
		tag.append("<INPUT type='hidden' name='");
		tag.append(NAME);
		tag.append("Page' value='");
		tag.append(String.valueOf(pageNum));
		tag.append("'>\r\n");

		tag.append("<TABLE");
		tag.append(" id=\"" + NAME + "\"");
		tag.append(" border=\"" + border + "\"");
		tag.append(" cellpadding=\"" + cellpadding + "\"");
		tag.append(" cellspacing=\"" + cellspacing + "\"");
		tag.append(" bgcolor=\"" + bgcolor + "\"");
		tag.append(getStyle());
		if(false == f.equals("")){
			tag.append(" FRAME=\"" + f + "\"");
		}
		tag.append(">\r\n");
		
		tag.append("\t<!--THEAD START-->\r\n");
		Enumeration headers = tableHeaders.elements();
		while(headers.hasMoreElements()){
			LinePatern l = (LinePatern)headers.nextElement();
			int w = l.getWidth();
			if(null != parameters &&
				XysfljParameters.COMMAND_PRINT == parameters.getCommandType()){
				w = (int)(((double)w)*printReduction);
			}
			tag.append("\t<COL width=\"" + w + "\">\r\n");
		}
		tag.append("\t<!--THEAD END-->\r\n");
		tag.append("\t<TBODY>\r\n");
		return tag.toString();
	}
	
	/**
	 * テーブルタグの内、カラム幅の定義までを出力する。<BR>
	 * <TABLE border="0" cellpadding="0" cellspacing="0">
	 * <TR>
	 * <TD Valign="top" width="35">例)</TD>
	 * <TD>
	 * &lt;TABLE border=&quot;1&quot; cellpadding=&quot;0&quot; cellspacing=&quot;0&quot; bgcolor=&quot;#F0FFFF&quot; style=&quot;table-layout:fixed&quot;&gt;<BR>
	 * &lt;!--THEAD START--&gt;<BR>
	 * &lt;COL width="71"&gt;<BR>
	 * &lt;COL width="76"&gt;<BR>
	 * &lt;COL width="76"&gt;<BR>
	 * &lt;!--THEAD END--&gt;<BR>
	 * </TD>
	 * </TR>
	 * </TABLE>
	 * @return &lt;TABLE&gt;から、&lt;COL width="99"&gt;まで。
	 */
	public String createStartTag(){
		return createStartTagWithFrame("");
	}
	
	/**
	 * テーブルのデータ行を出力するタグを返す。
	 * &lt;TR&gt;から&lt;/TR&gt;まで(1行)のタグをテーブルの要素(tableElements)
	 * の数、出力する。
	 * @return データ部分のタグ
	 * @throws NoDataException
	 * @throws SpanException
	 */
	public void writeDataTag()
		throws NoDataException,SpanException,IOException{
		boolean spanFlg = false;	//スパンが発生している
		
		int printedLines = 0;

		int recordSize = tableElements.size();	//レコードのかず
		int end = printStartLine + printLines;
		int cursor = 0;
		Enumeration records = tableElements.elements();
		TableData data = null;
		while(records.hasMoreElements()){
			
			data = (TableData)records.nextElement();
			
			//printLinesが0のときは、全部印刷する
			if(0 == printLines || printStartLine <= cursor && cursor < end || true == spanFlg){
				StringBuffer sb = new StringBuffer();
				
				sb.append("\t\t<TR");
				sb.append(dataRow.getOptionTag("",cursor,data));
				sb.append(">\r\n");
	
				spanFlg = false;
				
				Enumeration column = tableHeaders.elements();
				while(column.hasMoreElements()){
					LinePatern l = (LinePatern)column.nextElement();
					boolean rowSpanFlg = l.rowspanCheck();
					boolean colSpanFlg = dataRow.colspanCheck();
					
					if(false == rowSpanFlg && false == colSpanFlg){
						String value = data.get(l.getName());
						sb.append("\t\t\t<TD");
						sb.append(" " + l.getOptionTag(value,cursor,data));
						sb.append(">\r\n");
						sb.append("\t\t\t\t" + l.editValueWithFontTag(value,cursor,data));
						sb.append("<BR>\r\n\t\t\t</TD>\r\n");
					}
					
					if(true == l.isRowspan()){
						spanFlg = true;
					}
				}
				sb.append("\t\t</TR>\r\n");
				printedLines++;
				out.write(sb.toString());
			}
			cursor++;
		}

		//空行を出力する
		while(cursor < minLines){
			out.write(createEmptyLine(cursor));
			cursor++;
		}
		
		printStartLine += printedLines;
	}
	
	/**
	 * 表の空行を作成する。
	 * @param cursor
	 * @param data
	 * @return String
	 */
	private String createEmptyLine(int cursor)
		throws NoDataException,SpanException{
		StringBuffer tag = new StringBuffer();
		tag.append("\t\t<TR");
		tag.append(dataRow.getOptionTag("",cursor,null));
		tag.append(">\r\n");

		Enumeration column = tableHeaders.elements();
		while(column.hasMoreElements()){
			LinePatern l = (LinePatern)column.nextElement();
			if(false == l.rowspanCheck()){
				tag.append("\t\t\t<TD><BR></TD>\r\n");
			}
		}
		tag.append("\t\t</TR>\r\n");
		
		return tag.toString();
	}
	
	/**
	 * テーブルのデータ行を出力するタグを返す。
	 * &lt;TR&gt;から&lt;/TR&gt;まで(1行)のタグをテーブルの要素(tableElements)
	 * の数、出力する。
	 * @return データ部分のタグ
	 * @throws NoDataException
	 * @throws SpanException
	 */
	public void writeCsv()
		throws NoDataException,SpanException,IOException{
		int printedLines = 0;

		int recordSize = tableElements.size();	//レコードのかず
		int end = printStartLine + printLines;
		int cursor = 0;
		Enumeration records = tableElements.elements();
		TableData data = null;
		while(records.hasMoreElements()){
			
			data = (TableData)records.nextElement();
			
			//printLinesが0のときは、全部印刷する
			if(0 == printLines || printStartLine <= cursor && cursor < end){
				StringBuffer sb = new StringBuffer();
				
				boolean firstCol = true;
				Enumeration column = tableHeaders.elements();
				while(column.hasMoreElements()){
					LinePatern l = (LinePatern)column.nextElement();
					boolean rowSpanFlg = l.rowspanCheck();
					boolean colSpanFlg = dataRow.colspanCheck();
					
					if(firstCol){
						firstCol = false;
					}
					else{
						sb.append(",");
					}
					sb.append("\"");
					if(false == rowSpanFlg && false == colSpanFlg){
						String value = data.get(l.getName());
						sb.append(l.editValue(value,cursor,data));
					}
					else{
						sb.append("");
					}
					sb.append("\"");
				}
				sb.append("\r\n");
				printedLines++;
				out.write(sb.toString());
			}
			cursor++;
		}
		
		printStartLine += printedLines;
	}
	
	/**
	 * テーブルのデータ行を出力するタグを返す。
	 * &lt;TR&gt;から&lt;/TR&gt;まで(1行)のタグをテーブルの要素(tableElements)
	 * の数、出力する。
	 * @return データ部分のタグ
	 * @throws NoDataException
	 * @throws SpanException
	 */
	public String createDataTag()
		throws NoDataException,SpanException{
		StringBuffer tag = new StringBuffer();

		int cursor = 0;
		TableData data = null;
		Enumeration records = tableElements.elements();
		while(records.hasMoreElements()){
			data = (TableData)records.nextElement();
			String lineTag = createLineTag(cursor, data);	//1行分のタグを作成
			tag.append(lineTag);
			cursor++;
		}
		while(cursor < minLines){
			tag.append("\t\t<TR");
			tag.append(dataRow.getOptionTag("",cursor,data));
			tag.append(">\r\n");

			Enumeration column = tableHeaders.elements();
			while(column.hasMoreElements()){
				LinePatern l = (LinePatern)column.nextElement();
				if(false == l.rowspanCheck()){
					tag.append("\t\t\t<TD><BR></TD>\r\n");
				}
			}
			tag.append("\t\t</TR>\r\n");
			cursor++;
		}
		
		return XysfljGenericRules.convertParticularChar(tag.toString());
	}
	
	public String createLineTag(int cursor, TableData data)
		throws NoDataException,SpanException{
		StringBuffer tag = new StringBuffer();

			tag.append("\t\t<TR");
			tag.append(dataRow.getOptionTag("",cursor,data));
			tag.append(">\r\n");

			Enumeration column = tableHeaders.elements();
			while(column.hasMoreElements()){
				LinePatern l = (LinePatern)column.nextElement();
				boolean rowSpanFlg = l.rowspanCheck();
				boolean colSpanFlg = dataRow.colspanCheck();
				if(false == rowSpanFlg && false == colSpanFlg){
					String value = data.get(l.getName());
					tag.append("\t\t\t<TD");
					tag.append(" " + l.getOptionTag(value,cursor,data));
					tag.append(">\r\n");
					tag.append("\t\t\t\t" + l.editValueWithFontTag(value,cursor,data));
					tag.append("<BR>\r\n\t\t\t</TD>\r\n");
				}
			}
			tag.append("\t\t</TR>\r\n");

		return tag.toString();
	}

	/**
	 * 1ページに印刷する行数をセットする<BR>
	 */
	public void setPrintLines(int v){
		printLines = v;					//1ページに印刷する行数
	}

	/**
	 * 印刷するページ数を取得<BR>
	 */
	public int getPrintPageSize(){
		if(0 == printLines){
			return 1;
		}
		
		int recordSize = tableElements.size();	//レコードの数
		int x = 0;
		if(0 < recordSize % printLines){
			x=1;
		}
		return (recordSize / printLines) + x;
	}

	/**
	 * 印刷するページが残っているか?<BR>
	 */
	public boolean hasMorePrintPage(){
		if(0 == nowPrintPage ||							//最初のページのとき
				printStartLine < tableElements.size()){		//未出力のデータがある場合
			return true;
		}
		return false;
	}

	/**
	 * &lt;TABLE&gt; ・・・&lt;/TABLE&gt;の部分(TABLEのタグを含む)が出力される。<BR>
	 * テーブルタグの出力形式を変更したい場合は、サブクラスで、このメソッドをオーバーライドする。
	 * @return テーブルタグ
	 * @throws NoDataException
	 * @throws SpanException
	 */
	public String createTagForPrint()
		throws NoDataException,SpanException{

		XysfljGenericRules.outputMessage("テーブルタグ出力 開始");
		StringBuffer tag = new StringBuffer();
		tag.append(createStartTag());
		tag.append(createHeadTag());
		tag.append(createDataTagForPrint(nowPrintPage));
		tag.append(createEndTag());
		nowPrintPage++;
		if(hasMorePrintPage()){
			tag.append("<OBJECT width=\"1\" height=\"1\" style=\"page-break-before:always;\"></OBJECT>");
		}
		XysfljGenericRules.outputMessage("テーブルタグ出力 終了");
		return XysfljGenericRules.exchangeOutString(tag);
	}
	
	/**
	 * テーブルのデータ行を出力するタグを返す。
	 * &lt;TR&gt;から&lt;/TR&gt;まで(1行)のタグをテーブルの要素(tableElements)
	 * の数、出力する。
	 * @return データ部分のタグ
	 * @throws NoDataException
	 * @throws SpanException
	 */
	public String createDataTagForPrint(int page)
		throws NoDataException,SpanException{
		StringBuffer tag = new StringBuffer();
		boolean spanFlg = false;	//スパンが発生している
		
		int printedLines = 0;

		int recordSize = tableElements.size();	//レコードのかず
		int end = printStartLine + printLines;
		int cursor = 0;
		Enumeration records = tableElements.elements();
		while(records.hasMoreElements()){
			
			TableData data = (TableData)records.nextElement();
			
			//printLinesが0のときは、全部印刷する
			if(0 == printLines || printStartLine <= cursor && cursor < end || true == spanFlg){
				tag.append("\t\t<TR");
				tag.append(dataRow.getOptionTag("",cursor,data));
				tag.append(">\r\n");
	
				spanFlg = false;
				
				Enumeration column = tableHeaders.elements();
				while(column.hasMoreElements()){
					LinePatern l = (LinePatern)column.nextElement();
					boolean rowSpanFlg = l.rowspanCheck();
					boolean colSpanFlg = dataRow.colspanCheck();
					
					if(false == rowSpanFlg && false == colSpanFlg){
						String value = data.get(l.getName());
						tag.append("\t\t\t<TD");
						tag.append(" " + l.getOptionTag(value,cursor,data));
						tag.append(">\r\n");
						tag.append("\t\t\t\t" + l.editValueWithFontTag(value,cursor,data));
						tag.append("<BR>\r\n\t\t\t</TD>\r\n");
					}
					
					if(true == l.isRowspan()){
						spanFlg = true;
					}
				}
				tag.append("\t\t</TR>\r\n");
				printedLines++;
			}
			cursor++;
		}
		
		printStartLine += printedLines;
		return tag.toString();
	}
	
	protected void initPrintStartLine(){
		printStartLine = 0;
	}
	
	/**
	 * テーブルの閉じタグを返す。
	 * @return &lt;/TBODY&gt;&lt;/TABLE&gt;
	 */
	public String createEndTag(){
		return "\t</TBODY>\r\n</TABLE>\r\n";
	}
	public String createHeadTag(){
		StringBuffer tag = new StringBuffer();
		Enumeration headers = tableHeaders.elements();
		tag.append("\t\t<TR");
		if(false == headBgColor.equals("")){
			tag.append(" bgcolor=\"");
			tag.append(headBgColor);
			tag.append("\"");
		}
		tag.append(" align=\"center\">\r\n");
		while(headers.hasMoreElements()){
			LinePatern l = (LinePatern)headers.nextElement();
			boolean rowSpanFlg = l.rowspanCheck();
			boolean colSpanFlg = headRow.colspanCheck();
			if(false == rowSpanFlg && false == colSpanFlg){
				tag.append("\t\t\t<TD");
				tag.append(l.getHeadOptionTag());
				tag.append(">");
				tag.append(l.getLabel());
				tag.append("<BR></TD>\r\n");
			}
		}
		tag.append("\t\t</TR>\r\n");
		return XysfljGenericRules.exchangeOutString(tag);
	}
	public String createBottomLineTag(){
		StringBuffer tag = new StringBuffer();
		tag.append(createStartTag());
		
		tag.append("\t\t<TR></TR>\r\n");
		
		tag.append(createEndTag());
		return XysfljGenericRules.exchangeOutString(tag);
	}
	
	/**
	 * &lt;TABLE&gt; ・・・&lt;/TABLE&gt;の部分(TABLEのタグを含む)が出力される。<BR>
	 * テーブルタグの出力形式を変更したい場合は、サブクラスで、このメソッドをオーバーライドする。
	 * @return テーブルタグ
	 * @throws NoDataException
	 * @throws SpanException
	 */
	public String createTag()
		throws NoDataException,SpanException{
		if(XysfljParameters.COMMAND_PRINT != parameters.getCommandType()){
			XysfljGenericRules.outputMessage("テーブルタグ出力 開始");
			StringBuffer tag = new StringBuffer();
			tag.append(createStartTag());
			tag.append(createHeadTag());
			tag.append(createDataTag());
			tag.append(createEndTag());
			XysfljGenericRules.outputMessage("テーブルタグ出力 終了");
			return XysfljGenericRules.exchangeOutString(tag);
		}
		else{
			return createTagForPrint();
		}
	}

	public void writeTag(){
		if(null == out){
			parameters.addMessageByCode(1000);
			parameters.addMessage("Writerがセットされていません");
			parameters.executeErrorRoutine(new TableException());
		}
		
		try{
			boolean dived = checkDiv();
			Calendar startCal = Calendar.getInstance();
			XysfljGenericRules.outputMessage("テーブルタグ出力(directWriteTag) 開始",startCal);
			out.write(createStartTag());
			out.write(createHeadTag());
			if(dived){	//表示範囲を指定されていた場合
				out.write(createEndTag());
				int w = getDivWidth();
				int h = getDivHeight();
				out.write("<DIV style=\"width:" + w + "px;height:" + h + "px; overflow:auto;\">");
				out.write(createStartTag());
			}
			writeDataTag();
			out.write(createEndTag());
			if(dived){	//表示範囲を指定されていた場合
				out.write("</DIV>");
			}
			nowPrintPage++;
			if(hasMorePrintPage()){
				out.write("<OBJECT width=\"1\" height=\"1\" style=\"page-break-before:always;\"></OBJECT>");
			}
			Calendar endCal = Calendar.getInstance();
			long l = endCal.getTime().getTime() - startCal.getTime().getTime();
			double d = (double)l/1000;
			DecimalFormat df = new DecimalFormat("0.###");
			String t = df.format(d);
			XysfljGenericRules.outputMessage("テーブルタグ出力(directWriteTag) 終了 " + t +"秒かかりました。",endCal);
		}
		catch(Throwable e){
			XysfljGenericRules.outputMessage("directWriteTagメソッドでExceptionが発生しました。\r\n" + e.toString());
		}
	}
	
	/**
	 * テーブルの属性に、カラム定義を追加する。<BR>
	 * サブクラスで使用する。
	 * @param val
	 */
	protected void addHeader(LinePatern val){
		tableHeaders.add(val);
	}
	
//	public TableData getCurrentTableData(){
//		return currentTableData;
//	}

	/**
	 * 印刷イメージのカラム幅倍率をセットする。<BR>
	 * カラム幅倍率の初期値は0.68。
	 * @param double d
	 */
	public void setPrintReduction(double d){
		printReduction = d;
	}
	
	/**
	 * 印刷引数の値を印刷のときは、引数を計算した値を戻す。<BR>
	 */
	private int calcWidth(int i){
		int w = i;
		if(null != parameters &&
			XysfljParameters.COMMAND_PRINT == parameters.getCommandType()){
			w = (int)(((double)w)*printReduction);
		}
		return w;
	}
	
	private boolean checkDiv(){
		//横、縦ともに値がセットされているときはtrue
		if(0 < divWidth && 0 < divHeight){
			return true;
		}
		return false;
	}
	
	/**
	 * 表示領域をセットする
	 * @param 幅
	 * @param 高さ
	 */
	public void setDiv(int w,int h){
		divWidth = w;
		divHeight = h;
	}
	
	private int getDivWidth(){
		int x = calcWidth(divWidth);
		return x;
	}
	
	private int getDivHeight(){
		int x = calcWidth(divHeight);
		return x;
	}

	static public String editMoneyValue(String v){
		return editMoneyValue(v, 1);
	}
	
	static public String editMoneyValue(String v, int r){
		double d = 0;
		long l = 0;
		try{
			d = Double.parseDouble(v);
			d /= (double)r;
			if(d < 0){	//値が負の場合 (-0.5の四捨五入を-1にする処理)
				d *= -1;
				l = Math.round(d);
				l *= -1;
			}
			else{		//値が負ではない場合
				l = Math.round(d);
			}
		}
		catch(NumberFormatException e){
			return "";
		}
		String s = String.valueOf(l);
		String sign = "";
		if('-' == s.charAt(0)){
			sign = "-";
			s = s.substring(1,s.length());
		}
		StringBuffer sb = new StringBuffer(s);
		for(int i = s.length()-3 ; 0<i ; i-=3){
			sb.insert(i,',');
		}
		sb.insert(0,sign);
		s = sb.toString();
		return s;
	}

	static public String editRateValue(String val)
		throws IllegalArgumentException{
		double d = Double.parseDouble(val);
		d *= 100;
		d = Math.round(d);
		d /= 100;
		DecimalFormat format = new DecimalFormat("##0.00");
		String s = format.format(d);
		return s;
	}

	public class LinePatern{
		private int cursor = 0;
		private TableData currentTableData;
		
		final public int TYPE_NORMAL = 0;
		protected String name = "";
		protected String label = "";
		protected String bgcolor = "";
		protected String fontColor = "";
		protected int width = 0;
		
		/**
		 * createDataTagで操作している行を返す。<BR>
		 * 主に、継承先クラスのLinePaternクラスで行によって出力タグを変更する時、使用します。
		 * @return 現在、操作されている行の行番号
		 */
		protected int getCursor(){
			return cursor;
		}
		public TableData getCurrentTableData(){
			return currentTableData;
		}

		//COLSPAN制御変数
		protected int colspanCounter;
		private   int colspanLines;

		public boolean colspanCheck(){
			if(0 == colspanLines){
				return false;
			}
			
			colspanCounter++;
			if(colspanLines == colspanCounter){
				colspanCounter = 0;
				colspanLines = 0;
			}
			return true;
		}
		public void setColspan(int n)
			throws SpanException{
			if(0 != colspanLines){
				//エクセプション(すでにROWSPANになっている)
				throw new SpanException();
			}
			colspanCounter = 1;
			colspanLines = n;
		}

		//ROWSPAN制御変数
		protected int rowspanCounter;
		private   int rowspanLines;

		public boolean isRowspan(){
			if(0 == rowspanLines){
				return false;
			}
			return true;
		}
		
		public boolean rowspanCheck(){
			if(0 == rowspanLines){
				return false;
			}
			
			rowspanCounter++;
			if(rowspanLines == rowspanCounter){
				rowspanCounter = 0;
				rowspanLines = 0;
			}
			return true;
		}
		protected void setRowspan(int n)
			throws SpanException{
			if(0 != rowspanLines){
				//エクセプション(すでにROWSPANになっている)
				throw new SpanException();
			}
			rowspanCounter = 1;
			rowspanLines = n;
		}


//		public String getHeadOptionTag(int c){
//			cursor = c;
//			getHeadOptionTag();
//		}
		public String getHeadOptionTag(){
			return "";
		}

		public String getOptionTag(String value,int c,TableData d)
			throws SpanException{
			cursor = c;
			currentTableData = d;
			return getOptionTag(value);
		}
		public String getOptionTag(String value)
			throws SpanException{
			return "";
		}
		
		public String editValueWithFontTag(String val,int c,TableData d){
			cursor = c;
			currentTableData = d;
			return editValueWithFontTag(val);
		}
		public String editValueWithFontTag(String val){
			return val;
		}

		public String editValue(String val,int c,TableData d){
			cursor = c;
			currentTableData = d;
			return editValue(val);
		}
		public String editValue(String val){
			return val;
		}
		
		private LinePatern(){}	//使えない
		public LinePatern(String n,int w){
			name = n;
			label = n;
			width = w;
			rowspanLines = 0;
			rowspanCounter = 0;
			colspanLines = 0;
			colspanCounter = 0;
		}
		public void setLabel(String l){
			label = l;
		}
		public String getLabel(){
			return label;
		}
		public int getWidth(){
			return width;
		}
		public String getName(){
			return name;
		}
		public String getBgColor(){
			return bgcolor;
		}
	}

	protected class TableData extends Hashtable{
		public String get(String k)
			throws NoDataException{
			Object v = super.get(k);
			if(null != v){
				return v.toString();
			}
			else{
//				parameters.addMessage("不正なデータが存在します(" + k + ")。");
				return "";
//				throw new NoDataException();
			}
		}

      
      public Object put(Object k,Object v){
      	if(null == v){
      		v = "";
      	}
      	return super.put(k,v);
      }
	}
	static public class TableException extends XysfljException{
	}
	static public class SpanException extends TableException{
	}
	static public class NoDataException extends TableException{
	}
}
