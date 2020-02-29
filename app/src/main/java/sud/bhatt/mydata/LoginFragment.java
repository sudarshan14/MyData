package sud.bhatt.mydata;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sud.bhatt.mydata.Data.LoginUser;
import sud.bhatt.mydata.Data.LoginUserDetail;
import sud.bhatt.mydata.LoginUtil.LocalDataUtil;
import sud.bhatt.mydata.LoginUtil.Util;
import sud.bhatt.mydata.retrofithelper.APIError;
import sud.bhatt.mydata.retrofithelper.DataService;
import sud.bhatt.mydata.retrofithelper.ErrorUtils;
import sud.bhatt.mydata.retrofithelper.RetrofitClientInstance;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    Button loginButton;
    EditText etPassword, etEmail;
    TextView tv_forgotpassword;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        loginButton = view.findViewById(R.id.btn_login);
        etPassword = view.findViewById(R.id.et_password);
        etEmail = view.findViewById(R.id.et_email);
        tv_forgotpassword = view.findViewById(R.id.tv_forgotpassword);
        // Inflate the layout for this fragment
        final DataService service = RetrofitClientInstance.getRetrofitClient();
//        Call<ResponseBody> call = restService.addUserPhoto(HDFCBaseApplication.resolveAuthorization(), params);


        loginButton.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            Util.showProgressBar(getActivity(), "Logging in");
            String etEmailText = etEmail.getText().toString();
            String etPasswordText = etPassword.getText().toString();

            if (etEmailText.equalsIgnoreCase("") && etPasswordText.equalsIgnoreCase("")) {
                Toast.makeText(getActivity(), "Enter Details", Toast.LENGTH_LONG).show();
                Util.dismissProgressBar();
                return;
            }

//            LoginUser user = new LoginUser(etEmailText, etPasswordText);
            final Call<LoginUserDetail> call =service.initiateLogin(etEmailText, etPasswordText); //service.initiateLogin(user); //
            call.enqueue(new Callback<LoginUserDetail>() {
                @Override
                public void onResponse(Call<LoginUserDetail> call, Response<LoginUserDetail> response) {

                    if (!response.isSuccessful()) {
                        APIError error = ErrorUtils.parseError(response);
                        if (error != null)
                            handleLoginFailure(error.message());
//                        Log.d("error message", error.message());
                    } else {

                        if (response.body().getError()) {
                            handleLoginFailure(response.body().getMessage());
                        } else {
                            handleLoginSuccess(response.body());
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginUserDetail> call, Throwable t) {
                    handleLoginFailure("Something went wrong. Please try again.");
                }


            });
        });

        tv_forgotpassword.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ForgotPassword.class));
        });
        return view;
    }

    private void handleLoginSuccess(LoginUserDetail response) {

        String name = response.getName();
        String emailId = response.getEmail();
        String apiKey = response.getApiKey();
        Double mobile_no = response.getMobileNo();

        LocalDataUtil.setSharedPreference(getActivity(), "apiKey", apiKey);
        LocalDataUtil.setSharedPreference(getActivity(), "name", name);
        LocalDataUtil.setSharedPreference(getActivity(), "emailId", emailId);
        LocalDataUtil.setSharedPreference(getActivity(), "mobile_no", "" + mobile_no);

        startActivity(new Intent(getActivity(), CustInfoActivity.class));
        getActivity().finish();
        Util.dismissProgressBar();

    }

    private void handleLoginFailure(String message) {
        etPassword.setText("");
        Util.dismissProgressBar();
        RelativeLayout view = getActivity().findViewById(R.id.relativeLayout);
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
//        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }


}