#!/bin/csh
#------------------------------------------------------------------------#
# Shell-id     : supbd1010
# Shell-名     : supbd1010.csh
# 機能         : メニューサービス区分更新処理（新聞競馬新聞実績オンライン立ち上げ）
#                wtzzom001.sql
#                  PARM1:メニュー単位
#                  PARM2:サービス区分
#                        '00':サービス中
#                        '02':バッチ処理中のためサービス停止
#                        '03':トラブルのためサービス停止
#                        '04':マシン保守のためサービス停止
#                        '21':月次バッチ処理中のためサービス停止
# 作成者       : T.HARUYAMA
# 作成日       : 2004/02/05
# 修正履歴     :
# Ｎｏ   修正日付   修正者     修正内容
#    1  ----/--/--  XXXXXXXXX  XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
#---+----1----+----2----+----3----+----4----+----5----+----6----+----7---
#    x  2006/05/12  M.Tanaka    Ｅ１０Ｋ移行ガイドライン対応
#------------------------------------------------------------------------#
#       初期処理部
#------------------------------------------------------------------------#
#E10K BEF#set SUBSRCDIR="/prod/jcl/sub"
set PROD_DIR = /prod
set SUBSRCDIR="${PROD_DIR}/jcl/sub"
#
source $SUBSRCDIR/upbd.src
source $SUBSRCDIR/common.src
#
setenv ORAUID   $ORAUID
setenv ORAUIDL  $ORAUIDL
setenv ORAPWD   $ORAPWD
setenv ORAPWDL  $ORAPWDL
#
#------------------------------------------------------------------------#
#       wtzzom001.sql （210 DEPT-CLASS-SUBCLASS-発行形態）
#------------------------------------------------------------------------#
#E10K BEF#set SQL_PARM=(-s $ORAUID/$ORAPWD @$SQLDIR/wtzzom001.sql 'XY_XYKJ0210' '00')
set SQL_PARM=(-s $ORAUID/$ORAPWD @${PLSQLDIR}/wtzzom001.sql 'XY_XYKJ0210' '00')
#
# ORACLE TABLE SFM_メニュー単位@XXXX
#
source $SUBSRCDIR/execsql.src
if ( $status != $NORMAL ) then
        exit $ABEND
endif
#
#------------------------------------------------------------------------#
#       wtzzom001.sql （220 新聞社−発行形態）
#------------------------------------------------------------------------#
#E10K BEF#set SQL_PARM=(-s $ORAUID/$ORAPWD @$SQLDIR/wtzzom001.sql 'XY_XYKJ0220' '00')
set SQL_PARM=(-s $ORAUID/$ORAPWD @${PLSQLDIR}/wtzzom001.sql 'XY_XYKJ0220' '00')
#
# ORACLE TABLE SFM_メニュー単位@XXXX
#
source $SUBSRCDIR/execsql.src
if ( $status != $NORMAL ) then
        exit $ABEND
endif
#
#------------------------------------------------------------------------#
#       wtzzom001.sql （230 新聞社　商品／発売日（全国）別）
#------------------------------------------------------------------------#
#E10K BEF#set SQL_PARM=(-s $ORAUID/$ORAPWD @$SQLDIR/wtzzom001.sql 'XY_XYKJ0230' '00')
set SQL_PARM=(-s $ORAUID/$ORAPWD @${PLSQLDIR}/wtzzom001.sql 'XY_XYKJ0230' '00')
#
# ORACLE TABLE SFM_メニュー単位@XXXX
#
source $SUBSRCDIR/execsql.src
if ( $status != $NORMAL ) then
        exit $ABEND
endif
#
#------------------------------------------------------------------------#
#    コントロール区分　開始フラグ <0> セット
#------------------------------------------------------------------------#
#E10K BEF#set SQL_PARM=(-s $ORAUID/$ORAPWD @$SQLDIR/upc020_fset0.sql)
set SQL_PARM=(-s $ORAUID/$ORAPWD @${PLSQLDIR}/upc020_fset0.sql)
source $SUBSRCDIR/execsql.src
if ( $status != $NORMAL ) then
        exit $ABEND
endif
#------------------------------------------------------------------------#
#       後処理部
#------------------------------------------------------------------------#
echo "$SHELLNAME:t :  end  time : `date +%y/%m/%d:%H:%M:%S`" >> $LOG_FILE
exit $NORMAL
