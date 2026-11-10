package concurrent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import concurrent.exceptions.*;

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

    //  Error Messages
    private final static String MSG_FULL = "ERROR: Couldn't add equipment, max number has already been reached";
    private final static String MSG_REQUEST_NOT_ACTIVE = "ERROR: The request isn't active, current request status is: ";
    private final static String MSG_USER_DUPLICATE = "ERROR: A user with the same code already exists";
    private final static String MSG_CODE_DUPLICATE = "ERROR: An equipment with the same code already exists";

    //--------INSTANCE VARIABLES--------

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

    //---------CONSTRUCTOR--------

    /**
     * Constructor method for the LabMonitor class
     * It initializes the equipment list as an empty arraylist of equipment.
     * It initializes the user list as an empty arraylist of users.
     * It initializes the request list as an empty arraylist of requests.
     * It starts in a paused state.
     */
    public LabMonitor()
    {
        equipment = new ArrayList<Equipment>();
        users = new ArrayList<User>();
        requests = new ArrayList<Request>();
        listeners = new ArrayList<LabListener>();
        events = new ArrayList<String>();
        isPaused = true;    //  The monitor starts as paused
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
     * Getter method for the state of the lab
     * @return the state of the lab
     */
    public synchronized boolean isPaused()
    {
        return isPaused;
    }

    /**
     * This method allows for a user to be registered to the lab
     * @param toAdd the user to be registered to the lab
     * @throws DuplicateUserNameException if the user is already registered to the lab
     */
    public synchronized void addUser(User toAdd) throws DuplicateUserCodeException
    {
        for(User u : users) //Make sure there is no user with the same user code
        {
            if(u.getCode().equalsIgnoreCase(toAdd.getCode()))
            {
                throw new DuplicateUserCodeException(MSG_USER_DUPLICATE);
            }
        }
        users.add(toAdd);
        //  Register the user added avent and notify all listeners
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
        if(equipment.size() >= MAX_EQUIP)
        {
            //  Illegal State because more of MAX_EQUIP would be a "wrong" state
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
        equipment.add(toAdd);
        //  Register the equipment added event and notify all listeners
        addEvent(toAdd.getType() + " ("+ toAdd.getCode() + ") added"); 
    }

    /**
     * This method allows for a request to be added in the lab's request list
     * @param toAdd the request to add to the lab's list
     */
    public synchronized void addRequest(Request toAdd)
    {
        requests.add(toAdd);
        //  Register the added request event and notify all listeners
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

        Equipment toRelease = request.getRequestedEquipment();
        toRelease.setStatus(EquipmentStatus.AVAILABLE);
        request.setStatus(RequestStatus.CLOSED);

        //  Register the user gave back event and notify all listeners (request and equipment changed status)
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

        requestedEquipment.setStatus(EquipmentStatus.IN_USE);
        request.setStatus(RequestStatus.ACTIVE);

        //  Register the user obtained event and notify all listeners (request and equipment changed status)
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

    //--------SAVE AND LOAD METHODS--------

    /**
     * Method to save the state of the lab.
     * This saves on the file a copy of the user, equipment, request and event lists.
     * The lab get's paused before saving to prevent changes from happening during writing.
     * The ACTIVE requests are saved as CANCELLED and all of the equipment they obtained gets released
     * @param file the file where data will be written
     * @throws IOException  if something happens when writing the data
     */
    public synchronized void saveLabState(File file) throws IOException
    {

        if(!isPaused()) //  Pause the labmonitor before saving anything
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

        //  Use ObjectOutputStream to write data on the file
        //  Try to handle closing and unexpected closing automatically
        try (ObjectOutputStream fileWriter = new ObjectOutputStream(new FileOutputStream(file))) 
        { 
            fileWriter.writeObject(getUsers());             //  Save users
            fileWriter.writeObject(equipmentCopyToSave);    //  Save equipment
            fileWriter.writeObject(requestsCopyToSave);     //  Save requests
            fileWriter.writeObject(getEvents());            //  Save events
        }
    }

    /**
     * Method to load the state of the lab from a file
     * @param file from where the data will be loaded
     * @throws IOException if something happens when reading the data
     * @throws ClassNotFoundException if the file wasn't a previously saved file
     */
    public synchronized void loadLabState(File file) throws IOException, ClassNotFoundException 
    {
        //  Pause the LabMonitor before loading anything
        if(!isPaused)  
        {
            pause();
        }

        //  Load save file data

        //  Use ObjectInputStream to write data on the file
        //  Try to handle closing and unexpected closing automatically
        try (ObjectInputStream fileReader = new ObjectInputStream(new FileInputStream(file))) 
        {

            //  I suppress the warnings because i am certain of the order and type safety
            @SuppressWarnings ("unchecked")
            List<User> loadedUsers = (List<User>) fileReader.readObject();
            @SuppressWarnings ("unchecked")
            List<Equipment> loadedEquipment = (List<Equipment>) fileReader.readObject();
            @SuppressWarnings ("unchecked")
            List<Request> loadedRequests = (List<Request>) fileReader.readObject();
            @SuppressWarnings ("unchecked")
            List<String> loadedEvents = (List<String>) fileReader.readObject();
            
            //  Update the monitor's lists
            users.clear();
            users.addAll(loadedUsers);
            equipment.clear();
            equipment.addAll(loadedEquipment);
            requests.clear();
            requests.addAll(loadedRequests);
            events.clear();
            events.addAll(loadedEvents);
            
        }   

        //  Regenerate loaded pending threads
        regeneratePendingThreads();

        addEvent("The file has been read, the lab is now loaded and ready");
    }

    /**
     *  Service method to regenerate loaded pending threads
     */
    private void regeneratePendingThreads()
    {
        for(Request r : requests)
        {
            if(r.getStatus() == RequestStatus.PENDING)
            {
                RequestTask task = new RequestTask(r, this);
                task.setRegenerated();  //  Skip the addRequest call
                Thread requestThread = new Thread(task);
                requestThread.start();
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
