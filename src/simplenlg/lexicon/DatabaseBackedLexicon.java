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
package simplenlg.lexicon;

import simplenlg.exception.LexiconException;
import simplenlg.lexicon.db.DBAccessor;

/**
 * Extension of the {@link LexiconInterface} for lexicons that are backed by a
 * database of lexical information, in whatever format. A
 * <code>DatabaseBackedLexicon</code> is supplied with a
 * {@link simplenlg.lexicon.db.DBAccessor} to which it delegates the task of
 * loading lexical information from the database, and instantiating lexical
 * items which are then stored in the lexicon itself.
 * 
 * @author agatt
 * 
 */
public interface DatabaseBackedLexicon extends LexiconInterface {

	/**
	 * Set the <code>DBAccessor</code> that provides the link between this
	 * lexicon and the lexical database.
	 * 
	 * @param accessor
	 *            The accessor
	 */
	void setAccessor(DBAccessor accessor);

	/**
	 * 
	 * @return The <code>DBAccessor</code> if one has been set,
	 *         <code>null</code> otherwise.
	 */
	DBAccessor getAccessor();

	/**
	 * 
	 * @return <code>true</code> if a DBAccessor has been set.
	 */
	boolean hasAccessor();

	/**
	 * Loads all the data in the database, via a call to
	 * {@link simplenlg.lexicon.db.DBAccessor#loadData()}.
	 * 
	 * @throws LexiconException
	 *             In case any exceptions are thrown by
	 *             <code>DBAccessor.loadData()</code>, they should be wrapped in
	 *             a <code>LexiconException</code>.
	 */
	void loadData() throws LexiconException;

	/**
	 * Load a single item from the database, given its unique identifier.
	 * 
	 * <P>
	 * This method should involve an invocation of the
	 * {@link simplenlg.lexicon.db.DBAccessor#loadItemByID(String)} method in
	 * the lexicon's <code>DBAccessor</code>.
	 * 
	 * @param id
	 *            The identifier of the lexical item
	 * @throws LexiconException
	 *             In case any exceptions are thrown by <code>DBAccessor</code>,
	 *             they should be wrapped in a <code>LexiconException</code>.
	 */
	void loadItemByID(String id) throws LexiconException;

	/**
	 * Load the items in the database with the given database.
	 * 
	 * <P>
	 * This method should involve an invocation of the
	 * {@link simplenlg.lexicon.db.DBAccessor#loadItemByID(String)} method in
	 * the lexicon's <code>DBAccessor</code>.
	 * 
	 * @param baseform
	 *            The baseform
	 * 
	 * @throws LexiconException
	 *             In case any exceptions are thrown by
	 *             <code>DBAccessor.loadData()</code>, they should be wrapped in
	 *             a <code>LexiconException</code>.
	 * 
	 */
	void loadItemsByBaseform(String baseform) throws LexiconException;

}
