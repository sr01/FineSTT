//package com.stt.JavaJni;
//
//import android.app.ActivityManager;
//import android.content.Context;
//import android.content.Intent;
//import android.hardware.input.InputManager;
//import android.net.Uri;
//import android.os.Build;
//import android.os.RemoteException;
//import android.os.SystemClock;
//import android.view.KeyEvent;
//
//import com.stt.cbmlib.uitl.KeyDefine;
//import com.stt.control.ControlApp;
//import com.stt.control.StaticConstant;
//import com.stt.manager.ApkManager;
//import com.stt.manager.Define;
//import com.stt.projects.ProjectHandle;
//import com.stt.tool.tool;
//
//public class SimulatorKey {
//    private static String EXTRA_FROM_HOME_KEY = "android.intent.extra.FROM_HOME_KEY";
//
//    public static int onKey(int key) {
//        switch (key) {
//            case 51:
//                JniControl.simulate_key(KeyDefine.UART_KEY_CARVIDEO);
//                break;
//            case KeyDefine.UART_KEY_HOME /*{ENCODED_INT: 96}*/:
//                if (Build.VERSION.SDK_INT < 26) {
//                    if (ApkManager.startApkActivity(ControlApp.getAppContext(), false, Define.mLauncherPacketName, null, null, false) <= 0 && ApkManager.startApkActivity(ControlApp.getAppContext(), false, Define.mLauncher2PacketName, null, null, false) <= 0 && ApkManager.startApkActivity(ControlApp.getAppContext(), false, Define.mLauncher3PacketName, null, null, false) <= 0) {
//                        JniControl.simulate_key(102);
//                        break;
//                    }
//                } else {
//                    if (ProjectHandle.getCurProject().isSimulatorHomeKey()) {
//                        long downTime = SystemClock.uptimeMillis();
//                        sendEvent(0, 0, 3, downTime, downTime);
//                        sendEvent(1, 0, 3, downTime, downTime + 5);
//                    } else {
//                        startHome();
//                    }
//                    return 1;
//                }
//            case KeyDefine.UART_KEY_BACK /*{ENCODED_INT: 132}*/:
//                JniControl.simulate_key(StaticConstant.CMD_ENTER_COPY);
//                break;
//            default:
//                return -1;
//        }
//        return 1;
//    }
//
//    private static void sendEvent(int action, int flags, int code, long downTime, long when) {
//        InputManager.getInstance().injectInputEvent(new KeyEvent(downTime, when, action, code, (flags & 128) != 0 ? 1 : 0, 0, -1, 0, flags | 8 | 64, 257), 0);
//    }
//
//    public static boolean startHome() {
//        Context context = tool.getContext();
//        if (context == null) {
//            return false;
//        }
//        Intent intent = new Intent("android.intent.action.MAIN", (Uri) null);
//        intent.addCategory("android.intent.category.HOME");
//        intent.addFlags(270532608);
//        intent.putExtra(EXTRA_FROM_HOME_KEY, true);
//        intent.putExtra("flag", 1);
//        try {
//            ActivityManager.getService().stopAppSwitches();
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        } catch (Exception e2) {
//            e2.printStackTrace();
//        }
//        try {
//            context.startActivity(intent);
//            return true;
//        } catch (Exception e3) {
//            e3.printStackTrace();
//            return false;
//        }
//    }
//}
