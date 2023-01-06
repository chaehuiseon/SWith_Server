package com.example.demo;

import org.aspectj.lang.annotation.Before;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@ComponentScan
public class Jasypt {


    private final String pwd = "1";
    private final PooledPBEStringEncryptor encryptor;

    public Jasypt() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setPoolSize(4);
        encryptor.setPassword(pwd);
        encryptor.setAlgorithm("PBEWithMD5AndTripleDES");
        this.encryptor = encryptor;
    }


    @Test
    @DisplayName("암호화")
    void encode() {
        String content = "2";   //암호화 할 내용
        String encryptedContent = encryptor.encrypt(content); //암호화
        System.out.println("Enc : " + encryptedContent);
    }

    @Test
    @DisplayName("복호화")
    void decode() {
        String content = "1"; //복호화 할 내용
        String decryptedContent = encryptor.decrypt(content); //복호화
        System.out.println("DEC: " + decryptedContent);
    }


}
