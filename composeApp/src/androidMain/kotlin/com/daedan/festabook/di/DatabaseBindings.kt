package com.daedan.festabook.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.daedan.festabook.data.db.DATASTORE_FILE_NAME
import com.daedan.festabook.data.db.OLD_PREFS_FILE_NAME
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@BindingContainer
@ContributesTo(AppScope::class)
object DatabaseBindings {
    @Provides
    @SingleIn(AppScope::class)
    fun provideDataStore(application: Application): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            migrations = listOf(SharedPreferencesMigration(application, OLD_PREFS_FILE_NAME)),
            produceFile = { application.preferencesDataStoreFile(DATASTORE_FILE_NAME) },
        )
}
