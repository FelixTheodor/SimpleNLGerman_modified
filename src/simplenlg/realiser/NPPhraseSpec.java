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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import simplenlg.exception.SimplenlgException;
import simplenlg.features.AdjectiveType;
import simplenlg.features.Agreement;
import simplenlg.features.Case;
import simplenlg.features.Category;
import simplenlg.features.DiscourseFunction;
import simplenlg.features.Gender;
import simplenlg.features.NumberAgr;
import simplenlg.features.Person;
import simplenlg.features.Quantification;
import simplenlg.lexicon.LexiconInterface;
import simplenlg.lexicon.lexicalitems.Constants;
import simplenlg.lexicon.lexicalitems.Determiner;
import simplenlg.lexicon.lexicalitems.LexicalItem;
import simplenlg.lexicon.lexicalitems.Noun;
import simplenlg.lexicon.lexicalitems.Preposition;
import simplenlg.lexicon.lexicalitems.Pronoun;
import simplenlg.lexicon.morph.AdjectiveInflection;
import simplenlg.realiser.comparators.PhraseComparator;

/**
 * This class provides the basic representation of the Noun Phrase. In addition
 * to the simplenlg.features inherited from
 * {@link simplenlg.realiser.HeadedPhraseSpec}, an <code>NPPhraseSpec</code>
 * also has the following:
 * 
 * <OL>
 * <LI>A specifier. This is usually a determiner, but can also be a phrase, as
 * in possessive constructions, where the possessor phrase specifies the head.
 * Examples:
 * <UL>
 * <LI><i><strong>the</strong> man in the hat</i> [determiner]
 * <LI><i><strong>the man in the hat's</strong> cat</i> [possessive NP
 * specifier]
 * </UL>
 * </LI>
 * <LI>A {@link simplenlg.features.Person} feature. This is useful especially
 * when an NP is pronominalised. {@link simplenlg.features.Person#THIRD} by
 * default.</LI>
 * <LI>A {@link simplenlg.features.NumberAgr} feature. This determines whether
 * the NP is realised with singular or plural morphology.
 * {@link simplenlg.features.NumberAgr#SINGULAR} by default.</LI>
 * <LI>A {@link simplenlg.features.Gender} feature. Again, this is mostly useful
 * for pronominalisation since third-person personal pronouns carry gender
 * marking. {@link simplenlg.features.Gender#NEUTER} by default.</LI>
 * </OL>
 * 
 * @author ereiter, agatt
 */
public class NPPhraseSpec extends HeadedPhraseSpec<Noun> {

	Person person;
	NumberAgr number;
	Gender gender;
	Case cs;
	Quantification quant;
	
	boolean elideSpecifier; // elides specifier; necessary for contractions (in dem -> im)
	
	// components
	// private String specifier; // eg, the, a, your, ...
	Object specifier;

	Pronoun pronoun; // pronominal form of this NP
	String pronounForm; // set according to whether it's subject or object
	boolean treatAsPlural; // treat as plural
	boolean pronominal; // realise as pronoun
	boolean possessive; // inflect the head with possessive 's
	boolean raised; // true if this is a complement but is raised to subject
	boolean isAcronym; // true if this is treated as a proper name
	
	/* NPPhraseSpec can be made to refer to a different NP;
	 * this is useful to make pronominal NPs agree with an
	 * already existing NP 
	 */
	boolean refering;
	NPPhraseSpec refNP;
	
	// *****************************************
	// Constructors
	// *****************************************

	/**
	 * Constructs an empty NPPhraseSpec. This sets gender to
	 * {@link simplenlg.features.Gender#NEUTER}, number to
	 * {@link simplenlg.features.NumberAgr#SINGULAR} and person to
	 * {@link simplenlg.features.Person#THIRD}.
	 */
	public NPPhraseSpec() {
		super();
		this.elideSpecifier = false;
		this.treatAsPlural = false;
		this.pronominal = false;
		this.possessive = false;
		this.raised = false;
		this.isAcronym = false;
		this.quant = null;
		// head = new Noun("");
		this.specifier = new Determiner("");
		this.category = Category.NOUN;
		this.function = DiscourseFunction.NULL;
		this.number = NumberAgr.SINGULAR;
		this.person = Person.THIRD;
		this.gender = Gender.NEUTER;
		this.cs = Case.NOMINATIVE;
		this.pronoun = null;
		this.specifier = null;
		this.premodifierComparator = PhraseComparator.defaultInstance();
		this.refering = false;
		this.refNP = null;
	}

