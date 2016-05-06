#ifndef RUNTIME_HPP_INCLUDED
#define RUNTIME_HPP_INCLUDED

#include <thread>
#include <atomic>
#include <mutex>
#include <condition_variable>
#include <vector>

#include "runtimetypes.hpp"
#include "statemachineI.hpp"
#include "threadpool.hpp"
#include "eventI.hpp"
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

  void setupObject(StateMachineI* sm_)
  {
      static_cast<RuntimeType*>(this)->setupObjectSpecificRuntime(sm_);
  }
  
  void enqueObject(StateMachineI* sm)
  {
      static_cast<RuntimeType*>(this)->enqueObject(sm);
  }

  void setupObject(ObjectList& ol_)
  {
      for(auto it=ol_.begin();it!=ol_.end();++it)
      {
        static_cast<RuntimeType*>(this)->setupObjectSpecificRuntime(*it);
      }
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

  void removeObject(StateMachineI* sm)
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
  void setupObjectSpecificRuntime(StateMachineI*);
  void setConfiguration(ThreadConfiguration*);
  void enqueObject(StateMachineI*);
  void stopUponCompletion();
  void removeObject(StateMachineI*);
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
	void removeObject(StateMachineI*);
	void stopUponCompletion();
	void setConfiguration(ThreadConfiguration*);
	void enqueObject(StateMachineI*);
	bool isConfigurated();
	void setupObjectSpecificRuntime(StateMachineI*);
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
