package sud.bhatt.mydata.Data;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustDetail {

    @SerializedName("custInfo")
    @Expose
    private List<CustInfo> custInfo = null;

    public List<CustInfo> getCustInfo() {
        return custInfo;
    }

    public void setCustInfo(List<CustInfo> custInfo) {
        this.custInfo = custInfo;
    }

}