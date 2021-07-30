package com.app.colorsofgujarat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.app.patidarsaurabh.apputils.AppConstants
import com.app.colorsofgujarat.network.APIEndPointsInterface
import com.app.colorsofgujarat.network.RetrofitFactory
import com.app.colorsofgujarat.pojo.*
import com.app.colorsofgujarat.pojo.request.RegisterRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class SettingsViewModel : ViewModel() {

    private val mutableSettingsResponse = MutableLiveData<SettingsResponse>()
    private val mutableScratchCardResponse = MutableLiveData<ScratchCardResponse>()
    private val mutableCommonResponse = MutableLiveData<CommonResponse>()
    private val mutableStaticPageResponse = MutableLiveData<StaticPageResponse>()
    private val mutableContactUsResponse = MutableLiveData<ContactUsResponse>()
    private val mutableAddScratchCardResponse = MutableLiveData<CommonResponse>()
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

    fun getStaticPages() {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                val apiResponse = apiEndPointsInterface.getStaticPages()
                returnStaticPages(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getContactUs() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val apiResponse = apiEndPointsInterface.getContactUs()
                returnContactUs(apiResponse)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun inquiry(name: String, contact_no: String, email: String, message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.name,
                    name
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.contact_no,
                    contact_no
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.message,
                    message
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.email,
                    email
                )

                val apiResponse = apiEndPointsInterface.inquiry(
                    requestBodyBuilder.build()
                )
                returnCommonResponse(apiResponse)
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

    fun addScratchCard(user_mobile: String, points: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.USER_MOBILE,
                    user_mobile
                )

                requestBodyBuilder.addFormDataPart(
                    AppConstants.RequestParameters.points,
                    points
                )

                val apiResponse = apiEndPointsInterface.addScratchCard(
                    requestBodyBuilder.build()
                )
                returnAddScratchCardResponse(apiResponse)
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

    private suspend fun returnStaticPages(settingsResponse: StaticPageResponse) {
        withContext(Dispatchers.Main) {
            mutableStaticPageResponse.value = settingsResponse
        }
    }

    private suspend fun returnContactUs(settingsResponse: ContactUsResponse) {
        withContext(Dispatchers.Main) {
            mutableContactUsResponse.value = settingsResponse
        }
    }

    private suspend fun returnScratchCardResponse(scratchCardResponse: ScratchCardResponse) {
        withContext(Dispatchers.Main) {
            mutableScratchCardResponse.value = scratchCardResponse
        }
    }

    private suspend fun returnAddScratchCardResponse(scratchCardResponse: CommonResponse) {
        withContext(Dispatchers.Main) {
            mutableAddScratchCardResponse.value = scratchCardResponse
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

    fun addScratchCardResponse(): LiveData<CommonResponse> {
        return mutableAddScratchCardResponse
    }

    fun staticPages(): LiveData<StaticPageResponse> {
        return mutableStaticPageResponse
    }

    fun contactUs(): LiveData<ContactUsResponse> {
        return mutableContactUsResponse
    }
}