/*Whats the object-oriented way to become wealthy?....Inheritance

Programmers: Kris Larson

Description: The simulation class creates the environment
   that the robot will be in. This includes energy sources
   that the robot can find, the starting point for the robot,
   the type of memory structure the robot will be using, and
   how well the robot can retrieve memory.

*/

import java.util.*;
import java.awt.*;

public class Simulation {
   ArrayList<EnergySource> energyList = new ArrayList<EnergySource>();
   LinkedList<Double> distance = new LinkedList<Double>();
	Robot Rob;
	
	
	public Simulation(double probability) {
      for (int i = 0; i < 4; i++) {
			switch (i) {
				case 0: fifo(probability); 
					break;
				case 1: lifo(probability);
					break;
				case 2: random(probability);
					break;
				case 3: closest(probability);
					break;
			}	
		}
	}
	
	public void fifo(double probability) {
		System.out.println();
      double sum = 0;
		for (int i = 0; i < 20; i++) {
         energyCreation(energyList);
			Rob = new Robot(probability, robotStart(), energyList, 0, distance);
		}
      for(int i = 0; i < distance.size(); i++){
         sum += distance.get(i);
      }
      sum /= distance.size();
      distance.clear();
      System.out.println("Fifo Average Distance for " + probability + " is " + sum);
	}
	
	public void lifo(double probability) {
		System.out.println();
      double sum = 0;
		for (int i = 0; i < 20; i++) {
         energyCreation(energyList);
			Rob = new Robot(probability, robotStart(), energyList, 1, distance);
		}
      for(int i = 0; i < distance.size(); i++){
         sum += distance.get(i);
      }
      sum /= distance.size();
      distance.clear();
      System.out.println("Lifo Average Distance for " + probability + " is " + sum);      
	}
	
	public void random(double probability) {
		System.out.println();
      double sum = 0;
		for (int i = 0; i < 20; i++) {
         energyCreation(energyList);
			Rob = new Robot(probability, robotStart(), energyList, 2, distance);
		}
      for(int i = 0; i < distance.size(); i++){
         sum += distance.get(i);
      }
      sum /= distance.size();
      distance.clear();
      System.out.println("Random Average Distance for " + probability + " is " + sum);
	}
	
	public void closest(double probability) {
		System.out.println();
      double sum = 0;
		for (int i = 0; i < 20; i++) { 
         energyCreation(energyList);
			Rob = new Robot(probability, robotStart(), energyList, 3, distance);
		}
      for(int i = 0; i < distance.size(); i++){
         sum += distance.get(i);
      }
      sum /= distance.size();
      distance.clear();
      System.out.println("Closest Average Distance for " + probability + " is " + sum);
	}
	
	public void energyCreation(ArrayList<EnergySource> energyList) {
		EnergySource e;
		int x, y;
		Point energyPoint;
      Random ran = new Random();
      int i = 0;
      
      if (!energyList.isEmpty())
         energyList.clear(); //reset energy list for later runs
	
		while (energyList.size() < 40) {	
			x = ran.nextInt(200);
			y = ran.nextInt(200);
			energyPoint = new Point(x,y);
			e = new EnergySource(energyPoint);
			
			if (i == 0) {
				energyList.add(e);
				i++;
			}
			
         int num = 1;
			for (int j = 0; j < i; j++) { 
				if (energyList.get(j).getEnergyLocation().distance(e.getEnergyLocation()) < 20.0) {
               num = 0;
				}
			}
         if (num == 1) { 
            if (!energyList.contains(e)) {
                  energyList.add(e);
					   i++;
            }
         }
		}
	}
   
   public void energyRefresh(ArrayList<EnergySource> energyList) {
      for (int i = 0; i < energyList.size(); i++) {
         energyList.get(i).setEnergy(125);
      }
   }
	
	public Point robotStart() {
      Random ran = new Random();
		int x = ran.nextInt(200);
		int y = ran.nextInt(200);
		return new Point(x,y);
	}
	
	public static void main(String[] args) {  
              
      Simulation test1 = new Simulation(.8);
		System.out.print("---------------------------------------");
		Simulation test2 = new Simulation(.6);
 	   System.out.print("---------------------------------------");
		Simulation test3 = new Simulation(.4);
	}
}
