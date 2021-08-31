package kotlinholidaytracker.kotlinholidaytracker.repository

import kotlinholidaytracker.kotlinholidaytracker.entity.Holiday
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface HolidayRepository : JpaRepository<Holiday, Long> {
  fun findHolidayByName(name: String?): Optional<Holiday>
}