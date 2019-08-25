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
import simplenlg.features.*;
import simplenlg.lexicon.LexiconInterface;
import simplenlg.lexicon.lexicalitems.Constants;
import simplenlg.lexicon.lexicalitems.Verb;
import simplenlg.realiser.comparators.WordOrder;

import java.util.*;

/**
 * This class represents a verb phrase. In addition to the simplenlg.features
 * (premodifiers, postmodifiers and complements) inherited from
 * {@link simplenlg.realiser.HeadedPhraseSpec}, verb phrases have the following
 * simplenlg.features, for which getter and setter methods are supplied.
 * 
 * <OL>
 * <LI>modal auxiliary: E.g. <i><strong>must</strong> be the man behind the
 * curtain</i></LI>
 * <LI>tense: This is a value in {@link simplenlg.features.Tense}</LI>
 * <LI>Inflectional agreement simplenlg.features, namely:
 * <UL>
 * <LI>{@link simplenlg.features.Person}; cf. the difference between <i>We
 * <strong>are</strong> all craxy</i> and <i>he <strong>is</strong> crazy</i></LI>
 * <LI>{@link simplenlg.features.NumberAgr}; cf. the examples above, where
 * <i>are</i> is plural and <i>is</i> singular</LI>
 * </UL>
 * These simplenlg.features can be set manually, but in case a full sentence is
 * constructed, they are also automatically handled within the
 * {@link simplenlg.realiser.SPhraseSpec}.</LI>
 * <LI>perfect: A <code>boolean</code> value specifying whether the VP is
 * perfective. This feature interacts with tense, as exemplified below:
 * <UL>
 * <LI><i><strong>was eaten</strong></i> (+past, -perfect)</LI>
 * <LI><i><strong>had been eaten</strong></i> (+past, +perfect)</LI>
 * <LI><i><strong>has been eaten</strong></i> (+present, +perfect)
 * </UL>
 * </LI>
 * <LI>progressive: A <code>boolean</code> value, exemplified below:
 * <UL>
 * <LI><i><strong>was eating</strong></i> (+progressive)</LI>
 * <LI><i><strong>ate</strong></i> (-progressive)</LI>
 * </UL>
 * </LI>
 * <LI>passive: <code>boolean</code>, determines whether the VP is passive.</LI>
 * <LI>negated: <code>boolean</code>, determines whether the VP is negated.</LI>
 * <LI>Form: a value of {@link simplenlg.features.Form}</LI>
 * </OL>
 * 
 * @author agatt
 */
public class VPPhraseSpec extends HeadedPhraseSpec<Verb> {

	/** The realise auxiliary. */
	boolean perfect, progressive, passive, realiseAuxiliary;
	boolean mainVerbPerfect;

	/** The modal(s). */
	Verb modal;
	List<Verb> modals;

	/** The tense. */
	Tense tense;

	/** The person. */
	Person person;

	/** The number. */
	NumberAgr number;

	// Mood mood;

	/** The form. */
	Form form;

	// private vars to hold components of a realisation
	/** The auxiliary realisation. */
	String auxiliaryRealisation; // realisation of auxiliaries

	/** The main verb realisation. */
	String mainVerbRealisation; // realisation of main verb (and trailing "not")

	// mmb
	PPPhraseSpec passiveComplement;
	Position passiveComplementPosition;
	boolean passiveComplementRealisation;
	
	CoordinatePhraseSet<Phrase> surfaceSubject;
	String finiteVerbRealisation;
	String verbClusterRealisation;
	List<String> constituentRealisation;
	String vorfeldRealisation;
	
	HashMap<Position, List<Phrase>> constituentMap;
	WordOrder wordOrder;
	
	boolean suppressVorfeld;
	
	// currently used only for imperative form
	boolean honorific;
	
	/** The vg components. */
	Stack<String> vgComponents; // holds the components of the verb group

	// the argument to suppress in case of a WH question
	boolean suppressSubject, suppressObject, suppressIndirectObject;

	// interrogative features
	/** The interrogative. */
	boolean interrogative; // true if sentence is a question

	/** The interrog type. */
	InterrogativeType interrogType;

	/** The wh argument. */
	DiscourseFunction whArgument;

	
	/**
	 * Constructs an empty VPPhraseSpec.
	 */
	public VPPhraseSpec() {
		super();
		this.category = Category.VERB;
		this.perfect = false;
		this.progressive = false;
		this.passive = false;
		this.negated = false;
		this.modal = null;
		this.modals = new ArrayList<Verb>();
		this.tense = Tense.PRESENT;
		this.person = Person.THIRD;
		this.number = NumberAgr.SINGULAR;
		// mood = Mood.NORMAL;
		this.form = Form.NORMAL;
		this.head = new Verb("");
		this.realiseAuxiliary = true;
		this.auxiliaryRealisation = "";
		this.mainVerbRealisation = "";
		this.mainVerbPerfect = false;
		this.vgComponents = new Stack<String>();
		this.suppressSubject = false;
		this.suppressObject = false;
		this.suppressIndirectObject = false;
		this.honorific = false;
		this.interrogative = false;
		this.interrogType = null;
		this.whArgument = DiscourseFunction.NULL;
		// defines default word order:
		this.wordOrder = WordOrder.SIO;
		// new way of storing constituents:
		this.constituentMap = new HashMap<Position, List<Phrase>>();
		this.constituentRealisation = new ArrayList<String>();
		this.surfaceSubject = new CoordinatePhraseSet<Phrase>(this);
		this.passiveComplement = null;
		this.passiveComplementPosition = Position.DEFAULT;
		this.passiveComplementRealisation = true;
	}


	/**
	 * Constructs a <code>VPPhraseSpec</code> headed by a verb with the
	 * specified base form.
	 * 
	 * @param v
	 *            The baseform of the head verb, a <code>String</code>
	 */
	public VPPhraseSpec(String v) {
		this();
		setHead(v);
	}

