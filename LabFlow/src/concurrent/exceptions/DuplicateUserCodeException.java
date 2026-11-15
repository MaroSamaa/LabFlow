package concurrent.exceptions;

/**
 * Exception to address when two users have the same name
 * @author Marini Enrico
 */
public class DuplicateUserCodeException extends RuntimeException
{
    public DuplicateUserCodeException(String msg)
    {
        super(msg);
    }
}
