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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

import simplenlg.features.Agreement;
import simplenlg.features.AuxType;
import simplenlg.features.Case;
import simplenlg.features.Category;
import simplenlg.features.Gender;
import simplenlg.features.NumberAgr;
import simplenlg.lexicon.Lexicon;
import simplenlg.lexicon.lexicalitems.Adjective;
import simplenlg.lexicon.lexicalitems.Constants;
import simplenlg.lexicon.lexicalitems.LexicalItem;
import simplenlg.lexicon.lexicalitems.Noun;
import simplenlg.lexicon.lexicalitems.Verb;
import simplenlg.lexicon.morph.VerbInflection;
import simplenlg.lexicon.morph.VerbInflectionPattern;

/**
 * This class converts a {@link simplenlg.lexicon.Lexicon} object
 * into an XML stream containing its lexical items, which can be 
 * saved to a file. The output XML stream is in a format readable
 * by the {@link simplenlg.lexicon.db.XMLHandler} class.
 * 
 * The XML stream always includes the DTD (Document Type Definition)
 * for the output format. The resulting XML file is well-formed.
 *
 * When saving a {@link simplenlg.lexicon.DBLexicon} object, make
 * sure to perform {@link simplenlg.lexicon.DBLexicon#loadData()}
 * before saving, otherwise only the already loaded lexical
 * items will be saved.
 * 
 * As the lexicon was not initially designed to be easily written
 * to an XML stream (or saved to a file in any form), this
 * class is, at the moment, basically one big hack.
 * 
 * @author Marcel Bollmann
 *
 */
public class XMLWriter43 {

	private Writer out;
	private Lexicon lexicon;
	
	/**
	 * Instantiates a new <code>XMLWriter</code>.
	 */
	public XMLWriter43() {
		this.out = null;
		this.lexicon = null;
	}
	
	/**
	 * Instantiates a new <code>XMLWriter</code> which reads data
	 * from a given lexicon.
	 * 
	 * @param lex
	 * 			The lexicon to be converted into an XML stream
	 */
	public XMLWriter43(Lexicon lex) {
		this();
		this.lexicon = lex;
	}
	
	/**
	 * Returns the input lexicon.
	 * 
	 * @return
	 * 			A {@link simplenlg.lexicon.Lexicon} object
	 */
	public Lexicon getLexicon() {
		return lexicon;
	}

	/**
	 * Sets the input lexicon.
	 * 
	 * @param lexicon
	 * 			The lexicon to be converted into an XML stream
	 */
	public void setLexicon(Lexicon lexicon) {
		this.lexicon = lexicon;
	}
	
	/**
	 * Returns the {@link java.io.Writer} object to which the XML
	 * stream will be written.
	 * 
	 * @return
	 * 			A {@link java.io.Writer} object
	 */
	public Writer getWriter() {
		return out;
	}

	/**
	 * Sets the {@link java.io.Writer} to which the XML stream
	 * will be written.
	 * 
	 * @param out
	 * 			The {@link java.io.Writer} object
	 */
	public void setWriter(Writer out) {
		this.out = out;
	}
	
	/**
	 * Generates the XML stream from the input lexicon specified via
	 * {@link #setLexicon(Lexicon)} and saves it into a file. 
	 * This overrides any output writer specified via
	 * {@link #setWriter(Writer)}.
	 * 
	 * If a lexicon has not been set, this method does nothing.
	 * 
	 * @param filename
	 * 			The full path of the output file
	 * @throws IOException
	 */
	public void saveToFile(String filename) throws IOException {
		// TODO check filename
	    this.out = new BufferedWriter(new OutputStreamWriter(
	        new FileOutputStream(filename), "UTF8"));
	    try {
	    	this.save();
	    }
	    finally {
	    	out.close();
	    }
	}
	
	/**
	 * Generates the XML stream from the input lexicon specified via
	 * {@link #setLexicon(Lexicon)} and saves it into a file. 
	 * This overrides any output writer specified via
	 * {@link #setWriter(Writer)}.
	 * 
	 * If a lexicon has not been set, this method does nothing.
	 * 
	 * @param file
	 * 			The output file
	 * @throws IOException
	 */
	public void saveToFile(File file) throws IOException {
		this.saveToFile(file.getAbsolutePath());
	}
	
