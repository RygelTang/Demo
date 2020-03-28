package rygel.cn.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class ProgramActivity extends AppCompatActivity implements ProgramView {

    private static final String TAG = "ProgramActivity";

    private ViewPager program;
    private ViewPager ad;

    private ProgramPagerAdapter programAdapter;
    private ProgramPagerAdapter adAdapter;

    private ProgramPresenter presenter;

    private CountDownTimer deviceInfoUpdater = new CountDownTimer(Long.MAX_VALUE, 12 * 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            updateDeviceInfo();
        }

        @Override
        public void onFinish() {

        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLayout();
        presenter = new ProgramPresenterImpl(this, this);
    }

    private void initLayout() {
        program = findViewById(R.id.vpProgram);
        ad = findViewById(R.id.vpAD);

        initProgramLayout();
        initADLayout();
        showLogo();
    }

    private void initProgramLayout() {
        programAdapter = new ProgramPagerAdapter(this, getSupportFragmentManager(), program);
    }

    private void initADLayout() {
        adAdapter = new ProgramPagerAdapter(this, getSupportFragmentManager(), ad);
    }

    private void updateDeviceInfo() {

    }

    @Override
    public void start() {
        deviceInfoUpdater.cancel();
        programAdapter.start();
        program.setVisibility(View.VISIBLE);
        ad.setVisibility(View.GONE);
    }

    @Override
    public void pause() {
        programAdapter.pause();
    }

    @Override
    public void resume() {
        programAdapter.resume();
    }

    @Override
    public void moveToNext() {
        programAdapter.moveToNext();
    }

    @Override
    public void showLogo() {
        Log.d(TAG, "Show Logo");
        deviceInfoUpdater.start();
        program.setVisibility(View.GONE);
        ad.setVisibility(View.GONE);
    }

    @Override
    public void preloadProgram(String program) {
        programAdapter.preloadProgram(program);
    }

    @Override
    public void setPlayPlanFinishedCallback(ProgramFragment.OnPlayFinishedCallback callback) {
        programAdapter.setOnPlayFinishedCallback(callback);
    }

    @Override
    public void preloadAD(String program) {
        adAdapter.preloadProgram(program);

    }

    @Override
    public void setADFinishedCallback(ProgramFragment.OnPlayFinishedCallback callback) {
        adAdapter.setOnPlayFinishedCallback(callback);
    }

    @Override
    public void showAD() {
        adAdapter.start();
        program.setVisibility(View.GONE);
        ad.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgram() {
        adAdapter.pause();
        program.setVisibility(View.GONE);
        ad.setVisibility(View.VISIBLE);
    }
}
