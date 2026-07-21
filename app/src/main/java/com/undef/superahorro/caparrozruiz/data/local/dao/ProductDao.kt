package com.undef.superahorro.caparrozruiz.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.undef.superahorro.caparrozruiz.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
//los DAOs solo declaran que operaciones existen, sin escribir como funcionan (excpeto Querys mas complejos)
interface ProductDao {
    //como las dos primeras son consultas personalizadas las tenemos que escribir nosotros
    @Query("SELECT * FROM products WHERE purchase_id = :purchaseId ORDER BY id ASC")
    fun observeByPurchaseId(purchaseId: Long): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products ORDER BY id ASC")
    fun observeAll(): Flow<List<ProductEntity>>

    //el resto las genera Room automaticamente
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: ProductEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductEntity>)

    @Update
    suspend fun update(product: ProductEntity)

    @Query("DELETE FROM products WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM products WHERE purchase_id = :purchaseId")
    suspend fun deleteByPurchaseId(purchaseId: Long)

    @Query("DELETE FROM products")
    suspend fun deleteAll()
}
