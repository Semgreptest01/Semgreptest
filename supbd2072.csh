#!/bin/csh
#----------------------------------------------------------------------#
# ｓｈｅｌｌ−ｉｄ: supbd2072
# ｓｈｅｌｌ名    : supbd2072.csh
# 機能            : 新聞競馬新聞・県サマリーデータコード変換・ファイル連結
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
#---------------------------<1/10>--------------------------------------#
# <COUNTCHK PARM SET>
#
set MB_FILE  = $WKDIR03/upw295p21.mbx
set HDR_FILE = $WKDIR02/upw295p21.chk
set DAT_FILE = $WKDIR01/upw295p21.dat
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
#E10K BEF#set MDPORTF_IN  = "$WKDIR01/upw295p21.dat"
set CONV_IN  = "$WKDIR01/upw295p21.dat"
#E10K BEF#set I_RECFM     = "f"
set I_CODE      = "euc"
#E10K BEF#set MDPORTF_OUT = "$WKDIR01/upw295p21.sjs"
set CONV_OUT = "$WKDIR01/upw295p21.sjs"
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
cat $WKDIR01/upw295p21.sjs > $WKDIR01/upw295p20.sjs
#
echo "++ File Conversion  End  Time = `date +%y/%m/%d:%H:%M:%S`"    >> $LOG_FILE
#
#---------------------< FILE DELETE >-----------------------------------#
rm $WKDIR01/upw295p21.dat
#---------------------------<2/10>--------------------------------------#
#
# <COUNTCHK PARM SET>
#
set MB_FILE  = $WKDIR03/upw295p22.mbx
set HDR_FILE = $WKDIR02/upw295p22.chk
set DAT_FILE = $WKDIR01/upw295p22.dat
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
#E10K BEF#set MDPORTF_IN  = "$WKDIR01/upw295p22.dat"
set CONV_IN  = "$WKDIR01/upw295p22.dat"
#E10K BEF#set I_RECFM     = "f"
set I_CODE      = "euc"
#E10K BEF#set MDPORTF_OUT = "$WKDIR01/upw295p22.sjs"
set CONV_OUT = "$WKDIR01/upw295p22.sjs"
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
cat $WKDIR01/upw295p22.sjs >> $WKDIR01/upw295p20.sjs
#
echo "++ File Conversion  End  Time = `date +%y/%m/%d:%H:%M:%S`"    >> $LOG_FILE
#
#---------------------< FILE DELETE >-----------------------------------#
rm $WKDIR01/upw295p22.dat
#---------------------------<3/10>--------------------------------------#
#
# <COUNTCHK PARM SET>
#
set MB_FILE  = $WKDIR03/upw295p23.mbx
set HDR_FILE = $WKDIR02/upw295p23.chk
set DAT_FILE = $WKDIR01/upw295p23.dat
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
#E10K BEF#set MDPORTF_IN  = "$WKDIR01/upw295p23.dat"
set CONV_IN  = "$WKDIR01/upw295p23.dat"
#E10K BEF#set I_RECFM     = "f"
set I_CODE      = "euc"
#E10K BEF#set MDPORTF_OUT = "$WKDIR01/upw295p23.sjs"
set CONV_OUT = "$WKDIR01/upw295p23.sjs"
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
cat $WKDIR01/upw295p23.sjs >> $WKDIR01/upw295p20.sjs
#
echo "++ File Conversion  End  Time = `date +%y/%m/%d:%H:%M:%S`"    >> $LOG_FILE
#
#---------------------< FILE DELETE >-----------------------------------#
rm $WKDIR01/upw295p23.dat
#---------------------------<4/10>--------------------------------------#
#
# <COUNTCHK PARM SET>
#
set MB_FILE  = $WKDIR03/upw295p24.mbx
set HDR_FILE = $WKDIR02/upw295p24.chk
set DAT_FILE = $WKDIR01/upw295p24.dat
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
#E10K BEF#set MDPORTF_IN  = "$WKDIR01/upw295p24.dat"
set CONV_IN  = "$WKDIR01/upw295p24.dat"
#E10K BEF#set I_RECFM     = "f"
set I_CODE      = "euc"
#E10K BEF#set MDPORTF_OUT = "$WKDIR01/upw295p24.sjs"
set CONV_OUT = "$WKDIR01/upw295p24.sjs"
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
cat $WKDIR01/upw295p24.sjs >> $WKDIR01/upw295p20.sjs
#
echo "++ File Conversion  End  Time = `date +%y/%m/%d:%H:%M:%S`"    >> $LOG_FILE
#
#---------------------< FILE DELETE >-----------------------------------#
rm $WKDIR01/upw295p24.dat
#---------------------------<5/10>--------------------------------------#
#
# <COUNTCHK PARM SET>
#
set MB_FILE  = $WKDIR03/upw295p25.mbx
set HDR_FILE = $WKDIR02/upw295p25.chk
set DAT_FILE = $WKDIR01/upw295p25.dat
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
#E10K BEF#set MDPORTF_IN  = "$WKDIR01/upw295p25.dat"
set CONV_IN  = "$WKDIR01/upw295p25.dat"
#E10K BEF#set I_RECFM     = "f"
set I_CODE      = "euc"
#E10K BEF#set MDPORTF_OUT = "$WKDIR01/upw295p25.sjs"
set CONV_OUT = "$WKDIR01/upw295p25.sjs"
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
cat $WKDIR01/upw295p25.sjs >> $WKDIR01/upw295p20.sjs
#
echo "++ File Conversion  End  Time = `date +%y/%m/%d:%H:%M:%S`"    >> $LOG_FILE
#
#---------------------< FILE DELETE >-----------------------------------#
rm $WKDIR01/upw295p25.dat
#---------------------------<6/10>--------------------------------------#
#
# <COUNTCHK PARM SET>
#
set MB_FILE  = $WKDIR03/upw295p26.mbx
set HDR_FILE = $WKDIR02/upw295p26.chk
set DAT_FILE = $WKDIR01/upw295p26.dat
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
#E10K BEF#set MDPORTF_IN  = "$WKDIR01/upw295p26.dat"
set CONV_IN  = "$WKDIR01/upw295p26.dat"
#E10K BEF#set I_RECFM     = "f"
set I_CODE      = "euc"
#E10K BEF#set MDPORTF_OUT = "$WKDIR01/upw295p26.sjs"
set CONV_OUT = "$WKDIR01/upw295p26.sjs"
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
cat $WKDIR01/upw295p26.sjs >> $WKDIR01/upw295p20.sjs
#
echo "++ File Conversion  End  Time = `date +%y/%m/%d:%H:%M:%S`"    >> $LOG_FILE
#
#---------------------< FILE DELETE >-----------------------------------#
rm $WKDIR01/upw295p26.dat
#---------------------------<7/10>--------------------------------------#
#
# <COUNTCHK PARM SET>
#
set MB_FILE  = $WKDIR03/upw295p27.mbx
set HDR_FILE = $WKDIR02/upw295p27.chk
set DAT_FILE = $WKDIR01/upw295p27.dat
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
#E10K BEF#set MDPORTF_IN  = "$WKDIR01/upw295p27.dat"
set CONV_IN  = "$WKDIR01/upw295p27.dat"
#E10K BEF#set I_RECFM     = "f"
set I_CODE      = "euc"
#E10K BEF#set MDPORTF_OUT = "$WKDIR01/upw295p27.sjs"
set CONV_OUT = "$WKDIR01/upw295p27.sjs"
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
cat $WKDIR01/upw295p27.sjs >> $WKDIR01/upw295p20.sjs
#
echo "++ File Conversion  End  Time = `date +%y/%m/%d:%H:%M:%S`"    >> $LOG_FILE
#
#---------------------< FILE DELETE >-----------------------------------#
rm $WKDIR01/upw295p27.dat
#---------------------------<8/10>--------------------------------------#
#
# <COUNTCHK PARM SET>
#
set MB_FILE  = $WKDIR03/upw295p28.mbx
set HDR_FILE = $WKDIR02/upw295p28.chk
set DAT_FILE = $WKDIR01/upw295p28.dat
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
#E10K BEF#set MDPORTF_IN  = "$WKDIR01/upw295p28.dat"
set CONV_IN  = "$WKDIR01/upw295p28.dat"
#E10K BEF#set I_RECFM     = "f"
set I_CODE      = "euc"
#E10K BEF#set MDPORTF_OUT = "$WKDIR01/upw295p28.sjs"
set CONV_OUT = "$WKDIR01/upw295p28.sjs"
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
cat $WKDIR01/upw295p28.sjs >> $WKDIR01/upw295p20.sjs
#
echo "++ File Conversion  End  Time = `date +%y/%m/%d:%H:%M:%S`"    >> $LOG_FILE
#
#---------------------< FILE DELETE >-----------------------------------#
rm $WKDIR01/upw295p28.dat
#---------------------------<9/10>--------------------------------------#
#
# <COUNTCHK PARM SET>
#
set MB_FILE  = $WKDIR03/upw295p29.mbx
set HDR_FILE = $WKDIR02/upw295p29.chk
set DAT_FILE = $WKDIR01/upw295p29.dat
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
#E10K BEF#set MDPORTF_IN  = "$WKDIR01/upw295p29.dat"
set CONV_IN  = "$WKDIR01/upw295p29.dat"
#E10K BEF#set I_RECFM     = "f"
set I_CODE      = "euc"
#E10K BEF#set MDPORTF_OUT = "$WKDIR01/upw295p29.sjs"
set CONV_OUT = "$WKDIR01/upw295p29.sjs"
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
cat $WKDIR01/upw295p29.sjs >> $WKDIR01/upw295p20.sjs
#
echo "++ File Conversion  End  Time = `date +%y/%m/%d:%H:%M:%S`"    >> $LOG_FILE
#
#---------------------< FILE DELETE >-----------------------------------#
rm $WKDIR01/upw295p29.dat
#---------------------------<10/10>--------------------------------------#
#
# <COUNTCHK PARM SET>
#
set MB_FILE  = $WKDIR03/upw295p30.mbx
set HDR_FILE = $WKDIR02/upw295p30.chk
set DAT_FILE = $WKDIR01/upw295p30.dat
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
#E10K BEF#set MDPORTF_IN  = "$WKDIR01/upw295p30.dat"
set CONV_IN  = "$WKDIR01/upw295p30.dat"
#E10K BEF#set I_RECFM     = "f"
set I_CODE      = "euc"
#E10K BEF#set MDPORTF_OUT = "$WKDIR01/upw295p30.sjs"
set CONV_OUT = "$WKDIR01/upw295p30.sjs"
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
cat $WKDIR01/upw295p30.sjs >> $WKDIR01/upw295p20.sjs
#
echo "++ File Conversion  End  Time = `date +%y/%m/%d:%H:%M:%S`"    >> $LOG_FILE
#
#---------------------< FILE DELETE >-----------------------------------#
rm $WKDIR01/upw295p30.dat
#----------------------------------------------------------------------#
# 後処理部
#----------------------------------------------------------------------#
echo "$SHELLNAME:t : mdport : end   time = `date +%y/%m/%d:%H:%M:%S`" \
                                                                    >> $LOG_FILE
echo "$SHELLNAME:t : normal end"                                    >> $LOG_FILE
exit $NORMAL
#
