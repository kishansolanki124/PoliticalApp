package com.politics.politicalapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.app.patidarsaurabh.apputils.AppConstants
import com.politics.politicalapp.network.APIEndPointsInterface
import com.politics.politicalapp.network.RetrofitFactory
import com.politics.politicalapp.pojo.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class UserAdviseViewModel : ViewModel() {

    private val mutableSettingsResponse = MutableLiveData<UserAdviseResponse>()
    private val mutableUserAdviseDetailResponse = MutableLiveData<UserAdviseDetailResponse>()

    private var apiEndPointsInterface =
        RetrofitFactory.createService(APIEndPointsInterface::class.java)

    fun getUserAdviseList(district_id: String, start: String, end: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.district_id,
                    district_id
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.end,
                    end
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.start,
                    start
                )

                val apiResponse = apiEndPointsInterface.getUserAdvise(
                    requestBodyBuilder.build()
                )
                returnSettingsResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getUserAdviseDetail(aid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.aid,
                    aid
                )
                val apiResponse = apiEndPointsInterface.getUserAdviseDetail(
                    requestBodyBuilder.build()
                )
                returnUserAdviseDetailResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun returnSettingsResponse(settingsResponse: UserAdviseResponse) {
        withContext(Dispatchers.Main) {
            mutableSettingsResponse.value = settingsResponse
        }
    }

    private suspend fun returnUserAdviseDetailResponse(userAdviseDetailResponse: UserAdviseDetailResponse) {
        withContext(Dispatchers.Main) {
            mutableUserAdviseDetailResponse.value = userAdviseDetailResponse
        }
    }

    fun userAdviseList(): LiveData<UserAdviseResponse> {
        return mutableSettingsResponse
    }

    fun userAdviseDetail(): LiveData<UserAdviseDetailResponse> {
        return mutableUserAdviseDetailResponse
    }
}