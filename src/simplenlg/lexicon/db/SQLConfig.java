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
package simplenlg.lexicon.db;

public class SQLConfig {

	/**
	 * The Id field in the database for lexical items.
	 */
	static final String ENTRY_ID = "euid";

	// query for complements table
	static final String complementQuery = "select c.transitivity, "
			+ "c.comp1, c.comp1Restr, c.comp2, c.comp2Restr, "
			+ "c.gRestr1, c.gRestr2 from complements c where c.euid=?";

	// query for adj features
	static final String adjFeatureQuery = "select a.positions, a.stative from adjectives a where a.euid=?";

	// query for adverb features
	static final String advFeatureQuery = "select a.positions, a.polarity from adverbs a where a.euid=?";

	// determiner features
	static final String detFeatureQuery = "select d.detType from determiners d where d.euid=?";

	// conjunction features
	static final String conjFeatureQuery = "select c.conjType, c.argType from conjunctions c where c.euid=?";

	// pronoun features
	static final String proFeatureQuery = "select p.gender, p.case1, p.possession, p.quantification, "
			+ "p.pronType from pronouns p where p.euid=?";

	// query for all entries
	static final String allEntries = "select e.euid, e.baseform, e.category, e.hasComp, "
			+ "e.infl, e.agr from entries e";

	// query for entries by category (incomplete): where clause set in the
	// appropriate method
	static final String entriesByCategory = "select e.euid, e.baseform, e.category, e.hasComp, "
			+ "e.infl, e.agr from entries e ";

	// retrieve a single entry by id
	static final String entryByID = "select e.baseform, e.category, e.hasComp, e.infl, e.agr "
			+ "from entries e where e.euid=?";

	// retrieve entries by baseform
	static final String entryByBaseform = "select e.euid, e.category, e.hasComp, e.infl, e.agr "
			+ "from entries e where e.baseform=?";

	// retrieve entries by category and baseform
	static final String entryByCatBaseform = "select e.euid, e.hasComp, e.infl, e.agr "
			+ "from entries e where e.category=? and e.baseform=?";

	// retrieve spelling variants of a word
	static final String wordVariants = "select v.form from variants v where v.euid=?";

	// derivations
	static final String derivations = "select neuid, relation from derivations d where d.euid=?";

	// retrieve verbframes
	static final String verbFrames = "select r.euid, c.class, c.superclass, "
			+ "d.frameid, d.description1, d.description2, d.syntax "
			+ "from vnframeref r left join vnclass c on (c.classid = r.classid) "
			+ "left join vnframedef d on(r.frameid = d.frameid) "
			+ "where r.quality = 1 order by c.classid";

	// path through the syntax node of a verb frame
	static final String SYNTAX_PATH = "SYNTAX/*";

	// Syntactic restrictions node in verbnet xml
	static final String SYNRESTR = "SYNRESTR";

	// Selectional restrictiosn node in verbnet xml
	static final String SELRESTR = "SELRESTR";

	// XMl attribute with the value for a syntax element in a verb frame
	static final String SLOT_ELEMENT_VALUE_ATT = "value";
}
