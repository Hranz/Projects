/*

Programmers: Kris Larson

Description: An interface for the Memory structures so 
   they can be implemented into the Robot class more 
   smoothly. This contains the basic methods for each
   memory structure: forget, learn, and retrieve.

*/

import java.awt.Point;

public interface RobotMemory<Memory> {
	public int forget(Memory episode); 
   	
	public int learn(Memory episode);
	
	public Memory retrieve(double probability, Point robotPosition);
}
