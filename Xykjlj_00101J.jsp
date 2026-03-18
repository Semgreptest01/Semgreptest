<%@ page contentType="text/html; charset=EUC-JP" language="java" import="xykj.*"%>
<%@ include file="Xysflj_page_header.jsp"%>
<%
    //フォームデータ解析
    pageParameters = new Xykjlj_00101_Params(request);
    Xykjlj_00101_Params param = (Xykjlj_00101_Params)pageParameters;

    //テーブル作成
    Xykj0010Renewday renewdayTable = new Xykj0010Renewday(param);
    renewdayTable.setMinLines(1);

    //テーブル作成
    Xykj0010DetailTable detailTable = new Xykj0010DetailTable(param);
    detailTable.setMinLines(1);

    //テーブル作成
//    Xykj0010CheckTable checkTable = new Xykj0010CheckTable(param);
//    checkTable.setMinLines(1);

%>
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=EUC-JP">
<META http-equiv="Content-Style-Type" content="text/css">
<!--
// *---------------------------------------------------------------------
// * システム　　名称 : 出版物管理システム
// * 画面名称         : 利用者コード〜取引先コード登録（Xykjlj_00101J.jsp）
// * 会社名or所属名   : 富士ソフトABC 株式会社
// * 作成日           : 2004/02/18 13:00:00
// * 作成者           : M.Koga
// * *** 修正履歴 ***
// * No.  Date        Aouther       Description
// * 01   2004/09/22  M.Koga        新規
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
$help_path         = "<%= XysfljGenericRules.CONT_PATH_XYKJ%>help/Xykjlj_01301.htmS";
-->
</SCRIPT>
<SCRIPT language="JavaScript" src="<%= XysfljGenericRules.CONT_PATH_XYKJ%>common/common.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%= XysfljGenericRules.CONT_PATH_XYKJ%>common/selectline1_selectline2.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%= XysfljGenericRules.CONT_PATH_XYKJ%>common/selectline2_selectline3.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%= XysfljGenericRules.CONT_PATH_XYKJ%>common/Xysflj_button_common.js"></SCRIPT>
<SCRIPT language="JavaScript">
<!--
//-----------------------------------------------------------------------------------
// 関数名：ComKeyDown();
//
// 機　能：特殊キーの無効化
//-----------------------------------------------------------------------------------
function ComKeyDown() {
    if(event.keyCode == 8){
        var src = event.srcElement;
        if(src.readOnly ||
            !((src.tagName == 'INPUT' && src.type == 'text') || (src.tagName == 'TEXTAREA') || (src.tagName == 'INPUT' && src.type == 'file'))){
            return false;
        }
    }
    return true;
}
// ************************
// *** Ctrl+Nキー禁止対応
// ************************
    $submit_sw = "0";           // 送信中 SWITCH OFF
    Ctrl_Detect();              // Ctrlキー・禁止
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
    if(document.all)    return event.keyCode
}
    // ****************
    // *** 初期処理
    // ****************
    function initial(){
        var f = document.form1;
        document.title = G_name( "Sxykj00101" );    // *** 画面名称取得 ***
        // **** Servlet処理でエラーとなった場合には、以下の記述でエラー項目をpink設定する。****
        resizeTo(1024,740);     // 画面初期表示リサイズ
        focus();                // 自画面へフォーカス
        self.moveTo(0,0);       // Window 位置を0,0へ移動
        //利用者コードの初期値
        if("<%=detailTable.usercd%>" == "null"){
            f.text_usercd.value = "";
        } else {
            f.text_usercd.value = "<%=detailTable.usercd%>";
        }
        //エラーの場合、アラートを出力
        if("<%=detailTable.kensaku_flg%>" == "1"){
            f.select_customertype.options[0].selected = true;
            alert("<%=detailTable.alertmsg%>");
            return;
        }
        //取引先種別の設定
        if("<%=detailTable.customertype%>" == "1"){
            f.select_customertype.options[1].selected = true;
        } else if("<%=detailTable.customertype%>" == "2"){
            f.select_customertype.options[2].selected = true;
        } else {
            f.select_customertype.options[0].selected = true;
        }
        //エラーの場合、アラートを出力
        if("<%=detailTable.err_flg%>" == "1"){
            alert("<%=detailTable.alertmsg%>");
        }
        //検索ボタン押下時に利用者コードに数字以外の文字が入力されていた場合
        if("<%=detailTable.err_type%>" == "1"){
            ComChgBgColor(document.form1.text_usercd);
        }
        //入力された利用者コードが７桁以外の場合
        if("<%=detailTable.err_type%>" == "2"){
            ComChgBgColor(document.form1.text_usercd);
        }
        //入力された利用者コードが紐付けテーブルに存在しない場合
        if("<%=detailTable.err_type%>" == "3"){
            ComChgBgColor(document.form1.text_usercd);
        }
        //入力された利用者コードが紐付けテーブルに存在しない場合
        if("<%=detailTable.err_type%>" == "3"){
            ComChgBgColor(document.form1.text_usercd);
        }
        //利用者コードに数字以外の文字が入力されている場合
        if("<%=detailTable.err_type%>" == "4"){
            ComChgBgColor(document.form1.text_usercd);
        }
        //利用者コードに数字以外の文字が入力されている場合
        if("<%=detailTable.err_type%>" == "5"){
            ComChgBgColor(document.form1.text_usercd);
        }
        //取引先コードへマスタに存在しない値が入力されている場合
        if("<%=detailTable.err_type%>" == "6"){
            if("<%=detailTable.err_cust_no[0]%>" == "1"){
                ComChgBgColor(document.form1.customer_cd1);
            }
            if("<%=detailTable.err_cust_no[1]%>" == "1"){
                ComChgBgColor(document.form1.customer_cd2);
            }
            if("<%=detailTable.err_cust_no[2]%>" == "1"){
                ComChgBgColor(document.form1.customer_cd3);
            }
            if("<%=detailTable.err_cust_no[3]%>" == "1"){
                ComChgBgColor(document.form1.customer_cd4);
            }
            if("<%=detailTable.err_cust_no[4]%>" == "1"){
                ComChgBgColor(document.form1.customer_cd5);
            }
            if("<%=detailTable.err_cust_no[5]%>" == "1"){
                ComChgBgColor(document.form1.customer_cd6);
            }
            if("<%=detailTable.err_cust_no[6]%>" == "1"){
                ComChgBgColor(document.form1.customer_cd7);
            }
            if("<%=detailTable.err_cust_no[7]%>" == "1"){
                ComChgBgColor(document.form1.customer_cd8);
            }
            if("<%=detailTable.err_cust_no[8]%>" == "1"){
                ComChgBgColor(document.form1.customer_cd9);
            }
            if("<%=detailTable.err_cust_no[9]%>" == "1"){
                ComChgBgColor(document.form1.customer_cd10);
            }
        }
        //入力された利用者コードがマスタに存在しない場合
        if("<%=detailTable.err_type%>" == "7"){
            ComChgBgColor(document.form1.text_usercd);
        }
        //利用者コードを入力せずに検索ボタンを押下した場合
        if("<%=detailTable.err_type%>" == "8"){
            ComChgBgColor(document.form1.text_usercd);
        }
        set_customer_nm();
    }
