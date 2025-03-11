package quebec.virtualite.utils;

import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public abstract class CollectionUtils
{
    public static String commaSeparatedString(String... entries)
    {
        return commaSeparatedString(List.of(entries));
    }

    public static String commaSeparatedString(List<String> entries)
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

    public static <T> List<T> listFrom(T itemToAddAtTheBeginning, List<T> list)
    {
        val newList = new ArrayList<T>();
        newList.add(itemToAddAtTheBeginning);
        newList.addAll(list);

        return newList;
    }

    public static <T> List<T> listFrom(List<T> list, T itemToAddAtTheEnd)
    {
        val newList = new ArrayList<T>(list);
        newList.add(itemToAddAtTheEnd);

        return newList;
    }

    public static <A, B> List<B> map(List<A> items, Function<A, B> forEachItem)
    {
        return items
            .stream()
            .map(forEachItem)
            .collect(toList());
    }

    public static String nameAndBrackets(String name, String brackets)
    {
        return name + " (" + brackets + ")";
    }

    public static String nameAndBrackets(String name, List<String> brackets)
    {
        return name + " (" + commaSeparatedString(brackets) + ")";
    }

    public static <T> List<T> pair(T item1, T item2)
    {
        return List.of(item1, item2);
    }

    public static Integer sum(Integer... items)
    {
        return sum(List.of(items));
    }

    public static Integer sum(List<Integer> items)
    {
        Integer sum = 0;
        for (Integer item : items)
        {
            sum += item;
        }

        return sum;
    }
}
