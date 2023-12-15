package quebec.virtualite.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.util.Collections.addAll;
import static java.util.stream.Collectors.toList;

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

    public static <A, B> List<B> convert(List<A> input, Function<A, B> lambda)
    {
        return input
            .stream()
            .map(lambda)
            .collect(toList());
    }

    @SafeVarargs
    public static <T> List<T> list(T... items)
    {
        ArrayList<T> list = new ArrayList<>();
        addAll(list, items);

        return list;
    }

    public static String nameAndBrackets(String name, String brackets)
    {
        return name + " (" + brackets + ")";
    }

    public static String nameAndBrackets(String name, List<String> brackets)
    {
        return name + " (" + commaSeparatedList(brackets) + ")";
    }

    public static <T> List<T> pair(T item1, T item2)
    {
        return list(item1, item2);
    }
}
