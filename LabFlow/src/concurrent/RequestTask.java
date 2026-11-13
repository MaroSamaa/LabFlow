package concurrent;

/**
 * @author Marini Enrico
 * Class that makes requests "runnable"
 */
public class RequestTask implements Runnable
{
    public static final int DEFAULT_WORK_TIME = 3000;
    public static final int DEFAULT_WORK_TIME_INTERVALL = 100;

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
    private static volatile int workTime = DEFAULT_WORK_TIME;
    /**
     *  Default value = 100
     *  Intervall of work time before check on the monitor's pause status
     * */ 
    private static volatile int workTimeIntervall = DEFAULT_WORK_TIME_INTERVALL;

    //--------INSTANCE VARIABLES--------

    /**
     * The monitor to work on
     */
    private LabMonitor monitor;

    /**
     * The request to "execute"
     */
    private Request request;

    /**
     * True if thread was regenerated
     */
    private boolean isRegenerated;

    //--------CONSTRUCTOR--------

    public RequestTask(Request request, LabMonitor monitor)
    {
        this.monitor = monitor;
        this.request = request;
        isRegenerated = false;  //  Normally the request won't be regenerated
    }

    //--------ACCESSOR METHODS--------

    /**
     * Setter method for the isRegenerated variable
     */
    public void setRegenerated()
    {
        isRegenerated = true;
    }

    /**
     * Getter method for workTime parameter
     * @return the workTime parameter
     */
    public static int getWorkTime()
    {
        return workTime;
    }

    /**
     * Getter method for workTimeIntervall parameter
     * @return the workTimeIntervall parameter
     */
    public static int getWorkTimeIntervall()
    {
        return workTimeIntervall;
    }

    /**
     * Setter method for workTime parameter
     * @param newWorkTime the wanted workTime
     * @throws IllegalArgumentException if the new value is <= 0
     */
    public static void setWorkTime(int newWorkTime) throws IllegalArgumentException
    {
        if(newWorkTime <= 0)
        {
            throw new IllegalArgumentException("The work time value must be > 0");
        }
        workTime = newWorkTime;
    }

    /**
     * Setter method for workTimeIntervall parameter
     * @param newWorkTimeIntervall the wanted workTimeIntervall
     * @throws IllegalArgumentException if the new value is <= 0
     */
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

            simulateWork();                     //  Simulate work being done with the equipment

            monitor.releaseEquipment(request);  //  Return the equipment to the lab so it can be used by someone else

        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();                         //  Reset the interrupted flag "manually" to true (java forgets it was interrupted)
            System.out.println("ERROR: thread has been interrupted");   
        }
        catch(IllegalStateException e)
        {
            System.out.println(e.getMessage()); 
        }
    }

    /**
     * Service method that simulate a user working with an equipment; 
     * it works in smaller intervals to allow the pausing mid-work of the thread.
     * @throws InterruptedException if the thread get's interrupted for some reason
     */
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

