package sud.bhatt.mydata;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sud.bhatt.mydata.LoginUtil.Util;
import sud.bhatt.mydata.retrofithelper.APIError;
import sud.bhatt.mydata.retrofithelper.DataService;
import sud.bhatt.mydata.retrofithelper.ErrorUtils;
import sud.bhatt.mydata.retrofithelper.RetrofitClientInstance;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {
    Button registerButton;
    EditText etFullName, etEmail, etPhoneNo, etPassword, etRePassword;
    ProgressDialog progressBar;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        registerButton = view.findViewById(R.id.btn_register);
        etFullName = view.findViewById(R.id.et_name);
        etEmail = view.findViewById(R.id.et_email);
        etPhoneNo = view.findViewById(R.id.et_phone_no);
        etPassword = view.findViewById(R.id.et_password);
        etRePassword = view.findViewById(R.id.et_repassword);

        final DataService service = RetrofitClientInstance.getRetrofitClient();
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar = new ProgressDialog(getActivity());
                progressBar.setMessage("Downloading Details...");
                progressBar.show();
                String etFullNameText = etFullName.getText().toString();
                etFullNameText.substring(0).toUpperCase();
                String etEmailText = etEmail.getText().toString();
                String etPhoneNoText = etPhoneNo.getText().toString();
                String etPasswordText = etPassword.getText().toString();
                String etRePasswordText = etRePassword.getText().toString();


                if (Util.isEmpty(etFullNameText, etEmailText, etPhoneNoText, etPasswordText, etRePasswordText)) {
                    Toast.makeText(getActivity(), "Enter All Details", Toast.LENGTH_LONG).show();
                    Util.dismissProgressBar();
                    return;
                }

                if (!Util.isEmailCorrect(etEmailText)) {
                    Toast.makeText(getActivity(), "Email pattern is wrong", Toast.LENGTH_LONG).show();
                    Util.dismissProgressBar();
                    return;
                }
                if (!Util.isLengthTen(etPhoneNoText)) {
                    Toast.makeText(getActivity(), "Phone Number is Wrong", Toast.LENGTH_LONG).show();
                    Util.dismissProgressBar();
                    return;
                }
                if (!Util.isPasswordSame(etPasswordText, etRePasswordText)) {
                    Toast.makeText(getActivity(), "Password do not match", Toast.LENGTH_LONG).show();
                    Util.dismissProgressBar();
                    return;
                }

                final Call<ResponseBody> call = service.registerUser(Util.firstLetterCapital(etFullNameText), etEmailText, etPasswordText, etPhoneNoText);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if (!response.isSuccessful()) {
                            APIError error = ErrorUtils.parseError(response);
                            if(error.message() != null)
                             handleLoginFailure(error.message());
                            else
                                handleLoginFailure("Something went wrong Please try again.");
                            Log.d("error message", error.message());
                        } else {

                            if (response.code() == 201) {
                                handleLoginSuccess("Account Created Successfully.Please Login.");
                            } else {
                                handleLoginFailure("Something went wrong Please try again.");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        handleLoginFailure("Something went wrong Please try again");
                    }


                });


            }
        });


        return view;

    }



    private void handleLoginSuccess(String response) {
        //   viewPager = (ViewPager) getActivity().findViewById(viewPagerId);
        ViewPager viewPager = getActivity().findViewById(R.id.viewPager);
        viewPager.setCurrentItem(0);
        Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
//
//        String name = response.getName();
//        String emailId = response.getEmail();
//        String apiKey = response.getApiKey();
//        Double mobile_no = response.getMobileNo();

//        LocalDataUtil.setSharedPreference(getActivity(),"apikey",apiKey);
//
//        Intent intent = new Intent(getActivity(),DashBoard.class);
//        intent.putExtra("name",name);
//        intent.putExtra("emailId",emailId);
//        intent.putExtra("mobile_no",mobile_no);
//        startActivity(intent);
        progressBar.dismiss();

    }

    private void handleLoginFailure(String message) {

        progressBar.dismiss();
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}