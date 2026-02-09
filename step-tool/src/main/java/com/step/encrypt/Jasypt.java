package com.step.encrypt;

public class Jasypt {
//https://www.cnblogs.com/yangzhilong/p/10881589.html

    public static void main(String[] args) {
//# 加密的密钥
//# 测试环境可以采用在配置文件中配置
//# 生产环境建议采用启动参数的形式传入
//# 其他配置参数参考:com.ulisesbocchio.jasyptspringboot.properties.JasyptEncryptorConfigurationProperties
//# jasypt.encryptor.password=you salt
//
//# 解密得到原始密码
//        StandardPBEStringEncryptor textEncryptor = new StandardPBEStringEncryptor();
//        //设置秘钥
//        textEncryptor.setPassword("joii");
//        //加密
//        String sjoii = textEncryptor.encrypt("");
//        System.out.println("sjoii = " + sjoii);
//        //解密
//        String decrypt = textEncryptor.decrypt("Gp95Bf4xjtdkBVqv1RpT8GLfPADef6jx");
//        System.out.println("decrypt = " + decrypt);
//# spring.datasource.password= ENC(密文)

    }
}
