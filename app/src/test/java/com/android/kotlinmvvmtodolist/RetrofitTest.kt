package com.android.kotlinmvvmtodolist

import com.android.kotlinmvvmtodolist.retrofitConnect.RetrofitConstants
import com.android.kotlinmvvmtodolist.retrofitConnect.api.Api
import com.android.kotlinmvvmtodolist.retrofitConnect.api.RetrofitInstance
import org.junit.Test
import retrofit2.Call
import com.android.kotlinmvvmtodolist.retrofitConnect.api.RetrofitInstance.api
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class RetrofitTest {



    @Test
    fun testRetrofitBaseUrl() {
        runBlocking {
            val BASE_URL = "https://beta.mrdekk.ru/todobackend/"


            assert(RetrofitInstance.retrofit.baseUrl().toString() ==BASE_URL)
        }
    }

    @Test
    fun testPlacesService() {
        runBlocking {
        //Execute the API call
        val response = api.getList()
        //Check for error body
        val errorBody = response.errorBody()
        assert(errorBody == null)
        //Check for success body
        val responseWrapper = response.body()
        assert(responseWrapper != null)
        assert(response.code() == 200)
    }
    }

}