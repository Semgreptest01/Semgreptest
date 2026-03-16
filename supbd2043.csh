#!/bin/csh
#------------------------------------------------------------------------#
# Shell-id      : supbd2043
# Shell-Ì¾      : supbd2043.csh
# µ¡Ç½          : WEBÏ¢·ÈKEYŽÃŽÞŽ°ŽÀ(È¯ÇäÆü£±:¿·Ê¹ÍÑ)¥Æ¡¼¥Ö¥ë£Ì£Ï£Á£Ä
# ºîÀ®¼Ô        : T.HARUYAMA
# ºîÀ®Æü        : 2004/01/19
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
#E10K BEF#set CTL_FILE="/prod/ora8/ctl/upm150.ctl"
set CTL_FILE="${CTLDIR}/upm150.ctl"
set DAT_FILE=$WKDIR01/upw288p2.sjs
#------------------------------------------------------------------------#
#    ¥á¥¤¥óÉô
#------------------------------------------------------------------------#
#------------------------------------------------------------------------#
#    £Ä£ò£ï£ð  £É£î£ä£å£ø
#------------------------------------------------------------------------#
#E10K BEF#set SQL_PARM=(-s $ORAUID/$ORAPWD @/prod/ora8/upm150_pk_drp_con_$ORAUID.sql)
set SQL_PARM=(-s $ORAUID/$ORAPWD @${SQLDIR}/upm150_pk_drp_con_$ORAUID.sql)
source $SUBSRCDIR/execsql.src
if ( $status != $NORMAL ) then
        exit $ABEND
endif
#------------------------------------------------------------------------#
#    £Ô£á£â£ì£å  £Ô£ò£õ£î£ã£á£ô£å
#------------------------------------------------------------------------#
#E10K BEF#set SQL_PARM=(-s $ORAUID/$ORAPWD @/prod/ora8/upm150_trc_tbl_$ORAUID.sql)
set SQL_PARM=(-s $ORAUID/$ORAPWD @${SQLDIR}/upm150_trc_tbl_$ORAUID.sql)
source $SUBSRCDIR/execsql.src
if ( $status != $NORMAL ) then
        exit $ABEND
endif
#------------------------------------------------------------------------#
#    £Ó£ñ£ì £Ì£ï£á£ä
#------------------------------------------------------------------------#
#setenv NLS_DATE_FORMAT YYYYMMDD
#
#E10K BEF#source $SUBSRCDIR/sqlload.src
source $SUBSRCDIR/sqlload2.src
if ( $status != $NORMAL ) then
        exit $ABEND
endif
#
#------------------------------------------------------------------------#
#    £É£î£ä£å£ø £Ã£ò£å£á£ô£å
#------------------------------------------------------------------------#
#E10K BEF#set SQL_PARM=(-s $ORAUID/$ORAPWD @/prod/ora8/upm150_pk_add_con_$ORAUID.sql)
set SQL_PARM=(-s $ORAUID/$ORAPWD @${SQLDIR}/upm150_pk_add_con_$ORAUID.sql)
source $SUBSRCDIR/execsql.src
if ( $status != $NORMAL ) then
        exit $ABEND
endif
#------------------------------------------------------------------------#
#    Analyze
#------------------------------------------------------------------------#
#E10K BEF#set SQL_PARM=(-s $ORAUID/$ORAPWD @/prod/ora8/upm150_alz_tbl_$ORAUID.sql)
set SQL_PARM=(-s $ORAUID/$ORAPWD @${SQLDIR}/upm150_alz_tbl_$ORAUID.sql)
source $SUBSRCDIR/execsql.src
if ( $status != $NORMAL ) then
        exit $ABEND
endif
#------------------------------------------------------------------------#
#       ¸å½èÍýÉô
#------------------------------------------------------------------------#
echo "$SHELLNAME:t :  end  time : `date +%y/%m/%d:%H:%M:%S`" >> $LOG_FILE
exit $NORMAL
