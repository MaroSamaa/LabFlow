package concurrent;

import java.util.ArrayList;
import java.util.List;
import concurrent.exceptions.*;

/**
 *  @author Marini Enrico
 *  The lab monitor class is the monitor for the management of the resources (Equipment) requested by threads(RequestTaks).
 *  It ensures the resources get taken only when they are available (they aren't being used by anyone), if a request for a 
 *  non available equipment is's put in queue waiting for the resource to be available.
 *  It also implements a cap on the number of equipment that can be "stored" in the lab. 
 *  It communicates to the outside by notifying LabListeners. 
 *  It implements save and load features for it's state.
 * 
 */
public class LabMonitor 
{
    /**
     * Default value for maxEquip static variable
     */
    public final static int DEFAULT_MAX_EQUIP = 4;

    //---------Error Messages---------
    private final static String MSG_FULL = "ERROR: Couldn't add equipment, max number has already been reached";
    private final static String MSG_REQUEST_NOT_ACTIVE = "ERROR: The request isn't active, current request status is: ";
    private final static String MSG_USER_DUPLICATE = "ERROR: A user with the same code already exists";
    private final static String MSG_CODE_DUPLICATE = "ERROR: An equipment with the same code already exists";
    private static final String MSG_NEW_MAX_EQUIP_TOO_SMALL = "ERROR: The new maxEquip value should be > 0 and has to be greater than the number of equipment currently stored";

    //--------INSTANCE VARIABLES--------

    /**
     * The maximum number of equipment that can be "stored" in the lab
     */
    private int maxEquip;

    /**
     * Equipment "stored" in the lab
     */
    private List<Equipment> equipment;

    /**
     * List of users of the lab
     */
    private List<User> users;

    /**
     * List of all the requests made 
     */
    private List<Request> requests;

    /**
     * List of all the events that happened
     */
    private List<String> events;

    /**
     * List of LabListeners 
     */
    private List<LabListener> listeners;

    /**
     * State of the monitor
     */
    private boolean isPaused;

    /**
     * Counter that contains how many users are registered to the lab
     */
    private int userCounter;

    /**
     * Counter that contains how many equipments are stored in the lab
     */
    private int equipmentCounter;

    /**
     * Counter that contains how many requests are either PENDING or ACTIVE
     */
    private int activeRequestCounter;

    //---------CONSTRUCTOR--------

    /**
     * Constructor method for the LabMonitor class
     * It initializes the equipment list as an empty arraylist of equipment.
     * It initializes the user list as an empty arraylist of users.
     * It initializes the request list as an empty arraylist of requests.
     * It starts in a paused state.
     * It sets the starting maxEquip value to the default value
     */
    public LabMonitor()
    {
        maxEquip = DEFAULT_MAX_EQUIP;
        equipment = new ArrayList<Equipment>();
        users = new ArrayList<User>();
        requests = new ArrayList<Request>();
        listeners = new ArrayList<LabListener>();
        events = new ArrayList<String>();
        isPaused = true;    //  The monitor starts as paused
        //  Initialize counters at 0
        userCounter = 0;
        equipmentCounter = 0;
        activeRequestCounter = 0;
    }

    //--------ACCESSOR METHODS--------

    /**
     * Getter method for the user list
     * @return a deep copy of the user list
     */
    public synchronized ArrayList<User> getUsers()
    {
        ArrayList<User> usersDeepCopy = new ArrayList<User>();
        for(User u : users)
        {
            usersDeepCopy.add(new User(u));
        }
        return usersDeepCopy;
    }

    /**
     * Getter method for the equipment list
     * @return a deep copy of the equipment list
     */
    public synchronized ArrayList<Equipment> getEquipment()
    {
        ArrayList<Equipment> equipmentDeepCopy = new ArrayList<Equipment>();
        for(Equipment e: equipment)
        {
            equipmentDeepCopy.add(new Equipment(e));
        }
        return equipmentDeepCopy;
    }

    /**
     * Getter method for the request list
     * @return a deep copy of the request list
     */
    public synchronized ArrayList<Request> getRequests()
    {
        ArrayList<Request> requestsDeepCopy = new ArrayList<Request>();
        for(Request r : requests)
        {
            requestsDeepCopy.add(new Request(r));
        }
        return requestsDeepCopy;
    }

    /**
     * Getter method for the event list
     * @return a copy of the event list
     */
    public synchronized ArrayList<String> getEvents()
    {
        return new ArrayList<String>(events);   //No need for a deep copy, Strings are immutable
    }

    /**
     * Getter method for the user counter
     * @return the number of users registered to the lab
     */
    public synchronized int getUserCounter()
    {
        return userCounter;
    }

    /**
     * Getter method for the equipment counter
     * @return the number of equipment stored in the lab
     */
    public synchronized int getEquipmentCounter()
    {
        return equipmentCounter;
    }

