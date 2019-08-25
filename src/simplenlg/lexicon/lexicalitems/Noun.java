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

import simplenlg.exception.LexiconException;
import simplenlg.features.Agreement;
import simplenlg.features.Case;
import simplenlg.features.Category;
import simplenlg.features.Gender;
import simplenlg.features.NumberAgr;
import simplenlg.lexicon.morph.NounInflection;

/**
 * This class extends the {@link simplenlg.lexicon.lexicalitems.ContentWord}
 * abstract class for nouns. Nouns can be specified for their
 * {@link simplenlg.features.Agreement} properties.
 * 
 * @author agatt
 */
public class Noun extends ContentWord {

	Gender gender;
	NumberAgr number;
	Case cs;
	
	String accSg;
	String genSg;
	String datSg;
	String nomPl;
	String accPl;
	String genPl;
	String datPl;
	
	// true if noun declines like an adjective 
	boolean adjectiveInflection;
	// the adjective from which this noun is derived, if any
	Adjective baseAdjective;
	
	// OBSOLETE?
	// plural form
	String pluralForm;

	// OBSOLETE?
	// agreement type
	Agreement agreement;
	
	Boolean regularInflection;

	/**
	 * Initialises a noun with the given baseform.
	 * 
	 * @param baseform
	 *            The base form of the noun
	 */
	public Noun(String baseform) {
		super(baseform);
		this.category = Category.NOUN;
		this.gender = Gender.NEUTER;
		this.number = NumberAgr.SINGULAR;
		this.cs = Case.NOMINATIVE;
		this.agreement = null;
		this.regularInflection = true;
		this.adjectiveInflection = false;
		this.baseAdjective = null;
		generateSingularSuffixes("s");
		generatePluralSuffixes("");
	}
	
	public Noun(String baseform, Gender g) {
		this(baseform);
		this.gender = g;
		generateSingularSuffixes("s");
	}
	
	public Noun(String baseform, Gender g, String genSg, String nomPl) {
		this(baseform, g);
		generateSingularSuffixes(genSg);
		generatePluralSuffixes(nomPl);
	}
	
	public Noun(String baseform, String gender, String genSg, String nomPl) {
		this(baseform, Gender.NEUTER, genSg, nomPl);

		if (gender.equals("die")) {
			this.gender = Gender.FEMININE;
			generateSingularSuffixes(genSg);
		} else if (gender.equals("der")) {
			this.gender = Gender.MASCULINE;
			generateSingularSuffixes(genSg);
		}
		
	}
	
	public Noun(String baseform, Gender g, String accSg, String genSg, String datSg, String nondatPl, String datPl) {
		this(baseform, g);
		this.accSg = accSg;
		this.genSg = genSg;
		this.datSg = datSg;
		this.nomPl = nondatPl;
		this.accPl = nondatPl;
		this.genPl = nondatPl;
		this.datPl = datPl;
		this.regularInflection = false;
	}
	
	public Noun(String baseform, String gender, String accSg, String genSg, String datSg, String nondatPl, String datPl) {
		this(baseform, Gender.NEUTER, accSg, genSg, datSg, nondatPl, datPl);
		this.gender = Constants.detGender(gender);
	}
	
	/**
	 * Instantiates a new noun with the given id and baseform.
	 * 
	 * @param id
	 *            the id
	 * @param baseform
	 *            the baseform
	 */
	public Noun(String id, String baseform) {
		this(baseform);
		setID(id);
	}

	/**
	 * Instantiates a new noun with the given id, baseform and citation form.
	 * 
	 * @param id
	 *            the id
	 * @param baseform
	 *            the baseform
	 * @param citationform
	 *            the citationform
	 */
	public Noun(String id, String baseform, String citationform) {
		this(id, baseform);
		setCitationForm(citationform);
	}

	
	public String getSuffix(Case cas, NumberAgr num) {
		boolean plural = (num == NumberAgr.PLURAL);
		
		switch (cas) {
		case ACCUSATIVE:
			return (plural ? this.accPl : this.accSg);
		case GENITIVE:
			return (plural ? this.genPl : this.genSg);
		case DATIVE:
			return (plural ? this.datPl : this.datSg);
		case NOMINATIVE:
		default:
			// return (plural ? this.nomPl : this.baseForm); 
			return (plural ? this.nomPl : "");
		}
	}
	
