#ifndef ISTATEMACHINE_HPP_INCLUDED
#define ISTATEMACHINE_HPP_INCLUDED

#include <memory>
#include <mutex>
#include <atomic>
#include <string>

#include "ESRoot\Types.hpp"

class StateMachineThreadPool;

class IStateMachine
{
public:
  virtual void processEventVirtual () = 0;
  virtual void processInitTransition () = 0;

  void startSM() { _started = true; if (_pool != nullptr) handlePool(); }
  void send (const ES::EventRef e);
  void init ();
  ES::EventRef getNextMessage();
  bool emptyMessageQueue  () {return _messageQueue->isEmpty();}
  void setPool (ES::SharedPtr<StateMachineThreadPool> pool){_pool=pool;}
  void setMessageQueue (ES::SharedPtr<ES::MessageQueueType> messageQueue) {_messageQueue=messageQueue;}
  void setPooled (bool);
  bool isInPool() {return _inPool;}
  bool isStarted() const {return _started;}
  bool isInitialized() {return _initialized; }
  int getPoolId() {return poolId;}
  void setMessageCounter (std::atomic_int* counter) { message_counter = counter; }
  
  virtual std::string toString() {return "";}
  virtual ~IStateMachine();
protected:
  IStateMachine  (ES::SharedPtr<ES::MessageQueueType> messageQueue = ES::SharedPtr<ES::MessageQueueType>(new ES::MessageQueueType()));
  void setPoolId (int id) {poolId = id;}
private:
  void handlePool ();
  
  ES::SharedPtr<ES::MessageQueueType> _messageQueue;
  ES::SharedPtr<StateMachineThreadPool> _pool;//safe because: controlled by the runtime, but we can not set it in the constructor
  std::mutex _mutex;
  std::condition_variable _cond;
  std::atomic_bool _inPool;
  std::atomic_bool _started;
  std::atomic_bool _initialized;
  std::atomic_int* message_counter;
  int poolId;
  
  
  
};

#endif // ISTATEMACHINE_HPP_INCLUDED
