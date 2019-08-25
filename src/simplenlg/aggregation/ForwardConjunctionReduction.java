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

import java.util.List;

import simplenlg.realiser.CoordinateSPhraseSpec;
import simplenlg.realiser.SPhraseSpec;

/**
 * <P>
 * Implementation of the forward conjunction rule. Given two sentences
 * <code>s1</code> and <code>s2</code>, this rule elides any constituent in the
 * left periphery of <code>s2</code> which is also in <code>s1</code>. The left
 * periphery in simplenlg is roughly defined as the subjects, front modifiers
 * and cue phrase of an {@link simplenlg.realiser.SPhraseSpec}.
 * </P>
 * 
 * <P>
 * This rule elides any constituent in the left periphery of <code>s2</code>
 * which is <I>lemma-identical</I> to a constituent with the same function in
 * <code>s1</code>, that is, it is headed by the same lexical item, though
 * possibly with different inflectional features and/or modifiers. Note that
 * this means that <I>the tall man</I> and <I>the man</I> are counted as
 * "identical" for the purposes of this rule. This is only justifiable insofar
 * as the two NPs are co-referential. Since SimpleNLG does not make assumptions
 * about the referentiality (or any part of the semantics) of phrases, it is up
 * to the developer to ensure that this is always the case.
 * </P>
 * 
 * <P>
 * The current implementation is loosely based on the algorithm in Harbusch and
 * Kempen (2009), which is described here:
 * 
 * <a href="http://aclweb.org/anthology-new/W/W09/W09-0624.pdf">
 * http://aclweb.org/anthology-new/W/W09/W09-0624.pdf</a>
 * </P>
 * 
 * <P>
 * <strong>Implementation note:</strong> The current implementation only applies
 * ellipsis to phrasal constituents (i.e. not to their component lexical items).
 * </P>
 * 
 * @author agatt
 * 
 */
public class ForwardConjunctionReduction extends EllipsisRule {

	/**
	 * Creates a new <code>ForwardConjunctionReduction</code>.
	 */
	public ForwardConjunctionReduction() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SPhraseSpec apply(SPhraseSpec... sentences) {
		boolean success = false;

		if (notPassive(sentences)) {
			List<PhraseSet> leftPeriphery = PhraseChecker
					.leftPeriphery(sentences);

			for (PhraseSet pair : leftPeriphery) {

				if (pair.formIdentical()) {
					success = true;
					pair.elideRightmost();
				}
			}
		}

		return success ? new CoordinateSPhraseSpec(sentences) : null;
	}

}