    /**
     * Getter method for the active request counter
     * @return the number of PENDING or ACTIVE requests
     */
    public synchronized int getActiveRequestCounter()
    {
        return activeRequestCounter;
    }

    /**
     * Getter method for the state of the lab
     * @return the state of the lab
     */
    public synchronized boolean isPaused()
    {
        return isPaused;
    }

    public synchronized int getMaxEquip()
    {
        return maxEquip;
    }

    /**
     * Setter method for the max equip storable in the lab
     * @param newMaxEquip the new wanted value for maxEquip
     * @throws IllegalArgumentException if the new value is < 0 or the new value does not have room to accomodate all the equipment now stored
     */
    public synchronized void setMaxEquip(int newMaxEquip) throws IllegalArgumentException
    {
        //Ensure that the new value is >0 and that the new value has room for the now stored equipments
        if(newMaxEquip < 0 || newMaxEquip < equipmentCounter)  
        {
            throw new IllegalArgumentException(MSG_NEW_MAX_EQUIP_TOO_SMALL);
        }
        maxEquip = newMaxEquip;
    }

    /**
     * This method allows for a user to be registered to the lab
     * @param toAdd the user to be registered to the lab
     * @throws DuplicateUserNameException if the user is already registered to the lab
     */
    public synchronized void addUser(User toAdd) throws DuplicateUserCodeException
    {
        //Make sure there is no user with the same user code
        for(User u : users)
        {
            if(u.getCode().equalsIgnoreCase(toAdd.getCode()))
            {
                throw new DuplicateUserCodeException(MSG_USER_DUPLICATE);
            }
        }

        //  Register the user added avent and notify all listeners
        users.add(toAdd);
        userCounter++;
        addEvent(toAdd.toString() + " has been registered to the lab");
    }

    /**
     * This method allows for an equipment to be added in the lab "storage"
     * @param toAdd the equipment to be added in the lab "storage"
     * @throws EquipmentStorageFullException if no more equipment can be "stored" in the lab
     * @throws DuplicateEquipmentCodeException if the new equipment has the same code of another equipment already "stored"
     */
    public synchronized void addEquipment(Equipment toAdd) throws EquipmentStorageFullException, DuplicateEquipmentCodeException
    {
        //  Check if the storage isn't full 
        if(equipmentCounter >= maxEquip)
        {
            throw new EquipmentStorageFullException(MSG_FULL);
        }

        //  Check if another equipment in the lab's list has the same code
        for(Equipment e : equipment)
        {
            if(e.getCode().equalsIgnoreCase(toAdd.getCode()))
            {
                throw new DuplicateEquipmentCodeException(MSG_CODE_DUPLICATE);
            }
        }

        //  Register the equipment added event and notify all listeners
        equipment.add(toAdd);
        equipmentCounter++;
        addEvent(toAdd.getType() + " ("+ toAdd.getCode() + ") added"); 
    }

    /**
     * This method allows for a request to be added in the lab's request list
     * @param toAdd the request to add to the lab's list
     */
    public synchronized void addRequest(Request toAdd)
    {
        //  Register the added request event and notify all listeners
        requests.add(toAdd);
        activeRequestCounter++;
        addEvent(toAdd.getUser().toString() + " requested: " + toAdd.getEquipmentType());
    }

    /**
     * Service method to register events and notify all listeners about the event that happened
     * @param msg the message representing the occurred event
     */
    private void addEvent(String msg)
    {
        //  Ask the pc for the time, and don't give me the ms 
        String timestamp = java.time.LocalTime.now().withNano(0).toString();
        events.add(timestamp + " " + msg);
        notifyListeners(); //   Notify all listeners because an event has happened
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
     * Method to get if even one of the lab's requests is active
     * @return true if at least one request's status is ACTIVE
     */
    public synchronized boolean hasActiveRequests()
    {
        for(Request r : requests)
        {
            if(r.getStatus() == RequestStatus.ACTIVE)
            {
                return true;
            }
        }
        return false;
    }

    //--------METHODS--------

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

        //  Register the user gave back event and notify all listeners (request and equipment changed status)
        Equipment toRelease = request.getRequestedEquipment();
        toRelease.setStatus(EquipmentStatus.AVAILABLE);
        request.setStatus(RequestStatus.CLOSED);
        activeRequestCounter--;
        addEvent(request.getUser().toString() + " gave back: " + toRelease.getCode());

        notifyAll();    //Notify all waiting threads the fastest to awake gets the equipment 
    }

