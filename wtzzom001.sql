-------------------------------------------------------------------------------
-- System Name            : ＭＳＩ
-- Apprication System Name: 共通システムwt
-- Program Name           : メニューサービス区分更新処理
-- File Name              : wtzzom001.sql
-- Author                 : Y.Okuhira
-- Date                   : 2003/07/16 (Create)
-- Modify Date            : 2003/08/11 Y.Okuhira 処理開始終了日時の表示
--                        : 2003/08/18 Y.Okuhira サービス区分変更'12'⇒'21'
-------------------------------------------------------------------------------
-- Note:
--        共通メニューセキュリティー提供のＰＬ／ＳＱＬパッケージ
--        sfzzop021（SFM_メニュー単位.サービス区分の更新）をＣＡＬＬし、
--        SFM_メニュー単位@le2bのサービス区分を更新する。
-------------------------------------------------------------------------------
-- Input:
--        PARM1:メニュー単位
--        PARM2:サービス区分
--                            '00':サービス中
--                            '02':バッチ処理中のためサービス停止
--                            '21':月次バッチ処理中のためサービス停止
-- Output:
--        SFM_メニュー単位@le2b
-------------------------------------------------------------------------------

set feed off
set verify off
set heading off
set underline off
set termout on
set serveroutput on
whenever oserror  exit sql.oscode rollback
whenever sqlerror exit sql.sqlcode rollback

variable RtnCd     number
variable ErrMsg    varchar2(100)

---------------------------------------------
-- 変数宣言
---------------------------------------------
DECLARE
  w_menu       varchar2(40);  -- メニュー単位
  w_sv_kbn     varchar2(02);  -- サービス区分
  pkg_rtn      varchar2(02);  -- FUNCTION リターン値
  w_time_start varchar2(19);  -- プログラム開始日時
  w_time_end   varchar2(19);  -- プログラム終了日時

---------------------------------------------
-- メイン処理
---------------------------------------------
BEGIN
    select to_char(sysdate,'yyyy/mm/dd hh24:mi:ss') into w_time_start from dual;
    dbms_output.put_line('sfadom029.sql : start time = ' || w_time_start);
    :RtnCd := 0;
    :ErrMsg := substr(sqlerrm,1,100);

---------------------------------------------
-- FUNCTIONの引数設定
---------------------------------------------
    w_menu   := '&&1';  -- メニュー単位
    w_sv_kbn := '&&2';  -- サービス区分
    dbms_output.put_line('【メニュー単位 : ' || w_menu || '／サービス区分:' || w_sv_kbn || '】');

---------------------------------------------
-- パッケージsfzzop021のＣＡＬＬ
---------------------------------------------
    pkg_rtn  := sfzzop021.main(w_menu,w_sv_kbn);

---------------------------------------------
-- FUNCTIONの返却値をSQLのリターン値に設定
---------------------------------------------
    :RtnCd   := pkg_rtn;

    if  pkg_rtn  !=  '00'  then
        :ErrMsg := '++ wtzzom001.sql FUNCTION sfzzop021.main ' ||
                   'CALL ERR STATUS = "' || pkg_rtn || '" ++';
    end if;

    select to_char(sysdate,'yyyy/mm/dd hh24:mi:ss') into w_time_end from dual;
    dbms_output.put_line('sfadom029.sql :  end  time = ' || w_time_end);

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