// --------------------------------------
// 取引先名をhiddenに格納
// --------------------------------------
    function set_customer_nm(){
        document.form1.customernm1.value = "<%=detailTable.customer_nm[0]%>";
        document.form1.customernm2.value = "<%=detailTable.customer_nm[1]%>";
        document.form1.customernm3.value = "<%=detailTable.customer_nm[2]%>";
        document.form1.customernm4.value = "<%=detailTable.customer_nm[3]%>";
        document.form1.customernm5.value = "<%=detailTable.customer_nm[4]%>";
        document.form1.customernm6.value = "<%=detailTable.customer_nm[5]%>";
        document.form1.customernm7.value = "<%=detailTable.customer_nm[6]%>";
        document.form1.customernm8.value = "<%=detailTable.customer_nm[7]%>";
        document.form1.customernm9.value = "<%=detailTable.customer_nm[8]%>";
        document.form1.customernm10.value = "<%=detailTable.customer_nm[9]%>";
    }
// ****************************************
// *** ＨＥＬＰを表示する（静的コンテンツ）
// ****************************************
    function help_chk() {
       window.open( $help_path, 'help', 'width=500, height=400,scrollbars=yes, status=yes,resizable=yes');
    }
// --------------------------------------
// 入力された値をhiddenへ代入
// --------------------------------------
    function input_data1(){
        document.form1.usercd.value = document.form1.text_usercd.value;
//        document.form1.customertype.value = document.form1.select_customertype.value;
    }
// --------------------------------------
// 入力された値をhiddenへ代入
// --------------------------------------
    function input_data2(){
        document.form1.usercd.value = document.form1.text_usercd.value;
        document.form1.customertype.value = document.form1.select_customertype.value;
        document.form1.customer1.value = document.form1.customer_cd1.value;
        document.form1.customer2.value = document.form1.customer_cd2.value;
        document.form1.customer3.value = document.form1.customer_cd3.value;
        document.form1.customer4.value = document.form1.customer_cd4.value;
        document.form1.customer5.value = document.form1.customer_cd5.value;
        document.form1.customer6.value = document.form1.customer_cd6.value;
        document.form1.customer7.value = document.form1.customer_cd7.value;
        document.form1.customer8.value = document.form1.customer_cd8.value;
        document.form1.customer9.value = document.form1.customer_cd9.value;
        document.form1.customer10.value = document.form1.customer_cd10.value;
    }
