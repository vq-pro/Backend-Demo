package quebec.virtualite.utils

import java.util.stream.Collectors.toList

object CollectionUtils
{
    fun commaSeparatedList(vararg entries: String?): String
    {
        return commaSeparatedList(listOf(*entries))
    }

    fun commaSeparatedList(entries: List<String?>): String
    {
        val output = StringBuilder()
        for (entry in entries)
        {
            if (output.isNotEmpty())
                output.append(", ")

            output.append(entry)
        }

        return output.toString()
    }

    fun <A, B> map(
        items: List<A>,
        forEachItem: (A) -> B

    ): List<B>
    {
        return items
            .stream()
            .map(forEachItem)
            .collect(toList())
    }

    fun nameAndBrackets(name: String, brackets: String): String
    {
        return "$name ($brackets)"
    }

    fun nameAndBrackets(name: String, brackets: List<String?>): String
    {
        return name + " (" + commaSeparatedList(brackets) + ")"
    }
}