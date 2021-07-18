package com.stt.cbmlib;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import com.stt.cbmlib.aidl.ISttClient;
import com.stt.cbmlib.aidl.ISttService;
import com.stt.cbmlib.uitl.ClientInfo;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SttService extends ISttService.Stub {
    private static final String TAG = "SttService";
    private static SttService mInstance = null;
    public final Object LOCK = new Object();
    private int mCurBindID = 1;
    public final HashMap<String, ClientInfo> mModuleList = new HashMap<>();
    private OnClientListener mOnClientListener = null;
    public final HashMap<Integer, ClientInfo> mSourceList = new HashMap<>();

    public interface OnClientListener {
        Bundle OnClientAction(boolean z, int i, int i2, Bundle bundle);

        Bundle OnClientAction(boolean z, String str, int i, Bundle bundle);

        void OnClientCreate(int i, int i2);

        void OnClientCreate(String str, int i);

        void OnClientDestroy(int i);

        void OnClientDestroy(String str);

        void OnStateChange(int i, int i2, int i3);

        Bundle onClienNotify(String str, int i, Bundle bundle);
    }

    public static synchronized SttService getInstance() {
        SttService sttService;
        synchronized (SttService.class) {
            if (mInstance == null) {
                mInstance = new SttService();
            }
            sttService = mInstance;
        }
        return sttService;
    }

    public SttService() {
        mInstance = this;
        this.mSourceList.clear();
        this.mCurBindID = 1;
        this.mOnClientListener = null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x005a, code lost:
        if (r8.mOnClientListener == null) goto L_0x0061;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x005c, code lost:
        r8.mOnClientListener.OnClientCreate(r9, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0061, code lost:
        r3.putInt(com.stt.cbmlib.SttClient.KEY_BIND_ID, r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0066, code lost:
        return r3;
     */
    @Override // com.stt.cbmlib.aidl.ISttService
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.os.Bundle sourceCreate(int r9, int r10, com.stt.cbmlib.aidl.ISttClient r11) throws android.os.RemoteException {
        /*
        // Method dump skipped, instructions count: 166
        */
        throw new UnsupportedOperationException("Method not decompiled: com.stt.cbmlib.SttService.sourceCreate(int, int, com.stt.cbmlib.aidl.ISttClient):android.os.Bundle");
    }

    @Override // com.stt.cbmlib.aidl.ISttService
    public void sourceDestroy(int source, int bindID) throws RemoteException {
        Log.d(TAG, "requestDestroy " + source);
        if (this.mOnClientListener != null) {
            this.mOnClientListener.OnClientDestroy(source);
        }
        synchronized (this.LOCK) {
            if (this.mSourceList.containsKey(Integer.valueOf(source))) {
                this.mSourceList.remove(Integer.valueOf(source));
            }
        }
    }

    @Override // com.stt.cbmlib.aidl.ISttService
    public Bundle sourceAction(boolean bDirectCall, int source, int action, Bundle b) throws RemoteException {
        Log.d(TAG, "requestAction " + source);
        if (this.mOnClientListener != null) {
            return this.mOnClientListener.OnClientAction(bDirectCall, source, action, b);
        }
        return null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0056, code lost:
        if (r8.mOnClientListener == null) goto L_0x005d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0058, code lost:
        r8.mOnClientListener.OnClientCreate(r9, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x005d, code lost:
        r3.putInt(com.stt.cbmlib.SttClient.KEY_BIND_ID, r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
        return r3;
     */
    @Override // com.stt.cbmlib.aidl.ISttService
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.os.Bundle moduleCreate(java.lang.String r9, int r10, com.stt.cbmlib.aidl.ISttClient r11) throws android.os.RemoteException {
        /*
        // Method dump skipped, instructions count: 158
        */
        throw new UnsupportedOperationException("Method not decompiled: com.stt.cbmlib.SttService.moduleCreate(java.lang.String, int, com.stt.cbmlib.aidl.ISttClient):android.os.Bundle");
    }

    @Override // com.stt.cbmlib.aidl.ISttService
    public void moduleDestroy(String strModuleName, int bindID) throws RemoteException {
        if (strModuleName != null) {
            Log.d(TAG, "requestDestroy " + strModuleName);
            if (this.mOnClientListener != null) {
                this.mOnClientListener.OnClientDestroy(strModuleName);
            }
            synchronized (this.LOCK) {
                if (this.mModuleList.containsKey(strModuleName)) {
                    this.mModuleList.remove(strModuleName);
                }
            }
        }
    }

    @Override // com.stt.cbmlib.aidl.ISttService
    public Bundle moduleAction(boolean bDirectCall, String strModuleName, int action, Bundle b) throws RemoteException {
        if (strModuleName == null) {
            return null;
        }
        Log.d(TAG, "requestAction " + strModuleName);
        if (this.mOnClientListener != null) {
            return this.mOnClientListener.OnClientAction(bDirectCall, strModuleName, action, b);
        }
        return null;
    }

    @Override // com.stt.cbmlib.aidl.ISttService
    public Bundle Notify(String strModuleName, int nId, Bundle b) {
        if (this.mOnClientListener != null) {
            return this.mOnClientListener.onClienNotify(strModuleName, nId, b);
        }
        return null;
    }

    public ISttClient getClient(int source) {
        ClientInfo info;
        ISttClient client = null;
        synchronized (this.LOCK) {
            if (this.mSourceList.containsKey(Integer.valueOf(source)) && (info = this.mSourceList.get(Integer.valueOf(source))) != null) {
                client = info.client;
            }
        }
        return client;
    }

    public ISttClient getClient(String strModuleName) {
        ClientInfo info;
        if (strModuleName == null) {
            return null;
        }
        ISttClient client = null;
        synchronized (this.LOCK) {
            if (this.mModuleList.containsKey(strModuleName) && (info = this.mModuleList.get(strModuleName)) != null) {
                client = info.client;
            }
        }
        return client;
    }

    public int getSource(int pid) {
        int source = SourceID.SOURCE_NONE.ordinal();
        synchronized (this.LOCK) {
            Iterator<Map.Entry<Integer, ClientInfo>> iterator = this.mSourceList.entrySet().iterator();
            while (true) {
                if (!iterator.hasNext()) {
                    break;
                }
                Map.Entry<Integer, ClientInfo> entry = iterator.next();
                ClientInfo info = entry.getValue();
                if (info != null && pid == info.pid) {
                    source = entry.getKey().intValue();
                    break;
                }
            }
        }
        return source;
    }

    public boolean setClientInfo(int source, int src, int state) {
        ClientInfo info;
        boolean bRet = false;
        synchronized (this.LOCK) {
            if (!(!this.mSourceList.containsKey(Integer.valueOf(source)) || (info = this.mSourceList.get(Integer.valueOf(source))) == null || info.state == state)) {
                info.state = state;
                bRet = true;
            }
        }
        if (bRet && this.mOnClientListener != null) {
            this.mOnClientListener.OnStateChange(source, src, state);
        }
        return bRet;
    }

    public boolean addClient(int source, int pid) {
        Log.d(TAG, "addClient: " + source + ", pid: " + pid);
        boolean bRet = false;
        synchronized (this.LOCK) {
            if (!this.mSourceList.containsKey(Integer.valueOf(source))) {
                this.mSourceList.put(Integer.valueOf(source), new ClientInfo(pid, 0, ClientInfo.ST_IDLE, null));
                bRet = true;
            }
        }
        return bRet;
    }

    public void setOnClientListener(OnClientListener onClientListener) {
        this.mOnClientListener = onClientListener;
    }
}
