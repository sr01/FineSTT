package com.stt.cbmlib;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.rosi.masts.base.Logger;
import com.stt.cbmlib.uitl.OnServiceToClientListener;

public abstract class BaseService extends Service implements OnServiceToClientListener {
    private static String tag = "BaseService";

    private SttClient mSttClient = null;

    protected abstract Logger getLogger();

    public abstract SourceID getSource();

    public IBinder onBind(Intent arg0) {
        return this.mSttClient;
    }

    public void onCreate() {
        super.onCreate();
    }

    public void onDestroy() {
        if (this.mSttClient != null) {
            this.mSttClient.requestDestroy();
            this.mSttClient = null;
        }
        super.onDestroy();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean z = false;
        if (this.mSttClient == null || !this.mSttClient.isBind()) {
            boolean direct = false;
            if (intent != null) {
                direct = intent.getBooleanExtra("direct", false);
            }
            int ordinal = getSource().ordinal();
            if (!direct) {
                z = true;
            }
            this.mSttClient = new SttClient(this, ordinal, this, z, getLogger());
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override // com.stt.cbmlib.uitl.OnServiceToClientListenerBase
    public void onServiceConnected() {
        getLogger().d(tag, "onServiceConnected");
    }

    @Override // com.stt.cbmlib.uitl.OnServiceToClientListenerBase
    public void onServiceDisconnected() {
        getLogger().d(tag, "onServiceDisconnected");
    }

    @Override // com.stt.cbmlib.uitl.OnServiceToClientListener
    public void onStop() {
    }

    @Override // com.stt.cbmlib.uitl.OnServiceToClientListener
    public void onResume() {
    }

    @Override // com.stt.cbmlib.uitl.OnServiceToClientListener
    public void onPause() {
    }

    @Override // com.stt.cbmlib.uitl.OnServiceToClientListener
    public int onKey(int id) {
        return 0;
    }

    @Override // com.stt.cbmlib.uitl.OnServiceToClientListener
    public void onCbmStop(int src) {
        getLogger().d(tag, "onCbmStop, src: " + src);
    }

    @Override // com.stt.cbmlib.uitl.OnServiceToClientListener
    public void onCbmResume(int src) {
        getLogger().d(tag, "onCbmResume, src: " + src);
    }

    @Override // com.stt.cbmlib.uitl.OnServiceToClientListener
    public void onCbmPause(int src) {
        getLogger().d(tag, "onCbmPause, src: " + src);
    }

    @Override // com.stt.cbmlib.uitl.OnServiceToClientListenerBase
    public Bundle onAction(boolean directCall, int action, Bundle b) {
        getLogger().d(tag, "onAction, directCall: " + directCall + ", action: " + action);
        return null;
    }

    public Bundle requestAction(boolean bDirectCall, int action, Bundle b) {
        if (this.mSttClient != null) {
            return this.mSttClient.requestAction(bDirectCall, action, b);
        }
        return null;
    }
}
