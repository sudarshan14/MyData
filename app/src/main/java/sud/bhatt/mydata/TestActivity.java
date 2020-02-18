package sud.bhatt.mydata;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import sud.bhatt.mydata.AlarmUtil.AlarmJobService;
import sud.bhatt.mydata.AlarmUtil.MyNotificationPublisher;

public class TestActivity extends AppCompatActivity {


    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;

    final Calendar myCalendar = Calendar. getInstance () ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void scheduleJobs(View v) {


        ComponentName componentName = new ComponentName(TestActivity.this, AlarmJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(123, componentName)
                .setRequiresCharging(false)
//                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_CELLULAR)
//                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .setOverrideDeadline(0)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(jobInfo);

        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d("JobScheduler", "Job scheduled");
        } else {
            Log.d("JobScheduler", "Job scheduling failed");
        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void cancelJob(View v) {
        JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);

    }
    private void scheduleNotification (Notification notification , long delay) {
        Intent notificationIntent = new Intent( this, MyNotificationPublisher. class ) ;
        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION_ID , 1 ) ;
        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context. ALARM_SERVICE ) ;
        assert alarmManager != null;
        alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , delay , pendingIntent) ;
    }

    private Notification getNotification (String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id ) ;
        builder.setContentTitle( "Scheduled Notification" ) ;
        builder.setContentText(content) ;
        builder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        return builder.build() ;
    }
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet (DatePicker view , int year , int monthOfYear , int dayOfMonth) {
            myCalendar .set(Calendar. YEAR , year) ;
            myCalendar .set(Calendar. MONTH , monthOfYear) ;
            myCalendar .set(Calendar. DAY_OF_MONTH , dayOfMonth) ;
            updateLabel() ;
        }
    } ;

    public void scheduleJob (View view) {
        new DatePickerDialog(
                TestActivity. this, date ,
                myCalendar .get(Calendar. YEAR ) ,
                myCalendar .get(Calendar. MONTH ) ,
                myCalendar .get(Calendar. DAY_OF_MONTH )
        ).show() ;
    }
    private void updateLabel () {
        String myFormat = "dd/MM/yy" ; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat , Locale. getDefault ()) ;
        Date date = myCalendar .getTime() ;
//        btnDate .setText(sdf.format(date)) ;
        scheduleNotification(getNotification( sdf.format(date)) , date.getTime()) ;
    }
}
