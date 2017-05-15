#ifndef ATOMIC_COUNTER_HPP
#define ATOMIC_COUNTER_HPP

#include <atomic>

namespace ES
{

class AtomicCounter {

public:
	AtomicCounter() : counter(0) {}
	void incrementCounter(int i = 1) { counter += i; }
	void decrementCounter(int i = 1) { counter -= i; }
	bool isZeroCounter() { return counter == 0; }

private:
	std::atomic_int counter;
};

}


#endif