package concurrent;

/**
 * @author Marini Enrico
 * Class that represets an equipment given a type and a code.
 * The equipment has a status representing it's possible statuses.
 */
public class Equipment 
{
    //--------INSTANCE VARIABLES--------

    /**
     * The equipment status
     */
    private EquipmentStatus status;

    /**
     * The equipment type ("it's name")
     */
    private String type;

    /**
     * The equipment's unique identifier
     */
    private String code;

    //--------CONSTRUCTOR--------

    /**
     * Constructor method for the Equipment class
     * @param type the type of the equipment
     * @param code the code of the equipment
     */
    public Equipment(String type, String code)
    {
        this.type = type;
        this.code = code;
        status = EquipmentStatus.AVAILABLE; //Each equipment shall start as available
    }

    //--------ACCESSOR METHODS--------

    /**
     * Getter method for the equipment's type
     * @return the type of the equipment
     */
    public String getType()
    {
        return type;
    }

    /**
     * Getter method for the equipment's code
     * @return the code of the equipment
     */
    public String getCode()
    {
        return code;
    }

    /**
     * Getter method for the equipment's status
     * @return the status of the equipment
     */
    public EquipmentStatus getStatus()
    {
        return status;
    }

    /**
     * Setter method for the equipment's status
     * @param toSet the wanted status for the equipment
     */
    public void setStatus(EquipmentStatus toSet)
    {
        this.status = toSet;
    }

    //--------METHODS--------
    @Override 
    public String toString()
    {
        return type;
    }
}
