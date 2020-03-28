package rygel.cn.demo;

import android.os.Message;
import android.os.SystemClock;

public abstract class CountDownTimer {

    private final long mMillisInFuture;

    private final long mCountdownInterval;

    private long mStopTimeInFuture;

    private long millisUntilFinished = 0;

    private boolean isPause = false;

    public CountDownTimer(long millisInFuture, long countDownInterval) {
        mMillisInFuture = millisInFuture;
        mCountdownInterval = countDownInterval;
    }


    public final void cancel() {
        mHandler.removeMessages(MSG);
    }

    public final void pause() {
        cancel();
        isPause = true;
    }

    public synchronized final CountDownTimer start() {
        if (mMillisInFuture <= 0) {
            onFinish();
            return this;
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture;
        if (isPause) {
            mStopTimeInFuture = mStopTimeInFuture - millisUntilFinished;
            isPause = false;
        }
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
        return this;
    }


    public abstract void onTick(long millisUntilFinished);


    public abstract void onFinish();


    private static final int MSG = 1;

    private Handler mHandler = new Handler(this);

    private static class Handler extends LeekLessHandler<CountDownTimer> {

        private Handler(CountDownTimer instance) {
            super(instance);
        }

        @Override
        public void handleMessage(Message msg) {
            final CountDownTimer timer = ref.get();
            if (timer != null) {
                synchronized (timer) {
                    final long millisLeft = timer.mStopTimeInFuture - SystemClock.elapsedRealtime();

                    if (millisLeft <= 0) {
                        timer.onFinish();
                        timer.millisUntilFinished = 0;
                    } else if (millisLeft < timer.mCountdownInterval) {
                        sendMessageDelayed(obtainMessage(MSG), millisLeft);
                    } else {
                        long lastTickStart = SystemClock.elapsedRealtime();
                        timer.onTick(millisLeft);
                        timer.millisUntilFinished = millisLeft;
                        long delay = lastTickStart + timer.mCountdownInterval - SystemClock.elapsedRealtime();

                        while (delay < 0) delay += timer.mCountdownInterval;

                        sendMessageDelayed(obtainMessage(MSG), delay);
                    }
                }
            }
        }
    }
}