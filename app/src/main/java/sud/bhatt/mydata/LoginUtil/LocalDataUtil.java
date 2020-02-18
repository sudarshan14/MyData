package sud.bhatt.mydata.LoginUtil;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalDataUtil {


    public static void setSharedPreference(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences("pref", 0);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(key, value);
        edit.apply();
    }


    public static String getSharedPreference(Context context, String key, String defaultValue) {
        SharedPreferences pref = context.getSharedPreferences("pref", 0);
        return pref.getString(key, defaultValue);
    }
}
