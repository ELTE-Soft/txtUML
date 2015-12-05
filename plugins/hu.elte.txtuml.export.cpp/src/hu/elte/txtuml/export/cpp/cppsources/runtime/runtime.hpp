#ifndef RUNTIME_HPP_INCLUDED
#define RUNTIME_HPP_INCLUDED

#include <thread>
#include <atomic>
#include <mutex>
#include <list>
#include <map>

#include "runtimetypes.hpp"
#include "statemachineI.hpp"
#include "threadpool.hpp"
#include "eventI.hpp"
#include "threadpoolmanager.hpp"
#include "runtimetypes.hpp"


class RuntimeI
{
public:
  RuntimeI();
  void setupObject(ObjectList& ol_);
  void setupObject(StateMachineI* sm_);
  void startObject(ObjectList& ol_);

  virtual void startObject(StateMachineI* sm_)=0;
  virtual void run()=0;
  virtual void removeObject(StateMachineI* sm_) {sm_->setRuntime(nullptr);}
  void stop();
  virtual void stopUponCompletion() = 0;

protected:
  virtual void setupObjectVirtual(StateMachineI*){}

  std::atomic_bool _stop;
  std::mutex _mutex;
  std::condition_variable _cond;
};


class SingleThreadRT:public RuntimeI
{
public:
  SingleThreadRT();
  void startObject(StateMachineI* sm_){sm_->startSM();}
  void run();
  void stopUponCompletion();

private:
  void setupObjectVirtual(StateMachineI* sm_){sm_->setMessageQueue(_messageQueue);}

  std::shared_ptr<MessageQueueType> _messageQueue;
  std::condition_variable waiting_empty_cond;
};

class ConfiguredThreadPoolsRT: public RuntimeI
{
public:
	ConfiguredThreadPoolsRT();
	
	void startObject(StateMachineI* sm_){sm_->startSM();}
	void run();
	void removeObject(StateMachineI*);
	void stopUponCompletion();
private:
	void setupObjectVirtual(StateMachineI*);
	
	std::map<id_type,int> number_of_objects;
	std::list<id_type> pool_ides;
	
	ThreadPoolManager* pool_manager;
};




#endif // RUNTIME_HPP_INCLUDED
