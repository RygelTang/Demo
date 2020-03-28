package rygel.cn.demo;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

import java.io.IOException;

/**
 * Created by lizhiqi on 15/5/28.
 */
public class IpsVideoView extends SurfaceView {

    private String TAG = "PlaySwitchLog";
    private String source;

    private RelativeLayout.LayoutParams layout;

    private MediaPlayer mediaPlayer;

    private Context context;

    private boolean isPrepared = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mediaPlayer != null) {
                Log.d(TAG, "视频素材开播");
                mediaPlayer.start();
            }
            super.handleMessage(msg);
        }
    };

    public IpsVideoView(Context context, String source, RelativeLayout.LayoutParams layout) {
        super(context);
        this.source = source;
        this.layout = layout;
        this.context = context;
        build();
    }

    private void build() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(layout.width, layout.height);
        params.setMargins(0, 0, 0, 0);
        this.setLayoutParams(params);
        this.getHolder().addCallback(callback);
    }

    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            start();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.d(TAG, "surfaceDestroyed");
            stop();
        }
    };

    protected void stop() {
        if (mediaPlayer != null) {
            Log.d(TAG, "mediaPlayer stop");
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    protected void start() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(source);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        mediaPlayer.setDisplay(this.getHolder());
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d(TAG, "video prepared!");
                isPrepared = true;
//                mediaPlayer.start();
//                mediaPlayer.setLooping(true);
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                mediaPlayer.setOnCompletionListener(null);
                stop();
                return false;
            }
        });
    }

    long time = 0L;

    public void play() {
        time = System.currentTimeMillis();
        Log.e("DEBUG_TAG", "play-" + time);
        ThreadPoolUtil.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                Log.e("DEBUG_TAG", "execute play after-" + (System.currentTimeMillis() - time));
                int retry = 0;
                while (retry < 50) {
                    try {
                        if (isPrepared) {
                            Log.e("DEBUG_TAG", "prepared play after-" + (System.currentTimeMillis() - time));
                            Message msg = handler.obtainMessage();
                            handler.handleMessage(msg);
                            break;
                        }
                        Thread.sleep(100L);
                        retry++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public void pause() {
        if (mediaPlayer != null) {
            Log.d(TAG, "mediaPlayer pause");
            mediaPlayer.pause();
        }
    }

    public void recover() {
        Log.d(TAG, "====recover====");
        if (mediaPlayer != null) {
            mediaPlayer.start();
            Log.d(TAG, "mediaPlayer.start()");
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.d("IpsVideoView", "release");
        stop();
        super.onDetachedFromWindow();
    }


}


