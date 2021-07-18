package com.rosi.masts.mvc.model.mcu

enum class McuToArmCommands(val value: Byte) {
    InputPacket(-63),       //C1
    StatusPacket(-61),      //C3
    RdsInfoPacket(-60),     //C4
    VolumePacket(-59),      //C5
    AudioPacket(-58),       //C6
    EQPacket(-57),          //C7
    PicPacket(-56),         //C8
    SourcePacket(-55),      //C9
    RadioPacket(-54),       //CA
    VoltagePacket(-52),     //CC
    AckPacket(-50),         //CE
    PasswordPacket(-49),    //CF
    TimePacket(-48),        //D0
    CalendarPacket(-47),    //D1
    VersionPacket(-46),     //D2
    DvdVersionPacket(-45),  //D3
    DvdStatusPacket(-44),   //D4
    TPMSPacket(-42),        //D6
    RdsNamePacket(-41),     //D7
    RdsMessagePacket(-40),  //D8
    TvStationPacket(-36),
    RdsStatusPacket(-35),
    SteerPacket(-34),
    ControlPacket(-33),
    SteerUnautoPacket(-31),
    UsbModePacket(-30),
    AirKeyPacket(-29),
    EQ10Packet(-28),
    EncryptErrorPacket(-27),
    CanbusPacket(-26)       //E6
}