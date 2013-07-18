import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)
 
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import java.awt.Font;

/**
 * A space ship. It comes from space, lands, and releases some Greeps into the world.
 * 
 * @author Michael Kolling
 * @author Davin McCall
 * @author Poul Henriksen
 * @version 2.0
 */
public class Ship extends Actor
{
    
    /**
     * Method that creates the Greeps. 
     * You can change the class that objects are created from here.
     */
    private Greep createGreep() 
    {

            return new MyGreep(this);
        }

            //return new SimpleGreep(this);
        }        
    //}
    
    //private int totalPassengers = 20;     // Total number of passengers in this ship.









    
    /**
     * Create a space ship. The parameter specifies at what height to land.
     */








    
    /**
     * Find out which direction we are moving in.
     */







    
    /**
     * Let the ship act: move or release greeps.
     */










 
    
    /**
     * Move the ship down or up (for movement before landing).
     */



        



        


            // Make sure we are at exactly the right target position



    
    /**
     * True if we have reached the intended landing position.
     */





    
    /**
     * Open the ship's hatch. This allows the greeps to come out.
     */




    
    /**
     * Possibly: Let one of the passengers out. Passengers appear at intervals, 
     * so this may or may not release the passenger.
     */












    /**
     * Record that we have collected another tomato.
     */














    
    /**
     * Return the current count of tomatos collected.
     */







    
    /**
     * Get the ship's data bank array. 
     */




    
    /**
     * Return the author name of this ship's Greeps.
     */



    




