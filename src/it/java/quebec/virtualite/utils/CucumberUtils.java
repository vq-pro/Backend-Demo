package quebec.virtualite.utils;

import io.cucumber.datatable.DataTable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static quebec.virtualite.utils.CollectionUtils.list;

public abstract class CucumberUtils
{
    public static List<String> header(String... columns)
    {
        return list(columns);
    }

    public static List<String> row(String... columns)
    {
        return list(columns);
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
