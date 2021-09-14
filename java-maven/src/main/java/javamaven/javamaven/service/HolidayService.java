package javamaven.javamaven.service;

import javamaven.javamaven.entity.Holiday;
import javamaven.javamaven.exception.BadRequestException;
import javamaven.javamaven.exception.NotFoundException;
import javamaven.javamaven.repository.HolidayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class HolidayService {
    @Autowired
    private HolidayRepository holidayRepository;

    public HolidayService(HolidayRepository holidayRepository) { this.holidayRepository = holidayRepository; }

    public List<Holiday> getAllHolidays() { return (List<Holiday>) holidayRepository.findAll(); }

    public Holiday getHolidayById(Long id) {
        Optional<Holiday> holidayById = holidayRepository.findById(id);
        if (holidayById.isEmpty()) {
            throw new NotFoundException("Holiday not found");
        }
        return holidayById.get();
    }

    public Holiday getHolidayByName(String name){
        Optional<Holiday> holidayByName = holidayRepository.findHolidayByName(name);
        if (holidayByName.isEmpty()) {
            throw new NotFoundException("Holiday not found");
        }
        return holidayByName.get();
    }

    public Holiday addNewHoliday(Holiday holiday) {
        Optional<Holiday> holidayByName = holidayRepository.findHolidayByName(holiday.getName());
        if (holidayByName.isPresent()) {
            throw new BadRequestException("Holiday already exist");
        }
        return holidayRepository.save(holiday);
    }

    public Holiday updateHoliday(Long id, Holiday newHoliday) {
        Optional<Holiday> holidayById = holidayRepository.findById(id);
        if (holidayById.isEmpty()) throw new NotFoundException("Holiday not found");

        if (newHoliday.getName() != null && newHoliday.getName().length() > 0 && !Objects.equals(holidayById.get().getName(), newHoliday.getName())) {
            holidayById.get().setName(newHoliday.getName());
        }

        if (newHoliday.getDate() != null && newHoliday.getDate().length() > 0 && !Objects.equals(holidayById.get().getDate(), newHoliday.getDate())) {
            holidayById.get().setDate(newHoliday.getDate());
        }

        holidayRepository.save(holidayById.get());

        return holidayById.get();
    }

    public void deleteHoliday(Long id) {
        boolean holidayExist = holidayRepository.existsById(id);
        if (!holidayExist) {
            throw new NotFoundException("Holiday does not exist");
        }
        holidayRepository.deleteById(id);
    }

    public List<Holiday> findHolidayInRange(String startDate, String endDate) {
//        List<Holiday> output = Collections.emptyList();
        List<Holiday> output = new ArrayList<>();
        List<Holiday> allHolidays = holidayRepository.findAll();

        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Long startDateMilli = df.parse(startDate).getTime();
            Long endDateMilli = df.parse(endDate).getTime();

            for (Holiday holiday : allHolidays) {
                Long currentDateMilli = df.parse(holiday.getDate()).getTime();
                if (currentDateMilli > startDateMilli && currentDateMilli <= endDateMilli) {
                    output.add(holiday);
                }
            }
        } catch (ParseException e) {
            // To catch wrong format e.g. yyyy-MMdd
            throw new BadRequestException("Wrong date format. Please provide the date of the holiday in the format of YYYY-MM-DD");
        }
        return output;
    }

    public List<Holiday> findDatesForAnnualLeaves() {
        // Assuming to take only 1 extra AL
//        List<Holiday> output = Collections.emptyList();
        List<Holiday> output = new ArrayList<>();
        List<Holiday> allHolidays = holidayRepository.findAll();

        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            for (Holiday holiday : allHolidays) {
                Date _date = df.parse(holiday.getDate());
                DateFormat df2 = new SimpleDateFormat("EEEE");
                String day = df2.format(_date);

                Long dayToALMilli = _date.getTime();
                if (day.equals("Tuesday")) {
                    dayToALMilli -= 86400000;
                }

                if (day.equals("Thursday")) {
                    dayToALMilli += 86400000;
                }

                if (day.equals("Tuesday") || day.equals("Thursday")) {
                    String dayToAl = df.format(new Date(dayToALMilli));
                    Holiday alEntry = new Holiday("Annual Leave", dayToAl);
                    output.add(alEntry);
                }
            }
        } catch (ParseException e) {
            // To catch wrong format e.g. yyyy-MMdd
            throw new BadRequestException("Wrong date format. Please provide the date of the holiday in the format of YYYY-MM-DD");
        }
        return output;
    }
}
