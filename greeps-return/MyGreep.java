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
        if (carryingTomato()) {
            if(atShip()) {
                dropTomato();
            }
            else {
                if (atWater() || moveWasBlocked()) {
                    // If we were blocked, try to move somewhere else
                    int r = getRotation();
                    setRotation (r + Greenfoot.getRandomNumber(2) * 200 - 80);
                    move();
                    kablam();
                }
                else{ turnHome();
                    move();
                }
            }
        }
        else {
            TomatoPile tomatoes = getTomatoes();
            if(tomatoes != null) {
                block();
                loadTomato();
                // Note: this attempts to load a tomato onto *another* Greep. It won't
                // do anything if we are alone here.
            }
            else {
                randomWalk();
                checkFood();

                if (numberOfOpponents(false) > 1) {

                    if (numberOfFriends(false) < 3)
                    // Can we see four or more opponents?
                        kablam();
                }

                if (atWater() || moveWasBlocked()) {
                    // If we were blocked, try to move somewhere else
                    int r = getRotation();
                    setRotation (r + Greenfoot.getRandomNumber(2) * 180 - 90);
                    move();
                }
                if (moveWasBlocked())
                {
                    kablam();

                }

            }
        }
    }

    /** 
     * Move forward, with a slight chance of turning randomly
     */
    public void randomWalk()
    {
        // there's a 3% chance that we randomly turn a little off course
        if (randomChance(3)) {
            turn((Greenfoot.getRandomNumber(3) - 1) * 100);
        }

        move();
    }

    /**
     * Is there any food here where we are? If so, try to load some!
     */
    public void checkFood()
    {
        // check whether there's a tomato pile here
        TomatoPile tomatoes = getTomatoes();
        if(tomatoes != null) {
            block();
            loadTomato();

            // Note: this attempts to load a tomato onto *another* Greep. It won't
            // do anything if we are alone here.
        }

        if(numberOfFriends(false) > 2) {
            loadTomato();
        }

        if(numberOfOpponents(false) > 2) {
            kablam();
        }

        /**
         * This method specifies the name of the greeps (for display on the result board).
         * Try to keep the name short so that it displays nicely on the result board.
         */
    } public String getName()
        {
            return "Felipe";  // write your name here!
        }
    }

