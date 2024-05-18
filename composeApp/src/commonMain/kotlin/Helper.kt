import androidx.compose.ui.text.capitalize
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun epochToNormalTime(time: Long): String {
    val instant = Instant.fromEpochSeconds(time)
    val date = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
    return "${date.year} ${date.month.name} ${date.dayOfMonth}"
}
