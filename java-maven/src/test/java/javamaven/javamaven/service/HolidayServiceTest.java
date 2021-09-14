package javamaven.javamaven.service;

import javamaven.javamaven.entity.Holiday;
import javamaven.javamaven.entity.User;
import javamaven.javamaven.exception.BadRequestException;
import javamaven.javamaven.exception.NotFoundException;
import javamaven.javamaven.repository.HolidayRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class HolidayServiceTest {

    @Mock
    private HolidayRepository mockHolidayRepository;

    private HolidayService holidayServiceTest;

    @BeforeEach
    void setUp() {
        holidayServiceTest = new HolidayService(mockHolidayRepository);
    }

    // Basic mock value
    private Long mockHolidayId = 1L;
    private String mockName = "mockName";
    private String mockDate = "2020-01-01";
    private String mockNewName = "mockNewName";

    @Test
    void canGetAllHolidays() {
        holidayServiceTest.getAllHolidays();

        verify(mockHolidayRepository).findAll();
    }

    @Test
    void canGetHolidayById() {
        Holiday holiday = new Holiday(mockName, mockDate);

        Optional<Holiday> holidayOptional = Optional.of(holiday);

        // To allow .findById to return an Optional object
        given(mockHolidayRepository.findById(mockHolidayId))
                .willReturn(holidayOptional);

        holidayServiceTest.getHolidayById(mockHolidayId);

        verify(mockHolidayRepository).findById(mockHolidayId);
    }

    @Test
    void cannotGetHolidayById() {
        Optional<Holiday> holidayOptional = Optional.empty();

        // To allow .findById to return an Optional object
        given(mockHolidayRepository.findById(mockHolidayId))
                .willReturn(holidayOptional);

        assertThatThrownBy(() -> holidayServiceTest.getHolidayById(mockHolidayId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Holiday not found");

        verify(mockHolidayRepository).findById(mockHolidayId);
    }

    @Test
    void canGetHolidayByName() {
        Holiday holiday = new Holiday(mockName, mockDate);

        Optional<Holiday> holidayOptional = Optional.of(holiday);

        given(mockHolidayRepository.findHolidayByName(mockName))
                .willReturn(holidayOptional);

        holidayServiceTest.getHolidayByName(mockName);

        verify(mockHolidayRepository).findHolidayByName(mockName);
    }

    @Test
    void cannotGetHolidayByName() {
        Optional<Holiday> holidayOptional = Optional.empty();

        given(mockHolidayRepository.findHolidayByName(mockName))
                .willReturn(holidayOptional);

        assertThatThrownBy(() -> holidayServiceTest.getHolidayByName(mockName))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Holiday not found");

        verify(mockHolidayRepository).findHolidayByName(mockName);
    }

    @Test
    void canAddNewHoliday() {
        Holiday holiday = new Holiday(mockName, mockDate);

        holidayServiceTest.addNewHoliday(holiday);

        ArgumentCaptor<Holiday> holidayArgumentCaptor = ArgumentCaptor.forClass(Holiday.class);

        verify(mockHolidayRepository).save(holidayArgumentCaptor.capture());

        Holiday capturedUser = holidayArgumentCaptor.getValue();

        assertThat(capturedUser).isEqualTo(holiday);

        verify(mockHolidayRepository).save(holiday);

    }

    @Test
    void shouldThrowMessageIfUnableToAddNewHolidayDueToExistingName() {
        Holiday holiday = new Holiday(mockName, mockDate);

        Optional<Holiday> holidayOptional = Optional.of(holiday);

        given(mockHolidayRepository.findHolidayByName(mockName))
                .willReturn(holidayOptional);

        assertThatThrownBy(() -> holidayServiceTest.addNewHoliday(holiday))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Holiday already exist");

        verify(mockHolidayRepository, never()).save(any());
    }

    @Test
    void canUpdateHoliday() {
        Holiday holiday = new Holiday(mockName, mockDate);
        Holiday updatingHolidayInput = new Holiday(mockNewName, mockDate);

        Optional<Holiday> holidayOptional = Optional.of(holiday);

        given(mockHolidayRepository.findById(mockHolidayId))
                .willReturn(holidayOptional);

        Holiday returnHoliday = holidayServiceTest.updateHoliday(mockHolidayId, updatingHolidayInput);

        verify(mockHolidayRepository).findById(mockHolidayId);

        assertThat(returnHoliday.getName()).isEqualTo(updatingHolidayInput.getName());
    }

    @Test
    void cannotUpdateHolidayDueToWrongId() {
        Holiday updatingHolidayInput = new Holiday(mockNewName, mockDate);

        Optional<Holiday> holidayOptional = Optional.empty();

        given(mockHolidayRepository.findById(mockHolidayId))
                .willReturn(holidayOptional);

        assertThatThrownBy(() -> holidayServiceTest.updateHoliday(mockHolidayId, updatingHolidayInput))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Holiday not found");

        verify(mockHolidayRepository).findById(mockHolidayId);
    }

    @Test
    void canDeleteHoliday() {
        given(mockHolidayRepository.existsById(mockHolidayId))
                .willReturn(true);

        holidayServiceTest.deleteHoliday(mockHolidayId);

        verify(mockHolidayRepository).existsById(mockHolidayId);
    }

    @Test
    void cannotDeleteHoliday() {
        given(mockHolidayRepository.existsById(mockHolidayId))
                .willReturn(false);

        assertThatThrownBy(() -> holidayServiceTest.deleteHoliday(mockHolidayId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Holiday does not exist");

        verify(mockHolidayRepository).existsById(mockHolidayId);
    }


    @Test
    void canFindHolidayInRange() {
        String mockStartDate = "2000-01-01";
        String mockEndDate = "2100-01-01";

        Holiday holiday = new Holiday(mockName, mockDate);

        List<Holiday> holidayList = new ArrayList<>();
        holidayList.add(holiday);

        given(mockHolidayRepository.findAll())
                .willReturn(holidayList);

        List<Holiday> outputList = holidayServiceTest.findHolidayInRange(mockStartDate, mockEndDate);

        assertThat(outputList).isEqualTo(holidayList);

        verify(mockHolidayRepository).findAll();
    }

    @Test
    void cannotFindHolidayInRangeDueToWrongDateFormat() {
        String mockStartDate = "2000-01";
        String mockEndDate = "2100-01-01";

        Holiday holiday = new Holiday(mockName, mockDate);

        List<Holiday> holidayList = new ArrayList<>();
        holidayList.add(holiday);

        given(mockHolidayRepository.findAll())
                .willReturn(holidayList);

        assertThatThrownBy(() -> holidayServiceTest.findHolidayInRange(mockStartDate, mockEndDate))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Wrong date format. Please provide the date of the holiday in the format of YYYY-MM-DD");

        verify(mockHolidayRepository).findAll();
    }

    @Test
    void canFindDatesForAnnualLeaves() {
        mockDate = "2020-01-07";
        String alCorrespondToMockDate = "2020-01-06";

        Holiday holiday = new Holiday(mockName, mockDate);
        List<Holiday> holidayList = new ArrayList<>();
        holidayList.add(holiday);

        given(mockHolidayRepository.findAll())
                .willReturn(holidayList);

        List<Holiday> outputList = holidayServiceTest.findDatesForAnnualLeaves();

        assertThat(outputList.get(0).getDate()).isEqualTo(alCorrespondToMockDate);

        verify(mockHolidayRepository).findAll();
    }
}