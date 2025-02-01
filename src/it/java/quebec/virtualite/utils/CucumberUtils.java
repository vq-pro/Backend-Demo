package quebec.virtualite.utils;

import io.cucumber.datatable.DataTable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class CucumberUtils
{
    public static List<String> header(String... columns)
    {
        return List.of(columns);
    }

    public static List<String> row(String... columns)
    {
        return List.of(columns);
    }

    @SafeVarargs
    public static DataTable tableFrom(List<String>... rows)
    {
        return DataTable.create(List.of(rows));
    }

    public static <T> DataTable tableFrom(
        List<T> items,
        List<String> header,
        Function<T, List<String>> forEachItem)
    {
        List<List<String>> actual = new ArrayList<>();
        actual.add(header);

        for (T item : items)
        {
            actual.add(forEachItem.apply(item));
        }

        return DataTable.create(actual);
    }
}
