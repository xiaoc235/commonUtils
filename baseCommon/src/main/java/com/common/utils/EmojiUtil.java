package com.common.utils;

import com.common.base.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @title 处理emoji表情 
 * @author tangxu
 * @Date  2016年12月12日 下午5:02:16
 */
public class EmojiUtil {
	

	private static final Logger logger = LoggerFactory.getLogger(EmojiUtil.class);
	
	/**
	 * 转码格式
	 */
	private static final String CHARACTER_CODING = "utf-8";
	
	/**
	 * 将str中的emoji表情转为byte数组
	 * 
	 * @param str
	 * @return
	 * @throws BusinessException 
	 */
	public static String resolveToByteFromEmoji(String str) throws BusinessException {
		if( CommonUtils.isBlank(str) ){
			final String msg = "emoji表情字符为空";
			//throw new BusinessException(msg);
		}
		Pattern pattern = Pattern
				.compile("[^(\u2E80-\u9FFF\\w\\s`~!@#\\$%\\^&\\*\\(\\)_+-？（）——=\\[\\]{}\\|;。，、《》”：；“！……’:'\"<,>\\.?/\\\\*)]");
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb2 = new StringBuffer();
		while (matcher.find()) {
			try {
				matcher.appendReplacement(sb2, resolveToByte(matcher.group(0)));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				logger.error(e.toString());
				//final String msg = "emoji解码错误，请输入合法字符";
				throw new BusinessException(e.getMessage());
			}
		}
		matcher.appendTail(sb2);
		return sb2.toString();
	}

	/**
	 * 将str中的byte数组类型的emoji表情转为正常显示的emoji表情
	 * 
	 * @param str
	 * @return
	 * @throws BusinessException 
	 */
	public static String resolveToEmojiFromByte(String str) throws BusinessException {
		if( CommonUtils.isBlank(str) ){
			final String msg = "emoji表情字符为空";
			throw new BusinessException(msg);
		}
		Pattern pattern2 = Pattern.compile("<:([[-]\\d*[,]]+):>");
		Matcher matcher2 = pattern2.matcher(str);
		StringBuffer sb3 = new StringBuffer();
		while (matcher2.find()) {
			try {
				matcher2.appendReplacement(sb3, resolveToEmoji(matcher2.group(0)));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				logger.error(e.toString());
				final String msg = "emoji解码错误，请输入合法字符";
				throw new BusinessException(msg);
			}
		}
		matcher2.appendTail(sb3);
		return sb3.toString();
	}

	private static String resolveToByte(String str) throws UnsupportedEncodingException {
		byte[] b = str.getBytes(CHARACTER_CODING);
		StringBuffer sb = new StringBuffer();
		sb.append("<:");
		for (int i = 0; i < b.length; i++) {
			if (i < b.length - 1) {
				sb.append(Byte.valueOf(b[i]).toString() + ",");
			} else {
				sb.append(Byte.valueOf(b[i]).toString());
			}
		}
		sb.append(":>");
		return sb.toString();
	}

	private static String resolveToEmoji(String str) throws UnsupportedEncodingException {
		str = str.replaceAll("<:", "").replaceAll(":>", "");
		String[] s = str.split(",");
		byte[] b = new byte[s.length];
		for (int i = 0; i < s.length; i++) {
			b[i] = Byte.valueOf(s[i]);
		}
		return new String(b,CHARACTER_CODING);
	}
}
