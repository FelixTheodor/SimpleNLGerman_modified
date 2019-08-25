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

import simplenlg.features.AdjectiveDegree;
import simplenlg.features.AdjectiveType;
import simplenlg.features.Case;
import simplenlg.features.Gender;
import simplenlg.features.NumberAgr;
import simplenlg.lexicon.lexicalitems.Adjective;
import simplenlg.lexicon.lexicalitems.Constants;
import simplenlg.lexicon.lexicalitems.Determiner;
import simplenlg.lexicon.lexicalitems.Pronoun;
import simplenlg.realiser.NPPhraseSpec;

// TODO: Auto-generated Javadoc
/**
 * The Class AdjectiveInflection.
 */
public class AdjectiveInflection {

	public static final InflectionPattern<Adjective> WEAK_INFLECTION = new InflectionPattern<Adjective>(
			new String[] { "e",  "en", "en", "en" },
			new String[] { "e",  "e",  "en", "en" },
			new String[] { "e",  "e",  "en", "en" },
			new String[] { "en", "en", "en", "en" }
			);
	
	public static final InflectionPattern<Adjective> STRONG_INFLECTION = new InflectionPattern<Adjective>(
			new String[] { "er", "en", "en", "em" },
			new String[] { "e",  "e",  "er", "er" },
			new String[] { "es", "es", "en", "em" },
			new String[] { "e",  "e",  "er", "en" }
			);

	public static final InflectionPattern<Adjective> MIXED_INFLECTION = new InflectionPattern<Adjective>(
			new String[] { "er", "en", "en", "en" },
			new String[] { "e",  "e",  "en", "en" },
			new String[] { "es", "es", "en", "en" },
			new String[] { "en", "en", "en", "en" }
			);

	
	
	public static final String getForm(Adjective a, Case c, NumberAgr n, Gender g, AdjectiveType t, AdjectiveDegree d) {
		String stem;
		
		switch (d) {
		case POSITIVE:
		default:
			stem = a.getPositive();
			break;
		case COMPARATIVE:
			stem = a.getComparative();
			break;
		case SUPERLATIVE:
			stem = a.getSuperlative();
			break;
		}
		
		if (t == AdjectiveType.UNINFLECTED) {
			if (d == AdjectiveDegree.SUPERLATIVE) {
				return "am " + stem + "n";
			} else {
				return stem;
			}
		} else if (d == AdjectiveDegree.POSITIVE && a.isElideFinalE()) {
			stem = elideFinalE(stem); 
		}
		
		if (stem.matches(".*e$")) {
			stem = stem.substring(0, stem.length()-1);
		}
		
		switch (t) {
		case STRONG:
		default:
			return STRONG_INFLECTION.apply(stem, c, n, g);
		case WEAK:
			return WEAK_INFLECTION.apply(stem, c, n, g);
		case MIXED:
			return MIXED_INFLECTION.apply(stem, c, n, g);
		}
	}

	public static final AdjectiveType determineAdjectiveType(Object spec, boolean plural) {
		AdjectiveType t = AdjectiveType.STRONG;
		
		if (spec != null) {
			if (spec instanceof Determiner) {
				if (((Determiner) spec).getBaseForm().equals("ein") && plural) {
					t = AdjectiveType.STRONG;
				} else if (((Determiner) spec).getBaseForm().endsWith("ein")) {
					t = AdjectiveType.MIXED;
				} else {
					t = AdjectiveType.WEAK;
				}
			} else if (spec instanceof Pronoun) {
				t = AdjectiveInflection.determineAdjectiveType((Pronoun) spec);
			}
		}

		return t;
	}
	
	
	public static final AdjectiveType determineAdjectiveType(Pronoun pro) {
		String baseform = pro.getBaseForm();
		
		if (baseform.equals("derselbe") || baseform.equals("derjenige") ||
				baseform.equals("dies") || baseform.equals("jen") ||
				baseform.equals("jed")  || baseform.equals("jedwed") ||
				baseform.equals("jeglich") || baseform.equals("alle") ||
				baseform.equals("welch") ||
				baseform.equals("manch") || baseform.equals("solch") ||
				baseform.equals("beide")) {
			return AdjectiveType.WEAK;
		}
		else if (baseform.equals("ein") || baseform.equals("irgendein") ||
				baseform.equals("kein") || Constants.isPossessivePronoun(pro)) {
			return AdjectiveType.MIXED;
		}
		else {
			return AdjectiveType.STRONG;
		}

	}
	
	
	public static final String elideFinalE(String base) {
		int i = base.lastIndexOf("e");
		if (i == -1) {
			return base; // no "e" character found
		} else {
			return (base.substring(0, i) + base.substring(i+1));
		}
	}


}
