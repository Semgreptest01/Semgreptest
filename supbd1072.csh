#!/bin/csh
#----------------------------------------------------------------------#
# ｓｈｅｌｌ−ｉｄ: supbd1072
# ｓｈｅｌｌ名    : supbd1072.csh
# 機能            : 県刊行別・県サマリーデータコード変換・ファイル連結
# 作成者          : K.Nasu  
# 作成日          : 2001/03/01
# 修正履歴        :
#    1  2003/06/20  M.Saitou   /prod/jcl/msisub -> /prod/jcl/sub
#                              コード変換 bcut -> mdport へ変更
#    2  2003/06/24  M.Saitou   upw295p.euc -> upw295p.sjs
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
#---------------------------<1/10>--------------------------------------#
# <COUNTCHK PARM SET>
#
set MB_FILE  = $WKDIR03/upw295p1.mbx
set HDR_FILE = $WKDIR02/upw295p1.chk
set DAT_FILE = $WKDIR01/upw295p1.dat
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
#E10K BEF#set MDPORTF_IN  = "$WKDIR01/upw295p1.dat"
set CONV_IN  = "$WKDIR01/upw295p1.dat"
#E10K BEF#set I_RECFM     = "f"
set I_CODE      = "euc"
#E10K BEF#set MDPORTF_OUT = "$WKDIR01/upw295p1.sjs"
set CONV_OUT = "$WKDIR01/upw295p1.sjs"
#E10K BEF#set O_RECFM     = "f"
set O_CODE      = "sjis"
#E10K BEF#set FORMATF     = "$CTLDIR/upw295p.ctl"
set FORMATF     = "${CODE_CTLDIR}/upw295p.fmt"
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
cat $WKDIR01/upw295p1.sjs > $WKDIR01/upw295p.sjs
#
echo "++ File Conversion  End  Time = `date +%y/%m/%d:%H:%M:%S`"    >> $LOG_FILE
#
#---------------------< FILE DELETE >-----------------------------------#
rm $WKDIR01/upw295p1.dat
#---------------------------<2/10>--------------------------------------#
#
# <COUNTCHK PARM SET>
#
set MB_FILE  = $WKDIR03/upw295p2.mbx
set HDR_FILE = $WKDIR02/upw295p2.chk
set DAT_FILE = $WKDIR01/upw295p2.dat
#
#----------------------------------------------------------------------#
#       カウントチェック
#----------------------------------------------------------------------#
source $SUBSRCDIR/countchk3.src
if ( $status != $NORMAL ) then
    exit $ABEND
endif
#
#----------------------------------------------------------------------#
#    　ファイル切り出し／変換　ファイル連結
#----------------------------------------------------------------------#
echo "++ File Conversion Start Time = `date +%y/%m/%d:%H:%M:%S`"    >> $LOG_FILE
#
#E10K BEF#set MDPORTF_IN  = "$WKDIR01/upw295p2.dat"
set CONV_IN  = "$WKDIR01/upw295p2.dat"
#E10K BEF#set I_RECFM     = "f"
set I_CODE      = "euc"
#E10K BEF#set MDPORTF_OUT = "$WKDIR01/upw295p2.sjs"
set CONV_OUT = "$WKDIR01/upw295p2.sjs"
#E10K BEF#set O_RECFM     = "f"
set O_CODE      = "sjis"
#E10K BEF#set FORMATF     = "$CTLDIR/upw295p.ctl"
set FORMATF     = "${CODE_CTLDIR}/upw295p.fmt"
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
cat $WKDIR01/upw295p2.sjs >> $WKDIR01/upw295p.sjs
#
echo "++ File Conversion  End  Time = `date +%y/%m/%d:%H:%M:%S`"    >> $LOG_FILE
#
#---------------------< FILE DELETE >-----------------------------------#
rm $WKDIR01/upw295p2.dat
#---------------------------<3/10>--------------------------------------#
#
# <COUNTCHK PARM SET>
#
set MB_FILE  = $WKDIR03/upw295p3.mbx
set HDR_FILE = $WKDIR02/upw295p3.chk
set DAT_FILE = $WKDIR01/upw295p3.dat
#
#----------------------------------------------------------------------#
#       カウントチェック
#----------------------------------------------------------------------#
source $SUBSRCDIR/countchk3.src
if ( $status != $NORMAL ) then
    exit $ABEND