    /**
     * This method allows for the allocation of an equipment to a request
     * @param request the request for the equipment
     * @throws InterruptedException if thread gets interrupted
     */
    public synchronized void requestEquipment(Request request) throws InterruptedException
    {
        //  A thread that starts when the lab is paused can deposit the request to the list but then it gets immediatly put to sleep
        while(isPaused())
        {
            wait();
        }

        Equipment requestedEquipment = findAvailableEquipment(request.getEquipmentType());

        while(requestedEquipment == null)   //  Continue trying until you (eventually) can use the equipment
        {
            request.setStatus(RequestStatus.PENDING);
            notifyListeners();  //Notify all the listeneres that a request had a Status change
            
            wait(); //  Make the thread exit the lock

            requestedEquipment = findAvailableEquipment(request.getEquipmentType());  //  when awaken retry the search 
        }

        //  Register the user obtained event and notify all listeners (request and equipment changed status)
        requestedEquipment.setStatus(EquipmentStatus.IN_USE);
        request.setStatus(RequestStatus.ACTIVE);
        addEvent(request.getUser().toString() + " obtained: " + requestedEquipment.getCode()); 

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
     * Service method to notify all listeners that something in the lab changed
     */
    private void notifyListeners()
    {
        for(LabListener ls : listeners)
        {
            ls.onLabStateChange();
        }
    }

    //--------SNAPSHOT METHODS--------

    /**
     * Method to create a LabSnapshot of the lab.
     * The lab get's paused before saving to prevent changes from happening during writing.
     * The ACTIVE requests are snapshot as CANCELLED and all of the equipment they obtained gets released
     * @return the LabSnapshot representing the lab state
     */
    public synchronized LabSnapshot createSnapshot() 
    {

        //  Pause the labmonitor before creating the snapshot
        if(!isPaused()) 
        {
            pause();
        }

        //  We modify a deep copy of the lists to maintain the labMonitor's current state in the current session

        //  Make all of the active request cancelled 
        ArrayList<Request> requestsCopyToSave = getRequests();    //  Work on a deep copy 
        for(Request r: requestsCopyToSave)
        {
            if(r.getStatus() == RequestStatus.ACTIVE)
            {
                r.setStatus(RequestStatus.CANCELLED);
            }
        } 

        //  Reset all IN_USE equipment to AVAILABLE status
        ArrayList<Equipment> equipmentCopyToSave = getEquipment();  //  Work on a deep copy 
        for(Equipment e: equipmentCopyToSave)
        {
            if(e.getStatus() == EquipmentStatus.IN_USE)
            {
                e.setStatus(EquipmentStatus.AVAILABLE);
            }
        }

        return new LabSnapshot(getUsers(), equipmentCopyToSave, requestsCopyToSave, getEvents());
    }

    /**
     * Method to restore a LabSnapshot.
     * @param toRestore LabSnapshot to restore
     */
    public synchronized void restoreSnapshot(LabSnapshot toRestore)
    {
        //  Pause the LabMonitor before restoring anything
        if(!isPaused)  
        {
            pause();
        }
         
        //  Update the monitor's lists with the snapshot's data
        users.clear();
        users.addAll(toRestore.getUsers());
        equipment.clear();
        equipment.addAll(toRestore.getEquipment());
        requests.clear();
        requests.addAll(toRestore.getRequests());
        events.clear();
        events.addAll(toRestore.getEvents());

        //  Restart counter from the information read
        userCounter = users.size();
        equipmentCounter = equipment.size();
        
        //  Regenerate loaded PENDING threads and update activeRequestCounter
        regeneratePendingThreads();

        addEvent("The file has been read, the lab is now loaded and ready to resume operations");
    }

    /**
     *  Service method to regenerate loaded pending threads and update activeRequestCounter after snapshot restoration
     */
    private void regeneratePendingThreads()
    {
        activeRequestCounter = 0;   //  Reset the counter to 0
        for(Request r : requests)
        {
            if(r.getStatus() == RequestStatus.PENDING)
            {
                RequestTask task = new RequestTask(r, this);
                task.setRegenerated();  //  Skip the addRequest call
                Thread requestThread = new Thread(task);
                requestThread.start();
                activeRequestCounter++;
            }
        }
    }

    //--------PAUSE AND PLAY METHODS--------

    /**
     * Method to call when pausing LabMonitor's operations
     */
    public synchronized void pause()
    {
        if(!isPaused)
        {
            isPaused = true;
            addEvent("The simulation has been paused");
            notifyListeners();
        }
    }

    /**
     * Method that threads can call to check if the monitor is paused
     * if this is true they get put to sleep.
     * @throws InterruptedException if thread gets interrupted
     */
    public synchronized void checkPaused() throws InterruptedException
    {
        while(isPaused())
        {
            wait(); //  while paused put all active threads to sleep
        }
    }

    /**
     * Method to call when resuming LabMonitor's operations
     */
    public synchronized void play()
    {
        if(isPaused())
        {
            isPaused = false;
            addEvent("The simulation has resumed");
            notifyListeners();
            notifyAll();    //  Wake up all threads
        }
    }
}
