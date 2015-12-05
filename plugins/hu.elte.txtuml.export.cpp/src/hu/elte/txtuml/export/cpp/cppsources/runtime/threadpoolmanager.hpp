#ifndef THEAD_POOL_MANAGER_H
#define THEAD_POOL_MANAGER_H

#include <map>
#include <list>
#include <math.h>

#include "threadpool.hpp"
#include "runtimetypes.hpp"

class ThreadPoolManager
{
	struct LinearFunction{
		public:
			LinearFunction(double gradient_,int constant_): gradient(gradient_),constant(constant_) {}
			int operator()(int n) {return  round(gradient*n + constant);} 
		private:
			double gradient;
			int constant;
	};
	
	
	public:
		ThreadPoolManager();
		~ThreadPoolManager()
		{
			for (std::map<id_type, StateMachineThreadPool*>::iterator it = id_matching_map.begin();  it != id_matching_map.end();  it++)
			{
				delete it->second;
			}

			for (std::map<id_type, LinearFunction*>::iterator it = function_matching_map.begin(); it!=  function_matching_map.end(); it++)
			{
				delete it->second;
			}

			
		}
		StateMachineThreadPool* get_pool(id_type);
		void recalculateThreads(id_type id,int n)
		{
			LinearFunction function = *(function_matching_map[id]);
			if (function(n) > maximum_thread_map.at(id_type) ){
				id_matching_map[id]->modifiedThreads(max);
			}
			else{
				id_matching_map[id]->modifiedThreads(function(n));
			}

		}
		
		void enqueObject(StateMachineI* sm)
		{
			id_type object_id = sm->getPoolId();
			id_matching_map[object_id]->enqueObject(sm);
		}
		
		std::list<id_type> get_idies()
		{
			std::list<id_type> idies;
			for(std::map<id_type,StateMachineThreadPool*>::iterator it = id_matching_map.begin(); it != id_matching_map.end(); it++ )
			{
				idies.push_back(it->first);
			}
			
			return idies;
		}
	
	private:
	
		std::map<id_type,StateMachineThreadPool*> id_matching_map;
		std::map<id_type,LinearFunction*>  function_matching_map;
		std::map<id_type,int> maximum_thread_map;
		unsigned int number_of_pools;
	
};

inline StateMachineThreadPool* ThreadPoolManager::get_pool(id_type id)
{
	return id_matching_map[id];
}

#endif // THEAD_POOL_MANAGER_H
