/*
   Programmers: Kristoffer Larson, Josue Ruiz
   Date:
   
   Description

*/

import SimulationFramework.Marker;
import java.awt.Point;
import java.awt.Color;

public class Node {
   
   private double g, totalDist;
   private Node prev;
   private Point p;
   
   public Node(Point p, double g, double h, Node prev) {
      //p is current the current Node being evaluated
      //c is white and size 3 for open set, black and size 2 for closed set
      //super(p, Color.WHITE, 3);
      this.p = p;
      this.g = g;
      totalDist = g + h;
      this.prev = prev;
   }//End Node() constructor
   
   public Point getPoint() {
      return p;
   }//End getPoint() method
   
   //The distance traveled.
   public double getDist() {
      return g;
   }//End getDist() method
   
   //The distance traveled + the heuristic distance to the end.
   public double getTotalDist() {
      return totalDist;
   }//End getTotalDist() method
   
   public Node getPrev() {
      return prev;
   }//End getPrev() method
   
}//End class