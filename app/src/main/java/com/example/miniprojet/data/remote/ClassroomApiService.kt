package com.example.miniprojet.data.remote

import com.example.miniprojet.data.model.Announcement
import com.example.miniprojet.data.model.Course
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

/**
 * Retrofit interface for Google Classroom API
 */
interface ClassroomApiService {
    
    @GET("v1/courses")
    suspend fun getCourses(
        @Header("Authorization") token: String
    ): Response<CourseListResponse>
    
    @GET("v1/courses/{courseId}/announcements")
    suspend fun getAnnouncements(
        @Header("Authorization") token: String,
        @Path("courseId") courseId: String
    ): Response<AnnouncementListResponse>
}

data class CourseListResponse(
    val courses: List<CourseResponse>
)

data class CourseResponse(
    val id: String,
    val name: String,
    val description: String?,
    val ownerId: String,
    val creationTime: String,
    val updateTime: String,
    val courseState: String
)

data class AnnouncementListResponse(
    val announcements: List<AnnouncementResponse>
)

data class AnnouncementResponse(
    val id: String,
    val text: String,
    val materials: List<MaterialResponse>?,
    val state: String,
    val alternateLink: String,
    val creationTime: String,
    val updateTime: String,
    val creatorUserId: String
)

data class MaterialResponse(
    val driveFile: DriveFileResponse?,
    val youtubeVideo: YoutubeVideoResponse?,
    val link: LinkResponse?,
    val form: FormResponse?
)

data class DriveFileResponse(
    val driveFile: DriveFile
)

data class DriveFile(
    val id: String,
    val title: String,
    val alternateLink: String,
    val thumbnailUrl: String?
)

data class YoutubeVideoResponse(
    val id: String,
    val title: String,
    val alternateLink: String,
    val thumbnailUrl: String?
)

data class LinkResponse(
    val url: String,
    val title: String,
    val thumbnailUrl: String?
)

data class FormResponse(
    val formUrl: String,
    val responseUrl: String,
    val title: String,
    val thumbnailUrl: String?
)
