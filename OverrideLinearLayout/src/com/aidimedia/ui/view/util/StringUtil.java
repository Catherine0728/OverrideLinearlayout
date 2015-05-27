package com.aidimedia.ui.view.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Paint;


/*
 * Copyright (C) 2005-2010 TENCENT Inc.All Rights Reserved.
 * FileName：StringUtilities.java
 * Description�?
 * History�?
 * 1.0 jiayiyan 2010-3-23 Create
 */

public class StringUtil
{
	
	private static final String	TAG							= "StringUtil";
	
	/** 文本处理状�?机：普�?状�? */
	public static final int		TEXT_COMMON	= 0;

	/** 文本处理状�?机：特殊字符状�? */
	public static final int		TEXT_SPEC	= 1;

	/** 文本处理状�?机：特殊字符-数字状�? */
	public static final int		TEXT_NUM	= 2;

	/** 文本处理状�?机：特殊字符-数字-十六进制状�? */
	public static final int		TEXT_HEX	= 3;

	/** 文本处理状�?机：特殊字符-数字-十进制状�?*/
	public static final int		TEXT_DEC	= 4;

	/** 文本处理状�?机：特殊字符-数字-八进制状�?*/
	public static final int		TEXT_OCTAL	= 5;

	/** 文本处理状�?机：特殊字符-空格状�? */
	public static final int		TEXT_BLOCK	= 6;


	public static final float	K			= 1024;
	public static final float	M			= 1024 * 1024;
	public static final float	G			= 1024 * 1024 * 1024;
	private static final String	SIZE_BYTE	= "%dB";
	private static final String	SIZE_KB		= "%.2fK";
	private static final String	SIZE_MB		= "%.2fM";
	private static final String	SIZE_GB		= "%.2fG";
	// private static final String SIZE_UNKNOWN = "未知";
	private static final String	SPEED_BYTE	= "%dB/S";
	private static final String	SPEED_KB	= "%.2fK/S";
	private static final String	SPEED_MB	= "%.2fM/S";
	private static final String	SPEED_GB	= "%.2fG/S";

