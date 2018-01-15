/**
 * Sequence diagram execution implementation package. These diagrams can be used
 * to test the models validity or in other terms to create test cases for the
 * models.
 * 
 * <h2>Overview</h2>
 * 
 * The executor uses two kinds of threads:
 * <ul>
 * <li>Exactly one model executor thread. This executes model code, processing
 * all asynchronous events one by one.</li>
 * <li>One or more interaction threads, strictly structured in a tree with the
 * root being linked to the model executor thread. These execute sequence
 * diagram code, one interaction each.</li>
 * </ul>
 * 
 * To ensure model and memory consistency, the following invariants have to be
 * kept:
 * 
 * <ul>
 * <li>At any given time, at most one interaction thread may execute user
 * code.</li>
 * <li>The model executor thread may only execute user code if non of the
 * interaction threads do.</li>
 * <li>The execution order of every line of user-written code has to be
 * deterministic.</li>
 * </ul>
 */
@SequenceDiagramRelated
package hu.elte.txtuml.api.model.execution.impl.seqdiag;

import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;
