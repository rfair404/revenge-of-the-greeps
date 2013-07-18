import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)

/**
 * A Greep is an alien creature that likes to collect tomatoes.
 * 
 * Rules:
 * 
 * Rule 1 
 * Only change the class ÔMyGreepÕ. No other classes may be modiÞed or created. 
 *
 * Rule 2 
 * You cannot extend the GreepsÕ memory. That is: you are not allowed to add 
 * Þelds (other than Þnal Þelds) to the class. Some general purpose memory is
 * provided. (The ship can also store data.) 
 * 
 * Rule 3 
 * You can call any method deÞned in the "Greep" superclass, except act(). 
 * 
 * Rule 4 
 * Greeps have natural GPS sensitivity. You can call getX()/getY() on any object
 * and get/setRotation() on yourself any time. Friendly greeps can communicate. 
 * You can call getMemory() and getFlag() on another greep to ask what they know. 
 * 
 * Rule 5 
 * No creation of objects. You are not allowed to create any scenario objects 
 * (instances of user-deÞned classes, such as MyGreep). Greeps have no magic 
 * powers Ð they cannot create things out of nothing. 
 * 
 * Rule 6 
 * You are not allowed to call any methods (other than those listed in Rule 4)
 * of any other class in this scenario (including Actor and World). 
 *  
 * If you change the name of this class you should also change it in
 * Ship.createGreep().
 * 
 * Please do not publish your solution anywhere. We might want to run this
 * competition again, or it might be used by teachers to run in a class, and
 * that would be ruined if solutions were available.
 * 
 * 
 * @author (your name here)
 * @version 0.1
 */
public class MyGreep extends Greep
{
    // Remember: you cannot extend the Greep's memory. So:
    // no additional fields (other than final fields) allowed in this class!
    
    private static final int WATER_TURN_DEGREES = 10;
    private static final int EDGE_TURN_DEGREES = 30;
    private static final int TOMATO_LOCATION_KNOWN = 1;

    /**
     * Default constructor. Do not remove.
     */
    public MyGreep(Ship ship)
    {
        super(ship);
    }
    
    /**
     * Do what a greep's gotta do.
     */
    public void act()
    {
        super.act();   // do not delete! leave as first statement in act().
        
        //first attempt to blow up any unfriendly greeps
        if (numberOfOpponents(false) > 3) {
            // Can we see four or more opponents?
            kablam();            
        } 

        //movement and turning, logically
        if (carryingTomato()) {
            if(atShip()) {
                dropTomato();
            }
            else {
                returnTomato();//has 1 move.
            }
        }
        else {
            if(atPile()){
                defendPile();//has 1 move, possibly
                checkFood();
            }
            else {
                tomatoSearch();//has 1 move
            }

        }
    }
    
    /** 
     * Move forward, with a slight chance of turning randomly
     */
    public void randomTurn()
    {
        // there's a 3% chance that we randomly turn a little off course
        if (randomChance(3)) {
            turn((Greenfoot.getRandomNumber(3) - 1) * 100);
        }
    }

    /**
     * Is there any food here where we are? If so, try to load some!
     */
    public void checkFood()
    {
        // check whether there's a tomato pile here
        TomatoPile tomatoes = getTomatoes();
        if(tomatoes != null) {
            loadTomato();
            // Note: this attempts to load a tomato onto *another* Greep. It won't
            // do anything if we are alone here.

            setMemory(0, TOMATO_LOCATION_KNOWN);
            setMemory(1, tomatoes.getX());
            setMemory(2, tomatoes.getY());

        }
    }

    /**
     * This method specifies the name of the greeps (for display on the result board).
     * Try to keep the name short so that it displays nicely on the result board.
     */
    public String getName()
    {
        return "Russell's Super Greeps";  // write your name here!
    }

    /**
     * This method asks other greeps about known tomato piles
     */
    public void getInfo()
    {
        Greep greep = getFriend(); //gets the other greeps

        

      


    }
    /**
    * make this greep continue searching
    */
    public void tomatoSearch(){
        
        checkFood();
        if(atWater()){
            waterTurn();
        }
        else if (atWorldEdge()){
            worldEdgeTurn();
        }

        else if (getMemory(0) == TOMATO_LOCATION_KNOWN) {
            // Hmm. We know where there are some tomatoes...
            turnTowards(getMemory(1), getMemory(2));
        }

        else if (moveWasBlocked()) {
            kablam();      
            //blockedPileTurn();

        }
        else {
            randomTurn();//keep going
        }

        move(); 
    }

    /**
    * turns the greep when at water
    */
    public void waterTurn(){
        turn(WATER_TURN_DEGREES);
    }
    /**
    * turns the greep when at water
    */
    public void worldEdgeTurn(){
        turn(EDGE_TURN_DEGREES);
    }

    public void blockedPileTurn(){

        // If we were blocked, try to move somewhere else
        int r = getRotation();
        setRotation (r + Greenfoot.getRandomNumber(2) * 180 - 90);
         
    }

    /**
    ** make this greep take tomatoes back to ship
    */
    public void returnTomato(){
        if(atWater())
            waterTurn();
        else
            turnHome();
        
        move();
    }

    /**
    ** make this greep take tomatoes back to ship
    */
    public void defendPile(){
       if(getTomatoes() != null) {            
            TomatoPile tomatoes = getTomatoes(); 
            if(!blockAtPile(tomatoes)) {
                // Not blocking so lets go towards the centre of the pile
                turnTowards(tomatoes.getX(), tomatoes.getY());
                move();
            }
        }
    }


    /**
    ** make this greep take tomatoes back to ship
    */
    public boolean atPile(){
        TomatoPile tomatoes = getTomatoes();
        if(tomatoes != null)
            return true;
        else return false;
    }


    /**
     * If we are at a tomato pile and none of our friends are blocking, we will block.
     * pulled from SimpleGreep
     * @return True if we are blocking, false if not.
     */
    private boolean blockAtPile(TomatoPile tomatoes) 
    {
        // Are we at the centre of the pile of tomatoes?  
        boolean atPileCentre = tomatoes != null && distanceTo(tomatoes.getX(), tomatoes.getY()) < 4;
        if(atPileCentre && getFriend() == null ) {
            // No friends at this pile, so we might as well block
            block(); 
            return true;
        }
        else {
            return false;
        }
    }
    /**
     * get distance to something
     * pulled from SimpleGreep
     */
    private int distanceTo(int x, int y)
    {
        int deltaX = getX() - x;
        int deltaY = getY() - y;
        return (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    /**
     * Test if we are close to one of the edges of the world. Return true if we are.
     * stolen from orgional creature class
     */
    public boolean atWorldEdge()
    {
        if(getX() < 3 || getX() > getWorld().getWidth() - 3)
            return true;
        if(getY() < 3 || getY() > getWorld().getHeight() - 3)
            return true;
        else
            return false;
    }

}
