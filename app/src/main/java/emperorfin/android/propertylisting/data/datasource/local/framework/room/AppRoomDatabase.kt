package emperorfin.android.propertylisting.data.datasource.local.framework.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import emperorfin.android.propertylisting.data.datasource.local.framework.room.dao.PropertyDao
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.currencyrate.CurrencyRateEntity
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.networkstat.NetworkStatEntity
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.property.PropertyEntity


@Database(entities = [PropertyEntity::class, CurrencyRateEntity::class, NetworkStatEntity::class], version = 2, exportSchema = false)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract val mPropertyDao: PropertyDao

    companion object {

        private const val DATABASE_NAME = "database_app"

        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

        fun getInstance(context: Context): AppRoomDatabase{

            synchronized(this) {
                var instance = INSTANCE

                if(instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppRoomDatabase::class.java,
                        DATABASE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }

        }

    }
}