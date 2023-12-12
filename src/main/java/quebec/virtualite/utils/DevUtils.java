package quebec.virtualite.utils;

public class DevUtils
{
    private DevUtils()
    {
    }

    public static <T> T IMPLEMENT()
    {
        throw new AssertionError("Implement this");
    }

    public static <T> T NEXT()
    {
        throw new AssertionError("GOOD! Next case!");
    }
}
