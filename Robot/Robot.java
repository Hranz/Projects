/*

Programmers: Kris Larson

Description: The robot class simulates a robot with 
   memory. It will explore and learn when it has a 
   enough energy and it will try to feed itself when 
   it doesn't have enough energy. If it can't retrieve
   a memory it has previously learned, it will explore
   until it can retrieve the location of an energy source
   When the robot runs out of energy it will become 
   inactive and stop. 

*/

import java.util.*;
import java.awt.*;

public class Robot {
   private double currentRobotE = 100.0;
   private double maxRobotE = currentRobotE;
   private double distanceTraveled = 0;
   private Point currentPosition;
   private double[] place;
   double previousX = 0.0;
	double previousY = 0.0;
	
   public Robot(double probability, Point startPosition, ArrayList<EnergySource> energyArray, int num, LinkedList<Double> distance) {
      currentPosition = startPosition;
      RobotMemory memory;
      
      if (num == 0) //memory structures that are used
         memory = new FifoStructure();
      else if (num == 1)
         memory = new LifoStructure();
      else if (num == 2)
         memory = new RandomStructure();
      else
         memory = new ClosestStructure();
         
      place = new double[3];
      place[0] = 0.0;
      place[1] = 7.0;
      place[2] = -7.0;
      
      for (;;) {
         if (currentRobotE >= .5 * maxRobotE) { 
            explore(place);
            energyDetection(energyArray, memory);
         }
         else if (currentRobotE < 0) {
            inactive(distance);
            return;
         }
         else
            hungry(probability, memory, energyArray, place);
      } //end for loop
   } //end Robot constructor
   
   public void explore(double[] place) {
      double x, y;
      int xMove, yMove;
      Point newPoint;
      
      boolean test = true;
		Random ran = new Random();
      
      while (test) { //loop until successful move
         x = currentPosition.getX(); //resets current position for move.
         y = currentPosition.getY();
         
		   xMove = ran.nextInt(3); //generates a values between 0-2
		   yMove = ran.nextInt(3);
         x += place[xMove];
         y += place[yMove];
			
		   if (previousX != x && previousY != y) { //checks to see if it moved backwards
			   if (x >= 0.0 && y >= 0.0 && x < 200.0 && y < 200.0) { //checks to make sure it's within bounds
               previousX = currentPosition.getX(); 
               previousY = currentPosition.getY(); //store old position
                
               newPoint = new Point((int)x, (int)y); //the point that will be travelled to
               currentRobotE -= currentPosition.distance(newPoint); //remove energy for move
			   	distanceTraveled += currentPosition.distance(newPoint); //add to distanceTraveled
               currentPosition.setLocation(x,y); //go to new location
               test = false; //succesful move accomplished
			   } //end if
		   } //end if
      } //end while loop
	} //end explore
   
   public void hungry(double probability, RobotMemory memory, ArrayList<EnergySource> energyArray, double[] place) {
      Memory mem = (Memory)memory.retrieve(probability, currentPosition); //attempt to retrieve a memory of an energy source
      
      if (mem == null)
        explore(place); 
      else {
        EnergySource e;
        currentRobotE -= currentPosition.distance(mem.getFoodLocation()); //remove energy for the distance the robot is going to travel
        distanceTraveled += currentPosition.distance(mem.getFoodLocation()); //add to distanceTraveled
        currentPosition.setLocation(mem.getFoodLocation()); //travel to food location
        double need = maxRobotE - currentRobotE; //caclulate how much energy the robot needs
        currentRobotE += mem.getFood((int)need); //restore robot's energy
        for (int i = 0; i < energyArray.size(); i++) {
           if (energyArray.get(i).getEnergyLocation().equals(mem.getFoodLocation()))    //find the equivalent energy source in
              energyArray.get(i).setEnergy(energyArray.get(i).getEnergy() - (int)need); //the energyArray and change it's stored energy
        } //end for loop
        if (mem.getFoodInside() == 0) {
           memory.forget(mem); //forget energy source if empty
        } //end if
        mem = null;
      } //end else
   } //end hungry
   
   public void inactive(LinkedList<Double> distance) {
      distance.addLast(distanceTraveled); //adds distanceTraveled to a Linked List to be brought back into the simulator
   } //end inactive
	
   public void energyDetection(ArrayList<EnergySource> energyArray, RobotMemory memory) {
      double detect = 15.0; //detection radius
      for (int i = 0; i < energyArray.size(); i++) { //loops through energyArray
         double dist = currentPosition.distance(energyArray.get(i).getEnergyLocation());
         if (energyArray.get(i).getEnergy() > 0) { //checks to see if there is energy in the energy source
            if (dist <= detect) { //tests if energy sources are within range
               Memory insert = new Memory(energyArray.get(i).getEnergyLocation(), energyArray.get(i).getEnergy());
               memory.learn(insert); //learn energy sources
            } //end if
         } //end if
      } //end for loop
   } //end method energyDetection
} //end class Robot
