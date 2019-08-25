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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import simplenlg.features.Agreement;
import simplenlg.features.AuxType;
import simplenlg.features.Category;
import simplenlg.features.Gender;
import simplenlg.lexicon.DatabaseBackedLexicon;
import simplenlg.lexicon.lexicalitems.Adjective;
import simplenlg.lexicon.lexicalitems.Constants;
import simplenlg.lexicon.lexicalitems.Noun;
import simplenlg.lexicon.lexicalitems.Verb;
import simplenlg.lexicon.morph.VerbInflectionPattern;

/**
 * This class provides an XML event handler for SAX, extending the 
 * {@link org.xml.sax.helpers.DefaultHandler} class, intended to parse
 * lexical data for SimpleNLG for German.
 * 
 * It constructs a {@link simplenlg.lexicon.lexicalitems.LexicalItem}
 * for each parsed lexical item which matches the search criteria;
 * possible criteria are lexical category, baseform/stem and/or ID.
 * The {@link simplenlg.lexicon.lexicalitems.LexicalItem} is then added
 * to a {@link simplenlg.lexicon.DatabaseBackedLexicon}. When no search 
 * criteria are given, all parsed items are added to the lexicon. 
 * 
 * @author Marcel Bollmann
 *
 */
public class XMLHandler extends DefaultHandler {
	
	// the lexicon to which the items should be added
	private DatabaseBackedLexicon lexicon;

	// search criteria for the lexical item we are looking for
	private Category cat;
	private String catString;
	private String id;
	private String baseform;
	
	// buffer to construct element data
	private StringBuffer buffer;
	
	private String currentCase;
	private String currentNumber;
	
	// true while inside a lexical item which meets our criteria 
	private boolean isRelevantItem;
	
	// temporary fields that store lexical item information
	private String thisId;
	private String stem;
	private String comp;
	private String sup;
	private String past;
	private String part;
	private String altSg;
	private String subj2;
	private String[][] suffixList;
	private String inflection;
	private Gender gender;
	private boolean adjElision;
	private AuxType aux;
	private boolean nounPluraleTantum;
	private boolean nounAdjectiveFlexion;
	
	/**
	 * Instantiates a new <code>XMLHandler</code>. If no further options are set,
	 * the handler will return all lexical items in the input data.
	 */
	XMLHandler() {
		this.cat = null;
		this.catString = null;
		this.id = null;
		this.baseform = null;
		this.isRelevantItem = false;
		this.currentCase = null;
		this.currentNumber = null;
		this.resetAllFields();
	}
	
	/**
	 * Instantiates a new <code>XMLHandler</code> which returns all lexical items
	 * with a given baseform/stem.
	 * 
	 * @param baseform
	 * 			The baseform 
	 */
	XMLHandler(String baseform) {
		this();
		this.baseform = baseform;
	}
	
	/**
	 * Instantiates a new <code>XMLHandler</code> which returns all lexical items
	 * of a given category and baseform/stem.
	 * 
	 * @param cat
	 * 			The category
	 * @param baseform
	 * 			The baseform
	 */
	XMLHandler(Category cat, String baseform) {
		this(baseform);
		this.setCat(cat);
	}
	
	/**
	 * Sets the lexicon to which the matching lexical items will be added.
	 * 
	 * @param lexicon
	 * 			A lexicon object
	 */
	public void setLexicon(DatabaseBackedLexicon lexicon) {
		this.lexicon = lexicon;
	}

	/**
	 * Returns the lexicon to which the matching lexical items will be added.
	 */
	public DatabaseBackedLexicon getLexicon() {
		return lexicon;
	}

	/**
	 * Returns the category of the lexical item(s) to search for.
	 */
	public Category getCat() {
		return cat;
	}

	/**
	 * Sets a lexical category as a search criterion.
	 *  
	 * @param cat
	 * 			The lexical category of the item(s) to search for
	 */
	public void setCat(Category cat) {
		this.cat = cat;
		this.catString = cat.toString();
	}

	/**
	 * Returns the ID of the lexical item to search for.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets an ID as a search criterion.
	 * 
	 * @param id
	 * 			The ID of the item to search for
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the baseform of the lexical item(s) to search for.
	 */
	public String getBaseform() {
		return baseform;
	}

	/**
	 * Sets a baseform/stem as a search criterion.
	 * 
	 * @param baseform
	 * 			The baseform of the item(s) to search for
	 */
	public void setBaseform(String baseform) {
		this.baseform = baseform;
	}

