package com.android.kotlinmvvmtodolist.di

import com.android.kotlinmvvmtodolist.repository.RetrofitRepository
import com.android.kotlinmvvmtodolist.retrofitConnect.RetrofitConstants
import com.android.kotlinmvvmtodolist.retrofitConnect.api.Api
import com.android.kotlinmvvmtodolist.retrofitConnect.api.RetrofitInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesBaseUrl() : String = "https://beta.mrdekk.ru/todobackend/"

    @Provides
    @Singleton
    fun provideRetrofit(BASE_URL : String) : Retrofit {
        val headerInterceptor: Interceptor = object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                var request: Request = chain.request()

                request =
                    request.newBuilder().addHeader("Authorization", "Bearer RalnorPresris").build()
                val response: Response = chain.proceed(request)
                return response
            }
        }

       val okHttp: OkHttpClient.Builder = OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)

        val builder: Retrofit.Builder by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttp.build())
        }
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideMainService(retrofit : Retrofit) : Api = retrofit.create(Api::class.java)


}