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
package simplenlg.features;

public enum Position implements Feature {

	VORFELD,
	FRONT,
	PRE_SUBJECT,
	SUBJECT,
	POST_SUBJECT,
	PRE_OBJECT,
	GENITIVE_OBJECT,		// genitive objects are treated like direct objects
	OBJECT,
	POST_OBJECT,
	PRE_INDIRECT_OBJECT,
	INDIRECT_OBJECT,
	POST_INDIRECT_OBJECT,
	
	DEFAULT;
	
	public boolean appliesTo(Category cat) {
		return true;
	}
	
	public DiscourseFunction mapToFunction() {
		switch (this) {
		case SUBJECT:
			return DiscourseFunction.SUBJECT;
		case OBJECT:
			return DiscourseFunction.OBJECT;
		case GENITIVE_OBJECT:
			return DiscourseFunction.GENITIVE_OBJECT;
		case INDIRECT_OBJECT:
			return DiscourseFunction.INDIRECT_OBJECT;
		case FRONT:
			return DiscourseFunction.FRONT_MODIFIER;
		default:
			return DiscourseFunction.NULL;
		}
	}
	
	public static Position[] complementPositions() {
		return new Position[] {
				Position.SUBJECT,
				Position.OBJECT,
				Position.INDIRECT_OBJECT,
				Position.GENITIVE_OBJECT
		};
	}
	
	public boolean isComplementPosition() {
		return (this.equals(OBJECT) || this.equals(SUBJECT) || 
				this.equals(INDIRECT_OBJECT) || this.equals(GENITIVE_OBJECT));
	}

}
