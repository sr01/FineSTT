package com.stt.cbmlib.uitl;

public interface OnServiceToClientListener extends OnServiceToClientListenerBase {
    void onCbmPause(int i);

    void onCbmResume(int i);

    void onCbmStop(int i);

    int onKey(int i);

    void onPause();

    void onResume();

    void onStop();
}
