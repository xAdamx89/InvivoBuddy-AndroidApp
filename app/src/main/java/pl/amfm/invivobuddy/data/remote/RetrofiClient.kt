package pl.amfm.invivobuddy.data.remote

import pl.amfm.invivobuddy.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val BASE_URL = BuildConfig.BASE_URL

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Zamienia JSON na nasze data klasy
            .build()
            .create(ApiService::class.java)
    }
}