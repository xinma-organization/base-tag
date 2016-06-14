package com.xinma.base.tag;

import java.math.BigInteger;

/**
 * 云码标签类库常量定义类，该类中定义的常量只增加不可修改
 * @author Alauda
 *
 * @date 2016年6月6日
 *
 */
public class TagConstants {

	/**************************************
	 ****** 标签编码版本
	 *************************************/
	
	/**
	 * 数字与字幕混合编码，编码后字符串长度24
	 */
	public static final byte CODE_VERSION_1 = 0x07;
	public static final byte CODE_VERSION_2 = 0x06;
	public static final byte CODE_VERSION_3 = 0x05;
	public static final byte CODE_VERSION_4 = 0x04;
	public static final byte CODE_VERSION_5 = 0x03;
	public static final byte CODE_VERSION_6 = 0x02;
	public static final byte CODE_VERSION_7 = 0x01;
	
	protected static final byte VERSION_1_HIDDEN_CODE_LEN = 24;
	protected static final byte VERSION_1_DISPLAY_CODE_LEN = 16;
	
	/**
	 * 64bit序号最大值
	 */
	protected static final BigInteger maskOf64Bit = new BigInteger("FFFFFFFFFFFFFFFF", 16);
	
	/**
	 * 114bit序号最大值
	 */
	protected static final BigInteger maskOf114Bit = new BigInteger("3FFFFFFFFFFFFFFFFFFFFFFFFFFFF", 16);
	
	/**
	 * 82bit序号最大值
	 */
	protected static final BigInteger maskOf82Bit = new BigInteger("3FFFFFFFFFFFFFFFFFFFF", 16);
			

	protected static final BigInteger codeTable36Length = BigInteger.valueOf(36);
	protected static final BigInteger codeTable40Length = BigInteger.valueOf(40);


	/**
	 * 该字符表用于纯数字格式字符串编码
	 */
	protected static final char codeTableDigital[] = { '4', '7', '8', '6', '2', '5', '1', '0',  '9', '3' };

	/**
	 * 该字符表用于displayCode编码
	 */
	protected static final char codeTable36[] = { 'E', 'B', 'A','S', 'M', '5', 'F', 'G', 'C',
			'L', 'H',	'Z', '-', 'D', '4', '1', 'W', '3', 'T', 'P', 'Y', '9', '8', 'R', 'J',
			'2', '+', '0', '7', 'N', 'X', 'V', 'Q', '6', 'K', 'U' };

	/**
	 * 该字符表用于CODE_VERSION_1的hiddenCode编码
	 */
	protected static final char codeTable40[] = { 'C', '5', 'K', 'Y', 'G', 'R', 'F', 'E', 'T', '6', 'V',
			'*', 'H', 'Q', '1', 'B', 'A', '4', '0', '9', 'X', 'U', '8', '-', 'P', 'W', '7', 'D', 'I',
			'.', 'L', 'Z', '$', 'O', 'J', 'N', '2', 'M', 'S', '3' };

	/**
	 * 查找某一字符在对应字符表的索引值
	 * @param ch 要查找的字符
	 * @param lookupTable 要查找的字符表
	 * @return 字符在表中的索引值
	 */
	public static byte getTableIndex(char ch, char[] lookupTable) {
		for (int i = 0; i < lookupTable.length; i++) {
			if (lookupTable[i] == ch)
				return (byte) i;
		}
		
		throw new IllegalArgumentException("illegal argument of ch.");
	}
	
	/**
	 * 按字节相异或，计算校验字节
	 * 
	 * @param data
	 *            需要计算异或的数据
	 * @return
	 */

	/**
	 * BCC(Block check Character)异或校验方法
	 * @param data
	 * @return 字节校验码
	 */
	public static int bccByteCode(BigInteger data) {
		int i = 0;
		int byteCode = 0;
		
		while (i < data.bitLength()) {
			byteCode ^= (data.shiftRight(i).intValue() & 0xFF);
			i += 8;
		}
		return byteCode;
	}
}
