package sud.bhatt.mydata.LoginUtil;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import sud.bhatt.mydata.CustInfoActivity;
import sud.bhatt.mydata.MainActivity;

public class MainEmptyActivity extends AppCompatActivity {

    private static MainEmptyActivity inst;

    public static MainEmptyActivity instance() {
        return inst;
    }


    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent activityIntent;

        // go straight to main if a token is stored
        if (Util.getApiKey(this) != null) {
            activityIntent = new Intent(this, CustInfoActivity.class);
        } else {
            activityIntent = new Intent(this, MainActivity.class);
        }

        startActivity(activityIntent);
        finish();
    }
}
