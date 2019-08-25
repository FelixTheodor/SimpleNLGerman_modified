/* ==================================================
 * SimpleNLG-Ger: An Adaption of SimpleNLG for German
 * ==================================================
 * 
 * Copyright (c) 2013, Marcel Bollmann
 * All rights reserved.
 * 
 * This package is a modification of "SimpleNLG: An API for Natural Language Generation".
 * The license of the original software is reproduced below and also applies to this
 * modification.
 * 
 * ==================================================
 * SimpleNLG: An API for Natural Language Generation
 * ==================================================
 *
 * Copyright (c) 2007, the University of Aberdeen
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted FOR RESEARCH PURPOSES ONLY, provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 * 		this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation and/or 
 *    other materials provided with the distribution.
 * 3. Neither the name of the University of Aberdeen nor the names of its contributors 
 * 	  may be used to endorse or promote products derived from this software without 
 *    specific prior written permission.
 *    
 *    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 *    AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 *    THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 *    ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE 
 *    FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 *    (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *     LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 *     ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 *     (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 *     EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *     
 *  Redistribution and use for purposes other than research requires special permission by the
 *  copyright holders and contributors. Please contact Ehud Reiter (ereiter@csd.abdn.ac.uk) for
 *  more information.
 *     
 *	   =================    
 *     Acknowledgements:
 *     =================
 *     This library contains a re-implementation of some rules derived from the MorphG package
 *     by Guido Minnen, John Carroll and Darren Pearce. You can find more information about MorphG
 *     in the following reference:
 *     	Minnen, G., Carroll, J., and Pearce, D. (2001). Applied Morphological Processing of English.
 *     		Natural Language Engineering 7(3): 207--223.
 *     Thanks to John Carroll (University of Sussex) for permission to re-use the MorphG rules. 
 */

package simplenlg.lexicon.morph;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class BasicPatterns.
 */
public class BasicPatterns {

	/** The Constant C. */
	public static final String C = "[bcdfghjklmnpqrstvwxyz]";

	/** The Constant V. */
	public static final String V = "[aeiou]";

	/** The Constant VL. */
	public static final String VL = "[lraeiou]";

	/** The Constant VY. */
	public static final String VY = "[yaeiou]";

	/** The Constant VX. */
	public static final String VX = "[xaeiou]";

	/** The Constant ANY_STEM. */
	public static final String ANY_STEM = "^(([a-zA-ZÄÖÜäöüß_0-9]+)(-[a-zA-ZÄÖÜäöüß_0-9]+)*)(\\s(([a-zA-ZÄÖÜäöüß_0-9]+)(-[a-zA-ZÄÖÜäöüß_0-9]+)*))*$";

	/** The Constant SYLLABLE. */
	public static final String SYLLABLE = "((" + BasicPatterns.C + "{1,3})("
			+ BasicPatterns.VY + "+)(" + BasicPatterns.C + "{0,2}))";

	/** The Constant SYLLABLE_Y. */
	public static final String SYLLABLE_Y = "((" + BasicPatterns.C + "{1,3})y)";

	/** The Constant SYLLABLE_E. */
	public static final String SYLLABLE_E = "((" + BasicPatterns.C + "{1,3})e)";

	/** The Constant VERBAL_PREFIX. */
	public static final String VERBAL_PREFIX = "((be|with|pre|un|over|re|mis|under|out|up|fore|for|counter|co|sub)(-?))";
	
	/** The Constant LAST_VOWEL, used for stem umlaut. */
	public static final String LAST_VOWEL = "(Au|Aa|au|aa|[aouAOU])[^aouAOU]*$";
	
	public static final String applyStemUmlaut(String word) {
		Pattern p = Pattern.compile(BasicPatterns.LAST_VOWEL);
		Matcher m = p.matcher(word);
		StringBuilder s = new StringBuilder(word);
		
		if (m.find()) {
			s.replace(m.start(1), m.end(1), createUmlaut(m.group(1)));
		}
		
		return s.toString();
	}
	
	private static final String createUmlaut(String vowel) {
		String umlaut = vowel;
		if (vowel.equals("a")) { umlaut = "ä"; }
		else if (vowel.equals("A")) { umlaut = "Ä"; }
		else if (vowel.equals("o")) { umlaut = "ö"; }
		else if (vowel.equals("O")) { umlaut = "Ö"; }
		else if (vowel.equals("u")) { umlaut = "ü"; }
		else if (vowel.equals("U")) { umlaut = "Ü"; }
		else if (vowel.equals("au")) { umlaut = "äu"; }
		else if (vowel.equals("Au")) { umlaut = "Äu"; }
		else if (vowel.equals("aa")) { umlaut = "ä"; }
		else if (vowel.equals("Aa")) { umlaut = "Ä"; }
		
		return umlaut;
	}
	
	
}
