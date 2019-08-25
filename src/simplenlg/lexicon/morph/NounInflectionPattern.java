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

import java.util.ArrayList;
import java.util.List;

import simplenlg.features.Case;
import simplenlg.features.NumberAgr;
import simplenlg.features.WordMorph;

// this class is not currently used

public class NounInflectionPattern {

	String[] suffixSg;
	String[] suffixPl;
	
	List<WordMorph> features;
	
	public NounInflectionPattern() {
		this.suffixSg = new String[] { "", "", "", "" };
		this.suffixPl = new String[] { "", "", "", "" };
		this.features = new ArrayList<WordMorph>();
	}
	
	public NounInflectionPattern(String[] suffixSg, String[] suffixPl) {
		this();
		this.suffixSg = suffixSg;
		this.suffixPl = suffixPl;
	}
	
	public NounInflectionPattern(String[] suffixSg, String[] suffixPl,
			WordMorph... feats) {
		this(suffixSg, suffixPl);
		for (WordMorph f : feats) {
			this.features.add(f);
		}
	}
	
	
	public String apply(String stem, Case c, NumberAgr n) {
		String suffix = getSuffix(c, n);
		
		if (this.features.contains(WordMorph.FULL_FORMS)) {
			return suffix;
		}
		if (this.features.contains(WordMorph.STEM_E_ELISION) && !suffix.equals("")) {
			stem = elideFinalE(stem);
		}
		
		return stem + suffix;
	}
	
	
	
	private String getSuffix(Case c, NumberAgr n) {
		String[] suffixList = n == NumberAgr.SINGULAR ? suffixSg : suffixPl;
		
		switch (c) {
		case NOMINATIVE:
		default:
			return suffixList[0];
		case ACCUSATIVE:
			return suffixList[1];
		case GENITIVE:
			return suffixList[2];
		case DATIVE:
			return suffixList[3];
		}
	}
	
	private static final String elideFinalE(String base) {
		int i = base.lastIndexOf("e");
		if (i == -1) {
			return base; // no "e" character found
		} else {
			return (base.substring(0, i) + base.substring(i+1));
		}
	}

}
