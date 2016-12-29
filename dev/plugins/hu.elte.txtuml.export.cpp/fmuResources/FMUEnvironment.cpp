#include "fmi2Functions.h"
#include "fmi2FunctionTypes.h"
#include "fmi2TypesPlatform.h"

#include "FMUEnvironment.hpp"

#include <iostream>

typedef struct {
    fmu_environment *fmu_env;
    $fmuclass *fmu_class;
} FMU;

typedef enum {
    fmi2RealType, fmi2IntegerType, fmi2BooleanType, fmi2StringType
} val_types;

class fmu_environment : public StateMachineBase {
  fmi2CallbackFunctions* callbacks;
  void *values[];
  val_types var_tys[];

  bool process_event(EventBaseCRef event) {
    if (event.t == $controlevent) {
      // set the environment variables by name-id pairs
      $setvariables
    }
    callbacks->stepFinished(NULL, fmi2OK);
  }

  void setInitialState() {
    // nothing to do
  }
  
  fmu_environment(val_types var_tys[]) {
    this->var_tys = var_tys;
    for (size_t i = 0; i < var_tys.length; ++i) {
      if (var_tys[i] == fmi2RealType) {
        values[i] = new fmi2Real;
      } else if (var_tys[i] == fmi2IntegerType) {
        values[i] = new fmi2Integer;
      } if (var_tys[i] == fmi2IntegerType) {
        values[i] = new fmi2Boolean;
      } else if (var_tys[i] == fmi2IntegerType) {
        values[i] = new fmi2String;
      }
    }
  }

  ~fmu_environment() {
    for (size_t i = 0; i < var_tys.length; ++i) {
      if (var_tys[i] == fmi2RealType) {
        delete static_cast<fmi2Real*>(values[i]);
      } else if (var_tys[i] == fmi2IntegerType) {
        delete static_cast<fmi2Integer*>(values[i]);
      } if (var_tys[i] == fmi2IntegerType) {
        delete static_cast<fmi2Boolean*>(values[i]);
      } else if (var_tys[i] == fmi2IntegerType) {
        delete static_cast<fmi2Integer*>(values[i]);
      }
    }
  }
};

__declspec(dllexport) const char* fmi2GetTypesPlatform() {
  return "default";
}

__declspec(dllexport) const char* fmi2GetVersion() {
  return "2.0";
}

__declspec(dllexport) fmi2Status fmi2SetDebugLogging( fmi2Component c,
                                                      fmi2Boolean loggingOn,
                                                      size_t nCategories,
                                                      const fmi2String categories[] ) {
  return fmi2OK;
}

__declspec(dllexport) fmi2Component fmi2Instantiate( fmi2String instanceName,
                                                     fmi2Type fmuType,
                                                     fmi2String fmuGUID,
                                                     fmi2String fmuResourceLocation,
                                                     const fmi2CallbackFunctions* functions,
                                                     fmi2Boolean visible,
                                                     fmi2Boolean loggingOn ) {
  FMU* fmu = new FMU;
  val_types envvars[] = { $envvariabletypes };
  fmu->fmu_env = new fmu_environment(envvars);
  fmu->fmu_env->callbacks = functions;
  fmu->fmu_class = new $fmuclass(fmu->fmu_env);
  // TODO: check instance name, GUID
  return fmu;
}

__declspec(dllexport) void fmi2FreeInstance(fmi2Component c) {
  FMU* fmu = static_cast<FMU*>(c);
}

__declspec(dllexport) fmi2Status fmi2SetupExperiment( fmi2Component c,
                                                      fmi2Boolean toleranceDefined,
                                                      fmi2Real tolerance,
                                                      fmi2Real startTime,
                                                      fmi2Boolean stopTimeDefined,
                                                      fmi2Real stopTime ) {
  return fmi2OK;
}

__declspec(dllexport) fmi2Status fmi2EnterInitializationMode(fmi2Component c) {
  return fmi2OK;
}

__declspec(dllexport) fmi2Status fmi2ExitInitializationMode(fmi2Component c) {
  return fmi2OK;
}

