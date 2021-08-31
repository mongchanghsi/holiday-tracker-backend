package kotlinholidaytracker.kotlinholidaytracker.service

import kotlinholidaytracker.kotlinholidaytracker.entity.Holiday
import kotlinholidaytracker.kotlinholidaytracker.exception.BadRequestException
import kotlinholidaytracker.kotlinholidaytracker.exception.NotFoundException
import kotlinholidaytracker.kotlinholidaytracker.repository.HolidayRepository
import org.springframework.stereotype.Service
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Service
class HolidayService(private val holidayRepository: HolidayRepository) {
  fun getAllHolidays(): List<Holiday> {
    return holidayRepository.findAll()
  }

  fun getHolidayById(id: Long): Holiday {
    val holidayById: Optional<Holiday> = holidayRepository.findById(id)
    if (holidayById.isEmpty) throw NotFoundException("Holiday not found")
    return holidayById.get()
  }

  fun getHolidayByName(name: String): Holiday {
    val holidayById: Optional<Holiday> = holidayRepository.findHolidayByName(name)
    if (holidayById.isEmpty) throw NotFoundException("Holiday not found")
    return holidayById.get()
  }

  fun addNewHoliday(holiday: Holiday): Holiday {
    val holidayByName: Optional<Holiday> = holidayRepository.findHolidayByName(holiday.name)
    if (holidayByName.isPresent) throw BadRequestException("Holiday already exist")
    return holidayRepository.save(holiday)
  }

  fun updateHoliday(id: Long, holiday: Holiday): Holiday {
    val holidayById: Optional<Holiday> = holidayRepository.findById(id)
    if (holidayById.isEmpty) throw NotFoundException("Holiday not found")
    holidayById.get().name = holiday.name
    holidayById.get().date = holiday.date
    holidayRepository.save(holidayById.get())
    return holidayById.get()
  }

  fun deleteHoliday(id: Long) {
    val holidayById: Optional<Holiday> = holidayRepository.findById(id)
    if (holidayById.isEmpty) throw BadRequestException("Holiday not found")
    holidayRepository.deleteById(id)
  }

  fun findHolidayInRange(startDate: String, endDate: String): List<Holiday> {
    val output: MutableList<Holiday> = mutableListOf()
    val allHolidays = holidayRepository.findAll()

    val df: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    val startDateMilli = df.parse(startDate).time
    val endDateMilli = df.parse(endDate).time

    try {
      allHolidays.forEach {
        val currentDateMilli = df.parse(it.date).time
        if (currentDateMilli in startDateMilli until endDateMilli + 1) {
          output.add(it)
        }
      }
    } catch (e: Exception) {
      println("Exception occurred $e")
    }

    return output
  }

  fun findDatesForAnnualLeaves(): List<Holiday> {
    // Assuming to take only 1 extra AL
    val output: MutableList<Holiday> = mutableListOf()
    val allHolidays = holidayRepository.findAll()

    val df = SimpleDateFormat("yyyy-MM-dd")

    try {
      allHolidays.forEach {
        val date2 = df.parse(it.date)
        val df2: DateFormat = SimpleDateFormat("EEEE")
        val day = df2.format(date2)

        var dayToALMilli = date2.time
        if (day == "Tuesday") {
          dayToALMilli -= 86400000
        }

        if (day == "Thursday") {
          dayToALMilli += 86400000
        }

        if (day == "Tuesday" || day == "Thursday") {
          val dayToAL = df.format(Date(dayToALMilli))
          output.add(Holiday(UUID.randomUUID().mostSignificantBits, "Annual Leave", dayToAL))
        }
      }
    } catch (e: Exception) {
      println("Exception occurred $e")
    }
    return output
  }
}