	public String getForm(Case cas, NumberAgr num) {
		return this.createFromBaseForm(this.getSuffix(cas, num));
	}

	public NumberAgr getNumber() {
		return this.number;
	}

	public void setNumber(NumberAgr number) {
		this.number = number;
	}

	public Case getCase() {
		return this.cs;
	}

	public void setCase(Case cs) {
		this.cs = cs;
	}

	/**
	 * Gets the gender for this noun.
	 * 
	 * @return the noun gender
	 */
	public Gender getGender() {
		return this.gender;
	}
	
	/**
	 * Sets the gender for this noun.
	 * 
	 * @param gen
	 * 				the new gender
	 */
	public void setGender(Gender gen) {
		this.gender = gen;
	}

	/**
	 * Gets the agreement code for this noun, specifying its count, mass, or
	 * group properties.
	 * 
	 * @return the agreement code
	 */
	public Agreement getAgreement() {
		return this.agreement;
	}

	/**
	 * Sets the agreement code for this noun, specifying its count, mass, or
	 * group properties.
	 * 
	 * @param agr
	 *            the new agreement
	 * @throws LexiconException
	 *             if the Agreement value is
	 *             {@link simplenlg.features.Agreement#FREE}, which cannot apply
	 *             to nouns.
	 */
	public void setAgreement(Agreement agr) {

		if (agr.appliesTo(this.category)) {
			this.agreement = agr;
		} else {
			throw new LexiconException(agr
					+ " cannot be applied to category NOUN");
		}
	}

	/**
	 * @return the adjectiveInflection
	 */
	public boolean isAdjectiveInflection() {
		return adjectiveInflection;
	}

	/**
	 * @param adjectiveInflection the adjectiveInflection to set
	 */
	public void setAdjectiveInflection(boolean adjectiveInflection) {
		this.adjectiveInflection = adjectiveInflection;
		this.baseAdjective = adjectiveInflection ? new Adjective(this.baseForm) : null;
	}

	public Adjective getBaseAdjective() {
		return baseAdjective;
	}
	
	/**
	 * @param baseAdjective the baseAdjective to set
	 */
	public void setBaseAdjective(Adjective baseAdjective) {
		this.adjectiveInflection = (baseAdjective == null) ? false : true;
		this.baseAdjective = baseAdjective;
	}

	/**
	 * Check whether this is a count noun. Equivalent to
	 * <code>Noun.getAgreement() == Agreement.COUNT</code>
	 * 
	 * @return <code>true</code> if the agreement code of this noun has been set
	 *         to {@link simplenlg.features.Agreement#COUNT}
	 * @see #setAgreement(Agreement)
	 */
	public boolean isCountNoun() {
		return this.agreement == Agreement.COUNT;
	}

	/**
	 * Check whether this is a mass noun. Equivalent to
	 * <code>Noun.getAgreement() == Agreement.MASS</code>
	 * 
	 * @return <code>true</code> if the agreement code of this noun has been set
	 *         to {@link simplenlg.features.Agreement#MASS}
	 * @see #setAgreement(Agreement)
	 */
	public boolean isMassNoun() {
		return this.agreement == Agreement.MASS;
	}

	/**
	 * Check whether this is a group-denoting noun, allowing both singular and
	 * plural agreement. Equivalent to
	 * <code>Noun.getAgreement() == Agreement.GROUP</code>
	 * 
	 * @return <code>true</code> if the agreement code of this noun has been set
	 *         to {@link simplenlg.features.Agreement#GROUP}
	 * @see #setAgreement(Agreement)
	 */
	public boolean isGroupNoun() {
		return this.agreement == Agreement.GROUP;
	}

	/**
	 * Check whether this is a fixed plural noun, i.e. a noun that always
	 * exhibits plural agreement. Equivalent to
	 * <code>Noun.getAgreement() == Agreement.FIXED_PLUR</code>
	 * 
	 * @return <code>true</code> if the agreement code of this noun has been set
	 *         to {@link simplenlg.features.Agreement#FIXED_PLUR}
	 * @see #setAgreement(Agreement)
	 */
	public boolean isFixedPluralNoun() {
		return this.agreement == Agreement.FIXED_PLUR;
	}