	/**
	 * Constructs an NPPhraseSpec with the specified noun. This sets gender to
	 * {@link simplenlg.features.Gender#NEUTER}, number to
	 * {@link simplenlg.features.NumberAgr#SINGULAR} and person to
	 * {@link simplenlg.features.Person#THIRD}.
	 * 
	 * @param noun
	 *            The baseform (<code>String</code>) of the head noun
	 */
	public NPPhraseSpec(String noun) {
		this();
		// Doesn't quite work this way ... noun could have spaces without having a determiner
/*		if (noun.indexOf(" ") > -1) {
			String det = noun.substring(0, noun.indexOf(" "));
			String head = noun.substring(noun.indexOf(" "));
			
			setSpecifier(det);
			setHead(head);
		} else {
			setHead(noun);
		}*/
		
		setHead(noun);
	}

	/**
	 * Constructs an NPPhraseSpec with the specified noun and specifier, given
	 * as Strings. This sets gender to {@link simplenlg.features.Gender#NEUTER},
	 * number to {@link simplenlg.features.NumberAgr#SINGULAR} and person to
	 * {@link simplenlg.features.Person#THIRD}.
	 * 
	 * @param spec
	 *            The specifier
	 * @param noun
	 *            The noun
	 */
	public NPPhraseSpec(String spec, String noun) {
		this(noun);

		if (!setSpecifier(spec)) {
			throw new SimplenlgException(
					"Cannot set the specifier supplied to constructor.");
		}

	}

	/**
	 * Constructs an an NPPhraseSpec with the specified noun and specifier. This
	 * sets number to {@link simplenlg.features.NumberAgr#SINGULAR} and person to
	 * {@link simplenlg.features.Person#THIRD}, while extracting gender from the
	 * supplied Noun object.
	 * 
	 * @param spec
	 *            The specifier, must be a <code>String</code> or a
	 *            {@link simplenlg.realiser.Phrase}
	 * @param noun
	 *            The head noun
	 */
	public NPPhraseSpec(Object spec, Noun noun) {
		this();
		setHead(noun);

		if (!setSpecifier(spec)) {
			throw new SimplenlgException(
					"Cannot set the specifier supplied to constructor.");
		}
	}

	/**
	 * Constructs an NPPhraseSpec with the specified noun. This
	 * sets number to {@link simplenlg.features.NumberAgr#SINGULAR} and person to
	 * {@link simplenlg.features.Person#THIRD}, while extracting gender from the
	 * supplied Noun object.
	 * 
	 * @param noun
	 *            The head noun
	 */
	public NPPhraseSpec(Noun noun) {
		this();
		setHead(noun);
	}

	public NPPhraseSpec(NPPhraseSpec refererNP) {
		this();
		setReference(refererNP);
	}
	
	public NPPhraseSpec(NPPhraseSpec refererNP, Pronoun pro) {
		this(refererNP);
		setPronoun(pro);
		this.pronominal = true;
	}

	// *****************************************
	// getters/setters
	// *****************************************

	/**
	 * Adds a modifier, trying to guess its position as follows:
	 * <OL>
	 * <LI>If the modifier passed is an {@link AdjPhraseSpec}, it is placed in
	 * premodifier position.
	 * <LI>If the modifier is a {@link PPPhraseSpec}, then it is placed in
	 * postmodifier position.
	 * <LI>Otherwise, the heuristics are as per the superclass
	 * {@link HeadedPhraseSpec}
	 * </OL>
	 * 
	 * @param mod
	 *            The modifier
	 * 
	 * @see HeadedPhraseSpec#addModifier(Object)
	 */
	@Override
	public void addModifier(Object mod) {

		if (mod instanceof AdjPhraseSpec) {
			addPremodifier(mod);
		} else if (mod instanceof PPPhraseSpec) {
			addPostmodifier(mod);
		} else if ((mod instanceof NPPhraseSpec) ||
				(mod instanceof Pronoun)) {
			addPremodifier(mod);
		} else {
			super.addModifier(mod);
		}
	}
	
	public void addRelativeClause(SPhraseSpec s) {
		addModifier(s);
	}
	
	// TODO: experimental
	public void addRelativeClause(SPhraseSpec s, DiscourseFunction d) {
		addRelativeClause(s, d, Constants.PRO_REL);
	}
	
