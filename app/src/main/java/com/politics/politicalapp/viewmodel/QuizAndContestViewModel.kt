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
import com.politics.politicalapp.pojo.QuizAndContestRunningResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class QuizAndContestViewModel : ViewModel() {

    private val mutableQuizAndContestResponse = MutableLiveData<QuizAndContestResponse>()
    private val mutableQuizAndContestDetailResponse =
        MutableLiveData<QuizAndContestRunningResponse>()
    private val mutableCommonResponse = MutableLiveData<CommonResponse>()

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

    fun getQuizAndContestDetail(qid: String, user_mobile: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.qid,
                    qid
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.user_mobile,
                    user_mobile
                )

                val apiResponse = apiEndPointsInterface.getQuizAndContestDetail(
                    requestBodyBuilder.build()
                )
                returnQuizAndContestDetailResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addQuiZAnswer(qid: String, user_mobile: String, user_answer: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.qid,
                    qid
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.user_mobile,
                    user_mobile
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.user_answer,
                    user_answer
                )

                val apiResponse = apiEndPointsInterface.addQuiZAnswer(
                    requestBodyBuilder.build()
                )
                returnQuizAndContestAnswerResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun returnQuizAndContestAnswerResponse(apiResponse: CommonResponse) {
        withContext(Dispatchers.Main) {
            mutableCommonResponse.value = apiResponse
        }
    }

    private suspend fun returnQuizAndContestResponse(scratchCardResponse: QuizAndContestResponse) {
        withContext(Dispatchers.Main) {
            mutableQuizAndContestResponse.value = scratchCardResponse
        }
    }

    private suspend fun returnQuizAndContestDetailResponse(scratchCardResponse: QuizAndContestRunningResponse) {
        withContext(Dispatchers.Main) {
            mutableQuizAndContestDetailResponse.value = scratchCardResponse
        }
    }


    fun quizAndContest(): LiveData<QuizAndContestResponse> {
        return mutableQuizAndContestResponse
    }

    fun quizAndContestDetail(): LiveData<QuizAndContestRunningResponse> {
        return mutableQuizAndContestDetailResponse
    }

    fun quizAndContestAnswer(): LiveData<CommonResponse> {
        return mutableCommonResponse
    }
}