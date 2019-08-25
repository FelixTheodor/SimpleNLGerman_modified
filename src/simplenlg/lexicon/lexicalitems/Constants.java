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

import simplenlg.features.*;
import simplenlg.lexicon.morph.InflectionPattern;
import simplenlg.lexicon.morph.VerbInflectionPattern;
import simplenlg.realiser.NPPhraseSpec;

import java.util.Locale;

/**
 * This class consists entirely of static final fields, defining a small number
 * of function words that the {@link simplenlg.lexicon.Lexicon} loads at runtime
 * by default.
 * 
 * @author agatt
 * 
 */
public class Constants {

	// VERB INFLECTION PATTERNS
	public static VerbInflectionPattern VERB_INFLECTION_DEFAULT = new VerbInflectionPattern(
			"default",
			new String[] { "e", "st",  "t", "en", "t",  "en" },
			new String[] { "",  "st",  "",  "en", "t",  "en" },
			new String[] { "e", "est", "e", "en", "et", "en" },
			new String[] { "e", "est", "e", "en", "et", "en" },
			new String[] { "e", "t" }
			);

	public static VerbInflectionPattern VERB_INFLECTION_NO_IMP_E = VERB_INFLECTION_DEFAULT.clone("no imperative-e", new String[] {"", "t"});
	public static VerbInflectionPattern VERB_INFLECTION_ALT_IMPERATIVE = VERB_INFLECTION_NO_IMP_E.clone("alt. imperative", WordMorph.ALTERNATE_IMPERATIVE);
	
	public static VerbInflectionPattern VERB_INFLECTION_ERN = new VerbInflectionPattern(
			"ern/eln",
			new String[] { "e", "st",  "t", "n",  "t",  "n"  },
			new String[] { "",  "st",  "",  "en", "t",  "en" },
			new String[] { "e", "st",  "e", "n",  "t",  "n"  },
			new String[] { "e", "est", "e", "en", "et", "en" },
			new String[] { "e", "t" }
			);
	
	public static VerbInflectionPattern VERB_INFLECTION_ERN_ELISION = VERB_INFLECTION_ERN.clone("ern/eln elision", WordMorph.STEM_E_ELISION);
	
	public static VerbInflectionPattern VERB_INFLECTION_EPENTHESIS = new VerbInflectionPattern(
			"epenthesis",
			new String[] { "e", "st",  "t", "en", "et", "en" },
			new String[] { "",  "st",  "",  "en", "et", "en" },
			new String[] { "e", "est", "e", "en", "et", "en" },
			new String[] { "e", "est", "e", "en", "et", "en" },
			new String[] { "e", "et" }
			);
	
	public static VerbInflectionPattern VERB_INFLECTION_MODAL = new VerbInflectionPattern(
			"modal",
			new String[] { "",  "st",  "",  "en", "t",  "en" },
			new String[] { "",  "st",  "",  "en", "t",  "en" },
			new String[] { "e", "est", "e", "en", "et", "en" },
			new String[] { "e", "est", "e", "en", "et", "en" },
			new String[] { "e", "t" },
			WordMorph.MODAL_INFLECTION
			);

	public static VerbInflectionPattern VERB_INFLECTION_TUN = new VerbInflectionPattern(
			"tun",
			new String[] { "e", "st",  "t", "n",  "t",  "n"  },
			new String[] { "",  "st",  "",  "en", "et", "en" },
			new String[] { "e", "est", "e", "en", "et", "en" },
			new String[] { "e", "est", "e", "en", "et", "en" },
			new String[] { "e", "t" }
			);

	public static VerbInflectionPattern VERB_INFLECTION_WERDEN = new VerbInflectionPattern(
			"werden",
			new String[] { "e", "st",  "d", "en", "et", "en" },
			new String[] { "",  "st",  "",  "en", "et", "en" },
			new String[] { "e", "est", "e", "en", "et", "en" },
			new String[] { "e", "est", "e", "en", "et", "en" },
			new String[] { "e", "et" }
			);
	
	public static VerbInflectionPattern VERB_INFLECTION_SEIN = new VerbInflectionPattern(
			"sein",
			new String[] { "bin", "bist", "ist", "sind", "seid", "sind" },
			new String[] { "war", "warst", "war", "waren", "wart", "waren" },
			new String[] { "sei", "seist", "sei", "seien", "seiet", "seien" },
			new String[] { "wäre", "wärst", "wäre", "wären", "wärt", "wären" },
			new String[] { "sei", "seid" },
			WordMorph.FULL_FORMS
			);

	public static VerbInflectionPattern[] ALL_INFLECTION_PATTERNS = new VerbInflectionPattern[] {
		Constants.VERB_INFLECTION_ALT_IMPERATIVE, 	Constants.VERB_INFLECTION_DEFAULT,
		Constants.VERB_INFLECTION_EPENTHESIS,		Constants.VERB_INFLECTION_ERN,
		Constants.VERB_INFLECTION_ERN_ELISION,		Constants.VERB_INFLECTION_MODAL,
		Constants.VERB_INFLECTION_NO_IMP_E,			Constants.VERB_INFLECTION_SEIN,
		Constants.VERB_INFLECTION_TUN,				Constants.VERB_INFLECTION_WERDEN
	};
	
	
	// DETERMINER
	
	public static final String[] ALL_DEFINITE = new String [] {
		"der", "die", "das", "den", "dem", "des"
	};

	public static final String[] ALL_INDEFINITE = new String [] {
		"ein", "eine", "einer", "eines", "einem", "einen",
		"einige", "einiger", "einigen"
	};
	
	public static final InflectionPattern<Determiner> DEFINITE_DET_PATTERN = new InflectionPattern<Determiner>(
			new String[] {"der", "den", "des", "dem"},
			new String[] {"die", "die", "der", "der"},
			new String[] {"das", "das", "des", "dem"},
			new String[] {"die", "die", "der", "den"},
			WordMorph.FULL_FORMS
			);
	
	public static final Determiner DEFINITE_DET = new Determiner("der", DEFINITE_DET_PATTERN, true);