	// TODO: experimental
	/* "cloning" of phrase is problematic because features that are changed
	 * after adding a relative clause will not be changed for the relative
	 * pronoun (e.g., setting the NP to plural will not affect the relative
	 * pronoun generated here, leading to ungrammatical realisations)
	 */
	public void addRelativeClause(SPhraseSpec s, DiscourseFunction d, Pronoun p) {
		//NPPhraseSpec pro = new NPPhraseSpec(this.head);
		//pro.setNumber(this.number);
		//pro.setPronoun(p);
		//pro.setPronominal(true);
		NPPhraseSpec np = new NPPhraseSpec(this, p);

		s.setComplementiser(np, d);
		this.addAttributivePostmodifier(s);
	}
	
	// TODO: experimental
	public void addRelativeClause(SPhraseSpec s, Preposition p) {
		addRelativeClause(s, p, Constants.PRO_REL);
	}
	
	// TODO: experimental
	public void addRelativeClause(SPhraseSpec s, Preposition p, Pronoun pn) {
		NPPhraseSpec np = new NPPhraseSpec(this, pn);
		PPPhraseSpec pro = new PPPhraseSpec(p, np);
		
		s.setComplementiser(pro);
		this.addAttributivePostmodifier(s);
	}
	
	public void addAttributiveRelativeClause(SPhraseSpec s, NPPhraseSpec np) {
		NPPhraseSpec rnp = new NPPhraseSpec(this, Constants.PRO_REL);
		np.setSpecifier(rnp);
		
		s.setComplementiser(np, DiscourseFunction.SUBJECT);
		this.addAttributivePostmodifier(s);
	}
	
	/**
	 * Sets the specifier (replaces existing specifier) The specifier of the
	 * noun phrase is a {@link simplenlg.lexicon.lexicalitems.Determiner}, a
	 * {@link simplenlg.lexicon.lexicalitems.Pronoun} or another phrase (usually
	 * a possessive NP). These are exemplified below.
	 * 
	 * <ol>
	 * <li>(Specifier as pronoun): <i><u>her</u> mother</i></li>
	 * <li>(Specifier as determiner): <i><u>the</u> dog</i></li>
	 * <li>(Phrase specifier): <i><u>the old man's</u> dog</i></li>
	 * </ol>
	 * 
	 * <P>
	 * In addition to the above, the method can be passed a string. This is
	 * treated as follows:
	 * 
	 * <OL>
	 * <LI>If one of the prespecified determiners exists in
	 * {@link simplenlg.lexicon.lexicalitems.Determiner} which has this string
	 * as baseform, the specifier of the NP is set to this determiner. For
	 * instance passing the method the string "a" will result in the specifier
	 * becoming {@link simplenlg.lexicon.lexicalitems.Constants#INDEFINITE_SG}.</LI>
	 * <LI>otherwise, the string is converted to an
	 * {@link simplenlg.realiser.StringPhraseSpec}.</LI>
	 * </OL>
	 * </P>
	 * 
	 * <p>
	 * This method can also be passed the value <code>null</code>, in which case
	 * the specifier of the noun phrase is unset. Note that, if the NP has a
	 * specifier, but is then pronominalised using
	 * {@link #setPronominal(boolean)}, the specifier is not realised.
	 * </p>
	 * 
	 * @param spec
	 *            The specifier, a <code>String</code>,
	 *            {@link simplenlg.lexicon.lexicalitems.Determiner},
	 *            {@link simplenlg.lexicon.lexicalitems.Pronoun} or
	 *            {@link simplenlg.realiser.Phrase}
	 * 
	 * @return <code>true</code> if the specifier has been set successfully.
	 */
	public boolean setSpecifier(Object spec) {

		if (spec == null) {
			this.specifier = spec;
			return true;

		} else if (spec instanceof String) {
			this.specifier = Constants.getDeterminer((String) spec);

			if (this.specifier != null) {
				return true;
			}
			
			// TODO: decide whether this stays deactivated permanently
			// this.specifier = Constants.getPronoun((String) spec);
			
			if (this.specifier != null) {
				return true;
			}
			
		} else if (spec instanceof Determiner) {
			this.specifier = spec;
			return true;
		} else if (spec instanceof Pronoun) {
			this.specifier = spec;
			return true;
		}

		try {
			this.specifier = makeConstituent(spec, null);
			return true;

		} catch (SimplenlgException se) {
			return false;
		}
	}

	/**
	 * An alias for {@link #setSpecifier(Object)}. This will set the specifier
	 * of the NP.
	 * 
	 * @param spec
	 *            The determiner or phrase specifier.
	 */
	public void setDeterminer(Object spec) {
		setSpecifier(spec);
	}

