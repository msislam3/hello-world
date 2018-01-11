package cst8284.shape;

import java.util.ArrayList;

public class TestSolidObject {
	
   private static ArrayList<SolidObject<BasicShape>> solidObjects = new ArrayList<>();
	
   public static void main(String[] args){

     /* Circle circle1 = new Circle(3.0);  // and use a depth = 5.0
      Square square1 = new Square(4.0);  // and use a depth = 2.0
      Rectangle rectangle1 = new Rectangle(2.0, 8.0); // and use a depth = 2.0
      Rectangle rectangle2 = new Rectangle(3.0, 5.0); // and use a depth = 2.0

      SolidObject<Circle> cylinder1 = new SolidObject<Circle>(circle1, 5.0);
      SolidObject<Square> cube1 = new SolidObject<Square>(square1, 2.0);
      SolidObject<Rectangle> block1 = new SolidObject<Rectangle>(rectangle1, 2.0);
      SolidObject<Rectangle> block2 = new SolidObject<Rectangle>(rectangle2, 2.0);
      
      solidObjects.add(cylinder1);
      solidObjects.add(cube1);
      solidObjects.add(block1);
      solidObjects.add(block2); */
	   
	   solidObjects.add(new SolidObject<BasicShape>(new Circle(3.0), 5.0));
	   solidObjects.add(new SolidObject<BasicShape>(new Square(4.0), 2.0));
	   solidObjects.add(new SolidObject<BasicShape>(new Rectangle(2.0, 8.0), 2.0));
	   solidObjects.add(new SolidObject<BasicShape>(new Rectangle(3.0, 5.0), 2.0));
		
      displayVolumeComparison(solidObjects); 
      displaySurfaceAreaComparison(solidObjects);

   }

   public static boolean isVolumeEqual(SolidObject<BasicShape> obj1, SolidObject<BasicShape> obj2) {
	   return obj1.getVolume() == obj2.getVolume();
   }
	
   public static boolean isSurfaceAreaEqual(SolidObject<BasicShape> obj1, SolidObject<BasicShape> obj2) {
	   return obj1.getSurfaceArea() == obj2.getSurfaceArea();
   }
   
   public static void displayVolumeComparison(ArrayList<SolidObject<BasicShape>> arList){
		
      // Print out column header
      System.out.println("\nCheck the array: are the volumes of the solid objects equal?\n");
      System.out.print("\t\t");
      for (SolidObject<BasicShape> ColumnHeader: arList)
         System.out.print("\t" + ColumnHeader.getName());
		
	 // Print out each row,starting with the name of the object
	 for (SolidObject<BasicShape> solidObjRow: arList){
	    System.out.println();	// Next line
	    System.out.print(solidObjRow.getName());
            for (SolidObject<BasicShape> solidObjColumn: arList)
               System.out.print("\t\t" + isVolumeEqual(solidObjColumn, solidObjRow));
         }    
      }
	
   public static void displaySurfaceAreaComparison(ArrayList<SolidObject<BasicShape>> arList){
		
      // Print out column header
      System.out.println("\nCheck the array: are the surface areas of the solid objects equal?\n");
      System.out.print("\t\t");
       for (SolidObject<BasicShape> ColumnHeader: arList)
         System.out.print("\t" + ColumnHeader.getName());
		
      // Print out each row,starting with the name of the object
      for (SolidObject<BasicShape> solidObjRow: arList){
         System.out.println();	// Next line
         System.out.print(solidObjRow.getName());
         for (SolidObject<BasicShape> solidObjColumn: arList)
            System.out.print("\t\t" + isSurfaceAreaEqual(solidObjColumn, solidObjRow));  
      }
   }
}
