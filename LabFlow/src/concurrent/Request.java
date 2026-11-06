package concurrent;

/**
 *  This class is used to create requests.
 *  A request has a status and it is made by a user that requests a type of equipment.
 */
public class Request 
{
    /**
     * The user making the request
     */
    private User user;
    /**
     * The equipment requested
     */
    private Equipment requestedEquipment;

    /**
     * The requested equipment type (the "name" of the requested equipment)
     */
    private String equipmentType;
    /**
     * The request status
     */
    private RequestStatus status;

    /**
     * Constructor method for the Request class
     * @param user the user making the request
     * @param equipmentType the equipment type requested by the user
     */
    public Request(User user,String equipmentType)
    {
        this.user = user;
        this.equipmentType = equipmentType;
        this.status = RequestStatus.PENDING; //  A new request shall always start as pending
        this.requestedEquipment = null;
    }

    //--------Accessor methods--------

    /**
     * Getter method for the request's status
     * @return the status of the request
     */
    public RequestStatus getStatus()
    {
        return status;
    }

    /**
     * Setter method for the request's status
     * @param toSet the wanted status of the request
     */
    public void setStatus(RequestStatus toSet)
    {
        this.status = toSet;
    }

    /**
     * Getter method for the equipment type requested
     * @return the equipment type requested
     */
    public String getEquipmentType()
    {
        return equipmentType;
    }

    /**
     * Getter method for the user making the request
     * @return the user making the request
     */
    public User getUser()
    {
        return user;
    }

    /**
     * Getter method for the requested equipment
     * @return the requested equipment
     */
    public Equipment getRequestedEquipment()
    {
        return requestedEquipment;
    }

    /**
     * Setter method for the requested equipment
     * @param toSet the equipment to be assigned to the request
     */
    public void setRequestedEquipment(Equipment toSet)
    {
        this.requestedEquipment = toSet;
    }

}
