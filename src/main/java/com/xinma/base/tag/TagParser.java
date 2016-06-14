package com.xinma.base.tag;

import java.math.BigInteger;

/**
 * 标签解析器，根据displayCode或者hiddenCode解析出CloudTag对象
 * @author Alauda
 *
 * @date 2016年6月7日
 *
 */
public class TagParser {

	/**
	 * 根据标签隐藏码获取标签版本号
	 * @param hiddenCode 标签隐码
	 * @return 标签版本号
	 */
	private static byte getCodeVersionByHiddenCode(String hiddenCode) {
		int len = hiddenCode.length();
		switch (len) {
		case TagConstants.VERSION_1_HIDDEN_CODE_LEN:
			return TagConstants.CODE_VERSION_1;
		default:
			throw new IllegalArgumentException("illegal argument of hiddenCode, can't get tag version from " + hiddenCode);
		}
	} 
	
	private static byte getCodeVersionByDisplayCode(String displayCode) {
		int len = displayCode.length();
		switch (len) {
		case TagConstants.VERSION_1_DISPLAY_CODE_LEN:
			return TagConstants.CODE_VERSION_1;
		default:
			throw new IllegalArgumentException("illegal argument of displayCode, can't get tag version from " + displayCode);
		}
	} 
	
	private static CloudTag parseVer1TagByHiddenCode(String hiddenCode) throws Exception {
		BigInteger tempCode = BigInteger.valueOf(0);
		
		CloudTag cloudTag = new CloudTag();
		cloudTag.setHiddenCode(hiddenCode);
		
		// 将字符串恢复过来
		for (int i = hiddenCode.length() - 1; i >= 0; i--) {
			tempCode = tempCode.multiply(BigInteger.valueOf(40))
					.add(BigInteger.valueOf(TagConstants.getTableIndex(hiddenCode.charAt(i),TagConstants.codeTable40)));
		}

		if (tempCode.bitLength() == 125) {
			if (tempCode.shiftRight(122).byteValue() == TagConstants.CODE_VERSION_1) {
				cloudTag.setCodeVersion(TagConstants.CODE_VERSION_1);
				
				int bccByte = tempCode.shiftRight(114).intValue() & 0xFF;
				tempCode = tempCode.and(TagConstants.maskOf114Bit);
				if (TagConstants.bccByteCode(tempCode) == bccByte) {
					tempCode = tempCode.shiftLeft(56).and(TagConstants.maskOf114Bit).or(tempCode.shiftRight(58));
					BigInteger randomCode = tempCode.shiftRight(64);
					tempCode = tempCode.and(TagConstants.maskOf64Bit);
					tempCode = tempCode.shiftLeft(34).and(TagConstants.maskOf64Bit).or(tempCode.shiftRight(30));
					BigInteger tagIndex = tempCode.xor(randomCode.shiftLeft(14).or(randomCode.and(BigInteger.valueOf(0x3FFF))));
					
					cloudTag.setHiddenRandomCode(randomCode.longValue());
					cloudTag.setUniqueCode(tagIndex.longValue());
				} else {
					throw new Exception("hiddenCode : "  + hiddenCode +  " bcc byte code error.");
				}
			} else {
				throw new Exception("hiddenCode : "  + hiddenCode +  " version code error.");
			}
		} else {
			throw new Exception("hiddenCode : "  + hiddenCode +  " bit length error.");
		}
		
		return cloudTag;
	}
	
	
	private static CloudTag parseVer1TagByDisplayCode(String displayCode) {

		CloudTag cloudTag = new CloudTag();
		cloudTag.setCodeVersion(TagConstants.CODE_VERSION_1);
		cloudTag.setDisplayCode(displayCode);
		
		BigInteger tempCode = BigInteger.valueOf(0);
		for (int i = displayCode.length() - 1; i >= 0; i--) {
			tempCode = tempCode.multiply(BigInteger.valueOf(36))
					.add(BigInteger.valueOf(TagConstants.getTableIndex(displayCode.charAt(i),TagConstants.codeTable36)));
		}
		
		tempCode = tempCode.shiftLeft(43).and(TagConstants.maskOf82Bit).or(tempCode.shiftRight(39));
		BigInteger randomCode = tempCode.shiftRight(64);
		tempCode = tempCode.and(TagConstants.maskOf64Bit);
		tempCode = tempCode.shiftLeft(21).and(TagConstants.maskOf64Bit).or(tempCode.shiftRight(43));
		tempCode = tempCode.xor(randomCode.shiftLeft(46).or(randomCode.shiftLeft(26))
				.or(randomCode.and(BigInteger.valueOf(0x3FF))));
		
		cloudTag.setDisplayRandomCode(randomCode.longValue());
		cloudTag.setUniqueCode(tempCode.longValue());
			

		return cloudTag;
	
	}
	/**
	 * 根据hiddenCode解析CloudTag对象
	 * @param hiddenCode 标签隐码
	 * @return CloudTag对象
	 * @throws Exception hiddenCode解析失败异常
	 */
	public static CloudTag decodeTagByHiddenCode(String hiddenCode) throws Exception {
		byte codeVersion = getCodeVersionByHiddenCode(hiddenCode);
		switch (codeVersion) {
		case TagConstants.CODE_VERSION_1:
			return parseVer1TagByHiddenCode(hiddenCode);

		default:
			throw new IllegalArgumentException("illegal argument of hiddenCode, can't get tag version from " + hiddenCode);
		}
	}
	
	/**
	 * 根据displayCode解析CloudTag对象
	 * @param displayCode 标签显码
	 * @return CloudTag对象
	 * @throws Exception displayCode解析失败异常
	 */
	public static CloudTag decodeTagByDisplayCode(String displayCode) throws Exception {
		byte codeVersion = getCodeVersionByDisplayCode(displayCode);
		switch (codeVersion) {
		case TagConstants.CODE_VERSION_1:
			return parseVer1TagByDisplayCode(displayCode);

		default:
			throw new IllegalArgumentException("illegal argument of displayCode, can't get tag version from " + displayCode);
		}
	}
}
