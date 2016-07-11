package com.xinma.base.tag;

import java.math.BigInteger;
import java.util.Random;

/**
 * 云码对象生成器
 * @author Alauda
 *
 * @date 2016年6月5日
 *
 */
public class TagBuilder {
	
	private byte codeVersion;
	
	private long tagCount;
	
	private long tagIndex = 0;
	
	private BigInteger startUniqueCode;

	private BigInteger curHiddenRandomCode;
	
	private BigInteger curDisplayRandomCode;
	
	private Random random = new Random();

	/**
	 * 构建云码对象生成器
	 * @param codeVersion 标签编码版本
	 * @param startUniqueCode 唯一码起始序号
	 * @param tagCount 要生成的云码对象数量
	 * @throws IllegalArgumentException
	 */
	public TagBuilder(byte codeVersion, BigInteger startUniqueCode, long tagCount) throws IllegalArgumentException {
		this.codeVersion = codeVersion;
		this.startUniqueCode = startUniqueCode;
		this.tagCount = tagCount;

		if (this.codeVersion == TagConstants.CODE_VERSION_1) {
			if (startUniqueCode.compareTo(TagConstants.maskOf64Bit) > 0
					|| startUniqueCode.add(BigInteger.valueOf(tagCount)).compareTo(TagConstants.maskOf64Bit) > 0) {
				throw new IllegalArgumentException("illegal argument of startUniqueCode or tagCount.");
			}
		} else {
			throw new IllegalArgumentException("illegal argument of codeVersion.");
		}
	}

	private String makeVer1HiddenCode() {

		curHiddenRandomCode = new BigInteger(50, random);
		BigInteger tempCode = BigInteger.valueOf(tagIndex).add(startUniqueCode).and(TagConstants.maskOf64Bit);
		// uniqueCode与随机数异或运算
		tempCode = tempCode.xor(curHiddenRandomCode.shiftLeft(14).or(curHiddenRandomCode.and(BigInteger.valueOf(0x3FFF))));
		// 移位运算(循环右移34位)
		tempCode = tempCode.shiftRight(34).or(tempCode.shiftLeft(30).and(TagConstants.maskOf64Bit));
		// 将uniqueCode与随机数合并
		tempCode = tempCode.or(curHiddenRandomCode.shiftLeft(64));
		// 移位运算(循环右移56位)
		tempCode = tempCode.shiftRight(56).or(tempCode.shiftLeft(58).and(TagConstants.maskOf114Bit));
		// 掺入版本号与校验位
		tempCode = BigInteger.valueOf(TagConstants.CODE_VERSION_1).shiftLeft(122)
				.or(BigInteger.valueOf(TagConstants.bccByteCode(tempCode)).shiftLeft(114)).or(tempCode);

		// 使用字符表中的字符编码
		StringBuilder builder = new StringBuilder();
		int n = 0;
		while (!tempCode.equals(BigInteger.ZERO)) {
			builder.append(TagConstants.codeTable40[tempCode.remainder(TagConstants.codeTable40Length).intValue()]); 
			tempCode = tempCode.divide(TagConstants.codeTable40Length);
			n++;
		}

		// 如果字符串长度不足24,则补充到24
		while (n < 24) {
			builder.append(TagConstants.codeTable40[0]);
			n++;
		}
		return builder.toString();
	
	}
	
	private String makeVer1DisplayCode() {

		curDisplayRandomCode = new BigInteger(18, random);
		BigInteger tempCode = startUniqueCode.add(BigInteger.valueOf(tagIndex)).and(TagConstants.maskOf64Bit);
		tempCode = tempCode.xor(curDisplayRandomCode.shiftLeft(46).or(curDisplayRandomCode.shiftLeft(26))
				.or(curDisplayRandomCode.and(BigInteger.valueOf(0x3FF))));
		// 移位运算(循环右移21位)
		tempCode = tempCode.shiftRight(21).or(tempCode.shiftLeft(43).and(TagConstants.maskOf64Bit));
		// 将uniqueCode与随机数合并
		tempCode = tempCode.or(curDisplayRandomCode.shiftLeft(64));
		// 移位运算(循环右移43位)
		tempCode = tempCode.shiftRight(43).or(tempCode.shiftLeft(39).and(TagConstants.maskOf82Bit));

		StringBuilder builder = new StringBuilder();
		int n = 0;
		while (!tempCode.equals(BigInteger.ZERO)) {
			builder.append(TagConstants.codeTable36[tempCode.remainder(TagConstants.codeTable36Length).intValue()]);
			tempCode = tempCode.divide(TagConstants.codeTable36Length);
			n++;
		}

		// 如果字符串长度不足16位，则补充够16位
		while (n < 16) {
			builder.append(TagConstants.codeTable36[0]);
			n++;
		}
		return builder.toString();
	
	}

	/**
	 * 生成下一云码对象
	 * @return CloudTag对象
	 */
	public CloudTag nextCloudTag() {		
		if (this.tagIndex >= this.tagCount) {
			this.tagIndex = this.tagCount;
			return null;
		}  
		
		CloudTag tag = new CloudTag();
		tag.setCodeVersion(this.codeVersion);
		tag.setTagId(getCurrentUniqueCode());

		switch (this.codeVersion) {
			case TagConstants.CODE_VERSION_1:
				tag.setDisplayCode(makeVer1DisplayCode());
				tag.setHiddenCode(makeVer1HiddenCode());
				break;
	
			default:
				throw new IllegalArgumentException("illegal argument of codeVersion.");
		}
		
		this.tagIndex++;
		
		tag.setHiddenRandomCode(this.curHiddenRandomCode.longValue());
		tag.setDisplayRandomCode(this.curDisplayRandomCode.longValue());
		return tag;
	}
	
	/**
	 * 获取当前uniqueCode
	 * @return 当前唯一码
	 */
	private long getCurrentUniqueCode() {
		return startUniqueCode.add(BigInteger.valueOf(this.tagIndex)).longValue();
	}


	/**
	 * 获取当前云码对象的位置，相对于起始的UniqueCode序号
	 * @return 当前云码对象的位置
	 */
	public long getTagIndex() {
		return this.tagIndex;
	}

	/**
	 * 获取要生成云码对象的总数
	 * @return 要生成云码对象的总数
	 */
	public long getTagCount() {
		return this.tagCount;
	}
}
