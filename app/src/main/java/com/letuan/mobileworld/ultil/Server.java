package com.letuan.mobileworld.ultil;

public class Server {
    //ao tep properties ay
    public static String localhost = "192.168.43.221:8081";
    public static String localhost2 = "localhost:8081";
    public static String localhost3 = "192.168.1.13:8081";
    public static String urlGetAllCategory = "http://" + localhost3 + "/getAllCategory";
    public static String urlGetProductNewest = "http://" + localhost3 + "/getProductNewest/";
    public static String urlPostRegisterAccount = "http://" + localhost3 + "/addUser";
    public static String urlPostLogin = "http://" + localhost3 + "/login";
}
