package javamaven.javamaven.repository;

import javamaven.javamaven.entity.Holiday;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class HolidayRepositoryTest {

    @Autowired
    private HolidayRepository holidayRepositoryTest;

    // Basic mock values
    private String mockName = "mockName";
    private String mockDate = "2020-01-01";

    @AfterEach
    void tearDown() {
        holidayRepositoryTest.deleteAll();
    }

    @Test
    void itShouldFindHolidayByNameIfExist() {
        Holiday holiday = new Holiday(mockName, mockDate);

        holidayRepositoryTest.save(holiday);

        Optional<Holiday> doesHolidayExist = holidayRepositoryTest.findHolidayByName(mockName);

        assertThat(doesHolidayExist.isPresent()).isTrue();
    }

    @Test
    void itShouldFindHolidayByNameIfDoesNotExist() {
        Optional<Holiday> doesHolidayExist = holidayRepositoryTest.findHolidayByName(mockName);

        assertThat(doesHolidayExist.isEmpty()).isTrue();
    }
}