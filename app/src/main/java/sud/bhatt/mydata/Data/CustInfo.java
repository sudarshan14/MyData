package sud.bhatt.mydata.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustInfo {

    @SerializedName("cust_id")
    @Expose
    private Integer custId;

    @SerializedName("u_name")
    @Expose
    private String uName;

    @SerializedName("u_mobile_num")
    @Expose
    private Long uMobileNum;

    @SerializedName("u_location")
    @Expose
    private String uLocation;
    @SerializedName("u_requirement")
    @Expose
    private String uRequirement;
    @SerializedName("u_budget")
    @Expose
    private String uBudget;
    @SerializedName("info_date")
    @Expose
    private String infoDate;

    @SerializedName("follow_up_date")
    @Expose
    private String followUpDate;


    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public String getUName() {
        return uName;
    }

    public void setUName(String uName) {
        this.uName = uName;
    }

    public Long getUMobileNum() {
        return uMobileNum;
    }

    public void setUMobileNum(Long uMobileNum) {
        this.uMobileNum = uMobileNum;
    }

    public String getULocation() {
        return uLocation;
    }

    public void setULocation(String uLocation) {
        this.uLocation = uLocation;
    }

    public String getURequirement() {
        return uRequirement;
    }

    public void setURequirement(String uRequirement) {
        this.uRequirement = uRequirement;
    }

    public String getUBudget() {
        return uBudget;
    }

    public void setUBudget(String uBudget) {
        this.uBudget = uBudget;
    }

    public String getInfoDate() {
        return infoDate;
    }

    public void setInfoDate(String infoDate) {
        this.infoDate = infoDate;
    }

    public String getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(String followUpDate) {
        this.followUpDate = followUpDate;
    }
}