// --------------------------------------
// 登録 ボタンが押された場合
// --------------------------------------
    function submit_chk1(){
        var i;
        // 各コントロールをロックする
        lockControlSubmit(document.form1);
        //利用者コードに数字以外の文字が入力されている場合
        if(document.form1.text_usercd.value.match(/\D/g)){
            alert("利用者コードに数字以外の文字が入力されています。");
            ComChgBgColor(document.form1.text_usercd);
            enableBotton();
            return;
        }
        //入力された利用者コードが７桁以外の場合
        if(document.form1.text_usercd.value.length != 7){
            alert("利用者コードの桁数が足りません。");
            ComChgBgColor(document.form1.text_usercd);
            enableBotton();
            return;
        }
        //入力された取引先コードが６桁以外の場合
        if(!chk_customer_cd()){
            alert("取引先コードの桁数が足りません。");
            enableBotton();
            return;
        }
        //取引先コードに数字以外の文字が入力されている場合
        if(!chk_customer_cd2()){
            alert("取引先コードに数字以外の文字が入力されています。");
            enableBotton();
            return;
        }
        //取引先種別が選択されていない場合
        if(document.form1.select_customertype[0].selected == true){
            alert("取引先種別が選択されていません。");
            ComChgBgColor(document.form1.select_customertype);
            enableBotton();
            return;
        }
        for(i=0; i< document.form1.h_user.length; i++){
            if(document.form1.h_user[i].value == document.form1.text_usercd.value){
                if(!confirm("データを変更致しますがよろしいでしょうか？")){
                    enableBotton();
                    return;
                }
                break;
            }
            if(i == document.form1.h_user.length - 1 &&
               document.form1.h_user[i].value != document.form1.text_usercd.value){
                if(!confirm("データを新規登録致しますがよろしいでしょうか？")){
                    enableBotton();
                    return;
                }
                break;
            }
        }

        document.form1.toroku.value = "1";
        input_data2();

        document.form1.submit();
    }
// --------------------------------------
// 検索 ボタンが押された場合
// --------------------------------------
    function submit_chk2(){
        var i;
        // 各コントロールをロックする
        lockControlSubmit(document.form1);
        //利用者コードが入力されていない場合
        if(document.form1.text_usercd.value == ""){
            alert("利用者コードを指定してください。");
            document.form1.err_type.value = "8";
            kensaku_err();
//          document.form1.kensaku_err.value = "1";
//          submit_chk3();
//          enableBotton();
            return;
        }
        //利用者コードに数字以外の文字が入力されている場合
        if(document.form1.text_usercd.value.match(/\D/g)){
            alert("利用者コードに数字以外の文字が入力されています。");
            document.form1.err_type.value = "1";
            kensaku_err();
//            enableBotton();
            return;
        }
        //入力された利用者コードが７桁以外の場合
        if(document.form1.text_usercd.value.length != 7){
            alert("利用者コードの桁数が足りません。");
            document.form1.err_type.value = "2";
            kensaku_err();
//            enableBotton();
            return;
        }
        //入力された利用者コードが紐付けテーブルに存在することを確認
        for(i=0; i<document.form1.h_user.length; i++){
            if(document.form1.h_user[i].value == document.form1.text_usercd.value){
                break;
            }
            //入力された利用者コードが紐付けテーブルに存在しない場合
            if(i == document.form1.h_user.length - 1 &&
               document.form1.h_user[i].value != document.form1.text_usercd.value){
                alert("検索結果は０件です。");
                document.form1.err_type.value = "3";
                kensaku_err();
//                enableBotton();
                return;
            }
        }
        //データ変更チェック
        if(!change_chk()){
            if(!confirm("入力したデータが消えてしまいますがよろしいでしょうか？")){
                enableBotton();
                return;
            }
        }
        document.form1.kensaku.value = "1";
        input_data1();
        document.form1.submit();
    }
// --------------------------------------
// クリア ボタンが押された場合
// --------------------------------------
    function submit_chk3(){
        if(!change_chk()){
            if(!confirm("入力したデータが消えてしまいますがよろしいでしょうか？")){
                return;
            }
        }
//        document.form1.usercd.value = "";
//        document.form1.customertype.value = "";
//        document.form1.customer1.value = "";
//        document.form1.customer2.value = "";
//        document.form1.customer3.value = "";
//        document.form1.customer4.value = "";
//        document.form1.customer5.value = "";
//        document.form1.customer6.value = "";
//        document.form1.customer7.value = "";
//        document.form1.customer8.value = "";
//        document.form1.customer9.value = "";
//        document.form1.customer10.value = "";
        //hidden
//        document.form1.text_usercd.value = "";
//        document.form1.select_customertype[0].selected = true;
//        document.form1.customer_cd1.value = "";
//        document.form1.customer_cd2.value = "";
//        document.form1.customer_cd3.value = "";
//        document.form1.customer_cd4.value = "";
//        document.form1.customer_cd5.value = "";
//        document.form1.customer_cd6.value = "";
//        document.form1.customer_cd7.value = "";
//        document.form1.customer_cd8.value = "";
//        document.form1.customer_cd9.value = "";
//        document.form1.customer_cd10.value = "";
        // 各コントロールをロックする
        lockControlSubmit(document.form1);

        document.form1.clear.value = "1";
        document.form1.submit();
    }
