package concurrent.exceptions;

/**
 * Exception to address when two equipment have the same code
 * @author Marini Enrico
 */
public class DuplicateEquipmentCodeException extends RuntimeException
{
    public DuplicateEquipmentCodeException(String msg) 
    {
        super(msg);
    }
}
