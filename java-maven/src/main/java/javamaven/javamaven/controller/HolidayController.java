package javamaven.javamaven.controller;

import javamaven.javamaven.entity.Holiday;
import javamaven.javamaven.exception.BadRequestException;
import javamaven.javamaven.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

class RangeInput {
    private String startDate;
    private String endDate;

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/holidays")
public class HolidayController {

    @Autowired
    private HolidayService holidayService;

    public HolidayController(HolidayService holidayService) { this.holidayService = holidayService; }

    @GetMapping
    public List<Holiday> getHolidays() {
        return holidayService.getAllHolidays();
    }

    @GetMapping("/search")
    public Holiday getHoliday(@RequestParam(required = false) Long id, @RequestParam(required = false) String name) {
        if (id == null && name == null) throw new BadRequestException("Please provide either id or name of the holiday");

        Holiday holiday = null;
        if (id != null) {
            holiday =  holidayService.getHolidayById(id);
        } else if (name != null) {
            holiday = holidayService.getHolidayByName(name);
        }
        return holiday;
    }

    @GetMapping("/range")
    public List<Holiday> getHolidayByRange(@RequestBody RangeInput dateRangeInput) {
        checkHolidayDateInput(dateRangeInput.getStartDate());
        checkHolidayDateInput(dateRangeInput.getEndDate());
        return holidayService.findHolidayInRange(dateRangeInput.getStartDate(), dateRangeInput.getEndDate());
    }

    @GetMapping("/annualleave")
    public List<Holiday> getDatesToTakeAnnualLeaves() {
        return holidayService.findDatesForAnnualLeaves();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Holiday addHoliday(@RequestBody Holiday holiday) {
        checkHolidayNameInput(holiday.getName());
        checkHolidayDateInput(holiday.getDate());
        return holidayService.addNewHoliday(holiday);
    }

    @PutMapping("/{id}")
    public Holiday updateHoliday(@PathVariable Long id, @RequestBody Holiday holiday) {
        checkHolidayNameInput(holiday.getName());
        checkHolidayDateInput(holiday.getDate());
        return holidayService.updateHoliday(id, holiday);
    }

    @DeleteMapping("/{id}")
    public void deleteHoliday(@PathVariable Long id) {
        holidayService.deleteHoliday(id);
    }

    public void checkHolidayNameInput(String name) {
        if (name.length() == 0 ) throw new BadRequestException("Please provide a holiday name");
    }

    public void checkHolidayDateInput(String date) {
        if (date.length() == 0) {
            throw new BadRequestException("Please provide the date of the holiday in the format of YYYY-MM-DD");
        }

        if (date.length() != 10) {
            throw new BadRequestException("Wrong date format. Please provide the date of the holiday in the format of YYYY-MM-DD");
        }

        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String after = df.format(df.parse(date));

            // To catch wrong month/day e.g. 2020-01-40
            if (!after.equals(date)) {
                throw new BadRequestException("Invalid date. Please provide the date of the holiday in the format of YYYY-MM-DD");
            }

        } catch (ParseException e) {
            // To catch wrong format e.g. yyyy-MMdd
            throw new BadRequestException("Wrong date format. Please provide the date of the holiday in the format of YYYY-MM-DD");
        }
    }
}
