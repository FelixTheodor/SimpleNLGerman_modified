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

import java.util.HashSet;
import java.util.Set;

import simplenlg.features.AuxType;
import simplenlg.features.Category;
import simplenlg.features.ComplementFrame;
import simplenlg.features.Form;
import simplenlg.features.InflectionType;
import simplenlg.features.NumberAgr;
import simplenlg.features.Person;
import simplenlg.features.Tense;
import simplenlg.features.VerbMorph;
import simplenlg.features.VerbType;
import simplenlg.features.WordMorph;
import simplenlg.lexicon.LexiconInterface;
import simplenlg.lexicon.morph.VerbInflection;
import simplenlg.lexicon.morph.VerbInflectionPattern;
import simplenlg.lexicon.verbnet.VerbnetClass;
import simplenlg.lexicon.verbnet.VerbnetFrame;

/**
 * This class extends the {@link simplenlg.lexicon.lexicalitems.ContentWord}
 * abstract class for verbs. Any instance of <code>Verb</code> is defined for
 * the following inflectional rules:
 * <OL>
 * <LI>past participle: {@link #getPastParticiple()}</LI>
 * <LI>present tense: {@link #getPresent(Person, NumberAgr)} and
 * {@link #getPresent3SG()}</LI>
 * <LI>present participle: {@link #getPresentParticiple()}</LI>
 * <LI>past tense: {@link #getPast()}</LI.
 * </OL>
 * 
 * <P>
 * In addition, verbs can be distinguished by their
 * {@link simplenlg.features.VerbType}, which indicates whether they are main
 * verbs, auxiliaries or modals.
 * <P>
 * Verbs also inherit various properties related to the representation of their
 * complements, from the ContentWord class. Some methods are also provided to
 * query a verb for the kinds of complements it allows, and their syntactic
 * behaviour.
 * 
 * @author agatt
 */
public class Verb extends ContentWord {

	// inflection forms to be held once set
	String presentParticipleForm, present_3SG, pastTenseForm,
			pastParticipleForm, particle;

	// mmb:
	String verbPrefix;
	String presentStem, pastStem, subjunctiveStem;
	String presentParticiple, perfectParticiple;
	String alternatePresentStem;
	AuxType auxType;
	VerbInflectionPattern pattern;
	
	// verb type
	VerbType type;

	// verbnet frames if any
	Set<VerbnetClass> verbnetClasses;

	// flags for complement types
	boolean allowsPassive, allowsDative, monotrans, ditrans, cplxtrans, link;

	/**
	 * Constructs a new instance of Verb with the given baseform. The string
	 * passed to the constructor may actually consist of verb and particle
	 * separated by a space. If the baseform does contain a space between two
	 * substrings (e.g. "get up"), it is assumed to be a phrasal verb.
	 * 
	 * @param baseform
	 *            The base form of this verb, possibly with partic;e
	 * 
	 */
	public Verb(String baseform) {
		super();

		// Detects separable verb prefix
		this.verbPrefix = "";
		if (baseform.indexOf("|") > 0) {
			int sepIndex = baseform.indexOf("|");
			String p = baseform.substring(0, sepIndex);
			
			if (!p.equals("")) {
				setVerbPrefix(p);
			}
			
			this.baseForm = baseform.substring(sepIndex + 1);
		} else {
			this.baseForm = baseform;
		}
		
		// Detects present stem
		this.presentStem = VerbInflection.detectStem(this.baseForm);
		
		// Default values (weak inflection)
		this.pastStem = this.presentStem + "te";
		this.perfectParticiple = "ge" + this.presentStem + "t";
		this.presentParticiple = this.baseForm.concat("d");
		this.alternatePresentStem = "";
		this.subjunctiveStem = this.pastStem;
		this.auxType = AuxType.HABEN;
		this.pattern = Constants.VERB_INFLECTION_DEFAULT;

		// OLD: Detects particles (e.g. "give up")
/*		if (baseform.indexOf(" ") > 0) {
			int spaceIndex = baseform.indexOf(" ");
			String p = baseform.substring(spaceIndex + 1);

			if (p.length() == 0 || baseform.length() == 0 || p.indexOf(" ") > 0) {
				throw new LexiconException("Impossible verb: " + baseform);
			}

			if (!p.equals("")) {
				setParticle(p);
			}

			this.baseForm = baseform.substring(0, spaceIndex);

		} else {
			this.baseForm = baseform;
		}
*/
		setCitationForm(this.baseForm);
		this.category = Category.VERB;
		this.type = VerbType.MAIN;

		// init collection of verbnet frames
		this.verbnetClasses = new HashSet<VerbnetClass>();

		// init flags to default values
		this.allowsDative = false;
		this.allowsPassive = false;
		this.monotrans = false;
		this.ditrans = false;
		this.cplxtrans = false;
		this.link = false;
	}

