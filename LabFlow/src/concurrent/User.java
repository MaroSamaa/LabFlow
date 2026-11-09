package concurrent;

import java.io.Serializable;

/**
 * @author Marini Enrico
 * Class that represents a user with a given name and a unique code
 */
public class User implements Serializable
{
    private static final long serialVersionUID = 1L;
    //--------INSTANCE VARIABLES--------

    /**
     * The name of the user
     */
    private String name;

    /**
     * The unique code of the user
     */
    private String code;
    
    //--------CONSTRUCTOR--------

    /**
     * Constructor method for the User class
     * @param name the name of the user
     * @param code the unique code of the user
     */
    public User(String name, String code)
    {
        this.name = name;
        this.code = code;
    }

    //--------ACCESSOR METHODS--------

    /**
     * Getter method for the name of the user
     * @return the name of the user
     */
    public String getName()
    {
        return name;
    }

    /**
     * Getter method for the unique code of the user
     * @return the code of the user
     */
    public String getCode()
    {
        return code;
    }

    //--------METHODS--------
    @Override 
    public String toString()
    {
        return name + " (" + code + ")";
    }

}
