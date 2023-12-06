package quebec.virtualite.utils;

import java.util.ArrayList;
import java.util.List;

public abstract class CollectionUtils
{
    public static String commaSeparatedList(String... entries)
    {
        return commaSeparatedList(list(entries));
    }

    public static String commaSeparatedList(List<String> entries)
    {
        StringBuilder output = new StringBuilder();
        for (String entry : entries)
        {
            if (!output.isEmpty())
                output.append(", ");

            output.append(entry);
        }

        return output.toString();
    }

    @SafeVarargs
    public static <T> List<T> list(T... items)
    {
        return new ArrayList<>(List.of(items));
    }

    public static String nameAndBrackets(String name, String brackets)
    {
        return name + " (" + brackets + ")";
    }

    public static String nameAndBrackets(String name, List<String> brackets)
    {
        return name + " (" + commaSeparatedList(brackets) + ")";
    }
}
