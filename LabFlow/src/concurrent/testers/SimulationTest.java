package concurrent.testers;

import concurrent.*;

/**
 *  @author Marini Enrico
 *  SimulationTest is a test class which checks if the expected behaviour of the program is correct
 *  it adds two resources and three users which all make a request for either one of the two resources
 *  this helps in checking that the resources get assigned to the fastest and if no resources are left 
 *  the thread goes awaits.
 *  After a fixed "simulated work time" the resources get returned and one of them gets assigned to 
 *  the still pending request.
*/
public class SimulationTest 
{
    public static void main(String[] args)
    {
        LabMonitor monitor = new LabMonitor();

        monitor.addEquipment(new Equipment("Oscilloscope", "OSC-01"));
        monitor.addEquipment(new Equipment("Oscilloscope", "OSC-02"));
        monitor.addEquipment(new Equipment("Function Generator", "FGN-01"));

        User u1 = new User("Maria");
        User u2 = new User("Greta");
        User u3 = new User("Alessia");

        Request r1 = new Request(u1,"Oscilloscope");
        Request r2 = new Request(u2,"Oscilloscope");
        Request r3 = new Request(u3,"Oscilloscope");
        Request r4 = new Request(u1,"Function Generator");

        Thread t1 = new Thread(new RequestTask(r1, monitor));
        Thread t2 = new Thread(new RequestTask(r2, monitor));
        Thread t3 = new Thread(new RequestTask(r3, monitor));
        Thread t4 = new Thread(new RequestTask(r4,monitor));

        System.out.println("Starting Simulation");

        t1.start();
        t2.start();
        t3.start();
        t4.start();

    }
}
