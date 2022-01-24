package com.tuwaiq.finalcapstone.domain.use_cases

import com.tuwaiq.finalcapstone.MyCallback
import com.tuwaiq.finalcapstone.domain.repo.MoodRepo
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

private const val TAG = "GetMoodsListUseCase"
class GetMoodsListUseCase @Inject constructor(private val moodRepo: MoodRepo) {

    suspend operator fun invoke(day: Int, myCallback: MyCallback) {
//        var a = listOf<Mood>()
//                moodRepo.getListOfMoods(myCallback).collect {
//                    a = it.filter { mood ->
//                        val b = formatDate(Date())
//                        val c = formatDate(mood.date)
//                        if (day == -1) {
//                            b == c
//                        } else {
//                            c == day.toString()
//                        }
//                    }
//                    Log.d(TAG, "list: $a")
//                }
        return moodRepo.getListOfMoods(myCallback)
    }

    private fun formatDate(date: Date): String {
        var spf = SimpleDateFormat("E LLL dd hh:mm:ss z yyyy")
        val parsed = spf.parse(date.toString())
        spf = SimpleDateFormat("d")
        return spf.format(parsed)
    }
}