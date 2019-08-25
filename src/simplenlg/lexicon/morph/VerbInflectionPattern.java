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

import simplenlg.features.Form;
import simplenlg.features.NumberAgr;
import simplenlg.features.Person;
import simplenlg.features.Tense;
import simplenlg.features.WordMorph;
import simplenlg.lexicon.lexicalitems.Verb;

public class VerbInflectionPattern {

	String   name;
	String[] sufPresInd;
	String[] sufPastInd;
	String[] sufPresSubj;
	String[] sufPastSubj;
	String[] sufImp;
	
	List<WordMorph> features;
	
	public VerbInflectionPattern() {
		this.name		 = "";
		this.sufPresInd  = new String[6];
		this.sufPastInd  = new String[6];
		this.sufPresSubj = new String[6];
		this.sufPastSubj = new String[6];
		this.sufImp      = new String[2];
		this.features    = new ArrayList<WordMorph>();
	}

	public VerbInflectionPattern(String name,
			String[] sufPresInd, String[] sufPastInd,
			String[] sufPresSubj, String[] sufPastSubj, String[] sufImp) {
		this();
		this.name = name;
		this.sufPresInd = sufPresInd;
		this.sufPastInd = sufPastInd;
		this.sufPresSubj = sufPresSubj;
		this.sufPastSubj = sufPastSubj;
		this.sufImp = sufImp;
	}

	public VerbInflectionPattern(String name, 
			String[] sufPresInd, String[] sufPastInd,
			String[] sufPresSubj, String[] sufPastSubj, String[] sufImp,
			WordMorph... featureList) {
		this(name, sufPresInd, sufPastInd, sufPresSubj, sufPastSubj, sufImp);
		for (WordMorph f : featureList) {
			this.features.add(f);
		}
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String apply(Verb v, Person p, NumberAgr n, Tense t, Form f) {
		String suffix = getSuffix(p, n, t, f);
		if (this.features.contains(WordMorph.FULL_FORMS)) {
			return suffix;
		}
		
		String stem = getStem(v, p, n, t, f);
		return addSuffix(stem, suffix);
	}
	
	
	private String getStem(Verb v, Person p, NumberAgr n, Tense t, Form f) {
		if (f == Form.IMPERATIVE) {
			return getImperativeStem(v, n);
		}
		else if (f == Form.SUBJUNCTIVE_II) {
			return v.getSubjunctiveStem();
		}
		
		switch (t) {
		default:
		case PRESENT:
			if (v.hasAlternatePresentStem() && n == NumberAgr.SINGULAR && f == Form.NORMAL &&
					(p != Person.FIRST || this.features.contains(WordMorph.MODAL_INFLECTION))) {
				return v.getAlternatePresentStem();					
			} else {
				return v.getPresentStem();
			}			
		case PAST:
			if (f == Form.SUBJUNCTIVE || f == Form.SUBJUNCTIVE_II) {
				return v.getSubjunctiveStem();
			} else {
				return v.getPastStem();
			}
		}
	}
	
	private String getImperativeStem(Verb v, NumberAgr n) {
		switch (n) {
		case SINGULAR:
		default:
			if (this.features.contains(WordMorph.ALTERNATE_IMPERATIVE)) {
				return v.getAlternatePresentStem();
			} else {
				return v.getPresentStem();
			}
		case PLURAL:
			return v.getPresentStem();
		}
	}
	
	public String getSuffix(Person p, NumberAgr n, Tense t, Form f) {
		return getSuffixArray(t, f)[getSuffixIndex(p, n, f)];
	}
	
	private String[] getSuffixArray(Tense t, Form f) {
		switch (f) {
		default:
		case NORMAL:
			switch (t) {
			case PRESENT:
			default:
				return this.sufPresInd;
			case PAST:
				return this.sufPastInd;
			}
		case SUBJUNCTIVE:
			switch (t) {
			case PRESENT:
			default:
				return this.sufPresSubj;
			case PAST:
				return this.sufPastSubj;
			}
		case SUBJUNCTIVE_II:
			return this.sufPastSubj;
		case IMPERATIVE:
			return this.sufImp;
		}
	}
	
	private int getSuffixIndex(Person p, NumberAgr n, Form f) {
		if (f == Form.IMPERATIVE) {
			return (n == NumberAgr.SINGULAR) ? 0 : 1;
		}
		
		int base = 0;
		if (n == NumberAgr.PLURAL) { base = 3; }
		
		switch (p) {
		case FIRST:
			return base;
		case SECOND:
			return base + 1;
		case THIRD:
		default:
			return base + 2;
		}
	}
	
	// Adds suffix to a verb form, respects "s contraction"
	private String addSuffix(String base, String suffix) {
		if (suffix.startsWith("s") && base.matches(".*[sßzx]$")) {
			return base.concat(suffix.substring(1));
		} else if (suffix.startsWith("e") && base.matches(".*e$")) {
			return base.concat(suffix.substring(1));
		} else if (suffix.startsWith("e") && this.features.contains(WordMorph.STEM_E_ELISION)) {
			return elideFinalE(base).concat(suffix);
		} else if (suffix.startsWith("t") && base.endsWith("t")) {
			return base.concat(suffix.substring(1));
		}
		
		return base.concat(suffix);
	}

	private static final String elideFinalE(String base) {
		int i = base.lastIndexOf("e");
		if (i == -1) {
			return base; // no "e" character found
		} else {
			return (base.substring(0, i) + base.substring(i+1));
		}
	}
	
	
	
	public void addFeature(WordMorph feature) {
		this.features.add(feature);
	}

	public VerbInflectionPattern clone(String newName, WordMorph... features) {
		VerbInflectionPattern newPattern = new VerbInflectionPattern(newName, sufPresInd, sufPastInd, sufPresSubj, sufPastSubj, sufImp, features);
		return newPattern;
	}

	public VerbInflectionPattern clone(String newName, String[] sufImp) {
		VerbInflectionPattern newPattern = new VerbInflectionPattern(newName, sufPresInd, sufPastInd, sufPresSubj, sufPastSubj, sufImp);
		return newPattern;
	}

}
