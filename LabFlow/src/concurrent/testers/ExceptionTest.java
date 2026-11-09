package concurrent.testers;

import concurrent.*;
import concurrent.exceptions.*;

/**
 *  @author Marini Enrico
 *  Test Class to test if the exceptions get thrown as expected.
*/
public class ExceptionTest 
{
    public static void main(String[] args)
    {
        LabMonitor monitor = new LabMonitor();
        
        //  Trying to add two equipments with the same name
        try
        {
            monitor.addEquipment(new Equipment("Oscilloscope", "OSC-01"));  
            monitor.addEquipment(new Equipment("Oscilloscope", "OSC-01"));
        }catch(DuplicateEquipmentCodeException e){System.out.println(e.getMessage());}

        //  Trying to add more equipments than allowed by the max limit
        try
        {  
            monitor.addEquipment(new Equipment("Oscilloscope", "OSC-02"));
            monitor.addEquipment(new Equipment("Oscilloscope", "OSC-03"));  
            monitor.addEquipment(new Equipment("Function Generator", "FGN-01"));    
            monitor.addEquipment(new Equipment("Function Generator", "FGN-02"));    
        }catch(EquipmentStorageFullException e){System.out.println(e.getMessage());}

        //  Trying to give back an equipment that was never obtained
        try
        {
            Request r1 = new Request(new User("Tester"), "Oscilloscope");
            monitor.releaseEquipment(r1);
        }catch(IllegalStateException e){System.out.println(e.getMessage());}

        //  Trying to add two users with the same name
        try
        {
            monitor.addUser(new User("TestName"));
            monitor.addUser(new User("TestName"));
        }catch(DuplicateUserNameException e){System.out.println(e.getMessage());}
    }
}
