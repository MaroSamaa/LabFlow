package concurrent;

/**
 * @author Marini Enrico
 * Class that represents a user with a given name
 */
public class User 
{
    //--------INSTANCE VARIABLES--------

    /**
     * The name of the user
     */
    private String name;
    
    //--------CONSTRUCTOR--------

    /**
     * Constructor method for the User class
     * @param name the name of the user
     */
    public User(String name)
    {
        this.name = name;
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

}
