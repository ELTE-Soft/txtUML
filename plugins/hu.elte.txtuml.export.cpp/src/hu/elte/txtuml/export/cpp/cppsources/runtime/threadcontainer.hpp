#ifndef THREAD_CONTAINER_HPP_INCLUDED
#define THREAD_CONTAINER_HPP_INCLUDED

#include <atomic>
#include <thread>
#include <map>
#include <mutex>

class ThreadContainer
{
	
	public:
		ThreadContainer();
		
		void addThread(std::thread*);
		void removeThread(std::thread::id);
		void removeAll();
		void reduceActiveThreads();
		
		void setExpectedThreads(int e) {expected_threads = e;}
		bool isTooManyWorkes() {return (expected_threads < active_threads);}
		bool isTooFewWorkes() {return (expected_threads > active_threads);}
		
	
	
	private:
	
		std::atomic_int active_threads; // number of active threads;
		std::atomic_int expected_threads; // how many threads should be..
		std::map<std::thread::id,std::thread*> threads;
		
		std::mutex _mutex;
	
	
	
};

#endif // THREAD_CONTAINER_HPP