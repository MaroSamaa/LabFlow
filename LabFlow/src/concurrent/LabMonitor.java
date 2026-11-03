package concurrent;

import java.util.ArrayList;
import java.util.List;

public class LabMonitor 
{
    private final static int MAX_EQUIP = 4;
    private final static String MSG_FULL = "ERROR: Couldn't add equipment, max number has been reached";

    List<Equipment> equipment;

    public LabMonitor()
    {
        equipment = new ArrayList<Equipment>();
    }

    public void addEquipment(Equipment toAdd) throws IllegalStateException
    {
        if(equipment.size() >= MAX_EQUIP)
        {
            //Illegal State because more of MAX_EQUIP would be a "wrong" state
            throw new IllegalStateException(MSG_FULL);
        }
        equipment.add(toAdd);
    }

    public synchronized void releaseEquipment()
    {

    }

    public synchronized Equipment requestEquipment(String type) throws InterruptedException
    {
        Equipment requestedEquipment = findAvailableEquipment(type);

        while(requestedEquipment == null)
        {
            System.out.println(Thread.currentThread().getName() + " waiting for: " + type); //to be replaced with event log message integration

            wait(); //  Make the thread exit the lock

            requestedEquipment = findAvailableEquipment(type);  //  when awaken retry the search 
        }
        requestedEquipment.setStatus(EquipmentStatus.IN_USE);
        return requestedEquipment;
    }

    private Equipment findAvailableEquipment(String type)
    {
        for(Equipment eq : equipment)
        {
            if(eq.getStatus() == EquipmentStatus.AVAILABLE && type.equalsIgnoreCase(eq.getType()))
            {
                return eq;
            }
        }
        return null;    //if an available equipment of the same type wasn't found return null
    }

}