	public static final InflectionPattern<Determiner> INDEFINITE_DET_PATTERN = new InflectionPattern<Determiner>(
			new String[] {"ein", "einen", "eines", "einem"},
			new String[] {"eine", "eine", "einer", "einer"},
			new String[] {"ein", "ein", "eines", "einem"},
			new String[] {"einige", "einige", "einiger", "einigen"},
			WordMorph.FULL_FORMS
			);
	
	public static final Determiner INDEFINITE_DET = new Determiner("ein", INDEFINITE_DET_PATTERN, false);

	public static final Determiner[] ALL_DETERMINERS = new Determiner[] {
			Constants.DEFINITE_DET, Constants.INDEFINITE_DET
	};
	

	// AUXILIARIES
	
	public static final Verb AUX_WERDEN = new Verb(
			"werden", "wurde", "geworden", "wir", "würde", AuxType.SEIN, VerbType.AUX, Constants.VERB_INFLECTION_WERDEN
			);
	
	public static final Verb AUX_HABEN = new Verb(
			"haben", "hatte", "gehabt", "ha", "hätte", AuxType.HABEN, VerbType.AUX, Constants.VERB_INFLECTION_DEFAULT
			);
	
	public static final Verb AUX_SEIN = new Verb(
			"sein", "war", "gewesen", "", "wär", "seiend", AuxType.SEIN, VerbType.AUX, Constants.VERB_INFLECTION_SEIN
			);

	public static final Verb[] ALL_AUXILIARIES = new Verb[] {
		Constants.AUX_HABEN, Constants.AUX_SEIN, Constants.AUX_WERDEN
	};
	
	
	// MODAL VERBS
	
	public static final Verb DÜRFEN = new Verb(
			"dürfen", "durfte", "gedurft", "darf", "dürfte", AuxType.HABEN, VerbType.MODAL, Constants.VERB_INFLECTION_MODAL
			);
	
	public static final Verb BEDÜRFEN = new Verb(
			"bedürfen", "bedurfte", "bedurft", "bedarf", "bedürfte", AuxType.HABEN, VerbType.MODAL, Constants.VERB_INFLECTION_MODAL
			);

	public static final Verb KÖNNEN = new Verb(
			"können", "konnte", "gekonnt", "kann", "könnte", AuxType.HABEN, VerbType.MODAL, Constants.VERB_INFLECTION_MODAL
			);
	
	public static final Verb MÖGEN = new Verb(
			"mögen", "mochte", "gemocht", "mag", "möchte", AuxType.HABEN, VerbType.MODAL, Constants.VERB_INFLECTION_MODAL
			);
	
	public static final Verb VERMÖGEN = new Verb(
			"vermögen", "vermochte", "vermocht", "vermag", "vermöchte", AuxType.HABEN, VerbType.MODAL, Constants.VERB_INFLECTION_MODAL
			);

	public static final Verb MÜSSEN = new Verb(
			"müssen", "musste", "gemusst", "muss", "müsste", AuxType.HABEN, VerbType.MODAL, Constants.VERB_INFLECTION_MODAL
			);
	
	public static final Verb SOLLEN = new Verb(
			"sollen", "sollte", "gesollt", "soll", "", AuxType.HABEN, VerbType.MODAL, Constants.VERB_INFLECTION_MODAL
			);
	
	public static final Verb WOLLEN = new Verb(
			"wollen", "wollte", "gewollt", "will", "", AuxType.HABEN, VerbType.MODAL, Constants.VERB_INFLECTION_MODAL
			);
	
	public static final Verb[] ALL_MODALS = new Verb[] {
		Constants.DÜRFEN, Constants.KÖNNEN, Constants.MÖGEN,
		Constants.MÜSSEN, Constants.SOLLEN, Constants.WOLLEN,
	};
	
	// is not modal, but inflects like one
	public static final Verb WISSEN = new Verb(
			"wissen", "wusste",	"gewusst", "weiß", "wüsste", AuxType.HABEN,	Constants.VERB_INFLECTION_MODAL
			);
	
	// PRONOUNS
	
	public static final InflectionPattern<Pronoun> PERS_PRO_1ST_PATTERN = new InflectionPattern<Pronoun>(
			new String[] { "ich", "mich", "meiner", "mir" },
			new String[] { "wir", "uns",  "unser",  "uns" },
			WordMorph.FULL_FORMS
		);
	public static final InflectionPattern<Pronoun> PERS_PRO_2ND_PATTERN = new InflectionPattern<Pronoun>(
			new String[] { "du",  "dich", "deiner", "dir" },
			new String[] { "ihr", "euch", "euer",   "euch" },
			WordMorph.FULL_FORMS
		);
	public static final InflectionPattern<Pronoun> PERS_PRO_HON_PATTERN = new InflectionPattern<Pronoun>(
			new String[] { "Sie", "Sie", "Ihrer", "Ihnen" },
			new String[] { "Sie", "Sie", "Ihrer", "Ihnen" },
			WordMorph.FULL_FORMS
		);
	public static final InflectionPattern<Pronoun> PERS_PRO_3RD_PATTERN = new InflectionPattern<Pronoun>(
			new String[] { "er",  "ihn", "seiner", "ihm" },
			new String[] { "sie", "sie", "ihrer",  "ihr" },
			new String[] { "es",  "es",  "seiner", "ihm" },
			new String[] { "sie", "sie", "ihrer",  "ihnen" },
			WordMorph.FULL_FORMS
		);

	public static final Pronoun PRO_1ST = new Pronoun("ich", PERS_PRO_1ST_PATTERN, PERS_PRO_1ST_PATTERN, Person.FIRST);
	public static final Pronoun PRO_2ND = new Pronoun("du", PERS_PRO_2ND_PATTERN, PERS_PRO_2ND_PATTERN, Person.SECOND);
	// TODO: semantically 2nd, but declination like 3rd person -- can this cause any trouble?
	public static final Pronoun PRO_3RD = new Pronoun("er", PERS_PRO_3RD_PATTERN, PERS_PRO_3RD_PATTERN, Person.THIRD);
	public static final Pronoun PRO_HON = new Pronoun("Sie", PERS_PRO_HON_PATTERN, PERS_PRO_HON_PATTERN, Person.THIRD);
	public static final Pronoun PRO_EXPLETIVE = new Pronoun("es", "es", "seiner", "ihm", "sie", "sie", "ihrer", "ihnen", Person.THIRD, Gender.NEUTER);

