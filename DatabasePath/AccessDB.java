/*
   Programmers: Kristoffer Larson, Josue Ruiz
   Date: May 8, 2014
   Description: Creates a connection and deals with retrieving and
      modifying information from the database.
*/

import java.sql.*;
import java.util.*;
import java.awt.*;
import java.lang.reflect.Field;

public class AccessDB {
   Connection connect = null;  // connection to database
   //Statement stmt = null;      // a SQL statement
   ResultSet rs = null;        // values returned from SQL query
   String sql = null;          // string for the sql statement
   
   //WayPoint Tables
   public AccessDB() {
      try {
         Class.forName("org.sqlite.JDBC");
         connect = DriverManager.getConnection("jdbc:sqlite:LarsonRuiz.db");
         System.out.println("Opened database successfully");  
         // Statement stmt = connect.createStatement();
      } 
      catch ( Exception e ) {
         System.err.println( e.getClass().getName() + "" + e.getMessage() );
         System.exit(0);
      }      
   }//End DB constructor
   
   public boolean exists(String table) {
      try {
         Statement stmt = connect.createStatement();
         long size = stmt.executeQuery("SELECT count(*) FROM " + table).getLong(1);
         System.out.println("Exist" + size);
         stmt.close();
         if (size > 1) {
            return true;
         }
         else {
            return false;
         }
      } 
      catch (SQLException e) {
         System.out.println("Doesn't exist");
         return false;
      }
   }//End exists
   
   public void disconnect() {
      endConnection();
   }//End disconnect
   
   public int getNumPlayers() {
      return getNumPlayers("iPlayer");
   }//End getNumPlayers
   
   public Point getPlayerStart(int id) {
      return getPlayerStart(id, "iPlayer"); //returns new Point(startX, startY);
   }//End getPlayerStart
   
   public Point getPlayerDest(int id) {
      return getPlayerDest(id, "iPlayer"); //returns new Point(destX, destY);
   }//End getPlayerDest
   
   public int getPlayerWealth(int id) {
      return getPlayerWealth(id, "gPlayer");
   }//End getPlayerWealth
   
   public int getPlayerStrength(int id) {
      return getPlayerStrength(id, "iPlayer");
   }//End getPlayerStrength
   
   public Color getPlayerColor(int id) {
      return getPlayerColor(id, "iPlayer");
   }//End getPlayerColor
   
   public int getPlayerConflict(int id) {
      return getPlayerConflict(id, "gPlayer");
   }//End getPlayerConflict
   
   public int getTreasure(Point place) {
      int g = getTreasure(place, "gTreasure");
      if (g > 0) {
         return g;
      }
      else
         return 0;
   }//End getTreasure
   
   public Point getMap(Point place) {
      Point p = getMap(place, "gMap");
      if (p != null) {
         return p;
      }
      else
         return null;
   }//End getMap
   
   public int getCity(Point place) {
      int c = getCity(place, "gCity");
      if (c > 0)
         return c;
      else
         return 0;
   }//End getCity
   
   public void setPlayerWealth(int id, int w) {
      setPlayerWealth(id, w, "gPlayer");
   }//End setPlayerWealth
   
   public void setPlayerLocation(int id, Point p) {
      setPlayerLocation(id, p, "gPlayer");
   }//End setPlayerLocation
   
   public void setTreasure(Point place) {
      setTreasure(place, "gTreasure");
      //set value to 0
   }//End setTreasure
   
   public void setMap(Point place) {
      setMap(place, "gMap");
      //set tPlace to ""
   }//End setMap
   
   public int[] wealthiestPlayers() {
      return wealthiestPlayers("gPlayer");
   }//End wealthiestPlayers
   
   private void endConnection() {
      try {
         rs.close();
         //stmt.close();
         connect.close();
      } 
      catch ( Exception e ) {
         System.err.println( e.getClass().getName() + "" + e.getMessage() );
         System.exit(0);
      } 
   }//End endConnection
   
