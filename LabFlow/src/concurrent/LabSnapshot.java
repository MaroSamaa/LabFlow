package concurrent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class enables the creation of snapshots of the lab state.
 * @author Marini Enrico
 */
public class LabSnapshot implements Serializable 
{
    private static final long serialVersionUID = 1L;
    
    //--------INSTANCE VARIABLES--------

    private final List<User> users;
    private final List<Equipment> equipment;
    private final List<Request> requests;
    private final List<String> events;

    //--------ACCESSOR METHODS--------

    /**
     * Getter method for the snapsot's user list
     * @return the snapshot's user list
     */
    public List<User> getUsers()
    {
        return users;
    }

    /**
     * Getter method for the snapsot's equipment list
     * @return the snapshot's equipment list
     */
    public List<Equipment> getEquipment()
    {
        return equipment;
    }

    /**
     * Getter method for the snapsot's request list
     * @return the snapshot's request list
     */
    public List<Request> getRequests()
    {
        return requests;
    }

    /**
     * Getter method for the snapsot's event list
     * @return the snapshot's event list
     */
    public List<String> getEvents()
    {
        return events;
    }
    //--------CONSTRUCTOR--------

    /**
     * Constructor method for the LabSnapshot
     * @param users the user list to snapshot
     * @param equipment the equipment list to snapshot
     * @param requests the request list to snapshot
     * @param events the event list to snapshot
     */
    public LabSnapshot(List<User> users,List<Equipment> equipment, List<Request> requests, List<String> events)
    {
        this.users = new ArrayList<User>(users);
        this.equipment = new ArrayList<Equipment>(equipment);
        this.requests = new ArrayList<Request>(requests);
        this.events = new ArrayList<String>(events);
    }

    /**
     * Save the snapshot to a file
     * @param file the file where the snapshot will be written
     * @throws IOException if an OutputStream error occurs
     */
    public void saveToFile(File file) throws IOException
    {
        //  Use ObjectOutputStream to write data on the file
        //  Try to handle closing and unexpected closing automatically
        try (ObjectOutputStream fileWriter = new ObjectOutputStream(new FileOutputStream(file))) 
        { 
            fileWriter.writeObject(this);
        }
    }

    /**
     * Static method to create a LabSnapshot from a save file
     * @param file the file to load the snapshot data
     * @return the LabSnapshot loaded with the data from the save file
     * @throws IOException if on InputStrean error occurs
     * @throws ClassNotFoundException if the file isn't a previously saved file
     */
    public static LabSnapshot loadFromFile(File file) throws IOException, ClassNotFoundException, ClassCastException
    {
        //  Use ObjectInputStream to write data on the file
        //  Try to handle closing and unexpected closing automatically
        try (ObjectInputStream fileReader = new ObjectInputStream(new FileInputStream(file))) 
        {
            return (LabSnapshot) fileReader.readObject();
        }
    }

}
