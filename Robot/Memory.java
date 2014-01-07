/*

Programmers: Kris Larson

Description: A node class to create memory for storage in
   memory structures. This is separated from energy sources
   which are not stored in the robot's memory.

*/

import java.awt.*;

public class Memory {
   private Point foodLocation;
   private double foodAmount;
   
   public Memory(Point place, double amount) {
      foodLocation = place;
      foodAmount = amount;
   } 
   
   public double getFood (double need) {
      if (foodAmount > need) {
         foodAmount -= need;
         return need;
      }
      else {
         double food = foodAmount;
         foodAmount = 0;
         return food;
      }
   }
   
   public Point getFoodLocation() {
      return foodLocation;
   }  
	
	public double getFoodInside() {
		return foodAmount;
	}
}
