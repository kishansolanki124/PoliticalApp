package com.politics.politicalapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.app.patidarsaurabh.apputils.AppConstants
import com.politics.politicalapp.network.APIEndPointsInterface
import com.politics.politicalapp.network.RetrofitFactory
import com.politics.politicalapp.pojo.CommonResponse
import com.politics.politicalapp.pojo.QuizAndContestResponse
import com.politics.politicalapp.pojo.ScratchCardResponse
import com.politics.politicalapp.pojo.SettingsResponse
import com.politics.politicalapp.pojo.request.RegisterRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class QuizAndContestViewModel : ViewModel() {

    private val mutableQuizAndContestResponse = MutableLiveData<QuizAndContestResponse>()

    private var apiEndPointsInterface =
        RetrofitFactory.createService(APIEndPointsInterface::class.java)

    fun getQuizAndContest(start: String, end: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.start,
                    start
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.end,
                    end
                )

                val apiResponse = apiEndPointsInterface.getQuizAndContest(
                    requestBodyBuilder.build()
                )
                returnQuizAndContestResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun returnQuizAndContestResponse(scratchCardResponse: QuizAndContestResponse) {
        withContext(Dispatchers.Main) {
            mutableQuizAndContestResponse.value = scratchCardResponse
        }
    }


    fun quizAndContest(): LiveData<QuizAndContestResponse> {
        return mutableQuizAndContestResponse
    }
}