#ifndef ASSOCATION_H
#define ASSOCATION_H

#include <list>

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

    T* getOne(bool cond(T*) = []{return true;})
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
        return o;
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
