#ifndef ASSOCIATION_H
#define ASSOCIATION_H

#include <list>

template<typename End1Type, typename End2Type>
struct Association
{
	typedef End1Type E1;
	typedef End2Type E2;
};

template <typename EndType>
class AssociationEnd
{
public:

    AssociationEnd(int lower, int upper): limits(lower,upper) {}

    void addAssoc(EndType* o)
    {
        if ((int)linkedObjects.size() < limits.second || limits.second == AssociationEnd::UNLIMITED())
        {
            linkedObjects.push_back(o);
        }
    }

    void removeAssoc(EndType* o)
    {
        linkedObjects.remove(o);
    }

    EndType* getOne(bool cond(EndType*) = [](EndType*){return true;})
    {
        EndType* object = nullptr;
        for(EndType* e : linkedObjects)
        {
            if(cond(e))
            {
                object = e;
            }
        }
        return object;
    }

    std::list<EndType*> getAll(bool cond(EndType*) = [](EndType*){return true;})
    {
        std::list<EndType*> conditionedObjects;
        for(EndType* e : linkedObjects)
        {
            if(cond(e))
            {
                conditionedObjects.push_back(e);
            }
        }
        return conditionedObjects;
    }

private:

    std::list<EndType*> linkedObjects;
    std::pair<int,int> limits; //lower, upper
	
	static int UNLIMITED() {return -1;}
};

#endif // ASSOCIATION_H