// --------------------------------------
// 前へ ボタンが押された場合
// --------------------------------------
    function submit_chk4(){
        // 各コントロールをロックする
        lockControlSubmit(document.form1);
        //利用者コードに数字以外の文字が入力されている場合
        if(document.form1.text_usercd.value.match(/\D/g)){
            alert("利用者コードに数字以外の文字が入力されています。");
            document.form1.err_type.value = "4";
            kensaku_err();
//            enableBotton();
            return;
        }
        //データ変更チェック
        if(!change_chk()){
            if(!confirm("入力したデータが消えてしまいますがよろしいでしょうか？")){
                enableBotton();
                return;
            }
        }
        document.form1.before.value = "1";
        input_data1();
        document.form1.submit();
    }
// --------------------------------------
// 次へ ボタンが押された場合
// --------------------------------------
    function submit_chk5(){
        // 各コントロールをロックする
        lockControlSubmit(document.form1);
        //利用者コードに数字以外の文字が入力されている場合
        if(document.form1.text_usercd.value.match(/\D/g)){
            alert("利用者コードに数字以外の文字が入力されています。");
            document.form1.err_type.value = "5";
            kensaku_err();
//            enableBotton();
            return;
        }
        //データ変更チェック
        if(!change_chk()){
            if(!confirm("入力したデータが消えてしまいますがよろしいでしょうか？")){
                enableBotton();
                return;
            }
        }
        document.form1.next.value = "1";
        input_data1();
        document.form1.submit();
    }
// --------------------------------------
// 閉じる ボタンが押された場合
// --------------------------------------
    function submit_chk6(){
        //データ変更チェック
        if(!change_chk()){
            if(!confirm("入力したデータが消えてしまいますがよろしいでしょうか？")){
                enableBotton();
                return;
            }
        }
        top_close();
    }
// --------------------------------------
// メニュー ボタンが押された場合
// --------------------------------------
    function submit_chk7(){
        window.opener.focus();
    }
// --------------------------------------
// データ変更チェック
// --------------------------------------
    function change_chk(){
        if(document.form1.customer1.value != document.form1.customer_cd1.value ||
           document.form1.customer2.value != document.form1.customer_cd2.value ||
           document.form1.customer3.value != document.form1.customer_cd3.value ||
           document.form1.customer4.value != document.form1.customer_cd4.value ||
           document.form1.customer5.value != document.form1.customer_cd5.value ||
           document.form1.customer6.value != document.form1.customer_cd6.value ||
           document.form1.customer7.value != document.form1.customer_cd7.value ||
           document.form1.customer8.value != document.form1.customer_cd8.value ||
           document.form1.customer9.value != document.form1.customer_cd9.value ||
           document.form1.customer10.value != document.form1.customer_cd10.value){
            return false;
        }
        return true;
    }
// --------------------------------------
// ボタンのロックを解除
// --------------------------------------
    function enableBotton(){
        document.form1.submit_button1.disabled = false;
        document.form1.submit_button2.disabled = false;
        document.form1.submit_button3.disabled = false;
        document.form1.submit_button4.disabled = false;
        document.form1.submit_button5.disabled = false;
        document.form1.submit_button6.disabled = false;
        document.form1.submit_button7.disabled = false;
    }
