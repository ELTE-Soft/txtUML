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
      //sm_->setRuntime(this);
      static_cast<RuntimeType*>(this)->setupObjectSpecificRuntime(sm_);
  }

  void setupObject(ObjectList& ol_)
  {
      for(auto it=ol_.begin();it!=ol_.end();++it)
      {
        //(*it)->setRuntime(this);
        static_cast<RuntimeType*>(this)->setupObjectSpecificRuntime(*it);
      }
  }

  void stop()
  {
      std::unique_lock<std::mutex> mlock(_mutex);
      _stop=true;
      mlock.unlock();
      _cond.notify_one();
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
      //sm->setRuntime(nullptr);
      static_cast<RuntimeType*>(this)->removeObject(sm);
  }


  void stopUponCompletion()
  {
        static_cast<RuntimeType*>(this)->stopUponCompletion();
  }

protected:
  RuntimeI(): _stop(false) {}

  std::atomic_bool _stop;
  std::mutex _mutex;
  std::condition_variable _cond;
};


class SingleThreadRT:public RuntimeI<SingleThreadRT>
{
public:

  static SingleThreadRT* createRuntime();
  void start();
  void setupObjectSpecificRuntime(StateMachineI*);
  void setConfiguration(ThreadConfiguration*);
  void stopUponCompletion();
  bool isConfigurated();
private:
  SingleThreadRT();
  static SingleThreadRT* instance;
  
  std::shared_ptr<MessageQueueType> _messageQueue;
  std::condition_variable waiting_empty_cond;
  std::atomic_bool waiting;
};

class ConfiguratedThreadedRT: public RuntimeI<ConfiguratedThreadedRT>
{
public:

    static ConfiguratedThreadedRT* createRuntime();
	
    void start();
	void removeObject(StateMachineI*);
	void stopUponCompletion();
	void setConfiguration(ThreadConfiguration*);
	bool isConfigurated();
	void setupObjectSpecificRuntime(StateMachineI*);
   ~ConfiguratedThreadedRT();
private:
        ConfiguratedThreadedRT();
        static ConfiguratedThreadedRT* instance;

        
        

	ThreadPoolManager* poolManager;
	std::vector<int> numberOfObjects;
};




#endif // RUNTIME_HPP_INCLUDED