endif
#
#----------------------------------------------------------------------#
#    　ファイル切り出し／変換　ファイル連結
#----------------------------------------------------------------------#
echo "++ File Conversion Start Time = `date +%y/%m/%d:%H:%M:%S`"    >> $LOG_FILE
#
#E10K BEF#set MDPORTF_IN  = "$WKDIR01/upw295p3.dat"
set CONV_IN  = "$WKDIR01/upw295p3.dat"
#E10K BEF#set I_RECFM     = "f"
set I_CODE      = "euc"
#E10K BEF#set MDPORTF_OUT = "$WKDIR01/upw295p3.sjs"
set CONV_OUT = "$WKDIR01/upw295p3.sjs"
#E10K BEF#set O_RECFM     = "f"
set O_CODE      = "sjis"
#E10K BEF#set FORMATF     = "$CTLDIR/upw295p.ctl"
set FORMATF     = "${CODE_CTLDIR}/upw295p.fmt"
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
cat $WKDIR01/upw295p3.sjs >> $WKDIR01/upw295p.sjs
#
echo "++ File Conversion  End  Time = `date +%y/%m/%d:%H:%M:%S`"    >> $LOG_FILE
#
#---------------------< FILE DELETE >-----------------------------------#
rm $WKDIR01/upw295p3.dat
#---------------------------<4/10>--------------------------------------#
#
# <COUNTCHK PARM SET>
#
set MB_FILE  = $WKDIR03/upw295p4.mbx
set HDR_FILE = $WKDIR02/upw295p4.chk
set DAT_FILE = $WKDIR01/upw295p4.dat
#
#----------------------------------------------------------------------#
#       カウントチェック
#----------------------------------------------------------------------#
source $SUBSRCDIR/countchk3.src
if ( $status != $NORMAL ) then
    exit $ABEND
endif
#
#----------------------------------------------------------------------#
#    　ファイル切り出し／変換　ファイル連結
#----------------------------------------------------------------------#
echo "++ File Conversion Start Time = `date +%y/%m/%d:%H:%M:%S`"    >> $LOG_FILE
#
#E10K BEF#set MDPORTF_IN  = "$WKDIR01/upw295p4.dat"
set CONV_IN  = "$WKDIR01/upw295p4.dat"
#E10K BEF#set I_RECFM     = "f"
set I_CODE      = "euc"
#E10K BEF#set MDPORTF_OUT = "$WKDIR01/upw295p4.sjs"
set CONV_OUT = "$WKDIR01/upw295p4.sjs"
#E10K BEF#set O_RECFM     = "f"
set O_CODE      = "sjis"
#E10K BEF#set FORMATF     = "$CTLDIR/upw295p.ctl"
set FORMATF     = "${CODE_CTLDIR}/upw295p.fmt"
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
cat $WKDIR01/upw295p4.sjs >> $WKDIR01/upw295p.sjs
#
echo "++ File Conversion  End  Time = `date +%y/%m/%d:%H:%M:%S`"    >> $LOG_FILE
#
#---------------------< FILE DELETE >-----------------------------------#
rm $WKDIR01/upw295p4.dat
#---------------------------<5/10>--------------------------------------#
#
# <COUNTCHK PARM SET>
#
set MB_FILE  = $WKDIR03/upw295p5.mbx
set HDR_FILE = $WKDIR02/upw295p5.chk
set DAT_FILE = $WKDIR01/upw295p5.dat
#
#----------------------------------------------------------------------#
#       カウントチェック
#----------------------------------------------------------------------#
source $SUBSRCDIR/countchk3.src
if ( $status != $NORMAL ) then
    exit $ABEND
endif
#
#----------------------------------------------------------------------#
#    　ファイル切り出し／変換　ファイル連結
#----------------------------------------------------------------------#
echo "++ File Conversion Start Time = `date +%y/%m/%d:%H:%M:%S`"    >> $LOG_FILE
#
#E10K BEF#set MDPORTF_IN  = "$WKDIR01/upw295p5.dat"
set CONV_IN  = "$WKDIR01/upw295p5.dat"
#E10K BEF#set I_RECFM     = "f"
set I_CODE      = "euc"
#E10K BEF#set MDPORTF_OUT = "$WKDIR01/upw295p5.sjs"
set CONV_OUT = "$WKDIR01/upw295p5.sjs"
#E10K BEF#set O_RECFM     = "f"
set O_CODE      = "sjis"
#E10K BEF#set FORMATF     = "$CTLDIR/upw295p.ctl"
set FORMATF     = "${CODE_CTLDIR}/upw295p.fmt"
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
cat $WKDIR01/upw295p5.sjs >> $WKDIR01/upw295p.sjs
#
echo "++ File Conversion  End  Time = `date +%y/%m/%d:%H:%M:%S`"    >> $LOG_FILE
#
#---------------------< FILE DELETE >-----------------------------------#
rm $WKDIR01/upw295p5.dat
#---------------------------<6/10>--------------------------------------#
#
# <COUNTCHK PARM SET>
#
set MB_FILE  = $WKDIR03/upw295p6.mbx
set HDR_FILE = $WKDIR02/upw295p6.chk
set DAT_FILE = $WKDIR01/upw295p6.dat
#
#----------------------------------------------------------------------#
#       カウントチェック
#----------------------------------------------------------------------#
source $SUBSRCDIR/countchk3.src
if ( $status != $NORMAL ) then
    exit $ABEND
