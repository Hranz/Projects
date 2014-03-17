/*
   Programmers: Kristoffer Larson, Josue Ruiz
   Date:
   
   Description

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
   
