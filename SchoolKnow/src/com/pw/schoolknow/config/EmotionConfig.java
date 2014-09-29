package com.pw.schoolknow.config;

import java.util.LinkedHashMap;
import java.util.Map;

import com.pw.schoolknow.R;

public class EmotionConfig {
	
	public static final int NUM_PAGE = 6;// ×Ü¹²ÓĞ¶àÉÙÒ³
	public static final int NUM = 20;// Ã¿Ò³20¸ö±íÇé,»¹ÓĞ×îºóÒ»¸öÉ¾³ıbutton
	private Map<String, Integer> mFaceMap = new LinkedHashMap<String, Integer>();
	
	public EmotionConfig(){
		initFaceMap();
	}
	
	public Map<String, Integer> getFaceMap() {
		if (!mFaceMap.isEmpty())
			return mFaceMap;
		return null;
	}
	
	private void initFaceMap() {
		// TODO Auto-generated method stub
		mFaceMap.put("[ßÚÑÀ]", R.drawable.f000);
		mFaceMap.put("[µ÷Æ¤]", R.drawable.f001);
		mFaceMap.put("[Á÷º¹]", R.drawable.f002);
		mFaceMap.put("[ÍµĞ¦]", R.drawable.f003);
		mFaceMap.put("[ÔÙ¼û]", R.drawable.f004);
		mFaceMap.put("[ÇÃ´ò]", R.drawable.f005);
		mFaceMap.put("[²Áº¹]", R.drawable.f006);
		mFaceMap.put("[ÖíÍ·]", R.drawable.f007);
		mFaceMap.put("[Ãµ¹å]", R.drawable.f008);
		mFaceMap.put("[Á÷Àá]", R.drawable.f009);
		mFaceMap.put("[´ó¿Ş]", R.drawable.f010);
		mFaceMap.put("[Ğê]", R.drawable.f011);
		mFaceMap.put("[¿á]", R.drawable.f012);
		mFaceMap.put("[×¥¿ñ]", R.drawable.f013);
		mFaceMap.put("[Î¯Çü]", R.drawable.f014);
		mFaceMap.put("[±ã±ã]", R.drawable.f015);
		mFaceMap.put("[Õ¨µ¯]", R.drawable.f016);
		mFaceMap.put("[²Ëµ¶]", R.drawable.f017);
		mFaceMap.put("[¿É°®]", R.drawable.f018);
		mFaceMap.put("[É«]", R.drawable.f019);
		mFaceMap.put("[º¦Ğß]", R.drawable.f020);

		mFaceMap.put("[µÃÒâ]", R.drawable.f021);
		mFaceMap.put("[ÍÂ]", R.drawable.f022);
		mFaceMap.put("[Î¢Ğ¦]", R.drawable.f023);
		mFaceMap.put("[·¢Å­]", R.drawable.f024);
		mFaceMap.put("[ŞÏŞÎ]", R.drawable.f025);
		mFaceMap.put("[¾ª¿Ö]", R.drawable.f026);
		mFaceMap.put("[Àäº¹]", R.drawable.f027);
		mFaceMap.put("[°®ĞÄ]", R.drawable.f028);
		mFaceMap.put("[Ê¾°®]", R.drawable.f029);
		mFaceMap.put("[°×ÑÛ]", R.drawable.f030);
		mFaceMap.put("[°ÁÂı]", R.drawable.f031);
		mFaceMap.put("[ÄÑ¹ı]", R.drawable.f032);
		mFaceMap.put("[¾ªÑÈ]", R.drawable.f033);
		mFaceMap.put("[ÒÉÎÊ]", R.drawable.f034);
		mFaceMap.put("[Ë¯]", R.drawable.f035);
		mFaceMap.put("[Ç×Ç×]", R.drawable.f036);
		mFaceMap.put("[º©Ğ¦]", R.drawable.f037);
		mFaceMap.put("[°®Çé]", R.drawable.f038);
		mFaceMap.put("[Ë¥]", R.drawable.f039);
		mFaceMap.put("[Æ²×ì]", R.drawable.f040);
		mFaceMap.put("[ÒõÏÕ]", R.drawable.f041);

		mFaceMap.put("[·Ü¶·]", R.drawable.f042);
		mFaceMap.put("[·¢´ô]", R.drawable.f043);
		mFaceMap.put("[ÓÒºßºß]", R.drawable.f044);
		mFaceMap.put("[Óµ±§]", R.drawable.f045);
		mFaceMap.put("[»µĞ¦]", R.drawable.f046);
		mFaceMap.put("[·ÉÎÇ]", R.drawable.f047);
		mFaceMap.put("[±ÉÊÓ]", R.drawable.f048);
		mFaceMap.put("[ÔÎ]", R.drawable.f049);
		mFaceMap.put("[´ó±ø]", R.drawable.f050);
		mFaceMap.put("[¿ÉÁ¯]", R.drawable.f051);
		mFaceMap.put("[Ç¿]", R.drawable.f052);
		mFaceMap.put("[Èõ]", R.drawable.f053);
		mFaceMap.put("[ÎÕÊÖ]", R.drawable.f054);
		mFaceMap.put("[Ê¤Àû]", R.drawable.f055);
		mFaceMap.put("[±§È­]", R.drawable.f056);
		mFaceMap.put("[µòĞ»]", R.drawable.f057);
		mFaceMap.put("[·¹]", R.drawable.f058);
		mFaceMap.put("[µ°¸â]", R.drawable.f059);
		mFaceMap.put("[Î÷¹Ï]", R.drawable.f060);
		mFaceMap.put("[Æ¡¾Æ]", R.drawable.f061);
		mFaceMap.put("[Æ®³æ]", R.drawable.f062);

		mFaceMap.put("[¹´Òı]", R.drawable.f063);
		mFaceMap.put("[OK]", R.drawable.f064);
		mFaceMap.put("[°®Äã]", R.drawable.f065);
		mFaceMap.put("[¿§·È]", R.drawable.f066);
		mFaceMap.put("[Ç®]", R.drawable.f067);
		mFaceMap.put("[ÔÂÁÁ]", R.drawable.f068);
		mFaceMap.put("[ÃÀÅ®]", R.drawable.f069);
		mFaceMap.put("[µ¶]", R.drawable.f070);
		mFaceMap.put("[·¢¶¶]", R.drawable.f071);
		mFaceMap.put("[²î¾¢]", R.drawable.f072);
		mFaceMap.put("[È­Í·]", R.drawable.f073);
		mFaceMap.put("[ĞÄËé]", R.drawable.f074);
		mFaceMap.put("[Ì«Ñô]", R.drawable.f075);
		mFaceMap.put("[ÀñÎï]", R.drawable.f076);
		mFaceMap.put("[×ãÇò]", R.drawable.f077);
		mFaceMap.put("[÷¼÷Ã]", R.drawable.f078);
		mFaceMap.put("[»ÓÊÖ]", R.drawable.f079);
		mFaceMap.put("[ÉÁµç]", R.drawable.f080);
		mFaceMap.put("[¼¢¶ö]", R.drawable.f081);
		mFaceMap.put("[À§]", R.drawable.f082);
		mFaceMap.put("[ÖäÂî]", R.drawable.f083);

		mFaceMap.put("[ÕÛÄ¥]", R.drawable.f084);
		mFaceMap.put("[¿Ù±Ç]", R.drawable.f085);
		mFaceMap.put("[¹ÄÕÆ]", R.drawable.f086);
		mFaceMap.put("[ôÜ´óÁË]", R.drawable.f087);
		mFaceMap.put("[×óºßºß]", R.drawable.f088);
		mFaceMap.put("[¹şÇ·]", R.drawable.f089);
		mFaceMap.put("[¿ì¿ŞÁË]", R.drawable.f090);
		mFaceMap.put("[ÏÅ]", R.drawable.f091);
		mFaceMap.put("[ÀºÇò]", R.drawable.f092);
		mFaceMap.put("[Æ¹ÅÒÇò]", R.drawable.f093);
		mFaceMap.put("[NO]", R.drawable.f094);
		mFaceMap.put("[ÌøÌø]", R.drawable.f095);
		mFaceMap.put("[âæ»ğ]", R.drawable.f096);
		mFaceMap.put("[×ªÈ¦]", R.drawable.f097);
		mFaceMap.put("[¿ÄÍ·]", R.drawable.f098);
		mFaceMap.put("[»ØÍ·]", R.drawable.f099);
		mFaceMap.put("[ÌøÉş]", R.drawable.f100);
		mFaceMap.put("[¼¤¶¯]", R.drawable.f101);
		mFaceMap.put("[½ÖÎè]", R.drawable.f102);
		mFaceMap.put("[Ï×ÎÇ]", R.drawable.f103);
		mFaceMap.put("[×óÌ«¼«]", R.drawable.f104);

		mFaceMap.put("[ÓÒÌ«¼«]", R.drawable.f105);
		mFaceMap.put("[±Õ×ì]", R.drawable.f106);
	}

}
