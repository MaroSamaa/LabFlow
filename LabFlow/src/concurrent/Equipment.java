package concurrent;

public class Equipment 
{
    private EquipmentStatus status;
    private String type;
    private String code;

    public Equipment(String type, String code)
    {
        this.type = type;
        this.code = code;
        status = EquipmentStatus.AVAILABLE; //Each equipment shall start as available
    }

    //  Getters
    public String getType()
    {
        return type;
    }

    public String getCode()
    {
        return code;
    }

    public EquipmentStatus getStatus()
    {
        return status;
    }
}
