package concurrent;

/**
 * Functional interface that allows for 
 * LabMonitor's listening.
 * @author Marini Enrico
 */
@FunctionalInterface 
public interface LabListener 
{
    /**
     * Method that will get called when something in the LabMonitor happens
     */
    void onLabStateChange();
}
