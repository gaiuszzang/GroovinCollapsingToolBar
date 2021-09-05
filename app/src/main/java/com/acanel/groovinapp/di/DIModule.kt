package com.acanel.groovinapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.acanel.groovinapp.repository.SettingRepository
import com.acanel.groovinapp.repository.impl.DataStoreSettingRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

/*
 * Interface DI with Singleton Components
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class InterfaceDIModule {
    @Binds
    abstract fun bindSettingRepository(repo: DataStoreSettingRepository): SettingRepository
}


/*
 * DataStore DI with Singleton Components
 */
private val Context.settingDataStore: DataStore<Preferences> by preferencesDataStore(name = "clockSetting")

@Module
@InstallIn(SingletonComponent::class)
class DataStoreDIModule {
    @Provides
    @SettingDataStore
    fun provideSettingDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.settingDataStore
    }
}
