package concurrent.exceptions;

/**
 *  Exception to address when two users have the same name
 */
public class DuplicateUserNameException extends RuntimeException
{
    public DuplicateUserNameException(String msg)
    {
        super(msg);
    }
}
