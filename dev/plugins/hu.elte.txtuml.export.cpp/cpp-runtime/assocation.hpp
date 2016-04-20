#ifndef ASSOCATION_H
#define ASSOCATION_H

#include <list>

namespace action 
{
    template<typename E1, typename E2>
    void link(E1* e1, E2* e2)
    {
        e1->link<E2>(e2);
        e2->link<E1>(e1);
    }
	
	template<typename E1, typename E2>
    void unlink(E1* e1, E2* e2)
    {
        e1->unlink<E2>(e2);
        e2->unlink<E1>(e1);
    }
}

enum Multiplicity {One, Many, Some};

template <typename T>
class Association
{

public:

    Association(Multiplicity multiplicity_): multiplicity(multiplicity_) {}

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

        if(object == nullptr)
        {
            //TODO sign error
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
