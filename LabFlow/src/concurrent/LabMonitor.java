package concurrent;

import java.util.ArrayList;
import java.util.List;

/**
 *  @author Marini Enrico
 *  The lab monitor class is the monitor for the management of the resources (Equipment) requested by threads(RequestTaks).
 *  It ensures the resources get taken only when they are available (they aren't being used by anyone), if a request for a 
 *  non available equipment is's put in queue waiting for the resource to be available.
 *  It also implements a cap on the number of equipment that can be "stored" in the lab.  
 * 
 */
public class LabMonitor 
{
    /**
     * The maximum number of equipment that can be "stored" in the lab
     */
    private final static int MAX_EQUIP = 4;
    private final static String MSG_FULL = "ERROR: Couldn't add equipment, max number has been reached";
    private final static String MSG_REQUEST_NOT_ACTIVE = "ERROR: The request isn't active, current request status is: ";
    private final static String MSG_USER_DUPLICATE = "ERROR: A user with the same name already exists";

    //--------INSTANCE VARIABLES--------

    /**
     * Equipment "stored" in the lab
     */
    List<Equipment> equipment;

    /**
     * List of users of the lab
     */
    List<User> users;

    /**
     * List of all the requests made 
     */
    List<Request> requests;

    /**
     * List of LabListeners 
     */
    List<LabListener> listeners;

    //---------CONSTRUCTOR--------

    /**
     * Constructor method for the LabMonitor class
     * It initializes the equipment list as an empty arraylist of equipment.
     * It initializes the user list as an empty arraylist of users.
     * It initializes the request list as an empty arraylist of requests.
     */
    public LabMonitor()
    {
        equipment = new ArrayList<Equipment>();
        users = new ArrayList<User>();
        requests = new ArrayList<Request>();
        listeners = new ArrayList<LabListener>();
    }

    //--------ACCESSOR METHODS--------

    /**
     * Getter method for the user list
     * @return a copy of the user list
     */
    public synchronized ArrayList<User> getUsers()
    {
        return new ArrayList<User>(users);
    }

    /**
     * Getter method for the equipment list
     * @return a copy of the equipment list
     */
    public synchronized ArrayList<Equipment> getEquipment()
    {
        return new ArrayList<Equipment>(equipment);
    }

    /**
     * Getter method for the request list
     * @return a copy of the request list
     */
    public synchronized ArrayList<Request> getRequests()
    {
        return new ArrayList<Request>(requests);
    }

    //--------METHODS--------

    /**
     * This method allows for a user to be registered to the lab
     * @param toAdd the user to be registered to the lab
     * @throws IllegalStateException if the user is already registered to the lab
     */
    public synchronized void addUser(User toAdd) throws IllegalStateException
    {
        for(User u : users) //Make sure there are no user with the same name
        {
            if(u.getName().equalsIgnoreCase(toAdd.getName()))
            {
                throw new IllegalStateException(MSG_USER_DUPLICATE);
            }
        }
        users.add(toAdd);
        System.out.println("User: " + toAdd.getName() + " added successfully"); //integrate with popup or event log
    }

    /**
     * This method allows for an equipment to be added in the lab "storage"
     * @param toAdd the equipment to be added in the lab "storage"
     * @throws IllegalStateException if no more equipment can be "stored" in the lab
     */
    public synchronized void addEquipment(Equipment toAdd) throws IllegalStateException
    {
        if(equipment.size() >= MAX_EQUIP)
        {
            //  Illegal State because more of MAX_EQUIP would be a "wrong" state
            throw new IllegalStateException(MSG_FULL);
        }
        equipment.add(toAdd);
        System.out.println(toAdd.getType() + " ("+ toAdd.getCode() + ") added");
    }

    /**
     * This method allows for an equipment (resource) to be released (set to AVAILABLE) and a request to be concluded (set to CLOSED)
     * @param request the request that is being conclunded
     * @throws IllegalStateException if the request doesn't have ACTIVE status (so if there is no equipment to be released)
     */
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
        notifyListeners();  //Notify all the listeners that a request had a status change
        System.out.println(request.getUser().getName() + " gave back: " + toRelease.getCode()); //to be replaced with event log message integration

        notifyAll();    //Notify all waiting threads the fastest to awake gets the equipment 
    }

    /**
     * This method allows for the allocation of an equipment to a request
     * @param request the request for the equipment
     * @throws InterruptedException if thread gets interrupted
     */
    public synchronized void requestEquipment(Request request) throws InterruptedException
    {
        requests.add(request);  //Add the request to the requests list
        notifyListeners();      //Notify all the listeners that a request has been added

        Equipment requestedEquipment = findAvailableEquipment(request.getEquipmentType());

        while(requestedEquipment == null)   //  Continue trying until you (eventually) can use the equipment
        {
            request.setStatus(RequestStatus.PENDING);
            notifyListeners();  //Notify all the listeneres that a request had a Status change
            
            wait(); //  Make the thread exit the lock

            requestedEquipment = findAvailableEquipment(request.getEquipmentType());  //  when awaken retry the search 
        }

        requestedEquipment.setStatus(EquipmentStatus.IN_USE);
        request.setStatus(RequestStatus.ACTIVE);
        notifyListeners();  //Notify all the listeneres that a request had a status change
        System.out.println(request.getUser().getName() + " obtained: " + requestedEquipment.getCode()); //to be replaced with event log message integration
        request.setRequestedEquipment(requestedEquipment);
    }

    /**
     * This method allows the search for an AVAILABLE equipment of a certain type
     * @param type the type of the equipment searched
     * @return the equipment searched if AVAILABLE
     * @return null if the equipment searched isn't available or if it doesn't exist in the lab
     */
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

    /**
     * Method to add a LabListener to the LabMonitor's list
     * @param toAdd the LabListener to add
     */
    public synchronized void addLabListener(LabListener toAdd)
    {
        listeners.add(toAdd);
        toAdd.onLabStateChange();   //When a new listener gets added notify about it
    }

    /**
     * Method to notify all listeners that something in the lab changed
     */
    private void notifyListeners()
    {
        for(LabListener ls : listeners)
        {
            ls.onLabStateChange();
        }
    }

}
