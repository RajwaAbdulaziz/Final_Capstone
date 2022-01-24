package com.tuwaiq.finalcapstone.domain.repo

import androidx.lifecycle.LiveData
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.tuwaiq.finalcapstone.MyCallback
import com.tuwaiq.finalcapstone.domain.model.Location
import com.tuwaiq.finalcapstone.domain.model.Mood
import com.tuwaiq.finalcapstone.utils.FirebaseUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

interface MoodRepo {

    suspend fun getListOfMoods(myCallback: MyCallback)

    suspend fun getProfileListOfMoods(): Flow<MutableList<Mood>>

    fun addMood(note: Mood)

    suspend fun getLanLong(googleMap: GoogleMap): Flow<List<Location>>

    fun deleteMood(id: String)

    suspend fun getDocumentId(): List<String>

    suspend fun checkIfMoodLoggedToday(): Flow<Boolean>
}