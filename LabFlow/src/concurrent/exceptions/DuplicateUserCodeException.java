package concurrent.exceptions;

/**
 *  Exception to address when two users have the same name
 */
public class DuplicateUserCodeException extends RuntimeException
{
    public DuplicateUserCodeException(String msg)
    {
        super(msg);
    }
}
