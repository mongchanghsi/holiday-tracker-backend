package kotlinholidaytracker.kotlinholidaytracker.entity

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "holiday")
data class Holiday (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long?,

    @Column(name = "name")
    var name: String?,

    @Column(name = "date")
    var date: String?,
) {
    val day: String
        get() {
            return try {
                val df = SimpleDateFormat("yyyy-MM-dd")
                val date2 = df.parse(this.date)
                val df2: DateFormat = SimpleDateFormat("EEEE")
                df2.format(date2)
            } catch (e: ParseException) {
                e.printStackTrace()
                "Unable to parse date"
            }
        }
}