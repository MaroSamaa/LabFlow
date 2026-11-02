package concurrent;

public class Equipment 
{
    private EquipmentStatus status;
    private String name;
    private String code;

    public Equipment(String name, String code)
    {
        this.name = name;
        this.code = code;
        status = EquipmentStatus.AVAILABLE; //Each equipment shall start as available
    }

    //  Getters
    public String getName()
    {
        return name;
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
