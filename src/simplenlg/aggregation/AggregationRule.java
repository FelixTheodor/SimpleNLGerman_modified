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

import java.util.Collection;

import simplenlg.realiser.CoordinateSPhraseSpec;
import simplenlg.realiser.SPhraseSpec;

/**
 * This class represents an aggregation rule. All such rules need to implement
 * an {@link #apply(SPhraseSpec...)} which takes an arbitrary number of
 * {@link simplenlg.realiser.SPhraseSpec}s and perform some form of aggregation
 * on them, returning an <code>SPhraseSpec</code> as a result, or
 * <code>null</code> if the operation fails.
 * 
 * @author agatt
 * 
 */
public abstract class AggregationRule {

	/**
	 * The main method in this class. Performs aggregation on an arbitrary
	 * number of sentences. The suggested way to implement this method in is to:
	 * (a) only perform aggregation if it is possible to do so on <I>all</I> the
	 * sentences supplied as parameters; (b) in case
	 * <code>sentences.length == 1</code>, return <code>sentences[0]</code>; (c)
	 * return <code>null</code> if <code>sentences.length == 0</code> or the
	 * operation can't be performed on all the supplied sentences..
	 * 
	 * @param sentences
	 *            the sentences
	 * @return the resulting sentence, if successful; <code>null</code>
	 *         otherwise
	 */
	public abstract SPhraseSpec apply(SPhraseSpec... sentences);

	/**
	 * Performs the same operation as {@link #apply(SPhraseSpec...)}, on the
	 * coordinates making up a coordinate clause.
	 * 
	 * <P>
	 * <strong>Implementation note:</strong>: this method involves a call to
	 * {@link #apply(SPhraseSpec...)}, supplying the components of the
	 * coordinate clause; new extensions of this class need not implement this
	 * method.
	 * </P>
	 * 
	 * @param sentence
	 *            the coordinate clause
	 * @return the result, if {@link #apply(SPhraseSpec...)} is successful
	 */
	public SPhraseSpec apply(CoordinateSPhraseSpec sentence) {
		Collection<SPhraseSpec> coords = sentence.getCoordinates();
		return apply(coords.toArray(new SPhraseSpec[coords.size()]));
	}

}
