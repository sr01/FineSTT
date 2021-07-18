package com.stt.JavaJni;

public class JavaToJni {
    public static final native int ArmToMcuPacket(int i, int i2, int i3, int i4, byte[] bArr, int i5);

    public static final native void DestroyArmMcuCom();

    public static final native int InitArmMcuCom();

    public static final native int SetLibInfo(int i, int i2, int i3);

    public static final native int debugJavaStringOut(String str);

    public static final native int getTouchMatchedStatus();

    public static final native int isArmMcuComOpen();

    public static final native int javaToJniGpio(int i, int i2, int i3, int i4, int i5);

    public static final native int sendUsbDvdCmd(int i);

    public static final native int setPowerOnType(int i);

    public static final native int setTouchPanelEnable(int i);

    public static final native int simulate_key(int i);

    static {
        System.loadLibrary("JniControl");
    }

    public static void JavaToJniInit() {
    }
}
