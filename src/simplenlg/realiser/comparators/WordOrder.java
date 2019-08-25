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

package simplenlg.realiser.comparators;

import java.util.Arrays;
import java.util.List;

import simplenlg.features.Position;

public enum WordOrder {

	SIO (Arrays.asList(
			Position.VORFELD,
			Position.FRONT,
			Position.PRE_SUBJECT,
			Position.SUBJECT,
			Position.POST_SUBJECT,
			Position.PRE_INDIRECT_OBJECT,
			Position.INDIRECT_OBJECT,
			Position.POST_INDIRECT_OBJECT,
			Position.PRE_OBJECT,
			Position.GENITIVE_OBJECT,
			Position.OBJECT,
			Position.POST_OBJECT,
			Position.DEFAULT)),
	
	SOI (Arrays.asList(
			Position.VORFELD,
			Position.FRONT,
			Position.PRE_SUBJECT,
			Position.SUBJECT,
			Position.POST_SUBJECT,
			Position.PRE_OBJECT,
			Position.GENITIVE_OBJECT,
			Position.OBJECT,
			Position.POST_OBJECT,
			Position.PRE_INDIRECT_OBJECT,
			Position.INDIRECT_OBJECT,
			Position.POST_INDIRECT_OBJECT,
			Position.DEFAULT)),
	
	OSI (Arrays.asList(
			Position.VORFELD,
			Position.FRONT,
			Position.PRE_OBJECT,
			Position.GENITIVE_OBJECT,
			Position.OBJECT,
			Position.POST_OBJECT,
			Position.PRE_SUBJECT,
			Position.SUBJECT,
			Position.POST_SUBJECT,
			Position.PRE_INDIRECT_OBJECT,
			Position.INDIRECT_OBJECT,
			Position.POST_INDIRECT_OBJECT,
			Position.DEFAULT)),

	OIS (Arrays.asList(
			Position.VORFELD,
			Position.FRONT,
			Position.PRE_OBJECT,
			Position.GENITIVE_OBJECT,
			Position.OBJECT,
			Position.POST_OBJECT,
			Position.PRE_INDIRECT_OBJECT,
			Position.INDIRECT_OBJECT,
			Position.POST_INDIRECT_OBJECT,
			Position.PRE_SUBJECT,
			Position.SUBJECT,
			Position.POST_SUBJECT,
			Position.DEFAULT)),

	IOS (Arrays.asList(
			Position.VORFELD,
			Position.FRONT,
			Position.PRE_INDIRECT_OBJECT,
			Position.INDIRECT_OBJECT,
			Position.POST_INDIRECT_OBJECT,
			Position.PRE_OBJECT,
			Position.GENITIVE_OBJECT,
			Position.OBJECT,
			Position.POST_OBJECT,
			Position.PRE_SUBJECT,
			Position.SUBJECT,
			Position.POST_SUBJECT,
			Position.DEFAULT)),

	ISO (Arrays.asList(
			Position.VORFELD,
			Position.FRONT,
			Position.PRE_INDIRECT_OBJECT,
			Position.INDIRECT_OBJECT,
			Position.POST_INDIRECT_OBJECT,
			Position.PRE_SUBJECT,
			Position.SUBJECT,
			Position.POST_SUBJECT,
			Position.PRE_OBJECT,
			Position.GENITIVE_OBJECT,
			Position.OBJECT,
			Position.POST_OBJECT,
			Position.DEFAULT));
	
	private List<Position> positions;
	
	WordOrder(List<Position> posList) {
		this.positions = posList;
	}
	
	public List<Position> getPositions() {
		return this.positions;
	}
	
	/**
	 * Returns the word order in which a given complement position
	 * has been moved to the front, while leaving the order of the
	 * other complements untouched.
	 * 
	 * @param pos
	 * 			The position to be fronted
	 * @return
	 * 			A word order object identical to this one,
	 * 			except that @{pos} appears in front position 
	 */
	public WordOrder getFrontedVariant(Position pos) {
		if (pos.equals(Position.SUBJECT)) {
			switch (this) {
			case IOS:
			case ISO:
				return WordOrder.SIO;
			case OSI:
			case OIS:
				return WordOrder.SOI;
			}
		} else if (pos.equals(Position.OBJECT)) {
			switch (this) {
			case SOI:
			case SIO:
				return WordOrder.OSI;
			case IOS:
			case ISO:
				return WordOrder.OIS;
			}
		} else if (pos.equals(Position.INDIRECT_OBJECT)) {
			switch (this) {
			case OIS:
			case OSI:
				return WordOrder.IOS;
			case SIO:
			case SOI:
				return WordOrder.ISO;
			}
		}
		
		return this;
	}
	
	
}
