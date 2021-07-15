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
import com.politics.politicalapp.pojo.GovtWorkListResponse
import com.politics.politicalapp.pojo.request.RegisterRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class GovtWorkViewModel : ViewModel() {

    private val mutableSettingsResponse = MutableLiveData<GovtWorkListResponse>()
    private var apiEndPointsInterface =
        RetrofitFactory.createService(APIEndPointsInterface::class.java)

    /**
     * Dispatchers.IO for network or disk operations that takes longer time and runs in background thread
     */
    fun getGovtWorkList(district_id: String,start: String,end: String) {
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

                val apiResponse = apiEndPointsInterface.getGovtWork(
                    requestBodyBuilder.build()
                )
                returnSettingsResponse(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun returnSettingsResponse(settingsResponse: GovtWorkListResponse) {
        withContext(Dispatchers.Main) {
            mutableSettingsResponse.value = settingsResponse
        }
    }

    fun govtWorkList(): LiveData<GovtWorkListResponse> {
        return mutableSettingsResponse
    }
}