endif
#
#----------------------------------------------------------------------#
#    　ファイル切り出し／変換　ファイル連結
#----------------------------------------------------------------------#
echo "++ File Conversion Start Time = `date +%y/%m/%d:%H:%M:%S`"    >> $LOG_FILE
#
#E10K BEF#set MDPORTF_IN  = "$WKDIR01/upw295p6.dat"
set CONV_IN  = "$WKDIR01/upw295p6.dat"
#E10K BEF#set I_RECFM     = "f"
set I_CODE      = "euc"
#E10K BEF#set MDPORTF_OUT = "$WKDIR01/upw295p6.sjs"
set CONV_OUT = "$WKDIR01/upw295p6.sjs"
#E10K BEF#set O_RECFM     = "f"
set O_CODE      = "sjis"
#E10K BEF#set FORMATF     = "$CTLDIR/upw295p.ctl"
set FORMATF     = "${CODE_CTLDIR}/upw295p.fmt"
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
cat $WKDIR01/upw295p6.sjs >> $WKDIR01/upw295p.sjs
#
echo "++ File Conversion  End  Time = `date +%y/%m/%d:%H:%M:%S`"    >> $LOG_FILE
#
#---------------------< FILE DELETE >-----------------------------------#
rm $WKDIR01/upw295p6.dat
#---------------------------<7/10>--------------------------------------#
#
# <COUNTCHK PARM SET>
#
set MB_FILE  = $WKDIR03/upw295p7.mbx
set HDR_FILE = $WKDIR02/upw295p7.chk
set DAT_FILE = $WKDIR01/upw295p7.dat
#
#----------------------------------------------------------------------#
#       カウントチェック
#----------------------------------------------------------------------#
source $SUBSRCDIR/countchk3.src
if ( $status != $NORMAL ) then
    exit $ABEND
endif
#
#----------------------------------------------------------------------#
#    　ファイル切り出し／変換　ファイル連結
#----------------------------------------------------------------------#
echo "++ File Conversion Start Time = `date +%y/%m/%d:%H:%M:%S`"    >> $LOG_FILE
#
#E10K BEF#set MDPORTF_IN  = "$WKDIR01/upw295p7.dat"
set CONV_IN  = "$WKDIR01/upw295p7.dat"
#E10K BEF#set I_RECFM     = "f"
set I_CODE      = "euc"
#E10K BEF#set MDPORTF_OUT = "$WKDIR01/upw295p7.sjs"
set CONV_OUT = "$WKDIR01/upw295p7.sjs"
#E10K BEF#set O_RECFM     = "f"
set O_CODE      = "sjis"
#E10K BEF#set FORMATF     = "$CTLDIR/upw295p.ctl"
set FORMATF     = "${CODE_CTLDIR}/upw295p.fmt"
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
cat $WKDIR01/upw295p7.sjs >> $WKDIR01/upw295p.sjs
#
echo "++ File Conversion  End  Time = `date +%y/%m/%d:%H:%M:%S`"    >> $LOG_FILE
#
#---------------------< FILE DELETE >-----------------------------------#
rm $WKDIR01/upw295p7.dat
#---------------------------<8/10>--------------------------------------#
#
# <COUNTCHK PARM SET>
#
set MB_FILE  = $WKDIR03/upw295p8.mbx
set HDR_FILE = $WKDIR02/upw295p8.chk
set DAT_FILE = $WKDIR01/upw295p8.dat
#
#----------------------------------------------------------------------#
#       カウントチェック
#----------------------------------------------------------------------#
source $SUBSRCDIR/countchk3.src
if ( $status != $NORMAL ) then
    exit $ABEND
