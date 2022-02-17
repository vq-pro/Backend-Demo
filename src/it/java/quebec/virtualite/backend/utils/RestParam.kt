package quebec.virtualite.backend.utils;

public class RestParam
{
    String key;
    Object value;

    public static RestParam param(String key, Object value)
    {
        RestParam param = new RestParam();
        param.key = key;
        param.value = value;

        return param;
    }
}
