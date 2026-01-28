package com.goodeelms.util;

public class GenderUtil {
	public static String getGenderByIdentityNumber(String identityNumber) {
		if (identityNumber == null || identityNumber.length() < 7) {
			return null;
		}

		char genderCh = identityNumber.contains("-") ? identityNumber.charAt(7) : identityNumber.charAt(6);

		if (genderCh == '1' || genderCh == '3' || genderCh == '5' || genderCh == '7') return "남";
		else if (genderCh == '2' || genderCh == '4' || genderCh == '6' || genderCh == '8') return "여";

		return null;
	}
}