__declspec(dllexport) fmi2Status fmi2Terminate(fmi2Component c) {
  return fmi2OK;
}

__declspec(dllexport) fmi2Status fmi2Reset(fmi2Component c) {
  FMU* fmu = static_cast<FMU*>(c);
  fmi2CallbackFunctions* callbacks = fmu->fmu_env->callbacks;
  fmu->fmu_env = new fmu_environment;
  fmu->fmu_env->callbacks = callbacks;
  return fmi2OK;
}

__declspec(dllexport) fmi2Status fmi2GetReal ( fmi2Component c, 
                                               const fmi2ValueReference vr[],
                                               size_t nvr, 
                                               fmi2Real value[] ) {
  FMU* fmu = static_cast<FMU*>(c);
  for (size_t i = 0; i < nvr; ++i) {
    value[i] = *static_cast<fmi2Real*>(fmu->fmu_env.values[vr[i]]);
  }
  return fmi2OK;
}

__declspec(dllexport) fmi2Status fmi2GetInteger ( fmi2Component c, 
                                                  const fmi2ValueReference vr[],
                                                  size_t nvr, 
                                                  fmi2Integer value[] ) {
  FMU* fmu = static_cast<FMU*>(c);
  for (size_t i = 0; i < nvr; ++i) {
    value[i] = *static_cast<fmi2Integer*>(fmu->fmu_env.values[vr[i]]);
  }
  return fmi2OK;
}

__declspec(dllexport) fmi2Status fmi2GetBoolean ( fmi2Component c, 
                                                  const fmi2ValueReference vr[],
                                                  size_t nvr, 
                                                  fmi2Boolean value[] ) {
  FMU* fmu = static_cast<FMU*>(c);
  for (size_t i = 0; i < nvr; ++i) {
    value[i] = *static_cast<fmi2Boolean*>(fmu->fmu_env.values[vr[i]]);
  }
  return fmi2OK;
}

__declspec(dllexport) fmi2Status fmi2GetString ( fmi2Component c, 
                                                 const fmi2ValueReference vr[],
                                                 size_t nvr, 
                                                 fmi2String value[] ) {
  FMU* fmu = static_cast<FMU*>(c);
  for (size_t i = 0; i < nvr; ++i) {
    value[i] = *static_cast<fmi2String*>(fmu->fmu_env.values[vr[i]]);
  }
  return fmi2OK;
}

__declspec(dllexport) fmi2Status fmi2SetReal ( fmi2Component c, 
                                               const fmi2ValueReference vr[],
                                               size_t nvr, 
                                               const fmi2Real value[] ) {
  FMU* fmu = static_cast<FMU*>(c);
  for (size_t i = 0; i < nvr; ++i) {
    *static_cast<fmi2Real*>(fmu->fmu_env.values[vr[i]]) = value[i];
  }
  return fmi2OK;
}

__declspec(dllexport) fmi2Status fmi2SetInteger ( fmi2Component c, 
                                                  const fmi2ValueReference vr[],
                                                  size_t nvr, 
                                                  const fmi2Integer value[] ) {
  FMU* fmu = static_cast<FMU*>(c);
  for (size_t i = 0; i < nvr; ++i) {
    *static_cast<fmi2Integer*>(fmu->fmu_env.values[vr[i]]) = value[i];
  }
  return fmi2OK;}

__declspec(dllexport) fmi2Status fmi2SetBoolean ( fmi2Component c, 
                                                  const fmi2ValueReference vr[],
                                                  size_t nvr, 
                                                  const fmi2Boolean value[] ) {
  FMU* fmu = static_cast<FMU*>(c);
  for (size_t i = 0; i < nvr; ++i) {
    *static_cast<fmi2Boolean*>(fmu->fmu_env.values[vr[i]]) = value[i];
  }
  return fmi2OK;
}

__declspec(dllexport) fmi2Status fmi2SetString ( fmi2Component c, 
                                                 const fmi2ValueReference vr[],
                                                 size_t nvr, 
                                                 const fmi2String value[] ) {
  FMU* fmu = static_cast<FMU*>(c);
  for (size_t i = 0; i < nvr; ++i) {
    *static_cast<fmi2String*>(fmu->fmu_env.values[vr[i]]) = value[i];
  }
  return fmi2OK;
}