	/**
	 * Check whether this is a fixed singular noun, i.e. a noun that always
	 * exhibits singular agreement and cannot be pluralised. Equivalent to
	 * <code>Noun.getAgreement() == Agreement.FIXED_SING</code>
	 * 
	 * @return <code>true</code> if the agreement code of this noun has been set
	 *         to {@link simplenlg.features.Agreement#FIXED_SING}
	 * @see #setAgreement(Agreement)
	 */
	public boolean isFixedSingularNoun() {
		return this.agreement == Agreement.FIXED_SING;
	}
	
	/**
	 * Checks whether the noun inflects in a regular way, i.e. all noun forms
	 * can be derived from the baseform plus Gen.Sg. and Nom.Pl. suffixes.
	 * This will be false if a constructor using more than these two suffixes
	 * has been called, or one of the suffixes has been manually set.
	 * 
	 * @return <code>true</code> if the noun inflects in a regular way
	 */
	@Override
	public boolean isRegular() {
		return this.regularInflection;
	}

	/**
	 * Generates all singular word forms from the Genitive Singular suffix.
	 * Dative and Accusative forms will be identical to the Genitive if the 
	 * suffix ends with an "n" (indicating weak declension), and identical to
	 * the Nominative form otherwise (indication strong declension).
	 * 
	 * @param genSg
	 * 				The Genitive Singular suffix.
	 */
	private void generateSingularSuffixes(String genSg) {
		this.genSg = genSg;
		
		if (this.gender.equals(Gender.MASCULINE) && genSg.matches(".*n")) {
			this.accSg = this.genSg;
		} else {
			this.accSg = "";
		}
		
		if (genSg.endsWith("n")) {
			this.datSg = this.genSg;
		} else if (genSg.endsWith("ns")) {
			this.datSg = this.genSg.substring(0, genSg.length()-1);
		} else {
			this.datSg = "";
		}
	}
	
	/**
	 * Generates all plural word forms from the Nominative Plural suffix or
	 * word form.  Accusative and Genitive forms will be identical to the
	 * Nominative form, while "n" is suffixed to the Dative form unless 
	 * the plural form already ends in one of {a,i,n,s,x,z}. 
	 * 
	 * @param nomPl
	 * 				The Nominative Plural, treated as a word form if it begins
	 * 				with an uppercase letter, treated as a suffix otherwise. 
	 */
	private void generatePluralSuffixes(String nomPl) {
		if (nomPl.startsWith("$")) {
			nomPl = NounInflection.applyUmlaut(this.baseForm).concat(nomPl.substring(1));
		}
		this.nomPl = nomPl;
		this.accPl = nomPl;
		this.genPl = nomPl;
		this.datPl = this.makeDativePluralSuffix(nomPl);
	}
	
	/**
	 * If input begins with an uppercase letter, returns the input; otherwise,
	 * concatenates the base form and the input (treats it as a suffix).
	 * 
	 * @param suffix
	 * 				A word form (beginning with an uppercase letter) or a suffix.
	 * @return
	 * 				The final word form.
	 */
	private String createFromBaseForm(String suffix) {
		if (suffix.isEmpty()) {
			return this.baseForm;
		} else if (Character.isUpperCase(suffix.charAt(0))) {
			return suffix;
		} else {
			return (this.baseForm + suffix);
		}
	}

	private String makeDativePluralSuffix(String plural) {
		if (plural.isEmpty() && this.baseForm.matches(".*[ainsxz]$")) {
			return plural;
		} else if (plural.matches(".*[ainsxz]$")) {
			return plural;
		} else {
			return (plural + "n");
		}
	}
	
	// TODO: this is a HACK
	public void makeCompound(String prefix) {
		this.accSg = prefix + this.createFromBaseForm(this.accSg).toLowerCase();
		this.datSg = prefix + this.createFromBaseForm(this.datSg).toLowerCase();
		this.genSg = prefix + this.createFromBaseForm(this.genSg).toLowerCase();
		this.nomPl = prefix + this.createFromBaseForm(this.nomPl).toLowerCase();
		this.accPl = prefix + this.createFromBaseForm(this.accPl).toLowerCase();
		this.datPl = prefix + this.createFromBaseForm(this.datPl).toLowerCase();
		this.genPl = prefix + this.createFromBaseForm(this.genPl).toLowerCase();
		this.baseForm = prefix + this.baseForm.toLowerCase();
	}

	
}