	/**
	 * Constructs a <code>VPPhraseSpec</code> headed by the specified
	 * {@link simplenlg.lexicon.lexicalitems.Verb}.
	 * 
	 * @param v
	 *            The verb
	 */
	public VPPhraseSpec(Verb v) {
		this();
		setHead(v);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simplenlg.realiser.HeadedPhraseSpec#setHead(java.lang.String)
	 */
	@Override
	public void setHead(String v) {
		this.head = castToVerb(v);
	}

	/**
	 * {@inheritDoc simplenlg.realiser.HeadedPhraseSpec}. In the verb phrase,
	 * complements can be direct objects or indirect objects. By default, any
	 * object passed as complement is assigned the function
	 * {@link simplenlg.features.DiscourseFunction#OBJECT}.
	 * 
	 * <P>
	 * Complements can be phrases or strings (which are converted to
	 * {@link simplenlg.realiser.StringPhraseSpec}s. If a complement is a
	 * sentence, i.e. an {@link simplenlg.realiser.SPhraseSpec}, the following
	 * occurs:
	 * <OL>
	 * <LI>the sentence is set to subordinate; see
	 * {@link simplenlg.realiser.SPhraseSpec#setClauseStatus(simplenlg.features.ClauseStatus)}
	 * </LI>
	 * <LI>if the sentence is in the imperative, its form is set to
	 * {@link simplenlg.features.Form#INFINITIVE}. E.g. a sentence like <i>Go
	 * out</i>, embedded as a complement of <i>tell</i> is realised as <i>tell
	 * (X) to go out</i>.</LI>
	 * </OL>
	 * 
	 * @param comp
	 *            the comp
	 * 
	 * @see #addComplement(Object, DiscourseFunction)
	 */
	@Override
	public void addComplement(Object comp) {
		addComplement(comp, DiscourseFunction.OBJECT);
	}

	/**
	 * Passes a complement, which is assigned the specified function.
	 * 
	 * @param comp
	 *            The complement.
	 * @param function
	 *            The discourse function (e.g.
	 *            {@link simplenlg.features.DiscourseFunction#INDIRECT_OBJECT})
	 * 
	 * @throws simplenlg.exception.SimplenlgException
	 *             if the specified function is not compatible with a complement
	 *             of a VP, specifically, if it is
	 *             {@link simplenlg.features.DiscourseFunction#FRONT_MODIFIER},
	 *             {@link simplenlg.features.DiscourseFunction#PREMODIFIER},
	 *             {@link simplenlg.features.DiscourseFunction#POSTMODIFIER},
	 *             {@link simplenlg.features.DiscourseFunction#SUBJECT} or
	 *             {@link simplenlg.features.DiscourseFunction#PREP_OBJECT}
	 * 
	 * @see #addComplement(Object)
	 */
	public void addComplement(Object comp, DiscourseFunction function) {

		if (function.equals(DiscourseFunction.SUBJECT) || 
				function.equals(DiscourseFunction.OBJECT) ||
				function.equals(DiscourseFunction.INDIRECT_OBJECT) ||
				function.equals(DiscourseFunction.GENITIVE_OBJECT)) {
			addConstituent(function.mapToPosition(), comp);
		}
		else {
			throw new SimplenlgException(
					"Complements of a verb phrase can only be assigned the functions"
							+ "OBJECT, GENITIVE_OBJECT, INDIRECT_OBJECT, SUBJECT");
		}
	}

	public void addIndirectObject(Object object) {
		addComplement(object, DiscourseFunction.INDIRECT_OBJECT);
	}

	/**
	 * Unlike {@link #setComplement(Object)}, which resets all complements this
	 * method will only replace the complements with the given function, if
	 * there are any.
	 * 
	 * @param comp
	 *            The new complement
	 * @param function
	 *            The function
	 */
	public void setComplement(Object comp, DiscourseFunction function) {

		if (function.equals(DiscourseFunction.SUBJECT) || 
				function.equals(DiscourseFunction.OBJECT) ||
				function.equals(DiscourseFunction.INDIRECT_OBJECT) ||
				function.equals(DiscourseFunction.GENITIVE_OBJECT)) {
			setConstituent(function.mapToPosition(), comp);
		}
		else {
			throw new SimplenlgException(
					"Complements of a verb phrase can only be assigned the functions"
							+ "OBJECT, GENITIVE_OBJECT, INDIRECT_OBJECT, SUBJECT");
		}

	}

	public boolean hasComplements(DiscourseFunction function) {
		if (function.equals(DiscourseFunction.SUBJECT) || 
				function.equals(DiscourseFunction.OBJECT) ||
				function.equals(DiscourseFunction.INDIRECT_OBJECT) ||
				function.equals(DiscourseFunction.GENITIVE_OBJECT)) {
			return hasConstituents(function.mapToPosition());
		}
		else {
			throw new SimplenlgException(
					"Complements of a verb phrase can only be assigned the functions"
							+ "OBJECT, GENITIVE_OBJECT, INDIRECT_OBJECT, SUBJECT");
		}
		
	}
	
	
	@Deprecated
	@Override
	public void setPremodifier(Object mod) {
		this.addPremodifier(mod);
	}

	@Deprecated
	@Override
	public void addPremodifier(Object mod) {
		this.addModifier(Position.FRONT, mod);
	}
	
	@Deprecated
	@Override
	public void setPostmodifier(Object mod) {
		this.addModifier(mod);
	}

	@Deprecated
	@Override
	public void addPostmodifier(Object mod) {
		this.addModifier(mod);
	}

	@Deprecated
	@Override
	public void addAttributivePostmodifier(Object mod) {
		this.addModifier(mod);
	}

	@Deprecated
	@Override
	public boolean hasPremodifiers() {
		// TODO Auto-generated method stub
		return super.hasPremodifiers();
	}

	@Deprecated
	@Override
	public boolean hasPostmodifiers() {
		// TODO Auto-generated method stub
		return super.hasPostmodifiers();
	}

	@Deprecated
	@Override
	public boolean hasAttributivePostmodifiers() {
		// TODO Auto-generated method stub
		return super.hasAttributivePostmodifiers();
	}

	@Deprecated
	@Override
	public List<Phrase> getPremodifiers() {
		// TODO Auto-generated method stub
		return super.getPremodifiers();
	}

	@Deprecated
	@Override
	public List<Phrase> getPostmodifiers() {
		// TODO Auto-generated method stub
		return super.getPostmodifiers();
	}

	@Deprecated
	@Override
	public List<Phrase> getAttributivePostmodifiers() {
		// TODO Auto-generated method stub
		return super.getAttributivePostmodifiers();
	}
	
	

	@Override
	public void addModifier(Object modifier) {
		addModifier(Position.DEFAULT, modifier);
	}

	public void addModifier(Position pos, Object mod) {
		addConstituent(pos, mod);
	}
	
	public List<Phrase> getModifiers(Position pos) {
		return this.getConstituents(pos);
	}
	
	public void setInterrogative(Object obj, DiscourseFunction func, boolean fronted) {
		if (obj instanceof InterrogativeType) {
			this.interrogType = (InterrogativeType) obj;
			this.interrogative = true;

			if (obj.equals(InterrogativeType.JA_NEIN)) {
				this.whArgument = null;
			} else {
				this.whArgument = func;
			}
		} else {
			this.interrogType = InterrogativeType.WH_ELEMENT;
			//this.whArgument = func;  probably wrong
			this.interrogative = true;

			// TODO: can't keep track of interrogative complements this way
			if (fronted) {
				if (func.isComplementFunction()) {
					this.addComplement(obj, func);
					this.wordOrder = this.wordOrder.getFrontedVariant(func.mapToPosition());
				} else {
					this.addModifier(Position.FRONT, obj);
				}
			} else {
				if (func.isComplementFunction()) {
					this.addComplement(obj, func);
				} else {
					this.addModifier(func.mapToPosition(), obj);
				}
			}
		
		}		
	}
	
	// does not work with WH_ELEMENTs
	public void deleteInterrogative() {
		this.interrogative = false;
		this.interrogType = null;
		this.whArgument = null;
	}
	
	public boolean isInterrogative() {
		return this.interrogative;
	}
	
	public InterrogativeType getInterrogativeType() {
		return this.interrogType;
	}

	public DiscourseFunction getWhArgument() {
		return this.whArgument;
	}

	
	protected void addConstituent(Position pos, Object con) {
		//Phrase element = makePhraseSpec(con);
		Phrase element = makeConstituent(con, pos.mapToFunction());
		if (this.constituentMap.containsKey(pos)) {
			this.constituentMap.get(pos).add(element);
		} else {
			List<Phrase> elemList = new ArrayList<Phrase>();
			elemList.add(element);
			this.constituentMap.put(pos, elemList);
		}
	}
	
	protected void setConstituent(Position pos, Object con) {
		//Phrase element = makePhraseSpec(con);
		Phrase element = makeConstituent(con, pos.mapToFunction());
		List<Phrase> elemList = new ArrayList<Phrase>();
		elemList.add(element);
		this.constituentMap.put(pos, elemList);
	}
	
	public List<Phrase> getConstituents(Position pos) {
		return constituentMap.containsKey(pos) ? constituentMap.get(pos) : null;
	}
	
	public boolean hasConstituents(Position pos) {
		return (this.constituentMap.containsKey(pos) &&
				!this.constituentMap.get(pos).isEmpty());
	}
	
	
	/**
	 * Sets the form of this <code>verbGroup</code>.
	 * 
	 * @param f
	 *            The {@link simplenlg.features.Form}
	 */
	public void setForm(Form f) {
		this.form = f;

		// can't have PAST or FUTURE with gerunds or infinitives
		if (this.form == Form.GERUND || this.form == Form.INFINITIVE
				|| this.form == Form.BARE_INFINITIVE) {
			this.tense = Tense.PRESENT;
		}
	}

	/**
	 * Gets the form.
	 * 
	 * @return The {@link simplenlg.features.Form} of this verb.
	 */
	public Form getForm() {
		return this.form;
	}

	/**
	 * Sets the </code>passive</code> feature of this <code>VPPhraseSpec</code>.
	 * If the verb is set to passive, then the following occurs at realisation
	 * stage:
	 * <OL>
	 * <LI>If the VP has indirect objects, these are suppressed. Thus, for
	 * example, the VP <i>give <strong>Mary<sub>IO</sub></strong> the
	 * book<sub>DO</sub></i> is realised as <i>is given to Mary</i>. In the
	 * context of a sentence, the indirect object is raised to subject position
	 * (e.g. <i>the book is given to Mary</i>)</LI>
	 * Otherwise, the object is suppressed. Thus, the VP <i>kick John</i>
	 * becomes <i>is kicked</i></LI>
	 * </OL>
	 * 
	 * @param pass
	 *            Whether the verb phrase is passive
	 */
	public void setPassive(boolean pass) {
		this.passive = pass;
	}

	/**
	 * Checks if is passive.
	 * 
	 * @return <code>true</code> if this <code>VPPhraseSpec</code> is passive.
	 */
	public boolean isPassive() {
		return this.passive;
	}

	/**
	 * Sets the </code>progressive</code> feature of this
	 * <code>VPPhraseSpec</code>.
	 * 
	 * @param prog
	 *            the prog
	 */
	public void setProgressive(boolean prog) {
		this.progressive = prog;
	}

	/**
	 * Checks if is progressive.
	 * 
	 * @return <code>true<code> if this <code>VPPhraseSpec</code> is
	 *         progressive.
	 */
	public boolean isProgressive() {
		return this.progressive;
	}

	/**
	 * Sets the </code>perfect</code> feature of this <code>VPPhraseSpec</code>.
	 * 
	 * @param perf
	 *            the perf
	 */
	public void setPerfect(boolean perf) {
		this.perfect = perf;
	}

	/**
	 * Checks if is perfect.
	 * 
	 * @return <code>true<code> if this <code>VPPhraseSpec</code> is passive.
	 */
	public boolean isPerfect() {
		return this.perfect;
	}

	/**
	 * Sets the </code>negated</code> feature of this <code>VPPhraseSpec</code>.
	 * This will result in automatic <i>do-</i>insertion at realisation stage if
	 * required. (e.g. <i>did not do</i>).
	 * 
	 * @param neg
	 *            the neg
	 */
	@Override
	public void setNegated(boolean neg) {
		this.negated = neg;
	}

	/**
	 * Checks if is negated.
	 * 
	 * @return <code>true<code> if this <code>VPPhraseSpec</code> is negated.
	 */
	@Override
	public boolean isNegated() {
		return this.negated;
	}

	/**
	 * Sets the tense of the verb.
	 * 
	 * @param t
	 *            A value of {@link simplenlg.features.Tense}
	 */
	public void setTense(Tense t) {
		// if (t.equals(Tense.FUTURE) && (modal == null))
		// modal = "will";
		this.tense = t;
	}

	/**
	 * Gets the tense.
	 * 
	 * @return the {@link simplenlg.features.Tense} of this phrase
	 */
	public Tense getTense() {
		return this.tense;
	}

	/**
	 * Gets the modal.
	 * 
	 * @return the modal auxiliary of this phrase if set, <code>null</code>
	 *         otherwise.
	 * 
	 * @see #setModal(Verb)
	 */
	public Verb getModal() {
		return this.modal;
	}

	/**
	 * Sets the modal auxiliary of the verb.
	 * 
	 * @param modal
	 *            The modal auxiliary.
	 */
	public void setModal(String modal) {
		this.clearModals();
		addModal(modal);
	}

	/**
	 * Sets the modal auxiliary of the verb.
	 * 
	 * @param modal
	 *            The modal auxiliary.
	 */
	public void setModal(Verb modal) {
		this.clearModals();
		addModal(modal);
	}
	
	/**
	 * Removes all modal auxiliaries.
	 */
	public void clearModals() {
		this.modals.clear();
	}
	
	public void addModal(String modal) {
		this.modals.add(Constants.getModal(modal));
	}
	
	public void addModal(Verb modal) {
		this.modals.add(modal);
	}

	public boolean isMainVerbPerfect() {
		return mainVerbPerfect;
	}

	/**
	 * Set the perfect feature for the main verb. If this <code>VPPhraseSpec</code> has any
	 * modal verbs, this causes the main verb (as opposed to the inflected verb, ie.
	 * the top-most modal) to be set to perfect. If no modals are given, setting
	 * this feature is identical to using <code>setPerfect</code>. 
	 * 
	 * @param mainVerbPerfect
	 */
	public void setMainVerbPerfect(boolean mainVerbPerfect) {
		this.mainVerbPerfect = mainVerbPerfect;
	}

	/**
	 * Set the person feature of this verb.
	 * 
	 * @param p
	 *            The {@link simplenlg.features.Person} value.
	 */
	public void setPerson(Person p) {
		this.person = p;
	}

	/**
	 * Gets the person.
	 * 
	 * @return the person
	 */
	public Person getPerson() {
		return this.person;
	}

	/**
	 * Set the number feature of this phrase.
	 * 
	 * @param n
	 *            The {@link simplenlg.features.NumberAgr} value.
	 */
	public void setNumber(NumberAgr n) {
		this.number = n;
	}

	/**
	 * Gets the number.
	 * 
	 * @return The {@link simplenlg.features.NumberAgr} feature of this phrase.
	 */
	public NumberAgr getNumber() {
		return this.number;
	}

	/**
	 * For use in case the head verb is phrasal. (e.g. "get up"). The method
	 * adds the particle to the main verb of this phrase.
	 * 
	 * @param particle
	 *            The particle
	 * 
	 * @throws <code>NullPointerException</code> if the head verb has not been
	 *         specified.
	 */
	public void setParticle(String particle) {
		this.head.setParticle(particle);
	}

	public boolean isHonorific() {
		return honorific;
	}

	public void setHonorific(boolean honorific) {
		this.honorific = honorific;
	}

	public boolean isSuppressVorfeld() {
		return suppressVorfeld;
	}


	public void setSuppressVorfeld(boolean suppressVorfeld) {
		this.suppressVorfeld = suppressVorfeld;
	}


	public void setImperativeForm(NumberAgr num, boolean honorific) {
		this.form = Form.IMPERATIVE;
		this.number = num;
		this.honorific = honorific;
	}
	
	
	public WordOrder getWordOrder() {
		return wordOrder;
	}

	public void setWordOrder(WordOrder wordOrder) {
		this.wordOrder = wordOrder;
	}

	public Position getPassiveComplementPosition() {
		return passiveComplementPosition;
	}


	public void setPassiveComplementPosition(Position passiveComplementPosition) {
		this.passiveComplementPosition = passiveComplementPosition;
	}


	public boolean isPassiveComplementRealisation() {
		return passiveComplementRealisation;
	}


	public void setPassiveComplementRealisation(boolean passiveComplementRealisation) {
		this.passiveComplementRealisation = passiveComplementRealisation;
	}


	/**
	 * Unlike the {@link #getComplements()} method, inherited from
	 * {@link simplenlg.realiser.HeadedPhraseSpec}, this method will return only
	 * complements with a given discourse function (e.g. Direct object(s) only).
	 * 
	 * @param func
	 *            The {@link simplenlg.features.DiscourseFunction}
	 * 
	 * @return A List if complements with this function, if any. An empty list
	 *         otherwise.
	 */
	public List<Phrase> getComplements(DiscourseFunction func) {
		Position pos = func.mapToPosition();
		return constituentMap.containsKey(pos) ? constituentMap.get(pos) : null;
	}

	/**
	 * This method returns those complements which would be raised to subject
	 * position within a sentence, if the sentence (and the verb phrase) is
	 * passive. This is worked out as follows:
	 * <OL>
	 * <LI>If there are direct objects, then they are returned.</LI>
	 * <LI>Indirect objects are returned otherwise. If the indirect object(s) is
	 * a prepositional phrase, its complement is returned.</LI>
	 * </OL>
	 * 
	 * @return the complements to raise to subject, a
	 *         <code>java.util.List&lt;Phrase&gt;</code>
	 * 
	 * @see #setPassive(boolean)
	 */
	public List<Phrase> getPassiveRaisingComplements() {
		List<Phrase> objects = new ArrayList<Phrase>();
		
		if (constituentMap.containsKey(Position.OBJECT)) {
			objects = constituentMap.get(Position.OBJECT);
		}
		else {
			if (constituentMap.containsKey(Position.INDIRECT_OBJECT)) {
				for (Phrase p : constituentMap.get(Position.INDIRECT_OBJECT)) {
					if (p instanceof PPPhraseSpec && !p.isElided()) {
						objects.addAll(((PPPhraseSpec) p).getComplements());
					} else if (!p.isElided()) {
						objects.add(p);
					}
				}
			}
			if (objects.isEmpty()) {  // add expletive "es"
				NPPhraseSpec np = new NPPhraseSpec(Constants.PRO_EXPLETIVE);
				objects.add(np);
			}
		}
		
		return flagRaisedNPs(objects, true);
	}

	/**
	 * Compare two <code>VPPhraseSpecs/code>s on the basis of grammatical
	 * simplenlg.features.
	 * 
	 * @param vp
	 *            The <code>VPPhraseSpec</code> that will be compared to this
	 *            one.
	 * 
	 * @return <code>true</code> if the two <code>VPPhraseSpec</code>s have
	 *         identical values on all simplenlg.features (tense, progressive,
	 *         passive, perfect, negated, person and number).
	 */
	public boolean hasSameFeatures(VPPhraseSpec vp) {
		return this.passive == vp.passive && this.perfect == vp.perfect
				&& this.negated == vp.negated
				&& this.progressive == vp.progressive
				&& this.person == vp.person && this.number == vp.number;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simplenlg.realiser.Phrase#coordinate(T[])
	 */
	public VPPhraseSpec coordinate(Phrase... coords) {

		if (coords.length == 0) {
			return this;
		}

		CoordinateVPPhraseSpec coord = new CoordinateVPPhraseSpec(this);

		try {

			for (Phrase p : coords) {
				coord.addCoordinates((VPPhraseSpec) p);
			}

			return coord;

		} catch (ClassCastException cce) {
			throw new SimplenlgException("Cannot coordinate: "
					+ "only phrases of the same type can be coordinated");
		}
	}

	// ***************************************
	// Realisation
	// ***************************************

	/**
	 * Determines whether only the main verb of this phrase is to be realised,
	 * or whether the full verb phrase, including auxiliary should be realised.
	 * This feature is useful for aggregation purposes, in case the
	 * <code>VPPhraseSpec</code> is a
	 * {@link simplenlg.realiser.CoordinateVPPhraseSpec}.
	 * 
	 * @param aux
	 *            If <code>false</code>, causes only the main verb to be
	 *            returned at realisation stage.
	 * 
	 * @see simplenlg.realiser.CoordinateVPPhraseSpec#aggregateAuxiliary(boolean)
	 */
	public void realiseAuxiliary(boolean aux) {
		this.realiseAuxiliary = aux;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simplenlg.realiser.HeadedPhraseSpec#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		boolean eq = false;

		if (o instanceof VPPhraseSpec) {
			VPPhraseSpec vp = (VPPhraseSpec) o;

			if (super.equals(vp)) {
				eq = this.tense == vp.tense && this.perfect == vp.perfect
						&& this.passive == vp.passive
						&& this.progressive == vp.progressive;
			}
		}

		return eq;
	}

	/*
	 * OLD TEXT:
	 * Overload of the realise(Realiser) method, permitting realisation for
	 * interrogative sentences. We don't want to set a field inside the VP, as
	 * it's the containing sentence, not the VP, which is interrogative.
	 */
	public String checkIfCommaIsNeeded(String text) {
		String returnText = text;
		if (text.contains(",")) {
			if (text.lastIndexOf(",") == text.indexOf(",") && !text.equals(",") && text.lastIndexOf(",")!= text.length()-1) {
				returnText = text + ",";
			}
		}
		return returnText;
	}

	String realise(Realiser r, boolean subordinate) {
		
		// experimental
		HashMap<Position,List<Phrase>> constituentMapBackup = (HashMap<Position, List<Phrase>>) this.constituentMap.clone();
		
		computeSurfaceArgs();
		computeAgreement();
		realiseVerbGroup(r, subordinate);
		computeComplementRealisation(r);
		//computeComplementCase(); //now called immediately before each complement realization
		realiseConstituents(r);
		
		// experimental
		this.constituentMap = constituentMapBackup;

		String vorfeldText = "";
		if (!this.isSuppressVorfeld() && !this.getForm().equals(Form.IMPERATIVE)) {
			vorfeldText = this.constituentRealisation.remove(0);
			vorfeldText = (checkIfCommaIsNeeded(vorfeldText));
			if (vorfeldText.equals(",")) { // vorfeld has sentential constituent
				vorfeldText = this.constituentRealisation.remove(0);  // the sentence
				vorfeldText += this.constituentRealisation.remove(0); // the trailing comma
			}
		}
		String mittelfeldText = r.realiseList(this.constituentRealisation);
		
		// TODO: temporary hack
		if (this.negated) {
			mittelfeldText = r.appendSpace(mittelfeldText, "nicht");
		}
		mittelfeldText = checkIfCommaIsNeeded(mittelfeldText);
				
		if (subordinate) {
			if (!this.modals.isEmpty() && this.perfect) {
				return r.appendSpace(vorfeldText, mittelfeldText, this.finiteVerbRealisation, this.verbClusterRealisation);
			} else {
				return r.appendSpace(vorfeldText, mittelfeldText, this.verbClusterRealisation, this.finiteVerbRealisation);
			}
		} else {
			return r.appendSpace(vorfeldText, this.finiteVerbRealisation, mittelfeldText, this.verbClusterRealisation);
		}
		
/*		if (this.realiseAuxiliary) {
			return r.appendSpace(this.auxiliaryRealisation, preMod, 
					this.mainVerbRealisation, comp, postMod);
		} else {
			return this.head.isCopular() ? r.appendSpace(
					this.mainVerbRealisation, preMod, comp, postMod) : r
					.appendSpace(preMod, this.mainVerbRealisation, comp,
							postMod);
		}*/
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * simplenlg.realiser.HeadedPhraseSpec#realise(simplenlg.realiser.Realiser)
	 */
	@Override
	String realise(Realiser r) {
		return realise(r, false);
	}

	/*
	 * Returns the element of the verb group that would be fronted in case this
	 * was being realised in the context of a question.
	 */
	@Deprecated
	String getInterrogativeFrontedElement(LexiconInterface lex) {

		// TODO: doesn't work anymore
		// case 1: VP has no AUX && it;'s not copular
		if (this.vgComponents.size() == 1 && !this.head.isCopular()) {
			return lex.getVerbForm("do", this.tense, this.person, this.number);
		} else if (!this.vgComponents.empty()) {
			return this.vgComponents.peek();
		} else {
			return null;
		}
	}
	
	protected void computeSurfaceArgs() {
		// ensure there is a subject
		if (!this.hasComplements(DiscourseFunction.SUBJECT)) {
			this.addComplement("", DiscourseFunction.SUBJECT);
		}
		
		// initialise the surface subject to empty
		this.surfaceSubject.clearCoordinates();

		// set passive complement to null
		this.passiveComplement = null;

		// passive case first
		if (isPassive()) {

			// case 1: this is interrogative and is WH
			if (this.interrogative && this.interrogType.isWhQuestion()) {
				switch (this.whArgument) {

				// no surface subject
				case SUBJECT:
					// subject in active = passive complement in passive
					buildPassiveInterrogativeComplement();
					this.surfaceSubject.addCoordinates(this
							.getPassiveRaisingComplements());
					break;

				// suppress indirect object
				case INDIRECT_OBJECT:
					this.suppressArg(DiscourseFunction.INDIRECT_OBJECT);
					this.surfaceSubject.addCoordinates(this
							.getPassiveRaisingComplements());
					buildPassiveComplement();
					break;

				// no surface subject
				case OBJECT:
				default:
					buildPassiveComplement();
					// object in active = subject in passive
					this.whArgument = DiscourseFunction.SUBJECT;
					break;
				}
			} else {
				this.surfaceSubject.addCoordinates(this
						.getPassiveRaisingComplements());
				buildPassiveComplement();
			}

			// non-passive case
		} else if (this.interrogative && this.interrogType.isWhQuestion()) {
			switch (this.whArgument) {

			// empty surface subject
			case SUBJECT:
				break;

			// suppress arg otherwise
			case OBJECT:
			case INDIRECT_OBJECT:
			default:
				this.suppressArg(this.whArgument);
				this.surfaceSubject.addCoordinates(this.getComplements(DiscourseFunction.SUBJECT));
				break;
			}
		} else {
			this.surfaceSubject.addCoordinates(this.getComplements(DiscourseFunction.SUBJECT));
		}
		
		if ((this.passiveComplement != null) && this.passiveComplementRealisation) {
			this.addModifier(this.passiveComplementPosition, this.passiveComplement);
		}
		
		if (this.interrogative) {
			if (this.interrogType == InterrogativeType.JA_NEIN) {
				// Vorfeld is always empty for yes/no questions
				this.setSuppressVorfeld(true);
			} else if (!this.interrogType.equals(InterrogativeType.WH_ELEMENT)) {
				NPPhraseSpec inp = (NPPhraseSpec) makeConstituent(new NPPhraseSpec(Constants.getInterrogative(this.interrogType)), this.whArgument);
				// TODO: position should not be rigid
				inp.setCase(this.whArgument.getCaseValue());
				this.addConstituent(Position.FRONT, inp);
			}
		}

	}	
	
	
	/*
	 * Does this even do anything anymore?
	 * 
	 * compute two components of realisation, auxiliary and main verb note that
	 * main verb realisation includes trailing "not". NB: Should be called after
	 * realiseVerbGroup(Realiser)
	 * 
	 * Parameters include a boolean, which if true causes the fronted aux to be
	 * omitted in questions).
	 */
	@Deprecated
	void computeRealisation(Realiser r, boolean interrogative) {
		this.mainVerbRealisation = ""; // main verb, plus ending "not" if
		// necessary
		this.auxiliaryRealisation = ""; // auxiliaries (including endModifiers)
		boolean mainVerbSeen = false; // have we seen the main verb
		String omit = null;

		// if it's interrogative, we don't realise the fronted part
		if (interrogative) {
			omit = getInterrogativeFrontedElement(r.getLexicon());
		}

		// TODO: doesn't work anymore (is this even needed?)
		for (String word : this.vgComponents) {
			// flag this word if we need to skip it
			boolean skip = omit != null && word.equals(omit);

			if (!mainVerbSeen) {

				if (!skip) {
					this.mainVerbRealisation = r.appendSpace(word,
							this.mainVerbRealisation);
				}

				if (!word.equalsIgnoreCase("not")) {
					mainVerbSeen = true;
				}

			} else if (!skip) {
				this.auxiliaryRealisation = r.appendSpace(word,
						this.auxiliaryRealisation);
			}
		}
	}

	/*
	 * build verb group from head verb out, populating the stack vgComponents
	 * element 0 being last word frontVG is Verb currently at front of VG,
	 * restVG is rest of VG
	 */
	void realiseVerbGroup(Realiser r, boolean subordinate) {
		this.vgComponents.clear(); // clear the components stack
		this.finiteVerbRealisation = "";
		this.verbClusterRealisation = "";

		if (this.getDiscourseFunction() != DiscourseFunction.NULL &&
				this.form == Form.NORMAL) {
			switch (this.getDiscourseFunction()) {
			case SUBJECT:
				this.form = Form.ZU_INFINITIVE;
				break;
			case OBJECT:
			default:
				this.form = Form.BARE_INFINITIVE;
				break;
			}
		}
		
		// VP will be realized as perfective if the verb is modal and past
		//boolean modalPast = false;

		// compute modal -- this affects tense
		/*String actualModal = null;

		if (this.form == Form.INFINITIVE) {
			actualModal = "to";
		} else if (this.form.allowsTense()) {
			if (this.tense == Tense.FUTURE && this.modal == null) {
				actualModal = "will";
			} else if (this.modal != null) {
				actualModal = this.modal;

				if (this.tense.equals(Tense.PAST)) {
					modalPast = true;
				}
			}
		}*/

		// start off with main verb
		Verb finiteVerb = this.head;

		if (this.passive) {
			this.verbClusterRealisation = r.appendSpace(finiteVerb.getPerfectParticiple(), this.verbClusterRealisation);
			finiteVerb = Constants.getAuxiliary("werden");
		}
		
		if (this.modals.isEmpty()) {
			if (this.perfect || this.mainVerbPerfect) {
				// if verbClusterRealisation is not empty at this point for non-passive sentences, this might go wrong
				String participle = this.passive ? "worden" : finiteVerb.getPerfectParticiple();
				this.verbClusterRealisation = r.appendSpace(this.verbClusterRealisation, participle);
				
				finiteVerb = Constants.getAuxiliary(finiteVerb.getAuxType());
			}
		}
		else {
			if (this.mainVerbPerfect) {
				String participle = this.passive ? "worden" : finiteVerb.getPerfectParticiple();
				this.verbClusterRealisation = r.appendSpace(this.verbClusterRealisation, participle);
				
				finiteVerb = Constants.getAuxiliary(finiteVerb.getAuxType());
			}
			
			if (this.form != Form.IMPERATIVE) {
				for (Verb m : this.modals) {
					this.verbClusterRealisation = r.appendSpace(this.verbClusterRealisation, finiteVerb.getInfinitive());
					finiteVerb = m;
				}
				
				if (this.perfect) {
					this.verbClusterRealisation = r.appendSpace(this.verbClusterRealisation, finiteVerb.getInfinitive());
					finiteVerb = Constants.getAuxiliary(finiteVerb.getAuxType());
				}
			}
		}
		
		if (this.tense == Tense.FUTURE && this.form != Form.IMPERATIVE) {
			this.verbClusterRealisation = r.appendSpace(this.verbClusterRealisation, finiteVerb.getInfinitive());
			finiteVerb = Constants.getAuxiliary("werden");
		}
		


/*		if (this.perfect) {
			if (finiteVerb.getVerbType() == VerbType.MODAL &&
					this.hasEmbeddedVP()) {
				this.vgComponents.push(finiteVerb.getInfinitive());
			} else if (this.passive) {
				this.verbClusterRealisation = r.appendSpace(this.verbClusterRealisation, "worden");
			} else if (this.modal != null && this.tense != Tense.FUTURE) {
				this.vgComponents.push(finiteVerb.getInfinitive());
			} else {
				this.vgComponents.push(finiteVerb.getPerfectParticiple());
			}
			
			finiteVerb = Constants.getAuxiliary(finiteVerb.getAuxType());
		}

		if (this.tense == Tense.FUTURE) {
			this.vgComponents.add(0, finiteVerb.getInfinitive());
			finiteVerb = Constants.getAuxiliary("werden");
		}
		
		if (this.modal != null) {
			if (this.perfect || this.tense == Tense.FUTURE) {
				this.vgComponents.add(0, this.modal.getInfinitive());
			}
			else {
				this.vgComponents.add(0, finiteVerb.getInfinitive());
				finiteVerb = this.modal;
			}
		}
*/		
		
		
		// negated
		/*if (this.negated) {
			if (!this.vgComponents.empty() || frontVG != null
					&& frontVG.isCopular()) {
				this.vgComponents.push("not");
			} else {
				if (frontVG != null) {
					this.vgComponents.push(frontVG.getBaseForm());
				}

				this.vgComponents.push("not");
				frontVG = (Verb) lex.getItem(Category.VERB, "do");
			}
		}*/

		// now inflect frontVG (if it exists) and push it on restVG
		if (finiteVerb != null) {
/*			if (this.form == Form.GERUND) {
				this.vgComponents.push(frontVG.getPresentParticiple());
			} else if (this.form == Form.PAST_PARTICIPLE) {
				this.vgComponents.push(frontVG.getPastParticiple());
			} else if (this.form == Form.PRESENT_PARTICIPLE) {
				this.vgComponents.push(frontVG.getPresentParticiple());
			} else if (!this.form.allowsTense() || interrogative
					&& !this.head.isCopular() && this.vgComponents.isEmpty()) {
				this.vgComponents.push(frontVG.getBaseForm());
			} else {
				NumberAgr numToUse = determineNumber();
				// mmb: temporary hack
				// this.vgComponents.push(lex.getVerbForm(frontVG.getBaseForm(),
				//		this.tense, this.person, numToUse));
				this.vgComponents.push(frontVG.getForm(this.person, numToUse, this.tense, Form.NORMAL));
			}*/
			
			if (this.form == Form.BARE_INFINITIVE || this.form == Form.INFINITIVE) {
				this.verbClusterRealisation = r.appendSpace(this.verbClusterRealisation, finiteVerb.getInfinitive());
			} else if (this.form == Form.ZU_INFINITIVE) {
				this.verbClusterRealisation = r.appendSpace(this.verbClusterRealisation, finiteVerb.getZuInfinitive());
			} else if (this.form == Form.IMPERATIVE) {
				if (this.honorific) {
					this.finiteVerbRealisation = finiteVerb.getHonorificImperativeForm();
				} else {
					this.finiteVerbRealisation = finiteVerb.getImperativeForm(this.number);
				}
				
				if (finiteVerb.hasVerbPrefix()) {
					this.verbClusterRealisation = r.appendSpace(finiteVerb.getVerbPrefix(), this.verbClusterRealisation);
				}
			} else {
				NumberAgr numToUse = this.getNumber();
				Tense tenseToUse = this.tense == Tense.FUTURE ? Tense.PRESENT : this.tense;
				this.finiteVerbRealisation = finiteVerb.getForm(this.person, numToUse, tenseToUse, this.form);

				if (finiteVerb.hasVerbPrefix()) {
					if (subordinate) {
						this.finiteVerbRealisation = finiteVerb.getVerbPrefix().concat(this.finiteVerbRealisation);
					} else {
						this.verbClusterRealisation = r.appendSpace(this.verbClusterRealisation, finiteVerb.getVerbPrefix());
					}
				}

			}
		}
		
/*		if (this.negated) {
			this.verbClusterRealisation = r.appendSpace("nicht", this.verbClusterRealisation);
		}
*/
	}
	
	
	private boolean hasPluralComplement() {
		boolean plur = false;
		for (Phrase comp : this.complements) {
			if (comp instanceof NPPhraseSpec
					&& ((NPPhraseSpec) comp).getNumber() == NumberAgr.PLURAL) {
				plur = true;
				break;
			}
		}

		return plur;
	}

	/*
	 * this method currently should not be called;
	 * computeComplementRealisation and realiseConstituents are used instead
	 */
	@Override
	String realiseComplement(Realiser r) {
		return "";
	}

	/*
	 * Realises all constituents in the constituent map.
	 */
	private void realiseConstituents(Realiser r) {
		this.constituentRealisation.clear();
		
		// TODO: sentence constituents sometimes require commas to be inserted 
		for (Position pos : this.wordOrder.getPositions()) {
			if (pos.equals(Position.SUBJECT)) {
				if (!suppressSubject) {
					computeComplementCase();
					this.constituentRealisation.add(this.surfaceSubject.realise(r));
				}
			}
			else if (this.constituentMap.containsKey(pos)) {
				List<Phrase> phrases = this.constituentMap.get(pos);
				switch (pos) {
				case OBJECT:
					if (!this.suppressObject) {
						computeComplementCase();
						this.constituentRealisation.add(r.realiseAndList(phrases));
					}
					break;
				case INDIRECT_OBJECT:
					if (!this.suppressIndirectObject) {
						computeComplementCase();
						for (Phrase p : collectIndirectObjectsByCategory(phrases))
							this.constituentRealisation.add(r.realise(p));
					}
					break;
				default:
					for (Phrase p : phrases) {
						if (p instanceof SPhraseSpec) {
							// surround sentential constituents with commas
							this.constituentRealisation.add(",");
							this.constituentRealisation.add(r.realise(p));
							this.constituentRealisation.add(",");
						} else {
							this.constituentRealisation.add(r.realise(p));
						}						
					}
				}
			}
		}
				
		if (this.constituentRealisation.isEmpty()) {
			this.constituentRealisation.add("");
		}
	}
	
	private void computeAgreement() {
		if (this.getForm() == Form.IMPERATIVE) {
			return;
		}
		
		List<Phrase> agreeNP = this.surfaceSubject.getCoordinates();
		setNumber(getNumberFeature(agreeNP));
		setPerson(getPersonFeature(agreeNP));
	}
	
	private void computeComplementRealisation(Realiser r) {
		if (this.passive) {
			List<Phrase> passiveRaisingComplements = getPassiveRaisingComplements();
			for (Object key : this.constituentMap.keySet()) {
				this.constituentMap.get(key).removeAll(passiveRaisingComplements);
			}
		}
		
		for (Position pos : Position.values()) {
			if (this.constituentMap.containsKey(pos)) {
				if (pos.isComplementPosition()) {
					Collections.sort(this.constituentMap.get(pos), this.complementComparator);
				}
				// case used to be set here
				// this.constituentMap.put(pos, makeConstituentList(this.constituentMap.get(pos), pos));
			}
		}
	}
	
	private void computeComplementCase() {
		for (Position pos : Position.values()) {
			if (pos.isComplementPosition() && this.constituentMap.containsKey(pos)) {
				DiscourseFunction df = pos.mapToFunction();
				Case cs = df.getCaseValue();
				// assign case & discourse function values
				for (Phrase p : this.constituentMap.get(pos)) {
					if (p != null) {
						if (!df.equals(DiscourseFunction.NULL)) {
							p.setDiscourseFunction(df);
						}
						if (p instanceof NPPhraseSpec) {
							((NPPhraseSpec) p).setCase(cs);
						}
					}
				}
			}
		}

		if (this.surfaceSubject.hasCoordinates()) {
			Case cs = this.getDiscourseFunction().equals(DiscourseFunction.OBJECT) ?
					Case.ACCUSATIVE : Case.NOMINATIVE;
			for (Phrase p : this.surfaceSubject.coordinates) {
				if (p instanceof NPPhraseSpec) {
					((NPPhraseSpec) p).setCase(cs);
				}
			}
		}
	}
	
	/*
	 * Collect indirect objects so that: - NPs are coordinated - PPs are only
	 * coordinated if they have the same head
	 */
	private List<Phrase> collectIndirectObjectsByCategory(
			List<Phrase> indirectObjects) {
		ListIterator<Phrase> iterator = indirectObjects.listIterator();
		List<Phrase> result = new ArrayList<Phrase>();
		NPPhraseSpec lastNP = null;

		while (iterator.hasNext()) {
			Phrase next = iterator.next();

			if (next instanceof NPPhraseSpec) {
				NPPhraseSpec np = (NPPhraseSpec) next;

				if (lastNP != null) {
					result.remove(lastNP);

					if (lastNP instanceof CoordinateNPPhraseSpec) {
						((CoordinateNPPhraseSpec) lastNP).addCoordinates(np);
						np = lastNP;

					} else {
						np = lastNP.coordinate(np);
					}
				}

				lastNP = np;
				result.add(np);

			} else {
				result.add(next);
			}

		}

		return result;
	}

	/*
	 * String realisePostmodifier(Realiser r) { List<Phrase> postMods = new
	 * ArrayList<Phrase>();
	 * 
	 * for( Phrase p: postmodifiers ) if( !(p instanceof AdvPhraseSpec))
	 * postMods.add(p);
	 * 
	 * if (postmodifierComparator != null) Collections.sort(postMods,
	 * postmodifierComparator);
	 * 
	 * return r.realiseList(postmodifiers); }
	 */

	/*
	 * Realise adverb phrases separately: by default, these are placed before
	 * the main verb, not in the default postmodifier position.
	 */
	/*
	 * String realiseAdverbials(Realiser r) { List<Phrase> adverbials = new
	 * ArrayList<Phrase>();
	 * 
	 * for( Phrase p: postmodifiers) if( p instanceof AdvPhraseSpec )
	 * adverbials.add(p);
	 * 
	 * return r.realiseAndList(adverbials); }
	 */

	// *****************************************************************************
	// PRIVATE/PROTECTED UTILITY METHODS
	// *****************************************************************************
	/*
	 * suppress some argument during realisation
	 */
	void suppressArg(DiscourseFunction function) {

		if (function.equals(DiscourseFunction.OBJECT)) {
			this.suppressObject = true;
		} else if (function.equals(DiscourseFunction.INDIRECT_OBJECT)) {
			this.suppressIndirectObject = true;
		} else if (function.equals(DiscourseFunction.SUBJECT)) {
			this.suppressSubject = true;
		}
	}

	/*
	 * reset argument suppression && complement raising
	 */
	void initialiseArgs() {
		this.suppressIndirectObject = false;
		this.suppressObject = false;
		this.suppressSubject = false;
		flagRaisedNPs(this.complements, false);
	}

	/*
	 * get the Person feature of this sentence (depending on the subjects)
	 */
	protected Person getPersonFeature(List<Phrase> agreeNP) {

		if (agreeNP.size() == 1) {
			Phrase agr = agreeNP.get(0);

			if (agr instanceof NPPhraseSpec) {
				return ((NPPhraseSpec) agr).getPerson();
			}
		}

		return Person.THIRD;
	}

	/*
	 * get the Number feature of this sentence
	 */
	protected NumberAgr getNumberFeature(List<Phrase> agreeNP) {

		if (agreeNP.size() == 1) {
			Phrase agr = agreeNP.get(0);

			if (agr instanceof NPPhraseSpec) {
				return ((NPPhraseSpec) agr).getNumber();
			} else if (agr instanceof StringPhraseSpec
					&& ((StringPhraseSpec) agr).isPlural()) {
				return NumberAgr.PLURAL;
			} else if (agr.isCoordinate()) {
				return NumberAgr.PLURAL;
			}
		}

		else if (agreeNP.size() > 1) {
			return NumberAgr.PLURAL;
		}

		return NumberAgr.SINGULAR;
	}
	
	void buildPassiveComplement() {
		List<Phrase> actualSubjects = getNonElidedSubjects();

		if (!actualSubjects.isEmpty()) {
			this.passiveComplement = new PPPhraseSpec(Constants.PASSIVE_VON);

			for (Phrase p : actualSubjects) {
				p.setDiscourseFunction(DiscourseFunction.PREP_OBJECT);
				this.passiveComplement.addComplement(p);
			}
		}
	}

	void buildPassiveInterrogativeComplement() {
		PPPhraseSpec pic = new PPPhraseSpec(Constants.PASSIVE_VON);
		NPPhraseSpec inp = new NPPhraseSpec(Constants.getInterrogative(this.interrogType));
		pic.setComplement(inp);
		
		this.interrogType = InterrogativeType.WH_ELEMENT;
		// TODO: should not be rigid
		//this.addModifier(this.getPassiveComplementPosition(), pic);
		this.addModifier(Position.FRONT, pic);
	}

	List<Phrase> getNonElidedSubjects() {
		List<Phrase> subjects = new ArrayList<Phrase>();

		for (Phrase p : this.getComplements(DiscourseFunction.SUBJECT)) {
			if (!p.isElided()) {
				subjects.add(p);
			}
		}

		return subjects;
	}

	/*
	 * check if a verb is a form of "be"
	 */
	boolean isBeVerb(String verb) {
		boolean beVerb = false;

		if (verb != null) {
			// returns T if this verb is a form of "be"
			beVerb = verb.equalsIgnoreCase("be") || verb.equalsIgnoreCase("am")
					|| verb.equalsIgnoreCase("are")
					|| verb.equalsIgnoreCase("is")
					|| verb.equalsIgnoreCase("was")
					|| verb.equalsIgnoreCase("were");
		}

		return beVerb;
	}

	/*
	 * create a verb from a string
	 */
	Verb castToVerb(String verb) {
		Verb v = isBeVerb(verb) ? new Verb("be") : new Verb(verb);
		return v;
	}

	/*
	 * flag NPs which are passive-raised
	 */
	List<Phrase> flagRaisedNPs(List<Phrase> flagged, boolean raise) {

		for (Phrase p : flagged) {
			if (p instanceof NPPhraseSpec) {
				((NPPhraseSpec) p).setRaised(raise);
			}
		}

		return flagged;
	}

	boolean hasEmbeddedVP() {
		for (Phrase p : this.complements) {
			if (p instanceof VPPhraseSpec) {
				return true;
			}
		}
		return false;
	}
	
}
