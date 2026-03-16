#!/bin/csh
#------------------------------------------------------------------------#
# Shell-id      : supbd2093
# Shell-名      : supbd2093.csh
# 機能          : 取次ファイル作成処理
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
set MB_FILE="$WKDIR/ueu723p.mbx"
set HDR_FILE="$WKDIR/ueu723p.chk"
set DAT_FILE="$WKDIR/ueu723p.dat"
#
#----------------------------------------------------------------------#
# < CountChk :ueu723p>
#----------------------------------------------------------------------#
source $SUBSRCDIR/countchk0.src
if ( $status != $NORMAL )   then
        exit $ABEND
endif
#------------------------------------------------------------------------#
#       bsort
#------------------------------------------------------------------------#
#E10K BEF#set BSORT_PARM=(-s -l2 -z51 -8.6asca,14.6asca -o)
set MFSORT_INREC="51"
set MFSORT_OUTREC="51"
set MFSORT_OPT=""
set MFSORT_KEY="(9,6,ch,a,15,6,ch,a)"
set MFSORT_IN="$WKDIR/ueu723p.dat"
set MFSORT_OUT="$WKDIR/ueu723w.dat"
#E10K BEF#set MFSORT_DIR="$BSORT_DIR01"
#
#E10K BEF#source $SUBSRCDIR/bsort.src
source $SUBSRCDIR/mfsort2.src
if ( $status != $NORMAL ) then
        exit $ABEND
endif

#------------------------------------------------------------------------#
#       UPBD003  取次ファイル作成処理
#------------------------------------------------------------------------#

set PGM_NAME="$PGMDIR/UPBD003"
set PGM_PARM=
#
#E10K BEF#setenv UEU723I $WKDIR/ueu723w.dat,BSAM
setenv UEU723I $WKDIR/ueu723w.dat
#E10K BEF#setenv UPT703O $WKDIR/upt703w.dat,BSAM
setenv UPT703O $WKDIR/upt703w.dat
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
