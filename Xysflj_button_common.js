<!-- JavaScript Code
//-----------------------------------------------------------------------------------#
// HTML_Name    : ボタン制御共通処理 Java-Script
// Name         : T.Kusajima
// Date         : 2003/08/19
// No.  Date            Name            Description
//-----------------------------------------------------------------------------------#
    //**************************************
    // 関数名 ： submit時コントロール制御処理
    // 概要   ： submit時の各コントロールをロックする。
    // 引数   ： frm (I) フォームオブジェクト
    // 戻り値 ： なし
    // 備考   ： 
    //**************************************
    function lockControlSubmit( frm ) {
        var i = 0;

        // 共通submitボタン
        if (frm.submit_button1 != null) {
            frm.submit_button1.disabled = true;
        }
        if (frm.submit_button2 != null) {
            frm.submit_button2.disabled = true;
        }
        if (frm.submit_button3 != null) {
            frm.submit_button3.disabled = true;
        }
        if (frm.submit_button4 != null) {
            frm.submit_button4.disabled = true;
        }
        if (frm.submit_button5 != null) {
            frm.submit_button5.disabled = true;
        }
        if (frm.submit_button6 != null) {
            frm.submit_button6.disabled = true;
        }
        if (frm.submit_button7 != null) {
            frm.submit_button7.disabled = true;
        }
        if (frm.submit_button8 != null) {
            frm.submit_button8.disabled = true;
        }
        if (frm.submit_button9 != null) {
            frm.submit_button9.disabled = true;
        }

        // 共通ボタン
        if (frm.close_button != null) {
            frm.close_button.disabled = true;
        }
        if (frm.dl_button != null) {
            frm.dl_button.disabled = true;
        }
        if (frm.mn_button != null) {
            frm.mn_button.disabled = true;
        }
        if (frm.prt_button != null) {
            frm.prt_button.disabled = true;
        }
        if (frm.help_button != null) {
            frm.help_button.disabled = true;
        }
        if (frm.up_button != null) {
            frm.up_button.disabled = true;
        }
        if (frm.down_button != null) {
            frm.down_button.disabled = true;
        }
        if (frm.left_button != null) {
            frm.left_button.disabled = true;
        }
        if (frm.right_button) {
            frm.right_button.disabled = true;
        }

        // 計算ボタン
        if (frm.calc_button != null) {
            frm.calc_button.disabled = true;
        }

        // 合計ボタン
        if (frm.total_button != null) {
            frm.total_button.disabled = true;
        }

        // 平均ボタン
        if (frm.avg_button != null) {
            frm.avg_button.disabled = true;
        }

        // 昇順ボタン
        if (frm.asc_button != null) {
            frm.asc_button.disabled = true;
        }

        // 降順ボタン
        if (frm.desc_button != null) {
            frm.desc_button.disabled = true;
        }

        // 全国／支社別／運営部別ボタン
        if (frm.search_type != null) {
            frm.search_type.disabled = true;
        }

        // 検索ボタン
        if (frm.btn_search != null) {
            frm.btn_search.disabled = true;
        }

        // 店選択ボタン
        if (frm.ten_btn != null) {
            frm.ten_btn.disabled = true;
        }

        // 店選択ボタン
        if (frm.ten_button != null) {
            frm.ten_button.disabled = true;
        }

        // 前月／当月ボタン
        if (frm.month_btn != null) {
            frm.month_btn.disabled = true;
        }

        // 検索ボタン
        if (frm.search_button != null) {
            frm.search_button.disabled = true;
        }

    }
    
    //**************************************
    // 関数名 ： メニューへ戻るボタンの制御。
    // 概要   ： メニュー画面にフォーカスを移す。
    // 戻り値 ： なし
    // 備考   ： 
    //**************************************
    function menu_chk() {
         window.opener.focus();
    }
// -->
