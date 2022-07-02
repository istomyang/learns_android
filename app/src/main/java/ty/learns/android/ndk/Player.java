package ty.learns.android.ndk;

import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class Player implements SurfaceHolder.Callback {

    private String dataSource; // 文件路径
    private SurfaceHolder surfaceHolder;
    private OnPrepareListener onPrepareListener;
    private OnErrorListener onErrorListener;
    private OnProgressListener onProgressListener;

    public void setSurfaceView(SurfaceView surfaceView) {
        if (null != this.surfaceHolder) {
            this.surfaceHolder.removeCallback(this);
        }
        this.surfaceHolder = surfaceView.getHolder();
        native_set_surface(surfaceHolder.getSurface());
        this.surfaceHolder.addCallback(this);
    }

    public void prepare() {
        native_prepare(dataSource);
    }

    // Call by native.
    public void onError(int errorCode) {
        stop();
        if (null != onErrorListener) {
            onErrorListener.onError(errorCode);
        }
    }

    // Call by native.
    public void onPrepare() {
        if (null != onPrepareListener) {
            onPrepareListener.onPrepared();
        }
    }

    // // Call by native.
    public void onProgress(int progress) {
        if (null != onProgressListener) {
            onProgressListener.onProgress(progress);
        }
    }

    public void start() {
        native_start();
    }

    public void stop() {
        native_stop();
    }

    public int getDuration() {
        return native_getDuration();
    }

    public void seek(final int progress) {
        new Thread() {
            @Override
            public void run() {
                native_seek(progress);
            }
        }.start();
    }

    public void release() {
        if (null != this.surfaceHolder) {
            this.surfaceHolder.removeCallback(this);
        }
        native_release();
    }

    private native void native_start();
    private native void native_prepare(String dataSource);
    private native void native_set_surface(Surface surface);
    private native void native_stop();
    private native void native_release();
    private native int native_getDuration();
    private native void native_seek(int progress);

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }
    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }
    public void setOnPrepareListener(OnPrepareListener onPrepareListener) {
        this.onPrepareListener = onPrepareListener;
    }
    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
    }

    public interface OnPrepareListener {
        void onPrepared();
    }

    public interface OnErrorListener {
        void onError(int error);
    }

    public interface OnProgressListener {
        void onProgress(int progress);
    }
}
