#ifndef TYPES_HPP
#define TYPES_HPP

#include <string>
#include <memory>
#include "Containers\threadsafequeue.hpp"

class IStateMachine;
class IEvent;

namespace ES
{
	//basic types
	using String = std::string;

	//ref types
	template<typename T>
	using SharedPtr = std::shared_ptr<T>;

	template<typename T>
	using Ptr = T*;

	/*template <typename T>
	class Ref
	{
	public:
		Ref(T* ptr = nullptr) : _ptr(ptr) {}
		Ref(const Ref& other) { _ptr = other.get(); }
		void operator = (const Ref<T> other) { _ptr = other.get(); }
		T* operator -> () const { return _ptr; }
		T* get() const { return _ptr; }
		bool operator == (Ref<T> other) const { return _ptr == other.get(); }
		bool operator != (Ref<T> other) const { return _ptr != other.get(); }
	private:
		T* _ptr;		
	};*/

	using EventRef = SharedPtr<IEvent>;
	using EventConstRef = SharedPtr<const IEvent>;

	using StateMachineRef = Ptr<IStateMachine>;
	using StateMachineConstRef = Ptr<const IStateMachine>;

	//ThreadSafeQueue types
	using MessageQueueType = ThreadSafeQueue<EventRef>;
	using PoolQueueType = ThreadSafeQueue<StateMachineRef>;

	

	



}

#endif