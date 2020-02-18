package sud.bhatt.mydata.retrofithelper;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import sud.bhatt.mydata.Data.CustDetail;
import sud.bhatt.mydata.Data.LoginUserDetail;
import sud.bhatt.mydata.Data.UpdateInfo;

public interface DataService {


//    @POST("users/new")
//    Call<LoginUser> createUser(@Body LoginUser user);


    @GET("custInfo")
    Call<CustDetail> getCustDetails(@Header("Authorization") String apiKey);


    @FormUrlEncoded
    @POST("login")
    Call<LoginUserDetail> initiateLogin(@Field("mobile_no") String mobileNo, @Field("password") String password);


    @FormUrlEncoded
    @POST("register")
    Call<ResponseBody> registerUser(@Field("name") String name, @Field("email") String email, @Field("password") String password, @Field("mobile_no") String mobile_no);

    @FormUrlEncoded
    @POST("createCustInfo")
    Call<UpdateInfo> createCustInfo(@Header("Authorization") String apiKey, @Field("u_name") String userName, @Field("u_mobile_num") String mobileNo, @Field("u_location") String location, @Field("u_requirement") String requirement, @Field("u_budget") String budget, @Field("info_date") String date, @Field("follow_up_date") String followUpDate);

    @DELETE("deleteCustomer/{id}")
    Call<ResponseBody> deleteCustomerInfo(@Header("Authorization") String apiKey ,@Path("id") Integer id);

    @FormUrlEncoded
    @PUT("updateCustomerDetail/{id}")
    Call<UpdateInfo> updateCustomerDetails(@Header("Authorization") String apiKey, @Path("id") Integer id, @Field("u_name") String userName, @Field("u_mobile_num") String mobileNo, @Field("u_location") String location, @Field("u_requirement") String requirement, @Field("u_budget") String budget, @Field("info_date") String date, @Field("follow_up_date") String followUpDate);


    @FormUrlEncoded
    @PUT("resetPassword/{mobile_no}")
    Call<UpdateInfo> resetPassword(@Path("mobile_no") String mobileNo, @Field("password") String password  );

}
