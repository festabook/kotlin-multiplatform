package com.daedan.festabook.di

import com.daedan.festabook.config.AppConfig
import com.daedan.festabook.config.createAppConfig
import com.daedan.festabook.data.service.DeviceService
import com.daedan.festabook.data.service.FAQService
import com.daedan.festabook.data.service.FestivalLineupService
import com.daedan.festabook.data.service.FestivalNotificationService
import com.daedan.festabook.data.service.FestivalService
import com.daedan.festabook.data.service.LostItemService
import com.daedan.festabook.data.service.NoticeService
import com.daedan.festabook.data.service.PlaceService
import com.daedan.festabook.data.service.ScheduleService
import com.daedan.festabook.data.service.api.FestaBookAuthPlugin
import com.daedan.festabook.data.service.createDeviceService
import com.daedan.festabook.data.service.createFAQService
import com.daedan.festabook.data.service.createFestivalLineupService
import com.daedan.festabook.data.service.createFestivalNotificationService
import com.daedan.festabook.data.service.createFestivalService
import com.daedan.festabook.data.service.createLostItemService
import com.daedan.festabook.data.service.createNoticeService
import com.daedan.festabook.data.service.createPlaceService
import com.daedan.festabook.data.service.createScheduleService
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.ResponseConverterFactory
import de.jensklingenberg.ktorfit.ktorfit
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

@BindingContainer
@ContributesTo(AppScope::class)
object NetworkBindings {
    @Provides
    fun provideJson(): Json = Json { ignoreUnknownKeys = true }

    @Provides
    fun provideResponseConverterFactory(): ResponseConverterFactory = ResponseConverterFactory()

    @Provides
    fun provideAppConfig(): AppConfig = createAppConfig()

    @Provides
    fun provideKtorfit(
        appConfig: AppConfig,
        responseConverterFactory: ResponseConverterFactory,
        authPlugin: FestaBookAuthPlugin,
        json: Json,
    ): Ktorfit =
        ktorfit {
            baseUrl(appConfig.baseUrl)
            converterFactories(responseConverterFactory)
            httpClient {
                HttpClient {
                    install(ContentNegotiation) {
                        json(json)
                    }
                    install(authPlugin.plugin)
                }
            }
        }

    @Provides
    fun provideScheduleService(ktorfit: Ktorfit): ScheduleService = ktorfit.createScheduleService()

    @Provides
    fun provideNoticeService(ktorfit: Ktorfit): NoticeService = ktorfit.createNoticeService()

    @Provides
    fun providePlaceService(ktorfit: Ktorfit): PlaceService = ktorfit.createPlaceService()

    @Provides
    fun provideDeviceService(ktorfit: Ktorfit): DeviceService = ktorfit.createDeviceService()

    @Provides
    fun provideFestivalNotificationService(ktorfit: Ktorfit): FestivalNotificationService = ktorfit.createFestivalNotificationService()

    @Provides
    fun provideFAQService(ktorfit: Ktorfit): FAQService = ktorfit.createFAQService()

    @Provides
    fun provideFestivalService(ktorfit: Ktorfit): FestivalService = ktorfit.createFestivalService()

    @Provides
    fun provideLostItemService(ktorfit: Ktorfit): LostItemService = ktorfit.createLostItemService()

    @Provides
    fun provideFestivalLineupService(ktorfit: Ktorfit): FestivalLineupService = ktorfit.createFestivalLineupService()
}
