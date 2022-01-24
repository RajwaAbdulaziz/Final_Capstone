package com.tuwaiq.finalcapstone.presentation.listFragment

import androidx.lifecycle.ViewModel
import com.tuwaiq.finalcapstone.MyCallback
import com.tuwaiq.finalcapstone.domain.use_cases.DeleteMoodUseCase
import com.tuwaiq.finalcapstone.domain.use_cases.GetDocumentIdUseCase
import com.tuwaiq.finalcapstone.domain.use_cases.GetMoodsListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "ListViewModel"
@HiltViewModel
class ListViewModel @Inject constructor(private val getMoodsListUseCase: GetMoodsListUseCase,
                                        private val deleteMoodUseCase: DeleteMoodUseCase,
                                        private val getDocumentIdUseCase: GetDocumentIdUseCase) : ViewModel() {

      suspend fun getListOfMoods(day: Int, myCallback: MyCallback){

          return getMoodsListUseCase(day, myCallback)
        }

    fun deleteMood(id: String) = deleteMoodUseCase(id)

    suspend fun getDocumentId(): List<String> {
        return getDocumentIdUseCase()
        }
    }
