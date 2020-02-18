package sud.bhatt.mydata;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Up {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void main(String[] args) throws ParseException {

//        String name = "sudarshan";
//        String[] names = name.split(" ");
////        int  len = name.split(" ").length;
//        System.out.println(names[0]);
//        String nameCapitalized = "";
//        for (String lname : names) {
//            nameCapitalized += lname.substring(0, 1).toUpperCase() + lname.substring(1)+" ";
//        }
//
//        System.out.println(nameCapitalized);

//        String startDateString = "08-12-2017";
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        System.out.println(LocalDate.parse(startDateString, formatter).format(formatter2));

        try {

            String title = null;

            if ("Hello".equalsIgnoreCase(title)) {
                System.out.println("hello");
            } else {
                System.out.println("no hello");
            }
//            String startDateString = "08-12-2017";
//            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
//            System.out.println(sdf2.format(sdf.parse(startDateString)));
//            String date = sdf2.format(sdf.parse(startDateString));

//            System.out.println(date.substring(0, 4));
//            System.out.println(date.substring(5, 7));
//            System.out.println(date.substring(8, 10));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
