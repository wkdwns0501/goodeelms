package com.goodeelms.filter;

public class XssFilter {
	
	public static String basicXssFilter(String content) {
	    if (content == null) return null;
	    
	    // HTML을 허용하되 스크립트 실행만 막는 최소한의 조치
	    return content.replaceAll("(?i)<script", "&lt;script")
                .replaceAll("(?i)javascript", "x-javascript")
                .replaceAll("(?i)onload", "x-onload")
                .replaceAll("(?i)onerror", "x-onerror") 
                .replaceAll("(?i)onclick", "x-onclick");
	}
}