	/**
	 * Instantiates a new verb with the given id and baseform. See
	 * {@link #Verb(String)} for details of the treatment of verb+particle
	 * combinations.
	 * 
	 * @param id
	 *            the id
	 * @param baseform
	 *            the baseform
	 */
	public Verb(String id, String baseform) {
		this(baseform);
		setID(id);
	}

/*	*//**
	 * Instantiates a new verb with the given id, baseform and citation form.
	 * See {@link #Verb(String)} for details of the treatment of verb+particle
	 * combinations.
	 * 
	 * @param id
	 *            the id
	 * @param baseform
	 *            the baseform
	 * @param citationform
	 *            the citation form
	 *//*
	public Verb(String id, String baseform, String citationform) {
		this(id, baseform);
		setCitationForm(citationform);
	}
*/
	
	
	// NEW CONSTRUCTORS
	
	public Verb(String baseform, String pastStem, String perfPart, String altSgStem, String subjStem, AuxType auxType) {
		this(baseform);
		this.pastStem = pastStem;
		this.perfectParticiple = perfPart;
		this.alternatePresentStem = altSgStem;
		this.subjunctiveStem = subjStem;
		this.auxType = auxType;
	}

	public Verb(String baseform, String pastStem, String perfPart, String altSgStem, String subjStem, AuxType auxType, VerbInflectionPattern ptrn) {
		this(baseform, pastStem, perfPart, altSgStem, subjStem, auxType);
		this.pattern = ptrn;
	}
	
	public Verb(String baseform, String pastStem, String perfPart, String altSgStem, String subjStem, AuxType auxType, VerbType vt, VerbInflectionPattern ptrn) {
		this(baseform, pastStem, perfPart, altSgStem, subjStem, auxType, ptrn);
		this.type = vt;
		if (vt == VerbType.MODAL) {
			ptrn.addFeature(WordMorph.MODAL_INFLECTION);
		}
	}

	public Verb(String baseform, String pastStem, String perfPart, String altSgStem, String subjStem, String presPart, AuxType auxType, VerbInflectionPattern ptrn) {
		this(baseform, pastStem, perfPart, altSgStem, subjStem, auxType, ptrn);
		this.presentParticiple = presPart;
	}

	public Verb(String baseform, String pastStem, String perfPart, String altSgStem, String subjStem, String presPart, AuxType auxType, VerbType vt, VerbInflectionPattern ptrn) {
		this(baseform, pastStem, perfPart, altSgStem, subjStem, presPart, auxType, ptrn);
		this.type = vt;
	}

	public Verb(String baseform, VerbInflectionPattern ptrn) {
		this(baseform);
		this.pattern = ptrn;
	}

	
	
	/**
	 * Instantiates a new verb with the given baseform, and its parent lexicon
	 * to which it belongs.
	 * 
	 * @param baseform
	 *            the baseform
	 * @param lex
	 *            the lex
	 * @deprecated As of Version 3.7, the parent lexicon should be set via
	 *             {@link #setParentLexicon(LexiconInterface)}
	 */
	@Deprecated
	public Verb(String baseform, LexiconInterface lex) {
		this(baseform);
		this.parentLexicon = lex;
	}