	/**
	 * Gets the specifier.
	 * 
	 * @return The specifier of this NP, if it has one, <code>null</code>
	 *         otherwise.
	 */
	public Object getSpecifier() {
		return this.specifier;
	}

	/**
	 * Checks for specifier.
	 * 
	 * @return <code>true</code> if the specified has been set
	 */
	public boolean hasSpecifier() {
		return this.specifier != null;
	}

	/**
	 * {@inheritDoc} <br>
	 * 
	 * @see #setHead(Noun)
	 * @see #setPronoun(Pronoun)
	 * 
	 */
	@Override
	public void setHead(String n) {
		Pronoun p = Constants.getPronoun(n);

		if (p != null) {
			setHead(p);
			this.number = Constants.getPronounNumber(n);
		} else {
			setHead(new Noun(n));
		}
	}

	/**
	 * {@inheritDoc} <br>
	 * <strong>Note</strong> In the case of <code>NPPhraseSpec</code>, the noun
	 * passed as parameter to this method is first checked to see if it matches
	 * any of the personal or expletive pronouns in the
	 * {@link simplenlg.lexicon.lexicalitems.Pronoun} class, in which case, it
	 * is the <code>pronoun</code> field that is set, the
	 * <code>pronominal</code> feature set to <code>true</code> and the person,
	 * number and gender features of the phrase are set accordingly.
	 * 
	 * @see #setPronoun(Pronoun)
	 * 
	 */
	@Override
	public void setHead(Noun n) {

		if (n instanceof Pronoun) {
			setPronoun((Pronoun) n);
			setPerson(((Pronoun) n).getPerson());
			this.pronominal = true;
		} else {
			this.head = n;
			setPerson(Person.THIRD);
		}
		
		setGender(n.getGender());
		Agreement agr = n.getAgreement();
		if (agr != null && agr == Agreement.FIXED_PLUR) {
			setPlural(true);
		}

	}

	/**
	 * Sets whether the NP is plural.
	 * <P>
	 * For example, if the noun is "apple", this is realised as
	 * <UL>
	 * <li><i>apple</i> if the parameter is <code>false</code>
	 * <li><i>apples</i> if the parameter is <code>true</code>
	 * </UL>
	 * 
	 * If the NP is set to plural, and its specifier was previously set as an
	 * indefinite determiner (i.e.
	 * {@link simplenlg.lexicon.lexicalitems.Constants#INDEFINITE_SG}) the
	 * determiner is automatically set to
	 * {@link simplenlg.lexicon.lexicalitems.Constants#INDEFINITE_PL}. Thus, if
	 * the NP was previously <i>an apple</i>, and this method is called with
	 * parameter <code>true</code> then it is now realised as <i>some
	 * apples</i>. This is done at realisation stage.
	 * 
	 * @param plur
	 *            plural if <code>true</code>
	 * 
	 * @see #setNumber(NumberAgr)
	 */
	public void setPlural(boolean plur) {
		this.number = plur ? NumberAgr.PLURAL : NumberAgr.SINGULAR;
	}

	/**
	 * Sets the number feature for this NP.
	 * 
	 * @param num
	 *            The {@link simplenlg.features.NumberAgr} feature
	 * 
	 * @see #setPlural(boolean)
	 */
	public void setNumber(NumberAgr num) {
		this.number = num;
	}

	/**
	 * Gets the number.
	 * 
	 * @return The {@link simplenlg.features.NumberAgr} feature of this noun
	 *         phrase.
	 */
	public NumberAgr getNumber() {
		return this.number;
	}

	/**
	 * Sets the person feature.
	 * 
	 * @param p
	 *            The {@link simplenlg.features.Person} feature to be set.
	 */
	public void setPerson(Person p) {
		this.person = p;
	}

	/**
	 * Gets the person.
	 * 
	 * @return The {@link simplenlg.features.Person} feature of this noun
	 *         phrase.
	 */
	public Person getPerson() {
		return this.person;
	}

	/**
	 * Sets the gender feature.
	 * 
	 * @param g
	 *            The {@link simplenlg.features.Gender} feature to be set.
	 */
	public void setGender(Gender g) {
		this.gender = g;
	}

	/**
	 * Gets the gender.
	 * 
	 * @return The {@link simplenlg.features.Gender} feature of this noun
	 *         phrase.
	 */
	public Gender getGender() {
		return this.gender;
	}

	public Case getCase() {
		return this.cs;
	}

