#include "Env.hpp"
#include "deployment.hpp"
#include "init_maps.hpp"

namespace Env
{
	void initEnvironment()
	{
		deployment::initRuntime();
		Model::initTransitionTables();
	}
}