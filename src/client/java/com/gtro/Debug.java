package com.gtro;

public class Debug {

    public static boolean isDebug;
    public static void log(String loginfo)
    {
        if(isDebug)
            System.out.println(loginfo);
    }
}
