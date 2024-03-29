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

package simplenlg.lexicon.morph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import simplenlg.lexicon.lexicalitems.ContentWord;
import simplenlg.lexicon.lexicalitems.Verb;

/**
 * TODO:
 * - Modify apply(T lex) for stem umlaut
 * 
 * Implementation of <code>MorphologicalRule<T extends LexicalItem</code> for
 * inflection. An inflection rule consists of:
 * <OL>
 * <LI>An <strong>exception rule list</strong>: An ordered list of zero or more
 * production-like pattern-action statements, instances of the
 * {@link simplenlg.lexicon.morph.PatternActionRule} class.</LI>
 * <LI>A <strong>default rule</strong>: This is a single
 * <code>PatternActionRule</code> that encapsulates the default operation of the
 * rule.
 * </OL>
 * 
 * <P>
 * The way inflection rules work is as follows:
 * 
 * <OL>
 * <LI>check the {@link simplenlg.features.InflectionType} of a lexical item. If
 * the value is {@link simplenlg.features.InflectionType#REGULAR} or
 * {@link simplenlg.features.InflectionType#REG_DOUBLING}, then apply the
 * default rule, possibly with consonant doubling. Otherwise, if the type is
 * {@link simplenlg.features.InflectionType#PERIPHRASTIC}, simply return the
 * baseform, as it is not inflected.</LI>
 * <LI>For all other cases, test the <code>PatternActionRule</code>s in order of
 * priority. If a rule has a precondition which matches the baseform of the
 * lexical item, fire that rule and return the result.</LI>
 * </OL>
 * 
 * 
 * <P>
 * <strong>NB:</strong>: Until Version 3.6 of SimpleNLG, this was an abstract
 * class extended by concrete classes <code>NominalInflectionRule</code>,
 * <code>VerbInflectionRule</code> and <code>AdjectiveInflectionRule</code>.
 * These latter classes have been removed in favour of simplifying the api, with
 * all inflection rules now instantiating a single parametrised type.
 * 
 * @author agatt
 * @since Version 3.7
 */