// --------------------------------------
// 取引先コードの桁数が６桁未満の場合
// --------------------------------------
    function chk_customer_cd(){
        var err_flg = 0;
        if(document.form1.customer_cd1.value.length != "" &&
           document.form1.customer_cd1.value.charAt(0) != " " &&
           document.form1.customer_cd1.value.length != 6){
            ComChgBgColor(document.form1.customer_cd1);
            err_flg += 1;
//            return false;
        }
        if(document.form1.customer_cd2.value.length != "" &&
           document.form1.customer_cd2.value.charAt(0) != " " &&
           document.form1.customer_cd2.value.length != 6){
            ComChgBgColor(document.form1.customer_cd2);
            err_flg += 1;
//            return false;
        }
        if(document.form1.customer_cd3.value.length != "" &&
           document.form1.customer_cd3.value.charAt(0) != " " &&
           document.form1.customer_cd3.value.length != 6){
            ComChgBgColor(document.form1.customer_cd3);
            err_flg += 1;
//            return false;
        }
        if(document.form1.customer_cd4.value.length != "" &&
           document.form1.customer_cd4.value.charAt(0) != " " &&
           document.form1.customer_cd4.value.length != 6){
            ComChgBgColor(document.form1.customer_cd4);
            err_flg += 1;
//            return false;
        }
        if(document.form1.customer_cd5.value.length != "" &&
           document.form1.customer_cd5.value.charAt(0) != " " &&
           document.form1.customer_cd5.value.length != 6){
            ComChgBgColor(document.form1.customer_cd5);
            err_flg += 1;
//            return false;
        }
        if(document.form1.customer_cd6.value.length != "" &&
           document.form1.customer_cd6.value.charAt(0) != " " &&
           document.form1.customer_cd6.value.length != 6){
            ComChgBgColor(document.form1.customer_cd6);
            err_flg += 1;
//            return false;
        }
        if(document.form1.customer_cd7.value.length != "" &&
           document.form1.customer_cd7.value.charAt(0) != " " &&
           document.form1.customer_cd7.value.length != 6){
            ComChgBgColor(document.form1.customer_cd7);
            err_flg += 1;
//            return false;
        }
        if(document.form1.customer_cd8.value.length != "" &&
           document.form1.customer_cd8.value.charAt(0) != " " &&
           document.form1.customer_cd8.value.length != 6){
            ComChgBgColor(document.form1.customer_cd8);
            err_flg += 1;
//            return false;
        }
        if(document.form1.customer_cd9.value.length != "" &&
           document.form1.customer_cd9.value.charAt(0) != " " &&
           document.form1.customer_cd9.value.length != 6){
            ComChgBgColor(document.form1.customer_cd9);
            err_flg += 1;
//            return false;
        }
        if(document.form1.customer_cd10.value.length != "" &&
           document.form1.customer_cd10.value.charAt(0) != " " &&
           document.form1.customer_cd10.value.length != 6){
            ComChgBgColor(document.form1.customer_cd10);
            err_flg += 1;
//            return false;
        }
        if(err_flg != 0){
            return false;
        } else {
            return true;
        }
    }
// -------------------------------------------------
// 取引先コードに数字以外の文字が入力されている場合
// -------------------------------------------------
    function chk_customer_cd2(){
        var err_flg = 0;
        if(document.form1.customer_cd1.value.match(/\D/g)){
            ComChgBgColor(document.form1.customer_cd1);
            err_flg += 1;
        }
        if(document.form1.customer_cd2.value.match(/\D/g)){
            ComChgBgColor(document.form1.customer_cd2);
            err_flg += 1;
        }
        if(document.form1.customer_cd3.value.match(/\D/g)){
            ComChgBgColor(document.form1.customer_cd3);
            err_flg += 1;
        }
        if(document.form1.customer_cd4.value.match(/\D/g)){
            ComChgBgColor(document.form1.customer_cd4);
            err_flg += 1;
        }
        if(document.form1.customer_cd5.value.match(/\D/g)){
            ComChgBgColor(document.form1.customer_cd5);
            err_flg += 1;
        }
        if(document.form1.customer_cd6.value.match(/\D/g)){
            ComChgBgColor(document.form1.customer_cd6);
            err_flg += 1;
        }
        if(document.form1.customer_cd7.value.match(/\D/g)){
            ComChgBgColor(document.form1.customer_cd7);
            err_flg += 1;
        }
        if(document.form1.customer_cd8.value.match(/\D/g)){
            ComChgBgColor(document.form1.customer_cd8);
            err_flg += 1;
        }
        if(document.form1.customer_cd9.value.match(/\D/g)){
            ComChgBgColor(document.form1.customer_cd9);
            err_flg += 1;
        }
        if(document.form1.customer_cd10.value.match(/\D/g)){
            ComChgBgColor(document.form1.customer_cd10);
            err_flg += 1;
        }
        if(err_flg != 0){
            return false;
        } else {
            return true;
        }
    }
// --------------------------------------------
// 検索ボタン押下時にエラーが発生した場合の処理
// --------------------------------------------
    function kensaku_err(){
        document.form1.kensaku_err.value = "1";
        document.form1.usercd.value = document.form1.text_usercd.value;
        submit_chk3();
        return;
    }
//-----------------------------------------------------------------------------------
// 関数名：ComChgBgColor(f_nm,color);
//                         |    |
//                         |    +----> 変えたい色
//                         +--> 色を変えたいオブジェクト（配列）
// 機能  ：文字の色を赤にする
// 戻り値：なし
//-----------------------------------------------------------------------------------
function ComChgBgColor(f_nm){
  f_nm.style.background = "pink";
  return;
}
// --------------------------------------
// 2003/12/18 kimura: 連動を無効にするための定義
// --------------------------------------
    function publishcom_brand_search(sw) {
    }
