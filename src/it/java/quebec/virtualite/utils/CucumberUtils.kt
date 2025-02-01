package quebec.virtualite.utils

import io.cucumber.datatable.DataTable

object CucumberUtils
{
    fun header(vararg columns: String) = columns.toList()

    fun row(vararg columns: String) = columns.toList()

    fun tableFrom(vararg rows: List<String>): DataTable
    {
        return DataTable.create(rows.toMutableList())
    }

    fun <T> tableFrom(items: Array<T>, header: List<String>, forEachItem: (T) -> List<String>): DataTable
    {
        return DataTable.create(listOf(header) + items.map(forEachItem))
    }
}