	/**
	 * Add a verbnet class to the set to which this verb belongs. The verb
	 * inherits all the verbnet frames defined for this class.
	 * 
	 * @param vnClass
	 *            The verbnet class
	 */
	public void addVerbnetClass(VerbnetClass vnClass) {

	}

	/**
	 * Check whether this verb belongs to any verbnet classes.
	 * 
	 * @return <code>true</code> if some verbnet classes have been specified for
	 *         this verb.
	 * @see #addVerbnetClass(VerbnetClass)
	 */
	public boolean hasVerbnetClass() {
		return !this.verbnetClasses.isEmpty();
	}

	/**
	 * Get the VerbnetFrames defined for this verb. These are the frames that
	 * the verb inherits from its verbnet classes.
	 * 
	 * @return a set of VerbnetFrames, if any verbnet classes have been
	 *         specified, the empty set otherwise.
	 */
	public Set<VerbnetFrame> getVerbnetFrames() {
		Set<VerbnetFrame> frames = new HashSet<VerbnetFrame>();

		for (VerbnetClass vnClass : this.verbnetClasses) {
			frames.addAll(vnClass.getFrames());
		}

		return frames;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * simplenlg.lexicon.lexicalitems.ContentWord#addComplementFrame(simplenlg
	 * .features.ComplementFrame)
	 */
	@Override
	public void addComplementFrame(ComplementFrame frame) {
		super.addComplementFrame(frame);

		if (frame.allowsDativeShift()) {
			this.allowsDative = true;
		}

		if (frame.allowsPassive()) {
			this.allowsPassive = true;
		}

		switch (frame.getTransitivity()) {
		case MONOTRANS:
			this.monotrans = true;
			break;
		case DITRANS:
			this.ditrans = true;
			break;
		case COMPLEX_TRANS:
			this.cplxtrans = true;
			break;
		case LINKING:
			this.link = true;
			break;
		}

	}

	/**
	 * Check whether this verb allows passive raising. This depends on the
	 * complements that it has, as defined via
	 * {@link #addComplementFrame(ComplementFrame)}. Note that a return value of
	 * <code>true</code> only means that at least one of the complements allows
	 * passive raising (there may be others that don't).
	 * 
	 * @return <code>true</code>, if at least one of the complements allows
	 *         passive raising
	 * 
	 * @see ComplementFrame#allowsPassive()
	 */
	public boolean allowsPassiveRaising() {
		return this.allowsPassive;
	}

	/**
	 * Check whether this verb allows dative shift. This depends on the
	 * complements that it has, as defined via
	 * {@link #addComplementFrame(ComplementFrame)}. Note that a return value of
	 * <code>true</code> only means that at least one of the complements allows
	 * dative shift (there may be others that don't).
	 * 
	 * @return <code>true</code>, if at least one of the complements allows
	 *         dative shift
	 * @see ComplementFrame#allowsDativeShift()
	 */
	public boolean allowsDativeShift() {
		return this.allowsDative;
	}

	/**
	 * Make this a phrasal verb, consisting of the baseform, as set in the
	 * constructor, and a (prepositional or adverbial) particle.
	 * 
	 * @param part
	 *            The particle
	 * 
	 * @see #setParticle(LexicalItem)
	 * @see #Verb(String)
	 */
	public void setParticle(String part) {
		this.particle = part;
	}

	/**
	 * Make this a phrasal verb with the lexical item passed as particle
	 * 
	 * @param part
	 *            The <code>LexicalItem</code> particle
	 */
	public void setParticle(LexicalItem part) {
		this.particle = part.getBaseForm();
	}

	/**
	 * Gets the particle, if one has been specified either in the constructor,
	 * or via the setter.
	 * 
	 * @return the particle
	 * @see #setParticle(String)
	 * @see #setParticle(LexicalItem)
	 * @see #Verb(String)
	 */
	public String getParticle() {
		return this.particle;
	}
	
