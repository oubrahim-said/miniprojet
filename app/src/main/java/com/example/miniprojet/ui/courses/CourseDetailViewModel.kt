package com.example.miniprojet.ui.courses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miniprojet.data.model.Course
import com.example.miniprojet.data.remote.ExamSchedule
import com.example.miniprojet.data.repository.CourseRepository
import com.example.miniprojet.data.repository.ExamRepository
import com.example.miniprojet.utils.NetworkResult
import kotlinx.coroutines.launch

class CourseDetailViewModel : ViewModel() {

    private val courseRepository = CourseRepository()
    private val examRepository = ExamRepository()

    private val _course = MutableLiveData<Course>()
    val course: LiveData<Course> = _course

    private val _exams = MutableLiveData<List<ExamSchedule>>()
    val exams: LiveData<List<ExamSchedule>> = _exams

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private var courseId: String? = null

    fun loadCourseDetails(courseId: String) {
        this.courseId = courseId
        _isLoading.value = true

        courseRepository.getCourseById(courseId).observeForever { course ->
            _course.value = course
            _isLoading.value = false
        }
    }

    fun loadExamSchedule(courseId: String) {
        _isLoading.value = true

        // No need for coroutine since the function is now synchronous
        when (val result = examRepository.getExamSchedule(courseId)) {
            is NetworkResult.Success -> {
                _exams.value = result.data
                _isLoading.value = false
            }
            is NetworkResult.Error -> {
                _errorMessage.value = result.message
                _exams.value = emptyList()
                _isLoading.value = false
            }
            is NetworkResult.Loading -> {
                // Already set loading to true above
            }
        }
    }

    fun toggleFavorite() {
        val currentCourse = _course.value ?: return
        val courseId = currentCourse.id
        val newFavoriteState = !currentCourse.isFavorite

        viewModelScope.launch {
            courseRepository.updateFavoriteStatus(courseId, newFavoriteState)
        }
    }
}
