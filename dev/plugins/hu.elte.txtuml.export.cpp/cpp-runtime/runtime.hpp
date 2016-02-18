#ifndef RUNTIME_HPP_INCLUDED
#define RUNTIME_HPP_INCLUDED

#include <thread>
#include <atomic>
#include <mutex>
#include <vector>

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
  void setupObject(StateMachineI* sm_);
  void setupObject(ObjectList& ol_);
  
  virtual void run()=0;
  virtual void removeObject(StateMachineI* sm) {sm->setRuntime(nullptr);}
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
  void run();
  void stopUponCompletion();

private:
  void setupObjectVirtual(StateMachineI* sm){sm->setMessageQueue(_messageQueue);}

  std::shared_ptr<MessageQueueType> _messageQueue;
  std::condition_variable waiting_empty_cond;
  std::atomic_bool waiting;
};

class ConfiguratedThreadedRT: public RuntimeI
{
public:
	ConfiguratedThreadedRT();
	~ConfiguratedThreadedRT();
	
	void run();
	void removeObject(StateMachineI*);
	void stopUponCompletion();
	void setConfiguration(ThreadConfiguration*);
private:
	void setupObjectVirtual(StateMachineI*);
	
	ThreadPoolManager* poolManager;
	std::vector<int> numberOfObjects;
};




#endif // RUNTIME_HPP_INCLUDED
