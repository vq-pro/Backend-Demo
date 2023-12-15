package quebec.virtualite.utils

import java.util.stream.Collectors.toList

object CollectionUtils
{
    fun <A, B> transform(
        items: List<A>,
        lambda: (A) -> B

    ): List<B>
    {
        return items
            .stream()
            .map(lambda)
            .collect(toList())
    }
}