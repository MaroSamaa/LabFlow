package concurrent;

public class User implements Runnable 
{

    private String name;

    public User(String name)
    {
        this.name = name;
    }

    public void run()
    {
        Thread.currentThread().setName(name);
    }

}
