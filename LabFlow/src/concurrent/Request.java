package concurrent;

public class Request 
{
    private Equipment requestedEquipment;
    private String equipmentType;
    private RequestStatus status;

    public Request(String equipmentType)
    {
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

    public Equipment getRequestedEquipment()
    {
        return requestedEquipment;
    }

    public void setRequestedEquipment(Equipment toSet)
    {
        this.requestedEquipment = toSet;
    }


}
