package concurrent;

import java.util.ArrayList;
import java.util.List;

public class LabMonitor 
{
    private final static int MAX_EQUIP = 4;
    private final static String MSG_FULL = "ERROR: Couldn't add equipment, max number has been reached";
    private final static String MSG_REQUEST_NOT_ACTIVE = "ERROR: The request isn't active, current request status is: ";

    List<Equipment> equipment;

    public LabMonitor()
    {
        equipment = new ArrayList<Equipment>();
    }

    public void addEquipment(Equipment toAdd) throws IllegalStateException
    {
        if(equipment.size() >= MAX_EQUIP)
        {
            //  Illegal State because more of MAX_EQUIP would be a "wrong" state
            throw new IllegalStateException(MSG_FULL);
        }
        equipment.add(toAdd);
        System.out.println(toAdd.getType() + " ("+ toAdd.getCode() + ") added");
    }

    public synchronized void releaseEquipment(Request request) throws IllegalStateException
    {
        //  Ensure that the request is active so that no closed or pending request can try and release an equipment they "don't own"  
        if (request.getStatus() != RequestStatus.ACTIVE)
        {
            throw new IllegalStateException(MSG_REQUEST_NOT_ACTIVE + request.getStatus());
        }

        Equipment toRelease = request.getRequestedEquipment();
        toRelease.setStatus(EquipmentStatus.AVAILABLE);
        request.setStatus(RequestStatus.CLOSED);
        System.out.println(request.getUser().getName() + " gave back: " + toRelease.getCode()); //to be replaced with event log message integration

        notifyAll();    //Notify all waiting threads the fastest to awake gets the equipment 
    }

    public synchronized void requestEquipment(Request request) throws InterruptedException
    {
        Equipment requestedEquipment = findAvailableEquipment(request.getEquipmentType());

        while(requestedEquipment == null)   //  Continue trying until you (eventually) can use the equipment
        {
            request.setStatus(RequestStatus.PENDING);
            
            wait(); //  Make the thread exit the lock

            requestedEquipment = findAvailableEquipment(request.getEquipmentType());  //  when awaken retry the search 
        }

        requestedEquipment.setStatus(EquipmentStatus.IN_USE);
        request.setStatus(RequestStatus.ACTIVE);
        System.out.println(request.getUser().getName() + " obtained: " + requestedEquipment.getCode()); //to be replaced with event log message integration
        request.setRequestedEquipment(requestedEquipment);
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
        return null;    //  if an available equipment of the same type wasn't found return null
    }

}
