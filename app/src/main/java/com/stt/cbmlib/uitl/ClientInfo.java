package com.stt.cbmlib.uitl;

import com.stt.cbmlib.aidl.ISttClient;

public class ClientInfo {
    public static int ST_ALIVE = 1;
    public static int ST_IDLE = 0;
    public static int ST_PAUSE = 2;
    public static int ST_STOP = 3;
    public int bindID;
    public ISttClient client = null;
    public int pid;
    public int state;

    public ClientInfo(int pid2, int bindID2, int state2, ISttClient client2) {
        this.pid = pid2;
        this.bindID = bindID2;
        this.state = state2;
        this.client = client2;
    }
}