	/**
	 * 转换字符�?
	 * 1.将转义字符替换；
	 * 2.将特殊进制数值转换为十进制数�?
	 * 
	 * @param text
	 */
	public static String convertString(String text)
	{
		if (text == null)
		{
			return null;
		}

		int type = TEXT_COMMON;
		StringBuffer textBuffer = new StringBuffer();
		char[] charArr = text.toCharArray();
		int iter = 0;
		/* 数字位数 */
		int digitalCount = 0;
		/* 数字字符串的实际数�? */
		int value = 0;

		while (iter < charArr.length)
		{
			switch (type)
			{
				case TEXT_COMMON:
				{
					switch (charArr[iter])
					{
						case 0x09:/* Tab */
						case 0x0A:/* 换行 */
						case 0x0D:/* 回车 */
						case 0x20:/* 空格 */
							textBuffer.append(' ');
							type = TEXT_BLOCK;
							break;
						case '&':
							// TODO:从Kjava抄写，但是从代码来看，在解析16进制数�?的时候会出错，以后验�?
							type = TEXT_SPEC;
							break;
						default:
							textBuffer.append(charArr[iter]);
							break;
					}
					break;
				}
				case TEXT_SPEC:
				{
					if (charArr[iter] == '#')
					{
						type = TEXT_NUM;
						value = 0;
						digitalCount = 0;
					}
					else
					{
						/* &amp;转义�?&' */
						if (iter + 4 <= charArr.length && (charArr[iter] == 'a' || charArr[iter] == 'A')
								&& (charArr[iter + 1] == 'm' || charArr[iter + 1] == 'M') && (charArr[iter + 2] == 'p' || charArr[iter + 2] == 'P')
								&& charArr[iter + 3] == ';')
						{
							textBuffer.append("&");
							iter += 3;
						}
						/* &lt;转义�?<' */
						else if (iter + 3 <= charArr.length && (charArr[iter] == 'l' || charArr[iter] == 'L')
								&& (charArr[iter + 1] == 't' || charArr[iter + 1] == 'T') && charArr[iter + 2] == ';')
						{
							textBuffer.append("<");
							iter += 2;
						}
						/* &gt;转义�?>' */
						else if (iter + 3 <= charArr.length && (charArr[iter] == 'g' || charArr[iter] == 'G')
								&& (charArr[iter + 1] == 't' || charArr[iter + 1] == 'T') && charArr[iter + 2] == ';')
						{
							textBuffer.append(">");
							iter += 2;
						}
						/* &apos;转义�?'' */
						else if (iter + 5 <= charArr.length && (charArr[iter] == 'a' || charArr[iter] == 'A')
								&& (charArr[iter + 1] == 'p' || charArr[iter + 1] == 'P') && (charArr[iter + 2] == 'o' || charArr[iter + 2] == 'O')
								&& (charArr[iter + 3] == 's' || charArr[iter + 3] == 'S') && charArr[iter + 4] == ';')
						{
							textBuffer.append("'");
							iter += 4;
						}
						/* &quot;转义�?"' */
						else if (iter + 5 <= charArr.length && (charArr[iter] == 'q' || charArr[iter] == 'Q')
								&& (charArr[iter + 1] == 'u' || charArr[iter + 1] == 'U') && (charArr[iter + 2] == 'o' || charArr[iter + 2] == 'O')
								&& (charArr[iter + 3] == 't' || charArr[iter + 3] == 'T') && charArr[iter + 4] == ';')
						{
							textBuffer.append("\"");
							iter += 4;
						}
						/* &nbsp;转义�? ' */
						else if (iter + 5 <= charArr.length && (charArr[iter] == 'n' || charArr[iter] == 'N')
								&& (charArr[iter + 1] == 'b' || charArr[iter + 1] == 'B') && (charArr[iter + 2] == 's' || charArr[iter + 2] == 'S')
								&& (charArr[iter + 3] == 'p' || charArr[iter + 3] == 'P') && charArr[iter + 4] == ';')
						{
							textBuffer.append(" ");
							iter += 4;
						}
						/* &yen;转义�?�? */
						else if (iter + 4 <= charArr.length && (charArr[iter] == 'y' || charArr[iter] == 'Y')
								&& (charArr[iter + 1] == 'e' || charArr[iter + 1] == 'E') && (charArr[iter + 2] == 'n' || charArr[iter + 2] == 'N')
								&& charArr[iter + 3] == ';')
						{
//							textBuffer.append("�?);
							iter += 3;
						}
						/* &copy;转义为版权符�?*/
						else if (iter + 5 <= charArr.length && (charArr[iter] == 'c' || charArr[iter] == 'C')
								&& (charArr[iter + 1] == 'o' || charArr[iter + 1] == 'O') && (charArr[iter + 2] == 'p' || charArr[iter + 2] == 'P')
								&& (charArr[iter + 3] == 'y' || charArr[iter + 3] == 'Y') && charArr[iter + 4] == ';')
						{
							textBuffer.append("\u00A9");
							iter += 4;
						}
						/* &ldquo;转义为中文左引号'�? */
						else if (iter + 6 <= charArr.length
								&& //
								(charArr[iter] == 'l' || charArr[iter] == 'L') && (charArr[iter + 1] == 'd' || charArr[iter + 1] == 'D')
								&& (charArr[iter + 2] == 'q' || charArr[iter + 2] == 'Q') && (charArr[iter + 3] == 'u' || charArr[iter + 3] == 'U')
								&& (charArr[iter + 4] == 'o' || charArr[iter + 4] == 'O') && (charArr[iter + 5] == ';'))
						{
//							textBuffer.append("�?);
							iter += 5;
						}
						/* &rdquo;转义为中文右引号'�? */
						else if (iter + 6 <= charArr.length && (charArr[iter] == 'r' || charArr[iter] == 'R')
								&& (charArr[iter + 1] == 'd' || charArr[iter + 1] == 'D') && (charArr[iter + 2] == 'q' || charArr[iter + 2] == 'Q')
								&& (charArr[iter + 3] == 'u' || charArr[iter + 3] == 'U') && (charArr[iter + 4] == 'o' || charArr[iter + 4] == 'O')
								&& (charArr[iter + 5] == ';'))
						{
//							textBuffer.append("�?);
							iter += 5;
						}
						/* &uarr;转义�?�? */
						else if (iter + 5 <= charArr.length
								&& (charArr[iter] == 'u' || charArr[iter] == 'U') //
								&& (charArr[iter + 1] == 'a' || charArr[iter + 1] == 'A') && (charArr[iter + 2] == 'r' || charArr[iter + 2] == 'R')
								&& (charArr[iter + 3] == 'r' || charArr[iter + 3] == 'R') && (charArr[iter + 4] == ';'))
						{
//							textBuffer.append("�?);
							iter += 4;
						}
						/* &rarr;转义�?�? */
						else if (iter + 5 <= charArr.length
								&& (charArr[iter] == 'r' || charArr[iter] == 'R') //
								&& (charArr[iter + 1] == 'a' || charArr[iter + 1] == 'A') && (charArr[iter + 2] == 'r' || charArr[iter + 2] == 'R')
								&& (charArr[iter + 3] == 'r' || charArr[iter + 3] == 'R') && (charArr[iter + 4] == ';'))
						{
//							textBuffer.append("�?);
							iter += 4;
						}
						/* &darr;转义�?�? */
						else if (iter + 5 <= charArr.length
								&& //
								(charArr[iter] == 'd' || charArr[iter] == 'D') //
								&& (charArr[iter + 1] == 'a' || charArr[iter + 1] == 'A') && (charArr[iter + 2] == 'r' || charArr[iter + 2] == 'R')
								&& (charArr[iter + 3] == 'r' || charArr[iter + 3] == 'R') && (charArr[iter + 4] == ';'))
						{
//							textBuffer.append("�?);
							iter += 4;
						}
						/* &larr;转义�?�? */
						else if (iter + 5 <= charArr.length
								&& //
								(charArr[iter] == 'l' || charArr[iter] == 'L') //
								&& (charArr[iter + 1] == 'a' || charArr[iter + 1] == 'A') && (charArr[iter + 2] == 'r' || charArr[iter + 2] == 'R')
								&& (charArr[iter + 3] == 'r' || charArr[iter + 3] == 'R') && (charArr[iter + 4] == ';'))
						{
//							textBuffer.append("�?);
							iter += 4;
						}
						/* &trade;转义为版权TM标志 */
						else if (iter + 6 <= charArr.length
								&& (charArr[iter] == 't' || charArr[iter] == 'T') //
								&& (charArr[iter + 1] == 'r' || charArr[iter + 1] == 'R') && (charArr[iter + 2] == 'a' || charArr[iter + 2] == 'A')
								&& (charArr[iter + 3] == 'd' || charArr[iter + 3] == 'D') && (charArr[iter + 4] == 'e' || charArr[iter + 4] == 'e')
								&& (charArr[iter + 5] == ';'))
						{
							textBuffer.append("\u8482");
							iter += 5;
						}
						/* &ndash;转义�?�? */
						else if (iter + 6 <= charArr.length
								&& (charArr[iter] == 'n' || charArr[iter] == 'N') //
								&& (charArr[iter + 1] == 'd' || charArr[iter + 1] == 'D') && (charArr[iter + 2] == 'a' || charArr[iter + 2] == 'A')
								&& (charArr[iter + 3] == 's' || charArr[iter + 3] == 'S') && (charArr[iter + 4] == 'h' || charArr[iter + 4] == 'H')
								&& (charArr[iter + 5] == ';'))
						{
//							textBuffer.append("�?);
							iter += 5;
						}
						/* &mdash;转义�?�? */
						else if (iter + 6 <= charArr.length
								&& (charArr[iter] == 'm' || charArr[iter] == 'M') //
								&& (charArr[iter + 1] == 'd' || charArr[iter + 1] == 'D') && (charArr[iter + 2] == 'a' || charArr[iter + 2] == 'A')
								&& (charArr[iter + 3] == 's' || charArr[iter + 3] == 'S') && (charArr[iter + 4] == 'h' || charArr[iter + 4] == 'H')
								&& (charArr[iter + 5] == ';'))
						{
//							textBuffer.append("�?);
							iter += 5;
						}
						/* &rsaquo;转化为U+203A */
						else if (iter + 7 <= charArr.length
								&& (charArr[iter] == 'r' || charArr[iter] == 'R')//
								&& (charArr[iter + 1] == 's' || charArr[iter + 1] == 'S') && (charArr[iter + 2] == 'a' || charArr[iter + 2] == 'A')
								&& (charArr[iter + 3] == 'q' || charArr[iter + 3] == 'Q') && (charArr[iter + 4] == 'u' || charArr[iter + 4] == 'U')
								&& (charArr[iter + 5] == 'o' || charArr[iter + 5] == 'O') && (charArr[iter + 6] == ';'))
						{
							textBuffer.append("\u203A");
							iter += 6;
						}
						else
						{
							textBuffer.append(charArr[iter - 1]);
							textBuffer.append(charArr[iter]);
						}
						type = TEXT_COMMON;
					}
					break;
				}
				case TEXT_NUM:
				{
					if (charArr[iter] == 'x' || charArr[iter] == 'X')
					{
						type = TEXT_HEX;
					}
					else if (charArr[iter] == 'o' || charArr[iter] == 'O')
					{
						type = TEXT_OCTAL;
					}
					else if (charArr[iter] <= '9' && charArr[iter] >= '0')
					{
						type = TEXT_DEC;
						/* 十进制没有开头表示字符，�?��指针要回�?*/
						iter--;
					}
					break;
				}
				case TEXT_DEC:
				{
					if (charArr[iter] <= '9' && charArr[iter] >= '0' && digitalCount++ < 5)
					{
						/* �?���?5536 */
						// TODO:为什么需要限制最大�?�?
						value = value * 10 + (charArr[iter] - '0');
					}
					else if (charArr[iter] == ';')
					{
						if (value > 65535)
						{
						}
						else
						{
							textBuffer.append((char) value);
						}
						type = TEXT_COMMON;
					}
					else
					{
						textBuffer.append(charArr[iter]);
						type = TEXT_COMMON;
					}
					break;
				}
				case TEXT_HEX:
				{
					if (charArr[iter] <= '9' && charArr[iter] >= '0' && digitalCount++ < 4)
					{ // 16,max=65535
						value = value * 16 + (charArr[iter] - '0');
					}
					else if (charArr[iter] <= 'F' && charArr[iter] >= 'A' && digitalCount++ < 4)
					{
						value = value * 16 + (charArr[iter] - 'A' + 10);
					}
					else if (charArr[iter] <= 'f' && charArr[iter] >= 'a' && digitalCount++ < 4)
					{
						value = value * 16 + (charArr[iter] - 'a' + 10);
					}
					else if (charArr[iter] == ';')
					{
						if (value > 65535)
						{
							// PkgTools.println("can ignore error: number is over!");
						}
						else
						{
							textBuffer.append((char) value);
						}
						type = TEXT_COMMON;
					}
					else
					{
						textBuffer.append(charArr[iter]);
						type = TEXT_COMMON;
					}
					break;
				}
				case TEXT_OCTAL:// 八进�?
					if (charArr[iter] <= '7' && charArr[iter] >= '0' && digitalCount++ < 8)
					{ // 8,max=65535
						value = value * 8 + (charArr[iter] - '0');
					}
					else if (charArr[iter] == ';')
					{
						if (value > 65535)
						{
							// PkgTools.println("can ignore error: number is over!");
						}
						else
						{
							textBuffer.append((char) value);
						}
						type = TEXT_COMMON;
					}
					else
					{
						textBuffer.append(charArr[iter]);
						type = TEXT_COMMON;
					}
					break;
				case TEXT_BLOCK:
					/* 空格，包括普通空格，'\r' '\n'以及制表�?*/
					if (charArr[iter] != 0x20 && charArr[iter] != 0x0D && charArr[iter] != 0x0A && charArr[iter] != 0x09)
					{
						type = TEXT_COMMON;
						iter--;
					}
					break;
				default:
					break;
			}
			iter++;
		}
		return textBuffer.toString();
	}

