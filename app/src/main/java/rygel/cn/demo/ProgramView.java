package rygel.cn.demo;

public interface ProgramView {

    void start();

    void pause();

    void resume();

    void moveToNext();

    void preloadProgram(String program);

    void setPlayPlanFinishedCallback(ProgramFragment.OnPlayFinishedCallback callback);

    void showLogo();

    void preloadAD(String program);

    void showAD();

    void showProgram();

    void setADFinishedCallback(ProgramFragment.OnPlayFinishedCallback callback);

}
