/*
   Programmers: Kristoffer Larson, Josue Ruiz
   Date: May 8, 2014
   Description: Sets up and initializes the database for use.
      
*/

import java.sql.*;
import java.util.*;
import java.awt.*;

public class MakeDB {
   Connection connect = null;  // connection to database
   Statement stmt = null;      // a SQL statement
   ResultSet rs = null;        // values returned from SQL query
   String sql = null;          // string for the sql statement
   
   //WayPoint Tables
   public MakeDB(HashMap<Point, WayPoint> map) {
      try {
         Class.forName("org.sqlite.JDBC");
         connect = DriverManager.getConnection("jdbc:sqlite:LarsonRuiz.db");
         System.out.println("Opened database successfully");  
         //connect.setAutoCommit(false);
         stmt = connect.createStatement();
      } 
      catch ( Exception e ) {
         System.err.println( e.getClass().getName() + "" + e.getMessage() );
         System.exit(0);
      }    
      if (!exists("iPlayer")){
         playerTable();
      }
      if (!exists("iTreasure") || !exists("iCity") || !exists("iMap"))
         pointTables(map);
         
      gameTables();
      //gamePTable();
      
      try {
         rs.close();
         stmt.close();
         connect.close();
      } 
      catch ( Exception e ) {
         System.err.println( e.getClass().getName() + "" + e.getMessage() );
         System.exit(0);
      }    
      
   }//End DB constructor
   
   public boolean exists(String table) {
      try {
         int size = stmt.executeQuery("SELECT count(*) FROM " + table + ";").getInt(1);
         System.out.println("Exist" + size);
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
   
   private void gameTables() {
      gamePTable();
      gameWPTables();
   }//End gameTables
   
   private void gamePTable() {
      try {
         // create gPlayer table
         sql = "DROP TABLE IF EXISTS gPlayer";
         stmt.executeUpdate(sql); 
         sql = "CREATE TABLE IF NOT EXISTS gPlayer " +
                "(ID varchar(10) primary key  not null," +
                " Place varchar(10)," +
                " Wealth int not null);";
         stmt.executeUpdate(sql);
         int size = stmt.executeQuery("SELECT count(*) FROM iPlayer;").getInt(1);
         
         String[] s = new String[size];
         rs = stmt.executeQuery( "SELECT * FROM iPlayer;" );
         while (rs.next()) {
            int id = rs.getInt("ID");
            int x = rs.getInt("StartX");
            int y  = rs.getInt("StartY");
            int wealth = rs.getInt("Wealth");
            String place = "" + x + "," + y;
            sql = String.format("INSERT INTO gPlayer (ID,Place,Wealth) VALUES (%d,'%s',%d);",
              id, place, wealth);
            s[id - 1] = sql;
         }//Display this table to make sure it has all 4 players in it.
         
         for (int i = 0; i < s.length; i++) {
            sql = s[i];
            stmt.executeUpdate(sql);
         }
         
         //connect.commit();
         System.out.println("Defined gPlayer Table");
      }
      catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ". attempt to create table: " + e.getMessage() );
         System.exit(0);
      }
      System.out.println("gPlayer Table created and inserted");
   }//End gamePTable
   
