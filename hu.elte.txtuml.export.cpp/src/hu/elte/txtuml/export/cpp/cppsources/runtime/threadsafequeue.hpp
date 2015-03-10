#ifndef THREADSAFEQUEUE_H_INCLUDED
#define THREADSAFEQUEUE_H_INCLUDED

#include <queue>
#include <thread>
#include <mutex>
#include <condition_variable>

template <typename T>
class ThreadSafeQueue
{
 public:

  T pop()
  {
    std::unique_lock<std::mutex> mlock(mutex_);
    while (queue_.empty())
    {
      cond_.wait(mlock);
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
      cond_.wait(mlock);
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
    cond_.notify_one();
  }

  T front()
  {
    std::unique_lock<std::mutex> mlock(mutex_);
    while (queue_.empty())
    {
      cond_.wait(mlock);
    }
    auto val = queue_.front();
    return val;
  }

  void pop_front()
  {
    std::unique_lock<std::mutex> mlock(mutex_);
    while (queue_.empty())
    {
      cond_.wait(mlock);
    }
    queue_.pop();
  }

   void pop_front(T& ret)
  {
    std::unique_lock<std::mutex> mlock(mutex_);
    while (queue_.empty())
    {
      cond_.wait(mlock);
    }
    ret=queue_.front();
    queue_.pop();
  }

  bool empty() const
  {
    return queue_.empty();
  }

  size_t size()
  {
    return queue_.size();
  }

  ThreadSafeQueue()=default;
  ThreadSafeQueue(const ThreadSafeQueue&) = delete;            // disable copying
  ThreadSafeQueue& operator=(const ThreadSafeQueue&) = delete; // disable assignment

 private:
  std::queue<T> queue_;
  std::mutex mutex_;
  std::condition_variable cond_;
};

#endif // THREADSAFEQUEUE_H_INCLUDED
