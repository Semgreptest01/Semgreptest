#!/bin/csh
#------------------------------------------------------------------------#
# Shell-id      : supbd1033
# Shell-Ì¾      : supbd1033.csh
# µ¡Ç½          : UPM_£Ó£Õ£Â£Ã£Ì£Á£Ó£Ó¡¡¥Æ¡¼¥Ö¥ë£Ì£Ï£Á£Ä¡¡¡¡¡¡¡¡
# ºîÀ®¼Ô        : K.Nasu   
# ºîÀ®Æü        : 2001/03/01
# ½¤ÀµÍúÎò      :
# £Î£ï   ½¤ÀµÆüÉÕ   ½¤Àµ¼Ô     ½¤ÀµÆâÍÆ
#    1  2003/04/24  M.Saitou   EP#3ÍÑ¤ËÊÑ¹¹ (/prod/ora8 -> /prod/ora7)
#    2  2003/06/20  M.Saitou   /prod/jcl/msisub -> /prod/jcl/sub
#                              /prod/ora7 -> /prod/ora8
#    3  2003/06/24  M.Saitou   upw298p.euc -> upw298p.sjs
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
#E10K BEF#set CTL_FILE="/prod/ora8/ctl/upm030.ctl"
set CTL_FILE="${CTLDIR}/upm030.ctl"
set DAT_FILE=$WKDIR01/upw298p.sjs
#------------------------------------------------------------------------#
#    ¥á¥¤¥óÉô
#------------------------------------------------------------------------#
#------------------------------------------------------------------------#
#    £Ä£ò£ï£ð  £É£î£ä£å£ø
#------------------------------------------------------------------------#
#E10K BEF#set SQL_PARM=(-s $ORAUID/$ORAPWD @/prod/ora8/upm030_pk_drp_con_$ORAUID.sql)
set SQL_PARM=(-s $ORAUID/$ORAPWD @${SQLDIR}/upm030_pk_drp_con_$ORAUID.sql)
source $SUBSRCDIR/execsql.src
if ( $status != $NORMAL ) then
   	exit $ABEND
endif
#------------------------------------------------------------------------#
#    £Ô£á£â£ì£å  £Ô£ò£õ£î£ã£á£ô£å
#------------------------------------------------------------------------# 
#E10K BEF#set SQL_PARM=(-s $ORAUID/$ORAPWD @/prod/ora8/upm030_trc_tbl_$ORAUID.sql)
set SQL_PARM=(-s $ORAUID/$ORAPWD @${SQLDIR}/upm030_trc_tbl_$ORAUID.sql)
source $SUBSRCDIR/execsql.src
if ( $status != $NORMAL ) then
   	exit $ABEND
endif
#------------------------------------------------------------------------#
#    £Ó£ñ£ì £Ì£ï£á£ä
#------------------------------------------------------------------------#
#E10K BEF#source $SUBSRCDIR/sqlload.src
source $SUBSRCDIR/sqlload2.src
if ( $status != $NORMAL ) then
   	exit $ABEND
endif

#------------------------------------------------------------------------#
#    £É£î£ä£å£ø £Ã£ò£å£á£ô£å
#------------------------------------------------------------------------#  
#E10K BEF#set SQL_PARM=(-s $ORAUID/$ORAPWD @/prod/ora8/upm030_pk_add_con_$ORAUID.sql)
set SQL_PARM=(-s $ORAUID/$ORAPWD @${SQLDIR}/upm030_pk_add_con_$ORAUID.sql)
source $SUBSRCDIR/execsql.src
if ( $status != $NORMAL ) then
   	exit $ABEND
endif

#------------------------------------------------------------------------#
#    Analyze
#------------------------------------------------------------------------# 
#E10K BEF#set SQL_PARM=(-s $ORAUID/$ORAPWD @/prod/ora8/upm030_alz_tbl_$ORAUID.sql)
set SQL_PARM=(-s $ORAUID/$ORAPWD @${SQLDIR}/upm030_alz_tbl_$ORAUID.sql)
source $SUBSRCDIR/execsql.src
if ( $status != $NORMAL ) then
   	exit $ABEND
endif
#------------------------------------------------------------------------#
#       ¸å½èÍýÉô
#------------------------------------------------------------------------#
echo "$SHELLNAME:t :  end  time : `date +%y/%m/%d:%H:%M:%S`" >> $LOG_FILE
exit $NORMAL
