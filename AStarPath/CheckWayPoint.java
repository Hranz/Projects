/*
   Programmer: Kristoffer Larson
   Date: March , 2014
   
   Description: Checks the WayPoint for the PlayerBot
*/

import SimulationFramework.*;
import java.awt.Point;

public class CheckWayPoint {

   PlayerBot aBot;
   SimFrame s;
   String status;

   public CheckWayPoint(PlayerBot aBot, WayPoint aWayPoint, SimFrame s) {
      this.aBot = aBot;
      this.s = s;
      
      if (aWayPoint.getCityValue() > 0) {
         cityWP(aWayPoint);
      } else if (aWayPoint.getGold() > 0) {
         goldWP(aWayPoint);
      } else if ((aWayPoint.getMapX() > 0 || aWayPoint.getMapY() > 0)) {
         mapWP(aWayPoint);
      } else {
         normalWP(aWayPoint);
      }
   
   }//End SpotCheck() Constructor
   
   public void cityWP(WayPoint aWayPoint) {
      status = "City (" + (int)aWayPoint.getWX() + ", " + (int)aWayPoint.getWY() + ") $ " +
               aWayPoint.getCityValue() + ", Player " + aBot.getStrength() + " $ " +
               aBot.getWealth();
      s.statusReport(status);
   }
   
   public void goldWP(WayPoint aWayPoint) {
      status = "Gold (" + (int)aWayPoint.getWX() + ", " + (int)aWayPoint.getWY() + ") $ " +
               aWayPoint.getGold() + ", Player " + aBot.getStrength() + " $ " +
               aBot.getWealth();
      s.statusReport(status);
   }
   
   public void mapWP(WayPoint aWayPoint) {
      status = "Map (" + (int)aWayPoint.getWX() + ", " + (int)aWayPoint.getWY() + 
               ") Treasure (" + aWayPoint.getMapX() + ", " + aWayPoint.getMapY() 
               + ") Player " + aBot.getStrength() + " $ " + aBot.getWealth();
      s.statusReport(status);
   }
   
   public void normalWP(WayPoint aWayPoint) {
      status = "WayPoint (" + (int)aWayPoint.getWX() + ", " + (int)aWayPoint.getWX() +
               "), Player " + aBot.getStrength() + " $ " + aBot.getWealth();
      s.statusReport(status);
   }

}
