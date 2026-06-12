package com.undef.superahorro.caparrozruiz.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.undef.superahorro.caparrozruiz.data.local.entity.PromotionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PromotionDao {
    @Query("SELECT * FROM promotions ORDER BY id ASC")
    fun getAll(): Flow<List<PromotionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(promotions: List<PromotionEntity>)

    @Query("DELETE FROM promotions")
    suspend fun deleteAll()
}
