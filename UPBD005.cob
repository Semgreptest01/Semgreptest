000010****************************************************************
000020*    システム名    ： 新聞競馬新聞実績
000030*    プログラム名  ： 出版社ファイル作成処理
000040*    プログラムＩＤ： UPBD005
000050*    作  成  者    ： T.HARUYAMA
000060*    作  成  日    ： 2003年12月 1日
000070*    修  正  日    ： 2004年01月29日 種別区分追加
000080****************************************************************
000090 IDENTIFICATION                    DIVISION.
000100 PROGRAM-ID.                       UPBD005.
000110 AUTHOR.                           T.HARUYAMA.
000120*
000130 ENVIRONMENT                       DIVISION.
000140 CONFIGURATION                     SECTION.
000150 SOURCE-COMPUTER.                  SUN.
000160 OBJECT-COMPUTER.                  SUN.
000170 SPECIAL-NAMES.
000180     ENVIRONMENT-NAME          IS  ORA-NAME
000190     ENVIRONMENT-VALUE         IS  ORA-VALUE
000200     CONSOLE                   IS  CONS
000210     ARGUMENT-NUMBER           IS  BANGO
000220     ARGUMENT-VALUE            IS  ATAI.
000230*----*---*---*---*---*---------*---*---*---*---*---*---*---*---*
000240 INPUT-OUTPUT                      SECTION.
000250 FILE-CONTROL.
000260*------------ファイル   （入力）-------------------------------*
000270     SELECT  UPT705I ASSIGN     TO  UPT705I
000280                     STATUS     IS  FILE-STATUS.
000290*------------ファイル   （出力）-------------------------------*
000300     SELECT  UPT707O ASSIGN
000310                                TO  UPT707O
000320             ACCESS  MODE       IS  SEQUENTIAL
000330             FILE    STATUS     FILE-STATUS.
000340*
000350 DATA                              DIVISION.
000360 FILE                              SECTION.
000370*
000380****************************************************************
000390*            ファイル   （入力）
000400****************************************************************
000410 FD  UPT705I        LABEL  RECORD STANDARD.
000420 01  UPT705I-REC.
000430     COPY   UPT705C.
000440*
000450****************************************************************
000460*            ファイル   （出力）
000470****************************************************************
000480 FD  UPT707O         LABEL  RECORD STANDARD.
000490 01  UPT707O-REC.
000500     COPY   UPT707C.
000510*
000520 WORKING-STORAGE                   SECTION.
000530*---------------< WORK-AREA定義 >-----------------------------*
000540 01  SW-AREA.
000550     03  END-SW                        PIC X(02) VALUE ZERO.
000560 01  WK-DATE.
000570     03  WK-Y                          PIC  9(2).
000580     03  WK-M                          PIC  9(2).
000590     03  WK-D                          PIC  9(2).
000600 01  WK-YYYYMMDD.
000610     03  WK-YYYY.
000620       05  WK-20                       PIC  9(2).
000630       05  WK-YY                       PIC  9(2).
000640     03  WK-MM                         PIC  9(2).
000650     03  WK-DD                         PIC  9(2).
000660     03  WK-TIME.
000670       05  WK-TIM                      PIC  9(2).
000680       05  WK-MIN                      PIC  9(2).
000690       05  WK-SEC                      PIC  9(2).
000700*---------------< キーの定義 >--------------------------------*
000710 01  KEY-AREA.
000720     03  NEW-KEY.
000730       05  NEW-KEY-SYUPPAN             PIC 9(06) VALUE ZERO.
000740     03  OLD-KEY.
000750       05  OLD-KEY-SYUPPAN             PIC 9(06) VALUE ZERO.
000760*
000770*---------------< ステータスの定義 >--------------------------*
000780 01  STATUS-AREA.
000790     03  FILE-STATUS                   PIC X(02) VALUE ZERO.
000800*---------------< カウントの定義 >----------------------------*
000810 01  UPT705-CNT-AREA.
000820     03  CNT-IN                        PIC 9(09) VALUE ZERO.
000830     03  CNT-OUT                       PIC 9(09) VALUE ZERO.
000840*
000850*---------------< メッセージ１の定義 >------------------------*
000860 01  MSG-PGID                          PIC X(08) VALUE
000870     "UPBD005".
000880 01  MESSAGE-AREA1.
000890     03  MESSAGE-START.
000900       05  FILLER                      PIC X(12) VALUE
000910     "++ UPBD005 ".
000920       05  MESSAGE-START-YMD.
000930         07  MESSAGE-START-YYYY        PIC 9(04).
000940         07  FILLER                    PIC X(01) VALUE "/".
000950         07  MESSAGE-START-MM          PIC 9(02).
000960         07  FILLER                    PIC X(01) VALUE "/".
000970         07  MESSAGE-START-DD          PIC 9(02).
000980         07  FILLER                    PIC X(01) VALUE " ".
000990         07  MESSAGE-START-TIM         PIC 9(02).
001000         07  FILLER                    PIC X(01) VALUE ":".
001010         07  MESSAGE-START-MIN         PIC 9(02).
001020         07  FILLER                    PIC X(01) VALUE ":".
001030         07  MESSAGE-START-SEC         PIC 9(02).
001040         07  FILLER                    PIC X(01) VALUE " ".
001050       05  FILLER                      PIC X(08) VALUE
001060     "*START* ".
001070     03  MESSAGE-END.
001080       05  FILLER                      PIC X(12) VALUE
001090     "++ UPBD005 ".
001100       05  MESSAGE-END-YMD.
001110         07  MESSAGE-END-YYYY          PIC 9(04).
001120         07  FILLER                    PIC X(01) VALUE "/".
001130         07  MESSAGE-END-MM            PIC 9(02).
001140         07  FILLER                    PIC X(01) VALUE "/".
001150         07  MESSAGE-END-DD            PIC 9(02).
001160         07  FILLER                    PIC X(01) VALUE " ".
001170         07  MESSAGE-END-TIM           PIC 9(02).
001180         07  FILLER                    PIC X(01) VALUE ":".
001190         07  MESSAGE-END-MIN           PIC 9(02).
001200         07  FILLER                    PIC X(01) VALUE ":".
001210         07  MESSAGE-END-SEC           PIC 9(02).
001220         07  FILLER                    PIC X(01) VALUE " ".
001230       05  FILLER                      PIC X(13) VALUE
001240     "*NORMAL END* ".
001250     03  MESSAGE-ABEND.
001260       05  FILLER                      PIC X(12) VALUE
001270     "++ UPBD005 ".
001280       05  MESSAGE-ABEND-YMD.
001290         07  MESSAGE-ABEND-YYYY        PIC 9(04).
001300         07  FILLER                    PIC X(01) VALUE "/".
001310         07  MESSAGE-ABEND-MM          PIC 9(02).
001320         07  FILLER                    PIC X(01) VALUE "/".
001330         07  MESSAGE-ABEND-DD          PIC 9(02).
001340         07  FILLER                    PIC X(01) VALUE " ".
001350         07  MESSAGE-ABEND-TIM         PIC 9(02).
001360         07  FILLER                    PIC X(01) VALUE ":".
001370         07  MESSAGE-ABEND-MIN         PIC 9(02).
001380         07  FILLER                    PIC X(01) VALUE ":".
001390         07  MESSAGE-ABEND-SEC         PIC 9(02).
001400         07  FILLER                    PIC X(01) VALUE " ".
001410       05  FILLER                      PIC X(15) VALUE
001420     "*ABNORMAL END* ".
001430     03  MESSAGE-0.
001440       05  MSG0-PGID                   PIC X(08).
001450       05  MSG0-NAME                   PIC X(41).
001460*
001470*---< メッセージ２の定義 >---*
001480 01  MESSAGE-AREA2.
001490     03  MSG-501I.
001500       05  FILLER                      PIC  X(41) VALUE
001510     "++ UPBD005  501I UPT705       IN COUNT = ".
001520     03  MSG-701I.
001530       05  FILLER                      PIC  X(41) VALUE
001540     "++ UPBD005  701I UPT707      OUT COUNT = ".
001550     03  MSG-501F.
001560       05  FILLER                      PIC  X(41) VALUE
001570     "++ UPBD005  501F UPT705  OPEN  ERROR ST= ".
001580       05  MSG-501F-ST                 PIC  9(03).
001590       05  FILLER                      PIC  X(01) VALUE " ".
001600     03  MSG-503F.
001610       05  FILLER                      PIC  X(41) VALUE
001620     "++ UPBD005  503F UPT705  READ  ERROR ST= ".
001630       05  MSG-503F-ST                 PIC  9(03).
001640       05  FILLER                      PIC  X(01) VALUE " ".
001650     03  MSG-701F.
001660       05  FILLER                      PIC X(41)     VALUE
001670     "++ UPBD005  701F OUTPUT  OPEN  ERROR ST= ".
001680       05  MSG-701F-ST                 PIC  9(03).
001690     03  MSG-703F.
001700       05  FILLER                      PIC  X(41) VALUE
001710     "++ UPBD005  703F OUTPUT  WRITE ERROR ST= ".
001720       05  MSG-703F-ST                 PIC  9(03).
001730*
001740 PROCEDURE                         DIVISION.
001750****************************************************************
001760*    (0.0)   メイン              処理                          *
001770****************************************************************
001780 MAIN-RTN                          SECTION.
001790*
001800     PERFORM      INIT-RTN.
001810*
001820     PERFORM      PROC-RTN
001830         UNTIL    END-SW           =   HIGH-VALUE.
001840*
001850     PERFORM      END-RTN.
001860*
001870     STOP         RUN.
001880*
001890 MAIN-EXT.
001900     EXIT.
001910****************************************************************
001920*    (1.0)   イニシャル          処理                          *
001930****************************************************************
001940 INIT-RTN                          SECTION.
001950     PERFORM      START-MSG-RTN.
001960*
001970     MOVE    99                    TO  RETURN-CODE.
001980*
001990*-----------< ファイル  （入力）ＯＰＥＮ  >--------------------*
002000     OPEN INPUT UPT705I.
002010     IF  FILE-STATUS               =   ZERO
002020         CONTINUE
002030     ELSE
002040         MOVE    101               TO  RETURN-CODE
002050         MOVE    FILE-STATUS       TO  MSG-501F-ST
002060         DISPLAY MSG-501F RETURN-CODE UPON CONS
002070         PERFORM ABEND-MSG-RTN
002080         STOP    RUN
002090     END-IF.
002100*-----------< ファイル  （出力）ＯＰＥＮ  >--------------------*
002110     OPEN OUTPUT UPT707O.
002120     IF  FILE-STATUS               =   ZERO
002130         CONTINUE
002140     ELSE
002150         MOVE    201               TO  RETURN-CODE
002160         MOVE    FILE-STATUS       TO  MSG-701F-ST
002170         DISPLAY MSG-701F RETURN-CODE UPON CONS
002180         PERFORM ABEND-MSG-RTN
002190         STOP    RUN
002200     END-IF.
002210*
002220*-----------< 初期ロード >-------------------------------------*
002230     PERFORM      READ-RTN.
002240 INIT-EXT.
002250     EXIT.
002260****************************************************************
002270*    (1.1)   READ-RTN
002280****************************************************************
002290 READ-RTN                          SECTION.
002300     READ      UPT705I
002310         AT    END
002320         MOVE  HIGH-VALUE                TO  END-SW
002330*
002340         NOT AT END
002350         IF    FILE-STATUS               =   ZERO
002360               ADD   +1                  TO  CNT-IN
002370               MOVE  UPT705-SYUPPAN-CD9  TO  NEW-KEY-SYUPPAN
002380         ELSE
002390               MOVE  103                 TO  RETURN-CODE
002400               MOVE FILE-STATUS          TO  MSG-503F-ST
002410               DISPLAY MSG-503F RETURN-CODE  UPON  CONS
002420               PERFORM ABEND-MSG-RTN
002430               STOP RUN
002440         END-IF.
002450 READ-EXT.
002460     EXIT.
002470****************************************************************
002480*    (2.0)   PROC-RTN                                          *
002490****************************************************************
002500 PROC-RTN                          SECTION.
002510*
002520     IF( NEW-KEY-SYUPPAN = OLD-KEY-SYUPPAN ) THEN
002530         CONTINUE
002540     ELSE
002550         PERFORM  EDIT-RTN
002560         PERFORM  WRITE-RTN
002570         MOVE     NEW-KEY-SYUPPAN       TO  OLD-KEY-SYUPPAN
002580     END-IF.
002590*
002600*-----------< READ処理 >-------------------------------------*
002610     PERFORM  READ-RTN.
002620*
002630 PROC-EXT.
002640     EXIT.
002650****************************************************************
002660*    (2.0.0)           編集      処理                          *
002670****************************************************************
002680 EDIT-RTN                          SECTION.
002690     INITIALIZE  UPT707O-REC.
002700****************************************************************
002710*    ここは、入力ファイルから
002720*    OUTPUT用ファイルへの編集を記述すること。
002730****************************************************************
002740     MOVE    UPT705-SYUPPAN-CD9
002750         TO  UPT707-TORIHIKI-CD9.
002760     MOVE    UPT705-SYUPPAN-CD9
002770         TO  UPT707-SYUPPAN-CD9.
002780     MOVE    1
002790         TO  UPT707-SYU-KBN9.
002800     MOVE    X"0A"
002810         TO  UPT707-KAI-CD.
002820*
002830 EDIT-EXT.
002840     EXIT.
002850****************************************************************
002860*    (2.0.1)           出力      処理                          *
002870****************************************************************
002880 WRITE-RTN                         SECTION.
002890*
002900     WRITE UPT707O-REC.
002910     IF    FILE-STATUS             =   ZERO
002920           ADD  1                  TO  CNT-OUT
002930     ELSE
002940           MOVE 202                TO  RETURN-CODE
002950           MOVE FILE-STATUS        TO  MSG-703F-ST
002960           DISPLAY  MSG-703F       RETURN-CODE UPON CONS
002970           PERFORM  ABEND-MSG-RTN
002980           STOP RUN
002990     END-IF.
003000*
003010 WRITE-EXT.
003020     EXIT.
003030****************************************************************
003040*    (3.0)     ＳＴＡＲＴメッセージ処理                        *
003050****************************************************************
003060 START-MSG-RTN                     SECTION.
003070     ACCEPT  WK-DATE               FROM   DATE.
003080     ACCEPT  WK-TIME               FROM   TIME.
003090     MOVE    WK-Y                  TO     WK-YY.
003100     MOVE    WK-M                  TO     WK-MM.
003110     MOVE    WK-D                  TO     WK-DD.
003120     MOVE    20                    TO     WK-20.
003130*
003140     MOVE    WK-YYYY               TO     MESSAGE-START-YYYY.
003150     MOVE    WK-MM                 TO     MESSAGE-START-MM.
003160     MOVE    WK-DD                 TO     MESSAGE-START-DD.
003170     MOVE    WK-TIM                TO     MESSAGE-START-TIM.
003180     MOVE    WK-MIN                TO     MESSAGE-START-MIN.
003190     MOVE    WK-SEC                TO     MESSAGE-START-SEC.
003200*
003210     DISPLAY  MESSAGE-START  UPON CONS.
003220*
003230 START-MSG-EXT.
003240     EXIT.
003250****************************************************************
003260*    (4.0)     ＡＢＥＮＤメッセージ処理                        *
003270****************************************************************
003280 ABEND-MSG-RTN                     SECTION.
003290     ACCEPT  WK-DATE               FROM   DATE.
003300     ACCEPT  WK-TIME               FROM   TIME.
003310     MOVE    WK-Y                  TO     WK-YY.
003320     MOVE    WK-M                  TO     WK-MM.
003330     MOVE    WK-D                  TO     WK-DD.
003340     MOVE    20                    TO     WK-20.
003350*
003360     MOVE    WK-YYYY               TO     MESSAGE-ABEND-YYYY.
003370     MOVE    WK-MM                 TO     MESSAGE-ABEND-MM.
003380     MOVE    WK-DD                 TO     MESSAGE-ABEND-DD.
003390     MOVE    WK-TIM                TO     MESSAGE-ABEND-TIM.
003400     MOVE    WK-MIN                TO     MESSAGE-ABEND-MIN.
003410     MOVE    WK-SEC                TO     MESSAGE-ABEND-SEC.
003420*
003430     DISPLAY  MESSAGE-ABEND  UPON CONS.
003440*
003450 ABEND-MSG-EXT.
003460     EXIT.
003470****************************************************************
003480*    (5.0)     ＥＮＤメッセージ処理                            *
003490****************************************************************
003500 END-MSG-RTN                     SECTION.
003510     ACCEPT  WK-DATE               FROM   DATE.
003520     ACCEPT  WK-TIME               FROM   TIME.
003530     MOVE    WK-Y                  TO     WK-YY.
003540     MOVE    WK-M                  TO     WK-MM.
003550     MOVE    WK-D                  TO     WK-DD.
003560     MOVE    20                    TO     WK-20.
003570*
003580     MOVE    WK-YYYY               TO     MESSAGE-END-YYYY.
003590     MOVE    WK-MM                 TO     MESSAGE-END-MM.
003600     MOVE    WK-DD                 TO     MESSAGE-END-DD.
003610     MOVE    WK-TIM                TO     MESSAGE-END-TIM.
003620     MOVE    WK-MIN                TO     MESSAGE-END-MIN.
003630     MOVE    WK-SEC                TO     MESSAGE-END-SEC.
003640*
003650     DISPLAY  MESSAGE-END  UPON CONS.
003660*
003670 END-MSG-EXT.
003680     EXIT.
003690****************************************************************
003700*    (6.0)              エンド 処理                            *
003710****************************************************************
003720 END-RTN                           SECTION.
003730*
003740     CLOSE     UPT705I.
003750     CLOSE     UPT707O.
003760     MOVE      ZERO                TO  RETURN-CODE.
003770*---------< 入力件数表示 >-------------------------------------*
003780     DISPLAY MSG-501I  CNT-IN  UPON CONS.
003790     DISPLAY MSG-701I  CNT-OUT UPON CONS.
003800     PERFORM END-MSG-RTN.
003810*
003820 END-EXT.
003830     EXIT.
