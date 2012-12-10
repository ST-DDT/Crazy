package de.st_ddt.crazyutil.paramitrisable;

import java.io.File;
import java.io.FileFilter;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class FileParamitrisable extends TypedParamitrisable<File>
{

	protected final static Pattern PATTERN_PATHSPERERATOR = Pattern.compile("//");
	protected final static Pattern PATTERN_SPACE = Pattern.compile(" ");
	private final File root;

	public FileParamitrisable(final File root, final File defaultValue)
	{
		super(defaultValue);
		this.root = root;
	}

	@Override
	public void setParameter(final String parameter) throws CrazyException
	{
		value = new File(root, parameter);
	}

	@Override
	public List<String> tab(final String parameter)
	{
		return tabHelp(root, parameter);
	}

	public static List<String> tabHelp(final File root, final String parameter)
	{
		final String[] split = PATTERN_PATHSPERERATOR.split(parameter);
		final String part = split[split.length - 1].toLowerCase();
		final File temp = new File(root, parameter).getParentFile();
		final String[] outPartSplit = PATTERN_SPACE.split(parameter);
		final String outPart = outPartSplit[outPartSplit.length - 1];
		final FileFilter filter = new FileFilter()
		{

			@Override
			public boolean accept(final File pathname)
			{
				return pathname.getName().toLowerCase().startsWith(part);
			}
		};
		final List<String> res = new LinkedList<String>();
		for (final File file : temp.listFiles(filter))
			res.add(outPart + "//" + file.getName());
		return res;
	}
}
