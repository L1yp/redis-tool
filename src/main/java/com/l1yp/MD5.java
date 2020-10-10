package com.l1yp;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author Lyp
 * @Date 2020-07-15
 * @Email l1yp@qq.com
 */
public class MD5 {
    public static byte[] toMD5Byte(byte[] generateSecret) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        md.update(generateSecret);
        byte[] digest = md.digest();
        return digest;
    }
}
