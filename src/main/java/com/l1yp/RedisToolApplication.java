package com.l1yp;

import com.l1yp.mapper.UserMapper;
import com.l1yp.model.UserInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

/**
 * @Author Lyp
 * @Date   2020-06-23
 * @Email  l1yp@qq.com
 */
@SpringBootApplication
public class RedisToolApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext ctx = SpringApplication.run(RedisToolApplication.class, args);
        System.out.println("http://localhost:9999/swagger-ui.html#/");
        UserMapper userMapper = ctx.getBean(UserMapper.class);
        List<UserInfo> userInfos = userMapper.selectAll();
        userInfos.stream().forEach(System.out::println);
        System.exit(0);
        // String str = "";
        // // Security.addProvider(new BouncyCastleProvider());
        // KeyPair keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        // PrivateKey privateKey = keyPair.getPrivate();
        // PublicKey publicKey = keyPair.getPublic();
        // System.out.println("privateKey = " + privateKey);
        // System.out.println("publicKey = " + publicKey);
        // Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
        // // com.sun.crypto.provider.RSACipher rsaCipher = new RSACipher();
        // //
        // cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        // byte[] clear = new byte[214];
        // for (int i = 0; i < 214; i++) {
        //     clear[i] = (byte) i;
        // }
        // byte[] bytes = cipher.doFinal(clear);
        // System.out.println("size = " + bytes.length + ", bytes = " + Arrays.toString(bytes));
        //
        //
        //
        // bytes = RSACore.rsa(bytes, (RSAPrivateKey) privateKey, true);
        // System.out.println("size = " + bytes.length + ", bytes = " + Arrays.toString(bytes));
        // RSAPadding padding = RSAPadding.getInstance(RSAPadding.PAD_OAEP_MGF1, 256);
        // bytes = padding.unpad(bytes);
        // System.out.println("size = " + bytes.length + ", bytes = " + Arrays.toString(bytes));
        // System.exit(0);

        // testRSA();
        // System.exit(0);
        //
        // String str = "<RSAKeyValue><Modulus>snEimxxZzICFSmJIP5BLncDzcaBObb4qi7VgMfX3zLsVi7Z5J9iLmfHTjUjsQ8ZmMEKkbAKdJZlr4Vpb7fzVdxfX5QIcj0SGSXJmMydPpOQguifGaAlijIKneFX6/JKAGJnzW21lVx71lPSsxaGFS/BdKBwXvumXz4gqkLsmP38=</Modulus><Exponent>AQAB</Exponent><P>9bvlfZwzQnPKxg9Nu9VuklKhlnKOwiz7XRNLRNmcL0sp7dIRqrg8Ug9dg04VYzMVHgvFRWl99KCq6q6pC+l/5Q==</P><Q>ueWOZN9f4VF+iRGbbof4Pq2eZ9l1RelfGgwMaBdixgLPX8049i7XCgHXawU1wCW0qpvhUFCbpNyKE5/OkOejkw==</Q><DP>dnOvaQAteV/lo9lmqB924FOyjpoEFLeoJzKQusyGzAlYWcpEN939PbUoZ43xI1NRX51e51m1nIECQoQU+qgAvQ==</DP><DQ>jPDWfPQ+pX48CYAn8C12sU7BAsuEdnTxfbYU0fapFKecwm29+iiZMSUkRie1EPGFgLSep132I5Bxv18yJfr5sQ==</DQ><InverseQ>irl+dc7XKFgkt1YviWJ7vOm7Jz92hJdhQ68QHMg9Zuhh/U7aSxAy+2a9MRLXyjPUVed0HxRQMISkmpwD8gZXwg==</InverseQ><D>hF64tSfK1ZFhT1SpE74O2e+Qp/GrPG2dUhYyTd2Q/SarTQEHzy8sH/7sWBqIaZ57n8FQB2/I82NkS7+kAl9UWcv81t4HpgEGKvv/Imupp7K4nOa4DAeKZnV3fgLnjmNWEzGkCvInmEyy7SjIp72BduvOEU+DmGZb1df+lgOST0E=</D></RSAKeyValue>";
        //
        // String strModule = sub(str, "<Modulus>", "</Modulus>");
        // String strExponent = sub(str, "<Exponent>", "</Exponent>");
        // String strP = sub(str, "<P>", "</P>");
        // String strQ = sub(str, "<Q>", "</Q>");
        // String strDP = sub(str, "<DP>", "</DP>");
        // String strDQ = sub(str, "<DQ>", "</DQ>");
        // String strInverseQ = sub(str, "<InverseQ>", "</InverseQ>");
        // String strD = sub(str, "<D>", "</D>");
        //
        // BigInteger module = new BigInteger(1, Base64.getDecoder().decode(strModule));
        // BigInteger e = new BigInteger(1, Base64.getDecoder().decode(strExponent));
        // BigInteger p = new BigInteger(1, Base64.getDecoder().decode(strP));
        // BigInteger q = new BigInteger(1, Base64.getDecoder().decode(strQ));
        // BigInteger n = p.multiply(q);
        // BigInteger pe = p.multiply(e);
        // BigInteger qe = q.multiply(e);
        // BigInteger dp = new BigInteger(1, Base64.getDecoder().decode(strDP));
        // BigInteger dq = new BigInteger(1, Base64.getDecoder().decode(strDQ));
        // BigInteger coeff = new BigInteger(1, Base64.getDecoder().decode(strInverseQ));
        // BigInteger d = new BigInteger(1, Base64.getDecoder().decode(strD));
        //
        // RSAPrivateKey privateKey = RSAPrivateCrtKeyImpl.newKey(KeyType.RSA, null, n, e, d, p, q, pe, qe, coeff);
        // System.out.println("读取XML的私钥 = " + privateKey);
        //
        // byte[] enc = Base64.getDecoder().decode("MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALJxIpscWcyAhUpiSD+QS53A83GgTm2+Kou1YDH198y7FYu2eSfYi5nx041I7EPGZjBCpGwCnSWZa+FaW+381XcX1+UCHI9EhklyZjMnT6TkILonxmgJYoyCp3hV+vySgBiZ81ttZVce9ZT0rMWhhUvwXSgcF77pl8+IKpC7Jj9/AgMBAAECgYEAhF64tSfK1ZFhT1SpE74O2e+Qp/GrPG2dUhYyTd2Q/SarTQEHzy8sH/7sWBqIaZ57n8FQB2/I82NkS7+kAl9UWcv81t4HpgEGKvv/Imupp7K4nOa4DAeKZnV3fgLnjmNWEzGkCvInmEyy7SjIp72BduvOEU+DmGZb1df+lgOST0ECQQD1u+V9nDNCc8rGD0271W6SUqGWco7CLPtdE0tE2ZwvSynt0hGquDxSD12DThVjMxUeC8VFaX30oKrqrqkL6X/lAkEAueWOZN9f4VF+iRGbbof4Pq2eZ9l1RelfGgwMaBdixgLPX8049i7XCgHXawU1wCW0qpvhUFCbpNyKE5/OkOejkwJAdnOvaQAteV/lo9lmqB924FOyjpoEFLeoJzKQusyGzAlYWcpEN939PbUoZ43xI1NRX51e51m1nIECQoQU+qgAvQJBAIzw1nz0PqV+PAmAJ/AtdrFOwQLLhHZ08X22FNH2qRSnnMJtvfoomTElJEYntRDxhYC0nqdd9iOQcb9fMiX6+bECQQCKuX51ztcoWCS3Vi+JYnu86bsnP3aEl2FDrxAcyD1m6GH9TtpLEDL7Zr0xEtfKM9RV53QfFFAwhKSanAPyBlfC");
        // System.out.println("转PKCS8的私钥 = " + RSAPrivateCrtKeyImpl.newKey(enc));
        //
        // System.exit(0);
        //
        //
        // byte[] bytes = ClassLoader.getSystemResourceAsStream("lyp.crt").readAllBytes();
        // bytes = HexUtils.fromHexString(new String(bytes));
        // X509Certificate impl = new sun.security.x509.X509CertImpl(bytes);
        // System.out.println("impl = " + impl);
        //
        // InputStream is = ClassLoader.getSystemResourceAsStream("rsa_pub.key");
        // URL url = ClassLoader.getSystemResource("rsa_pub.key");
        // PEMFile pemFile = new PEMFile(url.getPath());
        // System.out.println(pemFile.getPrivateKey());
        //
        // // System.out.println(RSAPublicKeyFromPEM(is));
        //
        // System.exit(0);
        //
        // retry:
        // for (int i = 0; i < 10; i++) {
        //     System.out.println("i = " + i);
        //     for (int j = 0; j < 10; j++) {
        //         System.out.println("j = " + j);
        //         if (j == 5){
        //             continue retry;
        //         }
        //     }
        // }
        //
        // System.exit(0);
        //
        // SimpleThread task1 = new SimpleThread("1");
        // SimpleThread task2 = new SimpleThread("2");
        // SimpleThread task3 = new SimpleThread("3");
        // SimpleThread task4 = new SimpleThread("4");
        // SimpleThread task5 = new SimpleThread("5");
        // SimpleThread task6 = new SimpleThread("6");
        // SimpleThread task7 = new SimpleThread("7");
        // SimpleThread task8 = new SimpleThread("8");
        //
        // HashThreadPoolExecutor executor = new HashThreadPoolExecutor(5);
        // executor.execute("1", task1);
        // executor.execute("1", task2);
        // executor.execute("1", task3);
        // executor.execute("1", task4);
        // executor.execute("1", task5);
        // executor.execute("1", task6);
        // executor.execute("2", task7);
        // executor.execute("4", task8);
        //
        // executor.shutdown();
        //

        // TaskManager manager = ctx.getBean(TaskManager.class);
        // SimpleTrigger trigger = new SimpleTrigger();
        // trigger.setInitialDelay(3000);
        // trigger.setPeriod(2000);
        // manager.startTask("trigger-test", trigger, new SimpleTask());



        // RedisTool redisTool = ctx.getBean("redisTool", RedisTool.class);
        //
        // RedisKey verifyKey = RedisKeyConst.VERIFY_CODE.build().arg(5).arg("5");
        // redisTool.set(verifyKey, "5678");
        // System.out.println("verify_code: " + redisTool.getString(verifyKey));
        //
        // RedisKey customKey = RedisKeyConst.CUSTOM_INFO.build().arg("666");
        // Map<String, Object> map = new HashMap<>(16);
        // map.put("kkk", 666);
        // redisTool.set(customKey, map);
        //
        // Map<String, Object> item = redisTool.get(customKey, Map.class);
        // System.out.println("item = " + item);
        //
        // RedisKey userRedisKey = RedisKeyConst.USER_INFO.build().arg("lyp");
        // boolean result = redisTool.set(userRedisKey, new User(23, "lyp"));
        // System.out.println("result = " + result);
        // User user = redisTool.get(userRedisKey, User.class);
        // System.out.println("user = " + user);

    }

}
