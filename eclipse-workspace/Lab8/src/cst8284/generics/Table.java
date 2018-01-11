package cst8284.generics;

import java.util.ArrayList;

public class Table {
	
	 public static <E> void displayComparisonTable(ArrayList<E> arList){
	      int index = 0;
		  // Print out column header
	      System.out.println("\nCheck the array: are the volumes of the objects equal?\n");
	      System.out.print("\t\t");
	      for (E ColumnHeader: arList)
	         System.out.print("\t" + ColumnHeader.getClass().getSimpleName() +index++);
			
		 // Print out each row,starting with the name of the object
	     index = 0;
		 for (E objRow: arList){
		    System.out.println();	// Next line
		    System.out.print(objRow.getClass().getSimpleName()+index++);
	            for (E objColumn: arList)
	               System.out.print("\t\t" + objColumn.equals(objRow));
	         }
	 }
}
