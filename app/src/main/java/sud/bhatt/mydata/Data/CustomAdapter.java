package sud.bhatt.mydata.Data;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sud.bhatt.mydata.R;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyCustomViewHolder> {

    List<CustInfo> custInfo;
    Context context;

    public CustomAdapter(Context context, List<CustInfo> custInfo) {
        this.context = context;
        this.custInfo = custInfo;
    }


    @Override
    public MyCustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowlayout, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        MyCustomViewHolder vh = new MyCustomViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyCustomViewHolder myCustomViewHolder, int position) {
//        animate(myCustomViewHolder);
        CustInfo lCustInfo = custInfo.get(position);

        myCustomViewHolder.custName.setText(lCustInfo.getUName());
        myCustomViewHolder.custPhoneNo.setText("" + lCustInfo.getUMobileNum());
        myCustomViewHolder.custLocation.setText(lCustInfo.getULocation());
        myCustomViewHolder.custRequirement.setText(lCustInfo.getURequirement());
        myCustomViewHolder.custBudget.setText(lCustInfo.getUBudget());
        myCustomViewHolder.custRecordDate.setText(lCustInfo.getInfoDate());
        myCustomViewHolder.custFollowUpDate.setText("Follow Up On: " + lCustInfo.getFollowUpDate());


    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return custInfo.size();
    }


    public class MyCustomViewHolder extends RecyclerView.ViewHolder {
        TextView custName, custPhoneNo, custLocation, custRequirement, custBudget, custRecordDate, custFollowUpDate;
        ImageView custImage, shareImage, callImage, editImage, deleteImage;

        public MyCustomViewHolder(@NonNull View itemView) {
            super(itemView);

            custName = itemView.findViewById(R.id.custName);
            custPhoneNo = itemView.findViewById(R.id.custPhoneNo);
            custLocation = itemView.findViewById(R.id.custLocation);
            custRequirement = itemView.findViewById(R.id.custRequirement);
            custBudget = itemView.findViewById(R.id.custBudget);
            custRecordDate = itemView.findViewById(R.id.custInfoTakenOn);
            custFollowUpDate = itemView.findViewById(R.id.custFollowUpDate);
            custImage = itemView.findViewById(R.id.imageViewCustomer);
            shareImage = itemView.findViewById(R.id.imageShare);
            callImage = itemView.findViewById(R.id.imageCall);
            editImage = itemView.findViewById(R.id.imageEdit);
            deleteImage = itemView.findViewById(R.id.imageDelete);
        }
    }


    public void animate(RecyclerView.ViewHolder viewHolder) {
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(context, R.anim.bounce_interpolator);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }
}
