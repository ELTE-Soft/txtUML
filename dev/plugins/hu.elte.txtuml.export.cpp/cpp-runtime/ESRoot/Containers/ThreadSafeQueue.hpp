#ifndef THREADSAFE_queueH_INCLUDED
#define THREADSAFE_queueH_INCLUDED

#include <queue>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <atomic>
#include <assert.h>

namespace ES
{

template <typename QueueType>
class ThreadSafeQueue
{
public:
	typedef typename QueueType::size_type  SizeType;
	typedef typename QueueType::value_type ValueType;

	ThreadSafeQueue(std::shared_ptr<std::condition_variable> cond = std::shared_ptr<std::condition_variable>(new std::condition_variable())) : 
		  _cond(cond), 
		  _stop(false) {

	}

	void enqueue(const ValueType& item)
	{
	  std::unique_lock<std::mutex> mlock(_mutex);
	  _queue.push(item);
	  mlock.unlock();
	  _cond->notify_one();
	}

	void dequeue(ValueType& ret)
	{
	  std::unique_lock<std::mutex> mlock(_mutex);
	  while (_queue.empty() && !_stop)
	  {
	    _cond->wait(mlock);
	  }

	  if (!_stop)
	  {
			ret = _queue.front();
			_queue.pop();
	  }

	}

	ValueType next()
	{
		assert(!isEmpty());
		return _queue.front();
	}

	bool isEmpty() const
	{
		return _queue.empty();
	}

	SizeType size()
	{
		return _queue.size();
	}

	void startQueue ()
	{
		_stop = false;
	
	}

	void stopQueue ()
	{
		_stop = true;
		_cond->notify_all ();
	}

	void modifyElements(std::function<bool(const ValueType&)> p, void m(ValueType&)) {
		std::unique_lock<std::mutex> mlock(_mutex);
		_queue.modifyElements(p,m);
	}

	ThreadSafeQueue(const ThreadSafeQueue&) = delete;            // disable copying
	ThreadSafeQueue& operator=(const ThreadSafeQueue&) = delete; // disable assignment

private:
	QueueType _queue;

	std::mutex _mutex;
	std::shared_ptr<std::condition_variable> _cond;
	std::atomic_bool _stop;
};



	
}

#endif // THREADSAFE_queueH_INCLUDED
