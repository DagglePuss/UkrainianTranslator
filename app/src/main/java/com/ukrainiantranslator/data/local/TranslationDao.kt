package com.ukrainiantranslator.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslationDao {

    @Query("SELECT * FROM translation_history ORDER BY timestamp DESC")
    fun getAllTranslations(): Flow<List<TranslationEntity>>

    @Query("SELECT * FROM translation_history ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentTranslations(limit: Int = 50): Flow<List<TranslationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslation(translation: TranslationEntity)

    @Delete
    suspend fun deleteTranslation(translation: TranslationEntity)

    @Query("DELETE FROM translation_history")
    suspend fun clearAllHistory()
}
