#include "threadcontainer.hpp"

ThreadContainer::ThreadContainer(): active_threads(0),expected_threads(0) {}

void ThreadContainer::addThread(std::thread* th)
{
	std::unique_lock<std::mutex> mlock(_mutex);
	
	threads.insert(std::pair<std::thread::id,std::thread*>(th->get_id(),th));
	active_threads = active_threads + 1;
}

void ThreadContainer::removeThread(std::thread::id thread_id)
{
	std::unique_lock<std::mutex> mlock(_mutex);
	
	threads.at(thread_id)->join(); // musn't delete while it is runing..
	
	delete threads.at(thread_id);
	threads.erase(thread_id);
}

void ThreadContainer::reduceActiveThreads()
{
	std::unique_lock<std::mutex> mlock(_mutex);
	active_threads--;
	
}

void ThreadContainer::removeAll()
{
	std::unique_lock<std::mutex> mlock(_mutex);
	
	for(std::map<std::thread::id,std::thread*>::iterator it = threads.begin(); it != threads.end();  it++)
	{
		it->second->join();
		delete it->second;
	}
	threads.clear();
	
}

ThreadContainer::~ThreadContainer()
{
	removeAll();
}