	public static final Pronoun[] PERSONAL_PRONOUNS = new Pronoun[] {
		Constants.PRO_1ST,		Constants.PRO_2ND,
		Constants.PRO_3RD,		Constants.PRO_HON,
		};
	
	public static final InflectionPattern<Pronoun> REFL_PRO_3RD_PATTERN = new InflectionPattern<Pronoun>(
			new String[] { "", "sich", "seiner", "sich" },
			new String[] { "", "sich", "ihrer",  "sich" },
			new String[] { "", "sich", "seiner", "sich" },
			new String[] { "", "sich", "ihrer",  "sich" },
			WordMorph.FULL_FORMS
		);
	public static final Pronoun PRO_REFLEXIVE_3RD = new Pronoun("sich", REFL_PRO_3RD_PATTERN, REFL_PRO_3RD_PATTERN, Person.THIRD);


	public static final InflectionPattern<Pronoun> POSS_ART_PATTERN = new InflectionPattern<Pronoun>(
			new String[] { "",  "en", "es", "em" },
			new String[] { "e", "e",  "er", "er" },
			new String[] { "",  "",   "es", "em" },
			new String[] { "e", "e",  "er", "en" }
		);
	public static final InflectionPattern<Pronoun> POSS_ART_ELISION_PATTERN = new InflectionPattern<Pronoun>(
			new String[] { "",  "en", "es", "em" },
			new String[] { "e", "e",  "er", "er" },
			new String[] { "",  "",   "es", "em" },
			new String[] { "e", "e",  "er", "en" },
			WordMorph.STEM_E_ELISION
		);
	public static final InflectionPattern<Pronoun> POSS_NOUN_PATTERN = new InflectionPattern<Pronoun>(
			new String[] { "er", "en", "es", "em" },
			new String[] { "e",  "e",  "er", "er" },
			new String[] { "es", "es", "es", "em" },
			new String[] { "e",  "e",  "er", "en" }
		);
	public static final InflectionPattern<Pronoun> POSS_NOUN_ELISION_PATTERN = new InflectionPattern<Pronoun>(
			new String[] { "er", "en", "es", "em" },
			new String[] { "e",  "e",  "er", "er" },
			new String[] { "es", "es", "es", "em" },
			new String[] { "e",  "e",  "er", "en" },
			WordMorph.STEM_E_ELISION
		);

	public static final Pronoun PRO_POSS_1ST_SG = new Pronoun("mein", POSS_ART_PATTERN, POSS_NOUN_PATTERN, Person.FIRST, NumberAgr.SINGULAR);
	public static final Pronoun PRO_POSS_1ST_PL = new Pronoun("unser", POSS_ART_PATTERN, POSS_NOUN_PATTERN, Person.FIRST, NumberAgr.PLURAL);
	public static final Pronoun PRO_POSS_2ND_SG = new Pronoun("dein", POSS_ART_PATTERN, POSS_NOUN_PATTERN, Person.SECOND, NumberAgr.SINGULAR);
	public static final Pronoun PRO_POSS_2ND_PL = new Pronoun("euer", POSS_ART_ELISION_PATTERN, POSS_NOUN_ELISION_PATTERN, Person.SECOND, NumberAgr.PLURAL);
	public static final Pronoun PRO_POSS_3RD_SG_MASC = new Pronoun("sein", POSS_ART_PATTERN, POSS_NOUN_PATTERN, Person.THIRD, NumberAgr.SINGULAR, Gender.MASCULINE);
	public static final Pronoun PRO_POSS_3RD_SG_NEUT = new Pronoun("sein", POSS_ART_PATTERN, POSS_NOUN_PATTERN, Person.THIRD, NumberAgr.SINGULAR, Gender.NEUTER);
	public static final Pronoun PRO_POSS_3RD_SG_FEM  = new Pronoun("ihr", POSS_ART_PATTERN, POSS_NOUN_PATTERN, Person.THIRD, NumberAgr.SINGULAR, Gender.FEMININE);
	public static final Pronoun PRO_POSS_3RD_PL = new Pronoun("ihr", POSS_ART_PATTERN, POSS_NOUN_PATTERN, Person.THIRD, NumberAgr.PLURAL);
	public static final Pronoun PRO_POSS_HON = new Pronoun("Ihr", POSS_ART_PATTERN, POSS_NOUN_PATTERN, Person.THIRD, NumberAgr.PLURAL);
	
	public static final InflectionPattern<Pronoun> PRO_WEAK_ADJ_PATTERN = new InflectionPattern<Pronoun>(
			new String[] { "",  "n", "n", "n" },
			new String[] { "",  "",  "n", "n" },
			new String[] { "",  "",  "n", "n" },
			new String[] { "n", "n", "n", "n" }
			);
	public static final InflectionPattern<Pronoun> PRO_STRONG_ADJ_PATTERN = new InflectionPattern<Pronoun>(
			new String[] { "r", "n", "n", "m" },
			new String[] { "",  "",  "r", "r" },
			new String[] { "s", "s", "n", "m" },
			new String[] { "",  "",  "r", "n" }
			);

