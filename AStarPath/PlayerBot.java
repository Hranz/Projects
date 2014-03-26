/*
   Programmers: Kristoffer Larson, Josue Ruiz
   Date: , 2014
   
   Description: 
      PlayerBot is a child of Bot. It travels between WayPoints
      and loses strength as it moves. It will gain strength at
      City WayPoints by paying gold, and gold at Gold WayPoints.
      Map WayPoints aren't handled in this project.
*/

import SimulationFramework.Bot;
import SimulationFramework.Marker;
import SimulationFramework.AnimatePanel;
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
   
   private int lastX, lastY, lastHeight, str, w;
   private Point dest;
   private boolean hasTMap;
   private HashMap<Point, WayPoint> map;
   private WayPoint go;
   private AnimatePanel animatePanel;
   private SimFrame sf;
   
   public PlayerBot(String label, Point a, int height, int str, int w, Point dest,
      AnimatePanel animatePanel) {
      
      super(label, a, Color.RED);
      this.str = str;
      this.w = w;
      lastX = (int)a.getX();
      lastY = (int)a.getY();
      lastHeight = height;
      hasTMap = false;
      this.dest = dest;
      this.animatePanel = animatePanel;
   }//End PlayerBot() Constructor
   
   public PlayerBot(String label, Point a, int height, Point dest, AnimatePanel animatePanel) {
      super(label, a, Color.RED);
      str = 2000;
      w = 1000;
      lastX = (int)a.getX();
      lastY = (int)a.getY();
      lastHeight = height;
      hasTMap = false;
      this.dest = dest;
      this.animatePanel = animatePanel;
   }//End PlayerBot() Constructor
   
   public void gainMap(HashMap<Point, WayPoint> map) {
      this.map = map;
   }//End gainMap() method
   
   public int getWealth() {
      return w;
   }//End getWealth() method
   
   public int getStrength() {
      return str;
   }//End getStrength() method
   
   public boolean getTMap() {
      return hasTMap;
   }//End getTMap() method
   
   public Point getDest() {
      return dest;
   }//End getDest() method
   
   public void setWealth(int w) {
      this.w = w;
   }//End setWealth() method
   
   public void setStrength(int str) {
      this.str = str;
   }//End setStrength() method
   
   public void setTMap(boolean has) {
      hasTMap = has;
   }//End getTMap() method
   
   public void setDest(Point dest) {
      this.dest = dest;
   }
   
   public void gainFrame(SimFrame s) {
      sf = s;
   }
   
   public void reset() {
      super.reset();
      lastX = (int) point.getX(); 
      lastY = (int) point.getY();
   }//End reset() method
   
   public void setMove(WayPoint go) {
      this.go = go;
   }//End setMove() method
   
   public void move() {
      int newX, newY, height;
      
      newX = go.getWX();
      newY = go.getWY();
      height = go.getHeight();
   
      int distanceOfMove = (int)(Math.sqrt(Math.pow(newX - lastX, 2)
         + Math.pow(newY - lastY, 2) + Math.pow(lastHeight - height, 2)));
   	
      moveTo(newX, newY);
      //Reduces the strength of the "bot"
      str -= distanceOfMove;
      //Sets current location of the "bot"
      lastX = newX;
      lastY = newY;
      lastHeight = height;
   }
   
   //Handles the simple movement between WayPoints
   public void simpleMove() {
   //       if (!moves.isEmpty()) {
   //             //Find closest WayPoint to destination
   //             oldDist = 200000.0;
   //             for (int i = 0; i < moves.size(); i++) {
   //                aWayPoint = moves.get(i);
   //                dist = Math.sqrt(Math.pow(((int)dest.getY() - aWayPoint.getWY()), 2) 
   //                   + Math.pow(((int)dest.getX() - aWayPoint.getWX()), 2) 
   //                   + Math.pow(destH - aWayPoint.getHeight(), 2));
   //                   
   //                if (dist < oldDist) {
   //                   oldDist = dist;
   //                   temp = new Point(aWayPoint.getWX(), aWayPoint.getWY());
   //                }//End if
   //             }//End for
   //             
   //             if (temp != null) {
   //                aBot.setMove(map.get(temp));
   //                aBot.move();//Bot moves and reduces strength
   //             }//End if
   //       }//End if
   }//End simpleMove() method
   
   private void waitT() {
      try {
         Thread.sleep(10);
      } 
      catch(InterruptedException ex) {
         Thread.currentThread().interrupt();
      }
   }//End wait() method
   
   private boolean setContains(PriorityQueue<Node> openSet, HashSet<Node> closedSet, Node in) {
      Iterator<Node> iterator = openSet.iterator();
      while (iterator.hasNext()) {
         if (iterator.next().getPoint().equals(in.getPoint())) {
            return true;
         }
      }
      
      iterator = closedSet.iterator();
      while (iterator.hasNext()) {
         if (iterator.next().getPoint().equals(in.getPoint())) {
            return true;
         }
      }
      return false;
      
   }//End setContains() method
   
   //Handles A* algorithm
   public List<WayPoint> aStarMove(Point dest, List<WayPoint> list, LarsonRuizTreasurePath app) {
      //Make into a class?
      PriorityQueue<Node> openSet = new PriorityQueue(20, new NodeComparator());
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
            closedSet.add(node);
            animatePanel.addTemporaryDrawable(new Marker(node.getPoint(), Color.BLACK, 2));
            animatePanel.repaint();
            openSet.clear();
            //break;
         } 
         else {
            double dist;
            tempNeigh = aWayPoint.getNeigh(); //ArrayList of points
            if (tempNeigh.size() > 0) { 
               for (int i = 0; i < aWayPoint.getNeighbors(); i++) {
                  in = new Node(tempNeigh.get(i), node.getDist() +
                     node.getPoint().distance(tempNeigh.get(i)), 
                     tempNeigh.get(i).distance(dest), node);
                  
                  contains = setContains(openSet, closedSet, in);
                  
                  //Can't use contains method for PriorityQueue and HashSet. Why?
                  if (!contains) {
                     openSet.add(in);
                     animatePanel.addTemporaryDrawable(new Marker(tempNeigh.get(i), Color.WHITE, 3));
                     //animatePanel.repaint();
                     //waitT();
                     sf.checkStateToWait();
                  }
               }//End for
            }//End if
            else { 
               return null; }
            
            openSet.remove(node);
            closedSet.add(node);
            animatePanel.addTemporaryDrawable(new Marker(node.getPoint(), Color.BLACK, 2));
            //animatePanel.repaint();
            //waitT();
            sf.checkStateToWait();
               
            node = openSet.peek();
            if (node != null)
               aWayPoint = map.get(node.getPoint());
            else 
               return null;
            
         }//End if
      }//End while
      
      //Build path 
      if (goal) {
         Stack<Node> stack = new Stack<Node>();
         
         stack.push(node);
         //aWayPoint = map.get(node.getPrev().getPoint());
         
         while (node != null) {
            stack.push(node);
            node = node.getPrev();
            //aWayPoint = map.get(node.getPoint());
         }//End while
      
            
         while(stack.size() > 0){
            list.add(map.get(stack.pop().getPoint()));
         }
         list.remove(0);//Remove the initial node
         return list;
      
      
      }//End if
      
      return null;
   }//End aStarMove() method
   
}//End Class
