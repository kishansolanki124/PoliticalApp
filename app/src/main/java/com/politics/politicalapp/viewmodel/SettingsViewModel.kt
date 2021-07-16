package com.politics.politicalapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.app.patidarsaurabh.apputils.AppConstants
import com.politics.politicalapp.pojo.SettingsResponse
import com.politics.politicalapp.network.APIEndPointsInterface
import com.politics.politicalapp.network.RetrofitFactory
import com.politics.politicalapp.pojo.CommonResponse
import com.politics.politicalapp.pojo.ScratchCardResponse
import com.politics.politicalapp.pojo.request.RegisterRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class SettingsViewModel : ViewModel() {

    private val mutableSettingsResponse = MutableLiveData<SettingsResponse>()
    private val mutableScratchCardResponse = MutableLiveData<ScratchCardResponse>()
    private val mutableCommonResponse = MutableLiveData<CommonResponse>()
    private var apiEndPointsInterface =
        RetrofitFactory.createService(APIEndPointsInterface::class.java)

    /**
     * Dispatchers.IO for network or disk operations that takes longer time and runs in background thread
     */
    fun getSettings(user_mobile: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.USER_MOBILE,
                    user_mobile
                )

                val apiResponse = apiEndPointsInterface.getSettings(
                    requestBodyBuilder.build()
                )
                returnSettingsResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getScratchCard(user_mobile: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.USER_MOBILE,
                    user_mobile
                )

                val apiResponse = apiEndPointsInterface.getScratchCard(
                    requestBodyBuilder.build()
                )
                returnScratchCardResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Dispatchers.IO for network or disk operations that takes longer time and runs in background thread
     */
    fun registration(registerRequest: RegisterRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.district_id,
                    registerRequest.district_id
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.city,
                    registerRequest.city
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.name,
                    registerRequest.name
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.mobile,
                    registerRequest.mobile
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.platform,
                    registerRequest.platform
                )

                val apiResponse = apiEndPointsInterface.registration(
                    requestBodyBuilder.build()
                )
                returnCommonResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun returnSettingsResponse(settingsResponse: SettingsResponse) {
        withContext(Dispatchers.Main) {
            mutableSettingsResponse.value = settingsResponse
        }
    }

    private suspend fun returnScratchCardResponse(scratchCardResponse: ScratchCardResponse) {
        withContext(Dispatchers.Main) {
            mutableScratchCardResponse.value = scratchCardResponse
        }
    }

    private suspend fun returnCommonResponse(settingsResponse: CommonResponse) {
        withContext(Dispatchers.Main) {
            mutableCommonResponse.value = settingsResponse
        }
    }


    fun settingsResponse(): LiveData<SettingsResponse> {
        return mutableSettingsResponse
    }

    fun commonResponse(): LiveData<CommonResponse> {
        return mutableCommonResponse
    }

    fun scratchCardResponse(): LiveData<ScratchCardResponse> {
        return mutableScratchCardResponse
    }
}