#ifndef ISTATEMACHINE_HPP_INCLUDED
#define ISTATEMACHINE_HPP_INCLUDED

#include <memory>
#include <mutex>
#include <atomic>
#include <string>

#include "runtimetypes.hpp"

class StateMachineThreadPool;

class IStateMachine
{
public:
  virtual void processEventVirtual () = 0;
  virtual void processInitTransition () = 0;

  void startSM() { _started = true; if (_pool != nullptr) handlePool(); }
  void runSM();
  void send (EventPtr e_);
  void init ();
  EventPtr getNextMessage () {return _messageQueue->front();}
  void deleteNextMessage  () {_messageQueue->pop_front();(*message_counter)--; }
  bool emptyMessageQueue  () {return _messageQueue->empty();}
  void setPool (StateMachineThreadPool* pool_){_pool=pool_;}
  void setMessageQueue (std::shared_ptr<MessageQueueType> messageQueue_) {_messageQueue=messageQueue_;}
  void setPooled (bool);
  bool isInPool() {return _inPool;}
  bool isStarted() {return _started;}
  bool isInitialized() {return _initialized; }
  int getPoolId() {return poolId;}
  void setMessageCounter (std::atomic_int* counter) { message_counter = counter; }
  
  virtual std::string toString() {return "";}
  virtual ~IStateMachine();
protected:
  IStateMachine  (std::shared_ptr<MessageQueueType> messageQueue_=std::shared_ptr<MessageQueueType>(new MessageQueueType()));
  void setPoolId (int id) {poolId = id;}
private:
  void handlePool ();
  
  std::shared_ptr<MessageQueueType> _messageQueue;
  StateMachineThreadPool* _pool;//safe because: controlled by the runtime, but we can not set it in the constructor
  std::mutex _mutex;
  std::condition_variable _cond;
  std::atomic_bool _inPool;
  std::atomic_bool _started;
  std::atomic_bool _initialized;
  std::atomic_int* message_counter;
  int poolId;
  
  
  
};

#endif // ISTATEMACHINE_HPP_INCLUDED
