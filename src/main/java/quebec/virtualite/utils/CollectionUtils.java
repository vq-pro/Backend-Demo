package quebec.virtualite.utils;

import java.util.ArrayList;
import java.util.List;

public abstract class CollectionUtils
{
    @SafeVarargs
    public static <T> List<T> list(T... items)
    {
        return new ArrayList<>(List.of(items));
    }
}
