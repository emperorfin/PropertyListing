package emperorfin.android.propertylisting.di

import android.content.Context
import com.skydoves.sandwich.coroutines.CoroutinesResponseCallAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import emperorfin.android.propertylisting.BuildConfig
import emperorfin.android.propertylisting.data.datasource.local.framework.room.AppRoomDatabase
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entitysource.PropertyLocalDataSource
import emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.dtosource.PropertyRemoteDataSource
import emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.webservice.hostelworld.api.PropertyApi
import emperorfin.android.propertylisting.data.repository.PropertyRepository
import emperorfin.android.propertylisting.domain.datalayer.dao.IPropertyDao
import emperorfin.android.propertylisting.domain.datalayer.datasource.PropertyDataSource
import emperorfin.android.propertylisting.domain.datalayer.repository.IPropertyRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton


@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class PropertyLocalDataSourceAnnotation

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class PropertyRemoteDataSourceAnnotation

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class LocalPropertyDao

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class RemotePropertyDao

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    @PropertyLocalDataSourceAnnotation
    abstract fun bindPropertyLocalDataSourceRoom(dataSource: PropertyLocalDataSource): PropertyDataSource

    @Singleton
    @Binds
    @PropertyRemoteDataSourceAnnotation
    abstract fun bindPropertyRemoteDataSourceRetrofit(dataSource: PropertyRemoteDataSource): PropertyDataSource
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindPropertyRepository(repository: PropertyRepository): IPropertyRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun providesAppDatabase(
        @ApplicationContext context: Context
    ): AppRoomDatabase {
        return AppRoomDatabase.getInstance(context)
    }

    @Provides
    @LocalPropertyDao
    fun providePropertyDao(database: AppRoomDatabase): IPropertyDao = database.mPropertyDao
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.HOSTEL_WORLD_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @RemotePropertyDao
    fun providePropertyApi(retrofit: Retrofit): IPropertyDao {
        return retrofit.create(PropertyApi::class.java)
    }
}