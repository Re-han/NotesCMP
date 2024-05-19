import androidx.compose.ui.text.capitalize
import domain.SortOrder
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun epochToNormalTime(time: Long): String {
    val instant = Instant.fromEpochSeconds(time)
    val date = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
    return "${date.year} ${date.month.name} ${date.dayOfMonth}"
}

fun stringToSortOrder(sortOrder: String?): SortOrder {
    return when (sortOrder) {
        SortOrder.Sort_By_Date_Latest.toString() -> {
            SortOrder.Sort_By_Date_Latest
        }

        SortOrder.Sort_By_Asc.toString() -> {
            SortOrder.Sort_By_Asc
        }

        SortOrder.Sort_By_Desc.toString() -> {
            SortOrder.Sort_By_Desc
        }

        SortOrder.Sort_By_Date_Oldest.toString() -> {
            SortOrder.Sort_By_Date_Oldest
        }
        else -> {
            SortOrder.Sort_By_Date_Latest
        }
    }
}