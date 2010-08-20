/**
 * @file   Conversor.java
 * @author Adenilson Cavalcanti <adedasil@axon2>
 * @date   Tue Jul  3 07:35:14 2007
 *
 * @brief  This class handles conversion from Unicode strings
 * to XML representation.
 *
 */
package br.org.indt.ndg.mobile.xmlhandle;

import java.util.Hashtable;

/** Helper class, will contain some constants and code for XML
 * to unicode processing.
 */
class Xmlhelper {
	/** This array holds XML entity references.
	 *
	 * I was thinking about to use a hashmap, but it turned out to
	 * be more difficult to access both the key + replace string
	 * in function \ref XML2entity. Using arrays is cleaner in this
	 * specific case.
	 */
	protected String [] xml_er;
	/** This array holds unicode entity references */
	protected String [] uni_er;

	/** Token marker for XML unicode symbols */
	protected String uni_token = new String("&#");

	/** Token to mark end of special symbol */
	protected String end_token = new String(";");

	/** Helper string buffer, used for string replacement */
	protected StringBuffer str_buffer;

	/** Default constructor, initialize hashmap */
	public Xmlhelper() {

		this.xml_er = new String[5];
		this.uni_er = new String[5];

		xml_er[0] = "&lt;";
		uni_er[0] = "<";

		xml_er[1] = "&gt;";
		uni_er[1] = ">";

		xml_er[2] = "&amp;";
		uni_er[2] = "&";

		xml_er[3] = "&quot;";
		uni_er[3] = "\"";

		xml_er[4] = "&apos;";
		uni_er[4] = "\'";

 		this.str_buffer = new StringBuffer();
	}

	/** Internal use only function, replaces in String object substring
	 * for a new substring. I'm not sure if regex are supported in J2ME.
	 *
	 * @param str Original string.
	 * @param pattern Substring to be replaced.
	 * @param replace New substring.
	 *
	 * @return Newly string with pattern replaced.
	 */
	private String replace(String str, String pattern, String replace) {
		//Clean up stringbuffer
		if (this.str_buffer.length() > 0)
			this.str_buffer.delete(0, str_buffer.length());

		int start = 0;
		int end = 0;
		if ((end = str.indexOf(pattern, start)) >= 0) {
			this.str_buffer.append(str.substring(start, end));
			this.str_buffer.append(replace);
			start = end + pattern.length();
		}
		this.str_buffer.append(str.substring(start));
		return this.str_buffer.toString();
	}

	/** Converts a XML with unicode symbols back to normal
	 * string format.
	 *
	 * @param text A XML string with unicode symbols (e.g.
	 * "Jos&#233;"
	 *
	 * @return A normal unicode string or null in error case.
	 *
	 */
	public String XML2uni(String text) {

		String result = null;
		String tmp_sequence;
		StringBuffer tmp_unicode = new StringBuffer();
		int pos1 = 0, pos2 = 0;

		while ((pos1 = text.indexOf(this.uni_token, pos1)) >= 0) {
			pos2 = text.indexOf(this.end_token, pos1);

			tmp_sequence = text.substring(pos1 +
						    this.uni_token.length(),
					     pos2);

			tmp_unicode.append((char)Integer.parseInt(
						   tmp_sequence));
			tmp_sequence = text.substring(pos1, pos2 + 1);
			text = this.replace(text, tmp_sequence,
					    tmp_unicode.toString());

			pos1++;
			tmp_unicode.delete(0, 1);
		}
		result = text;

		return result;

	}

	/** Converts a XML with scaped entity elements back to normal
	 * unicode characters (<, >, ", ').
	 *
	 * @param text A XML string with entity elements (e.g. &quot)
	 *
	 * @return A normal unicode string  or null in error case.
	 *
	 */
	public String XML2entity(String text) {
		String result = null;
		String tmp_sequence;
		int pos1 = 0;

		for (int i = 0; i < this.xml_er.length; ++i) {
			while ((pos1 = text.indexOf(xml_er[i], pos1)) >= 0) {

				text = this.replace(text, xml_er[i],
						    uni_er[i]);
			}
			result = text;
		}

		return result;
	}
}

/** Helper class, will contain some constants and code for unicode
 * to XML processing.
 */
class Unihelper {

	/** Scape sequence for XML unicode/special characters */
	String escape = new String("&#");

	/** Closing sequence for XML unicode/special characters */
	String closing = new String(";");

	/** Temporary string, helps to create output sequences */
	String buff = new String();

