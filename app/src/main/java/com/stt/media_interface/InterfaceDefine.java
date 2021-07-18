package com.stt.media_interface;

public class InterfaceDefine {
    public static final String ACTION_AUDIO_SERVICE_TO_MAIN = "com.stt.control.audio_to_main";
    public static final String ACTION_MAIN_TO_AUDIO_SERVICE = "com.stt.audioapk.main_to_audio";
    public static final String ACTION_MAIN_TO_VIDEO_SERVICE = "com.stt.videoapk.main_to_video";
    public static final String ACTION_VIDEO_SERVICE_TO_MAIN = "com.stt.control.video_to_main";
    public static final int MAIN_TO_MEDIA_AUDIO_MANAGER_CHANGE = 4;
    public static final int MAIN_TO_MEDIA_SERVICE_CMD_KEY = 3;
    public static final int MAIN_TO_MEDIA_SERVICE_ENTER = 1;
    public static final int MAIN_TO_MEDIA_SERVICE_EXIT = 2;
    public static final int MAIN_TO_MEDIA_SERVICE_FIRST_POWER_ON = 6;
    public static final int MAIN_TO_MEDIA_SERVICE_ON_CREATE = 20;
    public static final int MAIN_TO_MEDIA_SERVICE_ON_PAUSE = 23;
    public static final int MAIN_TO_MEDIA_SERVICE_ON_RESUME = 22;
    public static final int MAIN_TO_MEDIA_SERVICE_ON_START = 21;
    public static final int MAIN_TO_MEDIA_SERVICE_ON_STOP = 24;
    public static final int MAIN_TO_MEDIA_SERVICE_PRIORITY_PLAY_STORAGE_DEVICE = 5;
    public static final int MEDIA_SERVICE_TO_MAIN_CMD_EXIT = 3;
    public static final int MEDIA_SERVICE_TO_MAIN_ON_CREATE = 1;
    public static final int MEDIA_SERVICE_TO_MAIN_ON_DESTROY = 2;
    public static final int MEDIA_SERVICE_TO_MAIN_PLAYSTATUS = 4;
    public static final int UART_KEY_DOWN = 2;
    public static final int UART_KEY_NEXT = 1;
    public static final int UART_KEY_NONE = 0;
    public static final int UART_KEY_ONLY_PAUSE = 6;
    public static final int UART_KEY_ONLY_PLAY = 5;
    public static final int UART_KEY_PLAY = 7;
    public static final int UART_KEY_PREV = 4;
    public static final int UART_KEY_RAND = 10;
    public static final int UART_KEY_RPT = 9;
    public static final int UART_KEY_STOP = 8;
    public static final int UART_KEY_UP = 3;
}
