package com.stt.JavaJni;

import com.rosi.masts.utils.ByteArrayUtilsKt;
import com.rosi.masts.utils.Logger;
import com.rosi.masts.utils.android.AndroidLogger;
import com.rosi.masts.mvc.view.stt.JniToJavaActor;

public class JniToJava {
    private static Logger logger = AndroidLogger.INSTANCE;
    private static JniToJavaActor actor = null;
    private static final String tag = "JniToJava";

    public static void onMcuData(byte[] buf, int len) {
        logger.d(tag, "onMcuData, buf: " + ByteArrayUtilsKt.toHexArrayString(buf, len) + "\n");
        if (actor != null) {
            actor.onMcuData(buf, len);
        }
    }

    public static void onMcuUpdate(byte[] buf, int len) {
//        logger.d(tag, "onMcuUpdate, buf: " + ByteArrayUtilsKt.toHexArrayString(buf, len) + "\n");
    }

    public static void onMcuErrorData(byte[] buf, int len) {
//        logger.d(tag, "onMcuErrorData, buf: " + ByteArrayUtilsKt.toHexArrayString(buf, len) + "\n");
    }

    public static void onBtData(byte[] buf, int len) {
//        logger.d(tag, "onBtData, buf: " + ByteArrayUtilsKt.toHexArrayString(buf, len) + "\n");
    }

    public static void onGpsData(int gps, int arm) {
//        logger.d(tag, "onGpsData");
    }

    public static void init(JniToJavaActor actor, Logger logger) {
        JniToJava.actor = actor;
        JniToJava.logger = logger;
    }
}