	/**
	 * Generates the XML stream from the input lexicon, specified via
	 * {@link #setLexicon(Lexicon)}, and writes it to the output stream,
	 * specified via {@link #setWriter(Writer)}.
	 * 
	 * If either lexicon or output stream has not been set, the method
	 * does nothing.
	 * 
	 * @throws IOException
	 */
	public void save() throws IOException {
		if (out == null || lexicon == null) return;
		
		out.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>\n");
		//this.writeDTD();
		this.writeLexicon();
	}
	
	
	private void writeLexicon() throws IOException {
		out.write("<lexicon>\n");
		this.writeNouns();
		this.writeAdjectives();
		this.writeVerbs();
		out.write("</lexicon>\n");
	}
	
	private void writeNouns() throws IOException {
		//out.write("\t<Nouns>\n");
		for (LexicalItem l : lexicon.getItems(Category.NOUN)) {
			this.writeNoun((Noun) l);
		}
		//out.write("\t</Nouns>\n");
	}
	
	private void writeAdjectives() throws IOException {
		//out.write("\t<Adjectives>\n");
		for (LexicalItem l : lexicon.getItems(Category.ADJECTIVE)) {
			this.writeAdjective((Adjective) l);
		}
		//out.write("\t</Adjectives>\n");
	}
	
	private void writeVerbs() throws IOException {
		//out.write("\t<Verbs>\n");
		for (LexicalItem l : lexicon.getItems(Category.VERB)) {
			if (!Constants.isConstantVerb((Verb) l))
				this.writeVerb((Verb) l);
		}
		//out.write("\t</Verbs>\n");
	}
	
	
	
	private void writeNoun(Noun n) throws IOException {
		out.write("<word>\n");
		out.write("  <base>");
		out.write(n.getBaseForm());
		out.write("</base>\n");
		out.write("  <category>noun</category>\n");
		out.write("  <id>");
		out.write(n.getID());
		out.write("</id>\n");
		
		out.write("  <gender>");
		out.write(getGenderString(n.getGender()));
		out.write("</gender>\n");
		if (n.isFixedPluralNoun()) {
			out.write("  <fixedplural/>\n");
		}
		if (n.isAdjectiveInflection()) {
			out.write("  <adjectiveinflection/>\n");
		}
		
		if (n.isFixedPluralNoun() || n.isAdjectiveInflection()) {
			// don't write anything
		} else if (n.isRegular()) {
			out.write("  <infsuffix>");
			out.write(n.getSuffix(Case.GENITIVE, NumberAgr.SINGULAR));
			out.write("::");
			out.write(n.getSuffix(Case.NOMINATIVE, NumberAgr.PLURAL));
			out.write("</infsuffix>\n");
		} else {
			out.write("  <inflectionnotimplemented/>\n");
			//this.writeNounSuffix(n, Case.ACCUSATIVE, NumberAgr.SINGULAR);
			//this.writeNounSuffix(n, Case.GENITIVE, NumberAgr.SINGULAR);
			//this.writeNounSuffix(n, Case.DATIVE, NumberAgr.SINGULAR);
			//this.writeNounSuffix(n, Case.NOMINATIVE, NumberAgr.PLURAL);
			//this.writeNounSuffix(n, Case.DATIVE, NumberAgr.PLURAL);
		}
		
		out.write("</word>\n");
	}
	
	private void writeNounSuffix(Noun n, Case c, NumberAgr num) throws IOException {
		String suffix = n.getSuffix(c, num);
		boolean form  = false;
		if (!suffix.isEmpty() && Character.isUpperCase(suffix.charAt(0))) form = true;
		
		out.write(form ? "\t\t\t<form " : "\t\t\t<suffix ");
		out.write("case=\"");
		out.write(getCaseString(c));
		out.write("\" num=\"");
		out.write(getNumberString(num));
		out.write("\">");
		out.write(suffix);
		out.write(form ? "</form>\n" : "</suffix>\n");
	}
	
	private void writeAdjective(Adjective a) throws IOException {
		out.write("<word>\n");
		out.write("  <base>");
		out.write(a.getBaseForm());
		out.write("</base>\n");
		out.write("  <category>adjective</category>\n");
		out.write("  <id>");
		out.write(a.getID());
		out.write("</id>\n");

		if (a.isElideFinalE()) {
			out.write("  <elision/>\n");
		}
		
		out.write("  <comp>");
		out.write(a.getComparative());
		out.write("</comp>\n");
		
		out.write("  <super>");
		out.write(a.getSuperlative());
		out.write("</super>\n");
		
		out.write("</word>\n");
	}
	
