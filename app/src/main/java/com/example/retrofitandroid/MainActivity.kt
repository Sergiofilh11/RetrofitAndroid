package com.example.retrofitandroid

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.retrofitandroid.ui.theme.RetrofitAndroidTheme
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val result = construirRetrofit(this)
            .create(ContestApi::class.java)
            .getContests()

        Log.d("[RETROFIT]", result.toString())

        setContent {
            RetrofitAndroidTheme {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Retrofit")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        construirRetrofit(this)
            .create(ContestApi::class.java)
            .getContests()
            .enqueue(object : Callback<Contest>{
                override fun onResponse(call: Call<Contest>, response: Response<Contest>) {
                    Log.d("[RETROFIT]", response.body()?.results.toString())
                }

                override fun onFailure(call: Call<Contest>, t: Throwable) {
                    Log.d("[RETROFIT]", t.toString())
                }
            })
    }
}

fun construirRetrofit(context: Context): Retrofit {
    // https://pokeapi.co/api/v2/contest-type/

    val okHttpClient = OkHttpClient.Builder()
        .build()

    val gson = GsonBuilder().create()

    return Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()
}

data class Item(
    val name: String,
    val url: String
)

data class Contest(
    val count: Int,
    val next: Int?,
    val previous: Int?,
    val results: List<Item>
)

interface ContestApi {

    @GET("contest-type")
    fun getContests(): Call<Contest>
}
