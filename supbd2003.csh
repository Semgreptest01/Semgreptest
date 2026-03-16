#!/bin/csh
#------------------------------------------------------------------------#
# Shell-id     : supbd2003
# Shell-名     : supbd2003.csh
# 機能         : メニューサービス区分(取引先)更新処理（新聞競馬新聞実績バッチ開始）
#                wtzzom003.sql
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
#       wtzzom003.sql （220 出版社−発行形態）
#------------------------------------------------------------------------#
#E10K BEF#set SQL_PARM=(-s $ORAUID/$ORAPWD @$SQLDIR/wtzzom003.sql 'XY_XYSK1805' '02')
set SQL_PARM=(-s $ORAUID/$ORAPWD @${PLSQLDIR}/wtzzom003.sql 'XY_XYSK1805' '02')
#
# ORACLE TABLE SMM_メニュー単位@XXXX
#
source $SUBSRCDIR/execsql.src
if ( $status != $NORMAL ) then
        exit $ABEND
endif
#
#------------------------------------------------------------------------#
#       wtzzom003.sql （230 新聞社　商品／発売日（全国）別）
#------------------------------------------------------------------------#
#E10K BEF#set SQL_PARM=(-s $ORAUID/$ORAPWD @$SQLDIR/wtzzom003.sql 'XY_XYSK1806' '02')
set SQL_PARM=(-s $ORAUID/$ORAPWD @${PLSQLDIR}/wtzzom003.sql 'XY_XYSK1806' '02')
#
# ORACLE TABLE SMM_メニュー単位@XXXX
#
source $SUBSRCDIR/execsql.src
if ( $status != $NORMAL ) then
        exit $ABEND
endif
#
#------------------------------------------------------------------------#
#       後処理部
#------------------------------------------------------------------------#
echo "$SHELLNAME:t :  end  time : `date +%y/%m/%d:%H:%M:%S`" >> $LOG_FILE
exit $NORMAL
