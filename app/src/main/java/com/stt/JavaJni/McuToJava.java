//package com.stt.JavaJni;
//
//import android.annotation.SuppressLint;
//import android.app.ActivityManager;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//
//import com.stt.cbmlib.SourceID;
//import com.stt.control.ControlApp;
//import com.stt.control.MainThreadHandler;
//import com.stt.control.StaticConstant;
//import com.stt.data.info.SttGlobalInfo;
//import com.stt.data.status.SttStatus;
//import com.stt.fuc.BroadCastFuc;
//import com.stt.handle.KeyEventHandle;
//import com.stt.handle.WheelEventHandle;
//import com.stt.log.SttLog;
//import com.stt.manager.PoweronStep;
//import com.stt.manager.Source;
//import com.stt.module.Define;
//import com.stt.module.ModuleManager;
//import com.stt.module.canbus.CanbusDefine;
//import com.stt.module.canbus.CanbusLogic;
//import com.stt.module.steersetup.Steer;
//import com.stt.module.tpms.TPMSDefine;
//import com.stt.source.poweroff.PoweroffLogic;
//import com.stt.source.radio.Radio2ApkConn;
//import com.stt.source.tv.TvData;
//import com.stt.status.McuStatusManager;
//import com.stt.status.StatusManager;
//import com.stt.time.McuTime;
//import com.stt.tool.SystemServiceTool;
//import com.stt.tool.tool;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.UnsupportedEncodingException;
//import java.util.Arrays;
//
//@SuppressLint({"NewApi"})
//public class McuToJava {
//    public static final int CANBUS_DATA = 1;
//    public static final byte MCU2ARM_ACK_HEAD = -50;
//    public static final byte MCU2ARM_AUDIO_HEAD = -58;
//    public static final byte MCU2ARM_CANBUS_HEAD = -26;
//    public static final byte MCU2ARM_CANDENAR_HEAD = -47;
//    public static final byte MCU2ARM_CHECK_HEAD = -38;
//    public static final byte MCU2ARM_CONTROL_HEAD = -33;
//    public static final byte MCU2ARM_DVD_STATUS_HEAD = -44;
//    public static final byte MCU2ARM_DVD_VERSION_HEAD = -45;
//    public static final byte MCU2ARM_EQ_10_HEAD = -28;
//    public static final byte MCU2ARM_EQ_HEAD = -57;
//    public static final int MCU2ARM_EXTERN_DVD_HEAD = -29;
//    public static final byte MCU2ARM_INPUT_HEAD = -63;
//    public static final byte MCU2ARM_LANGUAGE_HEAD = -37;
//    public static final byte MCU2ARM_MCU_ENCRYPT_ERROR_HEAD = -27;
//    public static final byte MCU2ARM_MCU_VERSION_CNT_16 = 16;
//    public static final byte MCU2ARM_MCU_VERSION_CNT_NEW = 14;
//    public static final byte MCU2ARM_MCU_VERSION_CNT_OLD = 7;
//    public static final byte MCU2ARM_MCU_VERSION_HEAD = -46;
//    public static final byte MCU2ARM_PASSWORD_HEAD = -49;
//    public static final byte MCU2ARM_PIC_HEAD = -56;
//    public static final byte MCU2ARM_PINGPU_HEAD = -62;
//    public static final byte MCU2ARM_RADIO_HEAD = -54;
//    public static final byte MCU2ARM_RDS_HEAD = -60;
//    public static final byte MCU2ARM_RDS_INFO_HEAD = -60;
//    public static final byte MCU2ARM_RDS_MESSAGE_HEAD = -40;
//    public static final byte MCU2ARM_RDS_PSNAME_CNT = 12;
//    public static final byte MCU2ARM_RDS_PSNAME_HEAD = -41;
//    public static final byte MCU2ARM_RDS_STATUS_HEAD = -35;
//    public static final byte MCU2ARM_SENSOR_HEAD = -32;
//    public static final byte MCU2ARM_SOURCE_HEAD = -55;
//    public static final byte MCU2ARM_STATUS_HEAD = -61;
//    public static final byte MCU2ARM_STEER_STATUS_HEAD = -34;
//    public static final byte MCU2ARM_STEER_UNAUTO_HEAD = -31;
//    public static final byte MCU2ARM_TIME_HEAD = -48;
//    public static final byte MCU2ARM_TPMS_HEAD = -42;
//    public static final byte MCU2ARM_TV_STATION_HEAD = -36;
//    public static final int MCU2ARM_USB_MODE_HEAD = -30;
//    public static final int MCU2ARM_VOLTAGE_VALUE_HEAD = -52;
//    public static final byte MCU2ARM_VOLUME_HEAD = -59;
//    public static final byte RADIO_MONO_FLAG = 2;
//    public static final byte RADIO_SIGNAL_FLAG = 1;
//    private static String TAG = "McuToJava:";
//    public static final int TPMS_DATA = 2;
//    private static int mBeforePowerKey;
//    private static int mCardPowerOn;
//    private static int mDiscPowerOn;
//    private static int mFirstPowerOn = 0;
//    private static int mIpodPowerOn;
//    private static int mQureyMemoryTick = 0;
//    private static int mRUsbPowerOn;
//    private static int mUsbPowerOn;
//    private static int mtmpDay = 0;
//    private static int mtmpHour = 0;
//    private static int mtmpMinute = 0;
//    private static int mtmpMonth = 0;
//    private static int mtmpSecond = 0;
//    private static int mtmpYear = 0;
//
//    public static void setFirstPowerOn(int first) {
//        mFirstPowerOn = first;
//        StatusManager.set(SttStatus.firstPowerOn, first);
//    }
//
//    public static int getFirstPowerOn() {
//        if (mFirstPowerOn > 0) {
//            return 1;
//        }
//        return -1;
//    }
//
//    public static int getDiscPowerOn() {
//        if (mDiscPowerOn > 0) {
//            return 1;
//        }
//        return -1;
//    }
//
//    public static int getUsbPowerOn() {
//        if (mUsbPowerOn > 0) {
//            return 1;
//        }
//        return -1;
//    }
//
//    public static int getRUsbPowerOn() {
//        if (mRUsbPowerOn > 0) {
//            return 1;
//        }
//        return -1;
//    }
//
//    public static int getCardPowerOn() {
//        if (mCardPowerOn > 0) {
//            return 1;
//        }
//        return -1;
//    }
//
//    public static int getIpodPowerOn() {
//        if (mIpodPowerOn > 0) {
//            return 1;
//        }
//        return -1;
//    }
//
//    public static int getBeforePowerKey() {
//        if (mBeforePowerKey > 0) {
//            return 1;
//        }
//        return -1;
//    }
//
//    public static void init() {
//    }
//
//    public static void clearPowerFlag() {
//        mFirstPowerOn = -1;
//        mDiscPowerOn = -1;
//        mUsbPowerOn = -1;
//        mRUsbPowerOn = -1;
//        mCardPowerOn = -1;
//        mIpodPowerOn = -1;
//        mBeforePowerKey = -1;
//        McuStatusManager.setIsReversed(-1);
//    }
//
//    @SuppressLint({"NewApi"})
//    public static void McuToJavaTimer() {
//        if (McuStatusManager.getAccInfo() >= 0 && Source.getCurSource() != SourceID.SOURCE_POWEROFF) {
//            ArmToMcuQureyPacket();
//            int i = mQureyMemoryTick;
//            mQureyMemoryTick = i + 1;
//            if (i > 10) {
//                mQureyMemoryTick = 0;
//                ActivityManager.MemoryInfo memoryInfo = SystemServiceTool.getMemoryInfo();
//                if (memoryInfo != null) {
//                    SttLog.i(TAG, "Memory info :availMem = " + memoryInfo.availMem + ", totalMem = " + memoryInfo.totalMem + ", threshold = " + memoryInfo.threshold + ", lowMemory = " + memoryInfo.lowMemory);
//                }
//            }
//        }
//    }
//
//    public static void onReadRxData(byte[] dataBuf, int len) {
//        switch (dataBuf[0]) {
//            case -63:
//                mcuToArmInputPacket(dataBuf, len);
//                return;
//            case -62:
//            case -53:
//            case -51:
//            case -43:
//            case -39:
//            case -38:
//            case -37:
//            case -32:
//            default:
//                return;
//            case -61:
//                mcuToArmMcuStatusPacket(dataBuf, len);
//                return;
//            case -60:
//                mcuToArmRdsInfoPacket(dataBuf, len);
//                return;
//            case -59:
//                mcuToArmVolumePacket(dataBuf, len);
//                return;
//            case -58:
//                mcuToArmAudioPacket(dataBuf, len);
//                return;
//            case -57:
//                mcuToArmEQPacket(dataBuf, len);
//                return;
//            case -56:
//                mcuToArmPicPacket(dataBuf, len);
//                return;
//            case -55:
//                mcuToArmSourcePacket(dataBuf, len);
//                return;
//            case -54:
//                mcuToArmRadioPacket(dataBuf, len);
//                return;
//            case MCU2ARM_VOLTAGE_VALUE_HEAD /*{ENCODED_INT: -52}*/:
//                mcuToArmVoltagePacket(dataBuf, len);
//                return;
//            case -50:
//                mcuToArmAckPacket(dataBuf, len);
//                return;
//            case -49:
//                mcuToArmPasswordPacket(dataBuf, len);
//                return;
//            case -48:
//                mcuToArmTimePacket(dataBuf, len);
//                return;
//            case -47:
//                mcuToArmCalendarPacket(dataBuf, len);
//                return;
//            case -46:
//                mcuToArmMcuVersionPacket(dataBuf, len);
//                return;
//            case -45:
//                mcuToArmDvdVersionPacket(dataBuf, len);
//                return;
//            case -44:
//                mcuToArmDvdStatusPacket(dataBuf, len);
//                return;
//            case -42:
//                mcuToArmTPMSPacket(dataBuf, len);
//                return;
//            case -41:
//                mcuToArmRdsNamePacket(dataBuf, len);
//                return;
//            case -40:
//                mcuToArmRdsMessagePacket(dataBuf, len);
//                return;
//            case -36:
//                mcuToArmTvStationPacket(dataBuf, len);
//                return;
//            case -35:
//                mcuToArmRdsStatusPacket(dataBuf, len);
//                return;
//            case -34:
//                mcuToArmSteerPacket(dataBuf, len);
//                return;
//            case -33:
//                mcuToArmControlPacket(dataBuf, len);
//                return;
//            case -31:
//                mcuToArmSteerUnautoPacket(dataBuf, len);
//                return;
//            case MCU2ARM_USB_MODE_HEAD /*{ENCODED_INT: -30}*/:
//                mcuToArmUsbModePacket(dataBuf, len);
//                return;
//            case MCU2ARM_EXTERN_DVD_HEAD /*{ENCODED_INT: -29}*/:
//                if ((dataBuf[2] & 255) >= 208 && (dataBuf[2] & 255) < 236) {
//                    mcuToArmAirKeyPacket(dataBuf, len);
//                    return;
//                }
//                return;
//            case -28:
//                mcuToArmEQ10Packet(dataBuf, len);
//                return;
//            case -27:
//                mcuToArmMcuEncryptErrorPacket(dataBuf, len);
//                return;
//            case -26:
//                mcuToArmCanbusPacket(dataBuf, len);
//                return;
//        }
//    }
//
//    public static int ArmToMcuQureyPacket() {
//        return ArmToMcu.sendQureyMcuStatePacket(Source.getCurSource().ordinal(), Source.getCurSource().ordinal(), Source.getCurSource().ordinal());
//    }
//
//    private static int mcuToArmInputPacket(byte[] buf, int size) {
//        SttLog.dHex("receive input data:", buf, size);
//        if (PoweronStep.getPoweronStep() < StaticConstant.POWER_ON_STEP.ARM_POWER_ON_STEP4.ordinal()) {
//            if (1 == buf[2]) {
//                mBeforePowerKey = 1;
//                SttLog.i(TAG, "Before power key");
//                SttLog.i(TAG, "\r\n--> McuToArmInputPacket m_bBeforePowerKey !");
//            } else if (24 == buf[2]) {
//                mDiscPowerOn = 1;
//                mUsbPowerOn = -1;
//                mRUsbPowerOn = -1;
//                mCardPowerOn = -1;
//                mIpodPowerOn = -1;
//                SttLog.i(TAG, "Before disc power on");
//                SttLog.i(TAG, "\r\n--> McuToArmInputPacket disc power on !");
//            } else if (25 == buf[2]) {
//                mDiscPowerOn = -1;
//                mUsbPowerOn = 1;
//                mRUsbPowerOn = -1;
//                mCardPowerOn = -1;
//                mIpodPowerOn = -1;
//                SttLog.i(TAG, "Before usb power on");
//                SttLog.i(TAG, "\r\n--> McuToArmInputPacket usb power on !");
//            } else if (81 == buf[2]) {
//                mDiscPowerOn = -1;
//                mUsbPowerOn = -1;
//                mRUsbPowerOn = 1;
//                mCardPowerOn = -1;
//                mIpodPowerOn = -1;
//                SttLog.i(TAG, "Before rusb power on");
//                SttLog.i(TAG, "\r\n--> McuToArmInputPacket Rusb power on !");
//            } else if (26 == buf[2]) {
//                mDiscPowerOn = -1;
//                mUsbPowerOn = -1;
//                mRUsbPowerOn = -1;
//                mCardPowerOn = 1;
//                mIpodPowerOn = -1;
//                SttLog.i(TAG, "Before card power on");
//                SttLog.i(TAG, "\r\n--> McuToArmInputPacket card power on !");
//            } else if (129 == buf[2]) {
//                mDiscPowerOn = -1;
//                mUsbPowerOn = -1;
//                mRUsbPowerOn = -1;
//                mCardPowerOn = -1;
//                mIpodPowerOn = 1;
//                SttLog.i(TAG, "Before ipod power on");
//                SttLog.i(TAG, "\r\n--> McuToArmInputPacket ipod power on !");
//            }
//        } else if (buf[2] != 0) {
//            SttLog.i(TAG, String.valueOf("key input ") + String.valueOf((int) buf[2]));
//            KeyEventHandle.sendKeyEvent(buf[2] & 255);
//            Bundle canBundle = new Bundle();
//            canBundle.putInt("KeyToCan", buf[2] & 255);
//            ModuleManager.NotifyModule(false, "canbus", 2, canBundle);
//        } else {
//            WheelEventHandle.sendSteerValue(((buf[4] & 255) << 8) + (buf[3] & 255));
//        }
//        return size;
//    }
//
//    private static int mcuToArmMcuStatusPacket(byte[] buf, int size) {
//        McuStatusManager.onMcuStatus(((buf[3] & 255) << 8) | (buf[2] & 255), buf[4] & 255);
//        return size;
//    }
//
//    private static int mcuToArmVolumePacket(byte[] buf, int size) {
//        SttLog.dHex("receive mcu volume data:", buf, size);
//        return size;
//    }
//
//    private static int mcuToArmAudioPacket(byte[] buf, int size) {
//        SttLog.dHex("receive mcu audio data:", buf, size);
//        return size;
//    }
//
//    private static int mcuToArmEQPacket(byte[] buf, int size) {
//        SttLog.dHex("receive mcu eq data:", buf, size);
//        return size;
//    }
//
//    private static int mcuToArmPicPacket(byte[] buf, int size) {
//        SttLog.dHex("receive mcu pic data:", buf, size);
//        return size;
//    }
//
//    private static int mcuToArmSteerPacket(byte[] buf, int size) {
//        Bundle bundle = new Bundle();
//        SttLog.dHex("receive mcu Steer ad data:", buf, size);
//        int whickKey = buf[6] & 255 & 15;
//        int highKey = (((buf[5] & 255) & 240) << 4) | (buf[2] & 255);
//        int midKey = (buf[3] & 255) | (((buf[5] & 255) & 15) << 8);
//        bundle.putInt("whickKey", whickKey);
//        bundle.putInt("highAdValue", highKey);
//        bundle.putInt("midAdValue", midKey);
//        bundle.putInt("lowAdValue", (buf[4] & 255) | (((buf[6] & 255) & 240) << 4));
//        ModuleManager.NotifyModule(true, Define.STEER_SETUP, 1, bundle);
//        return size;
//    }
//
//    private static int mcuToArmSourcePacket(byte[] buf, int size) {
//        SttLog.dHex("receive mcu source data:", buf, size);
//        PoweronStep.getPoweronStep();
//        StaticConstant.POWER_ON_STEP.ARM_POWER_ON_STEP_END.ordinal();
//        return size;
//    }
//
//    private static int mcuToArmRadioPacket(byte[] buf, int size) {
//        SttLog.dHex("receive mcu radio data:", buf, size);
//        if (buf[1] != size) {
//            return 1;
//        }
//        int hi = (buf[4] & 255) | ((buf[5] & 255) << 8);
//        int lo = (buf[3] & 255) | ((buf[2] & 255) << 8);
//        SttLog.v("mcuToArmRadioPacket", "freq = " + (lo & 65535));
//        Radio2ApkConn.getInstance().sendRadioSignal(((hi & 65535) << 16) + (lo & 65535));
//        return size;
//    }
//
//    private static int mcuToArmAckPacket(byte[] buf, int size) {
//        if (PoweronStep.getPoweronStep() == StaticConstant.POWER_ON_STEP.ARM_POWER_ON_STEP0.ordinal()) {
//            PoweronStep.setPoweronStep(0, StaticConstant.POWER_ON_STEP.ARM_POWER_ON_STEP1);
//            if ((buf[3] & 255 & 1) > 0) {
//                setFirstPowerOn(1);
//                SttLog.i(TAG, "\r\n--> McuToArmAckPacket m_bFirstPowerOn !");
//                SttLog.i(TAG, "First Power On");
//            }
//            if ((buf[3] & 255 & 2) > 0) {
//                McuStatusManager.setIsReversed(1);
//                SttLog.i(TAG, "\r\n--> McuToArmAckPacket m_bIsReversed !");
//                SttLog.i(TAG, "Reversed");
//            }
//        }
//        return size;
//    }
//
//    private static int mcuToArmPasswordPacket(byte[] buf, int size) {
//        SttLog.dHex("receive mcu password data:", buf, size);
//        return size;
//    }
//
//    private static int mcuToArmTimePacket(byte[] buf, int size) {
//        byte b = buf[2];
//        byte b2 = buf[3];
//        byte b3 = buf[4];
//        byte b4 = buf[5];
//        byte b5 = buf[6];
//        byte b6 = buf[7];
//        if (!(mtmpHour == b4 && mtmpMinute == b5 && mtmpSecond == b6 && mtmpYear == b && mtmpMonth == b2 && mtmpDay == b3)) {
//            mtmpYear = (b + MCU2ARM_TIME_HEAD) - 1900;
//            mtmpMonth = b2;
//            mtmpDay = b3;
//            mtmpHour = b4;
//            mtmpMinute = b5;
//            mtmpSecond = b6;
//            new Handler(Looper.getMainLooper()).post(new Runnable() {
//                /* class com.stt.JavaJni.McuToJava.AnonymousClass1 */
//
//                public void run() {
//                    if (!McuTime.mNeedSendTimeToMcu) {
//                        McuTime.setSystemDateTime(McuToJava.mtmpYear, McuToJava.mtmpMonth, McuToJava.mtmpDay, McuToJava.mtmpHour, McuToJava.mtmpMinute, McuToJava.mtmpSecond);
//                    }
//                }
//            });
//        }
//        return size;
//    }
//
//    private static int mcuToArmCalendarPacket(byte[] buf, int size) {
//        int year = buf[2] + MCU2ARM_TIME_HEAD;
//        byte b = buf[3];
//        byte b2 = buf[4];
//        if (!(mtmpYear == year && mtmpMonth == b && mtmpDay == b2)) {
//            mtmpYear = year;
//            mtmpMonth = b;
//            mtmpDay = b2;
//            new Handler(Looper.getMainLooper()).post(new Runnable() {
//                /* class com.stt.JavaJni.McuToJava.AnonymousClass2 */
//
//                public void run() {
//                    boolean z = McuTime.mNeedSendTimeToMcu;
//                }
//            });
//        }
//        return size;
//    }
//
//    private static int mcuToArmMcuVersionPacket(byte[] buf, int size) {
//        SttLog.dHex("receive mcu version data:", buf, size);
//        int ret = 0;
//        String mcuVersion = "";
//        byte[] versionBuf = new byte[16];
//        Arrays.fill(versionBuf, (byte) 0);
//        if (buf[1] == 7) {
//            versionBuf[0] = buf[2];
//            versionBuf[1] = buf[3];
//            versionBuf[2] = buf[4];
//            versionBuf[3] = buf[5];
//            try {
//                mcuVersion = new String(versionBuf, "utf-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            MainThreadHandler.sendMsg(43);
//            ret = 7;
//        } else if (buf[1] == 14) {
//            for (int i = 0; i < 11; i++) {
//                versionBuf[i] = buf[i + 2];
//            }
//            try {
//                mcuVersion = new String(versionBuf, "utf-8");
//            } catch (UnsupportedEncodingException e2) {
//                e2.printStackTrace();
//            }
//            MainThreadHandler.sendMsg(43);
//            ret = 14;
//        } else if (buf[1] == 16) {
//            for (int i2 = 0; i2 < 13; i2++) {
//                versionBuf[i2] = buf[i2 + 2];
//            }
//            try {
//                mcuVersion = new String(versionBuf, "utf-8");
//            } catch (UnsupportedEncodingException e3) {
//                e3.printStackTrace();
//            }
//            ret = 16;
//        }
//        if (mcuVersion != null && !mcuVersion.isEmpty()) {
//            SttGlobalInfo.set(ControlApp.getAppContext().getContentResolver(), SttGlobalInfo.McuVersion, mcuVersion);
//        }
//        return ret;
//    }
//
//    private static int mcuToArmDvdVersionPacket(byte[] buf, int size) {
//        byte[] versionBuf = new byte[16];
//        SttLog.dHex("receive mcu dvd version data:", buf, size);
//        Arrays.fill(versionBuf, (byte) 0);
//        versionBuf[0] = buf[2];
//        versionBuf[1] = buf[3];
//        versionBuf[2] = buf[4];
//        versionBuf[3] = buf[5];
//        return size;
//    }
//
//    private static int mcuToArmDvdStatusPacket(byte[] buf, int size) {
//        SttLog.dHex("receive mcu dvd status data:", buf, size);
//        return size;
//    }
//
//    private static int mcuToArmRdsNamePacket(byte[] buf, int size) {
//        byte[] psInfoBuf = new byte[10];
//        SttLog.dHex("receive mcu rds name data:", buf, size);
//        Arrays.fill(psInfoBuf, (byte) 0);
//        for (int i = 0; i < 8; i++) {
//            psInfoBuf[i] = buf[i + 2];
//        }
//        Radio2ApkConn.getInstance().sendRDSName(tool.Utf8ToString(psInfoBuf));
//        return size;
//    }
//
//    private static int mcuToArmRdsMessagePacket(byte[] buf, int size) {
//        return size;
//    }
//
//    private static int mcuToArmRdsStatusPacket(byte[] buf, int size) {
//        byte[] rdsInfoBuf = new byte[10];
//        SttLog.dHex("receive mcu rds status data:", buf, size);
//        Arrays.fill(rdsInfoBuf, (byte) 0);
//        for (int i = 0; i < 8; i++) {
//            rdsInfoBuf[i] = buf[i + 2];
//        }
//        Radio2ApkConn.getInstance().sendRDSStatus(tool.Utf8ToString(rdsInfoBuf));
//        return size;
//    }
//
//    private static int mcuToArmRdsInfoPacket(byte[] buf, int size) {
//        SttLog.dHex("receive mcu rds info data:", buf, size);
//        Radio2ApkConn.getInstance().sendRDSInfo(((buf[3] & 255) << 8) | (buf[2] & 255));
//        return size;
//    }
//
//    private static int mcuToArmControlPacket(byte[] buf, int size) {
//        SttLog.dHex("receive mcu control data:", buf, size);
//        if (buf[3] == 9) {
//            PoweroffLogic.onMcuControlSleep();
//        }
//        return size;
//    }
//
//    private static int mcuToArmVoltagePacket(byte[] buf, int size) {
//        SttLog.dHex("receive mcu voltage data:", buf, size);
//        int dwHigh = (buf[2] << 24) + (buf[3] << MCU2ARM_MCU_VERSION_CNT_16) + (buf[4] << 8) + buf[5];
//        int dwLow = (buf[6] << 24) + (buf[7] << MCU2ARM_MCU_VERSION_CNT_16) + (buf[8] << 8) + buf[9];
//        Steer.getInstance().setSteerBaseVoltageH(dwHigh);
//        Steer.getInstance().setSteerBaseVoltageL(dwLow);
//        return size;
//    }
//
//    private static int mcuToArmUsbModePacket(byte[] buf, int size) {
//        SttLog.dHex("receive mcu usb mode data:", buf, size);
//        return size;
//    }
//
//    private static int mcuToArmSteerUnautoPacket(byte[] buf, int size) {
//        SttLog.dHex("receive mcu steer unauto data:", buf, size);
//        return size;
//    }
//
//    private static int mcuToArmEQ10Packet(byte[] buf, int size) {
//        return size;
//    }
//
//    private static int mcuToArmCanbusPacket(byte[] buf, int size) {
//        if (!(buf == null || size == 0)) {
//            SttLog.dHex(TAG, buf, size);
//            if (buf[2] == 1) {
//                Intent intent = new Intent(CanbusDefine.ACTION_CANBUS_READ_DATA);
//                intent.putExtra("Canbusdata", buf);
//                intent.putExtra("CanbusLen", size);
//                BroadCastFuc.sendBroadcast(intent);
//            }
//        }
//        return size;
//    }
//
//    private static int mcuToArmTPMSPacket(byte[] buf, int size) {
//        if (!(buf == null || size == 0)) {
//            SttLog.dHex(TAG, buf, size);
//            if (buf[2] == 2) {
//                byte[] data = new byte[size];
//                byte b = buf[5];
//                System.arraycopy(buf, 3, data, 0, b);
//                Intent intent = new Intent(TPMSDefine.ACTION_TPMS_READ_DATA);
//                intent.putExtra("data", data);
//                intent.putExtra("len", (int) b);
//                BroadCastFuc.sendBroadcast(intent);
//            }
//        }
//        return size;
//    }
//
//    private static int mcuToArmAirKeyPacket(byte[] buf, int size) {
//        if (!(buf == null || size == 0)) {
//            SttLog.dHex(TAG, buf, size);
//            if (buf[1] == 4) {
//                CanbusLogic.onSendAirKeyToCanApk(buf);
//            }
//        }
//        return size;
//    }
//
//    private static int mcuToArmTvStationPacket(byte[] buf, int size) {
//        SttLog.dHex("receive mcu tv station data:", buf, size);
//        int nBuf2 = buf[2] & 255;
//        int nBuf3 = buf[3] & 255;
//        int nBuf4 = buf[4] & 255;
//        int nBuf5 = buf[5] & 255;
//        int nBuf6 = buf[6] & 255;
//        if (buf[1] != size) {
//            return 1;
//        }
//        int uStatus = nBuf2 & 7;
//        int uPN = (nBuf2 & 120) >> 3;
//        int uFlag = nBuf2 >> 7;
//        TvData.setTvFormat(uPN);
//        switch (uStatus) {
//            case 0:
//                TvData.setTvStationTotal(nBuf3);
//                if (nBuf4 != 255) {
//                    TvData.setTvStationFreq(nBuf4, (nBuf5 << 8) | nBuf6);
//                    TvData.setTvStationFormat(nBuf4, uPN);
//                    return size;
//                }
//                TvData.saveTvData();
//                return size;
//            case 1:
//                JniControl.ArmToMcuPacket(37, TvData.getTvStationTotal(), nBuf4, TvData.getTvStationFreq(nBuf4), null, TvData.getTvStationFormat(nBuf4));
//                return size;
//            case 2:
//                TvData.setTvStationFormat(nBuf4, uPN);
//                TvData.saveTvData();
//                return size;
//            case 3:
//                JniControl.ArmToMcuPacket(38, TvData.getTvFormat(), TvData.getCurFreqIndex(), 0, null, 0);
//                return size;
//            case 4:
//                TvData.setCurFreqIndex(nBuf3);
//                TvData.setTvBand(nBuf4);
//                Bundle bundle_freq = new Bundle();
//                boolean isScan = false;
//                if (uFlag > 0) {
//                    isScan = true;
//                }
//                bundle_freq.putBoolean("tvScanFlag", ((nBuf2 >> 7) & 1) == 1);
//                bundle_freq.putBoolean("tvScan", isScan);
//                bundle_freq.putInt("tvFreq", (nBuf5 << 8) | nBuf6);
//                TvData.getObjects().sendTvData(bundle_freq);
//                return size;
//            case 5:
//            case 6:
//            default:
//                return size;
//        }
//    }
//
//    private static int mcuToArmMcuEncryptErrorPacket(byte[] buf, int size) {
//        if (!(buf == null || size == 0 || buf.length <= 2)) {
//            SttLog.dHex(TAG, buf, size);
//            final int code = buf[2] & 255;
//            new Handler(MainThreadHandler.getMainLooper()).postDelayed(new Runnable() {
//                /* class com.stt.JavaJni.McuToJava.AnonymousClass3 */
//
//                public void run() {
//                    try {
//                        File saveFile = new File(com.stt.manager.Define.mMcuErrorFilePath);
//                        if (!saveFile.exists()) {
//                            File dirFile = new File(com.stt.manager.Define.mMcuErrorFilePath.substring(0, com.stt.manager.Define.mMcuErrorFilePath.lastIndexOf("/")));
//                            if (!dirFile.exists()) {
//                                dirFile.mkdirs();
//                            }
//                            saveFile.createNewFile();
//                        }
//                        FileOutputStream fos = new FileOutputStream(saveFile, true);
//                        fos.write((String.valueOf(Integer.toHexString(code)) + "\r\n").getBytes());
//                        fos.flush();
//                        fos.close();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, 2000);
//        }
//        return size;
//    }
//}