	public void setCase(Case cs) {
		this.cs = cs;
	}

	public boolean isDefinite() {
		return this.quant == Quantification.DEFINITE;
	}
	
	public Quantification getQuantification() {
		return this.quant;
	}
	
	/**
	 * Sets quantification of this noun phrase. Does <i>not</i> change the
	 * determiner unless none was specified before.
	 * 
	 * @param q
	 * 			The new quantification valu.
	 */
	public void setQuantification(Quantification q) {
		this.quant = q;
		if (!this.hasSpecifier()) {
			if (q == Quantification.DEFINITE) {
				this.setDeterminer(Constants.getDeterminer(true));
			} else if (q == Quantification.INDEFINITE) {
				this.setDeterminer(Constants.getDeterminer(false));
			}
		}
	}

	/**
	 * Specifies what the pronominal form of this noun phrase must be. Note that
	 * this does <i>not</i> make the phrase be realised as a pronoun. For it to
	 * be realised as such, the {@link #setPronominal(boolean)} method must be
	 * set to <code>true</code>.
	 * 
	 * @param pro
	 *            The pronoun as a <code>String</code>
	 * 
	 * @see #setPronoun(Pronoun)
	 * @see #setPronominal(boolean)
	 */

	public void setPronoun(String pro) {
		this.pronoun = Constants.getPronoun(pro);

		if (this.pronoun == null) {
			this.pronoun = new Pronoun(pro);
		}
	}

	/**
	 * Specifies what the pronominal form of this noun phrase must be. Note that
	 * this does <i>not</i> make the phrase be realised as a pronoun. For it to
	 * be realised as such, the {@link #setPronominal(boolean)} method must be
	 * set to <code>true</code>.
	 * 
	 * @param pro
	 *            The pronoun
	 * 
	 * @see #setPronoun(String)
	 * @see #setPronominal(boolean)
	 */

	public void setPronoun(Pronoun pro) {
		this.pronoun = pro;
	}

	/**
	 * Returns the pronoun associated with this NP. If this has been previously
	 * set, the value returned is the value set, otherwise, the value is the
	 * pronoun corresponding to the person/number/gender/possessive features of
	 * this NP.
	 * 
	 * @return The pronominal form.
	 */
	public Pronoun getPronoun() {

		if (this.pronoun != null) {
			return this.pronoun;
		} else if (this.possessive) {
			return Constants.getPossessivePronoun(this.person, this.number,
					this.gender);
		} else {
			return Constants.getPersonalPronoun(this.person);
		}
	}
	

	/**
	 * Gets the pronoun as string.
	 * 
	 * @return The <code>String</code> value of the pronoun associated with this
	 *         NP.
	 * 
	 * @see #getPronoun()
	 */
	public String getPronounAsString() {
		return getPronoun().getBaseForm();
	}

	/**
	 * Sets whether this phrase should be realised as a pronoun. If the pronoun
	 * form has been set explicitly, using {@link #setPronoun(Pronoun)} or
	 * {@link #setPronoun(String)}, this is the form in which the phrase is
	 * realised. Otherwise, the pronoun form will be determined at the
	 * realisation stage using the person, number and gender simplenlg.features
	 * of this phrase, as well as its discourse function.
	 * 
	 * <P>
	 * For example, if the NP <i>the man</i> has the discourse function
	 * {@link simplenlg.features.DiscourseFunction#OBJECT}, and its gender has
	 * been set to {@link simplenlg.features.Gender#MASCULINE}, the pronominal
	 * form selected is
	 * {@link simplenlg.lexicon.lexicalitems.Constants#PRO_3SGM_NOM}, and the
	 * form of the pronoun returned is the one corresponding to the function,
	 * namely <i>him</i>.
	 * 
	 * <P>
	 * Setting a noun phrase as pronominal does <i>not</i> make the phrase lose
	 * its constituent information (head, complements, specifier etc).
	 * Therefore, unsetting this feature by passing this method
	 * <code>false</code> will simply make this phrase be realised as a full
	 * phrase.
	 * 
	 * @param pro
	 *            If <code>true</code>, the phrase is realised as a pronoun.
	 */
	public void setPronominal(boolean pro) {
		this.pronominal = pro;
	}

	/**
	 * Checks if is pronominal.
	 * 
	 * @return <code>true</code> if this phrase has been set to be realised as a
	 *         pronoun
	 */
	public boolean isPronominal() {
		return this.pronominal;
	}

