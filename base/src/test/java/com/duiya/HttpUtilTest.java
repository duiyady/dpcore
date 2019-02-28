package com.duiya;

import com.duiya.model.ResponseModel;
import com.duiya.utils.HttpUtil;

import java.io.IOException;

public class HttpUtilTest {
    public static void main(String[] args) throws IOException {
        String string = HttpUtil.sendPost("http://172.33.12.153:8080/dpcore_master/slave/regist", "flag=WnMLeNwq7scP71evATaKviUXRewu25Xtr4jVAEHgKDuYuEjc63KM2+sOzFD9BJm1frBRqzefpvNr\n" +
                "v5I4JFRz1sncveNySBxOkeEEbSDkLHQZbXzdhqlf8lDQojAe/4YaPRHpiGekQdPyCMFXmWCLxyxt\n" +
                "0WzjfZfQSqqCSfU/H70=QXYnRQSwjSsrgMFg2UkRyaaX4GzrWx/Ty686H/AHD4l3VKrudC1Yfury9TuqN7tTt+gL5Rp76lgg\n" +
                "9cFtzxBEucjkDyqufECNUDQrNwPfxTLPlrGPoDBmBf8/D1cm+TXIOY3KjIXWZKPEumJDrSf/koCP\n" +
                "ZEYHUKxVsbe/lTYWl1s=&pubKeyStr=MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDJKoV70ntbuCBytyuP2N13LQ/DyjigQvC3NNX8\n" +
                "6KbYCZedt202Us9Iob/V61pyRs+eLOT286fuZZ4h+96s1E5KTObCnRT/p45i4Y49MxBPivvwDtiP\n" +
                "XA/4DTRlXpF2977GDmGDxq1XRhoQf6JIgNJkqdDFjT/u4BnC2uv2gVRZdQIDAQAB&baseUrl=http://172.33.12.153:10086/dpcore_slave");
        System.out.println(string);
    }
}
