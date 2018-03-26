package hu.elte.txtuml.api.model.seqdiag;

import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

/**
 * Combined fragment types.
 * <p>
 * Currently only used by plantUML exporter.
 */
@SequenceDiagramRelated
public enum CombinedFragmentType {
	SEQ, STRICT, ALT, LOOP, OPT
}
