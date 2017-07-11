#ifndef ISTATEMACHINE_HPP_INCLUDED
#define ISTATEMACHINE_HPP_INCLUDED

#include <memory>
#include <mutex>
#include <atomic>
#include <string>

#include "ESRoot/Types.hpp"
#include "ESRoot/Containers/ThreadSafeQueue.hpp"
#include "ESRoot/AtomicCounter.hpp"

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

	bool processNextEvent();

	void startSM();
	void deleteSM();
	void send(const ES::EventRef e);
	
	ES::EventRef getNextMessage();
	void setPool(ES::SharedPtr<Execution::StateMachineThreadPool> pool);
	void setMessageQueue(ES::SharedPtr<ES::MessageQueueType> messageQueue);
	void setPooled(bool value);
	void setMessageCounter(ES::SharedPtr<ES::AtomicCounter> counter);

	int getPoolId() const;
	virtual std::string toString() const;

protected:
	IStateMachine();
	void setPoolId(int id);

private:
	void handlePool();

	ES::SharedPtr<ES::MessageQueueType> _messageQueue;
	ES::SharedPtr<Execution::StateMachineThreadPool> _pool;
	std::mutex _mutex;
	std::condition_variable _cond;
	std::atomic_bool _inPool;
	std::atomic_bool _started;
	ES::SharedPtr<ES::AtomicCounter> messageCounter;

	void init();
	void destroy();

	int poolId;



};

}


#endif // ISTATEMACHINE_HPP_INCLUDED
