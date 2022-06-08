package com.myplaygroup.app.feature_main.presentation.admin.monthly_plans

import com.google.common.truth.Truth
import com.myplaygroup.app.core.utility.MainCoroutineRule
import com.myplaygroup.app.feature_main.data.repository.FakeMonthlyPlansRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MonthlyPlansViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    lateinit var viewModel: MonthlyPlansViewModel
    lateinit var monthlyPlansRepository: FakeMonthlyPlansRepository

    @Before
    fun setUp() {
        monthlyPlansRepository = FakeMonthlyPlansRepository()
        viewModel = MonthlyPlansViewModel(monthlyPlansRepository)
    }

    @Test
    fun `On init, check that monthly plans is fetched`() = runBlocking {
        delay(10)
        Truth.assertThat(viewModel.state.monthlyPlans).isEqualTo(monthlyPlansRepository.monthlyPlans)
    }
}