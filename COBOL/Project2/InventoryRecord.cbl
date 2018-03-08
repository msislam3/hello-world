       PROGRAM-ID. InventoryRecord as "InventoryRecord".
       AUTHOR. RIFAT SHAMS.
       DATE-WRITTEN. 26 FEBRUARY 2018

       ENVIRONMENT DIVISION.
       INPUT-OUTPUT SECTION.
       FILE-CONTROL.
           SELECT INVENT-FILE-IN
               ASSIGN TO
               "INVENT01.TXT"
               ORGANIZATION IS LINE SEQUENTIAL.
               
           SELECT INVENT-REPORT-OUT
               ASSIGN TO
                   "INVREPRT.TXT"
                   ORGANIZATION IS LINE SEQUENTIAL.
       CONFIGURATION SECTION.

       DATA DIVISION.
       FILE SECTION.
       FD  INVENT-FILE-IN
           RECORD CONTAINS 40 CHARACTERS.
       01  INVENT-RECORD-IN.
           05  PART-NUMBER-IN             PIC 9(5).
           05  PART-NAME-IN               PIC X(20).
           05  PART-QUANTITY-IN-HAND-IN   PIC 9(3).
           05  PART-UNIT-PRICE-IN         PIC 9(4).
           05  PART-SUPPLIER-CODE-IN      PIC X(5).
           05  PART-RE-ORDER-POINT-IN     PIC 9(3).
       
       FD  INVENT-REPORT-OUT
           RECORD CONTAINS 46 CHARACTERS.
       01  REPORT-LINE-OUT                 PIC X(46).
     
       WORKING-STORAGE SECTION.
       01  INVENT-RECORD-OUT.
           05  FILLER                      PIC X VALUE SPACES.
           05  PART-NUMBER-OUT             PIC 9(5).
           05  FILLER                      PIC X(2) VALUE SPACES.
           05  PART-NAME-OUT               PIC X(20).
           05  FILLER                      PIC X(5) VALUE SPACES.
           05  PART-QUANTITY-OUT           PIC 9(3).
           05  FILLER                      PIC X(3) VALUES SPACES.
           05  PART-VALUE-OUT              PIC 9(7).
       01  ROW-FILLER-WS                   PIC X(46).                  
       01  COLUMN-HDR-LINE-WS.
            05  FILLER                      PIC X VALUE SPACES.
            05  FILLER                      PIC X(6) VALUE "NUMBER".
            05  FILLER                      PIC X VALUE SPACES.
            05  FILLER                      PIC X(8) VALUE "PARTNAME". 
            05  FILLER                      PIC X(17) VALUES SPACES.
            05  FILLER                      PIC X(3) VALUE "QTY".
            05  FILLER                      PIC X(3) VALUES SPACES.
            05  FILLER                      PIC X(5) VALUE "VALUE".
            05  FILLER                      PIC X(2).
       01  TOTAL-VALUE-LINE-WS.
           05  FILLER                      PIC X VALUE SPACES.
           05  FILLER                      PIC X(10) VALUE "TOTALVALUE".
           05  FILLER                      PIC X(2) VALUE SPACES.
           05  INVENT-TOTAL-VALUE       PIC 9(10) VALUE 0.
       01  RECORD-READ-LINE-WS.
           05  FILLER                     PIC X VALUE SPACES.
           05  FILLER                     PIC X(12) 
           VALUE "RECORDS READ".
           05  FILLER                     PIC X(4) VALUE SPACES.
           05  INVENT-RECORD-IN-CTR    PIC 9(2) VALUE 0.
       01  RECORD-WRITTEN-LINE-WS.
           05  FILLER                     PIC X VALUE SPACES.
           05  FILLER                     PIC X(15) 
           VALUE "RECORDS WRITTEN".
           05  FILLER                     PIC X(1) VALUE SPACES.
           05  INVENT-RECORD-OUT-CTR    PIC 9(2) VALUE 0.            
       01  EOF-FLAG    PIC A(3).

       PROCEDURE DIVISION.
       100-PRODUCE-INVENT-REPORT.
           
           PERFORM 200-INITIATE-INVENT-REPORT-JOB.
           
           PERFORM 200-WRITE-COLUMN-HDR.
           
           PERFORM 200-READ-INVENT-RECORD-IN
               UNTIL EOF-FLAG="YES".
           
           PERFORM 200-WRITE-SUMMARY.
           
           PERFORM 200-TERM-INVENT-FILE.
           STOP RUN.
         
      *OPEN INVENTORY RECORD FILE
       200-INITIATE-INVENT-REPORT-JOB.
           OPEN INPUT INVENT-FILE-IN.
           OPEN OUTPUT INVENT-REPORT-OUT.

      *CLOSE INVENTORY RECORD FILE
       200-TERM-INVENT-FILE.
           CLOSE INVENT-FILE-IN.
           CLOSE INVENT-REPORT-OUT.

       200-WRITE-COLUMN-HDR.
           WRITE REPORT-LINE-OUT  FROM COLUMN-HDR-LINE-WS AFTER         
           ADVANCING 1 LINE.
           WRITE REPORT-LINE-OUT  FROM ROW-FILLER-WS.
       
       200-WRITE-SUMMARY.
           WRITE REPORT-LINE-OUT FROM TOTAL-VALUE-LINE-WS AFTER 
           ADVANCING 1 LINE.
           WRITE REPORT-LINE-OUT FROM RECORD-READ-LINE-WS AFTER 
           ADVANCING 1 LINE.
           WRITE REPORT-LINE-OUT FROM RECORD-WRITTEN-LINE-WS.
       
       200-READ-INVENT-RECORD-IN.
           READ INVENT-FILE-IN
               AT END MOVE "YES" TO EOF-FLAG
               NOT AT END PERFORM 700-CREATE-WRITE-INV-RECORD-OUT.

       700-CREATE-WRITE-INV-RECORD-OUT.
           ADD 1 TO  INVENT-RECORD-IN-CTR.
           PERFORM 900-CREATE-INV-RECORD-OUT.
           PERFORM 900-WRITE-INV-RECORD-OUT.
       
       900-CREATE-INV-RECORD-OUT.
           MOVE PART-NUMBER-IN TO PART-NUMBER-OUT.
           MOVE PART-NAME-IN TO PART-NAME-OUT.
           MOVE PART-QUANTITY-IN-HAND-IN TO PART-QUANTITY-OUT.
           MULTIPLY PART-QUANTITY-IN-HAND-IN BY PART-UNIT-PRICE-IN 
           GIVING PART-VALUE-OUT.
           ADD PART-VALUE-OUT TO INVENT-TOTAL-VALUE.

       900-WRITE-INV-RECORD-OUT.
           WRITE REPORT-LINE-OUT FROM INVENT-RECORD-OUT.
           ADD 1 TO INVENT-RECORD-OUT-CTR.