// --------------------------------------
//  change_item
//  全項目選択時
// --------------------------------------------
    function change_item(inForm) {
        document.form1.select_selectline1.options.selectedIndex = 0;
        clear_selectline2(inForm);
        inForm.select_selectline2.disabled = true; //無効
        clear_salectline3(inForm);
        inForm.select_selectline3.disabled = true; //無効
    }
// --------------------------------------------
//  change_publishcom
//  出版社選択時
// --------------------------------------------
    function change_publishcom(inForm) {
    }
// --------------------------------------
//  ボタン無効化処理追加
// --------------------------------------
    function button_disable(){
        document.form1.submit_button1.disabled = true;
        document.form1.submit_button2.disabled = true;
        document.form1.submit_button3.disabled = true;
        document.form1.submit_button4.disabled = true;
        document.form1.submit_button5.disabled = true;
        document.form1.submit_button6.disabled = true;
        document.form1.submit_button7.disabled = true;
    }
// --------------------------------------
//  2003/12/18 kimura:ボタン有効化処理追加
// --------------------------------------
    function button_enable(){
        document.form1.submit_button0.disabled=false;   // 検索ボタンの有効化
        Button1_enable(0,0,0,0,0,0,0,0,0);              // 通常ボタン１〜９の有効化
        Button2_enable(0,1,0,0,0,0,0,0,0);              // 共通ボタンの有効化
    }

// --------------------------------------------
//  button_sts_chg(必ず必要)
//  2003/12/18 kimura: 検索・ダウンロードボタン制御追加
// --------------------------------------------
    function button_sts_chg() {
        if (document.form1.select_publishcom.value == 0 ||
            (document.form1.select_Item.value == 0 && document.form1.select_selectline1.value == 0)) {
            button_disable();
        } else {
            button_enable();
        }
    }

-->
</SCRIPT>
</HEAD>
<BODY topmargin="5" leftmargin="10" onload="initial();" bgcolor="#F0FFFF" scroll="yes"
 oncontextmenu="return true" onkeydown = "return ComKeyDown();"><!-- *** 本番時は、左記の記述を上記BODYタグに追加すること *** -->
<FORM method="post" name="form1" target="_top" action="Xykjlj_00101J.jsp">
<%= param.createTagOfStatus()%>
<%= param.createTagOfCommandType()%>
<SCRIPT language="JavaScript">
<!--
    Header("Sxykj00101","",975,0,0,0,0,1);
//          |            |                                |  | | | |
//          |            |                                |  | | | +---> 日付時刻 SW
//          |            |                                |  | | +-----> HELP SW
//          |            |                                |  | +-------> 印刷 SW
//          |            |                                |  +---------> 終了 SW
//          |            |                                +-----------> ﾒﾆｭｰ SW
//          |            +---------------> ヘッダー横幅（通常は、975）
//          +---------------------------> 画面ＩＤ
    msg_start( 975,0 );                // メッセージ表示領域 ***START***
<%/*param.addMessage("123","456");*/%>
<%= param.createMessagePrintJS()%>
    msg_end("","","","","","","","");
-->
</SCRIPT>
<TABLE cellpadding="0" cellspacing="0" width="970" style="table-layout:fixed">
<TBODY><TR align="right">
<TD><%=renewdayTable.createTag()%></TD>
</TR></TBODY>
</TABLE>
<!--
<TABLE cellpadding="0" cellspacing="0" width="1024" style="table-layout:fixed">
-->
<TABLE cellpadding="0" cellspacing="0" width="970" style="table-layout:fixed">
<!--
<COL WIDTH="92"><COL WIDTH="300"><COL WIDTH="122"><COL WIDTH="510">
-->
<COL WIDTH="90"><COL WIDTH="300"><COL WIDTH="100"><COL WIDTH="480">
<TBODY><TR>
    <TD>　</TD>
    <TD>利用者コード</TD>
    <TD>　</TD>
    <TD>

        <SCRIPT language="JavaScript">
        <!--
           Button1_set(750,"登録","検索","クリア","","","","","","");
           Button2_set(0,0,0,0,0,0,0,0,0);
        -->
        </SCRIPT>
    </TD>
</TR>
<TR>
    <TD>　</TD>
<%if(detailTable.user_cd[0] == null){%>
        <TD><INPUT size="28" type="text" name="text_usercd" value="" maxlength=7></TD>
<%} else {%>
        <TD><INPUT size="28" type="text" name="text_usercd" value="<%=detailTable.user_cd[0]%>" maxlength=7></TD>
<%}%>
</TR>
<TR>
    <TD>　</TD>
    <TD>　</TD>
    <TD>　</TD>
    <TD>
        <SCRIPT language="JavaScript">
        <!--
            Button1_set(720,"","","","前へ","次へ","","","","");
            Button2_set(0,0,0,0,0,0,0,0,0);
        -->
        </SCRIPT>
    </TD>
