#include <iostream>
#include <thread>

#include "Env.hpp"
#include "deployment.hpp"
#include "associations.hpp"
#include "Action.hpp"
#include "event.hpp"
#include "Gearbox.hpp"
#include "Engine.hpp"
#include "Lamp.hpp"

int main()
{
	Env::initEnvironment();
	UsedRuntimePtr rt = UsedRuntimeType::getRuntimeInstance();
	rt->startRT();

	Model::Gearbox g;
	Model::Engine e;
	Model::Lamp l;
	Action::link<typename Model::GE::g, typename Model::GE::e>(&g, &e);
	Action::link<typename Model::GL::g, typename Model::GL::l>(&g, &l);
	Action::link<typename Model::LE::l, typename Model::LE::e>(&l, &e);
	Action::start(&g);
	Action::start(&e);
	Action::start(&l);

	int time = 50;
	for (int i = 0; i < 3; i++)
	{
		std::this_thread::sleep_for(std::chrono::milliseconds(time));
		Action::log("");
		Action::send(&l, ES::SharedPtr<Model::SwitchLight_EC>(new Model::SwitchLight_EC()));
	}

	std::this_thread::sleep_for(std::chrono::milliseconds(2 * time));

	for (int i = 0; i < 3; i++)
	{
		std::this_thread::sleep_for(std::chrono::milliseconds(3 * time));
		Action::log("");
		Action::send(&g, ES::SharedPtr<Model::Forward_EC>(new Model::Forward_EC()));

		std::this_thread::sleep_for(std::chrono::milliseconds(time));
		Action::log("");
		Action::send(&g, ES::SharedPtr<Model::Backward_EC>(new Model::Backward_EC()));
	}

	rt->stopUponCompletion();

	std::cin.get();
	return 0;
}
