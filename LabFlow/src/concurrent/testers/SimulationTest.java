package concurrent.testers;

import java.util.ArrayList;

import javax.swing.SwingUtilities;

import org.w3c.dom.events.Event;

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

        //  Adding equipment to the lab
        monitor.addEquipment(new Equipment("Oscilloscope", "OSC-01"));
        monitor.addEquipment(new Equipment("Oscilloscope", "OSC-02"));
        monitor.addEquipment(new Equipment("Function Generator", "FGN-01"));

        //  Creating users
        User u1 = new User("Maro","001");
        User u2 = new User("Sara","002");
        User u3 = new User("Lia","003");
        User u4 = new User("Alessia","004");
        User u5 = new User("Enea","005");

        //  Creating requests
        Request r1 = new Request(u1,"Oscilloscope");
        Request r2 = new Request(u2,"Oscilloscope");
        Request r3 = new Request(u3,"Oscilloscope");
        Request r4 = new Request(u4,"Function Generator");
        Request r5 = new Request(u5,"Function Generator");

        //  Set a smoller work time for faster testing
        RequestTask.setWorkTime(1000);

        //  Creating threads (one for each request)
        Thread t1 = new Thread(new RequestTask(r1, monitor));
        Thread t2 = new Thread(new RequestTask(r2, monitor));
        Thread t3 = new Thread(new RequestTask(r3, monitor));
        Thread t4 = new Thread(new RequestTask(r4,monitor));
        Thread t5 = new Thread(new RequestTask(r5, monitor));

        System.out.println("Starting Simulation");

        //  Starting Threads
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        monitor.play();

        //  Wait until all threads end their execution
        try
        {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
        }catch(InterruptedException e) {System.out.println("Simulation error");}

        //  Print the lab's event log
        ArrayList<String> eventList = monitor.getEvents();
        for(String s:eventList)
        {
            System.out.println(s);
        }

    }

}
