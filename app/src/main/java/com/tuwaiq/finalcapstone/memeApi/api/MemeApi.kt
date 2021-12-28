package com.tuwaiq.finalcapstone.memeApi.api


import com.tuwaiq.finalcapstone.memeApi.models.ApiResponse
import retrofit2.Response
import retrofit2.http.GET

interface MemeApi {

    @GET("/get_memes")
    suspend fun getMemes(): Response<ApiResponse>

    @GET("/get_memes")
    suspend fun getMemes2(): Response<String>

}