	/**
	 * Checks if is plural.
	 * 
	 * @return <code>true</code> if this NP is syntactically plural, i.e. if
	 *         either {@link #setPlural(boolean)} or
	 *         {@link #setTreatAsPlural(boolean)} have been set to
	 *         <code>true</code>
	 */
	public boolean isPlural() {
		return this.number.equals(NumberAgr.PLURAL) || this.treatAsPlural;
	}

	public boolean isElideSpecifier() {
		return elideSpecifier;
	}

	public void setElideSpecifier(boolean elideSpecifier) {
		this.elideSpecifier = elideSpecifier;
	}

	/**
	 * Force this NP to be treated as an. This is useful for NPs consisting of
	 * actual names or acronyms, whose capitalisation should not be altered by
	 * the realiser's default behaviour.
	 * 
	 * @param name
	 *            whether to treat the NP as a proper name
	 */
	public void setIsAcronym(boolean name) {
		this.isAcronym = name;
	}

	/**
	 * Check if this NP is an acronym. The method returns true if
	 * {@link #setIsAcronym(boolean)} has been set to <code>true</code>.
	 * 
	 * @return <code>true</code> if this NP is treated as a name
	 */
	public boolean isAcronym() {
		return this.isAcronym;
	}

	/**
	 * If <code>true</code>, the NP is not inflected as a plural, but is
	 * syntactically treated as a plural
	 * <P>
	 * This is useful if a plural noun is specified. For example, if setNoun
	 * specifies "apples" as the noun, then treatAsPlural should be set.
	 * 
	 * @param treatAsPlural
	 *            If <code>true</code>, makes the head noun be treated as
	 *            plural, but not inflected using plural morphology.
	 */
	public void setTreatAsPlural(boolean treatAsPlural) {
		this.treatAsPlural = treatAsPlural;
	}

	/**
	 * If set to <code>true</code>, the NP will be realised as follows:
	 * 
	 * <OL>
	 * <LI>If the phrase is pronominal (i.e. {@link #setPronominal(boolean)} is
	 * set to <code>true</code> the possessive form of the pronoun is returned,
	 * <i>unless</i> the pronominal form has been explicitly set using
	 * {@link #setPronoun(String)} or {@link #setPronoun(Pronoun)}.
	 * 
	 * <LI>If the phrase is not pronominal, then the head is realised with the
	 * genitive <i>'s</i> clitic.
	 * </OL>
	 * 
	 * @param poss
	 *            Whether this phrase is possessive
	 */
	public void setPossessive(boolean poss) {
		this.possessive = poss;
	}

	/**
	 * Checks if is possessive.
	 * 
	 * @return <code> true</code> if this NP has been specified as possessive.
	 */
	public boolean isPossessive() {
		return this.possessive;
	}

	/**
	 * Checks if is expletive.
	 * 
	 * @return <code>true</code> iff: (a) this <code>NPPhraseSpec</code> has
	 *         been set to <code>pronominal</code> and (b) the pronoun
	 *         associated with this phrase is expletive.
	 * 
	 * @see #setPronominal(boolean)
	 * @see #setPronoun(Pronoun)
	 * @see simplenlg.lexicon.lexicalitems.Pronoun#isExpletive()
	 */
	public boolean isExpletive() {

		if (this.pronominal && this.pronoun != null) {
			return getPronoun().isExpletive();
		}

		return false;
	}
	
	/**
	 * Sets an NPPhraseSpec object from which agreement features are copied.
	 * Also, sets this NPPhraseSpec to pronominal realisation.
	 * 
	 * @param refererNP The NP from which agreement features should be copied
	 */
	public void setReference(NPPhraseSpec refererNP) {
		if (refererNP == null) {
			this.refering = false;
			this.refNP = null;
		} else {
			this.refering = true;
			this.refNP = refererNP;
			this.pronominal = true;
		}
	}
	
	public NPPhraseSpec getReference() {
		return this.refNP;
	}


	// ********************************************
	// REALISATION
	// ********************************************

	/**
	 * {@inheritDoc}
	 */
	public NPPhraseSpec coordinate(Phrase... coords) {

		if (coords.length == 0) {
			return this;
		}

		CoordinateNPPhraseSpec coord = new CoordinateNPPhraseSpec(this);

		try {
			for (Phrase p : coords) {
				coord.addCoordinates((NPPhraseSpec) p);
			}

			return coord;

		} catch (ClassCastException cce) {
			throw new SimplenlgException("Cannot coordinate: "
					+ "only phrases of the same type can be coordinated");
		}

	}

