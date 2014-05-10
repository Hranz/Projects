/*
   Programmers: Kristoffer Larson, Josue Ruiz
   Date: March 28, 2014
   
   Description: A comparator class for the PriorityQueue in
      the A* algorithm, inside the PlayerBot class.

*/

import java.util.Comparator;

public class NodeComparator implements Comparator<Node>{
   public int compare(Node one, Node two) {
      if(one.getTotalDist() == two.getTotalDist())
         return 0;
      else if(one.getTotalDist() > two.getTotalDist())
         return 1;
      else
         return -1;
   }//End compare() method
}//End NodeComparator class
   
