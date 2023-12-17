package quebec.virtualite.utils

import io.cucumber.datatable.DataTable

object CucumberUtils
{
    fun <T> tableFrom(items: Array<T>, header: List<String>, forEachItem: (T) -> List<String>): DataTable
    {
        val actual = ArrayList<List<String>>()
        actual.add(header)

        for (item in items)
        {
            actual.add(forEachItem(item))
        }

        return DataTable.create(actual)
    }
}