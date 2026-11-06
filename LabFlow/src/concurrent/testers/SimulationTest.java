package concurrent.testers;

import concurrent.*;

public class SimulationTest 
{
    public static void main(String[] args)
    {
        LabMonitor monitor = new LabMonitor();

        monitor.addEquipment(new Equipment("Oscilloscope", "OSC-01"));
        monitor.addEquipment(new Equipment("Oscilloscope", "OSC-02"));

        User u1 = new User("Maria");
        User u2 = new User("Greta");
        User u3 = new User("Alessia");

        Request r1 = new Request(u1,"Oscilloscope");
        Request r2 = new Request(u2,"Oscilloscope");
        Request r3 = new Request(u3,"Oscilloscope");

        Thread t1 = new Thread(new RequestTask(r1, monitor));
        Thread t2 = new Thread(new RequestTask(r2, monitor));
        Thread t3 = new Thread(new RequestTask(r3, monitor));

        System.out.println("Starting Simulation");

        t1.start();
        t2.start();
        t3.start();

    }
}
