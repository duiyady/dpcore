package com.duiya;

import com.duiya.utils.RSAUtil;

import java.security.Key;

public class FileUploadtest {

    public static void main(String[] args) throws Exception{
        getFlag();
    }

    public static void getFlag() throws Exception{
        String mess = "OmEl+KsmwDP4vevnKw/rjbflyNjQtM9nldC7ts8Shqd57FI0J5AYOhdXHc8FnYrCUxYHyNoVwkwQamVs9zn5DwwiYvY3z/jdBi/Vh0Eau8lw/kBXSqEzyzJU6X6u0XJU8TyMlmJ5ccRIjb8JKOv9z21B0ybOo2dAhoP4P1ntM+g=";
        String mi = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIg/N/Aw5mXPkoPOArX6SYyZVmar\n" +
                "TfxVuSPrep2soxIzdjYTjQ4rlntu0Yd4QJ6Hc7iRx6nwqUn4qUV5jlQRkqjsx9ghC/9Ig9dMJKcu\n" +
                "oVHZ+ldzfggBzvP0kA9EJTKd6g1y1MfSUu5HS8tJy8s7ogjT3U3O1N9m0vPXGzkK30NHAgMBAAEC\n" +
                "gYB40bbFJFfriLqgbxswTJbUO1XHDbnGPi5DkBnXoLOq/D+jjIrVOgnjw03DF/wVmIAh+ARayrMU\n" +
                "iZFxrm1sV3j5TY8dUH1BhEuFPdKXtbf0OYwZjVVglNldYCdWDw+5NELe5eAsPDUk2LrE5+qm2Tj2\n" +
                "q1lD3O1T7iRYbaPfzP4twQJBANTYKYhBjlKcfXEm9KH4inMwzASx95uWfm3s4l+I+wG2aPyry13c\n" +
                "NxDIpVkwV2cMrQkGx4ZrYJj1WQGHN7kcqYkCQQCj3zZcM78rKgkh8nwDrvR3eNBolP5LN94p/RkG\n" +
                "WDP/hmOrdujpqlrk0/awYnCWwYO8P7yT7QY7uYaHAnDHfOJPAkEA0n44jL0wcHi9AiNhebGf0o2m\n" +
                "ptoOIzvxKutDcPG6MxZfu8p2ZAu+FYYpKAxL0+jQU4X12vTkYkD51i4QqKw06QJAKPE97a9oFeq8\n" +
                "uWSSI4F0xtzChT6kgjqFIUyBEhq7cx4qjM56xtavOvjSa6NMqf3TYhcykGrp4cYqnFS5xL2o9QJA\n" +
                "U+gOimy4vuC0xJBugcFBLTEbUlMHi4O6UH2pFICao9LcmOTykSfZZLtCnIqPMLFPPMRCNzfnR78w\n" +
                "QxnptTCgjA==";
        Key pr = RSAUtil.getPrivateKey(mi);
        String mmm = RSAUtil.decrypt(mess, pr);
        System.out.println(mmm);


    }
}
