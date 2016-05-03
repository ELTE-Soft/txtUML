#ifndef THREAD_CONTAINER_HPP_INCLUDED
#define THREAD_CONTAINER_HPP_INCLUDED

#include <atomic>
#include <thread>
#include <map>
#include <mutex>


enum thread_state {working, ready_to_stop, stopped};

struct EventProcessorThread
{
	std::thread* _thread;
	thread_state _state;
	
	EventProcessorThread() {}
	EventProcessorThread(std::thread* thread_): _thread(thread_), _state(working) {}
	EventProcessorThread(EventProcessorThread&& e) {_thread = e._thread; _state = e._state; ;e._thread = nullptr;}
	EventProcessorThread& operator=(EventProcessorThread&&) = delete;
	EventProcessorThread(const EventProcessorThread&) = delete;
	EventProcessorThread& operator=(const EventProcessorThread&) = delete;
   ~EventProcessorThread()
	{
		if (_thread != nullptr)
		{
			
			_thread->join();
			
			delete _thread;
		}
	}
};

class ThreadContainer
{
	
	public:
		ThreadContainer();
		~ThreadContainer();
		
		void addThread(std::thread*);
		void removeAll();
        void gettingThreadsReadyToStop();
		void signStop(std::thread::id);
		bool isReadyToStop(std::thread::id);
		
		void setExpectedThreads(int e) {expected_threads = e;}
		bool isTooManyWorkes() {return (expected_threads < active_threads);}
		bool isTooFewWorkes() {return (expected_threads > active_threads);}
		
	
	
	private:
	
		std::atomic_int active_threads; // number of active threads;
		std::atomic_int expected_threads; // how many threads should be..
		std::map<std::thread::id,EventProcessorThread> threads;
		
		std::mutex _mutex;
	
	
	
};

typedef std::map<std::thread::id,EventProcessorThread>::iterator cont_it;


#endif // THREAD_CONTAINER_HPP
