/** @file FixedArray.hpp
*/

#ifndef FIXED_ARRAY_HPP

namespace ESContainer
{

/*! Fixed size array for static thread configurations. */

template<typename T>
class FixedArray
{
public:
	FixedArray(): configs(nullptr) {}
	FixedArray(int n) : fixedSize(n)
	{
		configs = new T[n];
	}
	FixedArray(int n, T init) : fixedSize(n)
	{
		configs = new T[n];
		for (int i = 0; i < n; i++)
		{
			configs[i] = init;
		}
	}
	~FixedArray()
	{
		freeArray();
	}

	FixedArray(const FixedArray& c)
	{
		copy(c);
	}

	FixedArray& operator=(const FixedArray& c)
	{
		if (&c != this) {
			freeArray();
			copy(c);
		}

		return *this;
	}

	int getSize() { return fixedSize; }

	T& 	operator[] (int i) { return configs[i]; }
	T 	operator[] (int i) const {return configs[i]; }

private:
	void copy(const FixedArray& c)
	{
		fixedSize = c.fixedSize;
		configs = new T[fixedSize];
		for (int i = 0; i < fixedSize; i++)
		{
			configs[i] = c.configs[i];
		}
	}

	void freeArray() {
		if (configs != nullptr) {
			delete[] configs;
		}
	}

	T * configs;
	int fixedSize;
};


}

#define FIXED_ARRAY_HPP
#endif // !FIXED_ARRAY_HPP



