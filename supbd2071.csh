#!/bin/csh
#----------------------------------------------------------------------#
# ｓｈｅｌｌ−ｉｄ: supbd2071
# ｓｈｅｌｌ名    : supbd2071.csh
# 機能            : 新聞・県サマリーデータ   ＭＢ取込み
# 作成者          : T.HARUYAMA
# 作成日          : 2003/12/31
# 修正履歴        :
#    x  2006/05/12  M.Tanaka    Ｅ１０Ｋ移行ガイドライン対応
#       2010/07/08  R.Wakaki    MB-ID変更
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
#----------------------------------------------------------------------#
# メイン部
#----------------------------------------------------------------------#
#----------------------------------------------------------------------#
#       ＭＢ取り出し （１／１０）
#----------------------------------------------------------------------#
#E10K BEF#set F_FILE = MB.UPW295P.101                # 論理ファイル名
set F_FILE = ${FTPASSTYPE}.AIG014P.101                # 論理ファイル名
set D_FILE = $WKDIR03/upw295p21.mbx         # 取出し後ファイル
set E_ERASE = -e                           # 取出し後ファイル削除区分
#
source $SUBSRCDIR/ftpget.src
if ( $status != $NORMAL ) then
    echo "$SHELLNAME:t : abnormal end time : `date +%y/%m/%d:%H:%M:%S`" \ | tee -a $LOG_FILE
    exit $ABEND
endif
#
#----------------------------------------------------------------------#
#       ＭＢ取り出し （２／１０）
#----------------------------------------------------------------------#
#E10K BEF#set F_FILE = MB.UPW295P.102                # 論理ファイル名
set F_FILE = ${FTPASSTYPE}.AIG014P.102                # 論理ファイル名
set D_FILE = $WKDIR03/upw295p22.mbx        # 取出し後ファイル
set E_ERASE = -e                           # 取出し後ファイル削除区分
#
source $SUBSRCDIR/ftpget.src
if ( $status != $NORMAL ) then
    echo "$SHELLNAME:t : abnormal end time : `date +%y/%m/%d:%H:%M:%S`" \ | tee -a $LOG_FILE
    exit $ABEND
endif
#
#----------------------------------------------------------------------#
#       ＭＢ取り出し （３／１０）
#----------------------------------------------------------------------#
#E10K BEF#set F_FILE = MB.UPW295P.103                # 論理ファイル名
set F_FILE = ${FTPASSTYPE}.AIG014P.103                # 論理ファイル名
set D_FILE = $WKDIR03/upw295p23.mbx         # 取出し後ファイル
set E_ERASE = -e                           # 取出し後ファイル削除区分
#
source $SUBSRCDIR/ftpget.src
if ( $status != $NORMAL ) then
    echo "$SHELLNAME:t : abnormal end time : `date +%y/%m/%d:%H:%M:%S`" \ | tee -a $LOG_FILE
    exit $ABEND
endif
#
#----------------------------------------------------------------------#
#       ＭＢ取り出し （４／１０）
#----------------------------------------------------------------------#
#E10K BEF#set F_FILE = MB.UPW295P.104                # 論理ファイル名
set F_FILE = ${FTPASSTYPE}.AIG014P.104                # 論理ファイル名
set D_FILE = $WKDIR03/upw295p24.mbx        # 取出し後ファイル
set E_ERASE = -e                           # 取出し後ファイル削除区分
#
source $SUBSRCDIR/ftpget.src
if ( $status != $NORMAL ) then
    echo "$SHELLNAME:t : abnormal end time : `date +%y/%m/%d:%H:%M:%S`" \ | tee -a $LOG_FILE
    exit $ABEND
endif
#
#----------------------------------------------------------------------#
#       ＭＢ取り出し （５／１０）
#----------------------------------------------------------------------#
#E10K BEF#set F_FILE = MB.UPW295P.105                # 論理ファイル名
set F_FILE = ${FTPASSTYPE}.AIG014P.105                # 論理ファイル名
set D_FILE = $WKDIR03/upw295p25.mbx        # 取出し後ファイル
set E_ERASE = -e                           # 取出し後ファイル削除区分
#
source $SUBSRCDIR/ftpget.src
if ( $status != $NORMAL ) then
    echo "$SHELLNAME:t : abnormal end time : `date +%y/%m/%d:%H:%M:%S`" \ | tee -a $LOG_FILE
    exit $ABEND
