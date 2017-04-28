package micro.main;

import hu.elte.txtuml.api.model.external.ExternalClass;

public interface GUIInterface extends ExternalClass {
	public void lampOn();
	public void lampOff();
	public void magnetronOn();
	public void magnetronOff();
}
