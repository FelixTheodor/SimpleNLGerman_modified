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
package simplenlg.framework;

import simplenlg.features.Case;
import simplenlg.features.Category;
import simplenlg.features.Gender;
import simplenlg.features.NumberAgr;
import simplenlg.features.Person;
import simplenlg.lexicon.Lexicon;
import simplenlg.lexicon.lexicalitems.Adjective;
import simplenlg.lexicon.lexicalitems.Adverb;
import simplenlg.lexicon.lexicalitems.Complementiser;
import simplenlg.lexicon.lexicalitems.Conjunction;
import simplenlg.lexicon.lexicalitems.Constants;
import simplenlg.lexicon.lexicalitems.Determiner;
import simplenlg.lexicon.lexicalitems.LexicalItem;
import simplenlg.lexicon.lexicalitems.Noun;
import simplenlg.lexicon.lexicalitems.Preposition;
import simplenlg.lexicon.lexicalitems.Pronoun;
import simplenlg.lexicon.lexicalitems.Verb;
import simplenlg.realiser.*;

public class NLGFactory {

	private Lexicon lexicon;

	/**
	 * Creates a new NLGFactory object.
	 */
	public NLGFactory() {
		this(null);
	}
	
	/**
	 * Creates a new NLGFactory object.
	 * 
	 * @param newLexicon The lexicon used for word look-up
	 */
	public NLGFactory(Lexicon newLexicon) {
		setLexicon(newLexicon);
	}
	
	/**
	 * Gets the lexicon used for word look-up.
	 */
	public Lexicon getLexicon() {
		return lexicon;
	}

	/**
	 * Sets the lexicon.
	 */
	public void setLexicon(Lexicon lexicon) {
		this.lexicon = lexicon;
	}
	
	// LEXICAL ITEMS
		
	/**
	 * Looks up a word in the lexicon and returns the appropriate
	 * lexical item.
	 * 
	 * Compound nouns and complex verbs may be given with their last 
	 * component separated by a vertical bar (e.g., <em>Heimat|stadt, 
	 * auf|geben</em>).  Complex verb clusters including spaces are
	 * also accepted (e.g., <em>Gassi gehen</em>).
	 * 
	 * @param word (The base form of) the word
	 * @param cat  The lexical category of the word
	 * 
	 * @return A lexical item of the type corresponding to the given
	 * category.  If the word is found in the lexicon, all lexical
	 * features will be set accordingly. 
	 */
	public LexicalItem createWord(String word, Category cat) {
		LexicalItem item = null;
		if (this.lexicon != null) {
			if (word.contains("|") || word.contains(" ")) {
				if (cat.equals(Category.NOUN)) {
					item = this.lexicon.getCompoundNoun(word);
				}
				else if (cat.equals(Category.VERB)) {
					item = this.lexicon.getComplexVerb(word);
				}
			}
			else if (cat.equals(Category.PRONOUN)) {
				item = Constants.getPronoun(word);
			}
			
			if (item == null) {
				item = this.lexicon.getItem(cat, word);
			}
		} 
		if (item == null) {
			switch (cat) {
			case ADJECTIVE:
				return new Adjective(word);
			case ADVERB:
				return new Adverb(word);
			case VERB:
				return new Verb(word);
			case NOUN:
				return new Noun(word);
			case PREPOSITION:
				return new Preposition(word);
			case PRONOUN:
				return new Pronoun(word);
			case DETERMINER:
				return new Determiner(word);
			case COMPLEMENTISER:
				return new Complementiser(word);
			case CONJUNCTION:
				return new Conjunction(word);
			}
		}
		return item;
	}
	
	/**
	 * Looks up a noun in the lexicon and returns a Noun object.
	 * 
	 * Compound nouns may be given with their last 
	 * component separated by a vertical bar (e.g., <em>Heimat|stadt</em>).
	 * 
	 * @param word (The base form of) the noun
	 * @param cat  The lexical category of the noun
	 * 
	 * @return A Noun object.  If the word is found in the lexicon, all 
	 * lexical features will be set accordingly. 
	 */
	public Noun createNoun(String word) {
		return (Noun) createWord(word, Category.NOUN);
	}
	
