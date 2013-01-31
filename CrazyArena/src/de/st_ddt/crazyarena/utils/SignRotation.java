package de.st_ddt.crazyarena.utils;

import java.util.Arrays;

import org.bukkit.util.Vector;

public enum SignRotation
{
	NORTH(3, new Vector(0, 0, -1), new Vector(1, 0, 0)),
	EAST(4, new Vector(1, 0, 0), new Vector(0, 0, 1)),
	SOUTH(2, new Vector(0, 0, 1), new Vector(-1, 0, 0)),
	WEST(5, new Vector(-1, 0, 0), new Vector(0, 0, -1));

	private final byte direction;
	private final Vector vector;
	private final Vector textVector;

	private SignRotation(final int direction, final Vector vector, final Vector textVector)
	{
		this((byte) direction, vector, textVector);
	}

	private SignRotation(final byte direction, final Vector vector, final Vector textVector)
	{
		this.direction = direction;
		this.vector = vector;
		this.textVector = textVector;
	}

	private final static SignRotation[] rotations = new SignRotation[16];
	static
	{
		Arrays.fill(rotations, null);
		for (final SignRotation rotation : values())
			rotations[rotation.getDirection()] = rotation;
	}

	public static SignRotation getByBytes(final byte bytes)
	{
		return rotations[bytes];
	}

	public byte getDirection()
	{
		return direction;
	}

	public Vector getVector()
	{
		return vector.clone();
	}

	public Vector getTextVector()
	{
		return textVector.clone();
	}
}