	@Override
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
		if (this.isRelevantItem && buffer != null) {
			buffer.append(arg0, arg1, arg2);
	    }
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {

		// New lexical item
		if (qName.equals("noun") || qName.equals("adjective")
				|| qName.equals("verb")) {
			if (this.lookingFor(qName)) {
				this.isRelevantItem = true;
				this.resetAllFields();
				this.readAttributes(atts);
				
				// if searching by ID, we can already check here for a match
				if (this.id != null && !(this.id.equals(this.thisId))) {
					this.isRelevantItem = false;
				}
			} else {
				this.isRelevantItem = false;
			}
		}
		else {
			this.buffer = new StringBuffer();
			if (qName.equals("suffix") || qName.equals("form")) {
				this.currentCase = atts.getValue("case");
				this.currentNumber = atts.getValue("num");
			}
		}
		
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		if (!this.isRelevantItem) return;
		
		if (qName.equals("stem"))         this.stem = this.buffer.toString();
		else if (qName.equals("comp"))    this.comp = this.buffer.toString();
		else if (qName.equals("super"))    this.sup = this.buffer.toString();
		else if (qName.equals("past"))    this.past = this.buffer.toString();
		else if (qName.equals("part"))    this.part = this.buffer.toString();
		else if (qName.equals("alt-sg")) this.altSg = this.buffer.toString();
		else if (qName.equals("subj2"))  this.subj2 = this.buffer.toString();
		else if (qName.equals("suffix") || qName.equals("form")) {
			this.setSuffix(this.currentCase, this.currentNumber, this.buffer.toString());
		}
		else if (qName.equals("noun") && this.isMatch()) {
			if (this.lexicon != null)
				this.lexicon.addItem(this.buildNoun());
		}
		else if (qName.equals("adjective") && this.isMatch()) {
			if (this.lexicon != null)
				this.lexicon.addItem(this.buildAdjective());
		}
		else if (qName.equals("verb") && this.isMatch()) {
			if (this.lexicon != null)
				this.lexicon.addItem(this.buildVerb());			
		}
		
	}


	/**
	 * Checks if our search criteria include items of a given category.
	 * 
	 * @param cat
	 * 			A lexical category 
	 * @return
	 * 			True if the category matches our search criteria
	 */
	private boolean lookingFor(String cat) {
		if (this.cat == null || this.cat == Category.ANY) return true;
		else return cat.equalsIgnoreCase(this.catString);
	}
	
	/**
	 * Checks if the temporarily stored parsed input data matches our
	 * search criteria.
	 *  
	 * @return
	 * 			True if the data match our search criteria
	 */ 
	private boolean isMatch() {
		if (this.id != null) {
			if (!this.id.equals(this.thisId)) return false;
		}
		if (this.baseform != null) {
			if (!this.baseform.equals(this.stem)) return false;
		}
		return true;
	}
	
	/**
	 * Reads all attributes of a lexical item tag into the appropriate,
	 * temporary fields.
	 * 
	 * @param atts
	 * 			The attributes of a lexical item tag
	 */
	private void readAttributes(Attributes atts) {
		
		for (int i=0; i<atts.getLength(); i++) {
			String attr = atts.getLocalName(i);
			String value = atts.getValue(i);
			// TODO: ID is currently discarded!
			//       Somehow causes trouble with lexicon-generated IDs,
			//       as the same ID can apparently can be generated twice
			//if (attr.equals("id")) this.thisId = value;
			this.thisId = null;
			//
			if (attr.equals("elision")) {
				if (value.equalsIgnoreCase("true")) {
					this.adjElision = true;
				} else {
					this.adjElision = false;
				}
			}
			else if (attr.equals("aux")) {
				if (value.equalsIgnoreCase("sein")) {
					this.aux = AuxType.SEIN;
				} else if (value.equalsIgnoreCase("haben")) {
					this.aux = AuxType.HABEN;
				}
			}
			else if (attr.equals("inflection")) this.inflection = value;
			else if (attr.equals("gender")) {
				if (value.equals("feminine")) this.gender = Gender.FEMININE;
				else if (value.equals("masculine")) this.gender = Gender.MASCULINE;
				else this.gender = Gender.NEUTER;
			}
			else if (attr.equals("fixedplural")) {
				if (value.equals("true")) this.nounPluraleTantum = true;
			}
			else if (attr.equals("adjectiveflexion")) {
				if (value.equals("true")) this.nounAdjectiveFlexion = true;
			}
		}
		
	}
	