	public static final Pronoun PRO_POSS_ALT_1ST_SG = new Pronoun("meinige", PRO_WEAK_ADJ_PATTERN, PRO_WEAK_ADJ_PATTERN, Person.FIRST, NumberAgr.SINGULAR);
	public static final Pronoun PRO_POSS_ALT_1ST_PL = new Pronoun("unsrige", PRO_WEAK_ADJ_PATTERN, PRO_WEAK_ADJ_PATTERN, Person.FIRST, NumberAgr.PLURAL);
	public static final Pronoun PRO_POSS_ALT_2ND_SG = new Pronoun("deinige", PRO_WEAK_ADJ_PATTERN, PRO_WEAK_ADJ_PATTERN, Person.SECOND, NumberAgr.SINGULAR);
	public static final Pronoun PRO_POSS_ALT_2ND_PL = new Pronoun("eurige",  PRO_WEAK_ADJ_PATTERN, PRO_WEAK_ADJ_PATTERN, Person.SECOND, NumberAgr.PLURAL);
	public static final Pronoun PRO_POSS_ALT_3RD_SG_MASC = new Pronoun("seinige", PRO_WEAK_ADJ_PATTERN, PRO_WEAK_ADJ_PATTERN, Person.THIRD, NumberAgr.SINGULAR, Gender.MASCULINE);
	public static final Pronoun PRO_POSS_ALT_3RD_SG_NEUT = new Pronoun("seinige", PRO_WEAK_ADJ_PATTERN, PRO_WEAK_ADJ_PATTERN, Person.THIRD, NumberAgr.SINGULAR, Gender.NEUTER);
	public static final Pronoun PRO_POSS_ALT_3RD_SG_FEM = new Pronoun("ihrige", PRO_WEAK_ADJ_PATTERN, PRO_WEAK_ADJ_PATTERN, Person.THIRD, NumberAgr.SINGULAR, Gender.FEMININE);
	public static final Pronoun PRO_POSS_ALT_3RD_PL = new Pronoun("ihrige", PRO_WEAK_ADJ_PATTERN, PRO_WEAK_ADJ_PATTERN, Person.THIRD, NumberAgr.PLURAL);
	public static final Pronoun PRO_POSS_ALT_HON = new Pronoun("Ihrige", PRO_WEAK_ADJ_PATTERN, PRO_WEAK_ADJ_PATTERN, Person.THIRD, NumberAgr.PLURAL);

	public static final Pronoun[] POSSESSIVE_PRONOUNS = new Pronoun[] {
		Constants.PRO_POSS_1ST_SG,	Constants.PRO_POSS_1ST_PL,
		Constants.PRO_POSS_2ND_SG,  Constants.PRO_POSS_2ND_PL,
		Constants.PRO_POSS_3RD_SG_MASC,
		Constants.PRO_POSS_3RD_SG_NEUT,
		Constants.PRO_POSS_3RD_SG_FEM,
		Constants.PRO_POSS_3RD_PL,	Constants.PRO_POSS_HON,
		Constants.PRO_POSS_ALT_1ST_SG, Constants.PRO_POSS_ALT_1ST_PL,
		Constants.PRO_POSS_ALT_2ND_SG, Constants.PRO_POSS_ALT_2ND_PL,
		Constants.PRO_POSS_ALT_3RD_SG_MASC,
		Constants.PRO_POSS_ALT_3RD_SG_NEUT,
		Constants.PRO_POSS_ALT_3RD_SG_FEM,
		Constants.PRO_POSS_ALT_3RD_PL, Constants.PRO_POSS_ALT_HON,
	};
	
	
	// identisch mit REL_PRON_PATTERN bis auf (optionales) derer <-> deren
	public static final InflectionPattern<Pronoun> DEM_PRON_DERDIEDAS_PATTERN = new InflectionPattern<Pronoun>(
			new String[] {"der", "den", "dessen", "dem"},
			new String[] {"die", "die", "deren",  "der"},
			new String[] {"das", "das", "dessen", "dem"},
			new String[] {"die", "die", "derer",  "denen"},
			WordMorph.FULL_FORMS
		);

	public static final InflectionPattern<Pronoun> DEM_PRON_DERJENIGE_PATTERN = new InflectionPattern<Pronoun>(
			new String[] { "derjenige", "denjenigen", "desjenigen", "demjenigen" },
			new String[] { "diejenige", "diejenige",  "derjenigen", "derjenigen" },
			new String[] { "dasjenige", "dasjenige",  "desjenigen", "demjenigen" },
			new String[] { "diejenigen","diejenigen", "derjenigen", "denjenigen" },
			WordMorph.FULL_FORMS
		);
	public static final InflectionPattern<Pronoun> DEM_PRON_DERSELBE_PATTERN = new InflectionPattern<Pronoun>(
			new String[] { "derselbe", "denselben", "desselben", "demselben" },
			new String[] { "dieselbe", "dieselbe",  "derselben", "derselben" },
			new String[] { "dasselbe", "dasselbe",  "desselben", "demselben" },
			new String[] { "dieselben","dieselben", "derselben", "denselben" },
			WordMorph.FULL_FORMS
		);
	
	public static final Pronoun PRO_DEM_DAS	   = new Pronoun("das", DEM_PRON_DERDIEDAS_PATTERN, DEM_PRON_DERDIEDAS_PATTERN);
	public static final Pronoun PRO_DEM_DIESER = new Pronoun("dies", POSS_NOUN_PATTERN, POSS_NOUN_PATTERN);
	public static final Pronoun PRO_DEM_JENER  = new Pronoun("jen", POSS_NOUN_PATTERN, POSS_NOUN_PATTERN);
	public static final Pronoun PRO_DEM_DERJENIGE = new Pronoun("derjenige", DEM_PRON_DERJENIGE_PATTERN, DEM_PRON_DERJENIGE_PATTERN);
	public static final Pronoun PRO_DEM_DERSELBE  = new Pronoun("derselbe", DEM_PRON_DERSELBE_PATTERN, DEM_PRON_DERSELBE_PATTERN);

	public static final Pronoun[] DEMONSTRATIVE_PRONOUNS = new Pronoun[] {
		Constants.PRO_DEM_DIESER,	Constants.PRO_DEM_JENER,
		Constants.PRO_DEM_DAS,
		Constants.PRO_DEM_DERJENIGE, Constants.PRO_DEM_DERSELBE,
	};
	