	private void writeVerb(Verb v) throws IOException {
		out.write("<word>\n");
		out.write("  <base>");
		out.write(v.getBaseForm());
		out.write("</base>\n");
		out.write("  <category>verb</category>\n");
		out.write("  <id>");
		out.write(v.getID());
		out.write("</id>\n");

		if (v.getAuxType().equals(AuxType.SEIN)) {
			out.write("  <aux>sein</aux>\n");
		}
		VerbInflectionPattern pattern = v.getPattern();
		if (!pattern.equals(Constants.VERB_INFLECTION_DEFAULT)) {
			out.write("  <infpattern>");
			out.write(pattern.getName());
			out.write("</infpattern>\n");
		}
		
		// optional values
		String stem = VerbInflection.detectStem(v.getInfinitiveStem());
		String past = v.getPastStem();
		String part = v.getPerfectParticiple();
		String subj = v.getSubjunctiveStem();
		
		if (!past.equals(stem.concat("te")) || 
				!part.equals("ge".concat(stem).concat("t"))) {
			out.write("  <paststem>");
			out.write(past);
			out.write("</paststem>\n");
			out.write("  <participle>");
			out.write(part);
			out.write("</participle>\n");
		}
		if (v.hasAlternatePresentStem()) {
			out.write("  <alt-sg>");
			out.write(v.getAlternatePresentStem());
			out.write("</alt-sg>\n");
		}		
		if (!subj.equals(past)) {
			out.write("  <subj2>");
			out.write(v.getSubjunctiveStem());
			out.write("</subj2>\n");
		}
		
		out.write("</word>\n");	
	}
	
	
	
	private void writeDTD() throws IOException {
		out.write("<!DOCTYPE Lexicon [\n");
		out.write("<!ELEMENT Lexicon (Nouns?, Adjectives?, Verbs?)>\n\n");
		out.write("<!ELEMENT Nouns (noun)*>\n");
		out.write("<!ELEMENT noun (stem, (suffix|form), (suffix|form)+)>\n");
		out.write("<!ATTLIST noun\n");
		out.write("  id     ID                          #REQUIRED\n");
		out.write("  gender (masculine|feminine|neuter) \"neuter\"\n");
		out.write("  fixedplural (true|false) \"false\"\n");
		out.write("  adjectiveflexion (true|false) \"false\"\n");
		out.write(">\n\n");
		out.write("<!ELEMENT Adjectives (adjective)*>\n");
		out.write("<!ELEMENT adjective (stem, comp, super)>\n");
		out.write("<!ATTLIST adjective\n");
		out.write("  id       ID            #REQUIRED\n");
		out.write("  elision  (true|false)  \"false\"\n");
		out.write(">\n\n");
		out.write("<!ELEMENT Verbs (verb)*>\n");
		out.write("<!ELEMENT verb (stem, past?, part?, alt-sg?, subj2?)>\n");
		out.write("<!ATTLIST verb\n");
		out.write("  id	      ID            #REQUIRED\n");
		out.write("  aux	      (haben|sein)  \"haben\"\n");
		out.write("  inflection  CDATA         #IMPLIED\n");
		out.write(">\n\n");
		out.write("<!ELEMENT stem   (#PCDATA)>\n");
		out.write("<!ELEMENT comp   (#PCDATA)>\n");
		out.write("<!ELEMENT super  (#PCDATA)>\n");
		out.write("<!ELEMENT past   (#PCDATA)>\n");
		out.write("<!ELEMENT part   (#PCDATA)>\n");
		out.write("<!ELEMENT alt-sg (#PCDATA)>\n");
		out.write("<!ELEMENT subj2  (#PCDATA)>\n");
		out.write("<!ELEMENT suffix (#PCDATA)>\n");
		out.write("<!ATTLIST suffix\n");
		out.write("  case  (nom|acc|gen|dat)  #REQUIRED\n");
		out.write("  num   (sg|pl)            #REQUIRED\n");
		out.write(">\n\n");
		out.write("<!ELEMENT form   (#PCDATA)>\n");
		out.write("<!ATTLIST form\n");
		out.write("  case  (nom|acc|gen|dat)  #REQUIRED\n");
		out.write("  num   (sg|pl)            #REQUIRED\n");
		out.write(">\n\n");
		out.write("]>\n\n");
	}
	
	public static String getGenderString(Gender g) {
		switch (g) {
		case MASCULINE:
			return "masculine";
		case FEMININE:
			return "feminine";
		case NEUTER:
		default:
			return "neuter";
		}
	}
	
	private static String getCaseString(Case c) {
		switch (c) {
		case NOMINATIVE:
		default:
			return "nom";
		case ACCUSATIVE:
			return "acc";
		case GENITIVE:
			return "gen";
		case DATIVE:
			return "dat";
		}
	}
	
	private static String getNumberString(NumberAgr num) {
		switch (num) {
		case SINGULAR:
		default:
			return "sg";
		case PLURAL:
			return "pl";
		}
	}
	
}
