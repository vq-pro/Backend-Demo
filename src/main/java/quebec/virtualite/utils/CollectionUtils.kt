package quebec.virtualite.utils

import java.util.stream.Collectors.toList

object CollectionUtils
{
    fun commaSeparatedString(vararg entries: String?): String
    {
        return commaSeparatedString(listOf(*entries))
    }

    fun commaSeparatedString(entries: List<String?>): String
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

    fun <T> listFrom(itemToAddAtTheBeginning: T, list: List<T>): List<T>
    {
        val newList = ArrayList<T>()
        newList.add(itemToAddAtTheBeginning)
        newList.addAll(list)

        return newList
    }

    fun <T> listFrom(list: List<T>, itemToAddAtTheEnd: T): List<T>
    {
        val newList = ArrayList<T>()
        newList.addAll(list)
        newList.add(itemToAddAtTheEnd)

        return newList
    }

    fun <A, B> map(
        items: Array<A>,
        forEachItem: (A) -> B

    ): List<B>
    {
        return map(items.toList(), forEachItem)
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
        return name + " (" + commaSeparatedString(brackets) + ")"
    }
}
