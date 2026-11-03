package concurrent;

public class RequestTask implements Runnable
{
    private final static int WORK_TIME = 1000;  //  Variable to set the time to simulate "work with the equipment"

    private LabMonitor monitor;
    private Request request;

    public RequestTask(Request request, LabMonitor monitor)
    {
        this.monitor = monitor;
        this.request = request;
    }

    @Override 
    public void run()
    {
        try
        {
            System.out.println("User: " + request.getUser().getName() + " made a request for: "+ request.getEquipmentType());
            monitor.requestEquipment(request); //   Request the equipment and wait until you get it

            Thread.sleep(WORK_TIME);

            monitor.releaseEquipment(request);  //  Return the equipment to the lab so it can be used by someone else

        }catch(InterruptedException e)
        {
            Thread.currentThread().interrupt(); //  Reset the interrupted flag "manually" to true (java forgets it was interrupted)
            System.out.println("ERROR: thread has been interrupted");   //to be replaced with event log message integration
        }
    };
}

