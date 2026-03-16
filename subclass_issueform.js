<!-- JavaScript Code
//-----------------------------------------------------------------------------------#
// HTML_Name	: DR−SV COMBO 連動 Java-Script ( dr_sv.js )
// Name 		: Y.Takabayashi
// Date 		: 2003/05/19
// No.	Date			Name			Description
//-----------------------------------------------------------------------------------#
// 使用方法
// (1)<HEAD>〜</HEAD>に以下のスクリプト指定をします。
//	  <SCRIPT language="JavaScript" src="../../contdata/XYDL/common/dr_sv.js"></SCRIPT>
// (2)DR_COMBO の箇所に以下の記述をする。
//	 <SELECT name="select_dr" style="width : 120px;"
//	   onChange="dr_sv_search(this.value,0);">
//	 <OPTION value="000000" selected></OPTION>
//	 <OPTION value="713013">新潟ＤＲ</OPTION>
//					 ：
//	 <OPTION value="713390">越谷ＤＲ</OPTION>
//	 </SELECT>
// (3)SV_COMBO は以下の記述をする。
//	 <SELECT name="select_sv" style="width : 215px;">
//	 <OPTION value="0000000" selected></OPTION>
//	 </SELECT>
// (4)SV COMBO のクリアは、
//　　clear_sv(document.form1);
// (5)dr_sv_search()の説明
//	  dr_sv_search(0);
//				   |
//				   +--> 0:空行なし 1:空行あり
//-----------------------------------------------------------------------------------
// ＳＶ配列
var dummy_issueform =  new Array("('','0',true,true)");
var Array_issueform =  new Array("('','0',true,true)");
// ----------------
//	DR_SV_SEARCH
// ----------------
function subclass_issueform_search(sw) {
	if ( $submit_sw == "1" ){ return; }				// 送信中は、何もせずに抜ける。
  if ( document.form1.select_subclass.value != "00" ) {
	ApWindow=window.open('', 'sub1', 'fullscreen=0, width=285, height=30, top=110, left=558, scrollbars=0, status=1, titlebars=0, resizable=0');
	ApWindow.document.write('<html><head><title>出版物管理システム</title>');
	ApWindow.document.write('</head><body><form name=form1 target="_self">');
	ApWindow.document.write('お待ちください。');
	ApWindow.document.write('<INPUT type="hidden" name="dept_cd" value="">');
	ApWindow.document.write('<INPUT type="hidden" name="subclass_cd" value="">');
	ApWindow.document.write('<INPUT type="hidden" name="blank_sw" value="">');
	ApWindow.document.write('<INPUT type="hidden" name="CommandType" value="0">');
	ApWindow.document.write('</form></body></html>')
	ApWindow.document.form1.action="Xysklj_issueformJ.jsp";
	ApWindow.document.form1.dept_cd.value = form1.select_deptclass.value;
	ApWindow.document.form1.subclass_cd.value = form1.select_subclass.value;
	ApWindow.document.form1.blank_sw.value = sw;
	ApWindow.document.form1.method = "POST";
	ApWindow.document.form1.submit();
	document.form1.select_issueform.disabled = false;
	focus();
	$submit_sw = "1";								// 送信中 SWITCH ON
  }else{
	clear_issueform(document.form1);
	document.form1.select_issueform.disabled = true;
	alert("SUBCLASSを選択してください。");
  }
}
// ****************************
// * ten_COMBO_BOX 書換
// ****************************
function pop_issueform(inForm) {
  var selectedArray = Array_issueform;
  var x = 0;
  var Array_name = "";
  while (selectedArray.length < inForm.select_issueform.options.length) {
   inForm.select_issueform.options[(inForm.select_issueform.options.length - 1)] = null;
  }

  x = Array_issueform.length;
  for (var i=0; i < x; i++) {
	inForm.select_issueform.options[i]= null;
  }

  Array_name = eval("Array_issueform");
  for (var i=0; i < x; i++) {
	 eval("inForm.select_issueform.options[i]=" + "new Option" + eval(Array_name)[i]);
  }

  inForm.select_issueform.disabled = false;
//	inForm.submit_button0.disabled = false; //検索ボタンを有効
}
// ****************************
// * ten_COMBO_BOX クリア
// ****************************
function clear_issueform(inForm) {
  var selectedArray = dummy_issueform;
  var x = 0;
  var Array_name = "";
  while (selectedArray.length < inForm.select_issueform.options.length) {
   inForm.select_issueform.options[(inForm.select_issueform.options.length - 1)] = null;
  }

  x = dummy_issueform.length;
  for (var i=0; i < x; i++) {
	inForm.select_issueform.options[i]= null;
  }

  Array_name = eval("dummy_issueform");
  for (var i=0; i < x; i++) {
	 eval("inForm.select_issueform.options[i]=" + "new Option" + eval(Array_name)[i]);
  }
  inForm.select_issueform.disabled = true;
}
// -->