   private int getNumPlayers(String table) {
      try {
         Statement stmt = connect.createStatement();
         int size = stmt.executeQuery("SELECT count(*) FROM " + table).getInt(1);//if type error change from int to long
         stmt.close();
         return size;
      } 
      catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ". attempt to retrieve number of players: " + e.getMessage() );
         return 0;
      }
   }//End getNumPlayers
   
   private Point getPlayerStart(int id, String table) {
      try {
         Statement stmt = connect.createStatement();
         int startX = stmt.executeQuery("SELECT StartX FROM " + table + " WHERE ID = " + id + ";").getInt(1);
         int startY = stmt.executeQuery("SELECT StartY FROM " + table + " WHERE ID = " + id + ";").getInt(1);
         stmt.close();
         return new Point(startX, startY);
      }
      catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ". attempt to retrieve start point of a player: " + e.getMessage() );
         return null;
      }
   }//End getPlayerStart
   
   private Point getPlayerDest(int id, String table) {
      try {
         Statement stmt = connect.createStatement();
         int goalX = stmt.executeQuery("SELECT GoalX FROM " + table + " WHERE ID = " + id + ";").getInt(1);
         int goalY = stmt.executeQuery("SELECT GoalY FROM " + table + " WHERE ID = " + id + ";").getInt(1);
         stmt.close();
         return new Point(goalX, goalY);
      }
      catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ". attempt to retrieve end point of a player: " + e.getMessage() );
         return null;
      }
   }//End getPlayerDest
   
   private int getPlayerWealth(int id, String table) {
      try {
         Statement stmt = connect.createStatement();
         int wealth = stmt.executeQuery("SELECT Wealth FROM " + table + " WHERE ID = " + id + ";").getInt(1);
         stmt.close();
         return wealth;
      }
      catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ". attempt to retrieve wealth of a player: " + e.getMessage() );
         return -1;
      }
   }//End getPlayerWealth
   
   private int getPlayerStrength(int id, String table) {
      try {
         Statement stmt = connect.createStatement();
         int str = stmt.executeQuery("SELECT Strength FROM " + table + " WHERE ID = " + id + ";").getInt(1);
         stmt.close();
         return str;
      }
      catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ". attempt to retrieve strength of a player: " + e.getMessage() );
         return -1;
      }
   }//End getPlayerStrength
   
   private Color getPlayerColor(int id, String table) {
      try {
         Statement stmt = connect.createStatement();
         String c = stmt.executeQuery("SELECT Color FROM " + table + " WHERE ID = " + id +";").getString(1);
         
         Color color;
         Field field = Class.forName("java.awt.Color").getField(c.toLowerCase()); // toLowerCase because the color fields are RED or red, not Red
         color = (Color)field.get(null);
         stmt.close();
         return color;
      }
      catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ". attempt to retrieve color of a player: " + e.getMessage() );
         return null;
      }
   }//End getPlayerColor
   
   //returns the id of a player at the the same location as the first player
   // if there are more than 2 at a location, the one with the highest wealth will
   // be returned.
   private int getPlayerConflict(int id, String table) {
      try {
         Statement stmt = connect.createStatement();
         ArrayList<Integer> idMatch = new ArrayList<Integer>();
         
         String place = stmt.executeQuery("SELECT Place FROM " + table + " WHERE ID = " + id + ";").getString(1);
         rs = stmt.executeQuery("SELECT ID FROM " + table + " WHERE Place = '" + place + "' AND ID <> " + id + ";");
         
         while (rs.next()) {
            idMatch.add(rs.getInt("ID"));
         }
         int size = idMatch.size();
         
         //more than two players at the same place
         if (size > 1) {
            int max = stmt.executeQuery("SELECT Wealth FROM " + table + " WHERE ID = " + idMatch.get(0) + ";").getInt(1);
            int index = 0, temp;
            //find who has the highest wealth
            for (int i = 1; i < size; i++) {
               temp = stmt.executeQuery("SELECT Wealth FROM " + table + " WHERE ID = " + idMatch.get(i) + ";").getInt(1);
               if (temp > max) {
                  max = temp;
                  index = i;
               }
            }//End for loop
            stmt.close();
            //returns the id of the player with the highest wealth
            return idMatch.get(index);
         }
         else {
            stmt.close();
            id = idMatch.get(0);
            //return id of the only other player at that location
            return id;
         }
         
      }
      catch ( Exception e ) {
         //System.err.println( e.getClass().getName() + ". attempt to find conflict: " + e.getMessage() );
         return id;
      }
   }//End getPlayerConflict
   
   private int getTreasure(Point place, String table) {
      try {
         Statement stmt = connect.createStatement();
         String p = "" + (int)place.getX() + "," + (int)place.getY();
         int g = stmt.executeQuery("SELECT Gold FROM " + table + " WHERE Place = '" + p + "';").getInt(1);
         stmt.close();
         return g;
      }
      catch ( Exception e ) {
         //System.err.println( e.getClass().getName() + ". attempt to treasure from table: " + e.getMessage() );
         return 0;
      }
   }//End getTreasure
   
   private Point getMap(Point place, String table) {
      try {
         Statement stmt = connect.createStatement();
         String p = "" + (int)place.getX() + "," + (int)place.getY();
         String s = stmt.executeQuery("SELECT TreasurePlace FROM " + table + " WHERE Place = '" + p + "';").getString(1);
         String [] tp = s.split(",");
         stmt.close();
         return new Point(Integer.parseInt(tp[0]), Integer.parseInt(tp[1]));
      }
      catch ( Exception e ) {
         //System.err.println( e.getClass().getName() + ". attempt to retrieve map from table: " + e.getMessage() );
         return null;
      }
   }//End getMap
   
   private int getCity(Point place, String table) {
      try {
         Statement stmt = connect.createStatement();
         String p = "" + (int)place.getX() + "," + (int)place.getY();
         int c = stmt.executeQuery("SELECT Cost FROM " + table + " WHERE Place = '" + p + "';").getInt(1);
         stmt.close();
         return c;
      }
      catch ( Exception e ) {
         //System.err.println( e.getClass().getName() + ". attempt to retrieve city from table: " + e.getMessage() );
         return 0;
      }
   }//End getCity
   
   private void setPlayerWealth(int id, int w, String table) {
      try {
         Statement stmt = connect.createStatement();
         sql = "UPDATE " + table + " SET Wealth = " + w + " WHERE ID = " + id + ";";
         stmt.executeUpdate(sql);
         //connect.commit();
         stmt.close();
      }
      catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ". attempt to set player wealth: " + e.getMessage() );
      }
   }//End setPlayerWealth
   
   private void setPlayerLocation(int id, Point p, String table) {
      try {
         Statement stmt = connect.createStatement();
         String place = "" + (int)p.getX() + "," + (int)p.getY();
         sql = "UPDATE " + table + " SET Place = '" + place + "' WHERE ID = " + id + ";";
         stmt.executeUpdate(sql);
         //connect.commit();
         stmt.close();
      }
      catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ". attempt to set player location: " + e.getMessage() );
      }
   }//End setPlayerLocation
   
   private void setTreasure(Point place, String table) {
      try {
         Statement stmt = connect.createStatement();
         String p = "" + (int)place.getX() + "," + (int)place.getY();
         sql = "UPDATE " + table + " SET Gold = " + 0 + " WHERE Place = '" + p + "';";
         stmt.executeUpdate(sql);
         //connect.commit();
         stmt.close();
      }
      catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ". attempt to set treasure to 0: " + e.getMessage() );
      }
   }//End setTreasure
   
   private void setMap(Point place, String table) {
      try {
         Statement stmt = connect.createStatement();
         String p = "" + (int)place.getX() + "," + (int)place.getY();
         sql = "UPDATE " + table + " SET TreasurePlace = " + null + " WHERE Place = '" + p + "';";
         stmt.executeUpdate(sql);
         //connect.commit();
         stmt.close();
      }
      catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ". attempt to set map to null: " + e.getMessage() );
      }
   }//End setMap
   
   private int[] wealthiestPlayers(String table) {
      try {
         Statement stmt = connect.createStatement();
         int[] rank = new int[getNumPlayers()];
         int i = 0;
         rs = stmt.executeQuery("SELECT * FROM " + table + " ORDER BY Wealth DESC;");
         
         while (rs.next()) {
            rank[i] = rs.getInt("ID");
            i++;
         }
         
         stmt.close();
         return rank;
      }
      catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ". attempt to sort players by wealth: " + e.getMessage() );
         return null;
      }
   }//End wealthiestPlayers

}//End AccessDB class
