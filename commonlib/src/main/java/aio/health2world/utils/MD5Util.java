package aio.health2world.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName: MD5Util
 * @Description:TODO(MD5加密类)
 * @author: lishiyou
 * @date: 2015-1-9 下午2:28:41
 * 
 */
public class MD5Util {
	public static String getMD5String(String str) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes(StandardCharsets.UTF_8));

		} catch (NoSuchAlgorithmException e) {
			System.out.println("NoSuchAlgorithmException caught!");
			System.exit(-1);
		}

        byte[] byteArray = messageDigest.digest();

		// StringBuffer md5StrBuff = new StringBuffer();
		StringBuilder md5StrBuff = new StringBuilder();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		// 16位加密，从第9位到25位(字符串中的字母都被转化成大写字母)
//		return md5StrBuff.substring(8, 24).toString().toUpperCase();
		return md5StrBuff.toString().toLowerCase();
	}
}