	public static final InflectionPattern<Pronoun> REL_PRON_PATTERN = new InflectionPattern<Pronoun>(
			new String[] { "der", "den", "dessen", "dem" },
			new String[] { "die", "die", "deren",  "der" },
			new String[] { "das", "das", "dessen", "dem" },
			new String[] { "die", "die", "deren",  "denen" },
			WordMorph.FULL_FORMS
		);
	
	public static final Pronoun PRO_REL = new Pronoun("der", REL_PRON_PATTERN, REL_PRON_PATTERN);
	public static final Pronoun PRO_WELCHER = new Pronoun("welch", POSS_NOUN_PATTERN, POSS_NOUN_PATTERN);

	public static final InflectionPattern<Pronoun> INTERROGATIVE_WER_PATTERN = new InflectionPattern<Pronoun>(
			new String[] { "wer", "wen", "wessen", "wem" },
			WordMorph.FULL_FORMS
		);
	public static final InflectionPattern<Pronoun> INTERROGATIVE_WAS_PATTERN = new InflectionPattern<Pronoun>(
			new String[] { "was", "was", "wessen", "was" },
			WordMorph.FULL_FORMS
		);
	
	public static final Pronoun PRO_INTER_WER = new Pronoun("wer", INTERROGATIVE_WER_PATTERN, INTERROGATIVE_WER_PATTERN);
	public static final Pronoun PRO_INTER_WAS = new Pronoun("was", INTERROGATIVE_WAS_PATTERN, INTERROGATIVE_WAS_PATTERN);
	
	public static final InflectionPattern<Pronoun> JEMAND_UNINF_PATTERN = new InflectionPattern<Pronoun>(
			new String[] { "", "", "", "es" }
		);
	public static final InflectionPattern<Pronoun> JEMAND_PATTERN = new InflectionPattern<Pronoun>(
			new String[] { "", "en", "es", "em" }
		);
	public static final InflectionPattern<Pronoun> BEIDE_PATTERN = new InflectionPattern<Pronoun>(
			new String[] { "s", "s", "s", "m" },
			new String[] { "",  "",  "r", "n" }
		);

	
	public static final Pronoun PRO_INDEF_ALLE 		= new Pronoun("all", POSS_NOUN_PATTERN, POSS_NOUN_PATTERN);
	public static final Pronoun PRO_INDEF_ANDERE    = new Pronoun("andere", PRO_STRONG_ADJ_PATTERN, PRO_STRONG_ADJ_PATTERN);
	public static final Pronoun PRO_INDEF_BEIDE		= new Pronoun("beide", BEIDE_PATTERN, BEIDE_PATTERN); 
	public static final Pronoun PRO_INDEF_EINER		= new Pronoun("ein", POSS_NOUN_PATTERN, POSS_NOUN_PATTERN); // POSS_ART_PATTERN als Artikel?
	public static final Pronoun PRO_INDEF_ETLICHE 	= new Pronoun("etliche", PRO_STRONG_ADJ_PATTERN, PRO_STRONG_ADJ_PATTERN);
	public static final Pronoun PRO_INDEF_IRGENDEIN = new Pronoun("irgendein", POSS_ART_PATTERN, POSS_NOUN_PATTERN);
	public static final Pronoun PRO_INDEF_IRGENDWELCHER = new Pronoun("irgendwelch", POSS_NOUN_PATTERN, POSS_NOUN_PATTERN);
	public static final Pronoun PRO_INDEF_JEDER 	= new Pronoun("jed", POSS_NOUN_PATTERN, POSS_NOUN_PATTERN);
	public static final Pronoun PRO_INDEF_JEGLICHER = new Pronoun("jeglich", POSS_NOUN_PATTERN, POSS_NOUN_PATTERN);
	public static final Pronoun PRO_INDEF_JEDWEDER 	= new Pronoun("jedwed", POSS_NOUN_PATTERN, POSS_NOUN_PATTERN);
	public static final Pronoun PRO_INDEF_JEMAND	= new Pronoun("jemand", JEMAND_UNINF_PATTERN, JEMAND_PATTERN);
	public static final Pronoun PRO_INDEF_IRGENDJEMAND = new Pronoun("irgendjemand", JEMAND_UNINF_PATTERN, JEMAND_PATTERN);
	public static final Pronoun PRO_INDEF_NIEMAND	= new Pronoun("niemand", JEMAND_UNINF_PATTERN, JEMAND_PATTERN);
	public static final Pronoun PRO_INDEF_KEIN		= new Pronoun("kein", POSS_ART_PATTERN, POSS_NOUN_PATTERN);
	public static final Pronoun PRO_INDEF_MANCHER	= new Pronoun("manch", POSS_NOUN_PATTERN, POSS_NOUN_PATTERN);
	public static final Pronoun PRO_INDEF_MEHRERE	= new Pronoun("mehrer", POSS_NOUN_PATTERN, POSS_NOUN_PATTERN);

	
	public static final Pronoun[] ALL_PRONOUNS = new Pronoun[] {
		Constants.PRO_1ST,		Constants.PRO_2ND,
		Constants.PRO_HON,		Constants.PRO_3RD,
		Constants.PRO_EXPLETIVE,
		Constants.PRO_POSS_1ST_SG,	Constants.PRO_POSS_1ST_PL,
		Constants.PRO_POSS_2ND_SG,  Constants.PRO_POSS_2ND_PL,
		Constants.PRO_POSS_3RD_SG_MASC,
		Constants.PRO_POSS_3RD_SG_NEUT,
		Constants.PRO_POSS_3RD_SG_FEM,
		Constants.PRO_POSS_3RD_PL,	Constants.PRO_POSS_HON,
		Constants.PRO_DEM_DIESER,	Constants.PRO_DEM_JENER,
		Constants.PRO_DEM_DAS,
		Constants.PRO_REFLEXIVE_3RD,
		Constants.PRO_REL,			Constants.PRO_WELCHER,
		Constants.PRO_POSS_ALT_1ST_SG, Constants.PRO_POSS_ALT_1ST_PL,
		Constants.PRO_POSS_ALT_2ND_SG, Constants.PRO_POSS_ALT_2ND_PL,
		Constants.PRO_POSS_ALT_3RD_SG_MASC,
		Constants.PRO_POSS_ALT_3RD_SG_NEUT,
		Constants.PRO_POSS_ALT_3RD_SG_FEM,
		Constants.PRO_POSS_ALT_3RD_PL, Constants.PRO_POSS_ALT_HON,
		Constants.PRO_DEM_DERJENIGE,   Constants.PRO_DEM_DERSELBE,
		Constants.PRO_INTER_WER,	   Constants.PRO_INTER_WAS,
		Constants.PRO_INDEF_ALLE,		Constants.PRO_INDEF_ETLICHE,
		Constants.PRO_INDEF_IRGENDEIN,	Constants.PRO_INDEF_IRGENDWELCHER,
		Constants.PRO_INDEF_JEDER,		Constants.PRO_INDEF_JEGLICHER,
		Constants.PRO_INDEF_JEDWEDER,	Constants.PRO_INDEF_JEMAND,
		Constants.PRO_INDEF_NIEMAND,	Constants.PRO_INDEF_IRGENDJEMAND,
		Constants.PRO_INDEF_KEIN,		Constants.PRO_INDEF_MANCHER,
		Constants.PRO_INDEF_MEHRERE,	Constants.PRO_INDEF_EINER,
		Constants.PRO_INDEF_BEIDE,		Constants.PRO_INDEF_ANDERE,
	};
	
