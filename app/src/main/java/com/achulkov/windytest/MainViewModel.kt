package com.achulkov.windytest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.runningReduce
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class MainViewModel : ViewModel() {

    private val _resultFlow = MutableStateFlow<String>("")
    val resultFlow: Flow<String> = _resultFlow.asStateFlow()

    fun startFlow(count: Int) {
        val flows = List(count) { i ->
            flow {
                delay((i + 1) * 100L)
                emit(i + 1)
            }
        }

        viewModelScope.launch(Dispatchers.Default) {
            flows.merge()
                .runningReduce { accumulator, value -> accumulator + value }
                .collect { value ->
                    _resultFlow.emit(value.toString())
                }
        }


    }
}