   private void gameWPTables() {
      try {
         stmt.executeUpdate("DROP TABLE IF EXISTS gTreasure;");
         stmt.executeUpdate("DROP TABLE IF EXISTS gCity;");
         stmt.executeUpdate("DROP TABLE IF EXISTS gMap;");
         
         //create gTreasure
         System.out.println("Created gTreasure table");
         sql = "CREATE TABLE gTreasure AS SELECT * FROM iTreasure;"; 
         stmt.executeUpdate(sql);
         //create gCity
         System.out.println("Created gCity table");
         sql = "CREATE TABLE gCity AS SELECT * FROM iCity;"; 
         stmt.executeUpdate(sql);
         //create gMap
         System.out.println("Created gMap table");
         sql = "CREATE TABLE gMap AS SELECT * FROM iMap;"; 
         stmt.executeUpdate(sql);
      }
      catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ". attempt to create table: " + e.getMessage() );
         System.exit(0);
      }

   }//End gameWPTables
   
   private void playerTable() {
      try {
         // create iPlayer table
         sql = "CREATE TABLE IF NOT EXISTS iPlayer " +
                "(ID varchar(10) primary key  not null," +
                " StartX int not null," +
                " StartY int not null," +
                " GoalX int not null," +
                " GoalY int not null," +
                " Wealth int not null," +
                " Strength int not null," +
                " Color varchar(10) not null);"; 
         stmt.executeUpdate(sql);
         //connect.commit();
         System.out.println("Defined iPlayer Table");
      }
      catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ". attempt to create table: " + e.getMessage() );
         System.exit(0);
      }
      System.out.println("iPlayer Table created and inserted");
      addPlayer();
   }//End playerTable
   
   private void pointTables(HashMap<Point, WayPoint> map) {
      try {
         // create iTreasure table
         stmt.executeUpdate("DROP TABLE IF EXISTS iTreasure;");
         sql = "CREATE TABLE IF NOT EXISTS iTreasure " +
                "(Place varchar(10) primary key  not null," +
                " Gold int not null);"; 
         stmt.executeUpdate(sql);
         stmt.executeUpdate("DROP TABLE IF EXISTS iMap;");
         sql = "create table if not exists iMap " +
                "(Place varchar(10) primary key  not null," +
                " TreasurePlace varchar(10));";
         stmt.executeUpdate(sql);
         // create iCity table
         stmt.executeUpdate("DROP TABLE IF EXISTS iCity;");
         sql = "create table if not exists iCity " +
                "(Place varchar(10) primary key  not null," +
                " Cost int not null);";
         stmt.executeUpdate(sql);
         //connect.commit();
         System.out.println("Defined Initial WayPoint Tables");
      }
      catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ". attempt to create table: " + e.getMessage() );
         System.exit(0);
      }    
      System.out.println("iTreasure Table created and inserted");
      addTreasure(map);
      System.out.println("iMap Table created and inserted");
      addMap(map); 
      System.out.println("iCity Table created and inserted");
      addCity(map);
   }//End pointTables
   
   private void addPlayer() {
      try {
         String sql = "INSERT INTO iPlayer (ID,StartX,StartY,GoalX,GoalY,Wealth,Strength,Color) " +
                   "VALUES (1, 20, 20, 500, 500, 1000, 2000, 'Red' );"; 
         stmt.executeUpdate(sql);
      
         sql = "INSERT INTO iPlayer (ID,StartX,StartY,GoalX,GoalY,Wealth,Strength,Color) " +
            "VALUES (2, 500, 500, 20, 20, 1000, 2000, 'Pink' );"; 
         stmt.executeUpdate(sql);
      
         sql = "INSERT INTO iPlayer (ID,StartX,StartY,GoalX,GoalY,Wealth,Strength,Color) " +
            "VALUES (3, 20, 480, 500, 20, 1000, 2000, 'Orange' );";
         stmt.executeUpdate(sql);
      
         sql = "INSERT INTO iPlayer (ID,StartX,StartY,GoalX,GoalY,Wealth,Strength,Color) " +
            "VALUES (4, 500, 20, 20, 480, 1000, 2000, 'Blue' );"; 
         stmt.executeUpdate(sql);
      
         //connect.commit();
      } 
      catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(0);
      }
   
   }//End addPlayer
   
   private void addTreasure(HashMap<Point, WayPoint> map) {
      Iterator<WayPoint> it = map.values().iterator();
      WayPoint wp; String place;
      try { 
         while(it.hasNext()) {
            wp = it.next();
            if (wp.getColor().equals(Color.YELLOW)) {
               place = "" + (int)wp.getWX() + "," + (int)wp.getWY();
               sql = String.format("INSERT INTO iTreasure (Place, Gold) VALUES ('%s', %d)",
                  place, wp.getGold());
               stmt.executeUpdate(sql);
            } 
         }
         //connect.commit();
      }
      catch ( Exception e ) {
         System.err.println( e.getClass().getName() + "  insert into table " + 
                  " " + e.getMessage() );
         System.exit(0);  
      }     
   }//End addTreasure
   
   private void addMap(HashMap<Point, WayPoint> map) {
      Iterator<WayPoint> it = map.values().iterator();
      WayPoint wp; String place, tPlace;
      try { 
         while(it.hasNext()) {
            wp = it.next();
            if (wp.getColor().equals(Color.MAGENTA)) {
               place = "" + (int)wp.getWX() + "," + (int)wp.getWY();
               tPlace = "" + (int)wp.getMapX() + "," + (int)wp.getMapY();
               sql = String.format("INSERT INTO iMap (Place, TreasurePlace) VALUES ('%s', '%s')",
                  place, tPlace);
               stmt.executeUpdate(sql);
            } 
         }
         //connect.commit();
      }
      catch ( Exception e ) {
         System.err.println( e.getClass().getName() + "  insert into table " + 
                  " " + e.getMessage() );
         System.exit(0);  
      }     
   }//End addMap
   
   private void addCity(HashMap<Point, WayPoint> map) {
      Iterator<WayPoint> it = map.values().iterator();
      WayPoint wp; String place;
      try { 
         while(it.hasNext()) {
            wp = it.next();
            if (wp.getColor().equals(Color.CYAN)) {
               place = "" + (int)wp.getWX() + "," + (int)wp.getWY();
               sql = String.format("INSERT INTO iCity (Place, Cost) VALUES ('%s', %d)",
                  place, wp.getCityValue());
               stmt.executeUpdate(sql);
            } 
         }
         //connect.commit();
      }
      catch ( Exception e ) {
         System.err.println( e.getClass().getName() + "  insert into table " + 
                  " " + e.getMessage() );
         System.exit(0);  
      }     
   }//End addCity
  
}//End DB class
