package com.rgcloud.util;

import android.os.Handler;
import android.widget.Button;

/**
 * 倒计时类，使用方法
 * new CountDownUtil(Button, "%s秒", 10).start();
 * 只需在程序中调用简简单单的一行代码即可实现倒计时功能，当然也可以添加监听事件
 **/
public class CountDownUtil implements Runnable {
    private Button button;
    private String countDownText;
    private String defaultText;
    private int remindSecond = 60;
    private Handler handler;
    private OnCountDownListener countDownListener;

    public CountDownUtil(Button button, String countDownText, int remindSecond) {
        this.button = button;
        this.countDownText = countDownText;
        this.remindSecond = remindSecond;
        defaultText = button.getText().toString();
        handler = new Handler();
    }

    /**
     * 开始倒计时
     */
    public void start() {
        handler.removeCallbacks(this);
        button.setClickable(false);
        handler.post(this);
        if (countDownListener != null) {
            countDownListener.onStart();
        }
    }

    /**
     * 结束
     */
    private void stop() {
        handler.removeCallbacks(this);
        button.setClickable(true);
        button.setText(defaultText);
        if (countDownListener != null) {
            countDownListener.onStop();
        }
    }

    @Override
    public void run() {
        if (remindSecond > 0) {
            button.setClickable(false);
            button.setText(String.format(countDownText, remindSecond));
            remindSecond--;
            if (countDownListener != null) {
                countDownListener.onUpdate(remindSecond);
            }
            handler.postDelayed(this, 1000);
        } else {
            stop();
        }

    }

    public void setCountDownListener(OnCountDownListener countDownListener) {
        this.countDownListener = countDownListener;
    }

    /**
     * 回调
     */
    public interface OnCountDownListener {
        void onStart();

        void onStop();

        void onUpdate(int remindSecond);
    }
}