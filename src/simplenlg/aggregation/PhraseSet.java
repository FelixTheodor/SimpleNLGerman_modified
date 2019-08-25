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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import simplenlg.features.DiscourseFunction;
import simplenlg.realiser.HeadedPhraseSpec;
import simplenlg.realiser.Phrase;
import simplenlg.realiser.StringPhraseSpec;

/**
 * This class wraps an ordered list of phrases which are constituents of two or
 * more (different) clauses and have the same discourse function in their parent
 * clause. FunctionPairs are used by {@link EllipsisRule}s to collect candidate
 * phrase for elision.
 * 
 * @author agatt
 * 
 */
public class PhraseSet {

	private DiscourseFunction function;
	private List<Phrase> phrases;
	List<EllipsisRule> leftMarkers, rightMarkers;

	/**
	 * Construct a set of compatible phrases and their function
	 * 
	 * @param function
	 *            their function
	 * @param phrases
	 *            the list of constituent phrases for the function.
	 */
	public PhraseSet(DiscourseFunction function, Phrase... phrases) {
		this.function = function;
		this.phrases = new ArrayList<Phrase>(Arrays.asList(phrases));
	}

	/**
	 * Add a phrase
	 * 
	 * @param phrase
	 *            the phrase to add
	 */
	public void addPhrase(Phrase phrase) {
		this.phrases.add(phrase);
	}

	/**
	 * Add a collection of phrases.
	 * 
	 * @param phrases
	 *            the phrases to add
	 */
	public void addPhrases(Collection<Phrase> phrases) {
		this.phrases.addAll(phrases);
	}

	/**
	 * 
	 * @return the function the pair of phrases have in their respective clauses
	 */
	public DiscourseFunction getFunction() {
		return this.function;
	}

	/**
	 * Elide the rightmost constituents in the phrase list, that is, all phrases
	 * except the first.
	 */
	public void elideRightmost() {
		for (int i = 1; i < this.phrases.size(); i++) {
			Phrase phrase = this.phrases.get(i);

			if (phrase != null) {
				phrase.setElided(true);
			}
		}
	}

	/**
	 * Elide the leftmost consitutents in the phrase list, that is, all phrases
	 * except the rightmost.
	 */
	public void elideLeftmost() {
		for (int i = this.phrases.size() - 2; i >= 0; i--) {
			Phrase phrase = this.phrases.get(i);

			if (phrase != null) {
				phrase.setElided(true);
			}
		}
	}

	/**
	 * Check whether the phrases are lemma identical. This method returns
	 * <code>true</code> in the following cases:
	 * 
	 * <OL>
	 * <LI>All phrases are {@link simplenlg.realiser.StringPhraseSpec}s and they
	 * have identical String content</LI>
	 * <LI>All phrases are {@link simplenlg.realiser.HeadedPhraseSpec}s and they
	 * have the same lexical head, irrespective of inflectional variations.</LI>
	 * </OL>
	 * 
	 * @return <code>true</code> if the pair is lemma identical
	 */
	public boolean lemmaIdentical() {
		boolean ident = !this.phrases.isEmpty();

		for (int i = 1; i < this.phrases.size() && ident; i++) {
			Phrase left = this.phrases.get(i - 1);
			Phrase right = this.phrases.get(i);

			if (left != null && right != null) {
				if (left instanceof StringPhraseSpec) {
					ident = ((StringPhraseSpec) left).getString().equals(
							((StringPhraseSpec) right).getString());
				} else if (left instanceof HeadedPhraseSpec<?>) {
					ident = ((HeadedPhraseSpec<?>) left).getHead().equals(
							((HeadedPhraseSpec<?>) right).getHead());
				}
			}
		}

		return ident;
	}

	/**
	 * Check whether the phrases in this set are identical in form. This method
	 * returns true if for every pair of phrases <code>p1</code> and <p2>,
	 * <code>p1.equals(p2)</code>.
	 * 
	 * @return <code>true</code> if all phrases in the set are form-identical
	 */
	public boolean formIdentical() {
		boolean ident = !this.phrases.isEmpty();

		for (int i = 1; i < this.phrases.size() && ident; i++) {
			Phrase left = this.phrases.get(i - 1);
			Phrase right = this.phrases.get(i);

			if (left != null && right != null) {
				if (left instanceof StringPhraseSpec) {
					ident = ((StringPhraseSpec) left).getString().equals(
							((StringPhraseSpec) right).getString());
				} else if (left instanceof HeadedPhraseSpec<?>) {
					ident = ((HeadedPhraseSpec<?>) left).equals(right);
				}
			}
		}

		return ident;
	}
}
