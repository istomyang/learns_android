package ty.learns.android.ndk;

import android.app.Activity;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;

public class HeyNDK {

    /*
     * 可以在CPP中编译为动态库，然后导入，使用。
     * */
    native void useDLLManualCPP();

//    private void useDLLManual() {
//        System.load("file-path");
//        useDLLManualCPP();
//    }


    // 返回对应人名称的问候语
    static native String[] getHeyWords(String[] names);

    public static void printHeyWords() {
        for (String s : getHeyWords(new String[]{"Tom", "Jerry"})) {
            Log.i("HeyNDK::printHeyWords", s);
        }
    }

    native void showCPPCallJava();

    public String showCPPCallJavaInstanceMethod(String str, int inter){
        Log.i("HeyNDK::showCPPCallJavaStaticMethod", str + inter);
        return str;
    }
    public static void showCPPCallJavaStaticMethod(String[] str, double d){
        Log.i("HeyNDK::showCPPCallJavaStaticMethod", Arrays.toString(str) + d);
    }

    native void runOtherThread();

    public static void showToast(Activity activity) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Toast.makeText(activity, "HeyNDK: showToast in current thread!", Toast.LENGTH_SHORT).show();
        } else {
            activity.runOnUiThread(() -> {
                Toast.makeText(activity, "HeyNDK: showToast in other thread!", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
