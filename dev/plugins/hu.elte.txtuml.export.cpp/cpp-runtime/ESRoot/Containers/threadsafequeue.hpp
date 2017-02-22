#ifndef THREADSAFEQUEUE_H_INCLUDED
#define THREADSAFEQUEUE_H_INCLUDED

#include <queue>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <atomic>

namespace ES
{

template <typename T>
class ThreadSafeQueue
{
public:
  typedef typename std::queue<T>::size_type size_type;

  void enqueue(const T& item)
  {
    std::unique_lock<std::mutex> mlock(mutex_);
    queue_.push(item);
    mlock.unlock();
    _cond.notify_one();
  }

   void dequeue(T& ret)
  {
    std::unique_lock<std::mutex> mlock(mutex_);
    while (queue_.empty() && !_stop)
    {
      _cond.wait(mlock);
    }

    if (!_stop)
    {
    ret=queue_.front();
    queue_.pop();
    }

  }

  bool isEmpty() const
  {
    return queue_.empty();
  }

  size_t size()
  {
    return queue_.size();
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
  std::queue<T> queue_;
  std::mutex mutex_;
  std::condition_variable _cond;
  std::atomic_bool _stop;
};
	
}

#endif // THREADSAFEQUEUE_H_INCLUDED
