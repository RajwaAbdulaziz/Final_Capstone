package com.tuwaiq.finalcapstone.data.remote.memeApi.repo

import android.util.Log
import com.tuwaiq.finalcapstone.data.remote.memeApi.api.MemeApi
import com.tuwaiq.finalcapstone.data.remote.memeApi.models.Meme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val TAG = "ApiRepo"
class ApiRepo {


    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.imgflip.com/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val memeApi: MemeApi = retrofit.create(MemeApi::class.java)

    
   
    
    
    suspend fun getMeme(): Flow<List<Meme>> {


        return flow {

            val response = memeApi.getMemes()

            if (response.isSuccessful) {
                Log.d(TAG, response.raw().toString())
                response.body()?.data?.memes?.let {
                    emit(it)
                }
            } else {
                Log.e(TAG, response.errorBody().toString())
            }
        }.flowOn(Dispatchers.IO)

    }
}