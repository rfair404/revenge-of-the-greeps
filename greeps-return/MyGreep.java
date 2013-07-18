import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)

/**
 * A Greep is an alien creature that likes to collect tomatoes.
 * 
 * Rules:
 * 
 * Rule 1 
 * Only change the class �MyGreep�. No other classes may be modi�ed or created. 
 *
 * Rule 2 
 * You cannot extend the Greeps� memory. That is: you are not allowed to add 
 * �elds (other than �nal �elds) to the class. Some general purpose memory is
 * provided. (The ship can also store data.) 
 * 
 * Rule 3 
 * You can call any method de�ned in the "Greep" superclass, except act(). 
 * 
 * Rule 4 
 * Greeps have natural GPS sensitivity. You can call getX()/getY() on any object
 * and get/setRotation() on yourself any time. Friendly greeps can communicate. 
 * You can call getMemory() and getFlag() on another greep to ask what they know. 
 * 
 * Rule 5 
 * No creation of objects. You are not allowed to create any scenario objects 
 * (instances of user-de�ned classes, such as MyGreep). Greeps have no magic 
 * powers � they cannot create things out of nothing. 
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
 * @author Russell Fair
 * @version 0.1
 */
public class MyGreep extends Greep
{
    // Remember: you cannot extend the Greep's memory. So:
    // no additional fields (other than final fields) allowed in this class!
    
    private static final int WATER_TURN_DEGREES = 40;
    private static final int EDGE_TURN_DEGREES = 30;
    private static final int TINY_TURN = 30;
    private static final boolean TOMATO_LOCATION_KNOWN = true;

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
        if (numberOfOpponents(true) > numberOfFriends(true)+1) {
            // Can we see four or more opponents?
            kablam();            
        } 

        //movement and turning, logically
        if (carryingTomato()) {
            if(atShip()) {
                dropTomato();
                //resetMemory();
                turnHome();
                turn(180);
            }
            else {
                returnTomato();//has 1 move.
            }
        }
        else {//not carrying ...
            if(atPile()){
                TomatoPile tomatoes = getTomatoes(); 
                setKnownLocation(tomatoes.getX(), tomatoes.getY());

                if(!blockAtPile(tomatoes)){
                   defendPile(tomatoes);//has 1 move, possibly 
                }  
                else{
                    turnTowards(tomatoes.getX(), tomatoes.getY());
                    //tomatoSearch();//has 1 move
                    //checkFood(); 
                }
            }
            else {
                tomatoSearch();//has 1 move
                checkFood();
            }

        }

        runCouter();
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
     * incriment the counter...
     */
    public void runCouter()
    {
        int counter = getMemory(3);

        if(counter >= 1)
            counter--;
        else 
            counter = 255;

        setMemory(3, counter);
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
            setKnownLocation(tomatoes.getX(), tomatoes.getY());
        }
    }
    /**
    * sets the greeps "GPS" with known coords
    */
    public void setKnownLocation(int x, int y){
            setFlag(1, TOMATO_LOCATION_KNOWN);
            setMemory(1, x);
            setMemory(2, y);
            setMemory(3, 200);
    }

    /**
    * sets the greeps "GPS" with known coords
    */
    public void setBlocking(boolean state){
            setFlag(2, state);
    }
    /**
    * sets the greeps "GPS" with known coords
    */
    public boolean isBlocking(Greep greep){
        return greep.getFlag(2);  
    }

    /**
    * sets the greeps "GPS" with known coords
    */
    public void resetMemory(){
            setFlag(1, false);
            setFlag(2, false);
            setMemory(1, 0);
            setMemory(2, 0);
            setMemory(3, 255);
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
    public void getGreepInfo()
    {

        if(getMemory(3) >= 240)
            return;

        Greep greep = getFriend(); //gets the other greeps

        if(greep == null)
            return;

        if(greep.getFlag(1) == TOMATO_LOCATION_KNOWN){
            setKnownLocation(greep.getMemory(1), greep.getMemory(2));
        }



    }

    /**
    * make this greep continue searching
    */
    public void tomatoSearch(){
        
        checkFood();
        getGreepInfo();
        
        int turncounter = getMemory(0);

        if(atWater()){
            waterTurn();
            setMemory(3, 250);
        }
        else if (atWorldEdge()){
            worldEdgeTurn();
        }
        else if(atKnownPile() && !atPile()){
            resetMemory();
        }
        else if (moveWasBlocked()) {
            kablam();      
            //blockedPileTurn();
        }
        else if(turncounter >= 245 ) {
            //do nothing for 10 turns after bouncing off water...
            //turnHome();
        }
        else if (getFlag(1) == TOMATO_LOCATION_KNOWN) {
            // Hmm. We know where there are some tomatoes...
            turnTowards(getMemory(1), getMemory(2));
        }
        else if(turncounter >= 240 ){
            turn(TINY_TURN);
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
        int bounces = getMemory(0);
        if(bounces == 0){
            bounces = 1;
        }
        
        if(Greenfoot.getRandomNumber(3) == 3 )
            turn(Greenfoot.getRandomNumber(WATER_TURN_DEGREES*bounces));
        else
            turn(Greenfoot.getRandomNumber(WATER_TURN_DEGREES)*-1*bounces);
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

        int turncounter = getMemory(3);

        if(atWater()){
            waterTurn();
            turncounter = 250;

                int bounces = getMemory(0);
                
                if (bounces >= 5){
                    bounces = 0;
                }
                    

                setMemory(0, bounces+1);
        }
        
        else{
            if(turncounter <= 1){
                kablam();
                turncounter = 230;
            }else if(turncounter >= 245 ) {
                //do nothing for 10 turns after bouncing off water...
                //turnHome();
            }else if(turncounter >= 240 ){
                turn(TINY_TURN);
            }else{
                turnHome();
            }
           
        }    
    

        setMemory(3,turncounter--);
        
        move();
    }

    /**
    ** make this greep take tomatoes back to ship
    */
    public void defendPile(TomatoPile tomatoes){
       if(getTomatoes() != null) {
        setKnownLocation(tomatoes.getX(), tomatoes.getY());

            if(distanceTo(tomatoes.getX(), tomatoes.getY()) >= 3){
                turnTowards(tomatoes.getX(), tomatoes.getY());
                move();
            } else {
        
                Greep greep = getFriend(); //gets the other greeps

                if(greep == null){
                    block();
                    setBlocking(true); 
                } else if(greep.getFlag(2)){
                    checkFood();
                    block();
                }
                else{
                    block();
                    setBlocking(true);
                }
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
    ** make this greep take tomatoes back to ship
    */
    public boolean atKnownPile(){
        
        if( getMemory(1) - getX() <= 5 && getMemory(2) - getY() <= 5 )
            return true;
        else 
            return false;
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
