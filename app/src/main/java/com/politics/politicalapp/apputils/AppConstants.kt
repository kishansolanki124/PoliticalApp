package app.app.patidarsaurabh.apputils

interface AppConstants {

    companion object {
        const val ACTION_CODE_100 = 100
        const val IS_LOGIN: String = "IS_LOGIN"
        const val MOBILE: String = "MOBILE"
        const val MOBILE_VERIFIED: String = "MOBILE_VERIFIED"
        const val NEWS_ID: String = "NEWS_ID"
        const val IMAGE_LIST: String = "IMAGE_LIST"
        const val IMAGE_URL: String = "IMAGE_URL"
        const val VATAN_GHAM_ID: String = "VATAN_GHAM_ID"
        const val PARIVAR_DARSHAN_ID: String = "PARIVAR_DARSHAN_ID"
        const val UMIYA_MANDIR_ID: String = "UMIYA_MANDIR_ID"
        const val SAMAJIK_SANSTHA_ID: String = "SAMAJIK_SANSTHA_ID"
        const val PATIDAR_SAMAJ_ID: String = "PATIDAR_SAMAJ_ID"
        const val IMAGE_POSITION: String = "IMAGE_POSITION"
        const val FILE_NAME: String = "FILE_NAME"
        const val ORDER_ID: String = "ORDER_ID"
        const val AMOUNT: String = "AMOUNT"
        const val ADD_LAVAJAM: String = "ADD_LAVAJAM"
        const val SHOW_SUBMIT: String = "SHOW_SUBMIT"
        const val ID: String = "id"
    }

    interface APIEndPoints {
        companion object {
            //const val BASE_URL = "https://www.patidarsaurabh.com/demo/patidarsaurabhapi/"
            const val BASE_URL = "https://www.patidarsaurabh.com/patidarsaurabhapi/"
            const val REGISTRATION: String = "registration"
            const val GET_NEWS: String = "get_news"
            const val GET_NEWS_DETAIL: String = "get_news_detail"
            const val ADD_NEWS_LIKE: String = "add_news_like"
            const val REMOVE_NEWS_LIKE: String = "remove_news_like"
            const val NEWS_CATEGORY: String = "get_news_category"
            const val GET_OPINION_POLL: String = "get_opinion_poll"
            const val GET_LAVAJAM_PLAN: String = "online_lavajam_plan"
            const val ADD_LAVAJAM: String = "online_lavajam"
            const val ADVERTISE_PAYMENT: String = "advertise_payment"
            const val ADD_OPINION_POLL: String = "add_opinion_poll"
            const val GET_SHRADDHANJALI: String = "get_shradhanjali"
            const val GET_AVSANNONDH: String = "get_avsan_nondh"
            const val GET_EMAGAZINE: String = "get_emagazine"
            const val EMAGAZINE_AUTH: String = "emagazine_authentication"
            const val GET_VATAN_NU_GHAM: String = "get_vatannugam"
            const val GET_PARIVAR_DARSHAN: String = "get_parivardarshan"
            const val GET_UMIYA_TEMPLE: String = "get_umiyamataji_temple"
            const val GET_SAMAJIK_SANSTHA: String = "get_samajik_sanstha"
            const val GET_PATIDAR_SAMAJ: String = "get_patidar_samaj"
            const val GET_GUJARATI_SAMAJ: String = "get_gujarati_samaj"
            const val INQUIRY: String = "inquiry"
            const val GET_VATAN_NU_GHAM_DETAIL: String = "get_vatannugam_detail"
            const val GET_PARIVAR_DARSHAN_DETAIL: String = "get_parivardarshan_detail"
            const val GET_UMIYA_TEMPLE_DETAIL: String = "get_umiyamataji_temple_detail"
            const val GET_SAMAJIK_SANSTHA_DETAIL: String = "get_samajik_sanstha_detail"
            const val GET_PATIDAR_SAMAJ_DETAIL: String = "get_patidar_samaj_detail"
            const val GET_PRATINIDHI: String = "get_prathinidhi"
            const val GET_NEWS_PORTAL: String = "get_news_portal"
            const val GET_STATIC_PAGE: String = "get_staticpage"
            const val GET_CONTACT_US: String = "get_contact_us"

        }
    }

    interface RequestParameters {
        companion object {
            const val MOBILE = "mobile"
            const val MOBILE_NO = "mobile_no"
            const val NAME = "name"
            const val LAVAJAM_ID = "lavajam_id"
            const val DISTRICT = "district"
            const val ADDRESS = "address"
            const val NOTE = "note"
            const val AMOUNT = "amount"
            const val PINCODE = "pincode"
            const val CITY = "city"
            const val CID = "cid"
            const val START = "start"
            const val END = "end"
            const val SEARCH = "search"
            const val NID = "nid"
            const val USER_MOBILE = "user_mobile"
            const val AID = "aid"
            const val PID = "pid"
            const val VID = "vid"
            const val UID = "uid"
            const val SID = "sid"
            const val EMAIL = "email"
            const val CONTACT_NO = "contact_no"
            const val SUBJECT = "subject"
            const val MESSAGE = "message"
            const val STATE_ID = "state_id"
            const val STATE = "state"
            const val CITY_ID = "city_id"
        }
    }
}