	public static boolean isEmpty(String str)
	{
		return str == null || "".equals(str.trim());
	}

	/**
	 * Get data size string.
	 */
	public static String getSizeString(float size)
	{
		if (size < 0)
		{
			return "";//AppEngine.getInstance().getContext().getString(R.string.unknown);
		}
		else if (size < K)
		{
			return String.format(SIZE_BYTE, (int) size);
		}
		else if (size < M)
		{
			return String.format(SIZE_KB, size / K);
		}
		else if (size < G)
		{
			return String.format(SIZE_MB, size / M);
		}
		else
		{
			return String.format(SIZE_GB, size / G);
		}
	}
	
	public static String getSizeStringWithoutB(float size)
	{
		if (size < 0)
		{
			return "";//AppEngine.getInstance().getContext().getString(R.string.unknown);
		}
		else if (size < K)
		{
//			return String.format(SIZE_BYTE, (int) size);
			return "0K";
		}
		else if (size < M)
		{
			return String.format(SIZE_KB, size / K);
		}
		else if (size < G)
		{
			return String.format(SIZE_MB, size / M);
		}
		else
		{
			return String.format(SIZE_GB, size / G);
		}
	}

	/**
	 * Get data size string.
	 */
	public static String getSizeString(long size)
	{
		return getSizeString((float) size);
	}

