package concurrent.testers;

import concurrent.*;

public class ExceptionTest 
{
    public static void main(String[] args)
    {
        LabMonitor monitor = new LabMonitor();
        //Trying to add more equipments than allowed by the max limit
        try
        {
            monitor.addEquipment(new Equipment("Oscilloscope", "OSC-01"));  
            monitor.addEquipment(new Equipment("Oscilloscope", "OSC-02"));  
            monitor.addEquipment(new Equipment("Oscilloscope", "OSC-03"));  
            monitor.addEquipment(new Equipment("Function Generator", "FGN-01"));    
            monitor.addEquipment(new Equipment("Function Generator", "FGN-02"));    
        }catch(IllegalStateException e){System.out.println(e.getMessage());};


        //Trying to give back an equipment that was never obtained
        try
        {
            Request r1 = new Request(new User("Tester"), "Oscilloscope");
            monitor.releaseEquipment(r1);
        }catch(IllegalStateException e){System.out.println(e.getMessage());};
        


    }
}
