/*

Programmers: Kris Larson
Last Edited: January 17, 2014

Description: A basis for the stats players and enemies will use.

*/




public class Stats {
   private int strength; //Physical attack power, how much can be held (weight), and some effect on physical defense
   private int endurence; //Health amount and stamina (How long a player or enemy can do something?), some effect on physical defense
   private int dexterity; //Physical attack speed, physical attack accuracy, speed of attack, and some effect on dodging
   private int intelligence; //Magic, mana amount, magical attack power, and some effect on dodging (ability to predict attacks)
   private int charisma; //Reduce enemy's attack and defense during battle
   private int luck; //Chance to perform critical hits (physical and magical)
   
   public Stats() {
      strength = 0;
      endurence = 0;
      dexterity = 0;
      intelligence = 0;
      charisma = 0;
      luck = 0;
   }
   
   public Stats(int str, int end, int dex, int itl, int cha, int luc) {
      strength = str;
      endurence = end;
      dexterity = dex;
      intelligence = itl;
      charisma = cha;
      luck = luc;
   }
   
   //get methods
   public int getStrength() {
      return strength;
   }
   
   public int getEndurence() {
      return endurence;
   }
      
   public int getDexterity() {
      return dexterity;
   }
      
   public int getIntelligence() {
      return intelligence;
   }
      
   public int getCharisma() {
      return charisma;
   }
      
   public int getLuck() {
      return luck;
   }
   
   //methods to increase stat
   public void addStrength(int str) {
      strength += str;
   }
   
   public void addEndurence(int end) {
      endurence += end;
   }
   
   public void addDexterity(int dex) {
      dexterity += dex;
   }
   
   public void addIntelligence(int itl) {
      intelligence += itl;
   }
   
   public void addCharisma(int cha) {
      charisma += cha;
   }
   
   public void addLuck(int luc) {
      luck += luc;
   }
//----------------------------------------------------------------------   
   private int health;
   private int mana;
   private int pAttack;
   private int pDefense;
   private int mAttack;
   private int mDefense;
   private int speed;
   private double pHitChance;
   private double dodgeChance;
   private double critChance;
   
   //methods to calculate stats for battle
   public int setHealth(int end) {
      health = endurance * 10; 
   }
   
   public int setMana(int itl) {
      mana = intelligence * 5;
   }
   
   public int setPAttack(int str) { //the range for the pAttack can have this calculate the maxAttack and when actually used
      pAttack = (strength * 2) + 3; //the minAttack can just be some value subtracted from maxAttack
   }
   
   public int setPDefense(int str, int end) {
      
   }
   
   public int setMAttack(int itl) {
   
   }
   
   public int setMDefense(int itl) {
   
   }
   
   public int setSpeed(int dex) {
   
   }
   
   public double setPHitChance(int dex) {
   
   }
   
   public double setDodgeChance(int dex, int itl) {
   
   }
   
   public double setCritChance(int luc) {
   
   }
}
