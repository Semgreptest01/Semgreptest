#!/bin/csh
#------------------------------------------------------------------------#
# Shell-id      : supbd2089
# Shell-名      : supbd2089.csh
# 機能          : 出版社ファイル作成処理
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
#------------------------------------------------------------------------#
#       bsort
#------------------------------------------------------------------------#
#E10K BEF#set BSORT_PARM=(-s -l2 -z14 -6.6asca -o)
set MFSORT_INREC="14"
set MFSORT_OUTREC="14"
set MFSORT_OPT=""
set MFSORT_KEY="(7,6,ch,a)"
set MFSORT_IN="$WKDIR/upt705w.dat"
set MFSORT_OUT="$WKDIR/upt705w2.dat"
#E10K BEF#set MFSORT_DIR="$BSORT_DIR01"
#
#E10K BEF#source $SUBSRCDIR/bsort.src
source $SUBSRCDIR/mfsort2.src
if ( $status != $NORMAL ) then
        exit $ABEND
endif
#
#------------------------------------------------------------------------#
#        出版社ファイル作成処理
#------------------------------------------------------------------------#

set PGM_NAME="$PGMDIR/UPBD005"
set PGM_PARM=
#
#E10K BEF#setenv UPT705I $WKDIR/upt705w2.dat,BSAM
setenv UPT705I $WKDIR/upt705w2.dat
#E10K BEF#setenv UPT707O $WKDIR/upt707w.dat,BSAM
setenv UPT707O $WKDIR/upt707w.dat
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
