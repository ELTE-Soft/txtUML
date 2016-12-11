package nuclearpower;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.external.ExternalClass;

public interface GUIInterface extends ExternalClass{
	void consumerStateChanged(String newState);
	void panelStateChanged(String newState);
	void weatherChanged(String newWeather);
	void powerStationStateChanged(String newState);
	void batteryStateChanged(String newState);
	void batteryCapacityChanged(int capacity);
	
	void setSolarPanel(ModelClass modelClass);
	void setConsumer(ModelClass modelClass);
}