endif
#
#----------------------------------------------------------------------#
#       ＭＢ取り出し （６／１０）
#----------------------------------------------------------------------#
#E10K BEF#set F_FILE = MB.UPW295P.106                # 論理ファイル名
set F_FILE = ${FTPASSTYPE}.AIG014P.106                # 論理ファイル名
set D_FILE = $WKDIR03/upw295p26.mbx        # 取出し後ファイル
set E_ERASE = -e                           # 取出し後ファイル削除区分
#
source $SUBSRCDIR/ftpget.src
if ( $status != $NORMAL ) then
    echo "$SHELLNAME:t : abnormal end time : `date +%y/%m/%d:%H:%M:%S`" \ | tee -a $LOG_FILE
    exit $ABEND
endif
#
#----------------------------------------------------------------------#
#       ＭＢ取り出し （７／１０）
#----------------------------------------------------------------------#
#E10K BEF#set F_FILE = MB.UPW295P.107                # 論理ファイル名
set F_FILE = ${FTPASSTYPE}.AIG014P.107                # 論理ファイル名
set D_FILE = $WKDIR03/upw295p27.mbx         # 取出し後ファイル
set E_ERASE = -e                           # 取出し後ファイル削除区分
#
source $SUBSRCDIR/ftpget.src
if ( $status != $NORMAL ) then
    echo "$SHELLNAME:t : abnormal end time : `date +%y/%m/%d:%H:%M:%S`" \ | tee -a $LOG_FILE
    exit $ABEND
endif
#
#----------------------------------------------------------------------#
#       ＭＢ取り出し （８／１０）
#----------------------------------------------------------------------#
#E10K BEF#set F_FILE = MB.UPW295P.108                # 論理ファイル名
set F_FILE = ${FTPASSTYPE}.AIG014P.108                # 論理ファイル名
set D_FILE = $WKDIR03/upw295p28.mbx         # 取出し後ファイル
set E_ERASE = -e                           # 取出し後ファイル削除区分
#
source $SUBSRCDIR/ftpget.src
if ( $status != $NORMAL ) then
    echo "$SHELLNAME:t : abnormal end time : `date +%y/%m/%d:%H:%M:%S`" \ | tee -a $LOG_FILE
    exit $ABEND
endif
#
#----------------------------------------------------------------------#
#       ＭＢ取り出し （９／１０）
#----------------------------------------------------------------------#
#E10K BEF#set F_FILE = MB.UPW295P.109                # 論理ファイル名
set F_FILE = ${FTPASSTYPE}.AIG014P.109                # 論理ファイル名
set D_FILE = $WKDIR03/upw295p29.mbx        # 取出し後ファイル
set E_ERASE = -e                           # 取出し後ファイル削除区分
#
source $SUBSRCDIR/ftpget.src
if ( $status != $NORMAL ) then
    echo "$SHELLNAME:t : abnormal end time : `date +%y/%m/%d:%H:%M:%S`" \ | tee -a $LOG_FILE
    exit $ABEND
endif
#
#----------------------------------------------------------------------#
#       ＭＢ取り出し （１０／１０）
#----------------------------------------------------------------------#
#E10K BEF#set F_FILE = MB.UPW295P.110                # 論理ファイル名
set F_FILE = ${FTPASSTYPE}.AIG014P.110                # 論理ファイル名
set D_FILE = $WKDIR03/upw295p30.mbx       # 取出し後ファイル
set E_ERASE = -e                           # 取出し後ファイル削除区分
#
source $SUBSRCDIR/ftpget.src
if ( $status != $NORMAL ) then
    echo "$SHELLNAME:t : abnormal end time : `date +%y/%m/%d:%H:%M:%S`" \ | tee -a $LOG_FILE
    exit $ABEND
endif
#
#----------------------------------------------------------------------#
# 後処理部
#----------------------------------------------------------------------#
exit $NORMAL
#