	public String getVerbPrefix() {
		return verbPrefix;
	}

	public void setVerbPrefix(String verbPrefix) {
		this.verbPrefix = verbPrefix;
	}

	public boolean hasVerbPrefix() {
		if (this.verbPrefix == null || this.verbPrefix.equals("")) {
			return false;
		} else {
			return true;
		}
	}
	
	public String getPresentStem() {
		return presentStem;
	}

	public void setPresentStem(String presentStem) {
		this.presentStem = presentStem;
	}

	public String getPastStem() {
		return pastStem;
	}

	public void setPastStem(String pastStem) {
		this.pastStem = pastStem;
	}

	public String getSubjunctiveStem() {
		if (this.subjunctiveStem.isEmpty()) {
			return this.getPastStem();
		} else {
			return this.subjunctiveStem;
		}
	}

	public void setSubjunctiveStem(String subjunctiveStem) {
		this.subjunctiveStem = subjunctiveStem;
	}
	
	public String getPerfectParticiple() {
		return render(this.perfectParticiple);
	}

	public void setPerfectParticiple(String perfectParticiple) {
		this.perfectParticiple = perfectParticiple;
	}

	public void setPresentParticiple(String presentParticiple) {
		this.presentParticiple = presentParticiple;
	}

	public String getAlternatePresentStem() {
		if (this.hasAlternatePresentStem()) {
			return this.alternatePresentStem;
		} else {
			return this.presentStem;
		}
	}

	public void setAlternatePresentStem(String alternatePresentStem) {
		this.alternatePresentStem = alternatePresentStem;
	}

