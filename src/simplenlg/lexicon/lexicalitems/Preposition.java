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

import simplenlg.features.Case;
import simplenlg.features.Category;

/**
 * Class representing prepositions.
 */
public class Preposition extends Word {

	Case governedCase;
	
	String leftForm;
	String rightForm;
	
	/**
	 * Instantiates a new preposition with the given baseform.
	 * 
	 * @param baseform
	 *            the baseform
	 */
	public Preposition(String baseform) {
		super(baseform);
		this.category = Category.PREPOSITION;
		this.governedCase = Case.ACCUSATIVE;
		this.leftForm = this.baseForm;
		this.rightForm = "";
	}

	public Preposition(String baseform, Case caseValue) {
		this(baseform);
		this.governedCase = caseValue;
	}

	public Preposition(String baseform, Case caseValue, boolean postpos) {
		this(baseform, caseValue);
		this.leftForm = "";
		this.rightForm = this.baseForm;
	}

	public Preposition(String leftform, String rightform, Case caseValue) {
		this(leftform + " ... " + rightform, caseValue);
		this.leftForm = leftform;
		this.rightForm = rightform;
	}
	
	/**
	 * Instantiates a new preposition with the given id and baseform.
	 * 
	 * @param id
	 *            the id
	 * @param baseform
	 *            the baseform
	 */
	public Preposition(String id, String baseform) {
		this(baseform);
		setID(id);
	}

	/**
	 * Instantiates a new preposition with the given id, baseform and citation
	 * form.
	 * 
	 * @param id
	 *            the id
	 * @param baseform
	 *            the baseform
	 * @param citationform
	 *            the citationform
	 */
	public Preposition(String id, String baseform, String citationform) {
		this(id, baseform);
		setID(id);
		setCitationForm(citationform);
	}

	public Case getGovernedCase() {
		return governedCase;
	}

	public void setGovernedCase(Case governedCase) {
		this.governedCase = governedCase;
	}

	public String getLeftForm() {
		return leftForm;
	}

	public void setLeftForm(String leftForm) {
		this.leftForm = leftForm;
	}

	public String getRightForm() {
		return rightForm;
	}

	public void setRightForm(String rightForm) {
		this.rightForm = rightForm;
	}

	public boolean isPostpositional() {
		return (this.leftForm.equals(""));
	}

	/**
	 * Gets a preposition which is predefined in the
	 * {@link simplenlg.lexicon.lexicalitems.Constants} class.
	 * 
	 * @param baseform
	 *            the baseform of the preposition
	 * 
	 * @return the preposition with the specified baseform
	 * @deprecated Use {@link Constants#getPreposition(String)} instead
	 * 
	 */
	@Deprecated
	public static Preposition getPreposition(String baseform) {
		return Constants.getPreposition(baseform);
	}

}
