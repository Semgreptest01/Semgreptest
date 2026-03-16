#!/bin/csh
#------------------------------------------------------------------------#
# Shell-id      : supbd2101
# Shell-名      : supbd2101.csh
# 機能          : 取次ファイル(取引_銘柄)作成処理
# 作成者        : T.HARUYAMA
# 作成日        : 2004/03/05
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
#------------------------------------------------------------------------#
#       bsort
#------------------------------------------------------------------------#
#E10K BEF#set BSORT_PARM=(-s -l2 -z51 -8.6asca,2.6asca -o)
set MFSORT_INREC="51"
set MFSORT_OUTREC="51"
set MFSORT_OPT=""
set MFSORT_KEY="(9,6,ch,a,3,6,ch,a)"
set MFSORT_IN="$WKDIR/ueu723p.dat"
set MFSORT_OUT="$WKDIR/upt715w.dat"
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

set PGM_NAME="$PGMDIR/UPBD009"
set PGM_PARM=
#
#E10K BEF#setenv UPT715I $WKDIR/upt715w.dat,BSAM
setenv UPT715I $WKDIR/upt715w.dat
#E10K BEF#setenv UPT717O $WKDIR/upt717w.dat,BSAM
setenv UPT717O $WKDIR/upt717w.dat
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

