package sud.bhatt.mydata.constants;

import sud.bhatt.mydata.BuildConfig;

public class Constants {

    private final static String PROTOCOL = BuildConfig.PROTOCOL;
    private final static String DOMAIN = BuildConfig.DOMAIN;
    private final static String CONTEXT = BuildConfig.CONTEXT;
    private final static String VERSION = BuildConfig.VERSION;

    public final static String BASE_URL = PROTOCOL + "://" + DOMAIN + "/" + CONTEXT + "/" + VERSION + "/";
}
