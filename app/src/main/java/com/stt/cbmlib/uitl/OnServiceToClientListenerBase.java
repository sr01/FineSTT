package com.stt.cbmlib.uitl;

import android.os.Bundle;

public interface OnServiceToClientListenerBase {
    Bundle onAction(boolean z, int i, Bundle bundle);

    void onServiceConnected();

    void onServiceDisconnected();
}
