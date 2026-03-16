#!/bin/csh
#------------------------------------------------------------------------#
# Shell-id      : supbd2083
# Shell-名      : supbd2083.csh
# 機能          : 即売ファイル作成処理
# 作成者        : T.HARUYAMA
# 作成日        : 2003/12/11
# 修正履歴      :
# Ｎｏ   修正日付   修正者     修正内容
#    x  2006/05/12  M.Tanaka    Ｅ１０Ｋ移行ガイドライン対応
#------------------------------------------------------------------------#
#------------------------------------------------------------------------#
#      初期処理部
#------------------------------------------------------------------------#
#E10K BEF#set SUBSRCDIR="/prod/jcl/sub"
set PROD_DIR = /prod
set SUBSRCDIR="${PROD_DIR}/jcl/sub"
#
source $SUBSRCDIR/upbd.src
source $SUBSRCDIR/common.src
#
set MB_FILE="$WKDIR/ueu721p.mbx"
set HDR_FILE="$WKDIR/ueu721p.chk"
set DAT_FILE="$WKDIR/ueu721p.dat"
#
#----------------------------------------------------------------------#
# < CountChk :ueu721p>
#----------------------------------------------------------------------#
source $SUBSRCDIR/countchk0.src
if ( $status != $NORMAL )   then
        exit $ABEND
endif
#------------------------------------------------------------------------#
#       bsort
#------------------------------------------------------------------------#
#E10K BEF#set BSORT_PARM=(-s -l2 -z101 -23.6asca,74.6asca -o)
set MFSORT_INREC="101"
set MFSORT_OUTREC="101"
set MFSORT_OPT=""
set MFSORT_KEY="(24,6,ch,a,75,6,ch,a)"
set MFSORT_IN="$WKDIR/ueu721p.dat"
set MFSORT_OUT="$WKDIR/ueu721w.dat"
#E10K BEF#set MFSORT_DIR="$BSORT_DIR01"
#
#E10K BEF#source $SUBSRCDIR/bsort.src
source $SUBSRCDIR/mfsort2.src
if ( $status != $NORMAL ) then
        exit $ABEND
endif

#------------------------------------------------------------------------#
#      即売ファイル作成処理
#------------------------------------------------------------------------#

set PGM_NAME="$PGMDIR/UPBD001"
set PGM_PARM=
#
#E10K BEF#setenv UEU721I $WKDIR/ueu721w.dat,BSAM
setenv UEU721I $WKDIR/ueu721w.dat
#E10K BEF#setenv UPT701O $WKDIR/upt701w.dat,BSAM
setenv UPT701O $WKDIR/upt701w.dat
#
source $SUBSRCDIR/program.src
if ( $status != $NORMAL ) then
        exit $ABEND
endif
#------------------------------------------------------------------------#
#       後処理部
#------------------------------------------------------------------------#
echo "$SHELLNAME:t :  end  time : `date +%y/%m/%d:%H:%M:%S`" >> $LOG_FILE
exit $NORMAL

