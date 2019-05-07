package pingpong.j.tests;

import static hu.elte.txtuml.api.model.seqdiag.Sequence.assertState;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.seqdiag.ExecMode;
import hu.elte.txtuml.api.model.seqdiag.ExecutionMode;
import hu.elte.txtuml.api.model.seqdiag.Lifeline;
import hu.elte.txtuml.api.model.seqdiag.Position;
import hu.elte.txtuml.api.model.seqdiag.Proxy;
import hu.elte.txtuml.api.model.seqdiag.Sequence;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import pingpong.j.model.Game;
import pingpong.j.model.Player;
import pingpong.j.model.Racket;
import pingpong.j.model.signals.Ball;

public class PingPongSequenceDiagram extends SequenceDiagram {

	Game game;

	@Position(1)
	Proxy<Racket> r1;

	@Position(2)
	Proxy<Racket> r2;

	@Override
	public void initialize() {
		game = Action.create(Game.class);
		Action.start(game);
	}

	@Override
	@ExecutionMode(ExecMode.LENIENT)
	public void run() {
		Lifeline<Game> g = Sequence.createLifeline(game);

		Proxy<Player> p1 = Sequence.createProxy(Player.class);
		r1 = Sequence.createProxy(Racket.class);
		r2 = Sequence.createProxy(Racket.class);

		Sequence.fromActor(new Ball(4), g);
		Sequence.assertSend(g, new Ball(4), p1);

		Sequence.assertSend(r1, new Ball(4), r2);
		Sequence.assertSend(r2, new Ball(3), r1);
		Sequence.assertSend(r1, new Ball(2), r2);
		Sequence.assertSend(r2, new Ball(1), r1);
		Sequence.assertSend(r1, new Ball(0), r2);
	}

}
