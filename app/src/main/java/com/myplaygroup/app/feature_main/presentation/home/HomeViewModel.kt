package com.myplaygroup.app.feature_main.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.domain.repository.ImageRepository
import com.myplaygroup.app.feature_main.domain.model.PlaygroupClass
import com.myplaygroup.app.feature_main.presentation.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val imageRepository: ImageRepository,
): ViewModel() {

    lateinit var mainViewModel: MainViewModel

    val calendarFlow = MutableStateFlow(
        listOf(
            PlaygroupClass(LocalDate.now().minusDays(1), "17:30"),
            PlaygroupClass(LocalDate.now().minusDays(3), "17:30"),
            PlaygroupClass(LocalDate.now().minusDays(5), "9:30"),
            PlaygroupClass(LocalDate.now().plusDays(1), "9:30"),
        )
    )
}