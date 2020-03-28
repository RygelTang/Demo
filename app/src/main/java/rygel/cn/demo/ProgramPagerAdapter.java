package rygel.cn.demo;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class ProgramPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "ProgramPagerAdapter";

    private List<ProgramFragment> programFragments = new ArrayList<>();
    private List<String> programs = new ArrayList<>();
    private ProgramFragment.OnPlayFinishedCallback callback;

    private ViewPager pager;
    private Context ctx;

    public ProgramPagerAdapter(Context ctx, FragmentManager fm, ViewPager pager) {
        super(fm);
        this.ctx = ctx;
        this.pager = pager;
        pager.setAdapter(this);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                // removeFirst();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public Fragment getItem(int i) {
        return programFragments.get(i);
    }

    @Override
    public int getCount() {
        return programFragments.size();
    }

    /**
     * 预加载节目单
     *
     * @param program
     */
    public void preloadProgram(String program) {
        programs.add(program);
        ProgramFragment programFragment = new ProgramFragment();
        programFragment.setOnPlayFinishedCallback(callback);
        programFragment.setProgram(ctx, program);
        programFragments.add(programFragment);
        notifyDataSetChanged();
        Log.d(TAG, "当前预加载队列长度：" + programs.size());
    }

    public void start() {
        int cur = pager.getCurrentItem();
        Log.d(TAG, "当前正在播放第" + cur + "个节目单");
        if (cur >= 0 && programFragments.size() > cur) {
            programFragments.get(cur).start();
        }
    }

    public void pause() {
        int cur = pager.getCurrentItem();
        if (cur >= 0 && programFragments.size() > cur) {
            programFragments.get(cur).pause();
        }
    }

    public void resume() {
        int cur = pager.getCurrentItem();
        if (cur >= 0 && programFragments.size() > cur) {
            programFragments.get(cur).resume();
        }
    }

    public void moveToNext() {
        pause();
//        removeFirst();
//        start();
        int cur = pager.getCurrentItem();
        if (cur >= 0 && cur < getCount()) {
            pager.setCurrentItem(cur + 1, false);
            start();
        }
    }

    private void removeFirst() {
        programs.remove(0);
        programFragments.remove(0);
        Log.d(TAG, "删除首节目单！");
        notifyDataSetChanged();
    }

    public void setOnPlayFinishedCallback(ProgramFragment.OnPlayFinishedCallback callback) {
        this.callback = callback;
    }

}
