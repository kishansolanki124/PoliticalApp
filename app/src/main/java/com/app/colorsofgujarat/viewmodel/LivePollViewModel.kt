package com.app.colorsofgujarat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.app.patidarsaurabh.apputils.AppConstants
import com.app.colorsofgujarat.network.APIEndPointsInterface
import com.app.colorsofgujarat.network.RetrofitFactory
import com.app.colorsofgujarat.pojo.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class LivePollViewModel : ViewModel() {

    private val mutableQuizAndContestResponse = MutableLiveData<LivePollListResponse>()
    private val mutableQuizAndContestDetailResponse =
        MutableLiveData<LivePollDetailResponse>()
    private val mutableCommonResponse = MutableLiveData<CommonResponse>()
    private val mutableGiveMLARatingResponse = MutableLiveData<GiveMLARatingResponse>()

    private var apiEndPointsInterface =
        RetrofitFactory.createService(APIEndPointsInterface::class.java)

    fun getLivePollList(user_mobile: String,start: String, end: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.start,
                    start
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.user_mobile,
                    user_mobile
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.end,
                    end
                )

                val apiResponse = apiEndPointsInterface.getLivePollList(
                    requestBodyBuilder.build()
                )
                returnQuizAndContestResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getLivePollDetail(pid: String, user_mobile: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.pid,
                    pid
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.user_mobile,
                    user_mobile
                )

                val apiResponse = apiEndPointsInterface.getLivePollDetail(
                    requestBodyBuilder.build()
                )
                returnQuizAndContestDetailResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addLivePollAnswer(pid: String, user_mobile: String, user_answer: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.pid,
                    pid
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.user_mobile,
                    user_mobile
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.user_rating,
                    user_answer
                )

                val apiResponse = apiEndPointsInterface.addLivePollAnswer(
                    requestBodyBuilder.build()
                )
                returnQuizAndContestAnswerResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun returnQuizAndContestAnswerResponse(apiResponse: GiveMLARatingResponse) {
        withContext(Dispatchers.Main) {
            mutableGiveMLARatingResponse.value = apiResponse
        }
    }

    private suspend fun returnQuizAndContestResponse(scratchCardResponse: LivePollListResponse) {
        withContext(Dispatchers.Main) {
            mutableQuizAndContestResponse.value = scratchCardResponse
        }
    }

    private suspend fun returnQuizAndContestDetailResponse(scratchCardResponse: LivePollDetailResponse) {
        withContext(Dispatchers.Main) {
            mutableQuizAndContestDetailResponse.value = scratchCardResponse
        }
    }


    fun quizAndContest(): LiveData<LivePollListResponse> {
        return mutableQuizAndContestResponse
    }

    fun quizAndContestDetail(): LiveData<LivePollDetailResponse> {
        return mutableQuizAndContestDetailResponse
    }

    fun quizAndContestAnswer(): LiveData<GiveMLARatingResponse> {
        return mutableGiveMLARatingResponse
    }
}