	/**
	 * Looks up a verb in the lexicon and returns a Verb object.
	 * 
	 * Complex verbs may be given with their last 
	 * component separated by a vertical bar (e.g., <em>auf|geben</em>).
	 * Complex verb clusters including spaces are
	 * also accepted (e.g., <em>Gassi gehen</em>).
	 * 
	 * @param word (The base form of) the verb
	 * @param cat  The lexical category of the verb
	 * 
	 * @return A Verb object.  If the word is found in the lexicon, all 
	 * lexical features will be set accordingly. 
	 */
	public Verb createVerb(String word) {
		return (Verb) createWord(word, Category.VERB);
	}

	// NOUN PHRASES

	/**
	 * Creates a noun phrase from a given string.
	 *
	 * <p>Input strings may consist of either only a noun, 
	 * including a compound noun, or a determiner/pronoun
	 * followed by a noun.</p>
	 * 
	 * <p>Examples for valid input strings:
	 * 
	 *   <ul>
	 *   <li><em>"Haus"</em></li>
	 *   <li><em>"das Haus"</em></li>
	 *   <li><em>"mein Haus"</em></li>
	 *   <li><em>"der Häuser|block"</em></li>
	 *   </ul>
	 * </p>
	 * 
	 * <p>Note that inflection is not considered when parsing
	 * the input string, so <em>"das Haus"</em> and <em>"dem Haus"</em>
	 * both return the same noun phrase, set to the default case
	 * (nominative). Ungrammatical strings such as <em>"die Haus"</em>
	 * are also accepted and result in the same object, as features such
	 * as gender are retrieved from the lexicon and not parsed from
	 * the input string.</p>
	 * 
	 * @param noun The input string
	 * @return A noun phrase representing the input string
	 */
	public NPPhraseSpec createNounPhrase(String noun) {
		if (noun.indexOf(" ") > -1) { // accept determiner+noun in a single string
			String spec = noun.substring(0, noun.indexOf(" "));
			String head = noun.substring(noun.indexOf(" ") + 1);
			
			if (Constants.getDeterminer(spec) != null
					|| Constants.getPronoun(spec) != null) {
				return createNounPhrase(spec, head);
			}
		}
		if (noun.indexOf("|") > -1) { // handle compound nouns
			Noun n = this.lexicon.getCompoundNoun(noun);
			return new NPPhraseSpec(n);
		}
		
		Pronoun pro = Constants.getPronoun(noun);
		if (pro != null) {
			return createPronounPhrase(pro);
		}
		Noun n = this.createNoun(noun);
		if (n != null) {
			return new NPPhraseSpec(n);
		} else {
			return new NPPhraseSpec(noun);
		}
	}
	
	/**
	 * Creates a noun phrase from a specifier and noun string.
	 * 
	 * @see {@link createNounPhrase(String)}
	 * @param spec The specifier (either as a String or a Word object)
	 * @param noun The noun (as a String)
	 */
	public NPPhraseSpec createNounPhrase(Object spec, String noun) {
		NPPhraseSpec np = createNounPhrase(noun);
		
		Object sp = null;
		if (spec instanceof String) {
			sp = Constants.getDeterminer((String) spec);
			if (sp == null) {
				sp = Constants.getPronoun((String) spec);
				if (sp != null && Constants.requiresPlural((Pronoun) sp)) {
					np.setPlural(true);
				}
			}
		}
		if (sp != null) {
			np.setSpecifier(sp);
		} else {
			np.setSpecifier(spec);
		}
		
		return np;
	}
	
	/**
	 * Creates a noun phrase from a specifier and noun.
	 * 
	 * @see {@link createNounPhrase(String)}
	 * @param spec The specifier (either as a String or a Word object)
	 * @param noun The noun (as a Noun object)
	 */
	public NPPhraseSpec createNounPhrase(Object spec, Noun noun) {
		NPPhraseSpec np = new NPPhraseSpec(noun);
		
		Object sp = null;
		if (spec instanceof String) {
			sp = Constants.getDeterminer((String) spec);
			if (sp == null) {
				sp = Constants.getPronoun((String) spec);
				if (sp != null && Constants.requiresPlural((Pronoun) sp)) {
					np.setPlural(true);
				}
			}
		}
		if (sp != null) {
			np.setSpecifier(sp);
		} else {
			np.setSpecifier(spec);
		}
		
		return np;
	}

	/**
	 * Creates a noun phrase that is a reference to another noun phrase.
	 * 
	 * @param ref The noun phrase to refer to
	 * @return The referential noun phrase
	 */
	public NPPhraseSpec createReferentialNounPhrase(NPPhraseSpec ref) {
		return new NPPhraseSpec(ref);
	}

