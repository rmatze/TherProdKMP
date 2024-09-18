package com.matze.therprodkmp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matze.therprodkmp.data.ApiService
import com.matze.therprodkmp.data.ApiServiceImpl
import com.matze.therprodkmp.data.WorkdayUiState
import com.matze.therprodkmp.model.MeetingRequest
import com.matze.therprodkmp.model.TimesheetRequest
import com.matze.therprodkmp.model.TreatmentRequest
import com.matze.therprodkmp.model.WorkdayRequest
import com.matze.therprodkmp.model.WorkdayResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class WorkdayViewModel : ViewModel() {

    private val _uiState =
        MutableStateFlow(WorkdayUiState())
    val uiState: StateFlow<WorkdayUiState> = _uiState.asStateFlow()

    private val apiService: ApiService = ApiServiceImpl()
    private val _workdays = MutableStateFlow<List<WorkdayResponse>>(emptyList())
    val workdays = _workdays.asStateFlow()
    private val _workdayById = MutableStateFlow<WorkdayResponse>(
        WorkdayResponse(
            0,
            "",
            emptyList(),
            emptyList(),
            emptyList()
        )
    )
    val workdayById = _workdayById.asStateFlow()

    init {
        fetchWorkdays()
    }

    fun fetchWorkdays() {
        viewModelScope.launch {
            try {
                val result = apiService.getWorkdays()
                _workdays.value = result
            } catch (e: Exception) {
                print(e.message)
            }
        }
    }

    fun fetchWorkdayById(workdayId: Int) {
        viewModelScope.launch {
            try {
                val result = apiService.getWorkdayById(workdayId)
                _workdayById.value = result
            } catch (e: Exception) {
                print(e.message)
            }
        }
    }

    fun addWorkday() {
        viewModelScope.launch {
            try {
                apiService.addWorkday(createWorkdayBody(_uiState.value))
                resetWorkday()
                val result = apiService.getWorkdays()
                _workdays.value = result
            } catch (e: Exception) {
                print(e.message)
            }
        }
    }

    private fun createWorkdayBody(workday: WorkdayUiState): WorkdayRequest {
        return WorkdayRequest(
            date = workday.date,
            timesheets = workday.timesheetRequests,
            meetings = workday.meetingRequests,
            treatments = workday.treatmentRequests
        )
    }

    fun deleteWorkdayById(workdayId: Int) {
        viewModelScope.launch {
            try {
                apiService.deleteWorkdayById(workdayId)
                val result = apiService.getWorkdays()
                _workdays.value = result
            } catch (e: Exception) {
                print(e.message)
            }
        }
    }

    fun setWorkdayId(workdayId: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                workdayId = workdayId
            )
        }
    }

    fun getWorkdayId() = _uiState.value.workdayId

    fun setDate(workdayDate: String) {
        _uiState.update { currentState ->
            currentState.copy(
                date = workdayDate
            )
        }
    }

    fun setTimesheets(timesheetRequestList: List<TimesheetRequest>) {
        _uiState.update { currentState ->
            currentState.copy(
                timesheetRequests = timesheetRequestList
            )
        }
    }

    fun setMeetings(meetingRequestList: List<MeetingRequest>) {
        _uiState.update { currentState ->
            currentState.copy(
                meetingRequests = meetingRequestList
            )
        }
    }

    fun setTreatments(treatmentRequestList: List<TreatmentRequest>) {
        _uiState.update { currentState ->
            currentState.copy(
                treatmentRequests = treatmentRequestList
            )
        }
    }

    /**
     * Returns a list of date options starting with the current date-2, current date, and the following date.
     */
    private fun workdayDateOptions(): List<String> {
        val dateOptions = mutableListOf<String>()
        val now = Clock.System.now()
        val timeZone = TimeZone.currentSystemDefault()
        var index = -2

        // add current date and the following 3 dates.
        repeat(4) {
            val day = now.plus(index, DateTimeUnit.DAY, timeZone)
            dateOptions.add(day.toLocalDateTime(timeZone).date.toString())
            index = index.inc()
        }
        return dateOptions
    }

    /**
     * Reset the order state
     */
    fun resetWorkday() {
        _uiState.value = WorkdayUiState()
    }
}