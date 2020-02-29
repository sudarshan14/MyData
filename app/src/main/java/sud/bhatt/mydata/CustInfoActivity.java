package sud.bhatt.mydata;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sud.bhatt.mydata.Data.CustDetail;
import sud.bhatt.mydata.Data.CustInfo;
import sud.bhatt.mydata.Data.CustomAdapter;
import sud.bhatt.mydata.LoginUtil.LocalDataUtil;
import sud.bhatt.mydata.LoginUtil.MainEmptyActivity;
import sud.bhatt.mydata.LoginUtil.Util;
import sud.bhatt.mydata.retrofithelper.APIError;
import sud.bhatt.mydata.retrofithelper.DataService;
import sud.bhatt.mydata.retrofithelper.ErrorUtils;
import sud.bhatt.mydata.retrofithelper.RetrofitClientInstance;

public class CustInfoActivity extends AppCompatActivity {
    DataService service;
    String apiKey;
    RecyclerView recyclerView;
    //    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    CustomAdapter customAdapter;
    List<CustInfo> custInfos = new ArrayList<>();
    FloatingActionButton addCustInfo;
    ProgressDialog progressBar;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_info);
        Util.showProgressBar(this, "Fetching Customer Details...");

        Log.d("lifecycle method","onCreate");
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.setTitle(LocalDataUtil.getSharedPreference(this, "name", "Hello User"));
        mSwipeRefreshLayout =  findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            custInfos.clear();
            viewCustomersDetails();
            mSwipeRefreshLayout.setRefreshing(false);
        });


//        CustInfo info = new CustInfo();
//        info.setUName("sudarshan");
//        info.setUBudget("shd");
//        custInfos.add(info);

        addCustInfo = findViewById(R.id.addCustInfo);
        addCustInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustInfoActivity.this, DashBoardActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));


            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        customAdapter = new CustomAdapter(CustInfoActivity.this, custInfos);
        recyclerView.setAdapter(customAdapter);
        recyclerView.addOnItemTouchListener(new CustomRVItemTouchListener(this, recyclerView, new RecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                final int lposition = position;

                final ImageView shareImage = view.findViewById(R.id.imageShare);
                shareImage.setOnClickListener(v -> shareDetails(custInfos.get(lposition)));

                final ImageView callImage = view.findViewById(R.id.imageCall);
                callImage.setOnClickListener(v -> callCustomer(custInfos.get(lposition).getUMobileNum()));

                final ImageView editImage = view.findViewById(R.id.imageEdit);
                editImage.setOnClickListener(v -> {

                    Bundle bundle = new Bundle();
                    bundle.putInt("custId", custInfos.get(lposition).getCustId());
                    bundle.putString("custName", custInfos.get(lposition).getUName());
                    bundle.putString("custPhNo", "" + custInfos.get(lposition).getUMobileNum());
                    bundle.putString("custLocation", custInfos.get(lposition).getULocation());
                    bundle.putString("custProjectName", custInfos.get(lposition).getuProjectName());
                    bundle.putString("custRequirement", custInfos.get(lposition).getURequirement());
                    bundle.putString("custBudget", custInfos.get(lposition).getUBudget());
                    bundle.putString("custFollowUpDate", custInfos.get(lposition).getFollowUpDate());


                    startActivity(new Intent(CustInfoActivity.this, DashBoardActivity.class).putExtras(bundle));
                    // shareDetails(custInfos.get(lposition));
                });

                final ImageView deleteImage = view.findViewById(R.id.imageDelete);
                deleteImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteDetail(custInfos.get(lposition).getCustId());
                    }
                });

//                final LinearLayout shareLayout = view.findViewById(R.id.shareLayout);
//                shareLayout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        shareDetails(custInfos.get(lposition));
//                    }
//                });

