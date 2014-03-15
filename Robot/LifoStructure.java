/*

Programmers: Kris Larson

Description: A memory structure for the robot. The
   learn method will store new energy sources onto
   a stack simulated Linked List. If an energy source 
   that has already been learned is encountered again, 
   it will be brought to the top of the stack. It's 
   retrieve method can return the last energy source 
   it has returned to the robot.

*/

import java.awt.Point;
import java.util.*;

public class LifoStructure<E extends Memory> implements RobotMemory<E>  {
   public Deque<E> list = new LinkedList<E>();
   public Deque<E> list2 = new LinkedList<E>();
   boolean x, y;
   
   public int forget(E episode) { 
		list.removeFirst();
		return list.size();
	}
   
   public int learn(E episode) { //makes use of Deque
      int loop = list.size();
      if (list.isEmpty()) {
         list.addFirst(episode);
         return list.size();
      }
      for (int i = 0; i < loop; i++) {
         x = list.getFirst().getFoodLocation().getX() == episode.getFoodLocation().getX();
         y = list.getFirst().getFoodLocation().getY() == episode.getFoodLocation().getY();
            
         if(x && y) {
            list.removeFirst();
            while (!list2.isEmpty()) {
               list.addFirst(list2.removeFirst());
            }
            list.addFirst(episode);
            return list.size();
         }
         else {
            list2.addFirst(list.removeFirst());
            if (list.isEmpty()) {
               while (!list2.isEmpty()) {
                  list.addFirst(list2.removeFirst());
               }
               list.addFirst(episode);
               return list.size();
            }
         }
      }
      return list.size();
	}

   public E retrieve(double probability, Point robotPosition) {
		Random gen = new Random();
      double i = gen.nextDouble();
      
      if (!list.isEmpty()) {
         if (i <= probability) {       
   			return list.getFirst();
   		}
      }
		return null;
	}
}
