package com.zbsg.common.tools;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.DecimalFormat;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class MacUtil {
	private static Logger log = Logger.getLogger(MacUtil.class);

    public MacUtil() {
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    public static String getMacAddress(HttpServletRequest request) {
        String ip = getIpAddress(request);
        log.info("---Ip:" + ip);
        String smac = "";
        if(!"127.0.0.1".equals(ip)) {
            try {
                Process e = Runtime.getRuntime().exec("nbtstat -a " + ip);
                InputStreamReader ir = new InputStreamReader(e.getInputStream());
                LineNumberReader input = new LineNumberReader(ir);

                String line;
                while((line = input.readLine()) != null) {
                    if(line.indexOf("MAC Address") > 0) {
                        smac = line.substring(line.indexOf("-") - 2);
                    }
                }
            } catch (IOException var7) {
                var7.printStackTrace();
            }
        }

        log.info("---mac:" + smac);
        return smac;
    }

    public static boolean isMac(String mac) {
        boolean y = false;
        String macad = mac.trim();
        if(macad.length() != 17) {
            return y;
        } else if(macad.charAt(2) == 45 && macad.charAt(5) == 45 && macad.charAt(8) == 45 && macad.charAt(11) == 45 && macad.charAt(14) == 45) {
            macad = macad.replace('-', '0');

            for(int i = 0; i < macad.length(); ++i) {
                if(!Character.isUpperCase(macad.charAt(i)) && !Character.isDigit(macad.charAt(i))) {
                    y = false;
                    break;
                }

                y = true;
            }

            return y;
        } else {
            return y;
        }
    }

    public static String getUserIP(HttpServletRequest request) {
        String ip = getIpAddress(request);
        if(ip != null) {
            ip = ip.trim();
            if(ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }

            if(ip.length() > 20) {
                ip = ip.substring(0, 19);
            }
        }

        return ip;
    }

    public static String formatIp(String ip) {
        if(ip != null && !"".equals(ip)) {
            String formatIp = null;
            StringTokenizer st = new StringTokenizer(ip, ".");
            String ip1 = (String)st.nextElement();
            String ip2 = (String)st.nextElement();
            String ip3 = (String)st.nextElement();
            String ip4 = (String)st.nextElement();
            DecimalFormat df = new DecimalFormat("000");
            formatIp = df.format((long)Integer.parseInt(ip1)) + "." + df.format((long)Integer.parseInt(ip2)) + "." + df.format((long)Integer.parseInt(ip3)) + "." + df.format((long)Integer.parseInt(ip4));
            return formatIp;
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        String ip = "122.224.178.4, 58.4";
        if(ip.indexOf(",") > 0) {
            ip = ip.substring(0, ip.indexOf(","));
        }

        System.out.println(ip);
    }
}
