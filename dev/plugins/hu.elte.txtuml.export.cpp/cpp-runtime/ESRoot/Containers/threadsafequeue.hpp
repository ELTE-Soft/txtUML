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

template <typename T>
class ThreadSafeQueue
{
public:
  typedef typename std::queue<T>::size_type size_type;

  void enqueue(const T& item)
  {
    std::unique_lock<std::mutex> mlock(_mutex);
    _queue.push(item);
    mlock.unlock();
    _cond.notify_one();
  }

   void dequeue(T& ret)
  {
    std::unique_lock<std::mutex> mlock(_mutex);
    while (_queue.empty() && !_stop)
    {
      _cond.wait(mlock);
    }

    if (!_stop)
    {
    ret=_queue.front();
    _queue.pop();
    }

  }

	T next()
	{
		assert(!isEmpty());
		return _queue.front();
	}

  bool isEmpty() const
  {
    return _queue.empty();
  }

  size_t size()
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
	_cond.notify_all ();
  }

  ThreadSafeQueue() : _stop(false) {}
  ThreadSafeQueue(const ThreadSafeQueue&) = delete;            // disable copying
  ThreadSafeQueue& operator=(const ThreadSafeQueue&) = delete; // disable assignment

 private:
  std::queue<T> _queue;
  std::mutex _mutex;
  std::condition_variable _cond;
  std::atomic_bool _stop;
};
	
}

#endif // THREADSAFE_queueH_INCLUDED
