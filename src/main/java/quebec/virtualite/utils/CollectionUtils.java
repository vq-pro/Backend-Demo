package quebec.virtualite.utils;

import java.util.ArrayList;
import java.util.List;

public abstract class CollectionUtils
{
    public static boolean isNull(Object object)
    {
        return object == null;
    }

    public static boolean isNullOrEmpty(String string)
    {
        return isNull(string) || string.isEmpty();
    }

    @SafeVarargs
    public static <T> List<T> list(T... items)
    {
        return new ArrayList<>(List.of(items));
    }
}
