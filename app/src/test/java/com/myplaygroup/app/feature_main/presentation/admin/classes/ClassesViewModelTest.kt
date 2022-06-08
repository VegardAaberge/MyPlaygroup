package com.myplaygroup.app.feature_main.presentation.admin.classes

import com.google.common.truth.Truth
import com.myplaygroup.app.core.utility.MainCoroutineRule
import com.myplaygroup.app.feature_main.data.repository.FakeDailyClassesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ClassesViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    lateinit var viewModel: ClassesViewModel
    lateinit var dailyClassesRepository: FakeDailyClassesRepository

    @Before
    fun setUp() {
        dailyClassesRepository = FakeDailyClassesRepository()
        viewModel = ClassesViewModel(dailyClassesRepository)
    }

    @Test
    fun `On init, check that daily classes is fetched`() = runBlocking {
        delay(10)
        Truth.assertThat(viewModel.state.dailyClasses).isEqualTo(dailyClassesRepository.dailyClasses)
    }
}