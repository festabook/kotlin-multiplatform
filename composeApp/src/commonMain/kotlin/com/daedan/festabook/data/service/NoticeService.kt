package com.daedan.festabook.data.service

import com.daedan.festabook.data.model.response.notice.NoticeListResponse
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.GET

interface NoticeService {
    @GET("announcements")
    suspend fun getNotices(): Response<NoticeListResponse>
}
