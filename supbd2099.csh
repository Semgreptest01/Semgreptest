#!/bin/csh
#------------------------------------------------------------------------#
# Shell-id      : supbd2099
# Shell-名      : supbd2099.csh
# 機能          : 即売ファイル(取引_商品)作成処理
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
#E10K BEF#set BSORT_PARM=(-s -l2 -z101 -23.6asca,3.6asca -o)
set MFSORT_INREC="101"
set MFSORT_OUTREC="101"
set MFSORT_OPT=""
set MFSORT_KEY="(24,6,ch,a,4,6,ch,a)"
set MFSORT_IN="$WKDIR/ueu721p.dat"
set MFSORT_OUT="$WKDIR/upt711w.dat"
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

set PGM_NAME="$PGMDIR/UPBD007"
set PGM_PARM=
#
#E10K BEF#setenv UPT711I $WKDIR/upt711w.dat,BSAM
setenv UPT711I $WKDIR/upt711w.dat
#E10K BEF#setenv UPT713O $WKDIR/upt713w.dat,BSAM
setenv UPT713O $WKDIR/upt713w.dat
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

