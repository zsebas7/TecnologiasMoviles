package com.undef.superahorro.caparrozruiz.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.undef.superahorro.caparrozruiz.data.local.entity.PurchaseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchaseDao {
    @Query("SELECT * FROM purchases ORDER BY date DESC, time DESC, id DESC")
    fun observeAll(): Flow<List<PurchaseEntity>>

    @Query("SELECT * FROM purchases WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): PurchaseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(purchase: PurchaseEntity): Long

    @Update
    suspend fun update(purchase: PurchaseEntity)

    @Delete
    suspend fun delete(purchase: PurchaseEntity)

    @Query("DELETE FROM purchases WHERE id = :id")
    suspend fun deleteById(id: Long)
}
