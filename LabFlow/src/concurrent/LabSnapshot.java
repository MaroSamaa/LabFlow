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

public class LabSnapshot implements Serializable 
{
    private static final long serialVersionUID = 1L;
    
    //--------INSTANCE VARIABLES--------

    private final List<User> users;
    private final List<Equipment> equipment;
    private final List<Request> requests;
    private final List<String> events;

    //--------ACCESSOR METHODS--------

    public List<User> getUsers()
    {
        return users;
    }

    public List<Equipment> getEquipment()
    {
        return equipment;
    }

    public List<Request> getRequests()
    {
        return requests;
    }

    public List<String> getEvents()
    {
        return events;
    }
    //--------CONSTRUCTOR--------

    public LabSnapshot(List<User> users,List<Equipment> equipment, List<Request> requests, List<String> events)
    {
        this.users = new ArrayList<User>(users);
        this.equipment = new ArrayList<Equipment>(equipment);
        this.requests = new ArrayList<Request>(requests);
        this.events = new ArrayList<String>(events);
    }

    public void saveToFile(File file) throws IOException
    {
        //  Use ObjectOutputStream to write data on the file
        //  Try to handle closing and unexpected closing automatically
        try (ObjectOutputStream fileWriter = new ObjectOutputStream(new FileOutputStream(file))) 
        { 
            fileWriter.writeObject(this);
        }
    }

    public static LabSnapshot loadFromFile(File file) throws IOException, ClassNotFoundException
    {
        //  Use ObjectInputStream to write data on the file
        //  Try to handle closing and unexpected closing automatically
        try (ObjectInputStream fileReader = new ObjectInputStream(new FileInputStream(file))) 
        {
            return (LabSnapshot) fileReader.readObject();
        }
    }

}
