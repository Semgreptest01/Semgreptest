<%@ page contentType="text/html; charset=EUC-JP" language="java" import="xysk.*"%>
<%@ include file="Xysflj_page_header.jsp"%>
<%
	//フォームデータ解析
	pageParameters = new Xysklj_02301_Params(request);
	Xysklj_02301_Params param = (Xysklj_02301_Params)pageParameters;
	
	//テーブル作成
	Xysk0230Renewday renewdayTable = new Xysk0230Renewday(param);
	renewdayTable.setMinLines(1);

	//テーブル作成
	Xysk0230DetailTable detailTable = new Xysk0230DetailTable(param);
	detailTable.setMinLines(1);

	//テーブル作成
	Xysk0230CheckTable checkTable = new Xysk0230CheckTable(param);
	checkTable.setMinLines(1);

%>
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=EUC-JP">
<META http-equiv="Content-Style-Type" content="text/css">
<!--
// *---------------------------------------------------------------------
// * システム　　名称 : 出版物管理システム
// * 画面名称		  : 出版社 銘柄/発売日（全国）（Xysklj_02301J.jsp）
// * 会社名or所属名   : 富士ソフトABC 株式会社
// * 作成日 		  : 2003/07/01 11:00:00
// * 作成者 		  : M.Yoshizawa
// * *** 修正履歴 ***
// * No.  Date		  Aouther		Description
// * 01   2003/09/22  Y.Takabayashi Ctrlキー・禁止
// * 02   2006/05/11  K.Sasaki      hidden項目の追加 ��screenId ��screenNm ��objectNm
// *---------------------------------------------------------------------
-->
<STYLE TYPE="text/css">
<!--
 TD{font-size=9pt;}
 INPUT{ime-mode:inactive;text-align="left";}
 INPUT.ON_RIGHT{ime-mode:active;text-align="right";}
 INPUT.OF_RIGHT{ime-mode:inactive;text-align="right";}
 INPUT.ON_LEFT{ime-mode:active;text-align="left";}
 INPUT.OF_LEFT{ime-mode:inactive;text-align="left";}
 TEXTAREA{ime-mode:active;font-size=8pt;overflow:auto;}
 INPUT.BUTTON{width:5EM; font-size=10pt; text-align:center; 'ＭＳゴシック';height=20;}
 INPUT.DATE_BTN{width:1.5EM; font-size=8pt; text-align:center; 'ＭＳゴシック';height=20;}
-->
</STYLE>
<TITLE>出版物管理システム</TITLE>
<SCRIPT language="JavaScript">
<!--
$help_path		   = "<%= XysfljGenericRules.CONT_PATH_XYSK%>help/Xysklj_02301.htm";
-->
</SCRIPT>
<SCRIPT language="JavaScript" src="<%= XysfljGenericRules.CONT_PATH_XYSK%>common/common.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%= XysfljGenericRules.CONT_PATH_XYSK%>common/selectline1_selectline2.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%= XysfljGenericRules.CONT_PATH_XYSK%>common/selectline2_selectline3.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%= XysfljGenericRules.CONT_PATH_XYSK%>common/Xysflj_button_common.js"></SCRIPT>
<SCRIPT language="JavaScript">
<!--
// ************************
// *** Ctrl+Nキー禁止対応
// ************************
	$submit_sw = "0";			// 送信中 SWITCH OFF
	Ctrl_Detect();				// Ctrlキー・禁止
function Ctrl_Detect(){
	document.onkeydown=keydown;
	if(document.layers)document.captureEvents(Event.KEYDOWN)
}
function keydown(e) {
	//--キ−コ−ドを文字に直す
	var keyName=String.fromCharCode(getKEYCODE(e))
	var char_code = escape( keyName );
	if (event.ctrlKey){
		if (char_code == "N") {
			alert("Ctrl+N は使用できません。");
			return false;
		}
	}
}
function getKEYCODE(e){
	//--押されたキ−コ−ドを返す
	if(document.layers) return e.which
	if(document.all)	return event.keyCode
} 
	// ****************
	// *** 初期処理
	// ****************
	function initial(){
		document.title = G_name( "Sxysk02301" );	// *** 画面名称取得 ***
	// **** Servlet処理でエラーとなった場合には、以下の記述でエラー項目をpink設定する。****
	//	 document.form1.elements[eno0].style.background='pink';
		resizeTo(1024,740);		// 画面初期表示リサイズ
		focus();				// 自画面へフォーカス
		self.moveTo(0,0);		// Window 位置を0,0へ移動
		if(document.form1.CommandType.value == "<%= Xysklj_02301_Params.COMMAND_REFERENCE %>"){
	
			document.form1.select_Item.value = <%=request.getParameter("select_Item")%>;
			document.form1.select_selectline1.value = <%=request.getParameter("selectline1")%>;
			document.form1.select_selectline2.value = <%=request.getParameter("selectline2")%>;
			document.form1.select_selectline3.value = <%=request.getParameter("selectline3")%>;
	
			if (document.form1.select_selectline1.value != 0) {
				pop_selectline2(document.form1, document.form1.select_selectline1.value);
				document.form1.select_selectline2.value = <%=request.getParameter("selectline2")%>;
			} else {
				document.form1.select_selectline2.disabled = true;
			}
			if (document.form1.select_selectline2.value != 0) {
				pop_selectline3(document.form1, document.form1.select_selectline1.value + document.form1.select_selectline2.value);
				document.form1.select_selectline3.value = <%=request.getParameter("selectline3")%>;
			} else {
				document.form1.select_selectline3.disabled = true;
			}
		}
		else{
			focus();					// 自画面へフォーカス
			//無効
			document.form1.select_selectline2.disabled = true;
			document.form1.select_selectline3.disabled = true;
		}
		button_sts_chg();
	}
