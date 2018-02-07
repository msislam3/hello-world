       IDENTIFICATION DIVISION.
       PROGRAM-ID. lab4 as "lab4".
       AUTHOR. RIFAT SHAMS.
       DATE-WRITTEN. 8 February, 2018.
       
       ENVIRONMENT DIVISION.
       CONFIGURATION SECTION.
       INPUT-OUTPUT SECTION.
       FILE-CONTROL.
           SELECT SALES-FILE
               ASSIGN TO "SALES.DAT"
               ORGANIZATION IS LINE SEQUENTIAL.
       DATA DIVISION.
       FILE SECTION.
       FD  SALES-FILE
           RECORD CONTAINS 44 CHARACTERS.
       01  SALES-RECORD.
           05  CAR-REGISTRATION-NUMBER-IN PIC 9(8).
           05  CAR-TYPE-IN                PIC X(10).        
           05  CAR-MODEL-IN               PIC X(5).
           05  CAR-MODEL-YEAR-IN.
               10  MODEL-YEAR-IN    PIC 9(4).
               10  MODEL-MONTH-IN   PIC 9(2).
               10  MODEL-DAY-IN     PIC 9(2).
           05  RENTER-IN.
               10  RENTER-NAME-IN.
                   20  RENTER-FIRST-NAME-IN   PIC X(15).
                   20  RENTER-INITIAL-IN      PIC X(2).
                   20  RENTER-LAST-NAME-IN    PIC  X(28).
               10  RENTER-ADDRESS-IN.
                   20  STREET-ADDRESS-IN  PIC X(25).
                   20  CITY-IN            PIC X(15).
                   20  PROVINCE-IN        PIC X(15).
                   20  POSTAL-CODE-IN     PIC X(6).
               10  RETURN-DATE-IN.
                   20  RETURN-DATE-YEAR-IN    PIC 9(4).
                   20  RETURN-DATE-MONTH-IN   PIC 9(2).
                   20  RETURN-DATE-DAY-IN     PIC 9(2).
       WORKING-STORAGE SECTION.
       01  EOF-FLAG    PIC A(3).

       PROCEDURE DIVISION.
       PRODUCE-SALES-REPORT.
       
           PERFORM INIT-SALES-RPT.
       
           PERFORM DISPLAY-SALES-REC
	           UNTIL EOF-FLAG = "YES"
           PERFORM TERM-SALES-RPT.
       STOP RUN.

       INIT-SALES-RPT.
           OPEN INPUT SALES-FILE.
       
       
       DISPLAY-SALES-REC.
           READ SALES-FILE
            AT END MOVE "YES" TO EOF-FLAG.
           DISPLAY SALES-RECORD.
           
       TERM-SALES-RPT.
           CLOSE SALES-FILE.
       
       END PROGRAM lab4.
       
       
       