	/**
	 * Creates a noun phrase that is a reference to another noun phrase.
	 * 
	 * @param ref The noun phrase to refer to
	 * @param pronoun The pronoun to be used to refer to the NP
	 * @return The referential noun phrase
	 */
	public NPPhraseSpec createReferentialNounPhrase(NPPhraseSpec ref, String pronoun) {
		Pronoun pro = Constants.getPronoun(pronoun);
		if (pro != null) {
			return new NPPhraseSpec(ref, pro);
		}
		else {
			return new NPPhraseSpec(ref, new Pronoun(pronoun));
		}
	}
	
	/**
	 * Creates a noun phrase from a given pronoun string.
	 * 
	 * @param pro The pronoun (as a String)
	 */
	public NPPhraseSpec createPronounPhrase(String pro) {
		Pronoun p = Constants.getPronoun(pro);
		if (p != null) {
			return createPronounPhrase(p);
		} else {
			return new NPPhraseSpec(pro);
		}
	}

	/**
	 * Creates a noun phrase from a given Pronoun object.
	 * 
	 * <p>Copies the {@link Person} value from the supplied pronoun.</p>
	 * 
	 * @param pro The pronoun
	 */
	public NPPhraseSpec createPronounPhrase(Pronoun pro) {
		NPPhraseSpec np = new NPPhraseSpec(pro);
		np.setPerson(pro.getPerson());
		if (Constants.requiresPlural(pro)) {
			np.setPlural(true);
		}
		return np;
	}

	/**
	 * Creates a noun phrase consisting of a possessive pronoun with 
	 * the given agreement features.
	 * 
	 * @param p The person
	 * @param n The number
	 */
	public NPPhraseSpec createPossessivePronounPhrase(Person p, NumberAgr n) {
		NPPhraseSpec np = new NPPhraseSpec(Constants.getPossessivePronoun(p, n));
		return np;
	}

	/**
	 * Creates a noun phrase consisting of a personal pronoun with 
	 * the given agreement features.
	 * 
	 * @param p The person
	 * @param n The number
	 */
	public NPPhraseSpec createPersonalPronounPhrase(Person p, NumberAgr n) {
		NPPhraseSpec np = new NPPhraseSpec(Constants.getPersonalPronoun(p));
		if (n == NumberAgr.PLURAL) np.setPlural(true);
		return np;
	}
	
	/**
	 * Creates a noun phrase consisting of a personal pronoun with 
	 * the given agreement features.
	 * 
	 * @param p The person
	 * @param n The number
	 * @param g The gender
	 */
	public NPPhraseSpec createPersonalPronounPhrase(Person p, NumberAgr n, Gender g) {
		NPPhraseSpec np = createPersonalPronounPhrase(p, n);
		np.setGender(g);
		return np;
	}

	// PREPOSITIONAL PHRASES
	
	/**
	 * Creates a prepositional phrase from a preposition and a noun phrase.
	 * 
	 * @param prep The preposition (either as a string or a Preposition object)
	 * @param np The noun phrase (either as a string or a NPPhraseSpec object)
	 */
	public PPPhraseSpec createPrepositionalPhrase(Object prep, Object np) {
		Preposition p;
		if (prep instanceof String) {
			p = Constants.getPreposition((String) prep);
		} else if (prep instanceof Preposition) {
			p = (Preposition) prep;
		} else return null;
		
		Phrase comp;
		if (np instanceof String) {
			comp = createNounPhrase((String) np);
		} else if (np instanceof Phrase) {
			comp = (Phrase) np;
		} else return null;
		
		return new PPPhraseSpec(p, comp);
	}
	
	/**
	 * Creates a prepositional phrase from a preposition and a noun phrase.
	 * Allows to set grammatical case of the noun phrase directly, 
	 * for prepositions which are ambiguous in this regard (e.g. <em>'auf'</em>,
	 * <em>'zu'</em>).
	 * 
	 * @param prep The preposition (as a string)
	 * @param np The noun phrase (either as a string or a NPPhraseSpec object)
	 * @param c The case
	 */
	public PPPhraseSpec createPrepositionalPhrase(String prep, Object np, Case c) {
		Preposition p = new Preposition(prep, c);
		return createPrepositionalPhrase(p, np);
	}

