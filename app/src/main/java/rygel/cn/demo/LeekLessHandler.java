package rygel.cn.demo;

import android.os.Handler;

import java.lang.ref.SoftReference;

public class LeekLessHandler<T> extends Handler {

    protected SoftReference<T> ref;

    public LeekLessHandler(T instance) {
        this.ref = new SoftReference<>(instance);
    }

}