	public boolean hasAlternatePresentStem() {
		if (this.alternatePresentStem == null || this.alternatePresentStem.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}
	
	public VerbInflectionPattern getPattern() {
		return pattern;
	}

	public void setPattern(VerbInflectionPattern pattern) {
		this.pattern = pattern;
	}

	public AuxType getAuxType() {
		return auxType;
	}

	public void setAuxType(AuxType auxType) {
		this.auxType = auxType;
	}

	/**
	 * Returns the infinitive stem, without any prefixes.
	 * 
	 * @return the stem of the verb
	 */
	public String getInfinitiveStem() {
		return this.baseForm;
	}
	
	/**
	 * Returns the baseform, with the verb particicle if one has been specified.
	 * 
	 * @return the full baseform of the verb
	 */
	@Override
	public String getBaseForm() {
		if (hasVerbPrefix()) {
			String separator = this.verbPrefix.endsWith(" ") ? "" : "|";
			return this.verbPrefix + separator + this.baseForm;
		} else {
			return this.baseForm;
		}
	}

	/**
	 * Returns the baseform, giving the option to include the particule in the
	 * String returned, or not. For example, if the verb is <I>fall down</I>,
	 * and this method is invoked with value <code>false</code>, the String
	 * returned is <I>fall</I>.
	 * 
	 * @param includeParticle
	 *            whether to include the particle
	 * @return the baseform, with or without the particle
	 */
	public String getBaseform(boolean includeParticle) {

		if (includeParticle) {
			return render(this.baseForm);
		} else {
			return this.baseForm;
		}
	}

	
	public String getForm(Person p, NumberAgr n, Tense t, Form f) {
		return this.pattern.apply(this, p, n, t, f);
	}
	
	public String getImperativeForm(NumberAgr n) {
		return this.getForm(null, n, null, Form.IMPERATIVE);
	}
	
	public String getHonorificImperativeForm() {
		// TODO: hardcoded exception...
		if (this.pattern.equals(Constants.VERB_INFLECTION_TUN)) {
			return "tun Sie";
		} else {
			return this.getForm(Person.THIRD, NumberAgr.PLURAL, Tense.PRESENT, Form.SUBJUNCTIVE).concat(" Sie");
		}
	}

	/**
	 * Checks if is this is a phrasal verb, i.e has both a baseform and a
	 * particle, set either through the constructor or via the setter.
	 * 
	 * @return <code>true</code> if this verb has a particle
	 * @see #Verb(String)
	 * @see #setParticle(String)
	 */
	public boolean isPhrasalVerb() {
		return this.particle != null;
	}

	/**
	 * Checks if this is a copular verb. This runs a simple check on whether the
	 * baseform is <I>be</I>, which is the standard copular verb in English
	 * predicative constructions (as in <I>He <U>is</U> a painter</I>).
	 * 
	 * @return <code>true</code> if this is the verb "to be"
	 */
	public boolean isCopular() {
		return this.baseForm.equalsIgnoreCase("be");
	}

	/**
	 * Set the "isNullAffixVerb" parameter for this verb. If set to "true", this
	 * means that the verb is never inflected in any form. Mostly used for the
	 * modal verbs "would", "should" etc.
	 * 
	 * @param neverInflected
	 *            whether this verb is ever inflected in any form
	 * @deprecated As of Version 3.7, use
	 *             {@link #setInflectionType(InflectionType)} instead
	 */
	@Deprecated
	public void setIsNullAffixVerb(boolean neverInflected) {
		if (neverInflected) {
			this.pastParticipleForm = this.baseForm;
			this.pastTenseForm = this.baseForm;
			this.present_3SG = this.baseForm;
			this.presentParticipleForm = this.baseForm;
		}

		this.inflectionType = InflectionType.INVARIANT;
	}

	/**
	 * Check if this verb ever takes inflections.
	 * 
	 * @return <code>true</code> if this is a verb specified as never taking
	 *         inflections in any form.
	 * @deprecated As of version 3.7, use {@link #getInflectionType()} instead
	 */
	@Deprecated
	public boolean isNullAffixVerb() {
		return this.inflectionType == InflectionType.INVARIANT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * simplenlg.lexicon.lexicalitems.Word#setInflectionType(simplenlg.features
	 * .InflectionType)
	 */
	@Override
	public void setInflectionType(InflectionType t) {
		super.setInflectionType(t);

		if (t.equals(InflectionType.INVARIANT)) {
			this.pastParticipleForm = this.baseForm;
			this.pastTenseForm = this.baseForm;
			this.present_3SG = this.baseForm;
			this.presentParticipleForm = this.baseForm;
		}
	}

	/**
	 * Sets the type of verb, i.e. whether it is a main verb, a modal verb (e.g.
	 * "should") or an auxiliary (such as <I>be</I>). If the verb type is set to
	 * {@link simplenlg.features.VerbType#MODAL}, this results in the inflection
	 * type of this verb being set automatically to
	 * {@link simplenlg.features.InflectionType#INVARIANT}, since modal verbs
	 * never inflect.
	 * 
	 * @param type
	 *            the new verb type
	 */
	public void setVerbType(VerbType type) {
		this.type = type;

		if (this.type == VerbType.MODAL) {
			setInflectionType(InflectionType.INVARIANT);
		}
	}

	/**
	 * Gets the verb type.
	 * 
	 * @return the verb type, if set, <code>null</code> otherwise
	 */
	public VerbType getVerbType() {
		return this.type;
	}

	/**
	 * Checks whether this is a modal verb (e.g. "should"). Equivalent to
	 * <code>Verb.getInflectionType.equals(InflectionType.MODAL)</code>
	 * 
	 * @return <code>true</code> if this is a modal verb
	 */
	public boolean isModalVerb() {
		return this.type.equals(VerbType.MODAL);
	}

	/**
	 * Checks if the verb is an auxiliary verb.
	 * 
	 * @return true, if is auxiliary verb
	 */
	public boolean isAuxiliaryVerb() {
		return this.type.equals(VerbType.AUX);
	}

	/**
	 * Processes the baseform of this <code>Verb</code> to obtain its "en" form.
	 * The form is stored in a field the first time this method is called, and
	 * is simply returned on subsequent calls to this method.
	 * <p>
	 * This is a delegate method, calling
	 * {@link simplenlg.lexicon.morph.VerbInflection#PAST_PARTICIPLE}.
	 * 
	 * @return The past participle form (a <code>String</code>)
	 */
	public String getPastParticiple() {

		if (this.pastParticipleForm == null) {
			this.pastParticipleForm = VerbInflection.PAST_PARTICIPLE
					.apply(this);
		}

		return render(this.pastParticipleForm);
	}

	/**
	 * Sets the past participle form of this verb. Useful to override the
	 * default behaviour from the in-built rules.
	 * 
	 * @param ppart
	 *            - The past participle form
	 */
	public void setPastParticiple(String ppart) {
		this.pastParticipleForm = ppart;
	}


	public String getPresentParticiple() {
		return render(this.presentParticiple);
	}

	/**
	 * Gets the infinitive form of the verb.
	 * 
	 * @return The infinitive form
	 */
	public String getInfinitive() {
		return render(this.baseForm);
	}

	public String getZuInfinitive() {
		String inf = this.verbPrefix;
		String zu = (inf.equals("") || inf.endsWith(" ")) ? "zu " : "zu";
		inf += zu + this.baseForm;
		return inf;
	}
	
	/**
	 * Checks if this verb is intransitive. This is only <code>true</code> if
	 * the verb has no complements.
	 * 
	 * @return true, if is intransitive
	 * @see #addComplementFrame(ComplementFrame)
	 */
	public boolean isIntransitive() {
		return !this.monotrans && !this.ditrans && !this.cplxtrans;
	}

	/**
	 * Checks if this verb has at least one monotransitive complement (i.e. a
	 * complement consisting of a single NP).
	 * 
	 * @return <code>true</code>, if at least one of the complements of this
	 *         verb is monotransitive.
	 * @see #addComplementFrame(ComplementFrame)
	 * @see simplenlg.features.Transitivity#MONOTRANS
	 */
	public boolean isMonotransitive() {
		return this.monotrans;
	}

	/**
	 * Checks if this verb has at least one ditransitive complement (i.e. a
	 * complement consisting of two NPs).
	 * 
	 * <code>true</code>, if at least one of the complements of this verb is
	 * ditransitive.
	 * 
	 * @see #addComplementFrame(ComplementFrame)
	 * @see simplenlg.features.Transitivity#DITRANS
	 */
	public boolean isDitransitive() {
		return this.ditrans;
	}

	/**
	 * Checks if this verb has at least one complex transitive complement
	 * 
	 * <code>true</code>, if at least one of the complements of this verb is
	 * ditransitive.
	 * 
	 * @see #addComplementFrame(ComplementFrame)
	 * @see simplenlg.features.Transitivity#COMPLEX_TRANS
	 */
	public boolean isComplextransitive() {
		return this.cplxtrans;
	}

	/**
	 * Checks if this verb has at least one linking complement
	 * 
	 * <code>true</code>, if at least one of the complements of this verb is a
	 * linking complement.
	 * 
	 * @see #addComplementFrame(ComplementFrame)
	 * @see simplenlg.features.Transitivity#LINKING
	 */
	public boolean isLinkingVerb() {
		return this.link;
	}

	/**
	 * Gets the number of complements that this verb has
	 * 
	 * @return the num complements
	 */
	public int getNumComplements() {
		return this.complementationFrames.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simplenlg.lexicon.lexicalitems.Word#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {

		if (super.equals(o)) {
			Verb v = (Verb) o;
			return this.particle == v.particle
					|| this.particle.equals(v.particle);
		}

		return false;
	}

	// ***************************************************************
	// PRIVATE METHODS
	// ***************************************************************

	/*
	 * Render the verb into baseform + particle
	 */
	private String render(String morphForm) {
		return hasVerbPrefix() ? this.verbPrefix + morphForm : morphForm;
	}

}
