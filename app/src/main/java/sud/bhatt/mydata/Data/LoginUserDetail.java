package sud.bhatt.mydata.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginUserDetail {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobile_no")
    @Expose
    private Double mobileNo;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("apiKey")
    @Expose
    private String apiKey;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(Double mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}