/*
   Programmers: Kristoffer Larson, Josue Ruiz
   Date:
   
   Description

*/

import SimulationFramework.*;
import java.awt.Point;
import java.awt.Color;

public class Node implements Comparable<Node> {
   
   private double g, totalDist;
   private Node prev;
   private Point p;
   
   public Node(Point p, double g, double h, Node prev) {
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
   
   public int compareTo(Node n1) {
      if (this.p.equals(n1.p)) {
         return 0;
      } else {
         return -1;
      }//End if

   }//End compareTo() method
      
   public boolean equals(Object obj) {
		if (!(obj instanceof Node))
			return false;	
		if (obj == this)
			return true;
		return this.p.equals(((Node) obj).p);
	}
   
   public int hashCode() {
      return this.p.hashCode();
   }
   
}//End class