#ifndef RUNTIME_HPP_INCLUDED
#define RUNTIME_HPP_INCLUDED

#include <thread>
#include <atomic>

#include "runtimetypes.hpp"
#include "statemachineI.hpp"
#include "threadpool.hpp"
#include "eventI.hpp"

class RuntimeI
{
public:
  RuntimeI();
  void setupObject(ObjectList& ol_);
  void setupObject(StateMachineI* sm_);
  void startObject(ObjectList& ol_);

  virtual void startObject(StateMachineI* sm_)=0;
  virtual void run()=0;
  void stop();

protected:
  virtual void setupObjectVirtual(StateMachineI*){}

  std::atomic_bool _stop;
  std::mutex _mutex;
  std::condition_variable _cond;
};

class SeparateThreadRT: public RuntimeI
{
public:
  void startObject(StateMachineI* sm_);
  void run();
};


class SingleThreadRT:public RuntimeI
{
public:
  SingleThreadRT();
  void startObject(StateMachineI* sm_){sm_->startSM();}
  void run();

private:
  void setupObjectVirtual(StateMachineI* sm_){sm_->setMessageQueue(_messageQueue);}

  std::shared_ptr<MessageQueueType> _messageQueue;
};

class ThreadPoolRT:public RuntimeI
{
public:
  ThreadPoolRT(size_t threads_= defaultThreadCount,int workTime_= defaultWorkTime);
  void startObject(StateMachineI* sm_){sm_->startSM();}
  void run();

private:
  void setupObjectVirtual(StateMachineI* sm_){sm_->setPool(&_pool);}

  StateMachineThreadPool _pool;
};




#endif // RUNTIME_HPP_INCLUDED
