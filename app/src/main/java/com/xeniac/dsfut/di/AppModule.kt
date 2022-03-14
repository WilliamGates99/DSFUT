package com.xeniac.dsfut.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.xeniac.dsfut.repositories.MainRepository
import com.xeniac.dsfut.utils.Constants.PREFERENCE_INPUTS
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMainRepository() = MainRepository()

    @Singleton
    @Provides
    fun provideInputPrefs(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(PREFERENCE_INPUTS, MODE_PRIVATE)
}