	/**
	 * Get data speed string.
	 */
	public static String getSpeedString(float speed)
	{
		if (speed < 0)
		{
			return String.format(SPEED_BYTE, 0);
		}
		else if (speed < K)
		{
			return String.format(SPEED_BYTE, (int) speed);
		}
		else if (speed < M)
		{
			return String.format(SPEED_KB, speed / K);
		}
		else if (speed < G)
		{
			return String.format(SPEED_MB, speed / M);
		}
		else
		{
			return String.format(SPEED_MB, speed / G);
		}
	}

//	public static String getString(int resid)
//	{
//		Context context = AppEngine.getInstance().getContext();
//		return context.getString(resid);
//	}

//	public static String[] getStringArray(int resid)
//	{
//		Context context = AppEngine.getInstance().getContext();
//		return context.getResources().getStringArray(resid);
//	}

	/**
	 * 根据paint，width，截断text
	 * levijiang 2011-07-15
	 * 
	 * @param text
	 * @param paint
	 * @param width
	 * @return
	 */
	public static String textCutoff(final String text, Paint paint, int width)
	{
		if (isEmpty(text) || paint == null || width <= 0)
		{
			return text;
		}

		String dstText = text;
		float fontSize = paint.measureText(dstText);

		while (fontSize > width)
		{
			dstText = dstText.substring(0, dstText.length() - 1);
			fontSize = paint.measureText(dstText);
		}

		return dstText;
	}

