package sud.bhatt.mydata;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sud.bhatt.mydata.AlarmUtil.AlarmReceiver;
import sud.bhatt.mydata.Data.UpdateInfo;
import sud.bhatt.mydata.LoginUtil.LocalDataUtil;
import sud.bhatt.mydata.LoginUtil.MainEmptyActivity;
import sud.bhatt.mydata.LoginUtil.Util;
import sud.bhatt.mydata.retrofithelper.APIError;
import sud.bhatt.mydata.retrofithelper.DataService;
import sud.bhatt.mydata.retrofithelper.ErrorUtils;
import sud.bhatt.mydata.retrofithelper.RetrofitClientInstance;

public class DashBoardActivity extends AppCompatActivity {


    Button saveButton;
    EditText etFullName, etPhoneNo, etLocation, etProjectName, etRequirement, etBudget, etFollowUpDate;
    TextView tv_logo;
    DataService service;
    String apiKey;
    int custId;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        Log.d("lifecycle method","onCreate D");

        toolbar.setTitle(LocalDataUtil.getSharedPreference(this, "name", "Hello User"));

        saveButton = findViewById(R.id.btn_save_details);
        etFullName = findViewById(R.id.et_customer_name);
        etLocation = findViewById(R.id.et_cust_location);
        etProjectName = findViewById(R.id.et_cust_project_name);
        etPhoneNo = findViewById(R.id.et_cust_phone_no);
        etRequirement = findViewById(R.id.et_cust_requirement);
        etBudget = findViewById(R.id.et_cust_budget);
        etFollowUpDate = findViewById(R.id.follow_up_date);
        tv_logo = findViewById(R.id.tv_logo);
//        etFollowUpDate.setOnClickListener((v) -> {
//
//
//        });

