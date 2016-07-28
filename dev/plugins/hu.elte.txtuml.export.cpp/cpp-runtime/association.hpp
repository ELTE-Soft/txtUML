#ifndef ASSOCIATION_H
#define ASSOCIATION_H

#include <list>

namespace action 
{
	template<typename T, typename FirstRole, typename SecondRole>
	void link(typename FirstRole::EdgeType* e1, typename SecondRole::EdgeType* e2)
	{
		e1->template link<T,SecondRole>(e2);
		e2->template link<T,FirstRole>(e1);
	}
	
	template<typename T, typename FirstRole, typename SecondRole>
	void unlink(typename FirstRole::EdgeType* e1, typename SecondRole::EdgeType* e2)
	{
		e1->template link<T,SecondRole>(e2);
		e2->template link<T,FirstRole>(e1);
	}
}

template<typename End1Type, typename End2Type>
struct Association
{
	typedef End1Type E1;
	typedef End2Type E2;
};

template <typename T>
class AssociationEnd
{

public:

    AssociationEnd(int lower_, int upper_): limits(lower_,upper_) {}

    void addAssoc(T* o)
    {
        if ((int)linkedObjects.size() < limits.second || limits.second == AssociationEnd::UNLIMITED())
        {
            linkedObjects.push_back(o);
        }
    }

    void removeAssoc(T* o)
    {
        linkedObjects.remove(o);
    }

    T* getOne(bool cond(T*) = [](T*){return true;})
    {
        T* object = nullptr;
        for(T* e : linkedObjects)
        {
            if(cond(e))
            {
                object = e;
            }
        }
        return object;
    }

    std::list<T*> getAll(bool cond(T*) = [](T*){return true;})
    {
        std::list<T*> conditionedObjects;
        for(T* e : linkedObjects)
        {
            if(cond(e))
            {
                conditionedObjects.push_back(e);
            }
        }
        return conditionedObjects;
    }

private:

    std::list<T*> linkedObjects;
    std::pair<int,int> limits; //lower, upper
	
	static int UNLIMITED() {return -1;}
};

#endif // ASSOCIATION_H
