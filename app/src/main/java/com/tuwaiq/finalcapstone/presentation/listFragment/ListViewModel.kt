package com.tuwaiq.finalcapstone.presentation.listFragment

import androidx.lifecycle.ViewModel
import com.tuwaiq.finalcapstone.MyCallback
import com.tuwaiq.finalcapstone.domain.use_cases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "ListViewModel"
@HiltViewModel
class ListViewModel @Inject constructor(private val getMoodsListUseCase: GetMoodsListUseCase,
                                        private val deleteMoodUseCase: DeleteMoodUseCase,
                                        private val getDocumentIdUseCase: GetDocumentIdUseCase,
                                        private val checkMoodsVUseCase: CheckMoodsVUseCase,
                                        private val currentUserUseCase: CurrentUserUseCase
) : ViewModel() {

      suspend fun getListOfMoods(day: Int, myCallback: MyCallback){

          return getMoodsListUseCase(day, myCallback)
        }

    fun deleteMood(id: String) = deleteMoodUseCase(id)

    suspend fun getDocumentId(): List<String> {
        return getDocumentIdUseCase()
        }

    suspend fun checkMoodsV(): String? {
        return checkMoodsVUseCase()
    }

//    fun currentUsername(myCallback: MyCallback) {
//        return currentUserUseCase(myCallback)
//    }
}
