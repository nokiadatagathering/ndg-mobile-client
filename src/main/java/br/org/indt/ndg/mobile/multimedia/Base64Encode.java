package br.org.indt.ndg.mobile.multimedia;

public class Base64Encode 
{
	private static char[] map1 = new char[64];
	
	static 
	{
		int i = 0;
		
		for (char c = 'A'; c <= 'Z'; c++) 
		{
			map1[i++] = c;
		}
		
		for (char c = 'a'; c <= 'z'; c++) 
		{
			map1[i++] = c;
		}
		
		for (char c = '0'; c <= '9'; c++) 
		{
			map1[i++] = c;
		}
		
		map1[i++] = '+';
		map1[i++] = '/';
	}
	
	public static String base64Encode(byte[] in)
	{
		int iLen = in.length;
		int oDataLen = (iLen * 4 + 2) / 3; // output length without padding
		int oLen = ((iLen + 2) / 3) * 4; // output length including padding

		char[] out = new char[oLen];

		int ip = 0;
		int op = 0;

		int i0;
		int i1;
		int i2;
		int o0;
		int o1;
		int o2;
		int o3;

		while (ip < iLen)
		{
			i0 = in[ip++] & 0xff;
			i1 = ip < iLen ? in[ip++] & 0xff : 0;
			i2 = ip < iLen ? in[ip++] & 0xff : 0;
			
			o0 = i0 >>> 2;			
			o1 = ((i0 & 3) << 4) | (i1 >>> 4);
			o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);
			o3 = i2 & 0x3F;
			
			out[op++] = map1[o0];
			out[op++] = map1[o1];
			out[op] = op < oDataLen ? map1[o2] : '=';
			op++;
			out[op] = op < oDataLen ? map1[o3] : '=';
			op++;
		}

		return new String(out);
	}
}