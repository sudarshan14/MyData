package sud.bhatt.mydata.AlarmUtil;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class AlarmJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d("JobScheduler", "Job started");

        NotificationHelper notificationHelper = new NotificationHelper(getApplicationContext());
        NotificationCompat.Builder nb = notificationHelper.getNotification();
        notificationHelper.getManager().notify(1, nb.build());

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