	public static final Pronoun[] PLURAL_PRONOUNS = new Pronoun[] {
		Constants.PRO_HON,
		Constants.PRO_INDEF_ALLE,		Constants.PRO_INDEF_ETLICHE,
		Constants.PRO_INDEF_MEHRERE,	Constants.PRO_INDEF_BEIDE,	
	};
	
	
	// PREPOSITIONS
	
	public static final Preposition PASSIVE_VON = new Preposition("von", Case.DATIVE);
	
	public static final String[] PREPOSITIONS_ACCUSATIVE = new String[] {
		"à", "betreffend", "bis", "durch", "entlang", "für", "gegen", "je", "kontra",
		"ohne", "per", "pro", "um", "versus", "via", "wider",
	};
	
	public static final String[] PREPOSITIONS_DATIVE = new String[] {
		"ab", "aus", "außer", "bei", "binnen", "dank", "entgegen", "entsprechend",
		"fern", "gegenüber", "gemäß", "laut", "mit", "mitsamt", "nach", "nahe",
		"nebst", "samt", "seit", "vis-à-vis", "von", "zu", "zufolge", "zuliebe",
		"von ... an",
	};
	
	public static final String[] PREPOSITIONS_DAT_ACC = new String[] {
		"an", "auf", "hinter", "in", "neben", "über", "unter", "vor", "zwischen",
	};
	
	public static final String[] PREPOSITIONS_GENITIVE = new String[] {
		"abseits", "abzüglich", "angesichts", "anhand", "anlässlich", "anstatt",
		"anstelle", "aufgrund", "aufseiten", "ausgangs", "ausschließlich",
		"ausweislich", "außerhalb", "behufs", "beiderseits", "beidseits", "betreffs",
		"bezüglich", "dank", "diesseits", "eingangs", "eingedenk", "einschließlich",
		"exklusive", "fernab", "halber", "hinsichtlich", "infolge",
		"inklusive", "inmitten", "innerhalb", "innert", "in puncto", "jenseits",
		"kraft", "längs", "längsseits", "laut", "mangels", "mithilfe", "mittels",
		"namens", "ob",	"oberhalb", "punkto", "rücksichtlich", "seitens", "seitlich", 
		"statt", "trotz", "um ... willen", "unbeschadet", "uneingedenk", "unfern",
		"ungeachtet", "unterhalb", "unweit", "vermittels", "vonseiten",
		"vorbehaltlich", "vorbehältlich", "während", "wegen",
		"weitab", "zugunsten", "zuhanden", "zu Händen", "zulasten", "zuseiten",
		"zuungunsten", "zuzüglich", "zwecks",
	};
	
	
	// CONJUNCTIONS
	
	public static final Conjunction UND = new Conjunction("und",
			ConjunctionType.COORDINATING);

	public static final Conjunction ODER = new Conjunction("oder",
			ConjunctionType.COORDINATING);

	public static final Conjunction ABER = new Conjunction("aber",
			ConjunctionType.SUBORDINATING);

	public static final Conjunction WEIL = new Conjunction("weil",
			ConjunctionType.SUBORDINATING);

	public static final Conjunction DASS = new Conjunction("dass",
			ConjunctionType.SUBORDINATING);
	
	public static final Conjunction[] ALL_CONJUNCTIONS = new Conjunction[] {
			Constants.UND, Constants.ODER, Constants.ABER, Constants.WEIL,
			Constants.DASS};
	
	// VERBS (collected)
	
	public static final Verb TUN = new Verb(
			"tun", "tat", "getan", "", "tät", "tuend",	AuxType.HABEN,	Constants.VERB_INFLECTION_TUN
			);

	public static final Verb[] ALL_VERBS = new Verb[]{
		Constants.AUX_HABEN, Constants.AUX_SEIN, Constants.AUX_WERDEN,
		Constants.DÜRFEN, Constants.KÖNNEN, Constants.MÖGEN,
		Constants.MÜSSEN, Constants.SOLLEN, Constants.WOLLEN,
		Constants.TUN,	  Constants.WISSEN,
		Constants.BEDÜRFEN, Constants.VERMÖGEN
	};
	
	

	
	/**
	 * Utility method: Get the personal pronoun corresponding to a specific
	 * {@link simplenlg.features.Person}. 
	 * 
	 * @param p
	 *            A value of {@link simplenlg.features.Person}
	 * 
	 * @return The personal pronoun with these microplanner.features
	 */
	public static Pronoun getPersonalPronoun(Person p) {
		switch (p) {
		case FIRST:
			return Constants.PRO_1ST;
		case SECOND:
			return Constants.PRO_2ND;
		case THIRD:
		default:
			return Constants.PRO_3RD;
		}
	}