</TR>
<TR>
    <TD>　</TD>
    <TD>取引先種別</TD>
</TR><TR>
    <TD>　</TD>
    <TD>
        <SELECT name="select_customertype" style="width : 140px;" onChange="">
            <OPTION value=" ">
            <OPTION value="1">1 メーカーコード
            <OPTION value="2">2 取引先コード
        </SELECT>
    </TD>
    <TD>　</TD>
    <TD>
        <SCRIPT language="JavaScript">
        <!--
            Button1_set(720,"","","","","","閉じる","メニュー","","");
            Button2_set(0,0,0,0,0,0,0,0,0);
        -->
        </SCRIPT>
    </TD>
</TR><TR>
<TD>　</TD>
<TD>　</TD>
<TD>　</TD>
</TR><TR>
<TD>　</TD>
<TD>　</TD>
<TD>　</TD>
</TR><TR>
<TD>　</TD>
<TD>　</TD>
<TD>　</TD>
</TR><TR>
<TD>　</TD>
<TD>　</TD>
<TD>　</TD>
</TR><TR>
<TD>　</TD>
<TD>　</TD>
<TD>　</TD>
</TR><TR>
<TD>　</TD>
<TD>　</TD>
<TD>　</TD>
</TR><TR>
<TD>　</TD>
<TD>　</TD>
<TD>　</TD>
</TR><TR>
    <TD>　</TD>
    <TD>取引先コード</TD>
    <TD>　</TD>
    <TD>取引先名</TD>
</TR><TR>
    <TD>　</TD>
    <TD><INPUT size="28" type="text" name="customer_cd1" value="<%=detailTable.customer_cd[0]%>" maxlength=6></TD>
    <TD>　</TD>
    <TD><%=detailTable.customer_nm[0]%></TD>
</TR>
<TR>
<TD>　</TD>
<TD>　</TD>
<TD>　</TD>
</TR>
    <TD>　</TD>
    <TD><INPUT size="28" type="text" name="customer_cd2" value="<%=detailTable.customer_cd[1]%>" maxlength=6></TD>
    <TD>　</TD>
    <TD><%=detailTable.customer_nm[1]%></TD>
</TR><TR>
<TD>　</TD>
<TD>　</TD>
<TD>　</TD>
</TR><TR>
    <TD>　</TD>
    <TD><INPUT size="28" type="text" name="customer_cd3" value="<%=detailTable.customer_cd[2]%>" maxlength=6></TD>
    <TD>　</TD>
    <TD><%=detailTable.customer_nm[2]%></TD>
</TR><TR>
<TD>　</TD>
<TD>　</TD>
<TD>　</TD>
</TR><TR>
    <TD>　</TD>
    <TD><INPUT size="28" type="text" name="customer_cd4" value="<%=detailTable.customer_cd[3]%>" maxlength=6></TD>
    <TD>　</TD>
    <TD><%=detailTable.customer_nm[3]%></TD>
</TR><TR>
<TD>　</TD>
<TD>　</TD>
<TD>　</TD>
</TR><TR>
    <TD>　</TD>
    <TD><INPUT size="28" type="text" name="customer_cd5" value="<%=detailTable.customer_cd[4]%>" maxlength=6></TD>
    <TD>　</TD>
    <TD><%=detailTable.customer_nm[4]%></TD>
</TR><TR>
<TD>　</TD>
<TD>　</TD>
<TD>　</TD>
</TR><TR>
    <TD>　</TD>
    <TD><INPUT size="28" type="text" name="customer_cd6" value="<%=detailTable.customer_cd[5]%>" maxlength=6></TD>
    <TD>　</TD>
    <TD><%=detailTable.customer_nm[5]%></TD>
</TR><TR>
<TD>　</TD>
<TD>　</TD>
<TD>　</TD>
</TR><TR>
    <TD>　</TD>
    <TD><INPUT size="28" type="text" name="customer_cd7" value="<%=detailTable.customer_cd[6]%>" maxlength=6></TD>
    <TD>　</TD>
    <TD><%=detailTable.customer_nm[6]%></TD>
</TR><TR>
<TD>　</TD>
<TD>　</TD>
<TD>　</TD>
</TR><TR>
    <TD>　</TD>
    <TD><INPUT size="28" type="text" name="customer_cd8" value="<%=detailTable.customer_cd[7]%>" maxlength=6></TD>
    <TD>　</TD>
    <TD><%=detailTable.customer_nm[7]%></TD>
</TR><TR>
<TD>　</TD>
<TD>　</TD>
<TD>　</TD>
</TR><TR>
    <TD>　</TD>
    <TD><INPUT size="28" type="text" name="customer_cd9" value="<%=detailTable.customer_cd[8]%>" maxlength=6></TD>
    <TD>　</TD>
    <TD><%=detailTable.customer_nm[8]%></TD>
