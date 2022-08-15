package com.example.todoList.di.module


import com.example.todoList.api.GithubAPI
import com.example.todoList.util.URL
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
open class HttpModule {

    @Provides
    @Singleton
    open fun provideHttpLogging(): OkHttpClient {
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
        return okHttp.build()
    }

    @Provides
    @Singleton
    open fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
            .baseUrl(URL.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    open fun provideApiService(retrofit: Retrofit): GithubAPI = retrofit.create(GithubAPI::class.java)

}
