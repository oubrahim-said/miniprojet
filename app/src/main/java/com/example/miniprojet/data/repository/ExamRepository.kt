package com.example.miniprojet.data.repository

import com.example.miniprojet.data.remote.ExamSchedule
import com.example.miniprojet.data.remote.ExamScheduleSoapService
import com.example.miniprojet.utils.NetworkResult

class ExamRepository {
    private val examScheduleSoapService = ExamScheduleSoapService()

    fun getExamSchedule(courseId: String? = null): NetworkResult<List<ExamSchedule>> {
        return try {
            val exams = examScheduleSoapService.getExamSchedule(courseId)
            NetworkResult.Success(exams)
        } catch (e: Exception) {
            NetworkResult.Error("Error fetching exam schedule: ${e.message}")
        }
    }
}