	public static Pronoun getHonorificPronoun(boolean possessive) {
		if (possessive) {
			return Constants.PRO_POSS_HON;
		} else {
			return Constants.PRO_HON;
		}
	}
	
	/**
	 * Gets the possessive pronoun with the given person, number and gender
	 * features.
	 * 
	 * @param p
	 *            the person feature
	 * @param n
	 *            the number feature
	 * @param g
	 *            the gender feature
	 * 
	 * @return the possessive pronoun, if one is found, <code>null</code>
	 *         otherwise.
	 */
	public static Pronoun getPossessivePronoun(Person p, NumberAgr n, Gender g) {
		switch (p) {
		case FIRST:
			return n == NumberAgr.SINGULAR ? Constants.PRO_POSS_1ST_SG : Constants.PRO_POSS_1ST_PL;
		case SECOND:
			return n == NumberAgr.SINGULAR ? Constants.PRO_POSS_2ND_SG : Constants.PRO_POSS_2ND_PL;
		case THIRD:
		default:
			if (n == NumberAgr.SINGULAR) {
				switch (g) {
				case MASCULINE:
					return Constants.PRO_POSS_3RD_SG_MASC;
				case FEMININE:
					return Constants.PRO_POSS_3RD_SG_FEM;
				case NEUTER:
				default:
					return Constants.PRO_POSS_3RD_SG_NEUT;
				}
			} else {
				return Constants.PRO_POSS_3RD_PL;
			}
		}
	}

	public static Pronoun getPossessivePronoun(Person p, NumberAgr n) {
		return getPossessivePronoun(p, n, Gender.NEUTER);
	}
	
	public static Pronoun getPossessivePronoun(NPPhraseSpec np) {
		return getPossessivePronoun(Person.THIRD, np.getNumber(), np.getGender());
	}
	
	public static Pronoun getReflexivePronoun(Person p) {
		if (p == Person.THIRD) {
			return Constants.PRO_REFLEXIVE_3RD;
		} else {
			return getPersonalPronoun(p);
		}
	}
	
	
	/**
	 * Gets the pronoun with the given baseform
	 * 
	 * @param baseform
	 *            the baseform
	 * 
	 * @return the pronoun, if one is found, <code>null</code> otherwise.
	 */
	public static Pronoun getPronoun(String baseform) {

		if (baseform != null) {
			for (Pronoun p : Constants.ALL_PRONOUNS) {
				if (baseform.equals(p.baseForm)) {
					return p;
				}
			}
			for (Pronoun p : Constants.ALL_PRONOUNS) {
				if (p.hasForm(baseform)) {
					return p;
				}
			}
		}

		return null;
	}


	/**
	 * Tries to determine the number from a given pronoun form
	 * 
	 * @param baseform
	 *            the baseform
	 * 
	 * @return the number, if pronoun form is found, <code>null</code> otherwise.
	 */
	public static NumberAgr getPronounNumber(String baseform) {

		if (baseform != null) {
			for (Pronoun p : Constants.ALL_PRONOUNS) {
				if (baseform.equals(p.baseForm)) {
					return NumberAgr.SINGULAR;
				}
			}
			for (Pronoun p : Constants.ALL_PRONOUNS) {
				if (baseform.equals(p.getForm(Case.NOMINATIVE, NumberAgr.PLURAL))) {
					return NumberAgr.PLURAL;
				}
			}

		}

		return null;
	}

	/**
	 * Get the <code>Conjunction</code> matching a <code>String</code>.
	 * 
	 * @param s
	 *            The <code>String</code>
	 * 
	 * @return The conjunction whose baseform is <code>s</code>, if one is
	 *         defined in this class, <code>null</code> otherwise.
	 * 
	 */
	public static Conjunction getConjunction(String s) {

		if (s.equalsIgnoreCase("und")) {
			return Constants.UND;
		} else if (s.equalsIgnoreCase("oder")) {
			return Constants.ODER;
		} else if (s.equalsIgnoreCase("aber")) {
			return Constants.ABER;
		} else if (s.equalsIgnoreCase("weil")) {
			return Constants.WEIL;
		}

		return null;
	}

/*	*//**
	 * Gets the word form of the definite or indefinite determiner with 
	 * a specific number, gender and case.
	 * 
	 * @author Marcel Bollmann
	 * 
	 * @param n
	 * 			The desired number.
	 * @param g
	 * 			The desired gender.
	 * @param c
	 * 			The desired case.
	 * @param definite
	 * 			Definiteness of the determiner.
	 * @return
	 * 			A string of the word form of the determiner with the desired qualities.
	 *//*
	public static String getDeterminerForm(NumberAgr n, Gender g, Case c, boolean definite) {
		String det = "das";
		
		if (n == NumberAgr.PLURAL) {
			switch (c) {
			case NOMINATIVE:
			case ACCUSATIVE:
				det = definite ? "die" : "einige"; 
				break;
			case GENITIVE:
				det = definite ? "der" : "einiger";
				break;
			case DATIVE:
				det = definite ? "den" : "einigen";
				break;
			}
		} else { // n == NumberAgr.SINGULAR
			switch (g) {
			case MASCULINE:
				switch (c) {
				case NOMINATIVE:
					det = definite ? "der" : "ein";
					break;
				case ACCUSATIVE:
					det = definite ? "den" : "einen";
					break;
				case GENITIVE:
					det = definite ? "des" : "eines";
					break;
				case DATIVE:
					det = definite ? "dem" : "einem";
					break;
				}
				break;
			case FEMININE:
				switch (c) {
				case NOMINATIVE:
				case ACCUSATIVE:
					det = definite ? "die" : "eine";
					break;
				case GENITIVE:
				case DATIVE:
					det = definite ? "der" : "einer";
					break;
				}
				break;
			case NEUTER:
				switch (c) {
				case NOMINATIVE:
				case ACCUSATIVE:
					det = definite ? "das" : "ein";
					break;
				case GENITIVE:
					det = definite ? "des" : "eines";
					break;
				case DATIVE:
					det = definite ? "dem" : "einem";
					break;
				}
				break;
			}
		}
	
		return det;
	}*/
	
