package sud.bhatt.mydata.LoginUtil;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;

import java.util.Calendar;

import sud.bhatt.mydata.AlarmUtil.AlarmJobService;
import sud.bhatt.mydata.AlarmUtil.AlarmReceiverOne;
import sud.bhatt.mydata.AlarmUtil.AlertReceiver;

import static android.content.Context.ALARM_SERVICE;

public class Util {


    public static String getApiKey(Context context) {


        return LocalDataUtil.getSharedPreference(context, "apiKey", null);
    }

    public static boolean isEmpty(String etFullNameText, String etEmailText, String etPhoneNoText, String etPasswordText, String etRePasswordText) {
        //  boolean result = true;


        if (TextUtils.isEmpty(etFullNameText) || TextUtils.isEmpty(etEmailText) || TextUtils.isEmpty(etPhoneNoText) || TextUtils.isEmpty(etPasswordText) || TextUtils.isEmpty(etRePasswordText)) {
            return true;
        } else {
            return false;
        }

    }



    public static boolean isPasswordSame(String etPasswordText, String etRePasswordText) {

        return etPasswordText.equalsIgnoreCase(etRePasswordText);

    }

    public static boolean isLengthTen(String phoneNumner) {

        return phoneNumner.trim().length() == 10;

    }

    public static boolean isEmailCorrect(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }

    public static String firstLetterCapital(String input) {

        String[] names = input.split(" ");
//        int  len = name.split(" ").length;
        //    System.out.println(names[0]);
        String nameCapitalized = "";
        for (String lname : names) {
            if (names.length == 1)
                nameCapitalized += lname.substring(0, 1).toUpperCase() + lname.substring(1);
            else
                nameCapitalized += lname.substring(0, 1).toUpperCase() + lname.substring(1) + " ";
        }

        return nameCapitalized;
    }

    private static ProgressDialog progressBar;


    public static void showProgressBar(Context context, String text) {


        progressBar = new ProgressDialog(context);
        progressBar.setMessage(text);
        progressBar.show();

    }

    public static void dismissProgressBar() {

        if (progressBar!=null)
            progressBar.dismiss();
    }

    public static void setFollowUpDateAlarm(Context context, String year, String month, String date ){


        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(date), 11, 10);

        AlarmManager alarmManager;
        alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent myIntent = new Intent(context, AlertReceiver.class);
         PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setFollowUpDateAlarmJobScheduler(Context context, String year, String month, String date ){


        ComponentName componentName = new ComponentName(context, AlarmJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(123,componentName)
                .setRequiresCharging(false)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .build();

        JobScheduler scheduler = (JobScheduler)context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        scheduler.schedule(jobInfo);

    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void cancelJob(Context context) {
        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);

    }


    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0);

        alarmManager.cancel(pendingIntent);

    }
}
