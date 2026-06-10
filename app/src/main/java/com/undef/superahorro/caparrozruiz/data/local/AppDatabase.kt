package com.undef.superahorro.caparrozruiz.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.undef.superahorro.caparrozruiz.data.local.dao.ProductDao
import com.undef.superahorro.caparrozruiz.data.local.dao.PurchaseDao
import com.undef.superahorro.caparrozruiz.data.local.entity.ProductEntity
import com.undef.superahorro.caparrozruiz.data.local.entity.PurchaseEntity

@Database(
    entities = [PurchaseEntity::class, ProductEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun purchaseDao(): PurchaseDao
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "super_ahorro_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
