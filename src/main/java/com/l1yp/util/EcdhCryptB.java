package com.l1yp.util;

import com.l1yp.MD5;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.HexUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties.Build;
import org.springframework.util.StringUtils;

import javax.crypto.KeyAgreement;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

@Slf4j
public class EcdhCryptB {
    public static final String DEFAULT_PUB_KEY = "04edb8906046f5bfbe9abbc5a88b37d70a6006bfbabc1f0cd49dfb33505e63efc5d78ee4e0a4595033b93d02096dcd3190279211f7b4f6785079e19004aa0e03bc";
    public static final String DEFAULT_SHARE_KEY = "c129edba736f4909ecc4ab8e010f46a3";
    static String SvrPubKey = "04EBCA94D733E399B2DB96EACDD3F69A8BB0F74224E2B44E3357812211D2E62EFBC91BB553098E25E33A799ADC7F76FEB208DA7C6522CDB0719A305180CC54A82E";
    static final String X509Prefix = "3059301306072a8648ce3d020106082a8648ce3d030107034200";
    public static byte[] _c_pri_key = new byte[0];
    public static byte[] _c_pub_key = new byte[0];
    private static byte[] _g_share_key = new byte[0];
    private static boolean initFlg = false;
    public static PrivateKey pkcs8PrivateKey;
    private static int sKeyVersion = 1;
    private static boolean userOpenSSLLib = true;
    public static PublicKey x509PublicKey;

    public byte[] get_c_pub_key() {
        return (byte[]) _c_pub_key.clone();
    }

    public void set_c_pub_key(byte[] bArr) {
        if (bArr != null) {
            _c_pub_key = (byte[]) bArr.clone();
        } else {
            _c_pub_key = new byte[0];
        }
    }

    public void set_c_pri_key(byte[] bArr) {
        if (bArr != null) {
            _c_pri_key = (byte[]) bArr.clone();
        } else {
            _c_pri_key = new byte[0];
        }
    }

    public byte[] get_g_share_key() {
        return (byte[]) _g_share_key.clone();
    }

    public void set_g_share_key(byte[] bArr) {
        if (bArr != null) {
            _g_share_key = (byte[]) bArr.clone();
        } else {
            _g_share_key = new byte[0];
        }
    }

    public byte[] calShareKeyMd5ByPeerPublicKey(byte[] bArr) {
        return calShareKeyByBouncycastle(bArr);
    }


    private PublicKey constructX509PublicKey(String str) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
        return KeyFactory.getInstance("EC", "BC").generatePublic(new X509EncodedKeySpec(HexUtils.fromHexString(str)));
    }

    private byte[] calShareKeyByBouncycastle(byte[] bArr) {
        try {
            PublicKey constructX509PublicKey = constructX509PublicKey("3059301306072a8648ce3d020106082a8648ce3d030107034200" + HexUtils.toHexString(bArr));
            KeyAgreement instance = KeyAgreement.getInstance("ECDH", "BC");
            instance.init(pkcs8PrivateKey);
            instance.doPhase(constructX509PublicKey, true);
            byte[] generateSecret = instance.generateSecret();
            byte[] bArr2 = new byte[16];
            System.arraycopy(generateSecret, 0, bArr2, 0, 16);
            return MD5.toMD5Byte(bArr2);
        } catch (Throwable e) {
            return null;
        }
    }


    public int initShareKeyByDefault() {
        _c_pub_key = HexUtils.fromHexString("04edb8906046f5bfbe9abbc5a88b37d70a6006bfbabc1f0cd49dfb33505e63efc5d78ee4e0a4595033b93d02096dcd3190279211f7b4f6785079e19004aa0e03bc");
        _g_share_key = HexUtils.fromHexString("c129edba736f4909ecc4ab8e010f46a3");
        return 0;
    }

    public int initShareKey() {
        initFlg = true;
        if (initShareKeyByBouncycastle() != 0) {
            return initShareKeyByDefault();
        } else {
            userOpenSSLLib = false;
            return 0;
        }
    }

    public void setPubKey(String str, int i) {
        try {
            if (!StringUtils.isEmpty(str) && i > 0) {
                SvrPubKey = str;
                sKeyVersion = i;
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public int get_pub_key_ver() {
        return sKeyVersion;
    }

    private int initShareKeyByBouncycastle() {
        try {
            KeyPairGenerator instance = KeyPairGenerator.getInstance("EC", "BC");
            instance.initialize(new ECGenParameterSpec("prime256v1"));
            KeyPair genKeyPair = instance.genKeyPair();
            PublicKey publicKey = genKeyPair.getPublic();
            byte[] encoded = publicKey.getEncoded();
            PrivateKey privateKey = genKeyPair.getPrivate();
            privateKey.getEncoded();
            PublicKey constructX509PublicKey = constructX509PublicKey("3059301306072a8648ce3d020106082a8648ce3d030107034200" + SvrPubKey);
            KeyAgreement instance2 = KeyAgreement.getInstance("ECDH", "BC");
            instance2.init(privateKey);
            instance2.doPhase(constructX509PublicKey, true);
            byte[] generateSecret = instance2.generateSecret();
            byte[] bArr = new byte[16];
            System.arraycopy(generateSecret, 0, bArr, 0, 16);
            _g_share_key = MD5.toMD5Byte(bArr);
            _c_pub_key = new byte[65];
            System.arraycopy(encoded, 26, _c_pub_key, 0, 65);
            x509PublicKey = publicKey;
            pkcs8PrivateKey = privateKey;
            System.out.println("privateKey = " + privateKey);
            System.out.println("publicKey = " + publicKey);
            System.out.println("_g_share_key = " + HexUtils.toHexString(_g_share_key));
            System.out.println("privateKey = " + HexUtils.toHexString(privateKey.getEncoded()));
            System.out.println("publicKey = " + HexUtils.toHexString(publicKey.getEncoded()));
            return 0;
        } catch (Throwable th) {
            th.printStackTrace();
            return -2;
        }
    }


    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());
        new EcdhCryptB().initShareKeyByBouncycastle();
    }

}
