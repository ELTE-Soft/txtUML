package hu.elte.txtuml.diagnostics.animation;

import java.util.Map;
import java.util.TreeMap;

import org.eclipse.papyrus.infra.gmfdiag.css.service.IMarkerToPseudoSelectorMappingProvider;

/**
 * The mapping from marker kind to the selector in CSS file
 * @author gerazo
 */
public class AnimationMarkerToPseudoSelectorMappingProvider implements IMarkerToPseudoSelectorMappingProvider {
	private Map<String, String> mappings;
	
	public AnimationMarkerToPseudoSelectorMappingProvider() {}
	
	@Override
	public Map<String, String> getMappings() {
		if (mappings == null) {
			mappings = new TreeMap<String, String>();
			mappings.put(AnimationConfig.TXTUML_ANIMATION_MARKER_ID, AnimationConfig.TXTUML_ANIMATION_PSEUDO_SELECTOR);
		}
		return mappings;
	}

}