	/** Upper limit for normal ASCII codes. Any value over this
	 * is a unicode character.
	 */
	protected int ascii_limit = 127;

	/** This hash holds unicode to XML entity references
	 * TODO: check if J2ME supports Java 1.5 generics.
	 */
	//protected Map<Character, String> entity_reference;
	/* FIXME: this will be different for J2ME */
	protected Hashtable uni_entity_reference;

	/** Default constructor, initializes hashmaps */
	public Unihelper() {

		/* TODO: check if J2ME supports Java 1.5 generics. */
		//this.entity_reference =  new HashMap<Character, String>();
		/* FIXME: this will be different for J2ME */
		this.uni_entity_reference = new Hashtable();

		uni_entity_reference.put(new String("<"), new String("&lt;"));
		uni_entity_reference.put(new String(">"), new String("&gt;"));
		uni_entity_reference.put(new String("&"), new String("&amp;"));
		/* TODO: how to handle ']]' */
		//uni_entity_reference.put('\'', "&gt");

	}

	/** Use this function to test if a character is a Unicode
	 * symbol.
	 *
	 * @param character A character from a string.
	 *
	 * @return True for unicode, false otherwise.
	 */
	public boolean isUnicode(char character) {
		int tmp = (int) character;
		if (tmp > this.ascii_limit)
			return true;
		return false;
	}

	/** Converts a unicode character value to its scaped XML
	 * valid value.
	 *
	 * @param character A unicode character, see \ref isUnicode.
	 *
	 * @return A string with XML value or null in error case.
	 */
	public String uni2XML(char character) {
		int tmp;
		/* FIXME: WTF this doesn't work? */
// 		tmp = Character.getNumericValue(character);
// 		if (tmp == -1)
// 			return null;
		tmp = (int) character;

		this.buff = this.escape + tmp + this.closing;
		return this.buff;
	}


	/** Use this function to test if a character is special XML
	 * reference entity symbol.
	 *
	 * @param character A character from a string.
	 *
	 * @return True for entity, false otherwise.
	 */
	public boolean isXMLentity(char character) {
		return this.uni_entity_reference.containsKey(String.valueOf(character));
	}

	/** Use this function to convert a special XML entity reference
	 * (>, <, &, ", ') to scaped sequence.
	 *
	 * @param character A character from a string.
	 *
	 * @return A string with scaped sequence or null in error case.
	 */
	public String entity2XML(char character) {
		/* TODO: with generics, I don't need this casting! */
		return (String)this.uni_entity_reference.get(String.valueOf(character));
	}
}

/** Class to convert from Unicode characters to XML */
public class Conversor {

	/** Unicode to XML helper object */
	protected Unihelper uni_ref;

	/** XML to Unicode helper object */
	protected Xmlhelper xml_ref;

	/** Default constructor */
	public Conversor() {
		this.uni_ref = new Unihelper();
		this.xml_ref = new Xmlhelper();
	}

	/** Use this function to convert a unicode string with
	 * accents to XML representation.
	 *
	 * @param unicode Original string.
	 *
	 * @return Converted string or null in error.
	 */
	public String uni2xml(String unicode) {
		StringBuffer res = new StringBuffer();
		char tmp;

		for (int i = 0; i < unicode.length(); ++i) {
			tmp = unicode.charAt(i);
			if (uni_ref.isUnicode(tmp)) {
				res.append(uni_ref.uni2XML(tmp));
			} else if (uni_ref.isXMLentity(tmp)) {
				res.append(uni_ref.entity2XML(tmp));

			} else
				res.append(tmp);
		}

		return res.toString();
	}
        
	/** Use this function to convert a unicode string with
	 * XML characteres to XML representation.
	 *
	 * @param unicode Original string.
	 *
	 * @return Converted string or null in error.
	 */
        public String uni2xmlEntity(String unicode) {
                StringBuffer res = new StringBuffer();
		char tmp;

		for (int i = 0; i < unicode.length(); ++i) {
			tmp = unicode.charAt(i);
			if (uni_ref.isXMLentity(tmp)) {
				res.append(uni_ref.entity2XML(tmp));

			} else
				res.append(tmp);
		}

		return res.toString();
        }

	/** Use this function to convert a XML string with accent
	 * characters back to Unicode.
	 *
	 * @param xml String with scaped characters.
	 *
	 * @return Converted unicode string or null in error.
	 */
	public String xml2uni(String xml) {
		String res = this.xml_ref.XML2uni(xml);
		return this.xml_ref.XML2entity(res);
	}

}
