package com.xinma.base.tag;

import java.math.BigInteger;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TagBuilderTest {
	@Test
	  public void codeVersion1Test() throws Exception {
		BigInteger startUniqueCode = BigInteger.valueOf(10101020);
		long tagCount = 1000000;
		TagBuilder builder = new TagBuilder(TagConstants.CODE_VERSION_1,startUniqueCode, tagCount);
		
		CloudTag cloudTag = builder.nextCloudTag();
		while (cloudTag != null) {
			CloudTag decodeTag = TagParser.decodeTagByHiddenCode(cloudTag.getHiddenCode());
			Assert.assertEquals(cloudTag.getUniqueCode(), decodeTag.getUniqueCode());
			Assert.assertEquals(cloudTag.getCodeVersion(), decodeTag.getCodeVersion());
			Assert.assertEquals(cloudTag.getHiddenCode(), decodeTag.getHiddenCode());
			Assert.assertEquals(cloudTag.getHiddenRandomCode(), decodeTag.getHiddenRandomCode());
			//System.out.println(cloudTag.getUniqueCode() + "," + cloudTag.getHiddenCode() + "," + cloudTag.getDisplayCode() + "," + cloudTag.getHiddenRandomCode());
			decodeTag = TagParser.decodeTagByDisplayCode(cloudTag.getDisplayCode());
			Assert.assertEquals(cloudTag.getUniqueCode(), decodeTag.getUniqueCode());
			Assert.assertEquals(cloudTag.getCodeVersion(), decodeTag.getCodeVersion());
			Assert.assertEquals(cloudTag.getDisplayCode(), decodeTag.getDisplayCode());
			Assert.assertEquals(cloudTag.getDisplayRandomCode(), decodeTag.getDisplayRandomCode());
			
			cloudTag = builder.nextCloudTag();
		}
	  }
}
