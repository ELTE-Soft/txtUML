#ifndef ISTATEMACHINE_HPP_INCLUDED
#define ISTATEMACHINE_HPP_INCLUDED

#include <memory>
#include <mutex>
#include <atomic>
#include <string>

#include "ModelObject.hpp"
#include "statemachinebase.hpp"
#include "ESRoot/Containers/ThreadSafeQueue.hpp"
#include "ESRoot/AtomicCounter.hpp"

namespace Execution {
class StateMachineThreadPool;
}

namespace Model
{

class StateMachineOwner : public ES::ModelObject, public Model::StateMachineBase
{
public:
	virtual ~StateMachineOwner();
	virtual void start() override;
	virtual void deleteObject() override;
	virtual void send(const ES::EventRef e) override;

public:
	// for runtime
	bool processNextEvent();
	void setPool(ES::SharedPtr<Execution::StateMachineThreadPool> pool);
	void setPooled(bool value = true);
	void setMessageQueue(ES::SharedPtr<ES::MessageQueueType> messageQueue);
	void setMessageCounter(ES::SharedPtr<ES::AtomicCounter> counter);

	int getPoolId() const;
	virtual std::string toString() const;
	bool emptyMessageQueue() const;

protected:
	StateMachineOwner();

	virtual void processEventVirtual(ES::EventRef event) = 0;
	virtual void processInitTransition(ES::EventRef event) = 0;

	ES::EventRef getNextMessage();
	void setPoolId(int id);

private:
	void handlePool();
	void destroy();

	ES::SharedPtr<ES::MessageQueueType> _messageQueue;
	ES::SharedPtr<Execution::StateMachineThreadPool> _pool;
	std::mutex _mutex;
	std::condition_variable _cond;
	std::atomic_bool _inPool;
	std::atomic_bool _started;
	ES::SharedPtr<ES::AtomicCounter> messageCounter;
	

	int poolId;



};

class NotStateMachineOwner : public ES::ModelObject
{
public:
	virtual ~NotStateMachineOwner();
	virtual void start() override;
	virtual void deleteObject() override;
	virtual void send(const ES::EventRef e) override;

};

class SubStateMachine : public Model::StateMachineBase
{
};

}


#endif // ISTATEMACHINE_HPP_INCLUDED