	/**
	 * {@inheritDoc simplenlg.realiser.HeadedPhrase} In addition, also checks
	 * whether <code>o</code> has the same specifier as this phrase.
	 * 
	 * @param o
	 *            the o
	 * 
	 * @return true, if equals
	 */
	@Override
	public boolean equals(Object o) {

		if (o instanceof NPPhraseSpec && super.equals(o)) {
			NPPhraseSpec np = (NPPhraseSpec) o;

			if (np.hasSpecifier()) {

				if (hasSpecifier()) {
					return np.specifier.equals(this.specifier);
				} else {
					return false;
				}
			} else {
				return !hasSpecifier();
			}
		}

		return false;
	}

	// ********************************************
	// REALISATION
	// ********************************************

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * simplenlg.realiser.HeadedPhraseSpec#realise(simplenlg.realiser.Realiser)
	 */
	@Override
	String realise(Realiser r) {

		if (this.refering) { this.pronominal = true; }
		
		// set adjective agreement features
		computeModifierAgreement();
		
		// set genitive case for NP complements
		computeArgumentCase();
		
		// check if the determiner needs to be pluralised
		checkPluralDetFeatures();

		// do nothing if elided
		if (this.elided) {
			return "";
		} else {
			Case c = this.cs;  // HACK; case might get changed in modifiers
			String realisation = super.realise(r);
			String negation = this.negated ? "nicht" : "";
			this.cs = c;
			realisation = r.appendSpace(negation,
					realiseSpec(r, realisation), realisation);
			return realisation;
		}
	}

