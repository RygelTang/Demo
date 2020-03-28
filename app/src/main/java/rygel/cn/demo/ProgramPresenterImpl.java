package rygel.cn.demo;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ProgramPresenterImpl implements ProgramPresenter {

    private static final String TAG = "ProgramPresenterImpl";

    private Context ctx;
    private ProgramView view;
    private boolean showingLogo = true;
    private boolean inited = false;
    private int preloadIndex = 0;

    private ProgramFragment.OnPlayFinishedCallback playADCallback = new ProgramFragment.OnPlayFinishedCallback() {
        @Override
        public void onFinished() {

        }
    };

    private ProgramFragment.OnPlayFinishedCallback playPlanCallback = new ProgramFragment.OnPlayFinishedCallback() {
        @Override
        public void onFinished() {
            if (preloadIndex > 0 && preloadIndex < programs.size()) {
                preload(programs.get(preloadIndex));
                preloadIndex++;
            } else {
                preloadIndex = 0;
                if (preloadIndex < programs.size()) {
                    preload(programs.get(preloadIndex));
                    preloadIndex++;
                }
            }
            switchProgram();
        }
    };

    private Handler handler = new Handler();

    private List<String> programs = new ArrayList<>();

    private CountDownTimer programUpdater = new CountDownTimer(Long.MAX_VALUE, 10 * 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            updatePrograms();
        }

        @Override
        public void onFinish() {

        }
    };

    public ProgramPresenterImpl(Context context, ProgramView view) {
        this.ctx = context;
        this.view = view;
        programUpdater.start();
        showLogo();
        view.setPlayPlanFinishedCallback(playPlanCallback);
    }

    private void preloadAll(final List<String> programs) {
        if (programs != null) {
            for (String program : programs) {
                preload(program);
            }
        }
    }

    private void preload(String program) {
        view.preloadProgram(program);
    }

    private void updatePrograms() {
        boolean preloadAll = false;
        if (programs.isEmpty()) {
            preloadAll = true;
        }
        programs.clear();
        programs.add("https://stream7.iqilu.com/10339/upload_transcode/202002/18/20200218114723HDu3hhxqIT.mp4");
        programs.add("https://stream7.iqilu.com/10339/upload_transcode/202002/18/20200218093206z8V1JuPlpe.mp4");
        programs.add("https://stream7.iqilu.com/10339/article/202002/18/2fca1c77730e54c7b500573c2437003f.mp4");
        programs.add("https://stream7.iqilu.com/10339/upload_transcode/202002/18/20200218025702PSiVKDB5ap.mp4");
        programs.add("https://stream7.iqilu.com/10339/upload_transcode/202002/18/202002181038474liyNnnSzz.mp4");
        if (preloadAll) {
            preloadAll(programs);
            start();
        }
    }

    public void start() {
        if (view != null) {
            showingLogo = false;
            view.start();
        }
    }

    public void pause() {
        if (view != null) {
            view.pause();
        }
    }

    private void switchProgram() {
        if (view != null) {
            Log.d(TAG, "切换节目单");
            view.moveToNext();
        }
    }

    private void showLogo() {
        if (view != null) {
            showingLogo = true;
            view.showLogo();
            view.pause();
        }
    }

    @Override
    public String getDeviceInfo() {
        return "";
    }

}
