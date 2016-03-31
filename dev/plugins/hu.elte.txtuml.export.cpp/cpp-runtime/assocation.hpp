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

    T* getOne(bool cond(T*) = [](T* o){return true;})
    {
        T* object = nullptr;
        for(T* o : linkedObjects)
        {
            if(cond(o))
            {
                object = o;
            }
        }

        if(object == nullptr)
        {
            //TODO sign error
        }
        return object;
    }

    std::list<T*> getAll()
    {
        return linkedObjects;
    }

private:

    std::list<T*> linkedObjects;
    Multiplicity multiplicity;
};

#endif // ASSOCATION_H