	/**
	 * Creates a prepositional phrase from a preposition and a noun phrase.
	 * Allows to specify that a postposition should be created instead (e.g.,
	 * <em>'der Kinder <strong>wegen</strong>'</em>).
	 * 
	 * @param prep The preposition (as a string)
	 * @param np The noun phrase (either as a string or a NPPhraseSpec object)
	 * @param postpos A boolean value, indicating whether or not a postposition
	 * should be created
	 */
	public PPPhraseSpec createPrepositionalPhrase(String prep, Object np, boolean postpos) {
		Preposition p = Constants.getPostposition((String) prep);
		return createPrepositionalPhrase(p, np);
	}

	/**
	 * Creates a prepositional phrase from a preposition and a noun phrase given
	 * as specifier + head noun.
	 * 
	 * @param prep The preposition
	 * @param spec The specifier of the noun phrase
	 * @param noun The head noun of the noun phrase
	 */
	public PPPhraseSpec createPrepositionalPhrase(Object prep, Object spec, String noun) {
		return createPrepositionalPhrase(prep, createNounPhrase(spec, noun));
	}
	
	/**
	 * Creates a prepositional phrase from a preposition and a noun phrase given
	 * as specifier + head noun.
	 * Allows to set grammatical case of the noun phrase directly, 
	 * for prepositions which are ambiguous in this regard (e.g. <em>'auf'</em>,
	 * <em>'zu'</em>).
	 * 
	 * @param prep The preposition
	 * @param spec The specifier of the noun phrase
	 * @param noun The head noun of the noun phrase
	 * @param c The case
	 */
	public PPPhraseSpec createPrepositionalPhrase(String prep, Object spec, String noun, Case c) {
		return createPrepositionalPhrase(prep, createNounPhrase(spec, noun), c);
	}
	
	// ADJECTIVE PHRASES
	
	/**
	 * Creates an adjective phrase from a given string. Strings may consist only of
	 * the base form of an adjective.
	 */
	public AdjPhraseSpec createAdjectivePhrase(String adj) {
		Adjective a = lexicon.getAdjective(adj);
		if (a == null) {
			return new AdjPhraseSpec(adj);
		} else {
			return new AdjPhraseSpec(a);
		}
	}
	
	// SENTENCES
	
	/**
	 * Creates a sentence object from a given verb. The expletive <em>'es'</em>
	 * is used as the subject.
	 * 
	 * @param verb The main verb
	 */
	public SPhraseSpec createSentence(Object verb) {
		return createSentence(new NPPhraseSpec(Constants.PRO_EXPLETIVE), verb);
	}
	
	/**
	 * Creates a sentence object from a given verb and a subject noun phrase.
	 * 
	 * @param subj The subject noun phrase (as a string or
	 * 	a NPPhraseSpec object)
	 * @param verb The main verb
	 */
	public SPhraseSpec createSentence(Object subj, Object verb) {
		Object s = subj;
		if (subj instanceof String) {
			s = createNounPhrase((String) subj);
		}

		Object v = verb;
		if (v instanceof String) {
			v = lexicon.getComplexVerb((String) v);
		}
		if (v == null) {
			v = verb;
		}
		
		return new SPhraseSpec(s, v);
	}
	
	/**
	 * Creates a sentence object from a given verb as well as subject and object
	 * noun phrases.
	 * 
	 * @param subj The subject noun phrase
	 * @param verb The main verb
	 * @param comp The object noun phrase
	 */
	public SPhraseSpec createSentence(Object subj, Object verb, Object comp) {
		SPhraseSpec s = createSentence(subj, verb);
		
		Object c = comp;
		if (c instanceof String) {
			c = createNounPhrase((String) comp);
		}
		
		s.setComplement(c);
		return s;
	}

    public INFPhraseSpec createInfPhrase(String v, boolean comma) {
		return new INFPhraseSpec(v, comma);
    }

	public INFPhraseSpec createInfPhrase(String np, String v, boolean comma) {
		NPPhraseSpec obj = createNounPhrase(np);
		INFPhraseSpec ip =  new INFPhraseSpec(v, comma);
		ip.setObject(obj);
		return ip;
	}

	public INFPhraseSpec createInfPhrase(NPPhraseSpec np, String v, boolean comma) {
		NPPhraseSpec obj = np;
		INFPhraseSpec ip =  new INFPhraseSpec(v, comma);
		ip.setObject(obj);
		return ip;
	}

	public INFPhraseSpec createInfPhrase(String np, String v) {
		NPPhraseSpec npP = createNounPhrase(np);
		INFPhraseSpec ip =  new INFPhraseSpec(v, true);
		ip.setObject(npP);
		return ip;
	}

	public INFPhraseSpec createInfPhrase(String v) {
		return new INFPhraseSpec(v, true);
	}
}
