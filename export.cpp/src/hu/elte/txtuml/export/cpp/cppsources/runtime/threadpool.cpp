#include <atomic>
#include <functional>

#include "threadpool.hpp"

void timeOut(std::chrono::milliseconds& duration_,std::atomic_bool& stop_)
{
  std::this_thread::sleep_for( duration_ );
  stop_=true;
}

// the constructor just launches some amount of workers
StateMachineThreadPool::StateMachineThreadPool(size_t threads,int workTime_)
    :   stop(false),_workTime(workTime_)
{
    for(size_t i = 0;i<threads;++i)
        workers.emplace_back(
            [this]
            {
                StateMachineI* sm=nullptr;
                for(;;)
                {
                   if(this->stop)
                        return;
                    stateMachines.pop_front(sm);

                    std::atomic_bool stopped(false);
                    std::thread th(&timeOut,std::ref(_workTime),std::ref(stopped));
                    th.detach();

                    while(!stopped)
                    {
                      if(!sm->emptyMessageQueue())
                      {
                        sm->processEventVirtual();
                      }
                    }

                    if(!sm->emptyMessageQueue())
                    {
                        stateMachines.push_back(sm);
                    }
                    else
                    {
                       sm->setPooled(false);
                    }
                }
            }
        );
}


// the destructor joins all threads
StateMachineThreadPool::~StateMachineThreadPool()
{
    stop = true;
    for(size_t i = 0;i<workers.size();++i)
        workers[i].join();
}
