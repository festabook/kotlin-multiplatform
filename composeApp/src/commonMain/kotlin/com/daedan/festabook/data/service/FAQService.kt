package com.daedan.festabook.data.service

import com.daedan.festabook.data.model.response.faq.FAQResponse
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.GET

interface FAQService {
    @GET("questions")
    suspend fun fetchAllFAQs(): Response<List<FAQResponse>>
}
