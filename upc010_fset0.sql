/********************************************************************************/
/*　　　ＰＧＭ名　　　　：upc_コントロール更新
/*　　　ＰＧＭID　　　　：upc010_fset0.sql
/*　　　機能　　　　　　：upc_コントロール更新処理
/*　　　　　　　　　　　　　　　　
/*　　　対象テーブル　　：upc_コントロール
/*　　　作成者　　　　　：tanaka
/*　　　作成日　　　　　：????年??月??日
/*　　　修正履歴　　　　：2006/05/26  Ｅ１０Ｋ移行伴い、ソースヘッダ部、
/*　　　　　　　　　　　　　　　　　　宣言部、例外処理を追加しました。
/********************************************************************************/
whenever oserror  exit sql.sqlcode rollback
whenever sqlerror exit sql.sqlcode rollback
set feed off
set verify off
set heading off
set underline off
set termout on
set serveroutput on size 20000

variable RtnCd     number
variable ErrMsg    varchar2(100)

---------------------------------------------
-- 変数宣言
---------------------------------------------
DECLARE
  w_date       varchar2(10);  -- 日付
  w_time_start varchar2(19);  -- プログラム開始日時
  w_time_end   varchar2(19);  -- プログラム終了日時

BEGIN
---------------------------------------------
-- 初期処理
---------------------------------------------
    select to_char(sysdate,'yyyy/mm/dd hh24:mi:ss') into w_time_start from dual;
    dbms_output.put_line('upc010_fset0.sql : start time = ' || w_time_start);
    :RtnCd := 0;
    :ErrMsg := substr(sqlerrm,1,100);
--
UPDATE UPC_コントロール
 SET   コントロール区分 = '0',
       作成者ＩＤ = 'SYS',
       作成日時 = SYSDATE
 WHERE 制御種別 = '01';
commit;
--
---------------------------------------------
-- 終了処理
---------------------------------------------
    select to_char(sysdate,'yyyy/mm/dd hh24:mi:ss') into w_time_end from dual;
    dbms_output.put_line('upc010_fset0.sql :  end  time = ' || w_time_end);

---------------------------------------------
-- 例外処理
---------------------------------------------
EXCEPTION
  when others then
    :RtnCd := sqlcode;
    :ErrMsg := substr(sqlerrm,1,100);
    rollback;
END;
/

print RtnCd
print ErrMsg

EXIT :RtnCd;
/
