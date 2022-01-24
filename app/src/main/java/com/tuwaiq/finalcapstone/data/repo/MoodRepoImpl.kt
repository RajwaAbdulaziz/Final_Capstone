package com.tuwaiq.finalcapstone.data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.tuwaiq.finalcapstone.MyCallback
import com.tuwaiq.finalcapstone.domain.model.Location
import com.tuwaiq.finalcapstone.domain.model.Mood
import com.tuwaiq.finalcapstone.domain.repo.MoodRepo
import com.tuwaiq.finalcapstone.presentation.moodDetailsFragment.c
import com.tuwaiq.finalcapstone.utils.FirebaseUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.coroutineContext

private const val TAG = "MoodRepoImpl"
class MoodRepoImpl: MoodRepo {

    override suspend fun getListOfMoods(myCallback: MyCallback) {
            var m: MutableList<Mood> = mutableListOf()
          //  return flow {

                val a = FirebaseUtils().fireStoreDatabase.collection("Mood").addSnapshotListener { value, error ->
                    if (value?.isEmpty == false) {
                            m = value.toObjects(Mood::class.java)
                        myCallback.onCallback(m)

                    }
                }
        }

    override suspend fun getProfileListOfMoods(): Flow<MutableList<Mood>> {
            var m: MutableList<Mood>
            return flow {

                val a = FirebaseUtils().fireStoreDatabase.collection("Mood")
                    .whereEqualTo("owner", FirebaseUtils().auth.currentUser?.uid).get().await()
                m = a.toObjects(Mood::class.java)
                emit(m)
            }
    }

    override fun addMood(note: Mood) {
        val ref = FirebaseUtils().fireStoreDatabase.collection("Mood").document()
        note.moodId = ref.id
        note.privatePic = c
        ref.set(note)
    }

    override suspend fun getLanLong(googleMap: GoogleMap): Flow<List<Location>> {
        val m = mutableListOf<Location>()
        var c = Location()
        return flow {

            val a = FirebaseUtils().fireStoreDatabase.collection("Mood").get().await()
            a.forEach {
               c = Location(it.getString("owner")!!, it.getString("mood")!!, LatLng(it.getDouble("lat")!!, it.getDouble("long")!!))
                m.add(c)
            }

            emit(m as List<Location>)
        }
    }

    override fun deleteMood(id: String) {
        FirebaseUtils().fireStoreDatabase.collection("Mood").document(id).delete()
    }

    override suspend fun getDocumentId(): List<String> {
        val list = mutableListOf<String>()
        val a = FirebaseUtils().fireStoreDatabase.collection("Mood").get().await()
        a.forEach {docId ->
                list.add(docId.id)
        }
        Log.d(TAG, "list: $list")
        return list
    }

    override suspend fun checkIfMoodLoggedToday(): Flow<Boolean> {
        return flow {
            val userMoods = FirebaseUtils().fireStoreDatabase.collection("Mood").whereEqualTo("owner", FirebaseUtils().auth.currentUser?.uid)

            userMoods.get().await().forEach{
                val firstDate = formatDate(it.getDate("date")!!)
                val secondDate = formatDate(Date())

                if (firstDate == secondDate) {
                    Log.d(TAG, "yesss")
                    emit(true)
                } else {
                    emit(false)
                }
            }
        }.flowOn(Dispatchers.Main)
    }

    private fun formatDate(date: Date): String {
        var spf = SimpleDateFormat("E LLL dd hh:mm:ss z yyyy")
        val parsed = spf.parse(date.toString())
        spf = SimpleDateFormat("E LLL dd z yyyy")
        return spf.format(parsed)
    }
}