__declspec(dllexport) fmi2Status fmi2GetFMUstate (fmi2Component c, fmi2FMUstate* FMUstate) {
  return fmi2Error; // not supported
}

__declspec(dllexport) fmi2Status fmi2SetFMUstate (fmi2Component c, fmi2FMUstate FMUstate) {
  return fmi2Error; // not supported
}

__declspec(dllexport) fmi2Status fmi2FreeFMUstate (fmi2Component c, fmi2FMUstate* FMUstate) {
  return fmi2Error; // not supported
}

__declspec(dllexport) fmi2Status fmi2SerializedFMUstateSize(fmi2Component c, fmi2FMUstate FMUstate, size_t *size) {
  return fmi2Error; // not supported
}

__declspec(dllexport) fmi2Status fmi2SerializeFMUstate ( fmi2Component c, 
                                                         fmi2FMUstate FMUstate,
                                                         fmi2Byte serializedState[], 
                                                         size_t size ) {
  return fmi2Error; // not supported
}

__declspec(dllexport) fmi2Status fmi2DeSerializeFMUstate ( fmi2Component c,
                                                           const fmi2Byte serializedState[],
                                                           size_t size, 
                                                           fmi2FMUstate* FMUstate ) {
  return fmi2Error; // not supported
}

__declspec(dllexport) fmi2Status fmi2GetDirectionalDerivative( fmi2Component c,
                                                               const fmi2ValueReference vUnknown_ref[],
                                                               size_t nUnknown,
                                                               const fmi2ValueReference vKnown_ref[],
                                                               size_t nKnown,
                                                               const fmi2Real dvKnown[],
                                                               fmi2Real dvUnknown[] ) {
  return fmi2Error; // not supported
}

__declspec(dllexport) fmi2Status fmi2SetRealInputDerivatives( fmi2Component c,
                                                              const fmi2ValueReference vr[],
                                                              size_t nvr,
                                                              const fmi2Integer order[],
                                                              const fmi2Real value[] ) {
  return fmi2Error; // not supported
}

__declspec(dllexport) fmi2Status fmi2GetRealOutputDerivatives ( fmi2Component c,
                                                                const fmi2ValueReference vr[],
                                                                size_t nvr,
                                                                const fmi2Integer order[],
                                                                fmi2Real value[] ) {
  return fmi2Error; // not supported
}

__declspec(dllexport) fmi2Status fmi2DoStep( fmi2Component c,
                                             fmi2Real currentCommunicationPoint,
                                             fmi2Real communicationStepSize,
                                             fmi2Boolean noSetFMUStatePriorToCurrentPoint) {
  FMU* fmu = static_cast<FMU*>(c);
  $cyclesignal sig = new $cyclesignal($cyclesignalmembers);
  fmu->fmu_class->send(sig);
  return fmi2Pending;
}

__declspec(dllexport) fmi2Status fmi2CancelStep(fmi2Component c) {
  // TODO: terminate
  return fmi2OK;
}

__declspec(dllexport) fmi2Status fmi2GetStatus ( fmi2Component c, 
                                                 const fmi2StatusKind s,
                                                 fmi2Status* value ) {
  // TODO: get status
  return fmi2OK;
}

__declspec(dllexport) fmi2Status fmi2GetRealStatus (fmi2Component c, const fmi2StatusKind s, fmi2Real* value) {
  // TODO: get status
  return fmi2OK;
}

__declspec(dllexport) fmi2Status fmi2GetIntegerStatus(fmi2Component c, const fmi2StatusKind s, fmi2Integer* value) {
  // TODO: get status
  return fmi2OK;
}

__declspec(dllexport) fmi2Status fmi2GetBooleanStatus(fmi2Component c, const fmi2StatusKind s, fmi2Boolean* value) {
  // TODO: get status
  return fmi2OK;
}

__declspec(dllexport) fmi2Status fmi2GetStringStatus (fmi2Component c, const fmi2StatusKind s, fmi2String* value) {
  // TODO: get status
  return fmi2OK;
}
