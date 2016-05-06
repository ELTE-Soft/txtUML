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
	void stopUponCompletion(std::atomic_int*);
	void startPool();
	void modifiedThreads(int);
	void setWorkersCounter(std::atomic_int* counter) {this->worker_threads = counter;}
	void setStopReqest(std::condition_variable* stop_req) {stop_request_cond = stop_req;}
	~StateMachineThreadPool();
private:
	
	ThreadContainer workers;
	// the task queue
	PoolQueueType stateMachines; //must be blocking queue
	int threads;
	
	void incrementWorkers();
	void reduceWorkers();

	// synchronization
	std::atomic_bool stop;
	
	std::atomic_int* worker_threads;
	
	std::condition_variable cond;
	std::condition_variable* stop_request_cond;

	std::mutex mu;
	std::mutex modifie_mutex;
	std::mutex stop_request_mu;
};


#endif
