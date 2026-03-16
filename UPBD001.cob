000010****************************************************************
000020*    システム名    ： 新聞競馬新聞実績
000030*    プログラム名  ： 即売ファイル作成処理
000040*    プログラムＩＤ： UPBD001
000050*    作  成  者    ： T.HARUYAMA
000060*    作  成  日    ： 2003年12月 1日
000070*    修  正  日    ： 2004年01月27日
000080****************************************************************
000090 IDENTIFICATION                    DIVISION.
000100 PROGRAM-ID.                       UPBD001.
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
000270     SELECT  UEU721I      ASSIGN     TO  UEU721I
000280                      STATUS     IS  FILE-STATUS.
000290*------------ファイル   （出力）-------------------------------*
000300     SELECT  UPT701O      ASSIGN
000310                                TO  UPT701O
000320             ACCESS  MODE       IS  SEQUENTIAL
000330             FILE    STATUS     FILE-STATUS.
000340*
000350 DATA                              DIVISION.
000360 FILE                              SECTION.
000370*
000380****************************************************************
000390*            ファイル   （入力）
000400****************************************************************
000410 FD  UEU721I        LABEL  RECORD STANDARD.
000420 01  UEU721I-REC.
000430     COPY   UEU721C.
000440*
000450****************************************************************
000460*            ファイル   （出力）
000470****************************************************************
000480 FD  UPT701O         LABEL  RECORD STANDARD.
000490 01  UPT701O-REC.
000500     COPY   UPT701C.
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
000730       05  NEW-KEY-TORIHIKI            PIC 9(06) VALUE ZERO.
000740       05  NEW-KEY-SYUPPAN             PIC 9(06) VALUE ZERO.
000750     03  OLD-KEY.
000760       05  OLD-KEY-TORIHIKI            PIC 9(06) VALUE ZERO.
000770       05  OLD-KEY-SYUPPAN             PIC 9(06) VALUE ZERO.
000780*
000790*---------------< ステータスの定義 >--------------------------*
000800 01  STATUS-AREA.
000810     03  FILE-STATUS                   PIC X(02) VALUE ZERO.
000820*---------------< カウントの定義 >----------------------------*
000830 01  UEU721-CNT-AREA.
000840     03  CNT-IN                        PIC 9(09) VALUE ZERO.
000850     03  CNT-OUT                       PIC 9(09) VALUE ZERO.
000860*
000870*---------------< メッセージ１の定義 >------------------------*
000880 01  MSG-PGID                          PIC X(08) VALUE
000890     "UPBD001".
000900 01  MESSAGE-AREA1.
000910     03  MESSAGE-START.
000920       05  FILLER                      PIC X(12) VALUE
000930     "++ UPBD001 ".
000940       05  MESSAGE-START-YMD.
000950         07  MESSAGE-START-YYYY        PIC 9(04).
000960         07  FILLER                    PIC X(01) VALUE "/".
000970         07  MESSAGE-START-MM          PIC 9(02).
000980         07  FILLER                    PIC X(01) VALUE "/".
000990         07  MESSAGE-START-DD          PIC 9(02).
001000         07  FILLER                    PIC X(01) VALUE " ".
001010         07  MESSAGE-START-TIM         PIC 9(02).
001020         07  FILLER                    PIC X(01) VALUE ":".
001030         07  MESSAGE-START-MIN         PIC 9(02).
001040         07  FILLER                    PIC X(01) VALUE ":".
001050         07  MESSAGE-START-SEC         PIC 9(02).
001060         07  FILLER                    PIC X(01) VALUE " ".
001070       05  FILLER                      PIC X(08) VALUE
001080     "*START* ".
001090     03  MESSAGE-END.
001100       05  FILLER                      PIC X(12) VALUE
001110     "++ UPBD001 ".
001120       05  MESSAGE-END-YMD.
001130         07  MESSAGE-END-YYYY          PIC 9(04).
001140         07  FILLER                    PIC X(01) VALUE "/".
001150         07  MESSAGE-END-MM            PIC 9(02).
001160         07  FILLER                    PIC X(01) VALUE "/".
001170         07  MESSAGE-END-DD            PIC 9(02).
001180         07  FILLER                    PIC X(01) VALUE " ".
001190         07  MESSAGE-END-TIM           PIC 9(02).
001200         07  FILLER                    PIC X(01) VALUE ":".
001210         07  MESSAGE-END-MIN           PIC 9(02).
001220         07  FILLER                    PIC X(01) VALUE ":".
001230         07  MESSAGE-END-SEC           PIC 9(02).
001240         07  FILLER                    PIC X(01) VALUE " ".
001250       05  FILLER                      PIC X(13) VALUE
001260     "*NORMAL END* ".
001270     03  MESSAGE-ABEND.
001280       05  FILLER                      PIC X(12) VALUE
001290     "++ UPBD001 ".
001300       05  MESSAGE-ABEND-YMD.
001310         07  MESSAGE-ABEND-YYYY        PIC 9(04).
001320         07  FILLER                    PIC X(01) VALUE "/".
001330         07  MESSAGE-ABEND-MM          PIC 9(02).
001340         07  FILLER                    PIC X(01) VALUE "/".
001350         07  MESSAGE-ABEND-DD          PIC 9(02).
001360         07  FILLER                    PIC X(01) VALUE " ".
001370         07  MESSAGE-ABEND-TIM         PIC 9(02).
001380         07  FILLER                    PIC X(01) VALUE ":".
001390         07  MESSAGE-ABEND-MIN         PIC 9(02).
001400         07  FILLER                    PIC X(01) VALUE ":".
001410         07  MESSAGE-ABEND-SEC         PIC 9(02).
001420         07  FILLER                    PIC X(01) VALUE " ".
001430       05  FILLER                      PIC X(15) VALUE
001440     "*ABNORMAL END* ".
001450     03  MESSAGE-0.
001460       05  MSG0-PGID                   PIC X(08).
001470       05  MSG0-NAME                   PIC X(41).
001480*
001490*---< メッセージ２の定義 >---*
001500 01  MESSAGE-AREA2.
001510     03  MSG-501I.
001520       05  FILLER                      PIC  X(41) VALUE
001530     "++ UPBD001  501I UEU721       IN COUNT = ".
001540     03  MSG-701I.
001550       05  FILLER                      PIC  X(41) VALUE
001560     "++ UPBD001  701I UPT701      OUT COUNT = ".
001570     03  MSG-501F.
001580       05  FILLER                      PIC  X(41) VALUE
001590     "++ UPBD001  501F UEU721  OPEN  ERROR ST= ".
001600       05  MSG-501F-ST                 PIC  9(03).
001610       05  FILLER                      PIC  X(01) VALUE " ".
001620     03  MSG-503F.
001630       05  FILLER                      PIC  X(41) VALUE
001640     "++ UPBD001  503F UEU721  READ  ERROR ST= ".
001650       05  MSG-503F-ST                 PIC  9(03).
001660       05  FILLER                      PIC  X(01) VALUE " ".
001670     03  MSG-701F.
001680       05  FILLER                      PIC X(41)     VALUE
001690     "++ UPBD001  701F OUTPUT  OPEN  ERROR ST= ".
001700       05  MSG-701F-ST                 PIC  9(03).
001710     03  MSG-703F.
001720       05  FILLER                      PIC  X(41) VALUE
001730     "++ UPBD001  703F OUTPUT  WRITE ERROR ST= ".
001740       05  MSG-703F-ST                 PIC  9(03).
001750*
001760 PROCEDURE                         DIVISION.
001770****************************************************************
001780*    (0.0)   メイン              処理                          *
001790****************************************************************
001800 MAIN-RTN                          SECTION.
001810*
001820     PERFORM      INIT-RTN.
001830*
001840     PERFORM      PROC-RTN
001850         UNTIL    END-SW           =   HIGH-VALUE.
001860*
001870     PERFORM      END-RTN.
001880*
001890     STOP         RUN.
001900*
001910 MAIN-EXT.
001920     EXIT.
001930****************************************************************
001940*    (1.0)   イニシャル          処理                          *
001950****************************************************************
001960 INIT-RTN                          SECTION.
001970     PERFORM      START-MSG-RTN.
001980*
001990     MOVE    99                    TO  RETURN-CODE.
002000*
002010*-----------< ファイル  （入力）ＯＰＥＮ  >--------------------*
002020     OPEN INPUT UEU721I.
002030     IF  FILE-STATUS               =   ZERO
002040         CONTINUE
002050     ELSE
002060         MOVE    101               TO  RETURN-CODE
002070         MOVE    FILE-STATUS       TO  MSG-501F-ST
002080         DISPLAY MSG-501F RETURN-CODE UPON CONS
002090         PERFORM ABEND-MSG-RTN
002100         STOP    RUN
002110     END-IF.
002120*-----------< ファイル  （出力）ＯＰＥＮ  >--------------------*
002130     OPEN OUTPUT UPT701O.
002140     IF  FILE-STATUS               =   ZERO
002150         CONTINUE
002160     ELSE
002170         MOVE    201               TO  RETURN-CODE
002180         MOVE    FILE-STATUS       TO  MSG-701F-ST
002190         DISPLAY MSG-701F RETURN-CODE UPON CONS
002200         PERFORM ABEND-MSG-RTN
002210         STOP    RUN
002220     END-IF.
002230*
002240*-----------< 初期ロード >-------------------------------------*
002250     PERFORM      READ-RTN.
002260 INIT-EXT.
002270     EXIT.
002280****************************************************************
002290*    (1.1)   READ-RTN
002300****************************************************************
002310 READ-RTN                          SECTION.
002320     READ      UEU721I
002330         AT    END
002340         MOVE  HIGH-VALUE                TO  END-SW
002350*
002360         NOT AT END
002370         IF    FILE-STATUS               =   ZERO
002380               ADD   +1                  TO  CNT-IN
002390               MOVE  UEU721-TOR-CD9      TO  NEW-KEY-TORIHIKI
002400               MOVE  UEU721-MAKER-CD9    TO  NEW-KEY-SYUPPAN
002410         ELSE
002420               MOVE  103                 TO  RETURN-CODE
002430               MOVE FILE-STATUS          TO  MSG-503F-ST
002440               DISPLAY MSG-503F RETURN-CODE  UPON  CONS
002450               PERFORM ABEND-MSG-RTN
002460               STOP RUN
002470         END-IF.
002480 READ-EXT.
002490     EXIT.
002500****************************************************************
002510*    (2.0)   PROC-RTN                                          *
002520****************************************************************
002530 PROC-RTN                          SECTION.
002540*
002550     IF( NEW-KEY-TORIHIKI = OLD-KEY-TORIHIKI ) THEN
002560         IF( NEW-KEY-SYUPPAN = OLD-KEY-SYUPPAN ) THEN
002570              CONTINUE
002580         ELSE
002590              PERFORM  EDIT-RTN
002600              PERFORM  WRITE-RTN
002610              MOVE     NEW-KEY-TORIHIKI TO  OLD-KEY-TORIHIKI
002620              MOVE     NEW-KEY-SYUPPAN  TO  OLD-KEY-SYUPPAN
002630         END-IF
002640     ELSE
002650         PERFORM  EDIT-RTN
002660         PERFORM  WRITE-RTN
002670         MOVE     NEW-KEY-TORIHIKI      TO  OLD-KEY-TORIHIKI
002680         MOVE     NEW-KEY-SYUPPAN       TO  OLD-KEY-SYUPPAN
002690     END-IF.
002700*
002710*-----------< READ処理 >-------------------------------------*
002720     PERFORM  READ-RTN.
002730*
002740 PROC-EXT.
002750     EXIT.
002760****************************************************************
002770*    (2.0.0)           編集      処理                          *
002780****************************************************************
002790 EDIT-RTN                          SECTION.
002800     INITIALIZE  UPT701O-REC.
002810****************************************************************
002820*    ここは、入力ファイルから
002830*    OUTPUT用ファイルへの編集を記述すること。
002840****************************************************************
002850     MOVE    UEU721-TOR-CD9
002860         TO  UPT701-TORIHIKI-CD9.
002870     MOVE    UEU721-MAKER-CD9
002880         TO  UPT701-SYUPPAN-CD9.
002890     MOVE    2
002900         TO  UPT701-SYU-KBN9.
002910     MOVE    X"0A"
002920         TO  UPT701-KAI-CD.
002930*
002940 EDIT-EXT.
002950     EXIT.
002960****************************************************************
002970*    (2.0.1)           出力      処理                          *
002980****************************************************************
002990 WRITE-RTN                         SECTION.
003000*
003010     WRITE UPT701O-REC.
003020     IF    FILE-STATUS             =   ZERO
003030           ADD  1                  TO  CNT-OUT
003040     ELSE
003050           MOVE 202                TO  RETURN-CODE
003060           MOVE FILE-STATUS        TO  MSG-703F-ST
003070           DISPLAY  MSG-703F       RETURN-CODE UPON CONS
003080           PERFORM  ABEND-MSG-RTN
003090           STOP RUN
003100     END-IF.
003110*
003120 WRITE-EXT.
003130     EXIT.
003140****************************************************************
003150*    (3.0)     ＳＴＡＲＴメッセージ処理                        *
003160****************************************************************
003170 START-MSG-RTN                     SECTION.
003180     ACCEPT  WK-DATE               FROM   DATE.
003190     ACCEPT  WK-TIME               FROM   TIME.
003200     MOVE    WK-Y                  TO     WK-YY.
003210     MOVE    WK-M                  TO     WK-MM.
003220     MOVE    WK-D                  TO     WK-DD.
003230     MOVE    20                    TO     WK-20.
003240*
003250     MOVE    WK-YYYY               TO     MESSAGE-START-YYYY.
003260     MOVE    WK-MM                 TO     MESSAGE-START-MM.
003270     MOVE    WK-DD                 TO     MESSAGE-START-DD.
003280     MOVE    WK-TIM                TO     MESSAGE-START-TIM.
003290     MOVE    WK-MIN                TO     MESSAGE-START-MIN.
003300     MOVE    WK-SEC                TO     MESSAGE-START-SEC.
003310*
003320     DISPLAY  MESSAGE-START  UPON CONS.
003330*
003340 START-MSG-EXT.
003350     EXIT.
003360****************************************************************
003370*    (4.0)     ＡＢＥＮＤメッセージ処理                        *
003380****************************************************************
003390 ABEND-MSG-RTN                     SECTION.
003400     ACCEPT  WK-DATE               FROM   DATE.
003410     ACCEPT  WK-TIME               FROM   TIME.
003420     MOVE    WK-Y                  TO     WK-YY.
003430     MOVE    WK-M                  TO     WK-MM.
003440     MOVE    WK-D                  TO     WK-DD.
003450     MOVE    20                    TO     WK-20.
003460*
003470     MOVE    WK-YYYY               TO     MESSAGE-ABEND-YYYY.
003480     MOVE    WK-MM                 TO     MESSAGE-ABEND-MM.
003490     MOVE    WK-DD                 TO     MESSAGE-ABEND-DD.
003500     MOVE    WK-TIM                TO     MESSAGE-ABEND-TIM.
003510     MOVE    WK-MIN                TO     MESSAGE-ABEND-MIN.
003520     MOVE    WK-SEC                TO     MESSAGE-ABEND-SEC.
003530*
003540     DISPLAY  MESSAGE-ABEND  UPON CONS.
003550*
003560 ABEND-MSG-EXT.
003570     EXIT.
003580****************************************************************
003590*    (5.0)     ＥＮＤメッセージ処理                            *
003600****************************************************************
003610 END-MSG-RTN                     SECTION.
003620     ACCEPT  WK-DATE               FROM   DATE.
003630     ACCEPT  WK-TIME               FROM   TIME.
003640     MOVE    WK-Y                  TO     WK-YY.
003650     MOVE    WK-M                  TO     WK-MM.
003660     MOVE    WK-D                  TO     WK-DD.
003670     MOVE    20                    TO     WK-20.
003680*
003690     MOVE    WK-YYYY               TO     MESSAGE-END-YYYY.
003700     MOVE    WK-MM                 TO     MESSAGE-END-MM.
003710     MOVE    WK-DD                 TO     MESSAGE-END-DD.
003720     MOVE    WK-TIM                TO     MESSAGE-END-TIM.
003730     MOVE    WK-MIN                TO     MESSAGE-END-MIN.
003740     MOVE    WK-SEC                TO     MESSAGE-END-SEC.
003750*
003760     DISPLAY  MESSAGE-END  UPON CONS.
003770*
003780 END-MSG-EXT.
003790     EXIT.
003800****************************************************************
003810*    (6.0)              エンド 処理                            *
003820****************************************************************
003830 END-RTN                           SECTION.
003840*
003850     CLOSE     UEU721I.
003860     CLOSE     UPT701O.
003870     MOVE      ZERO                TO  RETURN-CODE.
003880*---------< 入力件数表示 >-------------------------------------*
003890     DISPLAY MSG-501I  CNT-IN  UPON CONS.
003900     DISPLAY MSG-701I  CNT-OUT UPON CONS.
003910     PERFORM END-MSG-RTN.
003920*
003930 END-EXT.
003940     EXIT.
