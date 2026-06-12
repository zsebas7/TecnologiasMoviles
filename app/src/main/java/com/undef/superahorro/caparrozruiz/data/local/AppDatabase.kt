package com.undef.superahorro.caparrozruiz.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.undef.superahorro.caparrozruiz.data.local.dao.ProductDao
import com.undef.superahorro.caparrozruiz.data.local.dao.PromotionDao
import com.undef.superahorro.caparrozruiz.data.local.dao.PurchaseDao
import com.undef.superahorro.caparrozruiz.data.local.entity.ProductEntity
import com.undef.superahorro.caparrozruiz.data.local.entity.PromotionEntity
import com.undef.superahorro.caparrozruiz.data.local.entity.PurchaseEntity

//clase central de Room, punto de entrada de lo que tiene que ver con la base de datos local
@Database(
    entities = [PurchaseEntity::class, ProductEntity::class, PromotionEntity::class], //cada entity es una tabla
    version = 2,
    exportSchema = false
)
//son abstractos porque Room genera la implementacion automaticamente
abstract class AppDatabase : RoomDatabase() {
    abstract fun purchaseDao(): PurchaseDao
    abstract fun productDao(): ProductDao
    abstract fun promotionDao(): PromotionDao

    companion object {
        @Volatile
        //patron singleton para que la app funcione con una sola base de datos
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
