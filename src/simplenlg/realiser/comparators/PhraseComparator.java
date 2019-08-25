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

package simplenlg.realiser.comparators;

import java.util.Comparator;
import java.util.List;

import simplenlg.features.Category;
import simplenlg.features.DiscourseFunction;
import simplenlg.lexicon.lexicalitems.LexicalItem;
import simplenlg.realiser.HeadedPhraseSpec;
import simplenlg.realiser.Phrase;

/**
 * This class implements a comparator for phrases. These comparators are used to
 * determine the order of pre- and post modifiers, as well as complements.
 * Comparison is done by specifying an ordering of phrases by category, by
 * function, and by baseform. In comparing two phrases, these lists are
 * consulted in the following order until a non-zero comparison value is
 * obtained: (a) category; (b) function; (c) baseform. In each case, if the
 * relevant item is not in the list, then a zero value is assumed.
 * 
 * @author agatt
 * 
 */
public class PhraseComparator implements Comparator<Phrase> {

	/**
	 * Convenience method, which creates a new instance of a phrase comparator
	 * using the predefined orderings in {@link OrderDefaults}.
	 * 
	 * @return the created <code>PhraseComparator</code>.
	 */
	public static PhraseComparator defaultInstance() {
		return new PhraseComparator(OrderDefaults.CATEGORY_ORDER,
				OrderDefaults.FUNCTION_ORDER, OrderDefaults.BASEFORM_ORDER);
	}

	protected List<Category> _catOrder;
	protected List<DiscourseFunction> _funcOrder;
	protected List<String> _baseOrder;

	/**
	 * Construct a new PhraseComparatior with the relevant lists.
	 * 
	 * @param catOrder
	 *            the order of phrases by category
	 * @param functionOrder
	 *            the order of phrases by function
	 * @param baseformOrder
	 *            the order of phrases by baseform
	 */
	public PhraseComparator(List<Category> catOrder,
			List<DiscourseFunction> functionOrder, List<String> baseformOrder) {
		setBaseformOrder(baseformOrder);
		setCategoryOrder(catOrder);
		setFunctionOrder(functionOrder);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Phrase p1, Phrase p2) {
		Category c1 = p1.getCategory();
		Category c2 = p2.getCategory();
		DiscourseFunction f1 = p1.getDiscourseFunction();
		DiscourseFunction f2 = p2.getDiscourseFunction();
		int comparison = 0;

		if (c1 != c2 && this._catOrder.contains(c1)
				&& this._catOrder.contains(c2)) {
			comparison = ((Integer) this._catOrder.indexOf(c1))
					.compareTo(this._catOrder.indexOf(c2));

		} else if (f1 != f2 && this._funcOrder.contains(f1)
				&& this._funcOrder.contains(f2)) {
			comparison = ((Integer) this._funcOrder.indexOf(f1))
					.compareTo(this._funcOrder.indexOf(f2));

		} else if (p1 instanceof HeadedPhraseSpec
				&& p2 instanceof HeadedPhraseSpec) {
			LexicalItem head1 = ((HeadedPhraseSpec<?>) p1).getHead();
			LexicalItem head2 = ((HeadedPhraseSpec<?>) p2).getHead();

			if (head1 != null && head2 != null) {
				String base1 = head1.getBaseForm();
				String base2 = head2.getBaseForm();

				if (this._baseOrder.contains(base1)
						&& this._baseOrder.contains(base2)) {
					comparison = ((Integer) this._baseOrder.indexOf(base1))
							.compareTo(this._baseOrder.indexOf(base2));
				}
			}

		}

		return comparison;
	}

	/**
	 * Set the order of phrases by category
	 * 
	 * @param categories
	 *            the category order
	 */
	public void setCategoryOrder(List<Category> categories) {
		this._catOrder = categories;
	}

	/**
	 * Get the list representing the ordering by category.
	 * 
	 * @return the category ordering
	 */
	public List<Category> getCategoryOrder() {
		return this._catOrder;
	}

	/**
	 * Get the list representing the ordering by function.
	 * 
	 * @return the function ordering
	 */
	public List<DiscourseFunction> getFunctionOrder() {
		return this._funcOrder;
	}

	/**
	 * Get the list representing the ordering by baseform.
	 * 
	 * @return the baseform ordering
	 */
	public List<String> getBaseformOrder() {
		return this._baseOrder;
	}

	/**
	 * Set the order of phrases by discourse function
	 * 
	 * @param functions
	 *            the function order
	 */
	public void setFunctionOrder(List<DiscourseFunction> functions) {
		this._funcOrder = functions;
	}

	/**
	 * Set the order of phrases by baseform.
	 * 
	 * @param forms
	 *            the baseform order
	 */
	public void setBaseformOrder(List<String> forms) {
		this._baseOrder = forms;
	}

}
