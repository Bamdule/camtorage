package com.camtorage;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CamtorageApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void jasypt() {
        String url = "jdbc:mariadb://studycafe-database-1.c2rdlwzsqvnq.ap-northeast-2.rds.amazonaws.com:3306/camtorage";
        String username = "admin";
        String password = "029775293";

        System.out.println(jasyptEncoding(url));
        System.out.println(jasyptEncoding(username));
        System.out.println(jasyptEncoding(password));
    }

    public String jasyptEncoding(String value) {

        String key = "xdweA9pIXGVSJ8nbPsZhqjBe1xu0XGw7H7u7p71sKnqJObc69hIhrIFOkQ8ne6JjzXYBl8EDYb0TX445megvYjdXNtVuXozxDeovfyfb2sbFm4cw0ikd72zN44yMxLtm";
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWithMD5AndDES");
        pbeEnc.setPassword(key);
        return pbeEnc.encrypt(value);
    }
}
