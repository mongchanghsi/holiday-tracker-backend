package kotlinholidaytracker.kotlinholidaytracker.service

import kotlinholidaytracker.kotlinholidaytracker.entity.Holiday
import kotlinholidaytracker.kotlinholidaytracker.exception.BadRequestException
import kotlinholidaytracker.kotlinholidaytracker.exception.NotFoundException
import kotlinholidaytracker.kotlinholidaytracker.repository.HolidayRepository
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.AssertionsForClassTypes
import org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.*
import org.mockito.BDDMockito.given
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class HolidayServiceTest {

  @Mock
  private lateinit var mockHolidayRepository: HolidayRepository

  private lateinit var holidayServiceTest: HolidayService

  @BeforeEach
  fun setUp() {
    holidayServiceTest = HolidayService(mockHolidayRepository)
  }

  // Basic mock value
  private val mockHolidayId = 1L
  private val mockName = "mockName"
  private var mockDate = "2020-01-01"
  private val mockNewName = "mockNewName"

  @Test
  fun canGetAllHolidays() {
    val result: List<Holiday> = holidayServiceTest!!.getAllHolidays()
    val expected: List<Holiday> = ArrayList()
    Assertions.assertThat(result === expected)
  }

  @Test
  fun canGetHolidayById() {
    val holiday = Holiday(mockHolidayId, mockName, mockDate)
    val holidayOptional: Optional<Holiday> = Optional.of<Holiday>(holiday)

    // To allow .findById to return an Optional object
    given(mockHolidayRepository.findById(mockHolidayId))
      .willReturn(holidayOptional)
    holidayServiceTest!!.getHolidayById(mockHolidayId)
  }

  @Test
  fun cannotGetHolidayById() {
    val holidayOptional: Optional<Holiday> = Optional.empty<Holiday>()

    // To allow .findById to return an Optional object
    given(mockHolidayRepository.findById(mockHolidayId))
      .willReturn(holidayOptional)
    assertThatThrownBy { holidayServiceTest!!.getHolidayById(mockHolidayId) }
      .isInstanceOf(NotFoundException::class.java)
      .hasMessageContaining("Holiday not found")
  }

  @Test
  fun canGetHolidayByName() {
    val holiday = Holiday(mockHolidayId, mockName, mockDate)
    val holidayOptional: Optional<Holiday> = Optional.of<Holiday>(holiday)
    given(mockHolidayRepository.findHolidayByName(mockName))
      .willReturn(holidayOptional)
    holidayServiceTest!!.getHolidayByName(mockName)
  }

  @Test
  fun cannotGetHolidayByName() {
    val holidayOptional: Optional<Holiday> = Optional.empty<Holiday>()
    given(mockHolidayRepository.findHolidayByName(mockName))
      .willReturn(holidayOptional)
    assertThatThrownBy { holidayServiceTest!!.getHolidayByName(mockName) }
      .isInstanceOf(NotFoundException::class.java)
      .hasMessageContaining("Holiday not found")
  }

//  @Test
//  fun canAddNewHoliday() {
//    val holiday = Holiday(mockHolidayId, mockName, mockDate)
//    holidayServiceTest!!.addNewHoliday(holiday)
//    val holidayArgumentCaptor: ArgumentCaptor<Holiday> = ArgumentCaptor.forClass(Holiday::class.java)
//    Mockito.verify<Any?>(mockHolidayRepository).save(holidayArgumentCaptor.capture())
//    val capturedUser: Holiday = holidayArgumentCaptor.getValue()
//    assertThat(capturedUser).isEqualTo(holiday)
//  }

  @Test
  fun shouldThrowMessageIfUnableToAddNewHolidayDueToExistingName() {
    val holiday = Holiday(mockHolidayId, mockName, mockDate)
    val holidayOptional: Optional<Holiday> = Optional.of<Holiday>(holiday)
    BDDMockito.given(mockHolidayRepository.findHolidayByName(mockName))
      .willReturn(holidayOptional)
    AssertionsForClassTypes.assertThatThrownBy { holidayServiceTest!!.addNewHoliday(holiday) }
      .isInstanceOf(BadRequestException::class.java)
      .hasMessageContaining("Holiday already exist")
  }

  @Test
  fun canUpdateHoliday() {
    val holiday = Holiday(mockHolidayId, mockName, mockDate)
    val updatingHolidayInput = Holiday(mockHolidayId, mockNewName, mockDate)
    val holidayOptional: Optional<Holiday> = Optional.of<Holiday>(holiday)
    given(mockHolidayRepository.findById(mockHolidayId))
      .willReturn(holidayOptional)
    val returnHoliday: Holiday = holidayServiceTest!!.updateHoliday(mockHolidayId, updatingHolidayInput)
    assertThat(returnHoliday.name).isEqualTo(updatingHolidayInput.name)
  }

  @Test
  fun cannotUpdateHolidayDueToWrongId() {
    val updatingHolidayInput = Holiday(mockHolidayId, mockNewName, mockDate)
    val holidayOptional: Optional<Holiday> = Optional.empty<Holiday>()
    given(mockHolidayRepository.findById(mockHolidayId))
      .willReturn(holidayOptional)
    assertThatThrownBy {
      holidayServiceTest!!.updateHoliday(
        mockHolidayId,
        updatingHolidayInput
      )
    }
      .isInstanceOf(NotFoundException::class.java)
      .hasMessageContaining("Holiday not found")
  }

  @Test
  @Disabled
  fun canDeleteHoliday() {
    given(mockHolidayRepository.existsById(mockHolidayId))
      .willReturn(true)
    holidayServiceTest!!.deleteHoliday(mockHolidayId)
  }

  @Test
  @Disabled
  fun cannotDeleteHoliday() {
    given(mockHolidayRepository.existsById(mockHolidayId))
      .willReturn(false)
    assertThatThrownBy { holidayServiceTest!!.deleteHoliday(mockHolidayId) }
      .isInstanceOf(NotFoundException::class.java)
      .hasMessageContaining("Holiday does not exist")
  }


  @Test
  fun canFindHolidayInRange() {
    val mockStartDate = "2000-01-01"
    val mockEndDate = "2100-01-01"
    val holiday = Holiday(mockHolidayId, mockName, mockDate)
    val holidayList: MutableList<Holiday> = ArrayList<Holiday>()
    holidayList.add(holiday)
    given(mockHolidayRepository.findAll())
      .willReturn(holidayList)
    val outputList: List<Holiday> = holidayServiceTest!!.findHolidayInRange(mockStartDate, mockEndDate)
    AssertionsForClassTypes.assertThat<List<Holiday>>(outputList).isEqualTo(holidayList)
  }

  @Test
  fun cannotFindHolidayInRangeDueToWrongDateFormat() {
    val mockStartDate = "2000-01"
    val mockEndDate = "2100-01-01"
    val holiday = Holiday(mockHolidayId, mockName, mockDate)
    val holidayList: MutableList<Holiday> = ArrayList<Holiday>()
    holidayList.add(holiday)
    given(mockHolidayRepository.findAll())
      .willReturn(holidayList)
    assertThatThrownBy {
      holidayServiceTest!!.findHolidayInRange(
        mockStartDate,
        mockEndDate
      )
    }
      .isInstanceOf(BadRequestException::class.java)
      .hasMessageContaining("Wrong date format. Please provide the date of the holiday in the format of YYYY-MM-DD")
  }

  @Test
  fun canFindDatesForAnnualLeaves() {
    mockDate = "2020-01-07"
    val alCorrespondToMockDate = "2020-01-06"
    val holiday = Holiday(mockHolidayId, mockName, mockDate)
    val holidayList: MutableList<Holiday> = ArrayList<Holiday>()
    holidayList.add(holiday)
    given(mockHolidayRepository.findAll())
      .willReturn(holidayList)
    val outputList: List<Holiday> = holidayServiceTest!!.findDatesForAnnualLeaves()
    assertThat(outputList[0].date).isEqualTo(alCorrespondToMockDate)
  }
}