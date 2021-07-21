package com.politics.politicalapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.app.patidarsaurabh.apputils.AppConstants
import com.politics.politicalapp.network.APIEndPointsInterface
import com.politics.politicalapp.network.RetrofitFactory
import com.politics.politicalapp.pojo.CommonResponse
import com.politics.politicalapp.pojo.PrizeDetailResponse
import com.politics.politicalapp.pojo.WinnerListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class WinnerViewModel : ViewModel() {

    private val mutableWinnerListResponse = MutableLiveData<WinnerListResponse>()
    private val mutablePrizeDetailResponse =
        MutableLiveData<PrizeDetailResponse>()
    private val mutableCommonResponse = MutableLiveData<CommonResponse>()

    private var apiEndPointsInterface =
        RetrofitFactory.createService(APIEndPointsInterface::class.java)

    fun getWinnerList(start: String, end: String) {
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

                val apiResponse = apiEndPointsInterface.getWinnerList(
                    requestBodyBuilder.build()
                )
                returnQuizAndContestResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getPrizeDetail(pid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.pid,
                    pid
                )

                val apiResponse = apiEndPointsInterface.getPrizeDetail(
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

    private suspend fun returnQuizAndContestAnswerResponse(apiResponse: CommonResponse) {
        withContext(Dispatchers.Main) {
            mutableCommonResponse.value = apiResponse
        }
    }

    private suspend fun returnQuizAndContestResponse(scratchCardResponse: WinnerListResponse) {
        withContext(Dispatchers.Main) {
            mutableWinnerListResponse.value = scratchCardResponse
        }
    }

    private suspend fun returnQuizAndContestDetailResponse(scratchCardResponse: PrizeDetailResponse) {
        withContext(Dispatchers.Main) {
            mutablePrizeDetailResponse.value = scratchCardResponse
        }
    }


    fun winnerList(): LiveData<WinnerListResponse> {
        return mutableWinnerListResponse
    }

    fun prizeDetail(): LiveData<PrizeDetailResponse> {
        return mutablePrizeDetailResponse
    }

    fun quizAndContestAnswer(): LiveData<CommonResponse> {
        return mutableCommonResponse
    }
}