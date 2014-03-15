/*

Programmers: Kris Larson

Description: A memory structure for the robot. The
   learn method will store new energy sources onto
   a queue designed Linked List. If an energy source 
   that has already been learned is encountered again, 
   it will be brought to the end of the queue. It's 
   retrieve method can return the last energy source 
   it has returned to the robot.
   
*/

import java.awt.Point;
import java.util.*;

public class FifoStructure<E extends Memory> implements RobotMemory<E>  {
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
         list.addLast(episode);
         return list.size();
      }
      for (int i = 0; i < loop; i++) {
         x = list.getLast().getFoodLocation().getX() == episode.getFoodLocation().getX();
         y = list.getLast().getFoodLocation().getY() == episode.getFoodLocation().getY();
            
         if(x && y) {
            list.removeLast();
            while (!list2.isEmpty()) {
               list.addLast(list2.removeLast());
            }
            list.addLast(episode);
            return list.size();
         }
         else {
            list2.addLast(list.removeLast());
            if (list.isEmpty()) {
               while (!list2.isEmpty()) {
                  list.addLast(list2.removeLast());
               }
               list.addLast(episode);
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
            E item = list.getFirst();
   			return item;		
   	   }
      }
		return null;
	}
}
