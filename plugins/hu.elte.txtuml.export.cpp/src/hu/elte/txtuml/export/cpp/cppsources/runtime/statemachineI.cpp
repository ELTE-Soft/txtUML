#include "statemachineI.hpp"
#include "threadpool.hpp"


 StateMachineI::StateMachineI(std::shared_ptr<MessageQueueType> messageQueue_)
                :_runtime(nullptr),_messageQueue(messageQueue_),_pool(nullptr),_inPool(false){}


void StateMachineI::runSM()
  {
    for(;;)
    {
      processEventVirtual();
    }
  }

void StateMachineI::send(EventPtr e_)
{
  if(_pool)
  {
    handlePool();
  }
  _messageQueue->push_back(e_);

}

void StateMachineI::handlePool()
{
  std::unique_lock<std::mutex> mlock(_mutex);
  if(!_inPool)
  {
    _inPool=true;
    _pool->enqueObject(this);
  }
}
