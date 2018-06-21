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
#include "StateMachineOwner.hpp"
#include "ESRoot/AtomicCounter.hpp"

namespace Execution {
	class StateMachineThreadPool;
}

namespace Execution
{


class PoolQueueType : public ES::ThreadSafeQueue<ES::Queue<ES::StateMachineRef>> {
public:
	PoolQueueType(StateMachineThreadPool * ownerPool_);
protected:
	virtual bool exitFromWaitingCondition();

private:
	StateMachineThreadPool * ownerPool;
};

class StateMachineThreadPool {

public:
	friend class PoolQueueType;

	using SharedConditionVar = ES::SharedPtr<std::condition_variable>;
public:
	StateMachineThreadPool();
	void task();
	void enqueueObject(ES::StateMachineRef);
	void stopPool();
	void stopUponCompletion();
	void startPool(unsigned);
	void modifyThreads(unsigned);

	void setWorkersCounter(ES::SharedPtr<ES::AtomicCounter> counter) { nOfWorkerThreads = counter; }
	void setMessageCounter(ES::SharedPtr<ES::AtomicCounter> counter) { nOfAllMessages = counter;   }

	void setStopReqest(std::condition_variable* stop_req) { stop_request_cond = stop_req; }
	~StateMachineThreadPool();
private:

	ThreadContainer workers;
	SharedConditionVar _sharedConditionVar;

	// the task queue
	PoolQueueType _stateMachines; //must be blocking queue

	void incrementWorkers();
	void reduceWorkers();
	
	ES::SharedPtr<ES::AtomicCounter> nOfWorkerThreads;
	ES::SharedPtr<ES::AtomicCounter> nOfAllMessages;
	std::condition_variable* stop_request_cond;

	// synchronization
	std::atomic_bool _stop;	
	std::mutex modifie_mutex;
	std::mutex stop_request_mu;
};

}



#endif
