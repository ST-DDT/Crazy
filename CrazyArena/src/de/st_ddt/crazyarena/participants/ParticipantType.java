package de.st_ddt.crazyarena.participants;

public enum ParticipantType
{
	SELECTING(true, false, false, false), READY(true, true, false, false), PARTICIPANT(false, true, false, false), DEFEADED(false, false, false, true), RESPAWNING(false, true, false, true), SPECTATOR, JUDGE(false), QUITEDPARTICIPANT(false, false, true, false), QUITEDJUDGE(true);

	public final static ParticipantType[] SPECTATORTYPES = new ParticipantType[] { SPECTATOR };
	public final static ParticipantType[] PLAYERTYPES = new ParticipantType[] { SELECTING, READY, PARTICIPANT, DEFEADED, RESPAWNING, QUITEDPARTICIPANT };
	public final static ParticipantType[] JUDGETYPES = new ParticipantType[] { JUDGE, QUITEDJUDGE };
	private final boolean player;
	private final boolean waiting;
	private final boolean playing;
	private final boolean judge;
	private final boolean quited;
	private final boolean dead;

	// SPECTATOR
	private ParticipantType()
	{
		this.player = false;
		this.waiting = false;
		this.playing = false;
		this.judge = false;
		this.quited = false;
		this.dead = false;
	}

	// PLAYER
	private ParticipantType(final boolean waiting, final boolean playing, final boolean quited, final boolean dead)
	{
		this.player = true;
		this.waiting = waiting;
		this.playing = playing;
		this.judge = false;
		this.quited = quited;
		this.dead = dead;
	}

	// JUDGE
	private ParticipantType(final boolean quited)
	{
		this.player = false;
		this.waiting = false;
		this.playing = false;
		this.judge = true;
		this.quited = quited;
		this.dead = false;
	}

	public boolean isPlayer()
	{
		return player;
	}

	public boolean isWaiting()
	{
		return waiting;
	}

	public boolean isPlaying()
	{
		return playing;
	}

	public boolean isJudge()
	{
		return judge;
	}

	public boolean isQuited()
	{
		return quited;
	}

	public boolean isDead()
	{
		return dead;
	}
}