        etFollowUpDate.setOnFocusChangeListener((v, hasFocus) -> {

            if (hasFocus) {
                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(DashBoardActivity.this,
                        (view, year, monthOfYear, dayOfMonth) -> etFollowUpDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth), mYear, mMonth, mDay);
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                c.set(mYear, mMonth, mDay, 11, 00);
                datePickerDialog.show();
            }

        });

        service = RetrofitClientInstance.getRetrofitClient();
        apiKey = LocalDataUtil.getSharedPreference(DashBoardActivity.this, "apiKey", "");

        saveButton.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            if (saveButton.getText().equals(getResources().getString(R.string.update_cust_info))) {
                try {
                    updateCustomerDetails();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (saveButton.getText().equals(getResources().getString(R.string.save))) {
                try {
                    createCustomerDetails();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }


        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            custId = extras.getInt("custId");

            etFullName.setText(extras.getString("custName"));
            etPhoneNo.setText(extras.getString("custPhNo"));
            etLocation.setText(extras.getString("custLocation"));
            etProjectName.setText(extras.getString("custProjectName"));
            etRequirement.setText(extras.getString("custRequirement"));
            etBudget.setText(extras.getString("custBudget"));
            etFollowUpDate.setText(extras.getString("custFollowUpDate"));

            //etFollowUpDate.setVisibility(View.VISIBLE);
            saveButton.setText(getString(R.string.update_cust_info));
            tv_logo.setText(getString(R.string.update_cust_info));

        }

    }

    private void updateCustomerDetails() throws ParseException {



        Util.showProgressBar(DashBoardActivity.this, "Updating Customer Details");
        final String etFullNameText = etFullName.getText().toString();
        final String etLocationText = etLocation.getText().toString();
        final String etProjectNameText = etProjectName.getText().toString();
        final String etPhoneNoText = etPhoneNo.getText().toString();
        final String etRequirementText = etRequirement.getText().toString();
        final String etBudgetText = etBudget.getText().toString();
        final String etFollowUpText = etFollowUpDate.getText().toString() ;

        if (Util.isEmpty(etFullNameText, etLocationText, etProjectNameText, etPhoneNoText, etRequirementText)) {
            Toast.makeText(this, "Enter All Details", Toast.LENGTH_LONG).show();
            Util.dismissProgressBar();
            return;
        }
        if (!Util.isLengthTen(etPhoneNoText)) {
            Toast.makeText(DashBoardActivity.this, "Phone Number is Wrong", Toast.LENGTH_LONG).show();
            Util.dismissProgressBar();
            return;
        }

        final Call<UpdateInfo> call = service.updateCustomerDetails(apiKey, custId, Util.firstLetterCapital(etFullNameText), etPhoneNoText, Util.firstLetterCapital(etLocationText), Util.firstLetterCapital(etProjectNameText), etRequirementText, etBudgetText, "", etFollowUpText);

        call.enqueue(new Callback<UpdateInfo>() {
            @Override
            public void onResponse(Call<UpdateInfo> call, Response<UpdateInfo> response) {
                if (!response.isSuccessful()) {
                    APIError error = ErrorUtils.parseError(response);
                    handleFailure(error.message());

                } else {

                    if (response.code() == 200) {
                        if (!response.body().getLerror()) {

                            handleUpdateSuccess(response.body().getLmessage(), etFollowUpText);
                        } else if (response.body().getLerror()) {
                            handleFailure(response.body().getLmessage());
                        }
                    } else {
                        handleFailure("Something went wrong Please try again.");
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateInfo> call, Throwable t) {
                handleFailure("Something went wrong Please try again.");
            }
        });


    }


    private void createCustomerDetails() throws ParseException {


//        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

        Util.showProgressBar(DashBoardActivity.this, "Saving Customer Details");
        final String etFullNameText = etFullName.getText().toString().trim();
        final String etLocationText = etLocation.getText().toString().trim();
        final String etProjectNameText = etProjectName.getText().toString().trim();
        final String etPhoneNoText = etPhoneNo.getText().toString().trim();
        final String etRequirementText = etRequirement.getText().toString().trim();
        final String etBudgetText = etBudget.getText().toString().trim();


//        System.out.println(formatter.format(date));
        if (Util.isEmpty(etFullNameText, etLocationText, etProjectNameText, etPhoneNoText, etRequirementText)) {
            Toast.makeText(this, "Enter All Details", Toast.LENGTH_LONG).show();
            Util.dismissProgressBar();
            return;
        }

        if (!Util.isLengthTen(etPhoneNoText)) {
            Toast.makeText(DashBoardActivity.this, "Phone Number is Wrong", Toast.LENGTH_LONG).show();
            Util.dismissProgressBar();
            return;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        final String etFollowUpText = etFollowUpDate.getText().toString() ;//formatter.format(formatter.parse(etFollowUpDate.getText().toString()));

//        String etFollowUpText = etFollowUpDate.getText().toString();


        Date date = new Date();
        String visitDate = formatter.format(date);

        final Call<UpdateInfo> call = service.createCustInfo(apiKey, Util.firstLetterCapital(etFullNameText), etPhoneNoText,
                Util.firstLetterCapital(etLocationText), Util.firstLetterCapital(etProjectNameText),
                etRequirementText, etBudgetText, visitDate, etFollowUpText);
        call.enqueue(new Callback<UpdateInfo>() {
            @Override
            public void onResponse(Call<UpdateInfo> call, Response<UpdateInfo> response) {
                if (!response.isSuccessful()) {
                    APIError error = ErrorUtils.parseError(response);
                    handleFailure(error.message());

                } else {

                    if (response.code() == 201) {

                        if (!response.body().getLerror())
                            handleSuccess(response.body().getLmessage(), etFollowUpText);
                        else if (response.body().getLerror()) {
                            handleFailure(response.body().getLmessage());
                        }
                    } else {
                        handleFailure("Something went wrong Please try again.");
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateInfo> call, Throwable t) {
                handleFailure("Something went wrong Please try again.");
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.logOut:
                LocalDataUtil.setSharedPreference(DashBoardActivity.this, "apiKey", null);
                startActivity(new Intent(DashBoardActivity.this, MainEmptyActivity.class));
                finish();
                break;


//            case R.id.view:
//                startActivity(new Intent(this, CustInfoActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
//                break;
        }


        return true;
    }


//    private void viewCustomersDetails() {
//
//        final Call<CustDetail> call = service.getCustDetails(apiKey);
//        call.enqueue(new Callback<CustDetail>() {
//            @Override
//            public void onResponse(Call<CustDetail> call, Response<CustDetail> response) {
//
//                if (!response.isSuccessful()) {
//                    APIError error = ErrorUtils.parseError(response);
//                    handleLoginFailure(error.message());
//
//                } else {
//
//                    if (response.code() == 200) {
//                        handleLoginSuccess(response.body());
//                    } else {
//                        handleLoginFailure("Something went wrong Please try again.");
//                    }
//                }
//            }
//
//
//            @Override
//            public void onFailure(Call<CustDetail> call, Throwable t) {
//                handleLoginFailure("Something went wrong Please try again.");
//            }
//        });
//    }


    private void handleUpdateSuccess(String response) {

        CoordinatorLayout view = findViewById(R.id.coordinateLayout);
        Snackbar.make(view, response, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void handleUpdateSuccess(String response, String date) {

        resetEditTextValues();
//        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, AlarmReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
//        // cal.add(Calendar.SECOND, 5);
//        alarmMgr.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        CoordinatorLayout view = findViewById(R.id.coordinateLayout);
        Snackbar.make(view, response, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        //  Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Util.dismissProgressBar();
        startActivity(new Intent(DashBoardActivity.this, CustInfoActivity.class));
        this.finish();
//        Util.setFollowUpDateAlarm(DashBoardActivity.this,  date.substring(0,4),date.substring(5,7), date.substring(8,10) );
//        Util.setFollowUpDateAlarmJobScheduler(DashBoardActivity.this,  date.substring(0,4),date.substring(5,7), date.substring(8,10) );

    }

    private void handleSuccess(String response, String date) {
        CoordinatorLayout view = findViewById(R.id.coordinateLayout);
        Snackbar.make(view, response, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        resetEditTextValues();

//        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, AlarmReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
//        // cal.add(Calendar.SECOND, 5);
//        alarmMgr.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);

        Util.dismissProgressBar();
//        Util.setFollowUpDateAlarm(DashBoardActivity.this,  date.substring(0,3),date.substring(5,6), date.substring(8,9) );

    }

    private void resetEditTextValues() {
        etFullName.setText("");
        etPhoneNo.setText("");
        etLocation.setText("");
        etRequirement.setText("");
        etBudget.setText("");
        etFollowUpDate.setText("");
        etProjectName.setText("");
    }

//    private void handleLoginSuccess(CustDetail response) {
//
//        CoordinatorLayout view = findViewById(R.id.coordinateLayout);
//
//        String details = "";
//        List<CustInfoActivity> custInfoList = response.getCustInfo();
//        for (CustInfoActivity custInfo : custInfoList) {
//            details += "\n " + custInfo.getUName() + custInfo.getInfoDate() + custInfo.getULocation() + custInfo.getURequirement() + custInfo.getUBudget();
//        }
//        Snackbar.make(view, details, Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
//        startActivity(new Intent(this, CustInfoActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
//    }

    private void handleFailure(String message) {
        CoordinatorLayout view = findViewById(R.id.coordinateLayout);
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        //  Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        Util.dismissProgressBar();
    }
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Log.d("lifecycle method","onRestart D");
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.d("lifecycle method","onResume D");
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        Log.d("lifecycle method","onPause D");
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Log.d("lifecycle method","onDestroy D");
//    }
}
