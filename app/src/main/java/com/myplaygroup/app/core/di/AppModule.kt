package com.myplaygroup.app.core.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.room.Room
import androidx.security.crypto.MasterKey
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.myplaygroup.app.core.data.remote.BasicAuthInterceptor
import com.myplaygroup.app.core.data.remote.NullHostNameVerifier
import com.myplaygroup.app.core.data.remote.PlaygroupApi
import com.myplaygroup.app.core.data.remote.TrustAllX509TrustManager
import com.myplaygroup.app.core.data.settings.UserSettings
import com.myplaygroup.app.core.data.settings.UserSettingsSerializer
import com.myplaygroup.app.core.util.Constants.ADMIN_DATABASE_NAME
import com.myplaygroup.app.core.util.Constants.BASE_URL
import com.myplaygroup.app.core.util.Constants.DATASTORE_FILE
import com.myplaygroup.app.core.util.Constants.KEYSET_NAME
import com.myplaygroup.app.core.util.Constants.LOCALHOST_URL
import com.myplaygroup.app.core.util.Constants.MAIN_DATABASE_NAME
import com.myplaygroup.app.core.util.Constants.MASTER_KEY_URI
import com.myplaygroup.app.core.util.Constants.PREFERENCE_FILE
import com.myplaygroup.app.feature_admin.data.local.AdminDatabase
import com.myplaygroup.app.feature_main.data.local.MainDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.json.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesMainDatabase(app: Application): MainDatabase {
        return Room.databaseBuilder(
            app,
            MainDatabase::class.java,
            MAIN_DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideMainDao(db: MainDatabase) = db.mainDao()

    @Provides
    @Singleton
    fun providesAdminDatabase(app: Application): AdminDatabase {
        return Room.databaseBuilder(
            app,
            AdminDatabase::class.java,
            ADMIN_DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideAdminDao(db: AdminDatabase) = db.mainDao()

    @Singleton
    @Provides
    fun provideBasicAuthInterceptor() = BasicAuthInterceptor()

    @Singleton
    @Provides
    fun provideMyPlaygroupApi(
        basicAuthInterceptor: BasicAuthInterceptor
    ) : PlaygroupApi {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(basicAuthInterceptor)
        if(BASE_URL == LOCALHOST_URL){
            builder.hostnameVerifier(NullHostNameVerifier())
        }
        val client = builder.build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(PlaygroupApi::class.java)
    }


    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO){
            engine {
                https{
                    trustManager = TrustAllX509TrustManager()
                }
            }
            install(Logging)
            install(WebSockets)
            install(ContentNegotiation) {
                json()
            }
        }
    }

    @Singleton
    @Provides
    fun provideAead(application: Application): Aead {
        AeadConfig.register()

        return AndroidKeysetManager.Builder()
            .withSharedPref(application, KEYSET_NAME, PREFERENCE_FILE)
            .withKeyTemplate(KeyTemplates.get(MasterKey.KeyScheme.AES256_GCM.name))
            .withMasterKeyUri(MASTER_KEY_URI)
            .build()
            .keysetHandle
            .getPrimitive(Aead::class.java)
    }

    @Singleton
    @Provides
    fun provideDataStore(
        application: Application,
        aead: Aead
    ): DataStore<UserSettings> {
        return DataStoreFactory.create(
            produceFile = { File(application.filesDir, DATASTORE_FILE) },
            serializer = UserSettingsSerializer(aead)
        )
    }
}