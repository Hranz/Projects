/*
   Programmers: Kristoffer Larson, Josue Ruiz
   Date: March 28, 2014
   
   Description: 
      PlayerBot is a child of Bot. It travels between WayPoints
      and loses strength as it moves. It will gain strength at
      City WayPoints by paying gold, and gold at Gold WayPoints.
      If it finds a map at a Map WayPoint, it go to the coordinates
      given and then repaths back to it's original destination. A
      PlayerBot has it's own method for pathing (A*).
*/

import SimulationFramework.Bot;
import SimulationFramework.*;
import java.awt.Point;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Stack;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Iterator;

public class PlayerBot extends Bot {
   
   private int id, str, oldX, oldY;
   private Point goal;
   private Stack<WayPoint> path;
   private boolean isPlaying, hasMap;
   private HashMap<Point, WayPoint> map;
   private AccessDB adb;
   
   //if the player isn't pathing to dest, don't pick up anything 
   public PlayerBot(int id, Point place, Point goal, int str,
      Stack<WayPoint> path, Color c, AccessDB adb) {
      
      super(""+id, place, c);
      this.id = id;
      this.str = str;
      oldX = (int)place.getX();
      oldY = (int)place.getY();
      this.path = path;
      this.goal = goal;
      this.adb = adb;
      hasMap = false;
      isPlaying = true;
   }//End PlayerBot() Constructor
   
   public void gainMap(HashMap<Point, WayPoint> map) {
      this.map = map;
   }//End gainMap() method
   
   public int getID() {
      return id;
   }//End getID
   
   public int getStrength() {
      return str;
   }//End getStrength() method
   
   public Point getGoal() {
      return goal;
   }//End getDest() method
   
   public Stack<WayPoint> getPath() {
      return path;
   }//End getPath
   
   public boolean hasMap() {
      return hasMap;
   }//End hasMap
   
   public void setWealth(int w) {
      adb.setPlayerWealth(id, w);
   }//End setWealth() method
   
   public void setStrength(int str) {
      this.str = str;
   }//End setStrength() method
   
   public void setDest(Point dest) {
      this.goal = goal;
   }//End setDest() method
   
   public void setHasMap() {
      hasMap = !hasMap;
   }//End setHasMap
   
   public boolean isPlaying() {
      return isPlaying;
   }
   
   public void stopPlaying() {
      isPlaying = false;
      int w = adb.getPlayerWealth(id);
      adb.setPlayerWealth(id, w + str);
      str = 0;
   }//End stopPlaying
   
   public void reset() {
      super.reset();
      oldX = (int) point.getX(); 
      oldY = (int) point.getY();
   }//End reset() method
   
   public void move() {
      int oldH, newX, newY, newH;
      WayPoint old = map.get(new Point(oldX, oldY));
      WayPoint go = path.pop();
      
      oldH = old.getHeight();
      newX = go.getWX();
      newY = go.getWY();
      newH = go.getHeight();
   
      int distanceOfMove = (int)(Math.sqrt(Math.pow(newX - oldX, 2)
         + Math.pow(newY - oldY, 2) + Math.pow(newH - oldH, 2)));
   	
      moveTo(newX, newY);
      //Reduces the strength of the PlayerBot
      str -= distanceOfMove;
      //Set new location of PlayerBot
      oldX = newX;
      oldY = newY;
      adb.setPlayerLocation(id, new Point(oldX, oldY));
   }
   
   //Handles A* algorithm
   public void aStarMove(Point dest) {
      PriorityQueue<Node> openSet = new PriorityQueue<Node>(20, new NodeComparator());
      HashSet<Node> closedSet = new HashSet<Node>();
      ArrayList<Point> tempNeigh;
      Node in;
      boolean goal = false, contains = false;
      
      //First Node in openSet
      Node node = new Node(this.getPoint(), 0, this.getPoint().distance(dest), null);
      openSet.add(node);
      WayPoint aWayPoint = map.get(this.getPoint());
      
      while (openSet.size() > 0) {
         //add point neighbors to tempNeigh and add to list if they aren't in the openSet or closedSet
         
         if (node.getPoint().equals(dest)) {
            goal = true;
            openSet.clear();
         } 
         else {
            double dist;
            tempNeigh = aWayPoint.getNeigh(); //ArrayList of points
            if (tempNeigh.size() > 0) { 
               for (int i = 0; i < aWayPoint.getNeighbors(); i++) {
                  in = new Node(tempNeigh.get(i), node.getDist() +
                     node.getPoint().distance(tempNeigh.get(i)), 
                     tempNeigh.get(i).distance(dest), node);
                  
                  if (!openSet.contains(in) && !closedSet.contains(in)) {
                     openSet.add(in);
                  }
               }//End for
            } else { 
               path = null; 
               return;
            }//End if
            
            openSet.remove(node);
            closedSet.add(node);
               
            node = openSet.peek();
            if (node != null)
               aWayPoint = map.get(node.getPoint());
            else {
               path = null;
               return;
            }
         }//End if
      }//End while
      
      //Build path 
      if (goal) {
         Stack<WayPoint> list = new Stack<WayPoint>();
         list.push(map.get(node.getPoint()));
         
         while (node != null) {
            list.push(map.get(node.getPoint()));
            node = node.getPrev();
         }//End while
         list.pop();
         path = list;
         return;
      }//End if
      
      path = null;
   }//End aStarMove() method
   
}//End Class
