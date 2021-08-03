package com.stt.cbmlib;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;

import com.rosi.masts.base.Logger;
import com.rosi.masts.utils.android.BundleUtilsKt;
import com.stt.cbmlib.aidl.ISttClient;
import com.stt.cbmlib.aidl.ISttService;
import com.stt.cbmlib.uitl.OnServiceToClientListener;
import com.stt.cbmlib.uitl.OnServiceToClientListenerBase;

public class SttClient extends ISttClient.Stub {
    public static final String CLIENT_CONNECT_SERVICE_ACTION_NAME = "com.stt.ServiceCenter";
    public static final int CMD_PAUSE = 2;
    public static final int CMD_PREPAUSE = 4;
    public static final int CMD_RESUME = 1;
    public static final int CMD_STOP = 3;
    private static final int EVENT_BIND_AIDL = 5;
    private static final int EVENT_KEY = 1;
    private static final int EVENT_SOURCE_ACTION = 2;
    private static final int EVENT_USER_ACTION = 4;
    public static final String KEY_BIND_ID = "bindid";
    public static final String SERVICE_CLASS_NAME = "com.stt.control.ServiceCenter";
    public static final String SERVICE_PACKET_NAME = "com.stt.control";
    private static final String TAG = "SttClient";
    private int mBindID = 0;
    private ServiceConnection mConnection = new ServiceConnection() {
        /* class com.stt.cbmlib.SttClient.AnonymousClass1 */

        public void onServiceConnected(ComponentName className, IBinder binder) {
            SttClient.this.mISttService = ISttService.Stub.asInterface(binder);
            mLogger.d(SttClient.TAG, "onServiceConnected");
            if (SttClient.this.mISttService != null) {
                SttClient.this.requestCreate();
            }
            if (SttClient.this.mSourceListener != null) {
                SttClient.this.mSourceListener.onServiceConnected();
            } else if (SttClient.this.mModuleListener != null) {
                SttClient.this.mModuleListener.onServiceConnected();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            SttClient.this.mISttService = null;
            if (SttClient.this.mSourceListener != null) {
                SttClient.this.mSourceListener.onServiceDisconnected();
            } else if (SttClient.this.mModuleListener != null) {
                SttClient.this.mModuleListener.onServiceDisconnected();
            }
        }
    };
    private Context mContext = null;
    private ISttService mISttService = null;
    private Handler mMainHandler = null;
    private OnServiceToClientListenerBase mModuleListener = null;
    private String mModuleName = null;
    private ClientBroadcastReceiver mReceiver = null;
    private int mSource = SourceID.SOURCE_NONE.ordinal();
    private OnServiceToClientListener mSourceListener = null;
    private boolean mbBindService = false;
    private Logger mLogger = null;

    public SttClient(Context context, int source, OnServiceToClientListener client, boolean bMainThread, Logger logger) {
        this.mLogger = logger;
        mLogger.d(TAG, TAG);
        this.mContext = context;
        this.mSourceListener = client;
        this.mSource = source;
        if (bMainThread) {
            createMainHandler();
        }
        registerReceiver();
        bindAidlService();
    }

    public SttClient(Context context, String strModuleName, OnServiceToClientListenerBase client, boolean bMainThread) {
        mLogger.d(TAG, TAG);
        this.mContext = context;
        this.mModuleListener = client;
        this.mModuleName = strModuleName;
        if (bMainThread) {
            createMainHandler();
        }
        registerReceiver();
        bindAidlService();
    }

    /* access modifiers changed from: protected */
    @Override // java.lang.Object
    public void finalize() {
        requestDestroy();
    }

    private void unRegisterReceiver() {
        if (this.mReceiver != null && this.mContext != null) {
            this.mContext.unregisterReceiver(this.mReceiver);
            this.mReceiver = null;
        }
    }

    private void registerReceiver() {
        if (this.mContext != null && this.mReceiver == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(CbmDefine.ACTION_STT_SERVICE_TO_CLIENT);
            this.mReceiver = new ClientBroadcastReceiver(this, null);
            this.mContext.registerReceiver(this.mReceiver, filter);
        }
    }

    private void unBindAidlService() {
        if (this.mbBindService && this.mContext != null && this.mConnection != null) {
            try {
                this.mContext.unbindService(this.mConnection);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.mbBindService = false;
            this.mISttService = null;
        }
    }

    private void bindAidlService() {
        if (!this.mbBindService && this.mContext != null && this.mConnection != null) {
            Intent intent = new Intent(CLIENT_CONNECT_SERVICE_ACTION_NAME);
            intent.setComponent(new ComponentName(SERVICE_PACKET_NAME, SERVICE_CLASS_NAME));
            try {
                this.mbBindService = this.mContext.bindService(intent, this.mConnection, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isBind() {
        return this.mISttService != null;
    }

    private void createMainHandler() {
        Looper mainLooper = Looper.getMainLooper();
        if (mainLooper == null) {
            mainLooper = Looper.myLooper();
        }
        if (mainLooper != null) {
            this.mMainHandler = new Handler(mainLooper) {
                /* class com.stt.cbmlib.SttClient.AnonymousClass2 */

                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 1:
                            if (SttClient.this.mSourceListener != null) {
                                SttClient.this.mSourceListener.onKey(msg.arg1);
                                return;
                            }
                            return;
                        case 2:
                            SttClient.this.setSourceAction(msg.arg1);
                            return;
                        case 3:
                        default:
                            return;
                        case 4:
                            if (SttClient.this.mSourceListener != null) {
                                SttClient.this.mSourceListener.onAction(false, msg.arg1, (Bundle) msg.obj);
                                return;
                            } else if (SttClient.this.mModuleListener != null) {
                                SttClient.this.mModuleListener.onAction(false, msg.arg1, (Bundle) msg.obj);
                                return;
                            } else {
                                return;
                            }
                    }
                }
            };
        }
    }

    @Override // com.stt.cbmlib.aidl.ISttClient
    public int onKey(int id) throws RemoteException {
        mLogger.d(TAG, "onKey " + id);
        if (this.mMainHandler != null) {
            this.mMainHandler.obtainMessage(1, id, 0).sendToTarget();
            return 1;
        } else if (this.mSourceListener != null) {
            return this.mSourceListener.onKey(id);
        } else {
            return 1;
        }
    }

    @Override // com.stt.cbmlib.aidl.ISttClient
    public void onSourceAction(int cmd) throws RemoteException {
        mLogger.d(TAG, "onSourceAction " + cmd);
        if (this.mMainHandler != null) {
            this.mMainHandler.obtainMessage(2, cmd, 0).sendToTarget();
        } else {
            setSourceAction(cmd);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setSourceAction(int cmd) {
        if (this.mSourceListener != null) {
            switch (cmd) {
                case 1:
                    this.mSourceListener.onResume();
                    return;
                case 2:
                    this.mSourceListener.onPause();
                    return;
                case 3:
                    this.mSourceListener.onStop();
                    return;
                default:
                    return;
            }
        }
    }

    @Override // com.stt.cbmlib.aidl.ISttClient
    public Bundle onUserAction(boolean bDirectCall, int action, Bundle b) throws RemoteException {
        if (!bDirectCall && this.mMainHandler != null) {
            this.mMainHandler.obtainMessage(4, action, 0, b).sendToTarget();
        } else if (this.mSourceListener != null) {
            return this.mSourceListener.onAction(true, action, b);
        } else {
            if (this.mModuleListener != null) {
                return this.mModuleListener.onAction(true, action, b);
            }
        }
        return null;
    }

    public void requestCreate() {
        Bundle b;
        mLogger.d(TAG, "requestCreate");
        if (this.mISttService == null) {
            mLogger.d(TAG, "requestCreate fail becase mISttService is null");
            return;
        }
        try {
            new Bundle();
            if (this.mModuleName != null) {
                b = this.mISttService.moduleCreate(this.mModuleName, this.mBindID, this);
            } else {
                b = this.mISttService.sourceCreate(this.mSource, this.mBindID, this);
            }
            if (b != null) {
                this.mBindID = b.getInt(KEY_BIND_ID, 0);
            }
        } catch (RemoteException e) {
            mLogger.e(TAG, "requestCreate fail");
        }
    }

    public void requestDestroy() {
        mLogger.d(TAG, "requestDestroy");
        if (this.mISttService != null) {
            try {
                if (this.mModuleName != null) {
                    this.mISttService.moduleDestroy(this.mModuleName, this.mBindID);
                } else {
                    this.mISttService.sourceDestroy(this.mSource, this.mBindID);
                }
            } catch (RemoteException e) {
                mLogger.e(TAG, "requestDestroy fail");
            }
        }
        if (this.mMainHandler != null) {
            this.mMainHandler.removeMessages(5);
        }
        unBindAidlService();
        unRegisterReceiver();
        this.mISttService = null;
    }

    public Bundle requestAction(boolean bDirectCall, int action, Bundle b) {
        mLogger.d(TAG, "requestAction " + action);
        if (this.mISttService == null) {
            mLogger.d(TAG, "requestAction fail becase mISttService is null");
            return null;
        }
        Bundle bundle = new Bundle();
        try {
            if (this.mModuleName != null) {
                return this.mISttService.moduleAction(bDirectCall, this.mModuleName, action, b);
            }
            return this.mISttService.sourceAction(bDirectCall, this.mSource, action, b);
        } catch (RemoteException e) {
            mLogger.e(TAG, "requestAction fail");
            return bundle;
        }
    }

    /* access modifiers changed from: private */
    public class ClientBroadcastReceiver extends BroadcastReceiver {
        private ClientBroadcastReceiver() {
        }

        /* synthetic */ ClientBroadcastReceiver(SttClient sttClient, ClientBroadcastReceiver clientBroadcastReceiver) {
            this();
        }

        public void onReceive(Context context, Intent intent) {
            String action;
            if (intent != null && (action = intent.getAction()) != null && action.equals(CbmDefine.ACTION_STT_SERVICE_TO_CLIENT)) {
                mLogger.d(TAG, "onReceive, intent: " + BundleUtilsKt.toPrettyString(intent));

                if (SttClient.this.mModuleName != null) {
                    String moduleName = intent.getStringExtra("moduleName");
                    if (moduleName == null || !moduleName.equals(SttClient.this.mModuleName)) {
                        return;
                    }
                } else if (intent.getIntExtra("source", SourceID.SOURCE_NONE.ordinal()) != SttClient.this.mSource) {
                    return;
                }

                String actionFuction = intent.getStringExtra("action_fuction");

                if (actionFuction == null) {
                    return;
                }
                if (actionFuction.equals("onSourceAction")) {

                    SttClient.this.setSourceAction(intent.getIntExtra("cmd", 0));

                } else if (actionFuction.equals("onUserAction")) {

                    int act = intent.getIntExtra("action", 0);
                    Bundle b = intent.getBundleExtra("b");

                    if (SttClient.this.mSourceListener != null) {
                        SttClient.this.mSourceListener.onAction(false, act, b);
                    } else if (SttClient.this.mModuleListener != null) {
                        SttClient.this.mModuleListener.onAction(false, act, b);
                    }
                } else if (actionFuction.equals("onKey") && SttClient.this.mSourceListener != null) {
                    SttClient.this.mSourceListener.onKey(intent.getIntExtra("id", 0));
                }
            }
        }
    }
}
