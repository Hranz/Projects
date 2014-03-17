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
import java.awt.Point;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Stack;
import java.util.List;
import java.util.HashMap;
import java.util.PriorityQueue;

public class PlayerBot extends Bot {
   
   private int lastX, lastY, lastHeight, strength, wealth;
   private HashMap<Point, WayPoint> map;
   private WayPoint go;
   
   public PlayerBot(String label, Point a, int height, int str, int w) {
      super(label, a, Color.RED);
      strength = str;
      wealth = w;
      lastX = (int)a.getX();
		lastY = (int)a.getY();
      lastHeight = height;
   }//End PlayerBot() Constructor
   
   public PlayerBot(String label, Point a, int height) {
      super(label, a, Color.RED);
      strength = 2000;
      wealth = 1000;
      lastX = (int)a.getX();
		lastY = (int)a.getY();
      lastHeight = height;
   }//End PlayerBot() Constructor
   
   public void gainMap(HashMap<Point, WayPoint> map) {
      this.map = map;
   }//End gainMap() method
   
   public int getWealth() {
      return wealth;
   }//End getWealth() method
   
   public int getStrength() {
      return strength;
   }//End getStrength() method
   
   public void setWealth(int wealth) {
      this.wealth = wealth;
   }//End setWealth() method
   
   public void setStrength(int strength) {
      this.strength = strength;
   }//End setStrength() method
   
   public void checkNode(WayPoint aWayPoint) {
   
   }//End checkNode() method
   
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
      strength -= distanceOfMove;
      //Sets current location of the "bot"
		lastX = newX;
	   lastY = newY;
      lastHeight = height;
   }
   
   //Handles the simple movement between WayPoints
//    public void simpleMove() {
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
//    }//End simpleMove() method
   
   //Handles A* algorithm
   public List<WayPoint> aStarMove(Point dest, List<WayPoint> list) {
      //Make into a class?
      PriorityQueue<Node> openSet = new PriorityQueue(20, new NodeComparator());
      ArrayList<Node> closedSet = new ArrayList<Node>();
      ArrayList<Point> tempNeigh;
      Node in;
      boolean goal = false;
      
      //First Node in openSet
      Node node = new Node(this.getPoint(), 0, this.getPoint().distance(dest), null);
      openSet.add(node);
      WayPoint aWayPoint = map.get(this.getPoint());
      
      while (openSet.size() > 0) {
         //add point neighbors to tempNeigh and add to list if they aren't in the openSet or closedSet
         
         if (node.getPoint().equals(dest)) {
            goal = true;
            closedSet.add(node);
            openSet.clear();
            //break;
         } else {
            double dist;
            tempNeigh = aWayPoint.getNeigh(); //ArrayList of points
            if (tempNeigh.size() > 0) { 
               for (int i = 0; i < aWayPoint.getNeighbors(); i++) {
                  in = new Node(tempNeigh.get(i), node.getDist() +
                     node.getPoint().distance(tempNeigh.get(i)), 
                     tempNeigh.get(i).distance(dest), node);
                  if (!(openSet.contains(in) || closedSet.contains(in)) && in != null) {
                     openSet.add(in);
                     in.makeWhite();
                     //AnimatePanel.addTemporaryDrawable(new Marker(tempNeigh.get(i), Color.WHITE, 3));
                     //AnimatePanel.repaint();
                  }
               }//End for
               openSet.remove(node);
               closedSet.add(node);
               node = openSet.peek();
               aWayPoint = map.get(node.getPoint());
            }
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
