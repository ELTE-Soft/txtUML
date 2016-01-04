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
    StateMachineThreadPool(size_t);
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
	void futureGetter();
	
	ThreadContainer workers;
	
	int delta;
    // the task queue
    PoolQueueType stateMachines; //must be blocking queue
	size_t threads;

    // synchronization
    std::atomic_bool stop;
	std::atomic_int worker_threads;
	
	std::condition_variable cond;
	std::condition_variable complite_cond;

	std::mutex mu;
	std::mutex worker_mu;
	std::mutex complite_mu;
	std::mutex start_mu;
	
	//synchronize remove threads
	std::future<void> f;
	std::condition_variable future_cond;
	std::condition_variable future_cond_alt;
	std::mutex future_mu;
	std::thread* future_getter_thread;
	bool getter_ready;
};


#endif
