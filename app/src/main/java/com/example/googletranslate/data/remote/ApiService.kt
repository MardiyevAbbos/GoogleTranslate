package com.example.googletranslate.data.remote

import com.example.googletranslate.model.Detect
import com.example.googletranslate.model.Translation
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface ApiService {

/*    @Headers("Content-Type: $content_type", "Accept-Encoding: $accept_encoding",
        "X-RapidAPI-Host: $x_rapidAPI_host", "X-RapidAPI-Key: $x_rapidAPI_key") */

    @POST("language/translate/v2")
    @FormUrlEncoded
    suspend fun wordTranslate(@Field("q") word: String,@Field("target") target: String,
                              @Field("source") source: String): Translation

    @POST("language/translate/v2/detect")
    @FormUrlEncoded
    suspend fun wordDetect(@Field("q") word: String): Detect

}