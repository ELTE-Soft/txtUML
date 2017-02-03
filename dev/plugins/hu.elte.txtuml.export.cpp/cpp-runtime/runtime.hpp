#ifndef RUNTIME_HPP_INCLUDED
#define RUNTIME_HPP_INCLUDED

#include <thread>
#include <atomic>
#include <mutex>
#include <condition_variable>
#include <vector>

#include "runtimetypes.hpp"
#include "istatemachine.hpp"
#include "threadpool.hpp"
#include "ievent.hpp"
#include "threadpoolmanager.hpp"
#include "runtimetypes.hpp"

template<typename RuntimeType>
class RuntimeI
{
public:

  static RuntimeI<RuntimeType>* createRuntime()
  {
      return RuntimeType::createRuntime();
  }

  void setupObject(IStateMachine* sm_)
  {
      static_cast<RuntimeType*>(this)->setupObjectSpecificRuntime(sm_);
  }
  
  void enqueueObject(IStateMachine* sm)
  {
      static_cast<RuntimeType*>(this)->enqueueObject(sm);
  }
  
  void configure(ThreadConfiguration* configuration)
  {
	  if(!(static_cast<RuntimeType*>(this)->isConfigurated()))
	  {
		  static_cast<RuntimeType*>(this)->setConfiguration(configuration);
	  }

  }
  
  void startRT()
  {
        static_cast<RuntimeType*>(this)->start();
  }

  void removeObject(IStateMachine* sm)
  {
      static_cast<RuntimeType*>(this)->removeObject(sm);
  }


  void stopUponCompletion()
  {
        static_cast<RuntimeType*>(this)->stopUponCompletion();
  }

protected:
  RuntimeI() {}
};


class SingleThreadRT:public RuntimeI<SingleThreadRT>
{
public:

  static SingleThreadRT* createRuntime();
  void start();
  void setupObjectSpecificRuntime(IStateMachine*);
  void setConfiguration(ThreadConfiguration*);
  void enqueueObject(IStateMachine*);
  void stopUponCompletion();
  void removeObject(IStateMachine*);
  bool isConfigurated();
private:
  SingleThreadRT();
  static SingleThreadRT* instance;  
  std::shared_ptr<PoolQueueType> _messageQueue;

};

class ConfiguratedThreadedRT: public RuntimeI<ConfiguratedThreadedRT>
{
public:
	
    static ConfiguratedThreadedRT* createRuntime();
	
    void start();
	void removeObject(IStateMachine*);
	void stopUponCompletion();
	void setConfiguration(ThreadConfiguration*);
	void enqueueObject(IStateMachine*);
	bool isConfigurated();
	void setupObjectSpecificRuntime(IStateMachine*);
   ~ConfiguratedThreadedRT();
private:
    ConfiguratedThreadedRT();
    static ConfiguratedThreadedRT* instance;
	ThreadPoolManager* poolManager;
	std::vector<int> numberOfObjects;
	std::atomic_int worker;
	std::atomic_int messages;
	std::condition_variable stop_request_cond;
};




#endif // RUNTIME_HPP_INCLUDED
