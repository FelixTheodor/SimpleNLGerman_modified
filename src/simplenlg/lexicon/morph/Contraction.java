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

import simplenlg.lexicon.lexicalitems.Determiner;
import simplenlg.lexicon.lexicalitems.Preposition;
import simplenlg.lexicon.lexicalitems.Pronoun;
import simplenlg.realiser.NPPhraseSpec;

public class Contraction {

	public static final boolean isObligatoryPrepContraction(Preposition p, NPPhraseSpec np) {
		Object o = np.getSpecifier();
		if (o == null || !(o instanceof Determiner)) return false;
		
		String det = ((Determiner) o).getForm(np.getCase(), np.getNumber(), np.getGender());
		String prep = p.getLeftForm();
		if (det.equals("dem")) {
			return (prep.equals("an") || prep.equals("bei") || prep.equals("in") ||
					prep.equals("von") || prep.equals("zu"));
		}
		if (det.equals("das")) {
			return (prep.equals("an") || prep.equals("in"));
		}
		if (det.equals("der")) {
			return (prep.equals("zu"));
		}
		
		return false;
	}
	
	public static final boolean isPossiblePrepContraction(Preposition p, NPPhraseSpec np) {
		String form;
		Object o = np.getSpecifier();
		if (o == null) return false;
		if (o instanceof Determiner) { // only definite determiner can contract
			if (((Determiner) o).getBaseForm().equals("der")) {
				form = ((Determiner) o).getForm(np.getCase(), np.getNumber(), np.getGender()); 
			} else {
				return false;
			}
		}
		else if (o instanceof Pronoun) { // only pronoun "derselbe" can contract
			if (((Pronoun) o).getBaseForm().equals("derselbe")) {
				form = ((Pronoun) o).getForm(np.getCase(), np.getNumber(), np.getGender());
			} else {
				return false;
			}
		}
		else { // no other object can contract
			return false;
		}
		
		String prep = p.getLeftForm();
		if (form.equals("dem")) { // || form.equals("demselben")) {
			return (prep.equals("an") || prep.equals("bei") || prep.equals("in") ||
					prep.equals("von") || prep.equals("zu") || prep.equals("hinter") ||
					prep.equals("über") || prep.equals("unter") || prep.equals("vor"));
		}
		if (form.equals("das")) { // || form.equals("dasselbe")) {
			return (prep.equals("an") || prep.equals("in") || prep.equals("auf") ||
					prep.equals("durch") || prep.equals("für") || prep.equals("hinter") ||
					prep.equals("über") || prep.equals("um") || prep.equals("unter") ||
					prep.equals("vor"));
		}
		if (form.equals("der")) { // || form.equals("derselbe")) {
			return (prep.equals("zu"));
		}
		if (form.equals("den")) { // || form.equals("denselben")) {
			return (prep.equals("hinter") || prep.equals("über") || prep.equals("unter"));
		}
		
		return false;
		
	}
		
	public static final String makePrepContraction(Preposition p, NPPhraseSpec np) {
		Object o = np.getSpecifier();
		if (o == null || !(o instanceof Determiner)) return "";
		
		String det = ((Determiner) o).getForm(np.getCase(), np.getNumber(), np.getGender());
		String prep = p.getLeftForm();
		
		if (det.equals("dem") && prep.endsWith("n")) {
			prep = prep.substring(0, prep.length() - 1);
		}

		return prep.concat(det.substring(det.length() - 1));
	}
	
}
