package com.l1yp;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.HexUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

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
import javax.crypto.KeyAgreement;

@Slf4j
public class EcdhCrypt {
    public static final String DEFAULT_PUB_KEY = "020b03cf3d99541f29ffec281bebbd4ea211292ac1f53d7128";
    public static final String DEFAULT_SHARE_KEY = "4da0f614fc9f29c2054c77048a6566d7";
    public static final String S_PUB_KEY = "04928D8850673088B343264E0C6BACB8496D697799F37211DEB25BB73906CB089FEA9639B4E0260498B51A992D50813DA8";
    public static final String X509_S_PUB_KEY = "3046301006072A8648CE3D020106052B8104001F03320004928D8850673088B343264E0C6BACB8496D697799F37211DEB25BB73906CB089FEA9639B4E0260498B51A992D50813DA8";
    public static byte[] _c_pri_key = new byte[0];
    public static byte[] _c_pub_key = new byte[0];
    private static byte[] _g_share_key = new byte[0];
    private static boolean initFlg = false;
    public static PrivateKey pkcs8PrivateKey;
    private static boolean userOpenSSLLib = true;
    public static PublicKey x509PublicKey;

    public native int GenECDHKeyEx(String str, String str2, String str3);

    public int GenereateKey() {
        try {
            int GenECDHKeyEx;
            synchronized (EcdhCrypt.class) {
                GenECDHKeyEx = GenECDHKeyEx("04928D8850673088B343264E0C6BACB8496D697799F37211DEB25BB73906CB089FEA9639B4E0260498B51A992D50813DA8", "", "");
            }
            return GenECDHKeyEx;
        } catch (UnsatisfiedLinkError e) {
            log.error("GenereateKey failed ", e);
            return -1;
        } catch (RuntimeException e2) {
            log.error("OpenSSL generate key failed, turn another method ", e2);
            return -2;
        } catch (Exception e3) {
            log.error("GenereateKey failed ", e3);
            return -3;
        } catch (Error e4) {
            log.error("GenereateKey failed ", e4.getMessage());
            return -4;
        }
    }

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
        // log.info("userOpenSSLLib " + userOpenSSLLib + " peerRawPublicKey " + HexUtils.toHexString(bArr) + " at " + t.m(), "");
        // if (true == userOpenSSLLib) {
        //     return calShareKeyByOpenSSL(HexUtils.toHexString(_c_pri_key), HexUtils.toHexString(_c_pub_key), HexUtils.toHexString(bArr));
        // }
        return calShareKeyByBouncycastle(bArr);
    }

    private byte[] calShareKeyByOpenSSL(String str, String str2, String str3) {
        log.info("calShareKeyByOpenSSL publickey " + str2, "");
        if (GenECDHKeyEx(str3, str2, str) == 0) {
            return _g_share_key;
        }
        // t.as.attr_api(2461268);
        return null;
    }

    /**
     * 构造java publicKey
     * @param str x509的der二进制格式的hex描述
     * @return
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private PublicKey constructX509PublicKey(String str) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
        log.info("constructX509PublicKey publickey {}", str);
        return KeyFactory.getInstance("EC", "BC").generatePublic(new X509EncodedKeySpec(HexUtils.fromHexString(str)));
    }

    private byte[] calShareKeyByBouncycastle(byte[] bArr) {
        try {
            String str = "3046301006072A8648CE3D020106052B8104001F03320004";
            if (bArr.length < 30) {
                str = "302E301006072A8648CE3D020106052B8104001F031A00";
            }
            PublicKey constructX509PublicKey = constructX509PublicKey(str + HexUtils.toHexString(bArr));
            log.info("raw public key " + HexUtils.toHexString(_c_pub_key), "");
            log.info("pkcs8PrivateKey " + pkcs8PrivateKey.toString(), "");
            KeyAgreement instance = KeyAgreement.getInstance("ECDH", "BC");
            instance.init(pkcs8PrivateKey);
            instance.doPhase(constructX509PublicKey, true);
            byte[] generateSecret = instance.generateSecret();
            byte[] toMD5Byte = MD5.toMD5Byte(generateSecret);
            log.info("share key " + HexUtils.toHexString(generateSecret), "");
            log.info("share key md5 " + HexUtils.toHexString(toMD5Byte), "");
            return toMD5Byte;
        } catch (ExceptionInInitializerError e) {
            log.error("create key failed ExceptionInInitializerError, ", e);
            return null;
        } catch (Exception e2) {
            log.info("calShareKeyByBouncycastle failed {}  peer public key {}", pkcs8PrivateKey.toString(), HexUtils.toHexString(bArr));
            return null;
        }
    }

    private int initShareKeyByOpenSSL() {
        if (_c_pub_key == null || _c_pub_key.length == 0 || _c_pri_key == null || _c_pri_key.length == 0 || _g_share_key == null || _g_share_key.length == 0) {
            log.info("_c_pub_key " + HexUtils.toHexString(_c_pub_key), "");
            log.info("_c_pri_key " + HexUtils.toHexString(_c_pri_key), "");
            log.info("_g_share_key " + HexUtils.toHexString(_g_share_key), "");
            log.info("initShareKeyByOpenSSL generate null key");
            return -2;
        }
        log.info("initShareKeyByOpenSSL OK");
        return 0;
    }

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        PublicKey publicKey = new EcdhCrypt().constructX509PublicKey("302E301006072A8648CE3D020106052B8104001F031A00020B03CF3D99541F29FFEC281BEBBD4EA211292AC1F53D7128");
        System.out.println(publicKey);
        System.exit(0);

        new EcdhCrypt().initShareKeyByBouncycastle();
    }

    private int initShareKeyByBouncycastle() {
        try {
            Security.addProvider(new BouncyCastleProvider());
            KeyPairGenerator instance = KeyPairGenerator.getInstance("EC", "BC");
            instance.initialize(new ECGenParameterSpec("secp192k1"));
            KeyPair genKeyPair = instance.genKeyPair();
            PublicKey publicKey = genKeyPair.getPublic(); // 客户端公钥
            System.out.println(publicKey);
            byte[] encoded = publicKey.getEncoded();
            PrivateKey privateKey = genKeyPair.getPrivate(); // 客户端私钥
            System.out.println(privateKey);
            privateKey.getEncoded();
            PublicKey constructX509PublicKey = constructX509PublicKey(X509_S_PUB_KEY); // 服务端公钥
            KeyAgreement instance2 = KeyAgreement.getInstance("ECDH", "BC");
            instance2.init(privateKey); // 客户端私钥
            instance2.doPhase(constructX509PublicKey, true);
            _g_share_key = MD5.toMD5Byte(instance2.generateSecret()); // 协商出 共享密钥
            _c_pub_key = new byte[49];
            System.arraycopy(encoded, 23, _c_pub_key, 0, 49);
            System.out.println(HexUtils.toHexString(_c_pub_key));
            x509PublicKey = publicKey;
            pkcs8PrivateKey = privateKey;
            log.info("initShareKeyByBouncycastle OK", "");
            return 0;
        } catch (ExceptionInInitializerError e) {
            log.error("create key pair and shared key failed ExceptionInInitializerError", e);
            return -1;
        } catch (Exception e2) {
            log.error("initShareKeyByBouncycastle failed", e2);
            return -2;
        }
    }

    public int initShareKeyByDefault() {
        _c_pub_key = HexUtils.fromHexString("020b03cf3d99541f29ffec281bebbd4ea211292ac1f53d7128");
        _g_share_key = HexUtils.fromHexString("4da0f614fc9f29c2054c77048a6566d7");
        log.info("initShareKeyByDefault OK", "");
        return 0;
    }

    public int initShareKey() {
        if (true == initFlg) {
            return 0;
        }
        initFlg = true;
        if (initShareKeyByOpenSSL() == 0) {
            userOpenSSLLib = true;
            return 0;
        } else if (initShareKeyByBouncycastle() != 0) {
            return initShareKeyByDefault();
        } else {
            userOpenSSLLib = false;
            return 0;
        }
    }
}
