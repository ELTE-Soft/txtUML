#ifndef THREAD_CONTAINER_HPP_INCLUDED
#define THREAD_CONTAINER_HPP_INCLUDED

#include <atomic>
#include <thread>
#include <map>
#include <mutex>
#include <condition_variable>
#include <assert.h>

#include "ESRoot/Types.hpp"
namespace Execution 
{

	class EventProcessorThread
	{
	public:
		typedef std::thread::id ThreadId;
	public:
		enum class ThreadState
		{
			ProcessingEvent,
			ReadyToStop
		};

	public:
		EventProcessorThread() {}
		EventProcessorThread(std::thread* thread_) : _thread(thread_), _state(ThreadState::ProcessingEvent) {}
		EventProcessorThread(EventProcessorThread&& e)
		{
			_thread = e._thread;
			_state = e._state; ;
			e._thread = nullptr;
		}
		virtual ~EventProcessorThread()
		{
			if (_thread != nullptr)
			{
				waitFinishing();
				delete _thread;
			}
		}
		bool isWorking() {

			return _state == ThreadState::ProcessingEvent;
		}

		void waitFinishing() {

			assert(!threadsAreIdentical()); //deadlock situatuion

			if (_thread->joinable()) {
				_thread->join();
			}

		}

		bool threadsAreIdentical() {
			ThreadId thisThreadId = std::this_thread::get_id();
			ThreadId processorId = _thread->get_id();

			return thisThreadId == processorId;

		}

		void modifyState(ThreadState state) {
			_state = state;
		}
private:
	std::thread * _thread;
	ThreadState _state;

private:
	EventProcessorThread& operator=(EventProcessorThread&&) = delete;
	EventProcessorThread(const EventProcessorThread&) = delete;
	EventProcessorThread& operator=(const EventProcessorThread&) = delete;


};

class ThreadContainer
{

public:
	ThreadContainer();
	~ThreadContainer();

	void addThread(std::thread*);
	void removeAll();
	void gettingThreadsReadyToStop(ES::SharedPtr<std::condition_variable> cond);
	bool isReadyToStop(EventProcessorThread::ThreadId id);

	void setExpectedThreads(int e) { expected_threads = e; }
	bool isTooManyWorkers() { return (expected_threads < active_threads); }
	bool isTooFewWorkers() { return (expected_threads > active_threads); }



private:
	void modifyThreadState(EventProcessorThread::ThreadId id, EventProcessorThread::ThreadState state);

	std::atomic_int active_threads; // number of active threads;
	std::atomic_int expected_threads; // how many threads should be..
	std::map<std::thread::id, EventProcessorThread> threads;

	std::mutex _mutex;



};

typedef std::map<std::thread::id, EventProcessorThread>::iterator cont_it;

}




#endif // THREAD_CONTAINER_HPP
