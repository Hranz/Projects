/*
   Programmers: Kristoffer Larson
   Date: February 24, 2014
   
   Description: 
      Creates WayPoint objects that the "bot" can traverse,
      and obtain gold or strength. There are four different
      WayPoints: City, Gold, Map, and blank. Extends the 
      Marker class so that the WayPoint objects will be 
      drawable, and be drawn on the SimFrame.
*/

import SimulationFramework.*;
import java.awt.*;

public class WayPoint extends Marker {
   
   private int x, y, height, cityValue, gold, mapX, mapY, neighbors;
   private boolean visted;
   
   //The "bot" can pay gold to gain strength here.
   public WayPoint(Point spot, int height, int cityValue, int neighbors) {
      super(spot, Color.CYAN, 5);
      this.x = (int)spot.getX();
      this.y = (int)spot.getY();
      this.height = height;
      this.cityValue = cityValue;
      this.neighbors = neighbors;
      visted = false;
   }//End Constructor for City WayPoints
   
   //Not used now, but the "bot" can get coordinates to find a gold WayPoint.
   public WayPoint(int x, int y, int height, int mapX, int mapY, int neighbors) {
      super(x, y, Color.MAGENTA, 5);
      this.x = x;
      this.y = y;
      this.height = height;
      this.mapX = mapX;
      this.mapY = mapY;
      this.neighbors = neighbors;
      visted = false;
   }//End Constructor for Map WayPoints
   
   //The "bot" can get gold here.
   public WayPoint(int x, int y, int height, int gold, int neighbors) {
      super(x, y, Color.YELLOW, 5);
      this.x = x;
      this.y = y;
      this.height = height;
      this.gold = gold;
      this.neighbors = neighbors;
      visted = false;
   }//End Constructor for Gold WayPoints
   
   //A WayPoint that the "bot" can travel to.
   public WayPoint(int x, int y, int height, int neighbors) {
      super(x, y, Color.BLACK);
      this.x = x;
      this.y = y;
      this.height = height;
      this.neighbors = neighbors;
      visted = false;
   }//End Constructor for Normal WayPoints 
   
   //WX to differentiate from Point class's getX()
   public int getWX() {
      return x;
   }//End getWX
   
   //WY to differentiate from Point class's getY()
   public int getWY() { 
      return y;
   }//End getWY
   
   public int getHeight() {
      return height;
   }//End getHeight
   
   public int getCityValue() {
      return cityValue;
   }//End getCityValue
   
   public int getGold() {
      return gold;
   }//End getGold
   
   public boolean getVisted() {
      return visted;
   }//End getVisted
   
   public void setVisted() {
      visted = true;
   }//End setVisted

}//End Class