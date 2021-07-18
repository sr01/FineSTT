package com.stt.cbmlib;

public class CbmDefine {
    public static final String ACTION_STT_CLIENT_TO_SERVICE = "com.stt.CLIENT_TO_SERVICE";
    public static final String ACTION_STT_SERVICE_TO_CLIENT = "com.stt.SERVICE_TO_CLIENT";

    public static SourceID getSourceID(int source) {
        if (source < 0 || source >= SourceID.values().length) {
            return SourceID.SOURCE_NONE;
        }
        return SourceID.values()[source];
    }
}
