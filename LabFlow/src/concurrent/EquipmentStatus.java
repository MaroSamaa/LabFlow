package concurrent;

/**
 * @author Marini Enrico
 * Represents the possible status of an equipment
 */
public enum EquipmentStatus 
{
    AVAILABLE("Available"),
    IN_USE("In Use");

    /**
     * The label of the status
     */
    private String label;

    /**
     * Constructor method for EquipmentStatus
     * @param label the lable of the status
     */
    EquipmentStatus(String label)
    {
        this.label = label;
    }

    /**
     * Getter method for status label
     * @return the label of the status
     */
    public String getLabel()
    {
        return label;
    }
}
