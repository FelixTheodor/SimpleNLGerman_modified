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
package simplenlg.aggregation;

import simplenlg.features.DiscourseFunction;
import simplenlg.features.Tense;
import simplenlg.realiser.CoordinateVPPhraseSpec;
import simplenlg.realiser.Phrase;
import simplenlg.realiser.SPhraseSpec;
import simplenlg.realiser.VPPhraseSpec;

/**
 * Implementation of a clausal coordination rule. The rule performs the
 * following operations on sentences:
 * 
 * <OL>
 * <LI>If the sentences have the same subject, a new sentence is returned with
 * that subject, and the VP from the component sentences conjoined</LI>
 * <LI>If the sentences have the same VP, a new sentence is returned with that
 * VP, and the subjects from the component sentences conjoined</LI>
 * </OL>
 * 
 * <P>
 * These operations only apply to sentences whose front modifiers are identical,
 * that is, sentences where, for every pair <code>s1</code> and <code>s2</code>,
 * <code>s1.getFrontModifiers().equals(s2.getFrontModifiers())</code>.
 * 
 * @author agatt
 */
public class ClauseCoordination extends CoordinationRule {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SPhraseSpec apply(SPhraseSpec... sentences) {
		SPhraseSpec result = null;
		SPhraseSpec s1 = sentences[0];
		Tense t = s1.getTense();
		boolean perf = s1.isPerfect();
		boolean pass = s1.isPassive();
		boolean prog = s1.isProgressive();

		if (PhraseChecker.areNotCoordinate(sentences)) {

			if (PhraseChecker.sameSentences(sentences)) {
				result = sentences[0];

			} else if (PhraseChecker.sameFrontMods(sentences)
					&& PhraseChecker.sameSurfaceSubjects(sentences)) {

				if (!PhraseChecker.sameVPArgs(sentences)
						&& PhraseChecker.sameVPHead(sentences)
						&& PhraseChecker.sameVPModifiers(sentences)) {

					VPPhraseSpec vp = new VPPhraseSpec();
					vp.setHead(sentences[0].getHead());
					result = new SPhraseSpec();

					for (SPhraseSpec s : sentences) {
						for (Phrase comp : s.getComplements()) {
							DiscourseFunction function = comp.getDiscourseFunction();
							vp.addComplement(comp, function);
						}
					}

					result.setVerbPhrase(vp);

					for (Phrase pre : sentences[0].getPremodifiers()) {
						vp.addPremodifier(pre);
					}

					for (Phrase post : sentences[0].getPostmodifiers()) {
						vp.addPostmodifier(post);
					}

				} else if (PhraseChecker.areNotExistential(sentences)) {
					CoordinateVPPhraseSpec cvp = new CoordinateVPPhraseSpec();
					result = new SPhraseSpec();

					for (SPhraseSpec s : sentences) {
						cvp.addCoordinates(s.getVerbPhrase());
					}

					cvp.aggregateAuxiliary(true);
					result.setVerbPhrase(cvp);
				}

				for (Phrase subj : sentences[0].getSubjects()) {
					result.addSubject(subj);
				}

				for (Phrase front : sentences[0].getFrontModifiers()) {
					result.addFrontModifier(front);
				}

				if (sentences[0].hasCuePhrase()) {
					result.setCuePhrase(sentences[0].getCuePhrase());
				}

			} else if (PhraseChecker.sameVP(sentences)) {
				result = new SPhraseSpec();

				for (SPhraseSpec s : sentences) {
					for (Phrase f : s.getFrontModifiers()) {
						result.addFrontModifier(f);
					}

					for (Phrase subj : s.getSubjects()) {
						result.addSubject(subj);
					}
				}

				result.setVerbPhrase(sentences[0].getVerbPhrase());
			}

			// need to reset all VP features
			if (result != null) {
				result.setTense(t);
				result.setPassive(pass);
				result.setPerfect(perf);
				result.setProgressive(prog);
			}
		}

		return result;

	}
}
