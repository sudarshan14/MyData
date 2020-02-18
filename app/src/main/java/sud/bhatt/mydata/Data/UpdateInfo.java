package sud.bhatt.mydata.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateInfo {

    @SerializedName("id")
    @Expose
    private String lid;


    @SerializedName("error")
    @Expose
    private boolean lerror;

    @SerializedName("message")
    @Expose
    private String lmessage;


    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }

    public boolean getLerror() {
        return lerror;
    }

    public void setLerror(boolean lerror) {
        this.lerror = lerror;
    }

    public String getLmessage() {
        return lmessage;
    }

    public void setLmessage(String lmessage) {
        this.lmessage = lmessage;
    }
}
