package ty.learns.android.ndk;

public class HeyFFmpeg {

    public static void sayHey() {
        System.out.println("HeyFFmpeg::sayHey" + ffmpegInfo());
    }

    private static native String ffmpegInfo();
}
