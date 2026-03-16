#!/bin/csh
#----------------------------------------------------------------------#
# ｓｈｅｌｌ−ｉｄ: supbd2062
# ｓｈｅｌｌ名    : supbd2062.csh
# 機能            : WEB連携KEYﾃﾞｰﾀ(発売日２)コード変換(新聞用)
# 作成者          : T.HARUYAMA
# 作成日          : 2004/01/19
# 修正履歴        :
#    x  2006/05/12  M.Tanaka    Ｅ１０Ｋ移行ガイドライン対応
#----------------------------------------------------------------------#
#----------------------------------------------------------------------#
# 初期処理部
#----------------------------------------------------------------------#
#E10K BEF#set SUBSRCDIR = "/prod/jcl/sub"
set PROD_DIR = /prod
set SUBSRCDIR = "${PROD_DIR}/jcl/sub"
#
source $SUBSRCDIR/upbd.src
source $SUBSRCDIR/common.src
#
# <COUNTCHK PARM SET>
#
set MB_FILE  = $WKDIR03/upw289p2.mbx
set HDR_FILE = $WKDIR02/upw289p2.chk
set DAT_FILE = $WKDIR01/upw289p2.dat
#
#----------------------------------------------------------------------#
# メイン部
#----------------------------------------------------------------------#
#----------------------------------------------------------------------#
#       カウントチェック
#----------------------------------------------------------------------#
source $SUBSRCDIR/countchk3.src
if ( $status != $NORMAL ) then
    exit $ABEND
endif
#
#----------------------------------------------------------------------#
#    　ファイル切り出し／変換
#----------------------------------------------------------------------#
echo "++ File Conversion Start Time = `date +%y/%m/%d:%H:%M:%S`"    >> $LOG_FILE
#
#E10K BEF#set MDPORTF_IN  = "$WKDIR01/upw289p2.dat"
set CONV_IN  = "$WKDIR01/upw289p2.dat"
#E10K BEF#set I_RECFM     = "f"
set I_CODE      = "euc"
#E10K BEF#set MDPORTF_OUT = "$WKDIR01/upw289p2.sjs"
set CONV_OUT = "$WKDIR01/upw289p2.sjs"
#E10K BEF#set O_RECFM     = "f"
set O_CODE      = "sjis"
#E10K BEF#set FORMATF     = "$CTLDIR/upw289p.ctl"
set FORMATF     = "${CODE_CTLDIR}/upw289p.fmt"
set I_RECDLM    = lf
set O_RECDLM    = lf
#------------------------------------------------------------------------#
#      文字コード変換
#------------------------------------------------------------------------#
#E10K BEF#source $SUBSRCDIR/mdportf.src
source $SUBSRCDIR/fixrec_conv.src
if ( $status != $NORMAL ) then
    echo "$SHELLNAME:t :  ABEND time : `date +%y/%m/%d:%H:%M:%S`" >> $LOG_FILE
    exit $ABEND
endif
#
echo "++ File Conversion  End  Time = `date +%y/%m/%d:%H:%M:%S`"    >> $LOG_FILE
#
#----------------------------------------------------------------------#
# 後処理部
#----------------------------------------------------------------------#
echo "$SHELLNAME:t : mdport : end   time = `date +%y/%m/%d:%H:%M:%S`" \
                                                                    >> $LOG_FILE
echo "$SHELLNAME:t : normal end"                                    >> $LOG_FILE
exit $NORMAL
#