	/**
	 * 将给定的两个数组合并，src+obj形式
	 * 
	 * @param src
	 * @param obj
	 * @return
	 */
	public static byte[] mergeByteData(byte[] src, byte[] obj)
	{
		if (src == null || src.length < 0)
			return obj;

		if (obj == null || obj.length < 0)
			return src;

		byte[] data = new byte[src.length + obj.length];
		System.arraycopy(src, 0, data, 0, src.length);
		System.arraycopy(obj, 0, data, src.length, obj.length);
		return data;
	}

	/**
	 * 从A标签中获取Title以及url，放在String[0]和String[1]�?
	 * 
	 * @param content
	 * @return
	 */
	public static String[] getAttributeFromTagA(String content)
	{
		if (isEmpty(content) || !content.contains("<a") || !content.contains("/a>"))
			return null;

		// 增加捕获，防止不标准的格式出�?
		try
		{
			int start = content.indexOf("<a");
			int end = content.indexOf("/a>");

			if (start != -1 && end != -1)
			{
				end += 3;

				// <a href="url">title</a>
				// 取出TagA
				String sTagA = content.substring(start, end);
				int urlStart = sTagA.indexOf("href=");
				if (urlStart <= 0)
					return null;
				String urlString = sTagA.substring(urlStart);
				int urlEnd = urlString.indexOf(">");
				if (urlEnd <= 0)
					return null;
				String[] attribute = { "", "" };
				attribute[0] = urlString.substring(6, urlEnd - 1);
				String titleString = urlString.substring(urlEnd);
				int titleEnd = titleString.indexOf("<");
				if (titleEnd <= 0)
					return null;
				attribute[1] = titleString.substring(1, titleEnd);
				return attribute;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将字节型数据转化�?6进制字符�?
	 */
	public static String byteToHexString(byte[] bytes)
	{
		StringBuffer buf = new StringBuffer(bytes.length * 2);

		for (int i = 0; i < bytes.length; i++)
		{
			if (((int) bytes[i] & 0xff) < 0x10)
			{
				buf.append("0");
			}
			buf.append(Long.toString((int) bytes[i] & 0xff, 16));
		}
		return buf.toString();
	}

	/**
	 * �?6进制字符串转化为字节型数�?
	 */
	public static byte[] hexStringToByte(String hexString)
	{
		if (hexString == null || hexString.equals("") || hexString.length() % 2 != 0)
		{
			return null;
		}
		byte[] bData = new byte[hexString.length() / 2];
		for (int i = 0; i < hexString.length(); i += 2)
		{
			bData[i / 2] = (byte) (Integer.parseInt(hexString.substring(i, i + 2), 16) & 0xff);
		}
		return bData;
	}

	public static byte[] md5Encrypt(byte[] byteArray)
	{
		byte[] retBytes = null;
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(byteArray);
			retBytes = md.digest();
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return retBytes;
	}

	/***
	 * It replace all "\r" and "\n" with " " from the argument, then return the fixed
	 * result.
	 * 
	 * @param text the string to be modified.
	 * @return the modified string
	 * @author Denverhan
	 */
	public static String removeNextLine(String text)
	{
		if (StringUtil.isEmpty(text))
		{
			return text;
		}

		Pattern p = Pattern.compile("\r|\n");
		Matcher m = p.matcher(text);
		String result = m.replaceAll(" ");

		return result;
	}
	
	/**
	 * restore%22to"
	 * @param text which is to be modfied
	 * @return the modified text.
	 */
	public static String restoreQuotation(String text)
	{
		if (StringUtil.isEmpty(text))
		{
			return text;
		}
		
		Pattern p = Pattern.compile("%22");
		Matcher m = p.matcher(text);
		String result = m.replaceAll("\"");
		
		return result;
	}
}
