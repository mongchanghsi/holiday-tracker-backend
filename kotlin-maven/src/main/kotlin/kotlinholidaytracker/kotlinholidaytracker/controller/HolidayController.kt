package kotlinholidaytracker.kotlinholidaytracker.controller

import kotlinholidaytracker.kotlinholidaytracker.entity.Holiday
import kotlinholidaytracker.kotlinholidaytracker.exception.BadRequestException
import kotlinholidaytracker.kotlinholidaytracker.service.HolidayService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat

class RangeInput (val startDate: String, val endDate: String)

@RestController
@RequestMapping("api/holiday")
class HolidayController(private val holidayService: HolidayService) {
  @GetMapping
  fun getHolidays(): List<Holiday> {
    return holidayService.getAllHolidays()
  }

  @GetMapping("/search")
  fun getHoliday(@RequestParam(required = false) id: Long?, @RequestParam(required = false) name: String?): Holiday? {
    if (id == null && name == null) throw BadRequestException("Please provide either id or name of the holiday")

    var holiday: Holiday? = null
    if (id != null) {
      holiday =  holidayService.getHolidayById(id)
    } else if (name != null) {
      holiday = holidayService.getHolidayByName(name)
    }
    return holiday
  }

  @GetMapping("/range")
  fun getHolidayByRange(@RequestBody dateRangeInput: RangeInput): List<Holiday> {
    checkHolidayDateInput(dateRangeInput.startDate)
    checkHolidayDateInput(dateRangeInput.endDate)
    return holidayService.findHolidayInRange(dateRangeInput.startDate, dateRangeInput.endDate)
  }

  @GetMapping("/annualleave")
  fun getDatesToTakeAnnualLeaves(): List<Holiday> {
    return holidayService.findDatesForAnnualLeaves()
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  fun addHoliday(@RequestBody holiday: Holiday): Holiday {
    checkHolidayNameInput(holiday.name)
    checkHolidayDateInput(holiday.date)
    return holidayService.addNewHoliday(holiday)
  }

  @PutMapping("/{id}")
  fun updateHoliday(@PathVariable id: Long, @RequestBody holiday: Holiday): Holiday {
    checkHolidayNameInput(holiday.name)
    checkHolidayDateInput(holiday.date)
    return holidayService.updateHoliday(id, holiday)
  }

  @DeleteMapping("/{id}")
  fun deleteHoliday(@PathVariable id: Long) {
    holidayService.deleteHoliday(id)
  }

  fun checkHolidayNameInput(name: String?) {
    if (name.isNullOrEmpty()) throw BadRequestException("Please provide a holiday name")
  }

  fun checkHolidayDateInput(date: String?) {
    if (date.isNullOrEmpty()) throw BadRequestException("Please provide the date of the holiday in the format of YYYY-MM-DD")

    if (date.toString().length !== 10) {
      throw BadRequestException("Wrong date format. Please provide the date of the holiday in the format of YYYY-MM-DD")
    }

    try {
      val df: DateFormat = SimpleDateFormat("yyyy-MM-dd")
      val after = df.format(df.parse(date))

      // To catch wrong month/day e.g. 2020-01-40
      if (after != date) {
        throw BadRequestException("Invalid date. Please provide the date of the holiday in the format of YYYY-MM-DD")
      }
    } catch (e: ParseException) {
      // To catch wrong format e.g. yyyy-MMdd
      throw BadRequestException("Wrong date format. Please provide the date of the holiday in the format of YYYY-MM-DD")
    }
  }
}