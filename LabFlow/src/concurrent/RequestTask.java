package concurrent;

/**
 * @author Marini Enrico
 * Class that makes requests "runnable"
 */
public class RequestTask implements Runnable
{
    private final static int WORK_TIME = 3000;          //  Variable to set the time to simulate "work with the equipment"
    private final static int WORK_TIME_INTERVALL = 100; //  Variable that determines the max time that passes before checking if the monitor is paused

    //--------INSTANCE VARIABLES--------

    /**
     * The monitor to work on
     */
    private LabMonitor monitor;

    /**
     * The request to "execute"
     */
    private Request request;

    //--------CONSTRUCTOR--------

    public RequestTask(Request request, LabMonitor monitor)
    {
        this.monitor = monitor;
        this.request = request;
    }

    //--------METHODS--------

    @Override 
    public void run()
    {
        try
        {
    
            monitor.requestEquipment(request); //   Request the equipment and wait until you get it

            simulateWork();

            monitor.releaseEquipment(request);  //  Return the equipment to the lab so it can be used by someone else

        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt(); //  Reset the interrupted flag "manually" to true (java forgets it was interrupted)
            System.out.println("ERROR: thread has been interrupted");   //to be replaced with event log message integration
        }
        catch(IllegalStateException e)
        {
            System.out.println(e.getMessage()); //to be replaced with event log message integration
        }
    }

    private void simulateWork() throws InterruptedException
    {
        //  Devide the whole work in intervalls and check if the monitor is paused before doing an intervall of work
        int timeWorked = 0;
        while(timeWorked < WORK_TIME)
        {
            monitor.checkPaused();
            Thread.sleep(WORK_TIME_INTERVALL);
            timeWorked += WORK_TIME_INTERVALL;
        }
    }
}

