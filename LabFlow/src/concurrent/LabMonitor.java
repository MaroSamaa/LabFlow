package concurrent;

import java.util.ArrayList;
import java.util.List;

public class LabMonitor 
{
    private final static int MAX_EQUIP = 4;

    List<Equipment> equipment;

    public LabMonitor()
    {
        equipment = new ArrayList<Equipment>();
    }
}