</TR><TR>
<TD>　</TD>
<TD>　</TD>
<TD>　</TD>
</TR><TR>
    <TD>　</TD>
    <TD><INPUT size="28" type="text" name="customer_cd10" value="<%=detailTable.customer_cd[9]%>" maxlength=6></TD>
    <TD>　</TD>
    <TD><%=detailTable.customer_nm[9]%></TD>
</TR><TR>
</TR></TBODY>
</TABLE>
<BR>
<!--ここ削除-->
</BODY>
</HTML>
<!--
 ************************
 *** 業務画面 *** END ***
 ************************
-->
<SCRIPT language="JavaScript">
<!--
   Screen_end();    // ****** 業務画面枠・終了指示 ******
-->
</SCRIPT>
<%=detailTable.getHimodukeTbl()%>
<INPUT type ="hidden" name="kensaku" value="0" >
<INPUT type ="hidden" name="kensaku_err" value="0" >
<INPUT type ="hidden" name="next" value="0" >
<INPUT type ="hidden" name="before" value="0" >
<INPUT type ="hidden" name="toroku" value="0" >
<INPUT type ="hidden" name="clear" value="0" >
<INPUT type ="hidden" name="usercd" value="<%=request.getParameter("usercd")%>" >
<INPUT type ="hidden" name="customertype" value="<%=request.getParameter("customertype")%>" >
<INPUT type ="hidden" name="err_type" value="" >
<!--取引先コード-->
<INPUT type ="hidden" name="customer1" value="<%=detailTable.customer_cd[0]%>" >
<INPUT type ="hidden" name="customer2" value="<%=detailTable.customer_cd[1]%>" >
<INPUT type ="hidden" name="customer3" value="<%=detailTable.customer_cd[2]%>" >
<INPUT type ="hidden" name="customer4" value="<%=detailTable.customer_cd[3]%>" >
<INPUT type ="hidden" name="customer5" value="<%=detailTable.customer_cd[4]%>" >
<INPUT type ="hidden" name="customer6" value="<%=detailTable.customer_cd[5]%>" >
<INPUT type ="hidden" name="customer7" value="<%=detailTable.customer_cd[6]%>" >
<INPUT type ="hidden" name="customer8" value="<%=detailTable.customer_cd[7]%>" >
<INPUT type ="hidden" name="customer9" value="<%=detailTable.customer_cd[8]%>" >
<INPUT type ="hidden" name="customer10" value="<%=detailTable.customer_cd[9]%>" >

<INPUT type ="hidden" name="customernm1" value="<%=detailTable.customer_nm[0]%>" >
<INPUT type ="hidden" name="customernm2" value="<%=detailTable.customer_nm[1]%>" >
<INPUT type ="hidden" name="customernm3" value="<%=detailTable.customer_nm[2]%>" >
<INPUT type ="hidden" name="customernm4" value="<%=detailTable.customer_nm[3]%>" >
<INPUT type ="hidden" name="customernm5" value="<%=detailTable.customer_nm[4]%>" >
<INPUT type ="hidden" name="customernm6" value="<%=detailTable.customer_nm[5]%>" >
<INPUT type ="hidden" name="customernm7" value="<%=detailTable.customer_nm[6]%>" >
<INPUT type ="hidden" name="customernm8" value="<%=detailTable.customer_nm[7]%>" >
<INPUT type ="hidden" name="customernm9" value="<%=detailTable.customer_nm[8]%>" >
<INPUT type ="hidden" name="customernm10" value="<%=detailTable.customer_nm[9]%>" >
<!--
<INPUT type ="hidden" name="customer1" value="<%=request.getParameter("customer1")%>" >
<INPUT type ="hidden" name="customer2" value="<%=request.getParameter("customer2")%>" >
<INPUT type ="hidden" name="customer3" value="<%=request.getParameter("customer3")%>" >
<INPUT type ="hidden" name="customer4" value="<%=request.getParameter("customer4")%>" >
<INPUT type ="hidden" name="customer5" value="<%=request.getParameter("customer5")%>" >
<INPUT type ="hidden" name="customer6" value="<%=request.getParameter("customer6")%>" >
<INPUT type ="hidden" name="customer7" value="<%=request.getParameter("customer7")%>" >
<INPUT type ="hidden" name="customer8" value="<%=request.getParameter("customer8")%>" >
<INPUT type ="hidden" name="customer9" value="<%=request.getParameter("customer9")%>" >
<INPUT type ="hidden" name="customer10" value="<%=request.getParameter("customer10")%>" >
-->
</FORM>
</BODY>
</HTML>
<%@ include file="Xysflj_page_footer.jsp"%>