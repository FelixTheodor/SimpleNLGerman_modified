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
package simplenlg.lexicon.lexicalitems;

import java.util.Arrays;
import java.util.List;

import simplenlg.features.Category;
import simplenlg.features.Feature;

/**
 * Enumeration of possible derivational relations that can exist between lexical
 * items, such as nominalisation. Since this enum implements
 * {@link simplenlg.features.Feature}, every relation can be queried for the
 * categories it applies to. For example, a <I>nominalisation</I> relation only
 * applies to adjectives or verbs.
 * <P>
 * 
 * Every derivational relation can additionally be queried for the inverse
 * relation. For example, the inverse of a {@link #NOMINALISATION} relation is
 * {@link #NOMINALISES}. Whereas the former expresses the relation between an
 * adjective/verb and the noun that nominalises it, the latter expresses the
 * inverse relation between the noun and the adjective/verb.
 * 
 * @author agatt
 * @since version 3.8
 * 
 */
public enum DerivationalRelation implements Feature {

	/**
	 * The relation that holds between a lexical item (adjective or verb) and
	 * the noun that nominalises it.
	 */
	NOMINALISATION(Category.ADJECTIVE, Category.VERB),

	/**
	 * The inverse of {@link #NOMINALISATION}
	 */
	NOMINALISES(Category.NOUN);

	private List<Category> categories;

	DerivationalRelation(Category... categories) {
		this.categories = Arrays.asList(categories);
	}

	public boolean appliesTo(Category cat) {
		return this.categories.contains(cat);
	}

	public DerivationalRelation getInverse() {
		DerivationalRelation inverse = null;

		switch (this) {
		case NOMINALISATION:
			inverse = NOMINALISES;
			break;
		case NOMINALISES:
			inverse = NOMINALISATION;
			break;
		}

		return inverse;
	}

}
