package com.example.lib_base.helper;

import static android.view.KeyEvent.KEYCODE_BACK;
import static android.view.KeyEvent.KEYCODE_HOME;
import static android.view.KeyEvent.KEYCODE_VOLUME_DOWN;
import static android.view.KeyEvent.KEYCODE_VOLUME_UP;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;


/**
 * @description: TODO 控制物理按键
 * @author: mlf
 * @date: 2024/8/17 11:05
 * @version: 1.0
 */
public class InputKeyHelper {
    /**
     * 执行shell 命令， 命令中不必再带 adb shell
     *
     * @param cmd
     * @return Sting  命令执行在控制台输出的结果
     */
    public static String execByRuntime(String cmd) {
        Process process = null;
        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
            inputStreamReader = new InputStreamReader(process.getInputStream());
            bufferedReader = new BufferedReader(inputStreamReader);

            int read;
            char[] buffer = new char[4096];
            StringBuilder output = new StringBuilder();
            while ((read = bufferedReader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            return output.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("InputKeyHelper", "出错");
            return null;
        } finally {
            if (null != inputStreamReader) {
                try {
                    inputStreamReader.close();
                } catch (Throwable t) {
                    //
                }
            }
            if (null != bufferedReader) {
                try {
                    bufferedReader.close();
                } catch (Throwable t) {
                    //
                }
            }
            if (null != process) {
                try {
                    process.destroy();
                } catch (Throwable t) {
                    //
                }
            }
        }
    }

    public static void setVolumeDown() {
        new Thread(){
            @Override
            public void run() {
                String s = execByRuntime("adb shell input keyevent " + KEYCODE_VOLUME_DOWN);
                Log.i("InputKeyHelper",s);
            }
        }.start();
    }
    public static void setVolumeUp() {
        new Thread(){
            @Override
            public void run() {
                String s=execByRuntime("input keyevent "+KEYCODE_VOLUME_UP);
                Log.i("InputKeyHelper",s);
            }
        }.start();
    }
    public static void home() {
        new Thread()
        {
            @Override
            public void run() {
                execByRuntime("input keyevent "+KEYCODE_HOME);
            }
        }.start();

    }
    public static void back() {
        new Thread(){
            @Override
            public void run() {
                execByRuntime("input keyevent "+KEYCODE_BACK);
            }
        }.start();

    }
}
