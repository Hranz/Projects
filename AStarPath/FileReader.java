import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.awt.Point;
import java.io.File;
import java.io.IOException;

public class FileReader {

   HashMap<Point, WayPoint> map;
   
   public FileReader(HashMap<Point, WayPoint> m, String fileName) {
      map = m;
      //Read from file
      File sourceFile = new File(fileName);
      
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
         map.put(keyPoint, 
            (new WayPoint(keyPoint, height, cityValue, neighbor, neigh)));
      } else if (gold > 0) //Gold WayPoint
         map.put(keyPoint, 
            (new WayPoint(x, y, height, gold, neighbor, neigh)));
      else if (mapX > 0 || mapY > 0) //Map WayPoint
         map.put(keyPoint, 
            (new WayPoint(x, y, height, mapX, mapY, neighbor, neigh)));
      else //Normal WayPoint
         map.put(keyPoint, 
            (new WayPoint(x, y, height, neighbor, neigh)));
   }//End readFile() method
}//End FileReader class