	/**
	 * Resets all temporary fields which store lexical item data.
	 */
	private void resetAllFields() {
		this.thisId = null;
		this.stem = null;
		this.gender = null;
		this.adjElision = false;
		this.aux = null;
		this.inflection = null;
		this.comp = null;
		this.sup = null;
		this.past = "";
		this.part = "";
		this.altSg = "";
		this.subj2 = "";
		this.suffixList = new String[4][2];
		this.nounPluraleTantum = false;
		this.nounAdjectiveFlexion = false;
	}
	
	/**
	 * Sets the suffix value for a given case/number combination.
	 *  
	 * @param cas
	 * 			The case of the suffix
	 * @param num
	 * 			The number of the suffix
	 * @param suffix
	 * 			The suffix
	 */
	private void setSuffix(String cas, String num, String suffix) {
		int arg0 = getCaseArrayIndex(cas);
		int arg1 = getNumberArrayIndex(num);
		this.suffixList[arg0][arg1] = suffix;
	}
	
	/**
	 * Gets the suffix value for a given case/number combination.
	 * 
	 * @param cas
	 * 			The case of the suffix
	 * @param num
	 * 			The number of the suffix
	 * @return
	 * 			The suffix
	 */
	private String getSuffix(String cas, String num) {
		int arg0 = getCaseArrayIndex(cas);
		int arg1 = getNumberArrayIndex(num);
		return this.suffixList[arg0][arg1];
	}

	private static int getCaseArrayIndex(String cas) {
		if (cas.equalsIgnoreCase("nom"))      return 0;
		else if (cas.equalsIgnoreCase("acc")) return 1;
		else if (cas.equalsIgnoreCase("dat")) return 2;
		else if (cas.equalsIgnoreCase("gen")) return 3;
		else return -1;
	}
	
	private static int getNumberArrayIndex(String num) {
		if (num.equalsIgnoreCase("sg"))       return 0;
		else if (num.equalsIgnoreCase("pl"))  return 1;
		else return -1;
	}
	
	/**
	 * Builds a {@link simplenlg.lexicon.lexicalitems.Noun} from the
	 * parsed lexical data.
	 * 
	 * @return
	 * 			A {@link simplenlg.lexicon.lexicalitems.Noun} object
	 */
	private Noun buildNoun() {
		Noun noun;

		if (this.nounAdjectiveFlexion || this.nounPluraleTantum) {
			noun = new Noun(this.stem, this.gender, "", "");
		}
		else if (this.getSuffix("acc", "sg") == null ||
				this.getSuffix("dat", "sg") == null ||
				this.getSuffix("dat", "pl") == null) {
			noun = new Noun(this.stem, this.gender, this.getSuffix("gen", "sg"), this.getSuffix("nom", "pl"));
		} else {
			noun = new Noun(this.stem, this.gender, this.getSuffix("acc", "sg"), this.getSuffix("gen", "sg"),
					this.getSuffix("dat", "sg"), this.getSuffix("nom", "pl"), this.getSuffix("dat", "pl"));
		}
		
		if (this.nounPluraleTantum) noun.setAgreement(Agreement.FIXED_PLUR);
		if (this.nounAdjectiveFlexion) noun.setAdjectiveInflection(true);
		noun.setID(this.thisId);
		return noun;
	}
	
	/**
	 * Builds a {@link simplenlg.lexicon.lexicalitems.Adjective} from the
	 * parsed lexical data.
	 * 
	 * @return
	 * 			A {@link simplenlg.lexicon.lexicalitems.Adjective} object
	 */
	private Adjective buildAdjective() {
		Adjective adjective = new Adjective(this.stem, this.comp, this.sup, this.adjElision);
		adjective.setID(this.thisId);
		return adjective;
	}
	
	/**
	 * Builds a {@link simplenlg.lexicon.lexicalitems.Verb} from the
	 * parsed lexical data.
	 * 
	 * @return
	 * 			A {@link simplenlg.lexicon.lexicalitems.Verb} object
	 */
	private Verb buildVerb() {
		Verb verb;
		
		// determine inflection pattern, if any
		VerbInflectionPattern pattern = Constants.VERB_INFLECTION_DEFAULT;
		if (this.inflection != null) {
			pattern = Constants.getInflectionPatternByName(this.inflection);
		}
		
		if (this.past.isEmpty() || this.part.isEmpty()) {
			verb = new Verb(this.stem, pattern);
			verb.setAuxType(this.aux);
		}
		else {
			verb = new Verb(this.stem, this.past, this.part, this.altSg, this.subj2, this.aux, pattern);
		}
		verb.setID(this.thisId);
		
		return verb;
	}
	
	
}
