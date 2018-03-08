       PROGRAM-ID. Project2.
       AUTHOR. RIFAT SHAMS.
       DATE-WRITTEN. 11 MARCH 2018

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
            05  COLUMN-HDR-NUMBER           PIC X(6) VALUE "NUMBER".
            05  FILLER                      PIC X VALUE SPACES.
            05  COLUMN-HDR-PART-NAME        PIC X(8) VALUE "PARTNAME".
            05  FILLER                      PIC X(17) VALUES SPACES.
            05  COLUMN-HDR-QTY              PIC X(3) VALUE "QTY".
            05  FILLER                      PIC X(3) VALUES SPACES.
            05  COLUMN-HDR-VALUE            PIC X(5) VALUE "VALUE".
            05  FILLER                      PIC X(2).
       01  TOTAL-VALUE-LINE-WS.
           05  FILLER                      PIC X VALUE SPACES.
           05  SUMMARY-HDR-TOTAL           PIC X(10) VALUE "TOTALVALUE".
           05  FILLER                      PIC X(2) VALUE SPACES.
           05  INVENT-TOTAL-VALUE          PIC 9(10) VALUE 0.
           05  FILLER                      PIC X(23) VALUE SPACES.
       01  RECORD-READ-LINE-WS.
           05  FILLER                     PIC X VALUE SPACES.
           05  SUMMARY-RECORD-READ        PIC X(12) VALUE 
           "RECORDS READ".
           05  FILLER                     PIC X(4) VALUE SPACES.
           05  INVENT-RECORD-IN-CTR       PIC 9(2) VALUE 0.
           05  FILLER                     PIC X(27) VALUE SPACES.
       01  RECORD-WRITTEN-LINE-WS.
           05  FILLER                     PIC X VALUE SPACES.
           05  FILLER                     PIC X(15) VALUE 
           "RECORDS WRITTEN".
           05  FILLER                     PIC X(1) VALUE SPACES.
           05  INVENT-RECORD-OUT-CTR      PIC 9(2) VALUE 0.
           05  FILLER                     PIC X(27) VALUE SPACES.
       01  EOF-FLAG    PIC A(3).

       PROCEDURE DIVISION.
       100-PRODUCE-INVENT-REPORT.
           
           PERFORM 200-INITIATE-INVENT-REPORT-JOB.

           PERFORM 200-PROCESS-INVENT-RECORD
               UNTIL EOF-FLAG="YES".
           
           PERFORM 200-WRITE-REPORT-SUMMARY.
           
           PERFORM 200-TERM-INVENT-FILE.
           STOP RUN.
         
      *INITIATES INVENTORY REPORT JOB
       200-INITIATE-INVENT-REPORT-JOB.
           
           PERFORM 700-OPEN-FILES
           PERFORM 700-WRITE-COLUMN-HDR.
           PERFORM 700-READ-INVENT-RECORD.
       
      *PROCESS EACH INVENTORY RECORD READ FROM THE FILE 
       200-PROCESS-INVENT-RECORD.
           PERFORM 700-PRODUCE-INVENT-RECORD-OUT.
           PERFORM 700-READ-INVENT-RECORD.
           
      *CLOSE INVENTORY RECORD FILE
       200-TERM-INVENT-FILE.
           CLOSE INVENT-FILE-IN.
           CLOSE INVENT-REPORT-OUT.
      
      *WRITE REPORT SUMMARY
       200-WRITE-REPORT-SUMMARY.
           WRITE REPORT-LINE-OUT FROM TOTAL-VALUE-LINE-WS AFTER 
           ADVANCING 1 LINE.
           WRITE REPORT-LINE-OUT FROM RECORD-READ-LINE-WS AFTER 
           ADVANCING 2 LINE.
           WRITE REPORT-LINE-OUT FROM RECORD-WRITTEN-LINE-WS.

      *OPEN INPUT AND OUTPUT FILES
       700-OPEN-FILES.
           OPEN INPUT INVENT-FILE-IN.
           OPEN OUTPUT INVENT-REPORT-OUT.
      
      *WRITE REPORT HEADER  
       700-WRITE-COLUMN-HDR.
           WRITE REPORT-LINE-OUT  FROM COLUMN-HDR-LINE-WS AFTER
           ADVANCING 1 LINE.
           WRITE REPORT-LINE-OUT  FROM ROW-FILLER-WS.
       
      *READ INVENTORY RECORD FROM FILE 
       700-READ-INVENT-RECORD.
           READ INVENT-FILE-IN
               AT END MOVE "YES" TO EOF-FLAG
               NOT AT END ADD 1 TO  INVENT-RECORD-IN-CTR.
       
      *CREATE AND WRITE INVENTORY REPORT DATA 
       700-PRODUCE-INVENT-RECORD-OUT.
           PERFORM 900-CREATE-INVENT-RECORD-OUT.
           PERFORM 900-WRITE-INVENT-RECORD-OUT.
      
      *CREATE INVENTORY REPORT FOR WRITING
       900-CREATE-INVENT-RECORD-OUT.
           MOVE PART-NUMBER-IN TO PART-NUMBER-OUT.
           MOVE PART-NAME-IN TO PART-NAME-OUT.
           MOVE PART-QUANTITY-IN-HAND-IN TO PART-QUANTITY-OUT.
           MULTIPLY PART-QUANTITY-IN-HAND-IN BY PART-UNIT-PRICE-IN 
           GIVING PART-VALUE-OUT.
           ADD PART-VALUE-OUT TO INVENT-TOTAL-VALUE.
      
      *WRITE INVENTORY REPORT DATA 
       900-WRITE-INVENT-RECORD-OUT.
           WRITE REPORT-LINE-OUT FROM INVENT-RECORD-OUT.
           ADD 1 TO INVENT-RECORD-OUT-CTR.