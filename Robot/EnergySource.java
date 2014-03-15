/*

Programmers: Kris Larson

Description: A node class for the construction of 
   energy sources across the "field" the robot travels on. 

*/

import java.awt.Point;
import java.util.LinkedList;

public class EnergySource {
   private Point energyLocation = new Point();
   private int energy;
   
   public EnergySource(Point place) {
      energyLocation = place;
      energy = 125;
   }
   
   public void setEnergy(int newEnergy) {
      energy = newEnergy;
   }
   
   public int getEnergy() {
      return energy;
   }
   
   public Point getEnergyLocation() {
      return energyLocation;
   }
}
