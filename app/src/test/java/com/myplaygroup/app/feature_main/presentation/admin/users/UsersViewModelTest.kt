package com.myplaygroup.app.feature_main.presentation.admin.users

import com.google.common.truth.Truth
import com.myplaygroup.app.core.utility.MainCoroutineRule
import com.myplaygroup.app.feature_main.data.repository.FakeUsersRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UsersViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    lateinit var viewModel: UsersViewModel
    lateinit var usersRepository: FakeUsersRepository

    @Before
    fun setUp() {
        usersRepository = FakeUsersRepository()
        viewModel = UsersViewModel(usersRepository)
    }

    @Test
    fun `On init, check that users is fetched`() = runBlocking {
        delay(10)
        Truth.assertThat(viewModel.state.appUsers).isEqualTo(usersRepository.appUsers)
    }
}