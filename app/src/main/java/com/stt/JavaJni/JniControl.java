package com.stt.JavaJni;

public class JniControl {
    public static final int AccOffDelay = 47;
    public static final int AirKeyLightPacket = 39;
    public static final int AudioPacket = 11;
    public static final int BTModulePacket = 40;
    public static final int BTPacket = 28;
    public static final int BackstagePacket = 22;
    public static final int BeepPacket = 0;
    public static final int BenzSourcePacket = 27;
    public static final int CalendarPacket = 3;
    public static final int CanBusUpdatePacket = 52;
    public static final int CanbusPacket = 35;
    public static final int ControlDirect = 7;
    public static final int ControlPacket = 8;
    public static final int DVDState = 46;
    public static final int DimmerPacket = 20;
    public static final int DspEQ32BassPacket = 51;
    public static final int DspEQ32Packet = 50;
    public static final int DvdPacket = 15;
    public static final int DvrPacket = 31;
    public static final int Eq10Packet = 34;
    public static final int EqPacket = 10;
    public static final int ExternDVDPacket = 32;
    public static final int GPIO_CMD_GET = 3;
    public static final int GPIO_CMD_INIT = 1;
    public static final int GPIO_CMD_NONE = 0;
    public static final int GPIO_CMD_SET = 2;
    public static final int GpsArmVoiceFlag = 43;
    public static final int HeadrestPacket = 41;
    public static final int KeyPanelType = 48;
    public static final int LibaryPacket = 17;
    public static final int MediaPlayPacket = 23;
    public static final int MediaTimePacket = 24;
    public static final int PagePacket = 2;
    public static final int PanelBkLightPacket = 25;
    public static final int PicPacket = 5;
    public static final int PowerADPacket = 9;
    public static final int QureyPacket = 19;
    public static final int RDSPacket = 26;
    public static final int RGBLightPacket = 36;
    public static final int RadioInfoPacket = 18;
    public static final int RadioPacket = 16;
    public static final int ScreenTypePacket = 29;
    public static final int SetupPacket = 53;
    public static final int SoundMix = 42;
    public static final int SourcePacket = 6;
    public static final int SteerPacket = 1;
    public static final int SteerVoltagePacket = 30;
    public static final int SubwooferValue = 44;
    public static final int TimePacket = 4;
    public static final int TpmsPacket = 49;
    public static final int TranspondPacket = 33;
    public static final int TvFormatPacket = 38;
    public static final int TvPacket = 14;
    public static final int TvSignalPacket = 13;
    public static final int TvStationPacket = 37;
    public static final int VariablePacket = 54;
    public static final int VolumeBalance = 45;
    public static final int VolumePacket = 12;
    public static final int iPodStatusPacket = 21;

    public static int InitArmMcuCom() {
        return JavaToJni.InitArmMcuCom();
    }

    public static int isArmMcuComOpen() {
        return JavaToJni.isArmMcuComOpen();
    }

    public static void DestroyArmMcuCom() {
        JavaToJni.DestroyArmMcuCom();
    }

    public static int ArmToMcuPacket(int id, int param1, int param2, int param3, byte[] buf, int bufSize) {
        return JavaToJni.ArmToMcuPacket(id, param1, param2, param3, buf, bufSize);
    }

    public static int simulate_key(int keyCode) {
        return JavaToJni.simulate_key(keyCode);
    }

    public static int SetLibInfo(int FirstPoweron, int Reserve1, int Reserve2) {
        return JavaToJni.SetLibInfo(FirstPoweron, Reserve1, Reserve2);
    }

    public static int javaToJniGpio(int cmd, int direction, int value, int group, int index) {
        return JavaToJni.javaToJniGpio(cmd, direction, value, group, index);
    }

    public static int setPowerOnType(int type) {
        return JavaToJni.setPowerOnType(type);
    }

    public static int setTouchPanelEnable(int onOff) {
        return JavaToJni.setTouchPanelEnable(onOff);
    }
}
