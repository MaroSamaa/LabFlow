package concurrent;

/**
 * @author Marini Enrico
 * Represents the possible status of a request
 */
public enum RequestStatus 
{
    PENDING("Pending"),
    ACTIVE("Active"),
    CLOSED("Closed"),
    CANCELLED("Cancelled");

    /**
     * The label of the status
     */
    private final String label;

    /**
     * Constructor method for RequestStatus
     * @param label the label of the status
     */
    RequestStatus(String label)
    {
        this.label = label;
    }

    /**
     * Getter method for the status label
     * @return the status label
     */
    public String getLabel()
    {
        return label;
    }

}
