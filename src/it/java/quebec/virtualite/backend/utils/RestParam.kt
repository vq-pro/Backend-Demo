package quebec.virtualite.backend.utils

data class RestParam
    (
    val key: String,
    val value: Any
)
{
    companion object
    {
        fun param(key: String, value: Any): RestParam
        {
            return RestParam(key, value)
        }
    }
}