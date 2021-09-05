package com.acanel.groovinapp.repository

import kotlinx.coroutines.flow.Flow

interface SettingRepository {
    fun is24HourDisplay(): Flow<Boolean>
    suspend fun set24HourDisplay(on: Boolean)

    fun isBurnInPrevention(): Flow<Boolean>
    suspend fun setBurnInPrevention(on: Boolean)

    fun getClockFontSizeLevel(): Flow<Int>
    suspend fun setClockFontSizeLevel(level: Int)
}