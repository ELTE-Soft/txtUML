#ifndef THREAD_POOL_H
#define THREAD_POOL_H

#include <vector>
#include <queue>
#include <memory>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <future>
#include <functional>
#include <stdexcept>
#include <chrono>
#include <atomic>

#include "runtimetypes.hpp"
#include "statemachineI.hpp"

class StateMachineThreadPool {
public:
    StateMachineThreadPool(size_t,int);
    void enqueObject(StateMachineI*);
    ~StateMachineThreadPool();
private:
    // need to keep track of threads so we can join them
    std::vector< std::thread > workers;
    // the task queue
    PoolQueueType stateMachines; //must be blocking queue

    // synchronization
    std::atomic_bool stop;
    std::chrono::milliseconds _workTime;
};

inline void StateMachineThreadPool::enqueObject(StateMachineI* sm_)
{
  stateMachines.push_back(sm_);
}


#endif