// --------------------------------------
// ダウンロード ボタンが押された場合
// --------------------------------------
	function download_chk(){

		document.form1.allitem.value = document.form1.select_Item.value;
		document.form1.selectline1.value = document.form1.select_selectline1.value;
		if (document.form1.select_selectline2.value == "") {
			document.form1.selectline2.value = "00";
		} else {
			document.form1.selectline2.value = document.form1.select_selectline2.value;
		}
		if (document.form1.select_selectline3.value == "") {
			document.form1.selectline3.value = "00";
		} else {
			document.form1.selectline3.value = document.form1.select_selectline3.value;
		}

		document.form1.CommandType.value = "<%= Xysklj_02301_Params.COMMAND_DOWNLOAD %>";
		document.form1.method = "POST";
		//document.form1.action = "../Xysk/Xysklj_02301Jd";
		document.form1.action = "<%= XysfljGenericRules.DOWNLOAD_PATH_XYSK%>Xysklj_02301Jd";
		document.form1.submit();
		document.form1.action = "";

	}	// ﾀﾞｳﾝﾛｰﾄﾞ が押された時
// ****************************************
// *** ＨＥＬＰを表示する（静的コンテンツ）
// ****************************************
	function help_chk() {
	   window.open( $help_path, 'help', 'width=500, height=400,scrollbars=no, status=yes,resizable=yes');
	}
// --------------------------------------
// 検索 ボタンが押された場合
// --------------------------------------
	function button1_func(){

		// 各コントロールをロックする
		lockControlSubmit(document.form1);
		
		document.form1.allitem.value = document.form1.select_Item.value;
		document.form1.selectline1.value = document.form1.select_selectline1.value;
		if (document.form1.select_selectline2.value == "") {
			document.form1.selectline2.value = "00";
		} else {
			document.form1.selectline2.value = document.form1.select_selectline2.value;
		}
		if (document.form1.select_selectline3.value == "") {
			document.form1.selectline3.value = "00";
		} else {
			document.form1.selectline3.value = document.form1.select_selectline3.value;
		}

		document.form1.CommandType.value = "<%= Xysklj_02301_Params.COMMAND_REFERENCE %>";
		document.form1.submit_button0.disabled = true;
		document.form1.submit();
	}
// --------------------------------------------
//	change_item
//	全項目選択時
// --------------------------------------------
	function change_item(inForm) {
		document.form1.select_selectline1.options.selectedIndex = 0;
		clear_selectline2(inForm);
		inForm.select_selectline2.disabled = true; //無効
		clear_salectline3(inForm);
		inForm.select_selectline3.disabled = true; //無効
	}
// --------------------------------------
// 連動を無効にするための定義
// --------------------------------------
	function news_publishcom_product_search(sw) {
	}
// --------------------------------------
// ボタン無効化処理
// --------------------------------------
	function button_disable(){
		document.form1.submit_button0.disabled=true; 	// 検索ボタンの無効化
		Button1_disable(0,0,0,0,0,0,0,0,0);				// 通常ボタン１〜９の無効化
		Button2_disable(0,1,0,0,0,0,0,0,0);				// 共通ボタンの無効化
	}
// --------------------------------------
// ボタン有効化処理
// --------------------------------------
	function button_enable(){
		document.form1.submit_button0.disabled=false; 	// 検索ボタンの有効化
		Button1_enable(0,0,0,0,0,0,0,0,0);				// 通常ボタン１〜９の有効化
		Button2_enable(0,1,0,0,0,0,0,0,0);				// 共通ボタンの有効化
	}
// --------------------------------------------
//	change_publishcom
//	新聞社選択時
// --------------------------------------------
	function change_publishcom(inForm) {
	}
// --------------------------------------------
//	button_sts_chg(必ず必要)
//	検索条件によりボタンの状態を変更する
// --------------------------------------------
	function button_sts_chg() {
		if (document.form1.select_publishcom.value == 0 ||
			(document.form1.select_Item.value == 0 && document.form1.select_selectline1.value == 0)) {
			button_disable();
		} else {
			button_enable();
		}
	}
--->
</SCRIPT>
</HEAD>
<BODY topmargin="5" leftmargin="10" onload="initial();" bgcolor="#F0FFFF" scroll="no"
 oncontextmenu="return false"><!-- *** 本番時は、左記の記述を上記BODYタグに追加すること *** -->
