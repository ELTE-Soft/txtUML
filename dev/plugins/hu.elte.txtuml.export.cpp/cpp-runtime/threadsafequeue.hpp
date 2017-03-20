#ifndef THREADSAFEQUEUE_H_INCLUDED
#define THREADSAFEQUEUE_H_INCLUDED

#include <queue>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <atomic>

template <typename T>
class ThreadSafeQueue
{
 public:

  T pop()
  {
    std::unique_lock<std::mutex> mlock(mutex_);
    while (queue_.empty())
    {
      _cond.wait(mlock);
    }
    auto val = queue_.front();
    queue_.pop();
    return val;
  }

  void pop(T& item)
  {
    std::unique_lock<std::mutex> mlock(mutex_);
    while (queue_.empty())
    {
      _cond.wait(mlock);
    }
    item = queue_.front();
    queue_.pop();
  }

  /*Boost Meta state MAchine Interface functions:
     pop_front
     empty // most be true, because of the msn backend event processing (recursion leads to memory depletion, in execute_queued_events,and process_events)
     front
     push_back
     size_type typedef;
  */

  typedef typename std::queue<T>::size_type size_type;


  void push_back(const T& item)
  {
    std::unique_lock<std::mutex> mlock(mutex_);
    queue_.push(item);
    mlock.unlock();
    _cond.notify_one();
  }

  T front()
  {
    std::unique_lock<std::mutex> mlock(mutex_);
    while (queue_.empty())
    {
      _cond.wait(mlock);
    }
    auto val = queue_.front();
    return val;
  }

  void pop_front()
  {
    std::unique_lock<std::mutex> mlock(mutex_);
    while (queue_.empty())
    {
      _cond.wait(mlock);
    }
    queue_.pop();
  }

   void pop_front(T& ret)
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

  bool empty() const
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

#endif // THREADSAFEQUEUE_H_INCLUDED
