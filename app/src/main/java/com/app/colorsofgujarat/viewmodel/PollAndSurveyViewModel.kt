package com.app.colorsofgujarat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.app.patidarsaurabh.apputils.AppConstants
import com.app.colorsofgujarat.network.APIEndPointsInterface
import com.app.colorsofgujarat.network.RetrofitFactory
import com.app.colorsofgujarat.pojo.CommonResponse
import com.app.colorsofgujarat.pojo.DistrictPollListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class PollAndSurveyViewModel : ViewModel() {

    private val mutableResponse = MutableLiveData<DistrictPollListResponse>()
    private val commonResponse = MutableLiveData<CommonResponse>()
    private var apiEndPointsInterface =
        RetrofitFactory.createService(APIEndPointsInterface::class.java)

    fun getGovtWorkList(district_id: String, user_mobile: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.district_id,
                    district_id
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.user_mobile,
                    user_mobile
                )

                val apiResponse = apiEndPointsInterface.getDistrictPoll(
                    requestBodyBuilder.build()
                )
                returnDistrictPollListResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addDistrictPollRating(
        district_id: String,
        user_mobile: String,
        pid: String,
        user_rating: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.district_id,
                    district_id
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.user_mobile,
                    user_mobile
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.pid,
                    pid
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.user_rating,
                    user_rating
                )

                val apiResponse = apiEndPointsInterface.addDistrictPollRating(
                    requestBodyBuilder.build()
                )
                returnDistrictPollRatingResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun returnDistrictPollListResponse(settingsResponse: DistrictPollListResponse) {
        withContext(Dispatchers.Main) {
            mutableResponse.value = settingsResponse
        }
    }

    fun getDistrictPoll(): LiveData<DistrictPollListResponse> {
        return mutableResponse
    }

    private suspend fun returnDistrictPollRatingResponse(settingsResponse: CommonResponse) {
        withContext(Dispatchers.Main) {
            commonResponse.value = settingsResponse
        }
    }

    fun getDistrictPollRating(): LiveData<CommonResponse> {
        return commonResponse
    }
}