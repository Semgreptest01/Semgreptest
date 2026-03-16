#!/bin/csh
#------------------------------------------------------------------------#
# Shell-id      : supbd2103
# Shell-Ì¾      : supbd2103.csh
# µ¡Ç½          : Â¨Çä_¼è¼¡(¼è¼¡_¾¦ÉÊ)ºîÀ®½èÍý
# ºîÀ®¼Ô        : T.HARUYAMA
# ºîÀ®Æü        : 2004/03/04
# ½¤ÀµÍúÎò      :
# £Î£ï   ½¤ÀµÆüÉÕ   ½¤Àµ¼Ô     ½¤ÀµÆâÍÆ
#    x  2006/05/12  M.Tanaka    £Å£±£°£Ë°Ü¹Ô¥¬¥¤¥É¥é¥¤¥óÂÐ±þ
#------------------------------------------------------------------------#
#------------------------------------------------------------------------#
#      ½é´ü½èÍýÉô
#------------------------------------------------------------------------#
#E10K BEF#set SUBSRCDIR="/prod/jcl/sub"
set PROD_DIR = /prod
set SUBSRCDIR="${PROD_DIR}/jcl/sub"
#
source $SUBSRCDIR/upbd.src
source $SUBSRCDIR/common.src
#
set SQL_NAME=sqlplus
#E10K BEF#set CTL_FILE="/prod/ora8/ctl/upt130.ctl"
set CTL_FILE="${CTLDIR}/upt130.ctl"
set DAT_FILE=$WKDIR01/upt721w.dat
#------------------------------------------------------------------------#
#       bsort
#------------------------------------------------------------------------#
#E10K BEF#set BSORT_PARM=(-s -l2 -z13 -0.6asca,5.6asca -o)
set MFSORT_INREC="13"
set MFSORT_OUTREC="13"
set MFSORT_OPT=""
set MFSORT_KEY="(1,6,ch,a,6,6,ch,a)"
set MFSORT_IN="$WKDIR/upt713w.dat $WKDIR/upt717w.dat"
set MFSORT_OUT="$WKDIR/upt719w.dat"
#E10K BEF#set MFSORT_DIR="$BSORT_DIR01"
#
#E10K BEF#source $SUBSRCDIR/bsort.src
source $SUBSRCDIR/mfsort2.src
if ( $status != $NORMAL ) then
        exit $ABEND
endif
#------------------------------------------------------------------------#
#    £Ð£ò£é£í£á£ò£ù £Ë£å£ù £Ä£ò£ï£ð
#------------------------------------------------------------------------#
#E10K BEF#set SQL_PARM=(-s $ORAUID/$ORAPWD @/prod/ora8/upt130_pk_drp_con_$ORAUID.sql)
set SQL_PARM=(-s $ORAUID/$ORAPWD @${SQLDIR}/upt130_pk_drp_con_$ORAUID.sql)
source $SUBSRCDIR/execsql.src
if ( $status != $NORMAL ) then
        exit $ABEND
endif
#------------------------------------------------------------------------#
#    £Ô£á£â£ì£å  £Ô£ò£õ£î£ã£á£ô£å
#------------------------------------------------------------------------#
#E10K BEF#set SQL_PARM=(-s $ORAUID/$ORAPWD @/prod/ora8/upt130_trc_tbl_$ORAUID.sql)
set SQL_PARM=(-s $ORAUID/$ORAPWD @${SQLDIR}/upt130_trc_tbl_$ORAUID.sql)
source $SUBSRCDIR/execsql.src
if ( $status != $NORMAL ) then
        exit $ABEND
endif
#----------------------------------------------------------------------#
#    ¡¡¥Õ¥¡¥¤¥ëÀÚ¤ê½Ð¤·¡¿ÊÑ´¹
#----------------------------------------------------------------------#
echo "++ File Conversion Start Time = `date +%y/%m/%d:%H:%M:%S`"    >> $LOG_FILE
#
#E10K BEF#set MDPORTF_IN  = "$WKDIR/upt719w.dat"
set CONV_IN  = "$WKDIR/upt719w.dat"
#E10K BEF#set I_RECFM     = "f"
set I_CODE      = "euc"
#E10K BEF#set MDPORTF_OUT = "$WKDIR/upt721w.dat"
set CONV_OUT = "$WKDIR/upt721w.dat"
#E10K BEF#set O_RECFM     = "f"
set O_CODE      = "sjis"
#E10K BEF#set FORMATF     = "$CTLDIR/upt721p.ctl"
set FORMATF     = "${CODE_CTLDIR}/upt721p.fmt"
set I_RECDLM    = lf
set O_RECDLM    = lf
#------------------------------------------------------------------------#
#      Ê¸»ú¥³¡¼¥ÉÊÑ´¹
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
#------------------------------------------------------------------------#
#    £Ó£ñ£ì £Ì£ï£á£ä
#------------------------------------------------------------------------#
#E10K BEF#source $SUBSRCDIR/sqlload.src
source $SUBSRCDIR/sqlload2.src
if ( $status != $NORMAL ) then
        exit $ABEND
endif
#------------------------------------------------------------------------#
#    £Ð£ò£é£í£á£ò£ù £Ë£å£ù £Ä£ò£ï£ð
#------------------------------------------------------------------------#
#E10K BEF#set SQL_PARM=(-s $ORAUID/$ORAPWD @/prod/ora8/upt130_pk_add_con_$ORAUID.sql)
set SQL_PARM=(-s $ORAUID/$ORAPWD @${SQLDIR}/upt130_pk_add_con_$ORAUID.sql)
source $SUBSRCDIR/execsql.src
if ( $status != $NORMAL ) then
        exit $ABEND
endif
#------------------------------------------------------------------------#
#    Analyze
#------------------------------------------------------------------------#
#E10K BEF#set SQL_PARM=(-s $ORAUID/$ORAPWD @/prod/ora8/upt130_alz_tbl_$ORAUID.sql)
set SQL_PARM=(-s $ORAUID/$ORAPWD @${SQLDIR}/upt130_alz_tbl_$ORAUID.sql)
source $SUBSRCDIR/execsql.src
if ( $status != $NORMAL ) then
        exit $ABEND
endif
#------------------------------------------------------------------------#
#       ¸å½èÍýÉô
#------------------------------------------------------------------------#
echo "$SHELLNAME:t :  end  time : `date +%y/%m/%d:%H:%M:%S`" >> $LOG_FILE
exit $NORMAL

