#include "threadcontainer.hpp"
#include <algorithm>


namespace Execution
{

ThreadContainer::ThreadContainer() : _activeThreads(0), _expectedThreads(0) {}

void ThreadContainer::addThread(std::thread* th)
{
	std::unique_lock<std::mutex> mlock(_mutex);

	threads.insert(std::pair<std::thread::id, EventProcessorThread>(th->get_id(), EventProcessorThread(th)));
	_activeThreads++;
}



void ThreadContainer::gettingThreadsReadyToStop(ES::SharedPtr<std::condition_variable> cond)
{
	std::unique_lock<std::mutex> mlock(_removeMutex);
	ContainerIterator it = threads.begin();
	
	while (isTooManyWorkers() && it != threads.end())
	{
		if (it->second.isWorking() && !it->second.threadsAreIdentical())
		{
			ContainerIterator it2;
			_activeThreads--;
			modifyThreadState(it->first, EventProcessorThread::ThreadState::ReadyToStop);
			cond->notify_all();
			it2 = it;
			it++;
			it2->second.waitFinishing();
			threads.erase(it2);

		}
		else
		{
			it++;
		}
	}

}

void ThreadContainer::modifyThreadState(EventProcessorThread::ThreadId id, EventProcessorThread::ThreadState state)
{
	std::unique_lock<std::mutex> mlock(_mutex);
	threads[id].modifyState(state);
}

bool ThreadContainer::isReadyToStop(EventProcessorThread::ThreadId thread_id)
{
	std::unique_lock<std::mutex> mlock(_mutex);
	return threads.count(thread_id) > 0 && !threads[thread_id].isWorking();
}

void ThreadContainer::removeAll()
{
	std::unique_lock<std::mutex> mlock(_removeMutex);
	threads.clear();
}

ThreadContainer::~ThreadContainer()
{
}

}



