#ifndef THREAD_POOL_H
#define THREAD_POOL_H

#include <memory>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <functional>
#include <stdexcept>
#include <atomic>

#include "threadcontainer.hpp"
#include "runtimetypes.hpp"
#include "statemachineI.hpp"

class StateMachineThreadPool {
	
public:
	StateMachineThreadPool(int);
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
	// the task queue
	PoolQueueType stateMachines; //must be blocking queue
	int threads;
        int reduce_number;

	// synchronization
	std::atomic_bool stop;
	std::atomic_int worker_threads;
	
	std::condition_variable cond;
	std::condition_variable complite_cond;

	std::mutex mu;
	std::mutex worker_mu;
	std::mutex complite_mu;
};


#endif
