#ifndef ASSOCATION_H
#define ASSOCATION_H

#include <list>

namespace action 
{
	template<typename T, typename FirstRole, typename SecondRole>
	void link(typename FirstRole::EdgeType* e1, typename SecondRole::EdgeType* e2)
	{
		e1->link<T,SecondRole>(e2);
		e2->link<T,FirstRole>(e1);
	}
	
	template<typename T, typename FirstRole, typename SecondRole>
	void unlink(typename FirstRole::EdgeType* e1, typename SecondRole::EdgeType* e2)
	{
		e1->link<T,SecondRole>(e2);
		e2->link<T,FirstRole>(e1);
	}
}

enum Multiplicity {One, Many, Some};

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

    AssociationEnd(Multiplicity multiplicity_): multiplicity(multiplicity_) {}

    void addAssoc(T* o)
    {
        if (multiplicity != Multiplicity::One || (linkedObjects.size() == 0))
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
    Multiplicity multiplicity;
};

#endif // ASSOCATION_H
