package concurrent;

public class Request 
{
    private User user;
    private Equipment requestedEquipment;
    private String equipmentType;
    private RequestStatus status;

    public Request(User user,String equipmentType)
    {
        this.user = user;
        this.equipmentType = equipmentType;
        this.status = RequestStatus.PENDING; //  A new request shall always start as pending
        this.requestedEquipment = null;
    }

    //--------Accessor methods--------

    public RequestStatus getStatus()
    {
        return status;
    }

    public void setStatus(RequestStatus toSet)
    {
        this.status = toSet;
    }

    public String getEquipmentType()
    {
        return equipmentType;
    }

    public User getUser()
    {
        return user;
    }

    public Equipment getRequestedEquipment()
    {
        return requestedEquipment;
    }

    public void setRequestedEquipment(Equipment toSet)
    {
        this.requestedEquipment = toSet;
    }


}
