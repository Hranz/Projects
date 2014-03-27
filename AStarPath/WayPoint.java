/*
   Programmers: Kristoffer Larson, Josue Ruiz
   Date: , 2014
   
   Description: 
      Creates WayPoint objects that the "bot" can traverse,
      and obtain gold or strength. There are four different
      WayPoints: City, Gold, Map, and blank. Extends the 
      Marker class so that the WayPoint objects will be 
      drawable, and be drawn on the SimFrame.
*/

import SimulationFramework.*;
import java.awt.*;
import java.util.ArrayList;

public class WayPoint extends Marker implements Comparable<WayPoint>{
   
   private int x, y, height, cityValue, gold, mapX, mapY, neighbors;
   private ArrayList<Point> neigh;
   private boolean visted;
   
   //The "bot" can pay gold to gain strength here.
   public WayPoint(Point spot, int height, int cityValue, int neighbors, ArrayList<Point> neigh) {
      super(spot, Color.CYAN, 5);
      this.x = (int)spot.getX();
      this.y = (int)spot.getY();
      this.height = height;
      this.cityValue = cityValue;
      this.neighbors = neighbors;
      this.neigh = neigh;
      //visted = false;
   }//End Constructor for City WayPoints
   
   //The "bot" can get coordinates to find a gold WayPoint.
   public WayPoint(int x, int y, int height, int mapX, int mapY, int neighbors, ArrayList<Point> neigh) {
      super(x, y, Color.MAGENTA, 5);
      this.x = x;
      this.y = y;
      this.height = height;
      this.mapX = mapX;
      this.mapY = mapY;
      this.neighbors = neighbors;
      this.neigh = neigh;
      //visted = false;
   }//End Constructor for Map WayPoints
   
   //The "bot" can get gold here.
   public WayPoint(int x, int y, int height, int gold, int neighbors, ArrayList<Point> neigh) {
      super(x, y, Color.YELLOW, 5);
      this.x = x;
      this.y = y;
      this.height = height;
      this.gold = gold;
      this.neighbors = neighbors;
      this.neigh = neigh;
      //visted = false;
   }//End Constructor for Gold WayPoints
   
   //A WayPoint that the "bot" can travel to.
   public WayPoint(int x, int y, int height, int neighbors, ArrayList<Point> neigh) {
      super(x, y, Color.BLACK);
      this.x = x;
      this.y = y;
      this.height = height;
      this.neighbors = neighbors;
      this.neigh = neigh;
      //visted = false;
   }//End Constructor for Normal WayPoints 
   
   public void makeNormal() { //Change the draw of the waypoint on the map
      //make into normalWayPoint
      setColor(Color.BLACK);
      setSize(2);
      this.gold = 0;
      this.mapX = 0;
      this.mapY = 0;
   }
   
   //WX to differentiate from Point class's getX()
   public int getWX() {
      return x;
   }//End getWX
   
   //WY to differentiate from Point class's getY()
   public int getWY() { 
      return y;
   }//End getWY
   
   public Point getPoint() {
      return new Point(x, y);
   }//End getPoint;
   
   public int getHeight() {
      return height;
   }//End getHeight
   
   public int getMapX() {
      return mapX;
   }//End getMapX
   
   public int getMapY() {
      return mapY;
   }//End getMapY
   
   public Point getMapPoint() {
      return new Point(mapX, mapY);
   }//End getMapPoint
   
   public int getCityValue() {
      return cityValue;
   }//End getCityValue
   
   public int getGold() {
      return gold;
   }//End getGold
   
   public int getNeighbors() {
      return neighbors;
   }//End getNeigh
   
   public ArrayList<Point> getNeigh() {
      return neigh;
   }//End getNeighbors
   
   public boolean getVisted() {
      return visted;
   }//End getVisted
   
   public void setVisted() {
      visted = true;
   }//End setVisted
   
   public int compareTo(WayPoint wp) {
      return -1;
   }
   
   public boolean equals(Object obj) {
		if (!(obj instanceof WayPoint))
			return false;	
		if (obj == this)
			return true;
		return this.getPoint().equals(((WayPoint) obj).getPoint());
	}
   
   public int hashCode() {
      return this.getPoint().hashCode();
   }

}//End Class