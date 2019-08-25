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

import java.io.File;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import simplenlg.features.Category;
import simplenlg.lexicon.DatabaseBackedLexicon;

/**
 * An accessor for lexical databases in XML format, implementing the
 * {@link simplenlg.lexicon.db.DBAccessor} interface.
 *  
 * @author Marcel Bollmann
 *
 */
public class XMLAccessor implements DBAccessor {

	// the full name (including path) of the XML file
	private String xmlfilename;
	
	private DatabaseBackedLexicon lexicon;
	
	private XMLReader parser;
	
	/**
	 * Instantiates a new <code>XMLAccessor</code>.
	 */
	public XMLAccessor() {
		super();
		this.xmlfilename = null;
		this.lexicon = null;
		
		try {
			this.parser = XMLReaderFactory.createXMLReader();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Instantiates a new <code>XMLAccessor</code> which reads data from
	 * a given XML file.
	 * 
	 * @param filename
	 * 			The full path of the XML file to read from
	 */
	public XMLAccessor(String filename) {
		this();
		this.xmlfilename = filename;
	}
	
	/**
	 * Instantiates a new <code>XMLAccessor</code> which reads data from
	 * a given XML file.
	 * 
	 * @param filename
	 * 			The file object of the XML file to read from
	 */
	public XMLAccessor(File file) {
		this();
		this.xmlfilename = file.getAbsolutePath();
	}
	
	
	@Override
	public void setLexicon(DatabaseBackedLexicon lex) {
		this.lexicon = lex;
	}

	@Override
	public boolean hasLexicon() {
		return this.lexicon != null;
	}

	@Override
	public void loadData() throws Exception {
		XMLHandler handler = new XMLHandler();
		handler.setLexicon(this.lexicon);
		parser.setContentHandler(handler);
		parser.parse(xmlfilename);
	}

	@Override
	public void loadData(Category... categories) throws Exception {
		for (Category cat : categories) {
			XMLHandler handler = new XMLHandler();
			handler.setCat(cat);
			handler.setLexicon(this.lexicon);
			parser.setContentHandler(handler);
			parser.parse(xmlfilename);
		}
	}

	@Override
	public void loadItem(Category cat, String baseform) throws Exception {
		XMLHandler handler = new XMLHandler(cat, baseform);
		handler.setLexicon(this.lexicon);
		parser.setContentHandler(handler);
		parser.parse(xmlfilename);
	}

	@Override
	public void loadItemByID(String id) throws Exception {
		XMLHandler handler = new XMLHandler();
		handler.setId(id);
		handler.setLexicon(this.lexicon);
		parser.setContentHandler(handler);
		parser.parse(xmlfilename);
	}

	@Override
	public void loadItemsByBaseform(String baseform) throws Exception {
		XMLHandler handler = new XMLHandler(baseform);
		handler.setLexicon(this.lexicon);
		parser.setContentHandler(handler);
		parser.parse(xmlfilename);
	}

	@Override
	public void connect() throws Exception {
		// TODO Auto-generated method stub
		// check if files exist / check well-formedness
		
		// check well-formedness
		// this.parser.setFeature("http://xml.org/sax/features/validation", true);
		// parser.parse(xmlfile);
		// this.parser.setFeature("http://xml.org/sax/features/validation", false);
		
	}

}
