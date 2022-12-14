package com.example.appscan
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface Endpoint {
    @Headers("x-apikey: 8295ce85fd9dd1fb6e743cf8466514805e968748ed0430e7cf5d1fff7df8cafe" ,"content-type: application/x-www-form-urlencoded","accept: application/json")
    @FormUrlEncoded
    @POST("urls")
    fun scanurl( @Field("url") url: String):Call<ResponseBody>


    @Headers("x-apikey: 8295ce85fd9dd1fb6e743cf8466514805e968748ed0430e7cf5d1fff7df8cafe" )
    @GET("analyses/{id}")
    fun getanalyses(@Path("id") id: String):Call<ResponseBody>


}