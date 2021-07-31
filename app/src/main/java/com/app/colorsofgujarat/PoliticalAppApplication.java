package com.app.colorsofgujarat;

import android.app.Application;
import android.content.Intent;

import com.app.colorsofgujarat.ui.activity.ContestDynamicActivity;
import com.app.colorsofgujarat.ui.activity.DailySpinAndWinActivity;
import com.app.colorsofgujarat.ui.activity.GovtWorkDetailActivity;
import com.app.colorsofgujarat.ui.activity.IntroActivity;
import com.app.colorsofgujarat.ui.activity.LivePollRunningActivity;
import com.app.colorsofgujarat.ui.activity.NewsDetailActivity;
import com.app.colorsofgujarat.ui.activity.QuizAndContestRunningActivity;
import com.app.colorsofgujarat.ui.activity.QuizAndContestWinnerActivity;
import com.app.colorsofgujarat.ui.activity.WinnerNamesActivity;
import com.app.colorsofgujarat.ui.activity.WinnerPrizeDetailActivity;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import app.app.patidarsaurabh.apputils.AppConstants;

public class PoliticalAppApplication extends Application {

    private static final String ONESIGNAL_APP_ID = "653f9a66-5185-45d5-b947-1c9260335b85";
    private static PoliticalAppApplication application;

    public static PoliticalAppApplication getApplication() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        initOneSignal();
    }

    private void initOneSignal() {
        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        OneSignal.setNotificationOpenedHandler(
                result -> {
                    JSONObject jsonObject = result.getNotification().getAdditionalData();
                    //try {
                    if (null != jsonObject.opt(AppConstants.TYPE)) {
                        String notificationType = jsonObject.optString(AppConstants.TYPE);
                        if (null != jsonObject.opt(AppConstants.ID)) {
                            String id = jsonObject.optString(AppConstants.ID);

                            if (notificationType.matches(AppConstants.NotificationType.GOVT_WORK)) {
                                startActivity(new Intent(this, GovtWorkDetailActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        .putExtra(AppConstants.GID, id));
                            } else if (notificationType.matches(AppConstants.NotificationType.NEWS)) {
                                startActivity(new Intent(this, NewsDetailActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        .putExtra(AppConstants.ID, id));
                            } else if (notificationType.matches(AppConstants.NotificationType.LIVE_POLL)) {
                                startActivity(new Intent(this, LivePollRunningActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        .putExtra(AppConstants.ID, id)
                                        .putExtra(AppConstants.SHOW_SUBMIT, true));
                            } else if (notificationType.matches(AppConstants.NotificationType.QUIZ_NEW)) {
                                startActivity(new Intent(this, QuizAndContestRunningActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        .putExtra(AppConstants.ID, id));
                            } else if (notificationType.matches(AppConstants.NotificationType.QUIZ_WINNER)) {
                                startActivity(new Intent(this, QuizAndContestWinnerActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        .putExtra(AppConstants.ID, id));
                            } else if (notificationType.matches(AppConstants.NotificationType.POINT_NEW)) {
                                startActivity(new Intent(this, WinnerPrizeDetailActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        .putExtra(AppConstants.ID, id));
                            } else if (notificationType.matches(AppConstants.NotificationType.POINT_WINNER)) {
                                startActivity(new Intent(this, WinnerNamesActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        .putExtra(AppConstants.ID, id));
                            }
                        }
                        else if (notificationType.matches(AppConstants.NotificationType.SPIN)) {
                            startActivity(new Intent(this, DailySpinAndWinActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        } else if (notificationType.matches(AppConstants.NotificationType.CONTEST)) {
                            startActivity(new Intent(this, ContestDynamicActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }else {
                            startActivity(new Intent(this, IntroActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                    } else {
                        startActivity(new Intent(this, IntroActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                });
    }
}