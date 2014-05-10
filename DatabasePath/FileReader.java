/*
   Programmers: Kristoffer Larson, Josue Ruiz
   Date: May 8, 2014
   
   Description: Reads in the values from the given file, constructs
      WayPoint objects of the appropriate type, and then stores them
      in the HashMap.

*/

import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.awt.Point;
import java.io.File;
import java.io.IOException;

public class FileReader {

   public HashMap<Point, WayPoint> map;
   private int c = 0, g = 0, m = 0;
   
   public FileReader(HashMap<Point, WayPoint> map, String fileName) {
      File sourceFile = new File(fileName);
      this.map = map;
      
      if (!sourceFile.exists()) {
         System.out.print(fileName + " doesn't exist.");
         System.exit(0);
      }//End if
      
      try {
         Scanner input = new Scanner(sourceFile);
         while(input.hasNext()) {
            readFile(input); 
         }//End while
      } catch (IOException e) {
         System.err.println("IOException: " + e.getMessage());
      }//End try/catch
   }//End FileReader() constructor
   
   private void readFile(Scanner input) {
      ArrayList<Point> neigh = new ArrayList<Point>();
      int nx, ny;
      
      //Creates WayPoints with values that are read in
      int x = input.nextInt();
      int y = input.nextInt();
         Point keyPoint = new Point(x, y);
      int height = input.nextInt();
      int cityValue = input.nextInt();
      int gold = input.nextInt();
      int mapX = input.nextInt();
      int mapY = input.nextInt();
      int neighbor = input.nextInt();
      
      for (int i = 0; i < neighbor; i++) {
         nx = input.nextInt();
         ny = input.nextInt();
         neigh.add(new Point(nx, ny));
      }//End loop
   
      if (cityValue > 0) { //City WayPoint
         c++;
         map.put(keyPoint, 
            (new WayPoint(keyPoint, height, cityValue, neighbor, neigh)));
      } else if (gold > 0) { //Gold WayPoint
         g++;
         map.put(keyPoint, 
            (new WayPoint(x, y, height, gold, neighbor, neigh)));
      } else if (mapX > 0 || mapY > 0) { //Map WayPoint
         m++;
         map.put(keyPoint, 
            (new WayPoint(x, y, height, mapX, mapY, neighbor, neigh)));
      } else {//Normal WayPoint
         map.put(keyPoint, 
            (new WayPoint(x, y, height, neighbor, neigh)));
      } //End if
   }//End readFile() method
   
   public int getNumCities() {
      return c;
   }//End getNumCities() method
   
   public int getNumGold() {
      return g;
   }//End getNumGold() method
   
   public int getNumMaps() {
      return m;
   }//End getNumMaps() method
   
}//End FileReader class