endif
#
#----------------------------------------------------------------------#
#    　ファイル切り出し／変換　ファイル連結
#----------------------------------------------------------------------#
echo "++ File Conversion Start Time = `date +%y/%m/%d:%H:%M:%S`"    >> $LOG_FILE
#
#E10K BEF#set MDPORTF_IN  = "$WKDIR01/upw295p8.dat"
set CONV_IN  = "$WKDIR01/upw295p8.dat"
#E10K BEF#set I_RECFM     = "f"
set I_CODE      = "euc"
#E10K BEF#set MDPORTF_OUT = "$WKDIR01/upw295p8.sjs"
set CONV_OUT = "$WKDIR01/upw295p8.sjs"
#E10K BEF#set O_RECFM     = "f"
set O_CODE      = "sjis"
#E10K BEF#set FORMATF     = "$CTLDIR/upw295p.ctl"
set FORMATF     = "${CODE_CTLDIR}/upw295p.fmt"
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
cat $WKDIR01/upw295p8.sjs >> $WKDIR01/upw295p.sjs
#
echo "++ File Conversion  End  Time = `date +%y/%m/%d:%H:%M:%S`"    >> $LOG_FILE
#
#---------------------< FILE DELETE >-----------------------------------#
rm $WKDIR01/upw295p8.dat
#---------------------------<9/10>--------------------------------------#
#
# <COUNTCHK PARM SET>
#
set MB_FILE  = $WKDIR03/upw295p9.mbx
set HDR_FILE = $WKDIR02/upw295p9.chk
set DAT_FILE = $WKDIR01/upw295p9.dat
#
#----------------------------------------------------------------------#
#       カウントチェック
#----------------------------------------------------------------------#
source $SUBSRCDIR/countchk3.src
if ( $status != $NORMAL ) then
    exit $ABEND
endif
#
#----------------------------------------------------------------------#
#    　ファイル切り出し／変換　ファイル連結
#----------------------------------------------------------------------#
echo "++ File Conversion Start Time = `date +%y/%m/%d:%H:%M:%S`"    >> $LOG_FILE
#
#E10K BEF#set MDPORTF_IN  = "$WKDIR01/upw295p9.dat"
set CONV_IN  = "$WKDIR01/upw295p9.dat"
#E10K BEF#set I_RECFM     = "f"
set I_CODE      = "euc"
#E10K BEF#set MDPORTF_OUT = "$WKDIR01/upw295p9.sjs"
set CONV_OUT = "$WKDIR01/upw295p9.sjs"
#E10K BEF#set O_RECFM     = "f"
set O_CODE      = "sjis"
#E10K BEF#set FORMATF     = "$CTLDIR/upw295p.ctl"
set FORMATF     = "${CODE_CTLDIR}/upw295p.fmt"
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
cat $WKDIR01/upw295p9.sjs >> $WKDIR01/upw295p.sjs
#
echo "++ File Conversion  End  Time = `date +%y/%m/%d:%H:%M:%S`"    >> $LOG_FILE
#
#---------------------< FILE DELETE >-----------------------------------#
rm $WKDIR01/upw295p9.dat
#---------------------------<10/10>--------------------------------------#
#
# <COUNTCHK PARM SET>
#
set MB_FILE  = $WKDIR03/upw295p10.mbx
set HDR_FILE = $WKDIR02/upw295p10.chk
set DAT_FILE = $WKDIR01/upw295p10.dat
#
#----------------------------------------------------------------------#
#       カウントチェック
#----------------------------------------------------------------------#
source $SUBSRCDIR/countchk3.src
if ( $status != $NORMAL ) then
    exit $ABEND
endif
#
#----------------------------------------------------------------------#
#    　ファイル切り出し／変換　ファイル連結
#----------------------------------------------------------------------#
echo "++ File Conversion Start Time = `date +%y/%m/%d:%H:%M:%S`"    >> $LOG_FILE
#
#E10K BEF#set MDPORTF_IN  = "$WKDIR01/upw295p10.dat"
set CONV_IN  = "$WKDIR01/upw295p10.dat"
#E10K BEF#set I_RECFM     = "f"
set I_CODE      = "euc"
#E10K BEF#set MDPORTF_OUT = "$WKDIR01/upw295p10.sjs"
set CONV_OUT = "$WKDIR01/upw295p10.sjs"
#E10K BEF#set O_RECFM     = "f"
set O_CODE      = "sjis"
#E10K BEF#set FORMATF     = "$CTLDIR/upw295p.ctl"
set FORMATF     = "${CODE_CTLDIR}/upw295p.fmt"
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
cat $WKDIR01/upw295p10.sjs >> $WKDIR01/upw295p.sjs
#
echo "++ File Conversion  End  Time = `date +%y/%m/%d:%H:%M:%S`"    >> $LOG_FILE
#
#---------------------< FILE DELETE >-----------------------------------#
rm $WKDIR01/upw295p10.dat
#----------------------------------------------------------------------#
# 後処理部
#----------------------------------------------------------------------#
echo "$SHELLNAME:t : mdport : end   time = `date +%y/%m/%d:%H:%M:%S`" \
                                                                    >> $LOG_FILE
echo "$SHELLNAME:t : normal end"                                    >> $LOG_FILE
exit $NORMAL
#
