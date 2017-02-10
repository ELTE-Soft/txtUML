/** @file runtime.hpp
*/

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
#include "ESRoot\Types.hpp"
template<typename RuntimeType>
class IRuntime
{
public:

  static ES::Ref<IRuntime<RuntimeType>> getRuntimeInstance()
  {
	  if (instance == nullptr)
	  {
		  instance = new RuntimeType();
	  }
	  return instance;
  }

  void setupObject(ES::StateMachineRef sm)
  {
      static_cast<RuntimeType*>(this)->setupObjectSpecificRuntime(sm);
  }
  
  void removeObject(ES::StateMachineRef sm)
  {
      static_cast<RuntimeType*>(this)->removeObject(sm);
  }
    
  void configure(ES::Ref<ThreadConfiguration> configuration)
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


  void stopUponCompletion()
  {
        static_cast<RuntimeType*>(this)->stopUponCompletion();
  }

protected:
  static ES::Ref<IRuntime<RuntimeType>> instance;
  IRuntime() {}
};


class SingleThreadRT:public IRuntime<SingleThreadRT>
{
public:
	virtual ~SingleThreadRT();

	void start();
	void setupObjectSpecificRuntime(ES::StateMachineRef);
	void removeObject(ES::StateMachineRef);
	void setConfiguration(ES::Ref<ThreadConfiguration>);
	bool isConfigurated();
	void stopUponCompletion();
private:
  SingleThreadRT();
  ES::Ref<MessageQueueType> _messageQueue;

};

class ConfiguratedThreadedRT: public IRuntime<ConfiguratedThreadedRT>
{
public:
	virtual ~ConfiguratedThreadedRT();

    void start();
	void setupObjectSpecificRuntime(ES::StateMachineRef);
	void removeObject(ES::StateMachineRef);
	void setConfiguration(ES::Ref<ThreadConfiguration>);
	bool isConfigurated();
	void stopUponCompletion();  
private:
    ConfiguratedThreadedRT();

	ES::Ref<ThreadPoolManager> poolManager;
	std::vector<int> numberOfObjects;
	std::atomic_int worker;
	std::atomic_int messages;
	std::condition_variable stop_request_cond;
};




#endif // RUNTIME_HPP_INCLUDED
