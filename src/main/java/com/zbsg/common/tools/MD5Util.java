package com.zbsg.common.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author zyt
 */
public class MD5Util {
    public MD5Util() {
    }

    public static String getEncryptedData(String str) {
        if(!str.equals("") && !str.equals((Object)null)) {
            String md5Str = null;

            MessageDigest nsae;
            byte[] enBytes;
            try {
                nsae = MessageDigest.getInstance("MD5");
                nsae.update(str.getBytes());
                enBytes = nsae.digest();
                md5Str = byte2hex(enBytes);
            } catch (NoSuchAlgorithmException var5) {
                var5.printStackTrace();
                return null;
            }

            md5Str = md5Str + "1ff433d9ddcc5c53b40bdcc0455a848c";

            try {
                nsae = MessageDigest.getInstance("SHA-1");
                nsae.update(md5Str.getBytes());
                enBytes = nsae.digest();
                return byte2hex(enBytes);
            } catch (NoSuchAlgorithmException var4) {
                var4.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    private static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";

        for(int n = 0; n < b.length; ++n) {
            stmp = Integer.toHexString(b[n] & 255);
            if(stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }

        return hs;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(getEncryptedData("boss2007"));
    }
}
