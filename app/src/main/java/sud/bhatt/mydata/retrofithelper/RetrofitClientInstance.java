package sud.bhatt.mydata.retrofithelper;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sud.bhatt.mydata.constants.Constants;

public class RetrofitClientInstance {
    public static Retrofit retrofit;
    public static DataService dataService;

    //    HttpLoggingInterceptor
    public static DataService getRetrofitClient() {


        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
// add your other interceptors …

// add logging as last interceptor
        httpClient.addInterceptor(logging);
        if (dataService != null) {
            return dataService;
        }

        retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        dataService = retrofit.create(DataService.class);

        return dataService;
    }
}
