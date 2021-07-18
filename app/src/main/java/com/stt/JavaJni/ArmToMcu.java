//package com.stt.JavaJni;
//
//import android.util.DisplayMetrics;
//
//import com.stt.cbmlib.SourceID;
//import com.stt.control.MainThreadHandler;
//import com.stt.data.settings.SttSettings;
//import com.stt.log.SttLog;
//import com.stt.manager.PoweronStep;
//import com.stt.manager.Source;
//import com.stt.projects.ProjectHandle;
//import com.stt.settings.Set;
//import com.stt.tool.SystemServiceTool;
//
//public class ArmToMcu {
//    private static int mFuncRds = -1;
//
//    public static int sendControlPacket(int cmd) {
//        return JniControl.ArmToMcuPacket(8, PoweronStep.getArmStartupStatus(), cmd, 0, null, 0);
//    }
//
//    public static void clearRdsFlag() {
//        mFuncRds = -1;
//    }
//
//    public static int sendRadioPacket(int freq, int status, int nStation, int nband) {
//        byte[] buf = new byte[4];
//        buf[0] = (byte) status;
//        buf[1] = (byte) nStation;
//        buf[2] = (byte) nband;
//        if (mFuncRds == -1) {
//            mFuncRds = Set.get(SttSettings.FuncRds, -1);
//        }
//        if (mFuncRds != -1) {
//            buf[3] = (byte) mFuncRds;
//        } else {
//            buf[3] = 0;
//        }
//        return JniControl.ArmToMcuPacket(16, freq, 0, 0, buf, 4);
//    }
//
//    public static int sendRDSPacket(int key, int status, int RdsType, int RdsStatus) {
//        return JniControl.ArmToMcuPacket(26, 0, 0, 0, new byte[]{(byte) key, (byte) status, (byte) RdsType, (byte) RdsStatus}, 4);
//    }
//
//    public static int sendBeepPacket(int data) {
//        return JniControl.ArmToMcuPacket(0, data, 0, 0, null, 0);
//    }
//
//    public static int sendDatePacket(int year, int month, int day) {
//        return JniControl.ArmToMcuPacket(3, year, month, day, null, 0);
//    }
//
//    public static int sendTimePacket(int hour, int minute, int second) {
//        return JniControl.ArmToMcuPacket(4, hour, minute, second, null, 0);
//    }
//
//    public static int sendSourcePacket(int source, int audioSource, int videoSource) {
//        if (ProjectHandle.getCurProject().isOnlyBtMusicAsSource() && source == SourceID.SOURCE_DIAL.ordinal() && Source.getBackgroundSource() != SourceID.SOURCE_DIAL) {
//            source = SourceID.SOURCE_TRANSITION.ordinal();
//        }
//        return JniControl.ArmToMcuPacket(6, source, audioSource, videoSource, null, 0);
//    }
//
//    public static int sendPowerADPacket(int adHigh, int adLow, int whichKey) {
//        return JniControl.ArmToMcuPacket(9, adHigh, adLow, whichKey, null, 0);
//    }
//
//    public static int sendVolumePacket(int volSource, int volume, int lound) {
//        return JniControl.ArmToMcuPacket(12, volSource, volume, lound, null, 0);
//    }
//
//    public static int sendQureyMcuStatePacket(int source, int volSource, int videoSource) {
//        return JniControl.ArmToMcuPacket(19, source, volSource, videoSource, null, 0);
//    }
//
//    public static int sendTvTouchDataPacket(int tvType, int status, int touchData) {
//        return JniControl.ArmToMcuPacket(14, tvType, status, 0, null, touchData);
//    }
//
//    public static int sendTvBtnDataPacket(int tvType, int status, int id) {
//        return JniControl.ArmToMcuPacket(14, tvType, status, 0, null, id);
//    }
//
//    public static int sendTvModePacket(int type) {
//        return JniControl.ArmToMcuPacket(14, type, 0, 0, null, 0);
//    }
//
//    public static int sendBtModePacket(int type) {
//        return JniControl.ArmToMcuPacket(40, type, 0, 0, null, 0);
//    }
//
//    public static int sendDspEQ32Packet(byte[] datas) {
//        if (datas == null || datas.length <= 0) {
//            return -1;
//        }
//        return JniControl.ArmToMcuPacket(50, 0, 0, 0, datas, datas.length);
//    }
//
//    public static int sendDspEQ32BassPacket(byte[] datas) {
//        if (datas == null || datas.length <= 0) {
//            return -1;
//        }
//        return JniControl.ArmToMcuPacket(51, 0, 0, 0, datas, datas.length);
//    }
//
//    public static int sendResolutionPacket(int count) {
//        DisplayMetrics dm = SystemServiceTool.getDisplayMetrics();
//        if (dm != null) {
//            MainThreadHandler.removeMsg(40);
//            if (dm.widthPixels == 1024 && dm.heightPixels == 600) {
//                return sendControlPacket(116);
//            }
//            if (dm.widthPixels == 800 && dm.heightPixels == 480) {
//                return sendControlPacket(115);
//            }
//            SttLog.e("ArmToMcu", "sendResolutionPacket error widthPixels: " + dm.widthPixels + ", heightPixels: " + dm.heightPixels);
//            return 0;
//        } else if (count > 0) {
//            MainThreadHandler.postMsgRemoveOld(40, count - 1, 0, null, 5000);
//            return 0;
//        } else {
//            SttLog.e("ArmToMcu", "sendResolutionPacket error try count full");
//            return 0;
//        }
//    }
//}
