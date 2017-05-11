#ifndef ISTATEMACHINE_HPP_INCLUDED
#define ISTATEMACHINE_HPP_INCLUDED

#include <memory>
#include <mutex>
#include <atomic>
#include <string>

#include "ESRoot/Types.hpp"
#include "ESRoot/Containers/ThreadSafeQueue.hpp"

namespace Execution {
class StateMachineThreadPool;
}

namespace Model
{

class IStateMachine
{
public:
	virtual ~IStateMachine();
	virtual void processEventVirtual() = 0;
	virtual void processInitTransition() = 0;

	void startSM();
	void send(const ES::EventRef e);
	void init();
	ES::EventRef getNextMessage();
	bool emptyMessageQueue();
	void setPool(ES::SharedPtr<Execution::StateMachineThreadPool> pool);
	void setMessageQueue(ES::SharedPtr<ES::MessageQueueType> messageQueue);
	void setPooled(bool value);
	bool isInPool() const;
	bool isStarted() const;
	bool isInitialized() const;
	bool isDestoryed() const;
	int getPoolId() const;
	void setMessageCounter(std::atomic_int* counter);
	virtual std::string toString() const;

protected:
	IStateMachine(ES::SharedPtr<ES::MessageQueueType> messageQueue = ES::SharedPtr<ES::MessageQueueType>(new ES::MessageQueueType()));
	void setPoolId(int id);
	void destroy();
private:
	void handlePool();

	ES::SharedPtr<ES::MessageQueueType> _messageQueue;
	ES::SharedPtr<Execution::StateMachineThreadPool> _pool;//safe because: controlled by the runtime, but we can not set it in the constructor
	std::mutex _mutex;
	std::condition_variable _cond;
	std::atomic_bool _inPool;
	std::atomic_bool _started;
	std::atomic_bool _initialized;
	std::atomic_bool _deleted;
	std::atomic_int* message_counter;

	int poolId;



};

}


#endif // ISTATEMACHINE_HPP_INCLUDED
