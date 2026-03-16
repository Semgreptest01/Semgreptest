#!/bin/csh
#------------------------------------------------------------------------#
# Shell-id      : supbd2087
# Shell-名      : supbd2087.csh
# 機能          : 即売_取次作成処理
# 作成者        : T.HARUYAMA
# 作成日        : 2004/01/07
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
#E10K BEF#set BSORT_PARM=(-s -l2 -z14 -0.6asca,5.6asca,11.1asca -o)
set MFSORT_INREC="14"
set MFSORT_OUTREC="14"
set MFSORT_OPT=""
set MFSORT_KEY="(1,6,ch,a,6,6,ch,a,12,1,ch,a)"
set MFSORT_IN="$WKDIR/upt701w.dat $WKDIR/upt703w.dat"
set MFSORT_OUT="$WKDIR/upt705w.dat"
#E10K BEF#set MFSORT_DIR="$BSORT_DIR01"
#
#E10K BEF#source $SUBSRCDIR/bsort.src
source $SUBSRCDIR/mfsort2.src
if ( $status != $NORMAL ) then
        exit $ABEND
endif
#------------------------------------------------------------------------#
#       後処理部
#------------------------------------------------------------------------#
echo "$SHELLNAME:t :  end  time : `date +%y/%m/%d:%H:%M:%S`" >> $LOG_FILE
exit $NORMAL

