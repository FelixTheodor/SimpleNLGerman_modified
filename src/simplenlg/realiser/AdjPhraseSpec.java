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

package simplenlg.realiser;

import simplenlg.exception.SimplenlgException;
import simplenlg.features.AdjectiveDegree;
import simplenlg.features.AdjectiveType;
import simplenlg.features.Case;
import simplenlg.features.Category;
import simplenlg.features.Gender;
import simplenlg.features.NumberAgr;
import simplenlg.lexicon.LexiconInterface;
import simplenlg.lexicon.lexicalitems.Adjective;
import simplenlg.lexicon.lexicalitems.LexicalItem;
import simplenlg.lexicon.lexicalitems.Noun;
import simplenlg.lexicon.morph.AdjectiveInflection;

// TODO: Auto-generated Javadoc
/**
 * The Class AdjPhraseSpec.
 */
public class AdjPhraseSpec extends HeadedPhraseSpec<Adjective> {

	/** The number. */
	NumberAgr number;
	Gender gender;
	Case cas;
	AdjectiveType adjType;
	AdjectiveDegree adjDegree;
	
	/**
	 * Constructs an empty NPPhraseSpec.
	 */
	public AdjPhraseSpec() {
		super();
		this.head = new Adjective("");
		this.number = NumberAgr.SINGULAR;
		this.gender = Gender.NEUTER;
		this.cas = Case.NOMINATIVE;
		this.adjType = AdjectiveType.UNINFLECTED;
		this.adjDegree = AdjectiveDegree.POSITIVE;
		this.category = Category.ADJECTIVE;
	}

	/**
	 * Constructs an AdjPhraseSpec.
	 * 
	 * @param adj
	 *            The string value of the adjective heading this phrase.
	 */
	public AdjPhraseSpec(String adj) {
		this();
		setHead(adj);
	}

	/**
	 * Constructs an <code>AdjPhraseSpec</code> with the specified head.
	 * 
	 * @param adj
	 *            The adjectival head, an instance of
	 *            {@link simplenlg.lexicon.lexicalitems.Adjective}
	 */
	public AdjPhraseSpec(Adjective adj) {
		this();
		setHead(adj);
	}

	// ***********************************************************
	// GETTERS/SETTERS
	// ***********************************************************

	/**
	 * Set whether this Adj phrase is plural. Useful especially in predicative
	 * constructions. ("They were beautiful")
	 * 
	 * @param plur
	 *            the plur
	 */
	public void setPlural(boolean plur) {
		this.number = plur ? NumberAgr.PLURAL : NumberAgr.SINGULAR;
	}

	/**
	 * Checks if is plural.
	 * 
	 * @return true, if is plural
	 */
	public boolean isPlural() {
		return this.number.equals(NumberAgr.PLURAL);
	}

	/**
	 * Sets the number.
	 * 
	 * @param n
	 *            the new number
	 */
	public void setNumber(NumberAgr n) {
		this.number = n;
	}

	/**
	 * Gets the number.
	 * 
	 * @return the number
	 */
	public NumberAgr getNumber() {
		return this.number;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Case getCase() {
		return cas;
	}

	public void setCase(Case cas) {
		this.cas = cas;
	}

	public AdjectiveType getAdjType() {
		return adjType;
	}

	public void setAdjType(AdjectiveType adjType) {
		this.adjType = adjType;
	}

	public AdjectiveDegree getDegree() {
		return adjDegree;
	}

	public void setDegree(AdjectiveDegree adjDegree) {
		this.adjDegree = adjDegree;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simplenlg.realiser.HeadedPhraseSpec#setHead(java.lang.String)
	 */
	@Override
	public void setHead(String adj) {
		this.head = new Adjective(adj);
	}

	/**
	 * Sets case, number, gender and adjective type to their respective
	 * values for agreeing with a given NP.
	 * 
	 * @param np
	 * 			The noun phrase with which the adjective phrase should
	 * 			agree
	 */
	public void setAgreementNP(NPPhraseSpec np) {
		this.setNumber(np.getNumber());
		this.setCase(np.getCase());
		this.setGender(np.getGender());
		this.setAdjType(AdjectiveInflection.determineAdjectiveType(np.getSpecifier(), np.isPlural()));
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see simplenlg.realiser.Phrase#coordinate(T[])
	 */
	public AdjPhraseSpec coordinate(Phrase... coords) {
		CoordinateAdjPhraseSpec coord = new CoordinateAdjPhraseSpec(this);

		if (coords.length == 0) {
			return this;
		}

		try {

			for (Phrase p : coords) {
				coord.addCoordinates((AdjPhraseSpec) p);
			}

			return coord;

		} catch (ClassCastException cce) {
			throw new SimplenlgException("Cannot coordinate: "
					+ "only phrases of the same type can be coordinated");
		}
	}

	
	@Override
	String realise(Realiser r) {
		String compText = "";
		String negText = "";
		
		if (this.hasComplements()) {
			this.setComplementCase();
			// alternate way for handling superlative would be to suppress complements instead
			if (this.adjDegree == AdjectiveDegree.POSITIVE ||
					this.adjDegree == AdjectiveDegree.SUPERLATIVE) {
				compText = "wie";
			} else if (this.adjDegree == AdjectiveDegree.COMPARATIVE) {
				compText = "als";
			}
		}
		if (this.negated) {
			negText = "nicht";
		}
		
		return r.appendSpace(negText, realisePremodifier(r), realiseHead(r),
				compText, realiseComplement(r), realisePostmodifier(r));
	}

	private void setComplementCase() {
		for (Phrase p : this.complements) {
			if (p instanceof NPPhraseSpec) {
				((NPPhraseSpec) p).setCase(Case.NOMINATIVE);
			}
		}
	}
	
	@Override
	String realiseHead(Realiser r) {
		if (this.head == null || this.head.getBaseForm() == null
				|| this.head.getBaseForm().length() == 0) {
			return "";
		}
		return this.head.getForm(this.cas, this.number, this.gender, this.adjType, this.adjDegree);
	}
	
	
}