	/**
	 * Gets the determiner with a specific baseform.
	 * 
	 * @param word
	 *            A <code>String</code>
	 * 
	 * @return The <code>Determiner</code> object which matches
	 *         <code>word</code>
	 */
	public static Determiner getDeterminer(String word) {
		for (String d : Constants.ALL_DEFINITE) {
			if (d.equalsIgnoreCase(word)) {
				return Constants.DEFINITE_DET;
			}
		}
		for (String d : Constants.ALL_INDEFINITE) {
			if (d.equalsIgnoreCase(word)) {
				return Constants.INDEFINITE_DET;
			}
		}
		
		return null;
	}

	public static Determiner getDeterminer(boolean definite) {
		if (definite) {
			return Constants.DEFINITE_DET;
		} else {
			return Constants.INDEFINITE_DET;
		}
	}
	
	/**
	 * Returns a Gender object from a string representing {der|die|das}.
	 * 
	 * @param gender
	 * 				The string.
	 * @return
	 * 				The corresponding gender, or NEUTER if not recognized.
	 */
	public static Gender detGender(String gender) {
		if (gender.equals("die")) {
			return Gender.FEMININE;
		} else if (gender.equals("der")) {
			return Gender.MASCULINE;
		} else {
			return Gender.NEUTER;
		}
	}
	
	/**
	 * Gets a preposition which is predifined in the
	 * {@link simplenlg.lexicon.lexicalitems.Constants} class.
	 * 
	 * @param baseform
	 *            the baseform of the preposition
	 * 
	 * @return the preposition with the specified baseform
	 * 
	 */
	public static Preposition getPreposition(String baseform) {
		Case c = getPrepositionCase(baseform);
		if (c == Case.DAT_ACC)	{ c = Case.ACCUSATIVE; }  // default decision
		else if (c == null)		{ c = Case.GENITIVE; }	  // default case
		
		return new Preposition(baseform, c);
	}
	
	public static Preposition getAdposition(String leftform, String rightform) {
		Case c = getPrepositionCase(leftform + " ... " + rightform);
		if (c == Case.DAT_ACC)	{ c = Case.ACCUSATIVE; }  // default decision
		else if (c == null)		{ c = Case.GENITIVE; }	  // default case
		
		return new Preposition(leftform, rightform, c);
	}

	public static Preposition getPostposition(String baseform) {
		Case c = getPrepositionCase(baseform);
		if (c == Case.DAT_ACC)	{ c = Case.ACCUSATIVE; }  // default decision
		else if (c == null)		{ c = Case.GENITIVE; }	  // default case
		
		return new Preposition("", baseform, c);
	}
		
	public static Case getPrepositionCase(String baseform) {

		for (String s : Constants.PREPOSITIONS_DAT_ACC) {
			if (s.equalsIgnoreCase(baseform)) {
				return Case.DAT_ACC;
			}
		}
		for (String s : Constants.PREPOSITIONS_ACCUSATIVE) {
			if (s.equalsIgnoreCase(baseform)) {
				return Case.ACCUSATIVE;
			}
		}
		for (String s : Constants.PREPOSITIONS_DATIVE) {
			if (s.equalsIgnoreCase(baseform)) {
				return Case.DATIVE;
			}
		}
		for (String s : Constants.PREPOSITIONS_GENITIVE) {
			if (s.equalsIgnoreCase(baseform)) {
				return Case.GENITIVE;
			}
		}
		
		return null;
	}
	
	public static Pronoun getInterrogative(InterrogativeType inttype) {
		switch (inttype) {
		case WAS:
			return Constants.PRO_INTER_WAS;
		case WER:
			return Constants.PRO_INTER_WER;
		case WO:
		case WARUM:
		case WIE:
		default:
			return new Pronoun(inttype.toString().toLowerCase());
		}
		
	}
	
	public static Verb getAuxiliary(String baseform) {
		if (baseform.equalsIgnoreCase("werden")) {
			return AUX_WERDEN;
		} else if (baseform.equalsIgnoreCase("haben")) {
			return AUX_HABEN;
		} else if (baseform.equalsIgnoreCase("sein")) {
			return AUX_SEIN;
		} else {
			return null;
		}
	}
	
	public static Verb getAuxiliary(AuxType type) {
		switch (type) {
		case HABEN:
		case HABEN_SEIN:
		default:
			return AUX_HABEN;
		case SEIN:
			return AUX_SEIN;
		}
	}
	
	public static Verb getModal(String modal) {
		for (Verb v : Constants.ALL_MODALS) {
			if (v.getBaseForm().equalsIgnoreCase(modal)) {
				return v;
			}
		}
		
		Verb v = new Verb(modal);
		v.setVerbType(VerbType.MODAL);
		return v;
	}
	
	
	public static boolean requiresPlural(Pronoun pro) {
		for (Pronoun plural : Constants.PLURAL_PRONOUNS) {
			if (pro.equals(plural)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isPossessivePronoun(Pronoun pro) {
		for (Pronoun p : Constants.POSSESSIVE_PRONOUNS) {
			if (p.equals(pro)) return true;
		}
		return false;
	}

	public static boolean isConstantVerb(Verb v) {
		for (Verb con : Constants.ALL_VERBS) {
			if (con.equals(v)) return true;
		}
		return false;
	}
	
	public static VerbInflectionPattern getInflectionPatternByName(String name) {
		for (VerbInflectionPattern ptrn : Constants.ALL_INFLECTION_PATTERNS) {
			if (ptrn.getName().equalsIgnoreCase(name)) return ptrn;
		}
		return Constants.VERB_INFLECTION_DEFAULT;
	}
	
	public static String capitalize(String word) {
		if (word.length()==1) {
			return word.toUpperCase(Locale.GERMAN);
		} else if (word.length()>1) {
			return word.substring(0, 1).toUpperCase(Locale.GERMAN).concat(word.substring(1));
		} else {
			return word;
		}
	}
	
}
