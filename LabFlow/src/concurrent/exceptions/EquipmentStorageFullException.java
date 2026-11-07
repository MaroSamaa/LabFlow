package concurrent.exceptions;

/**
 * Exception to address when the lab can't add more equipment due to de storage
 * being full.
 */
public class EquipmentStorageFullException extends RuntimeException
{
    public EquipmentStorageFullException(String msg)
    {
        super(msg);
    }
}
