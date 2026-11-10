package concurrent;

/**
 * @author Marini Enrico
 * Class that makes requests "runnable"
 */
public class RequestTask implements Runnable
{

    /*  
        Static because they are shared, volatile to ensure the consistency of the variables
        between threads without introducing synchronized methods (that produce overheads), this should be
        enough given that the operations on them are limited read / write of the whole variable in one go.
        It tells the thread to read the info from the ram instead of the local chache value (which might be an older one)
    */
    /** 
     * Total simulated work time on an equipment before releasing it;
     * defaul value = 3000
     * */ 
    private static volatile int workTime = 3000;
    /**
     *  Default value = 100
     *  Intervall of work time before check on the monitor's pause status
     * */ 
    private static volatile int workTimeIntervall = 100;

    //--------INSTANCE VARIABLES--------

    /**
     * The monitor to work on
     */
    private LabMonitor monitor;

    /**
     * The request to "execute"
     */
    private Request request;

    private boolean isRegenerated;

    //--------CONSTRUCTOR--------

    public RequestTask(Request request, LabMonitor monitor)
    {
        this.monitor = monitor;
        this.request = request;
        isRegenerated = false;  //  Normally the request won't be regenerated
    }

    //--------ACCESSOR METHODS--------
    public boolean isRegenerated()
    {
        return isRegenerated;
    }

    public void setRegenerated()
    {
        isRegenerated = true;
    }

    //--------ACCESSOR METHODS--------
    public static int getWorkTime()
    {
        return workTime;
    }

    public static int getWorkTimeIntervall()
    {
        return workTimeIntervall;
    }

    public static void setWorkTime(int newWorkTime) throws IllegalArgumentException
    {
        if(newWorkTime <= 0)
        {
            throw new IllegalArgumentException("The work time value must be > 0");
        }
        workTime = newWorkTime;
    }

    public static void setWorkTimeIntervall(int newWorkTimeIntervall) throws IllegalArgumentException
    {
        if(newWorkTimeIntervall <= 0)
        {
            throw new IllegalArgumentException("The time intervall value must be > 0");
        }
        workTimeIntervall = newWorkTimeIntervall;
    }

    //--------METHODS--------

    @Override 
    public void run()
    {
        try
        {
            //  If the requestTask was regenerated then don't add it to the list, it would be a duplicate
            if(!isRegenerated)
            {
                monitor.addRequest(request);
            }
            
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
        while(timeWorked < workTime)
        {
            monitor.checkPaused();
            Thread.sleep(workTimeIntervall);
            timeWorked += workTimeIntervall;
        }
    }
}

