package com.laniakea.kit;

import org.apache.commons.lang3.StringUtils;

/**
 * @author luochang
 * @version SystemInfo.java, v 0.1 v 0.1 2019年07月04日 11:00 luochang Exp
 */
public class SystemKit {

    private static String  LOCALHOST;

    private static String  HOSTMACHINE;

    private static boolean IS_WINDOWS;

    private static boolean IS_LINUX;

    private static boolean IS_MAC;

    static {
        boolean[] os = parseOSName();
        IS_WINDOWS = os[0];
        IS_LINUX = os[1];
        IS_MAC = os[2];

        LOCALHOST = NetKit.getLocalIpv4();
        HOSTMACHINE = parseHostMachine();
    }

    static boolean[] parseOSName() {
        boolean[] result = new boolean[] { false, false, false };
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("windows")) {
            result[0] = true;
        } else if (osName.contains("linux")) {
            result[1] = true;
        } else if (osName.contains("mac")) {
            result[2] = true;
        }
        return result;
    }

    public static boolean isWindows() {
        return IS_WINDOWS;
    }

    public static Boolean isLinux() {
        return IS_LINUX;
    }

    public static boolean isMac() {
        return IS_MAC;
    }

    public static int getCpuCores() {
        return Runtime.getRuntime().availableProcessors();
    }

    public static String getLocalHost() {
        return LOCALHOST;
    }

    public static void setLocalHost(String localhost) {
        LOCALHOST = localhost;
    }

    static String parseHostMachine() {
        String hostMachine = System.getProperty("host_machine");
        return StringUtils.isNotEmpty(hostMachine) ? hostMachine : null;
    }

    public static String getHostMachine() {
        return HOSTMACHINE;
    }
}