//                final LinearLayout shareLayout = view.findViewById(R.id.shareLayout);
//                shareLayout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        shareDetails(custInfos.get(lposition));
//                    }
//                });


            }

            @Override
            public void onLongClick(View view, int position) {
                // shareDetails(custInfos.get(position));
            }
        }));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);

        // specify an adapter (see also next example)

        service = RetrofitClientInstance.getRetrofitClient();
        apiKey = LocalDataUtil.getSharedPreference(CustInfoActivity.this, "apiKey", "");

        viewCustomersDetails();
    }

    private void deleteDetail(Integer custId) {
        Util.showProgressBar(this, "Deleting Customer Details...");
        final Call<ResponseBody> call = service.deleteCustomerInfo(apiKey, custId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (!response.isSuccessful()) {
                    APIError error = ErrorUtils.parseError(response);
                    handleLoginFailure(error.message());

                } else {

                    if (response.code() == 200) {

                        handleLoginSuccess("Customer Delete Successfully");
                    } else {
                        handleLoginFailure("Something went wrong Please try again.");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                handleLoginFailure("Something went wrong Please try again.");
            }
        });
    }

    private void callCustomer(Long uMobileNum) {

        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "" + uMobileNum, null));
        startActivity(intent);
    }


    private void shareDetails(CustInfo custInfo) {

        Uri imageUri = Uri.parse("android.resource://" + getPackageName()
                + "/drawable/" + "ic_launcher");
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        String shareText = "Hi, Please find the below customer. \n Name: " + custInfo.getUName() +
                "\n Location:" + custInfo.getULocation() +
                "\n Requirement:" + custInfo.getURequirement() +
                "\n Budget:" + custInfo.getUBudget() +
                "\n Visited On:" + custInfo.getInfoDate().substring(0, 10) +
                "\n Project Visited: "+custInfo.getuProjectName()+
                "\n \n Please Connect with the customer on the following Number: " + custInfo.getUMobileNum()+
                " on the following date: " + custInfo.getFollowUpDate();
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
//        if (imageUri != null)
//            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("text/plain");
//        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (shareIntent != null)
            startActivity(Intent.createChooser(shareIntent, "send"));


    }


    private void viewCustomersDetails() {
        final Call<CustDetail> call = service.getCustDetails(apiKey);
        call.enqueue(new Callback<CustDetail>() {
            @Override
            public void onResponse(Call<CustDetail> call, Response<CustDetail> response) {

                if (!response.isSuccessful()) {
                    APIError error = ErrorUtils.parseError(response);
                    handleLoginFailure(error.message());

                } else {

                    if (response.code() == 200) {
                        handleLoginSuccess(response.body());
                    } else {
                        handleLoginFailure("Something went wrong Please try again.");
                    }
                }
            }


            @Override
            public void onFailure(Call<CustDetail> call, Throwable t) {
                handleLoginFailure("Something went wrong Please try again.");
            }
        });
    }

    private void handleLoginSuccess(CustDetail response) {
//        custInfos = response.getCustInfo();
        custInfos.addAll(response.getCustInfo());
        customAdapter.notifyDataSetChanged();
        Util.dismissProgressBar();
//        String details = "";
//        List<sud.bhatt.mydata.Data.CustInfo> custInfoList = response.getCustInfo();
//        for (sud.bhatt.mydata.Data.CustInfo custInfo : custInfoList) {
//            details += "\n " + custInfo.getUName() + custInfo.getInfoDate() + custInfo.getULocation() + custInfo.getURequirement() + custInfo.getUBudget();
//        }


    }

    private void handleLoginSuccess(String response) {
        CoordinatorLayout view = findViewById(R.id.coordinateLayout);
        Snackbar.make(view, response, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();


    }

    private void handleLoginFailure(String message) {
        Util.dismissProgressBar();
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public class CustomRVItemTouchListener implements RecyclerView.OnItemTouchListener {

        //GestureDetector to intercept touch events
        GestureDetector gestureDetector;
        private RecyclerViewItemClickListener clickListener;

        public CustomRVItemTouchListener(Context context, final RecyclerView recyclerView, final RecyclerViewItemClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onClick(child, recyclerView.getChildLayoutPosition(child));
                    }
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    //find the long pressed view
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent e) {

            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, recyclerView.getChildLayoutPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public interface RecyclerViewItemClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
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
                LocalDataUtil.setSharedPreference(CustInfoActivity.this, "apiKey", null);
                startActivity(new Intent(CustInfoActivity.this, MainEmptyActivity.class));
                finish();
                break;


//            case R.id.view:
//                startActivity(new Intent(this, CustInfoActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
//                break;
        }


        return true;
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Log.d("lifecycle method","onRestart");
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.d("lifecycle method","onResume");
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        Log.d("lifecycle method","onPause");
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Log.d("lifecycle method","onDestroy");
//    }
}
