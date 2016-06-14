package com.xinma.base.tag;

/**
 * 二维码标签实体类
 * 
 * @author Alauda
 *
 * @date 2016年6月4日
 *
 */
public class CloudTag {

	/**
	 * 标签编码版本，不同版本的标签编码的方式‘编码后code的长度以及字符范围不同
	 */
	private Byte codeVersion;
	
	/**
	 * 标签系统唯一码，该码必须系统内唯一，不同version的序列号也不能相同，取值范围0 ~ 2^64
	 */
	private Long uniqueCode;
	
	/**
	 * 系统标签主入口,二维码标签的编码值
	 */
	private String hiddenCode;
	
	/**
	 * 编码时用于生成hiddenCode的随机数
	 */
	private Long hiddenRandomCode;
	
	
	/**
	 * 系统标签备用码，如果hiddenCode失效，使用displayCode查询
	 */
	private String displayCode;

	
	/**
	 * 编码时用于生成displayCode的随机数
	 */
	private Long displayRandomCode;

	public Byte getCodeVersion() {
		return codeVersion;
	}

	public void setCodeVersion(Byte codeVersion) {
		this.codeVersion = codeVersion;
	}

	public Long getHiddenRandomCode() {
		return hiddenRandomCode;
	}

	public void setHiddenRandomCode(Long hiddenRandomCode) {
		this.hiddenRandomCode = hiddenRandomCode;
	}

	public Long getUniqueCode() {
		return uniqueCode;
	}

	public void setUniqueCode(Long uniqueCode) {
		this.uniqueCode = uniqueCode;
	}

	public String getHiddenCode() {
		return hiddenCode;
	}

	public void setHiddenCode(String hiddenCode) {
		this.hiddenCode = hiddenCode;
	}

	public String getDisplayCode() {
		return displayCode;
	}

	public void setDisplayCode(String displayCode) {
		this.displayCode = displayCode;
	}

	public Long getDisplayRandomCode() {
		return displayRandomCode;
	}

	public void setDisplayRandomCode(Long displayRandomCode) {
		this.displayRandomCode = displayRandomCode;
	}
	
	public boolean isValid() {
		return this.uniqueCode != null && this.hiddenRandomCode != null;
	}
}
