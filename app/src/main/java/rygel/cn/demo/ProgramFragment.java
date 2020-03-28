package rygel.cn.demo;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYVideoProgressListener;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

public class ProgramFragment extends Fragment {

    private static final String TAG = "ProgramFragment";

    private CountDownTimer timer;

    private OnPlayFinishedCallback callback;

    private StandardGSYVideoPlayer layout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return layout;
    }

    public void setProgram(Context ctx, final String program) {
//        timer = new CountDownTimer(15 * 1000 - 500, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                Log.i(TAG, "剩余时长 ：" + (millisUntilFinished / 1000));
//            }
//
//            @Override
//            public void onFinish() {
//                if (callback != null) {
//                    callback.onFinished();
//                    callback = null;
//                }
//            }
//        };
        layout = new StandardGSYVideoPlayer(ctx, false);
        GSYVideoOptionBuilder gsyVideoOption = new GSYVideoOptionBuilder();
        gsyVideoOption.setIsTouchWiget(true)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setAutoFullWithSize(true)
                .setShowFullAnimation(false)
                .setNeedLockFull(true)
                .setUrl(program)
                .setCacheWithPlay(true)
                .setStartAfterPrepared(true)
                .setGSYVideoProgressListener(new GSYVideoProgressListener() {
                    @Override
                    public void onProgress(int progress, int secProgress, int currentPosition, int duration) {
                        Log.d(TAG, "progress : " + progress);
                        if (progress >= 95) {
                            if (callback != null) {
                                callback.onFinished();
                                callback = null;
                            }
                        }
                    }
                })
                .setVideoTitle("测试视频")
                .build(layout);
    }

    public void start() {
        Log.d(TAG, "开始播放");
        if (layout != null) {
            layout.getCurrentPlayer().startPlayLogic();
        }
        if (timer != null) {
            timer.start();
            if (layout != null) {
                layout.getCurrentPlayer().startPlayLogic();
            }
        }
    }

    public void pause() {
        if (layout != null) {
            layout.getCurrentPlayer().onVideoPause();
        }
        if (timer != null) {
            timer.cancel();
            if (layout != null) {
                layout.getCurrentPlayer().onVideoPause();
            }
        }
    }

    public void resume() {
        if (layout != null) {
            layout.getCurrentPlayer().onVideoResume();
        }
        if (timer != null) {
            timer.start();
            if (layout != null) {
                layout.getCurrentPlayer().onVideoResume();
            }
        }
    }

    public void setOnPlayFinishedCallback(OnPlayFinishedCallback callback) {
        this.callback = callback;
    }

    public interface OnPlayFinishedCallback {
        void onFinished();
    }

}
