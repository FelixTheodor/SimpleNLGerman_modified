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
package simplenlg.aggregation;

import java.util.ArrayList;
import java.util.List;

import simplenlg.realiser.CoordinateSPhraseSpec;
import simplenlg.realiser.SPhraseSpec;

/**
 * This class implements a generic aggregation procedure for clauses. The idea
 * is to supply it in advance with an arbitrary number of
 * {@link AggregationRule}s. These are then applied in sequence to the
 * parameters supplied on invocation of {@link #apply(SPhraseSpec...)}. The
 * order of application is the order in which rules are supplied.
 * 
 * <P>
 * Note that rules can interact, in the sense that the application of one rule
 * can stop another rule from applying.
 * </P>
 * 
 * @see #addRule(AggregationRule)
 * 
 * @author agatt
 * 
 */
public class ClauseAggregator {
	private List<AggregationRule> _rules;

	/**
	 * Factory method to facilitate creation of a ClauseAggregator. The returned
	 * instance wraps a {@link ForwardConjunctionReduction} rule, a
	 * {@link BackwardConjunctionReduction} rule, and a
	 * {@link ClauseCoordination} rule, in that order.
	 * 
	 * @return the aggregator
	 */
	public static ClauseAggregator newInstance() {
		ClauseAggregator aggregator = new ClauseAggregator();
		aggregator.addRule(new ForwardConjunctionReduction());
		aggregator.addRule(new BackwardConjunctionReduction());
		aggregator.addRule(new ClauseCoordination());
		return aggregator;
	}

	/**
	 * Constructs a ClauseAggregator
	 */
	public ClauseAggregator() {
		this._rules = new ArrayList<AggregationRule>();
	}

	/**
	 * Add an aggregation rule to this aggregator: rules are applied in the
	 * order in which they are added.
	 * 
	 * @param rule
	 *            the ellipsis rule
	 */
	public void addRule(AggregationRule rule) {
		this._rules.add(rule);
	}

	/**
	 * 
	 * @return the rules supplied to this aggregator
	 */
	public List<AggregationRule> getRules() {
		return this._rules;
	}

	/**
	 * Apply aggregation and ellipsis to an arbitrary number of sentences. This
	 * method will test each rule in the order given, applying it to the
	 * parameters or the result of the last successful rule.
	 * 
	 * @param sentences
	 *            the sentences to aggregate
	 * @return the result of aggregation
	 */
	public SPhraseSpec apply(SPhraseSpec... sentences) {
		SPhraseSpec result = null;

		if (sentences.length > 1 && !this._rules.isEmpty()) {

			for (AggregationRule rule : this._rules) {
				if (result == null) {
					result = rule.apply(sentences);

				} else if (result instanceof CoordinateSPhraseSpec) {
					SPhraseSpec intermediateResult = rule
							.apply((CoordinateSPhraseSpec) result);

					if (intermediateResult != null
							&& intermediateResult instanceof CoordinateSPhraseSpec) {
						result = intermediateResult;
					}
				}
			}
		}

		return result;
	}

	public SPhraseSpec apply(List<SPhraseSpec> sentences) {
		return apply(sentences.toArray(new SPhraseSpec[sentences.size()]));
	}

}
