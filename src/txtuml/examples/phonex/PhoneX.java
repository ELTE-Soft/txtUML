package txtuml.examples.phonex;

import txtuml.api.*;

class PhoneX extends Model {
	class Service extends ModelClass {
		ModelInt serviceId;

		ModelInt getServiceId() {
			return serviceId;
		}
		void setServiceId(ModelInt newId) {
			serviceId = newId;
		}
	}
	
	class ServiceHandlesCall extends Association {
		@One Service handler;
		@Many Call handled;
	}

	class ServiceHandlesRegistrationProcess extends Association {
		@One Service handler;
		@Many RegistrationProcess handled;
	}

	class ServiceHandlesUnregistrationProcess extends Association {
		@One Service handler;
		@Many UnregistrationProcess handled;
	}

	class Call extends ModelClass {
		// TODO: RingingExpirationTimer
		class WaitingForCall extends State {
		}
		class VerifyingCaller extends State {
		}
		class VerifyingCallee extends State {
		}
		class CallInitiation extends State {
		}
		class InACall extends State {
		}
		class CallTermination extends State {
		}
		class WaitingForCallerReset extends State {
		}
		class WaitingForCalleeReset extends State {
		}
		class Terminated extends State {
		}
		class HackingAttempt extends State {
		}
		class UnknownNumber extends State {
		}
		class LineIsBusy extends State {
		}
		class Init extends InitialState {}

		class CallReceived extends Signal {}
		class CallerBusyOrUnknown extends Signal {}
		class CallerVerifiedAndSynced extends Signal {}
		class CalleeUnknown extends Signal {}
		class CalleeBusy extends Signal {}
		class CalleeVerifiedAndSynced extends Signal {}
		class RingingTimerExpired extends Signal {}
		class EndCallReceived extends Signal {}
		class CallRequestAccepted extends Signal {}
		
		@From(Init.class) @To(WaitingForCall.class)
		class InitialTransition extends Transition {
			@Override public void effect(){}
		}
		@From(WaitingForCall.class) @To(VerifyingCaller.class) @Trigger(CallReceived.class)
		class StartVerification extends Transition {
		}
		@From(VerifyingCaller.class) @To(VerifyingCallee.class) @Trigger(CallerVerifiedAndSynced.class)
		class InVerification extends Transition {
		}
		@From(VerifyingCallee.class) @To(CallInitiation.class) @Trigger(CalleeVerifiedAndSynced.class)
		class VerificationSuccessfull extends Transition {
		}
		@From(CallInitiation.class) @To(InACall.class) @Trigger(CallRequestAccepted.class)
		class CallInitiationSuccessfull extends Transition {
		}
		@From(InACall.class) @To(CallTermination.class) @Trigger(EndCallReceived.class)
		class EndOfCall extends Transition {
		}
		@From(CallTermination.class) @To(WaitingForCalleeReset.class) @Trigger(CallerVerifiedAndSynced.class)
		class CallerTerminatedFirst extends Transition {
		}
		@From(CallTermination.class) @To(WaitingForCallerReset.class) @Trigger(CalleeVerifiedAndSynced.class)
		class CalleeTerminatedFirst extends Transition {
		}
		@From(WaitingForCalleeReset.class) @To(Terminated.class) @Trigger(CalleeVerifiedAndSynced.class)
		class CalleeTerminatedSecondly extends Transition {
		}
		@From(WaitingForCallerReset.class) @To(Terminated.class) @Trigger(CallerVerifiedAndSynced.class)
		class CallerTerminatedSecondly extends Transition {
		}
		@From(VerifyingCaller.class) @To(HackingAttempt.class) @Trigger(CallerBusyOrUnknown.class)
		class HackingDiscovered extends Transition {
		}
		@From(HackingAttempt.class) @To(Terminated.class) @Trigger(CallerVerifiedAndSynced.class)
		class HackingResolved extends Transition {
		}
		@From(VerifyingCallee.class) @To(UnknownNumber.class) @Trigger(CalleeUnknown.class)
		class UnknownCalleeFailure extends Transition {
		}
		@From(UnknownNumber.class) @To(Terminated.class) @Trigger(CallerVerifiedAndSynced.class)
		class UnknownCalleeResolved extends Transition {
		}
		@From(VerifyingCallee.class) @To(LineIsBusy.class) @Trigger(CalleeBusy.class)
		class BusyCalleeFailure extends Transition {
		}
		@From(LineIsBusy.class) @To(Terminated.class) @Trigger(CallerVerifiedAndSynced.class)
		class BusyResolved extends Transition {
		}
		@From(CallInitiation.class) @To(CallTermination.class) @Trigger(RingingTimerExpired.class)
		class RingingTimeout extends Transition {
		}
		@From(CallInitiation.class) @To(CallTermination.class) @Trigger(EndCallReceived.class)
		class Cancellation extends Transition {
		}
	}
	
	class CallerInitiatesCall extends Association {
		@One Call initiated;
		@One Caller initiator;
	}

	class CalleeJoinsCall extends Association {
		@One Call joined;
		@One Callee joiner;
	}

	class RegistrationProcess extends ModelClass {
	}

	class RegistrationProcessRegistersSubscriber extends Association {
		@One RegistrationProcess controller;
		@One UnregisteredSubscriber toBeRegistered;
	}

	class UnregistrationProcess extends ModelClass {
	}

	class UnregistrationProcessUnregistersSubscriber extends Association {
		@One UnregistrationProcess controller;
		@One RegisteredSubscriber toBeUnregistered;
	}

	class Subscriber extends ModelClass {
		ModelInt phoneNumber;
	}
	
	class SubscriberStatus extends ModelClass {
	}
	
	class StatusOfSubscriber extends Association {
		@One Subscriber statusOwner;
		@One SubscriberStatus status;
	}
	
	class Caller extends SubscriberStatus {
	}

	class Callee extends SubscriberStatus {
	}
	
	class RegisteredSubscriber extends SubscriberStatus {
	}

	class UnregisteredSubscriber extends SubscriberStatus {
	}
}