<FORM method="post" name="form1" target="_top" action="Xysklj_02301J.jsp">
<%= param.createTagOfStatus()%>
<%= param.createTagOfCommandType()%>
<SCRIPT language="JavaScript">
<!--
	Header("Sxysk02301","",975,0,0,0,0,1);
//			|			|	| | | | |
//			|			|	| | | | +---> 日付時刻 SW
//			|			|	| | | +-----> HELP SW
//			|			|	| | +-------> 印刷 SW
//			|			|	| +---------> 終了 SW
//			|			|	+-----------> ﾒﾆｭｰ SW
//			|			+---------------> ヘッダー横幅（通常は、975）
//			+---------------------------> 画面ＩＤ
	msg_start( 975,0 ); 			   // メッセージ表示領域 ***START***
<%= param.createMessagePrintJS()%>
	msg_end("","","","","","","","");
-->
</SCRIPT>
<TABLE cellpadding="0" cellspacing="0" width="970" style="table-layout:fixed">
<TBODY><TR align="right">
<TD><%=renewdayTable.createTag()%></TD>
</TR></TBODY>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" width="970" style="table-layout:fixed">
<COL WIDTH="30"><COL WIDTH="150"><COL WIDTH="30"><COL WIDTH="200"><COL WIDTH="60"><COL WIDTH="200"><COL WIDTH="100">
<TBODY><TR>
	<TD>項目</TD>
	<TD>
		<SELECT name="select_Item" style="width : 140px;" onChange="change_item(document.form1);button_sts_chg();">
			<OPTION value="0" selected>
			<OPTION value="1">全項目
		</SELECT>
	</TD>
	<TD>新聞社</TD>
	<TD><%= (param.getControl(XyskljNewsPUBLISHCOMCombo.NAME)).createTag()%></TD>
	<TD></TD>
	<TD></TD>
	<TD></TD>
</TR><TR>
	<TD>又は</TD>
	<TD></TD>
	<TD></TD>
	<TD></TD>
	<TD>
		<input type="button" name="submit_button0"
			 class="BUTTON" value="検索" onClick="button1_func()">	
	</TD>
	<TD></TD>
	<TD></TD>
</TR><TR>
	<TD>１行目</TD>
	<TD>
		<SELECT name="select_selectline1" style="width:140px;"onChange="pop_selectline2(document.form1, this.value);button_sts_chg();">
			<OPTION value="0" selected>
			<OPTION value="1">仕入数
			<OPTION value="2">販売数
			<OPTION value="3">販売率
			<OPTION value="4">配本店数
			<OPTION value="5">売切店舗数
			<OPTION value="6">売切店発生率
			<OPTION value="7">返品数
			<OPTION value="8">欠品数
			<OPTION value="9">PSA仕入
			<OPTION value="10">PSA販売
		</SELECT>	
	</TD>
	<TD></TD>
	<TD></TD>
	<TD></TD>
	<TD></TD>
	<TD></TD>
</TR><TR>
	<TD>２行目</TD>
	<TD>
		<SELECT name="select_selectline2" style="width:140px;"onChange="pop_selectline3(document.form1, select_selectline1.value + this.value);">
		<OPTION value="00" selected></OPTION>
		</SELECT>			
	</TD>
	<TD></TD>
	<TD></TD>
	<TD></TD>
	<TD></TD>
	<TD></TD>
</TR><TR>
	<TD>３行目</TD>
	<TD>
		<SELECT name="select_selectline3" style="width : 140px;">
		<OPTION value="00" selected></OPTION>
		</SELECT>
	</TD>
	<TD></TD>
	<TD></TD>
	<TD></TD>
	<TD></TD>
	<TD></TD>
</TR></TBODY>
</TABLE>

<SCRIPT language="JavaScript">
<!--
	Button1_set(975,"","","","","","","","","");
	Button2_set(1,1,1,0,1,0,0,0,0);
-->
</SCRIPT>

<TABLE border="1" style="table-layout:fixed" cellpadding="1" cellspacing="0">
<TR><%=checkTable.createTag()%></TR>
</TABLE>
<BR>

<%=detailTable.createTag()%></TR>

</BODY>
</HTML>
<!--
 ************************
 *** 業務画面 *** END ***
 ************************
-->
<SCRIPT language="JavaScript">
<!--
   Screen_end();	// ****** 業務画面枠・終了指示 ******
-->
</SCRIPT>
<INPUT type ="hidden" name="allitem" value="<%=request.getParameter("allitem")%>" >
<INPUT type ="hidden" name="selectline1" value="<%=request.getParameter("selectline1")%>" >
<INPUT type ="hidden" name="selectline2" value="<%=request.getParameter("selectline2")%>" >
<INPUT type ="hidden" name="selectline3" value="<%=request.getParameter("selectline3")%>" >
<INPUT type ="hidden" name="inout" value="<%=request.getParameter("inout")%>" >
<!-- 2006/05/11変更(E10K移行対応) -->
<INPUT type ="hidden" name="screenId" value="SXYSK02301" >
<INPUT type ="hidden" name="screenNm" value="230" >
</FORM>
</BODY>
</HTML>
<%@ include file="Xysflj_page_footer.jsp"%>
