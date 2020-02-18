package sud.bhatt.mydata.AlarmUtil;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import sud.bhatt.mydata.R;

class NotificationHelper extends ContextWrapper {


    private static final String CHANNEL_ID = "1098";
    private static final String CHANNEL_NAME = "reminder";

    private NotificationManager notificationManager;

    public NotificationHelper(Context base) {
        super(base);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }


    public NotificationManager getManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return  notificationManager;
    }

    public NotificationCompat.Builder getNotification(){

       return new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
               .setContentTitle("Alarm!")
               .setContentText("Your AlarmManager is working.")
               .setSmallIcon(R.drawable.ic_action_call);
    }
}
