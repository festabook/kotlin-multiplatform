package com.daedan.festabook.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.daedan.festabook.data.db.DATASTORE_FILE_NAME
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import okio.Path.Companion.toPath

@BindingContainer
@ContributesTo(AppScope::class)
object DatabaseBindings {
    @Provides
    @SingleIn(AppScope::class)
    fun provideDataStore(application: Application): DataStore<Preferences> =
        PreferenceDataStoreFactory.createWithPath {
            application.filesDir
                .resolve(DATASTORE_FILE_NAME)
                .absolutePath
                .toPath()
        }
}
