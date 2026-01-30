package com.goodeelms.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtil {
	public static String encryptPassword(String rawPassword) {
        try {
        	// 1. md 라는 믹서기(MessageDigest)에 SHA-256 방식을 사용한다고 지정한다. 
            MessageDigest md = MessageDigest.getInstance("SHA-256"); 
            
            // 2. UTF_8 로 인코딩한 바이트를 믹서기에 넣는다.
            md.update(rawPassword.getBytes(StandardCharsets.UTF_8)); 
            
            // 3. digest() 로 믹서기를 동작한다. 믹서기로 섞은 후 결과물인 바이트 단위 결과물을 저장한다.
            byte[] byteData = md.digest();
            
            StringBuilder sb = new StringBuilder();
            for (byte b : byteData) {
                // 4. 0xff와 AND 연산 하여 부호 비트 제거
            	// 5. 2자리의 16진수로 변환 
                sb.append(String.format("%02x", b & 0xff));
            }
            
            // 6. 변환된 암호문을 문자열 형태로 반환
            return sb.toString(); 
            
        } catch (NoSuchAlgorithmException e) {
            System.err.println("암호화 알고리즘을 찾을 수 없습니다: " + e.getMessage());
            return null;
        }
    }

	// 저장된 비밀번호와 입력 데이터가 일치하는지 확인하는 메서드
	public static boolean isPasswordMatch(String rawPassword, String encryptedPassword) {
        // 1. 입력받은 데이터를  암호화한다.
        String encryptedInput = encryptPassword(rawPassword);
        
        // 2. 암호화된 두 결과가 일치하는지 확인한다.
        return encryptedInput != null && encryptedInput.equals(encryptedPassword);
    }
	
}
