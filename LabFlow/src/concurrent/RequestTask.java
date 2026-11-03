package concurrent;

public class RequestTask implements Runnable
{
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

    }
}
