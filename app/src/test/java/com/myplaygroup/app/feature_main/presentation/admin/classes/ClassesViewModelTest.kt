package com.myplaygroup.app.feature_main.presentation.admin.classes

import com.google.common.truth.Truth
import com.myplaygroup.app.core.utility.MainCoroutineRule
import com.myplaygroup.app.feature_main.data.repository.FakeDailyClassesRepository
import com.myplaygroup.app.feature_main.domain.enums.DailyClassType
import com.myplaygroup.app.feature_main.domain.model.DailyClass
import io.github.boguszpawlowski.composecalendar.CalendarState
import io.github.boguszpawlowski.composecalendar.header.MonthState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth

class ClassesViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    lateinit var viewModel: ClassesViewModel
    lateinit var dailyClassesRepository: FakeDailyClassesRepository

    @Before
    fun setUp() = runBlocking{
        dailyClassesRepository = FakeDailyClassesRepository()
        viewModel = ClassesViewModel(dailyClassesRepository)

        viewModel.state = ClassesState(
            calendarState = CalendarState(
                monthState = MonthState(YearMonth.of(2022, 12)),
                selectionState = DynamicSelectionState(
                    selectionMode = SelectionMode.Single,
                    selection = emptyList()
                )
            ),
        )
        delay(10)
    }

    @Test
    fun `On init, check that daily classes is fetched`() = runBlocking {
        Truth.assertThat(viewModel.state.dailyClasses).isEqualTo(dailyClassesRepository.dailyClasses)
    }

    @Test
    fun `Generate classes tapped, classes created`() = runBlocking{

        val expectedNewDailyClasses = 22
        val classType = DailyClassType.MORNING
        val startTime = LocalTime.of(9, 30)
        val endTime = LocalTime.of(11, 30)

        val classesBefore = viewModel.state.dailyClasses.toList()

        viewModel.state = viewModel.state.copy(
            dailyClassType = classType,
            startTime = startTime,
            endTime = endTime,
        )

        viewModel.onEvent(ClassesScreenEvent.GenerateClassesTapped)

        val dailyClassesSize = viewModel.state.dailyClasses.size
        val newDailyClasses = viewModel.state.dailyClasses.filter { x -> !classesBefore.contains(x) }

        Truth.assertThat(newDailyClasses.size).isEqualTo(expectedNewDailyClasses)
        Truth.assertThat(dailyClassesSize).isEqualTo(expectedNewDailyClasses + classesBefore.size)

        newDailyClasses.forEach { dailyClass ->
            Truth.assertThat(dailyClass.classType).isEqualTo(classType.name)
            Truth.assertThat(dailyClass.startTime).isEqualTo(startTime)
            Truth.assertThat(dailyClass.endTime).isEqualTo(endTime)
        }
    }

    @Test
    fun `Upload classes tapped, id is set`() = runBlocking{

        val exisistingDailyClasses = viewModel.state.dailyClasses.toMutableList()
        exisistingDailyClasses.add(DailyClass(
                id = -1,
                classType = DailyClassType.EVENING.name,
                date = LocalDate.of(2022, 12, 1),
                endTime = LocalTime.of(19, 30),
                startTime = LocalTime.of(17, 30),
                dayOfWeek = DayOfWeek.WEDNESDAY
        ))
        viewModel.state = viewModel.state.copy(
            dailyClasses = exisistingDailyClasses
        )

        viewModel.onEvent(ClassesScreenEvent.UploadCreatedClasses)
        delay(10)

        val dailyClasses = viewModel.state.dailyClasses
        val unsyncedDailyClasses = dailyClasses.filter { x -> x.id == -1L }.size
        val syncedDailyClassesSize = dailyClasses.filter { x -> x.id != -1L }.size

        Truth.assertThat(unsyncedDailyClasses).isEqualTo(0)
        Truth.assertThat(syncedDailyClassesSize).isEqualTo(viewModel.state.dailyClasses.size)
    }

    @Test
    fun `Submit class tapped, class is changed`() = runBlocking{

        val startTime = LocalTime.of(17, 30)
        val endTime = LocalTime.of(19, 30)
        val classDate = LocalDate.of(2022, 12, 1)
        val cancelled = true

        val selectedClass = viewModel.state.dailyClasses.first { x -> x.id == 1L }

        viewModel.state = viewModel.state.copy(
            selectedClass = selectedClass
        )

        viewModel.onEvent(ClassesScreenEvent.SubmitSelectedClassTapped(
            startTime = startTime,
            endTime = endTime,
            classDate = classDate,
            delete = cancelled,
        ))

        val changedClass = viewModel.state.dailyClasses.first { x -> x.id == 1L }

        Truth.assertThat(changedClass.startTime).isEqualTo(startTime)
        Truth.assertThat(changedClass.endTime).isEqualTo(endTime)
        Truth.assertThat(changedClass.date).isEqualTo(classDate)
        Truth.assertThat(changedClass.cancelled).isEqualTo(cancelled)
        Truth.assertThat(viewModel.state.selectedClass).isEqualTo(null)
    }

    @Test
    fun `Cancel unsynced class, class is deleted`() = runBlocking{

        val exisistingDailyClasses = viewModel.state.dailyClasses.toMutableList()
        exisistingDailyClasses.add(DailyClass(
            id = -1,
            classType = DailyClassType.EVENING.name,
            date = LocalDate.of(2022, 12, 1),
            endTime = LocalTime.of(19, 30),
            startTime = LocalTime.of(17, 30),
            dayOfWeek = DayOfWeek.WEDNESDAY
        ))
        viewModel.state = viewModel.state.copy(
            dailyClasses = exisistingDailyClasses
        )

        val selectedClass = viewModel.state.dailyClasses.first { x -> x.id == -1L }
        viewModel.state = viewModel.state.copy(
            selectedClass = selectedClass
        )

        viewModel.onEvent(ClassesScreenEvent.SubmitSelectedClassTapped(
            startTime = selectedClass.startTime,
            endTime = selectedClass.endTime,
            classDate = selectedClass.date,
            delete = true,
        ))

        val unsyncedClasses = viewModel.state.dailyClasses.filter { x -> x.id == -1L }

        Truth.assertThat(unsyncedClasses.size).isEqualTo(0)
    }
}