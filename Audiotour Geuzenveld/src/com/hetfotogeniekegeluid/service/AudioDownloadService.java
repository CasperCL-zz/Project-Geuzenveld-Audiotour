package com.hetfotogeniekegeluid.service;

import com.google.android.vending.expansion.downloader.impl.DownloaderService;
import com.hetfotogeniekegeluid.receiver.AudioDownloadAlarmReceiver;

public class AudioDownloadService extends DownloaderService {
    // You must use the public key belonging to your publisher account
    public static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzsYLDfDcwZBSt6JSu4anGx8OrmwbvcRYUtfwAKaXvsU75xXstNROWqXrezYTA5vDhDiFSZsODbRo/MBvlitN34UqJR7PhZnGsBuh/r4TkPTmP1rC9ovEmpjnKbJmdz26BMGLKEkhHBut1Yc4Q9m198xaiU56e7vjO/bPHiTiECsqQR+2m+rvYgAKUCwOjAC8BeMaBxSY1uduTbUU5/3oa3I/7ksI0oWzO/GJ25IsqG16RAenxE6wKFD5OiND9zVBFj5VkJfX/jJXQkTCoNa58cMSptVIXlG3BKxnnIDgkLFcCBklRVVrFu7RKRP7R/rVv0S4JKhUg0J7VDmEvXwFPwIDAQAB";
    // You should also modify this salt
    public static final byte[] SALT = new byte[] { 5, -35, -72, 23, 59, -12,
            90, -34, 73, -92, -2, -9, 74, 52, 83, -123, -128, -42, 98, 56
    };

    @Override
    public String getPublicKey() {
        return BASE64_PUBLIC_KEY;
    }

    @Override
    public byte[] getSALT() {
        return SALT;
    }

    @Override
    public String getAlarmReceiverClassName() {
        return AudioDownloadAlarmReceiver.class.getName();
    }
}
