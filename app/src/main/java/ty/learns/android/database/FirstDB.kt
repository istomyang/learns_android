package ty.learns.android.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/*
* version: 每当您更改架构时，都必须增加版本号。
* exportSchema: 保留架构版本记录的备份
* */
@Database(entities = [First::class], version = 1, exportSchema = false)
abstract class FirstDB : RoomDatabase() {
    abstract val firstDAO: FirstDAO

    companion object {
        @Volatile // 这个注解意思是不缓存
        private var INSTANCE: FirstDB? = null

        fun getInstance(context: Context): FirstDB {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FirstDB::class.java,
                        "first_database"
                    ).fallbackToDestructiveMigration() // 迁移策略，目前：销毁重新创建数据库
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}