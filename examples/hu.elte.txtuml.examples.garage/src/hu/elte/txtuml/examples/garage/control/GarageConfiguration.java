package hu.elte.txtuml.examples.garage.control;


import hu.elte.txtuml.api.platform.Configuration;
import hu.elte.txtuml.api.platform.Group;
import hu.elte.txtuml.api.platform.Multithreading;

import hu.elte.txtuml.examples.garage.control.model.Alarm;
import hu.elte.txtuml.examples.garage.control.model.Door;
import hu.elte.txtuml.examples.garage.control.model.Keyboard;
import hu.elte.txtuml.examples.garage.control.model.Motor;


@Group(contains = {Door.class, Motor.class})
@Group(contains = {Keyboard.class})
@Group(contains  = {Alarm.class} )
@Multithreading(false)
public class GarageConfiguration extends Configuration{

}
