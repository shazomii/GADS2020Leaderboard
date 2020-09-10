package com.davenet.gadsleaderboard.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.davenet.gadsleaderboard.domain.Response
import com.davenet.gadsleaderboard.repository.SubmissionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class SubmitViewModel(application: Application) : AndroidViewModel(application) {

    private val submissionRepository = SubmissionRepository()
    /**
     * This is the job for all coroutines started by this viewModel.
     *
     * Cancelling this job will cancell all coroutines started by this ViewModel.
     */
    private val viewModelJob = SupervisorJob()

    /**
     * This is the main scope for all coroutines launched by SubmitViewModel.
     *
     * Since we pass viewModelJob, you can cancel all coroutines launched by uiScope by calling
     * viewModelJob.cancel()
     */
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val _loadingState: MutableLiveData<Boolean> = MutableLiveData()
    val loadingState: LiveData<Boolean>
        get() = _loadingState
    private val _submissionSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val submissionSuccess: LiveData<Boolean>
        get() = _submissionSuccess
    private val _toast: MutableLiveData<String> = MutableLiveData()
    val toast: LiveData<String>
        get() = _toast
    fun submitProject(
        firstName: String, lastName: String, emailAddress: String, projectLink: String
    ) {
        viewModelScope.launch {
            _loadingState.postValue(true)
            val result = submissionRepository.submitProject(
                firstName, lastName, emailAddress, projectLink
            )
            when (result) {
                is Response.Success -> {
                    _submissionSuccess.postValue(true)
                    _loadingState.postValue(false)
                }
                is Response.Loading -> {
                    _loadingState.postValue(true)
                    _submissionSuccess.postValue(false)
                }
                is Response.Error -> {
                    _submissionSuccess.postValue(false)
                    _loadingState.postValue(false)
                    _toast.value = result.message
                }
            }
        }
    }

    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**
     * Factory for constructing SubmitViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SubmitViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SubmitViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewModel")
        }
    }
}