public class InflectionRule<T extends ContentWord> implements
		MorphologicalRule<T> {

	// exception rules
	List<PatternActionRule> patternActionRules;

	// default rule
	PatternActionRule defaultRule;

	// rule name
	String name;

	// regex matcher for words (non-symbols)
	Matcher wordMatcher = Pattern.compile(BasicPatterns.ANY_STEM).matcher(
			"blablabla");

	// flag indicating if this rule should apply stem umlaut
	boolean umlaut;

	/**
	 * Instantiates a new (empty) inflection rule.
	 */
	public InflectionRule() {
		this.patternActionRules = new ArrayList<PatternActionRule>();
		this.umlaut = false;
	}

	/**
	 * Instantiates a new inflection rule with the given name.
	 * 
	 * @param ruleName
	 *            the rule name
	 */
	public InflectionRule(String ruleName) {
		this();
		setName(ruleName);
	}

	/**
	 * Instantiates a new inflection rule with the given name and a boolean value
	 * indicating whether this rule should apply stem umlaut.
	 * 
	 * @param ruleName
	 *            the rule name
	 *        applyUmlaut
	 *            whether this rule should apply stem umlaut
	 */
	public InflectionRule(String ruleName, boolean applyUmlaut) {
		this();
		setName(ruleName);
		setApplyUmlaut(applyUmlaut);
	}

	/**
	 * Instantiates a new inflection rule with the given name, and a default
	 * <code>PatternActionRule</code>.
	 * 
	 * @param ruleName
	 *            the rule name
	 * @param defRule
	 *            the default rule
	 */
	public InflectionRule(String ruleName, PatternActionRule defRule) {
		this(ruleName);
		setDefaultRule(defRule);
	}

	/**
	 * Instantiates a new inflection rule with the given name, default rule, and
	 * a set of <code>PatternActionRule</code>s that make up the exception list.
	 * 
	 * @param ruleName
	 *            the rule name
	 * @param defRule
	 *            the default rule
	 * @param ruleSet
	 *            the set of exception rules
	 */
	public InflectionRule(String ruleName, PatternActionRule defRule,
			PatternActionRule... ruleSet) {
		this(ruleName, defRule);
		setExceptionRules(ruleSet);
	}

	/**
	 * Instantiates a new inflection rule with the given name, a boolean value
	 * indicating whether this rule should apply stem umlaut,
	 * and a default rule.
	 * 
	 * @param ruleName
	 *            the rule name
	 * @param applyUmlaut
	 *            whether this rule should apply stem umlaut
	 * @param defRule
	 *            the default rule
	 */
	public InflectionRule(String ruleName, boolean applyUmlaut,
			PatternActionRule defRule) {
		this(ruleName, defRule);
		setApplyUmlaut(applyUmlaut);
	}

	/**
	 * Instantiates a new inflection rule with the given name, a boolean value
	 * indicating whether this rule should apply stem umlaut,
	 * default rule, and a set of <code>PatternActionRule</code>s that make up
	 * the exception list.
	 * 
	 * @param ruleName
	 *            the rule name
	 * @param applyUmlaut
	 *            whether this rule should apply stem umlaut
	 * @param defRule
	 *            the default rule
	 * @param ruleSet
	 *            the set of exception rules
	 */
	public InflectionRule(String ruleName, boolean applyUmlaut,
			PatternActionRule defRule, PatternActionRule... ruleSet) {
		this(ruleName, defRule);
		setExceptionRules(ruleSet);
		setApplyUmlaut(applyUmlaut);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simplenlg.lexicon.morph.MorphologicalRule#getName()
	 */
	public String getName() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simplenlg.lexicon.morph.MorphologicalRule#setName(java.lang.String)
	 */
	public void setName(String newName) {
		this.name = newName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simplenlg.lexicon.morph.MorphologicalRule#hasName()
	 */
	public boolean hasName() {
		return this.name != null;
	}

	/**
	 * Set whether this rule should apply stem umlaut. (e.g. "Fu� -> F��e")
	 * 
	 * @param consUmlaut
	 *            whether this rule should apply stem umlaut
	 */
	public void setApplyUmlaut(boolean consUmlaut) {
		this.umlaut = consUmlaut;
	}

	/**
	 * Check whether an inflection rule applies stem umlaut.
	 * 
	 * @return <code>true</code> if stem umlaut has been set for this rule
	 * @see #setApplyUmlaut(boolean)
	 */
	public boolean appliesUmlaut() {
		return this.umlaut;
	}

	/**
	 * Adds an arbitrary number of exception-handling
	 * <code>PatternActionRule</code>.
	 * 
	 * @param rules
	 *            the new rules
	 */
	public void addExceptionRules(PatternActionRule... rules) {
		addExceptionRules(Arrays.asList(rules));
	}

	/**
	 * Adds the exception-handling <code>PatternActionRule</code> in the
	 * specified collection.
	 * 
	 * @param rules
	 *            the new rules
	 */
	public void addExceptionRules(Collection<PatternActionRule> rules) {
		this.patternActionRules.addAll(rules);
		Collections.sort(this.patternActionRules);
	}

	/**
	 * Set the exception-handling rules.
	 * 
	 * @param actionRules
	 *            the new rules
	 * 
	 */
	public void setExceptionRules(PatternActionRule... actionRules) {
		this.patternActionRules.clear();
		addExceptionRules(actionRules);
	}

	/**
	 * Set the exception-handling rules to those in the specified collection.
	 * 
	 * @param actionRules
	 *            the new rules
	 * 
	 */
	public void setExceptionRules(Collection<PatternActionRule> actionRules) {
		this.patternActionRules.clear();
		addExceptionRules(actionRules);
	}

	/**
	 * Gets the number of <code>PatternActionRule</code>s in the exception list.
	 * 
	 * @return the number of rules
	 */
	public int getNumberOfRules() {
		return this.patternActionRules.size();
	}

	/**
	 * Set the default rule to handle all cases not marked as exceptions.
	 * 
	 * @param defRule
	 *            The new default rule.
	 */
	public void setDefaultRule(PatternActionRule defRule) {
		this.defaultRule = defRule;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seesimplenlg.lexicon.morph.MorphologicalRule#apply(simplenlg.lexicon.
	 * lexicalitems.LexicalItem)
	 */
	public String apply(T lex) {
		// HAS NOT BEEN MODIFIED FOR UMLAUT
		
		String base = "";

		if (lex instanceof Verb) {
			base = ((Verb) lex).getBaseform(false);
		} else {
			base = lex.getBaseForm();
		}

		// if infl type is unspecified, we just treat it as a string
		if (!lex.hasInflectionType()) {
			return apply(base);
		}

		String result;

		// otherwise, we look at the inflection type
		switch (lex.getInflectionType()) {

		// periphrastics and invariants take no inflection
		case PERIPHRASTIC:
		case INVARIANT:
			result = base;
			break;

		// regulars need the default rule
		case REGULAR:
			result = this.defaultRule.fire(base);
			break;

		// regulars which take cons doubling
		case REG_DOUBLING:
			if (this.umlaut) {
				result = this.defaultRule.fire(doubleFinalConsonant(base));
			} else {
				result = this.defaultRule.fire(base);
			}
			break;

		// everything else
		default:
			result = apply(base);
			break;
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see simplenlg.lexicon.morph.MorphologicalRule#apply(java.lang.String)
	 */
	public String apply(String word) {
		this.wordMatcher.reset(word);

		if (!this.wordMatcher.matches()) {
			return word;
		}

		if (this.appliesUmlaut()) { word = applyUmlaut(word); }
		
		String result = null;
		Iterator<PatternActionRule> iter = this.patternActionRules.iterator();

		while (iter.hasNext()) {
			PatternActionRule currentRule = iter.next();

			if (currentRule.applies(word)) {
				result = currentRule.fire(word);
				break;
			}
		}

		if (result == null && this.defaultRule != null) {
			result = this.defaultRule.fire(word);
		}

		return result;
	}

	/*
	 * Util method to double the final consonant of a word
	 */
	private String doubleFinalConsonant(String word) {
		StringBuffer buffer = new StringBuffer(word);
		buffer.append(buffer.charAt(buffer.length() - 1));
		return buffer.toString();
	}

	private String applyUmlaut(String word) {
		Pattern p = Pattern.compile(BasicPatterns.LAST_VOWEL);
		Matcher m = p.matcher(word);
		StringBuilder s = new StringBuilder(word);
		
		if (m.find()) {
			s.replace(m.start(1), m.end(1), createUmlaut(m.group(1)));
		}
		
		return s.toString();
	}
	
	private String createUmlaut(String vowel) {
		String umlaut = vowel;
		if (vowel.equals("a")) { umlaut = "ä"; }
		else if (vowel.equals("A")) { umlaut = "Ä"; }
		else if (vowel.equals("o")) { umlaut = "ö"; }
		else if (vowel.equals("O")) { umlaut = "Ö"; }
		else if (vowel.equals("u")) { umlaut = "ü"; }
		else if (vowel.equals("U")) { umlaut = "Ü"; }
		else if (vowel.equals("au")) { umlaut = "äu"; }
		else if (vowel.equals("Au")) { umlaut = "Äu"; }
		else if (vowel.equals("aa")) { umlaut = "ä"; }
		else if (vowel.equals("Aa")) { umlaut = "Ä"; }
		
		return umlaut;
	}
}