	/*
	 * realise the specifier arg0 = the realiser (needed in case specifier is a
	 * phrase) arg1 = agreement string (needed to compute det form in case of
	 * indefinite a/an)
	 */
	String realiseSpec(Realiser r, String agreementString) {

		if (this.specifier == null || this.elideSpecifier || this.pronominal) {
			return "";
		} else if (this.specifier instanceof Determiner) {
			return ((Determiner) this.specifier).getForm(this.cs, this.number, this.gender);
		} else if (this.specifier instanceof Pronoun) {
			return ((Pronoun) this.specifier).getSpecifierForm(this.cs, this.number, this.gender);
		} else {
			return ((PhraseSpec) this.specifier).realise(r);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * simplenlg.realiser.HeadedPhraseSpec#realiseHead(simplenlg.realiser.Realiser
	 * )
	 */
	@Override
	String realiseHead(Realiser r) {
		if (this.pronominal) return realisePronominalHead(r);
		
		if (this.head == null || this.head.getBaseForm() == null
				|| this.head.getBaseForm().length() == 0) {
			if (this.pronoun == null || this.pronoun.getBaseForm() == null
					|| this.pronoun.getBaseForm().length() == 0) {
				return "";
			} else {
				this.pronominal = true;
				return realisePronominalHead(r);
			}
		}
		
		// TODO: hack for nouns that inflect like adjectives
		//		 (ie., inflection depends on the specifier)
		if (this.head.isAdjectiveInflection()) {
			AdjPhraseSpec a = new AdjPhraseSpec(this.head.getBaseAdjective());
			a.setAgreementNP(this);
			String realisation = a.realise(r);
			return Constants.capitalize(realisation);
		}
		else {
			if (this.head instanceof Pronoun) {
				return ((Pronoun) this.head).getForm(this.cs, this.number, this.gender);
			} else {
				return this.head.getForm(this.cs, this.number);
			}
		}
		
	}

	// This method replaces checkPronounHeadFeatures() in GermanNLG
	String realisePronominalHead(Realiser r) {
		if (this.pronoun == null) {
			this.pronoun = Constants.PRO_3RD;
		}

		String realisation;
		if (this.refering) {
			realisation = this.pronoun.getForm(this.cs, 
											   this.refNP.getNumber(), 
											   this.refNP.getGender());
		} else {
			realisation = this.pronoun.getForm(this.cs, this.number, this.gender);
		}
		if (this.negated) { realisation = r.appendSpace("nicht", realisation); }
		
		return realisation;
	}
	
	/*
	 * Overriden because pronoun and adjective modifiers need to be realised
	 * differently, both in regard to inflection and punctuation
	 * 
	 * @see simplenlg.realiser.HeadedPhraseSpec#realisePremodifier(simplenlg.realiser.Realiser)
	 */
	@Override
	String realisePremodifier(Realiser r) {
		List<Phrase> pronominalModifiers = new ArrayList<Phrase>();
		List<Phrase> adjectiveModifiers = new ArrayList<Phrase>();
		
		if (this.premodifierComparator != null) {
			Collections.sort(this.premodifiers, this.premodifierComparator);
		}

		for (Phrase p : this.premodifiers) {
			if (p != null) {
				if (p instanceof NPPhraseSpec) {
					AdjPhraseSpec adj = new AdjPhraseSpec(((NPPhraseSpec) p).getPronounAsString());
					adj.setAgreementNP(this);
					pronominalModifiers.add(adj);
				} else if (p instanceof AdjPhraseSpec) {
					adjectiveModifiers.add(p);
				} else {
					pronominalModifiers.add(p);
				}
			}
		}
		
		String pre = r.realiseList(pronominalModifiers);
		pre = r.appendSpace(pre, r.realiseConjunctList(adjectiveModifiers, ","));

		return pre == null ? "" : pre;
	}

	
	/*
	 * Sets whether this NP is a complement raised to subject position. We don't
	 * change its discourse function, as this will interfere with the VP's
	 * passive complement raising.
	 */
	void setRaised(boolean raise) {
		this.raised = raise;
	}

	// ********************************************
	// UTILITY METHODS
	// ********************************************

	/*
	 * Check if the head of NP is a pronoun and set features accordingly. Also
	 * sets the form of the pronoun to return, depending on whether it is
	 * subject or object.
	 * 
	 * Replaced by method "realisePronominalHead".
	 * 
	 * @deprecated
	 */
	@Deprecated
	void checkPronounHeadFeatures() {

/*		// nothing to do about expletives
		if (isExpletive()) {
			this.pronounForm = this.pronoun.getBaseForm();
			return;
		}

		// the case of the pronoun we need
		Case pronounCase = Case.NOMINATIVE;
		Case functionCase = this.raised ? Case.NOMINATIVE : this.function
				.getCaseValue();

		if (functionCase == null) {
			functionCase = Case.NOMINATIVE;
		}

		if (this.pronoun != null) {
			this.number = this.pronoun.getNumber();
			this.gender = this.pronoun.getGender();
			this.person = this.pronoun.getPerson();
			pronounCase = this.pronoun.getCaseValue();
		}

		if (functionCase != pronounCase) {
			pronounCase = functionCase;
		}

		Pronoun pro = Constants.getPronoun(this.person, this.number,
				this.gender, pronounCase, this.possessive);

		if (pro == null) {
			System.err.println("NULL PRO: " + this.pronoun + " " + this.person
					+ " " + this.number + " " + this.gender + " " + pronounCase
					+ " " + this.possessive);
		}

		this.pronounForm = pro.getBaseForm();*/
	}

	/**
	 * Sets agreement features for modifying AdjPhraseSpecs or pronoun NPs
	 */
	void computeModifierAgreement() {
		for (Phrase p : this.getPremodifiers()) {
			if (p != null) {
				if (p instanceof AdjPhraseSpec) {
					((AdjPhraseSpec) p).setAgreementNP(this);
					
					if (this.pronominal) {
						p.setElided(true);
					}
				}
				else if (p instanceof NPPhraseSpec) {
					// ((NPPhraseSpec) p).setAgreementNP(this);

					if (this.pronominal) {
						p.setElided(true);
					}
				}
			}
		}
	}
	
	/**
	 * Sets genitive case for NP complements, as in "das Kind des Mannes",
	 * and for NP specifier, as in "des Mannes Kind"
	 */
	void computeArgumentCase() {
		if (this.specifier instanceof NPPhraseSpec) {
			((NPPhraseSpec) this.specifier).setCase(Case.GENITIVE);
		}
		for (Phrase p : this.complements) {
			if (p != null & p instanceof NPPhraseSpec) {
				((NPPhraseSpec) p).setCase(Case.GENITIVE);
			}
		}
	}
	
	/*
	 * Check if this NP is plural. If the determiner is singular, pluralise it.
	 */
	@Deprecated
	void checkPluralDetFeatures() {

/*		if (this.number.equals(NumberAgr.PLURAL)) {
			if (this.specifier instanceof Determiner
					&& ((Determiner) this.specifier)
							.equals(Constants.INDEFINITE_SG)) {
				this.specifier = Constants.INDEFINITE_PL;
			}
		}
*/	}
	
	void initializeArgs() {
		// TODO: is not complete yet
		this.elideSpecifier = false;
		//this.pronominal = false;
		this.possessive = false;
		this.raised = false;
	}
	
}
