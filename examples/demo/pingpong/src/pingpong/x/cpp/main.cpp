#include <iostream>

#include "Env.hpp"
#include "deployment.hpp"
#include "runtime/Action.hpp"
#include "EventStructures.hpp"
#include "Game.hpp"

int main()
{
  Env::initEnvironment();
  UsedRuntimePtr rt = UsedRuntimeType::getRuntimeInstance();

  Model::Game game;
  Action::start (&game);
  Action::send (&game, Model::BallPtr(new Model::Ball_EC (4)));

  rt->startRT();
  return 0;
}
