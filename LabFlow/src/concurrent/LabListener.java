package concurrent;

@FunctionalInterface 
public interface LabListener 
{
    //  Method that will be called when something in the lab changes
    void onLabStateChange();
}
