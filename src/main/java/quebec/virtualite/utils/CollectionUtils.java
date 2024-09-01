package quebec.virtualite.utils;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public abstract class CollectionUtils
{
    public static String commaSeparatedList(String... entries)
    {
        return commaSeparatedList(List.of(entries));
    }

    public static String commaSeparatedList(List<String> entries)
    {
        StringBuilder output = new StringBuilder();
        for (String entry : entries)
        {
            if (output.length() != 0)
                output.append(", ");

            output.append(entry);
        }

        return output.toString();
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
        return List.of(item1, item2);
    }

    public static <A, B> List<B> transform(List<A> items, Function<A, B> forEachItem)
    {
        return items
            .stream()
            .map(forEachItem)
            .collect(toList());
    }
}
