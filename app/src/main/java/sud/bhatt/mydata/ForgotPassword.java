package sud.bhatt.mydata;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sud.bhatt.mydata.Data.UpdateInfo;
import sud.bhatt.mydata.LoginUtil.LocalDataUtil;
import sud.bhatt.mydata.LoginUtil.Util;
import sud.bhatt.mydata.retrofithelper.APIError;
import sud.bhatt.mydata.retrofithelper.DataService;
import sud.bhatt.mydata.retrofithelper.ErrorUtils;
import sud.bhatt.mydata.retrofithelper.RetrofitClientInstance;

public class ForgotPassword extends AppCompatActivity {

    EditText phoneNumber, password, rePassword;
    String apiKey;
    DataService service;
    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        phoneNumber = findViewById(R.id.et_phone_no);
        password = findViewById(R.id.et_password);
        rePassword = findViewById(R.id.et_confirm_password);
        service = RetrofitClientInstance.getRetrofitClient();

    }

    public void updatePassword(View v) {

        Util.showProgressBar(ForgotPassword.this,"Resetting Password..");
        apiKey = LocalDataUtil.getSharedPreference(ForgotPassword.this, "apiKey", "");
        InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        String pwd = password.getText().toString();
        String rePwd = rePassword.getText().toString();
        String phNo = phoneNumber.getText().toString();

        if (pwd.equalsIgnoreCase("") && rePwd.equalsIgnoreCase("")) {
            Toast.makeText(ForgotPassword.this, "Enter Details", Toast.LENGTH_LONG).show();
            progressBar.dismiss();
            return;
        }

        if (phNo.length() < 10 || phNo.equalsIgnoreCase(""))  {
            progressBar.dismiss();
            Toast.makeText(ForgotPassword.this, "Phone No is wrong", Toast.LENGTH_LONG).show();
            return;
        }

        if (!pwd.equalsIgnoreCase(rePwd)) {
            progressBar.dismiss();
            Toast.makeText(ForgotPassword.this, "Password Do not match", Toast.LENGTH_LONG).show();
            password.setText("");
            rePassword.setText("");
            return;
        }

        final Call<UpdateInfo> call = service.resetPassword( phNo, pwd);
        call.enqueue(new Callback<UpdateInfo>() {


            @Override
            public void onResponse(Call<UpdateInfo> call, Response<UpdateInfo> response) {
                if (!response.isSuccessful()) {
                    APIError error = ErrorUtils.parseError(response);
                    handleLoginFailure(error.message());

                } else {

                    if (response.code() == 201) {
                        if(!response.body().getLerror())
                        handleLoginSuccess(response.body().getLmessage());
                        else if(response.body().getLerror()){
                            handleLoginFailure(response.body().getLmessage());
                        }
                    } else {
                        handleLoginFailure("Password update failed");
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateInfo> call, Throwable t) {
                handleLoginFailure("Update failed. Try again");
            }
        });


    }

    private void handleLoginSuccess(String response) {
        LocalDataUtil.getSharedPreference(this, "apiKey", null);
        Toast.makeText(ForgotPassword.this, response, Toast.LENGTH_LONG).show();
        this.finish();
//        startActivity(new Intent(ForgotPassword.this, MainActivity.class));

    }

    private void handleLoginFailure(String message) {
        RelativeLayout view = findViewById(R.id.relativeLayout);
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        Util.dismissProgressBar();
     //   Toast.makeText(ForgotPassword.this, message, Toast.LENGTH_LONG).show();
    }
}
