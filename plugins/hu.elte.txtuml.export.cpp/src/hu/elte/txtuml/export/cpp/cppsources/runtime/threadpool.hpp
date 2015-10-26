#ifndef THREAD_POOL_H
#define THREAD_POOL_H

#include <vector>
#include <queue>
#include <map>
#include <memory>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <future>
#include <functional>
#include <stdexcept>
#include <chrono>
#include <atomic>

#include "threadcontainer.hpp"


#include "runtimetypes.hpp"
#include "statemachineI.hpp"

class StateMachineThreadPool {
	
public:
    StateMachineThreadPool(size_t,int);
	void task();
    void enqueObject(StateMachineI*);
	void stopPool();
	void stopUponCompletion();
	void startPool();
	void incrementWorkers();
	void reduceWorkers();
	void modifiedThreads(int);
    ~StateMachineThreadPool();
private:

	ThreadContainer workers;
	
	int delta;
    // the task queue
    PoolQueueType stateMachines; //must be blocking queue
	size_t threads;
	std::atomic_int messages_to_procces;

    // synchronization
    std::atomic_bool stop;
	std::atomic_int worker_threads;
	
	std::condition_variable cond;
	std::condition_variable complite_cond;

	std::mutex mu;
	std::mutex worker_mu;
	std::mutex complite_mu;
	std::mutex start_mu;
};


#endif
