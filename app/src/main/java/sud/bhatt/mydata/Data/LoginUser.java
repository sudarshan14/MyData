package sud.bhatt.mydata.Data;

import com.google.gson.annotations.SerializedName;

public class LoginUser {

    @SerializedName("mobile_no")
    public String mobile_no;

    @SerializedName("password")
    public String password;

    public LoginUser(String mobile_no, String password) {
        this.mobile_no = mobile_no;
        this.password = password;
    }
}
