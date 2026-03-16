<!-- JavaScript Code
//-----------------------------------------------------------------------------------#
// HTML_Name	: DR−SV COMBO 連動 Java-Script ( dr_sv.js )
// Name 		: Y.Takabayashi
// Date 		: 2003/05/19
// No.	Date			Name			Description
// 01	2006/6/23		O.Ogawara		hiddenパラメータ追加(inout)
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
var dummy_saledate =  new Array("('','0000000000',true,true)");
var Array_saledate =  new Array("('','0000000000',true,true)");
// ----------------
//	DR_SV_SEARCH
// ----------------
function brand_saledate_search(sw) {
	if ( $submit_sw == "1" ){ return; }				// 送信中は、何もせずに抜ける。
  if ( document.form1.select_brand.value != "000000" ){
	ApWindow=window.open('', 'sub1', 'fullscreen=0, width=285, height=30, top=110, left=558, scrollbars=0, status=1, titlebars=0, resizable=0');
	ApWindow.document.write('<html><head><title>出版物管理システム</title>');
	ApWindow.document.write('</head><body><form name=form1 target="_self">');
	ApWindow.document.write('お待ちください。');
	ApWindow.document.write('<INPUT type="hidden" name="publishcom_cd" value="">');
	ApWindow.document.write('<INPUT type="hidden" name="brand_cd" value="">');
	ApWindow.document.write('<INPUT type="hidden" name="blank_sw" value="">');
	ApWindow.document.write('<INPUT type="hidden" name="CommandType" value="0">');
//	*********** E10K移行時追加(2006/06/23) *****************************************
	ApWindow.document.write('<INPUT type="hidden" name="inout" value="" >');
	ApWindow.document.write('</form></body></html>');
//	*********** E10K移行時追加(2006/06/23) *****************************************
	ApWindow.document.form1.inout.value = document.form1.inout.value;
	ApWindow.document.form1.action="Xysklj_publishsaledateJ.jsp";
	ApWindow.document.form1.publishcom_cd.value = form1.select_publishcom.value;
	ApWindow.document.form1.brand_cd.value = form1.select_brand.value;
	ApWindow.document.form1.blank_sw.value = sw;
	ApWindow.document.form1.method = "POST";
	ApWindow.document.form1.submit();
	document.form1.select_saledate.disabled = false;
	focus();
	$submit_sw = "1";								// 送信中 SWITCH ON
  }else{
	clear_saledate(document.form1);
	document.form1.select_saledate.disabled = true;
	alert("銘柄を選択してください。");
  }
}
// ****************************
// * ten_COMBO_BOX 書換
// ****************************
function pop_saledate(inForm) {
  var selectedArray = Array_saledate;
  var x = 0;
  var Array_name = "";
  while (selectedArray.length < inForm.select_saledate.options.length) {
   inForm.select_saledate.options[(inForm.select_saledate.options.length - 1)] = null;
  }

  x = Array_saledate.length;
  for (var i=0; i < x; i++) {
	inForm.select_saledate.options[i]= null;
  }

  Array_name = eval("Array_saledate");
  for (var i=0; i < x; i++) {
	 eval("inForm.select_saledate.options[i]=" + "new Option" + eval(Array_name)[i]);
  }

  inForm.select_saledate.disabled = false;
	//inForm.submit_button0.disabled = false; //検索ボタンを有効
}
// ****************************
// * ten_COMBO_BOX クリア
// ****************************
function clear_saledate(inForm) {
  var selectedArray = dummy_saledate;
  var x = 0;
  var Array_name = "";
  while (selectedArray.length < inForm.select_saledate.options.length) {
   inForm.select_saledate.options[(inForm.select_saledate.options.length - 1)] = null;
  }

  x = dummy_saledate.length;
  for (var i=0; i < x; i++) {
	inForm.select_saledate.options[i]= null;
  }

  Array_name = eval("dummy_saledate");
  for (var i=0; i < x; i++) {
	 eval("inForm.select_saledate.options[i]=" + "new Option" + eval(Array_name)[i]);
  }
  inForm.select_saledate.disabled = true;
}
// -->
