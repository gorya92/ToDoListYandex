package com.bignerdranch.android.testing.retrofitConnect.api

import com.bignerdranch.android.testing.retrofitConnect.RetrofitConstants.Companion.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    //Создание кастомного Interceptora
    val headerInterceptor: Interceptor = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var request: Request = chain.request()

            request =
                request.newBuilder().addHeader("Authorization", "Bearer RalnorPresris").build()
            val response: Response = chain.proceed(request)
            return response
        }
    }

    private val okHttp: OkHttpClient.Builder = OkHttpClient.Builder()
        .addInterceptor(headerInterceptor)

    val builder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp.build())
    }
    val retrofit: Retrofit = builder.build()

    val api: TodoListApi by lazy {
        retrofit.create(TodoListApi::class.java)
    }
}


