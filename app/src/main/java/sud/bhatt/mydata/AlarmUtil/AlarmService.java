package sud.bhatt.mydata.AlarmUtil;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;

import sud.bhatt.mydata.MainActivity;
import sud.bhatt.mydata.R;

public class AlarmService extends IntentService {
    private NotificationManager alarmNotificationManager;

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        sendNotification("Wake Up! Wake Up!");
    }

    private void sendNotification(String msg) {
//        Log.d("AlarmService", "Preparing to send notification...: " + msg);
//        alarmNotificationManager = (NotificationManager) this
//                .getSystemService(Context.NOTIFICATION_SERVICE);
//
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, MainEmptyActivity.class), 0);
//
//        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
//                this).setContentTitle("Alarm").setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
//                .setContentText(msg);
//
//
//        alamNotificationBuilder.setContentIntent(contentIntent);
//        alarmNotificationManager.notify(1, alamNotificationBuilder.build());
//        Log.d("AlarmService", "Notification sent.");


        NotificationManager notificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        //Create the content intent for the notification, which launches this activity
        Intent contentIntent = new Intent(this, MainActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (this, 101, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_action_call)
                .setContentTitle(this.getString(R.string.notification_title))
                .setContentText(this.getString(R.string.notification_text))
                .setContentIntent(contentPendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        //Deliver the notification
        notificationManager.notify(101, builder.build());



    }
}