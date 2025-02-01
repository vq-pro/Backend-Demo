package quebec.virtualite.utils;

import io.cucumber.datatable.DataTable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

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
        return DataTable.create(
            concat(
                Stream.of(header),
                items.stream().map(forEachItem))
                .collect(toList()));
    }
}
