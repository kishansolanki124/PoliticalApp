package com.app.colorsofgujarat;

import android.app.Application;

public class PoliticalAppApplication extends Application {

    //private static final String ONESIGNAL_APP_ID = "68f70451-9f1d-4101-a3e1-bbbfe5f58ac1";
    private static PoliticalAppApplication application;

    public static PoliticalAppApplication getApplication() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        //initOneSignal();
    }

//    private void initOneSignal() {
//        // Enable verbose OneSignal logging to debug issues if needed.
//        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
//        OneSignal.initWithContext(this);
//        OneSignal.setAppId(ONESIGNAL_APP_ID);
//
//        OneSignal.setNotificationOpenedHandler(
//                result -> {
//                    JSONObject jsonObject = result.getNotification().getAdditionalData();
//                    //try {
//                    if (null != jsonObject.opt(AppConstants.ID)) {
//                        startActivity(new Intent(this, NewsDetailsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                                .putExtra(AppConstants.NEWS_ID, jsonObject.opt(AppConstants.ID).toString()));
//                    } else {
//                        startActivity(new Intent(this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                    }
//                });
//    }
}