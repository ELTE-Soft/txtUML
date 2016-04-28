#ifndef STATEMACHINEI_HPP_INCLUDED
#define STATEMACHINEI_HPP_INCLUDED

#include <memory>
#include <mutex>
#include <atomic>

#include "runtimetypes.hpp"

//class RuntimeI;
class StateMachineThreadPool;

class StateMachineI
{
public:
  virtual void processEventVirtual()=0;
  virtual void processInitTranstion() = 0;

  void startSM() {_started = true;}
  void runSM();
  void send(EventPtr e_);
  void init();
  EventPtr getNextMessage(){return _messageQueue->front();}
  void deleteNextMessage() {_messageQueue->pop_front(); }
  bool emptyMessageQueue(){return _messageQueue->empty();}
  void setPool(StateMachineThreadPool* pool_){_pool=pool_;}
  void setMessageQueue(std::shared_ptr<MessageQueueType> messageQueue_){_messageQueue=messageQueue_;}
  void setPooled(bool);
  bool isInPool(){return _inPool;}
  bool isStarted() {return _started;}
  bool isInitialized() {return _initialized; }
  int getPoolId() {return poolId;}
  
  virtual ~StateMachineI();
protected:
  StateMachineI(std::shared_ptr<MessageQueueType> messageQueue_=std::shared_ptr<MessageQueueType>(new MessageQueueType()));
  //RuntimeI* _runtime;////safe because: controlled by the runtime, but we can not set it in the constructor
  void setPoolId(int id) {poolId = id;}
private:
  void handlePool();
  
  std::shared_ptr<MessageQueueType> _messageQueue;
  StateMachineThreadPool* _pool;//safe because: controlled by the runtime, but we can not set it in the constructor
  std::mutex _mutex;
  std::condition_variable _cond;
  std::atomic_bool _inPool;
  std::atomic_bool _started;
  std::atomic_bool _initialized;
  int poolId;
  
  
  
};

#endif // STATEMACHINEI_HPP_INCLUDED
