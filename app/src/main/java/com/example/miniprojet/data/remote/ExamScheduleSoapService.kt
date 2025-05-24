package com.example.miniprojet.data.remote

import java.util.Date

/**
 * SOAP service for exam schedule
 * Temporarily commented out due to dependency issues
 */
class ExamScheduleSoapService {

    companion object {
        private const val NAMESPACE = "http://example.com/examschedule"
        private const val METHOD_GET_EXAMS = "getExamSchedule"
        private const val SOAP_ACTION = "$NAMESPACE/$METHOD_GET_EXAMS"
        private const val URL = "https://example.com/examschedule"
    }

    fun getExamSchedule(courseId: String? = null): List<ExamSchedule> {
        // Temporarily returning mock data due to ksoap2 dependency issues
        val mockExams = listOf(
            ExamSchedule(
                id = "1",
                courseId = "CS101",
                courseName = "Introduction to Computer Science",
                date = Date(),
                location = "Room 101",
                durationMinutes = 120
            ),
            ExamSchedule(
                id = "2",
                courseId = "CS102",
                courseName = "Data Structures",
                date = Date(),
                location = "Room 102",
                durationMinutes = 90
            )
        )

        // Filter by courseId if provided
        return if (courseId != null) {
            mockExams.filter { it.courseId == courseId }
        } else {
            mockExams
        }
    }

    /* Original implementation commented out
    suspend fun getExamScheduleOriginal(courseId: String? = null): List<ExamSchedule> = withContext(Dispatchers.IO) {
        val request = SoapObject(NAMESPACE, METHOD_GET_EXAMS)

        if (courseId != null) {
            request.addProperty("courseId", courseId)
        }

        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
        envelope.setOutputSoapObject(request)
        envelope.dotNet = true

        val transport = HttpTransportSE(URL)

        try {
            transport.call(SOAP_ACTION, envelope)
            val response = envelope.response as SoapObject

            val examList = mutableListOf<ExamSchedule>()

            for (i in 0 until response.propertyCount) {
                val exam = response.getProperty(i) as SoapObject

                val id = exam.getPropertyAsString("id")
                val courseId = exam.getPropertyAsString("courseId")
                val courseName = exam.getPropertyAsString("courseName")
                val dateStr = exam.getPropertyAsString("date")
                val location = exam.getPropertyAsString("location")
                val duration = exam.getPropertyAsString("duration").toInt()

                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val date = dateFormat.parse(dateStr) ?: Date()

                examList.add(
                    ExamSchedule(
                        id = id,
                        courseId = courseId,
                        courseName = courseName,
                        date = date,
                        location = location,
                        durationMinutes = duration
                    )
                )
            }

            examList
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    */
}

data class ExamSchedule(
    val id: String,
    val courseId: String,
    val courseName: String,
    val date: Date,
    val location: String,
    val durationMinutes: Int
)
