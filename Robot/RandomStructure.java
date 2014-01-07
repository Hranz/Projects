/*

Programmers: Kris Larson

Description: A memory structure for the robot. It's
   retrieve method can return a random memory node
   from the learned energy sources to the robot.

*/

import java.awt.Point;
import java.util.*;

public class RandomStructure<E extends Memory> implements RobotMemory<E>{
   public LinkedList<E> list = new LinkedList<E>();
   boolean x, y;
	
   public int forget(E episode) {
      for (int i = 0; i < list.size(); i++) {
         x = list.get(i).getFoodLocation().getX() == episode.getFoodLocation().getX();
         y = list.get(i).getFoodLocation().getX() == episode.getFoodLocation().getX();
         if(x && y){
            list.remove(i);
   		   return list.size();
         }
      }
   
		list.remove(episode);
		return list.size();
	}

	public int learn(E episode) {
      int loop = list.size();
      if (list.isEmpty()) {
         list.add(episode);
         return list.size();
      }
      else {
         for (int i = 0; i < loop; i++) {
            x = list.get(i).getFoodLocation().getX() == episode.getFoodLocation().getX();
            y = list.get(i).getFoodLocation().getY() == episode.getFoodLocation().getY();
            if(x && y){
      		   return list.size();
            }
         }
           list.add(episode);
      }
      return list.size();
	}

	public E retrieve(double probability, Point robotPosition) {
		Random gen = new Random();
      double i = gen.nextDouble();
      
      if (!list.isEmpty()) {   
   		if (i <= probability) {
   			return list.get(gen.nextInt(list.size())); //Returns Memory Object. 
   		}
      }
		return null;
	}
}
