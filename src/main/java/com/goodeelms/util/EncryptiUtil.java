package com.goodeelms.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptiUtil {
	public static String encryptSHA256(String rawData) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256"); // 항상 64의 글자이다.
            
            // 2. 비밀번호를 바이트 배열로 변환하여 해싱
            md.update(rawData.getBytes());
            byte[] byteData = md.digest();
            
            // 3. 바이트 데이터를 16진수 문자열로 변환 (Hex format)
            StringBuilder sb = new StringBuilder();
            for (byte b : byteData) {
                // 0xff와 AND 연산 후 16진수 문자열로 변환
                sb.append(String.format("%02x", b));
            }
            
            return sb.toString();
            
        } catch (NoSuchAlgorithmException e) {
            System.err.println("암호화 알고리즘을 찾을 수 없습니다: " + e.getMessage());
            return null;
        }
    }
}
