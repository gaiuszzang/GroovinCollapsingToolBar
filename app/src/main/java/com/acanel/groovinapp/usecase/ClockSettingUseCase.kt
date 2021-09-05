package com.acanel.groovinapp.usecase

import com.acanel.groovinapp.repository.SettingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.Integer.max
import java.lang.Integer.min
import javax.inject.Inject

class ClockSettingUseCase @Inject constructor(
    private val repo: SettingRepository
) {
    fun is24HourDisplay() = repo.is24HourDisplay()
    suspend fun set24HourDisplay(on: Boolean) = repo.set24HourDisplay(on)

    fun isBurnInPrevention() = repo.isBurnInPrevention()
    suspend fun setBurnInPrevention(on: Boolean) = repo.setBurnInPrevention(on)

    private val fontSizeTable = arrayOf(
        intArrayOf(90, 60, 30),
        intArrayOf(105, 70, 35),
        intArrayOf(120, 80, 40),
        intArrayOf(135, 90, 45),
        intArrayOf(150, 100, 50),
    )
    fun getClockFontSizeLevel() = repo.getClockFontSizeLevel()
    suspend fun setClockFontSizeLevel(level: Int) = repo.setClockFontSizeLevel(level)
    fun getClockFontSizeLevelRange() = fontSizeTable.indices

    fun getTimeFontSizeSp(): Flow<Int> =
        repo.getClockFontSizeLevel().map {
            val level = min(max(0, it), fontSizeTable.size - 1)
            fontSizeTable[level][0]
        }

    fun getAmPmFontSizeSp(): Flow<Int> =
        repo.getClockFontSizeLevel().map {
            val level = min(max(0, it), fontSizeTable.size - 1)
            fontSizeTable[level][1]
        }

    fun getDateFontSizeSp(): Flow<Int> =
        repo.getClockFontSizeLevel().map {
            val level = min(max(0, it), fontSizeTable.size - 1)
            fontSizeTable[level][2]
        }
}