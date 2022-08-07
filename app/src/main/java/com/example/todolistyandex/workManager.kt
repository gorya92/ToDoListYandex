package com.example.todolistyandex

import android.R.attr.path
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bignerdranch.android.testing.retrofitConnect.RetrofitConstants
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class MyWorker(context: Context, params: WorkerParameters) : Worker(context,params) {
    override fun doWork(): Result {
        try {

            //Просто затычка) я не успеваю а это понадобится только потом
            var reader: BufferedReader? = null
            var stream: InputStream? = null
            var connection: HttpsURLConnection? = null
            try {
                val url = URL(RetrofitConstants.BASE_URL)
                connection = url.openConnection() as HttpsURLConnection
                connection!!.requestMethod = "GET"
                connection!!.readTimeout = 10000
                connection!!.connect()
                stream = connection!!.inputStream
                reader = BufferedReader(InputStreamReader(stream))
                val buf = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    buf.append(line).append("\n")
                }
                buf.toString()
            } finally {
                if (reader != null) {
                    reader.close()
                }
                if (stream != null) {
                    stream.close()
                }
                connection?.disconnect()
            }

        } catch (ex: Exception) {
            return Result.failure(); //или Result.retry()
        }
        return Result.success()
    }
}
