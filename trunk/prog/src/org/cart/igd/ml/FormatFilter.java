package org.cart.igd.ml;

import java.io.File;
import java.io.FilenameFilter;

public class FormatFilter implements FilenameFilter
{
	private String formatSuffix;

	public FormatFilter(String formatSuffix)
	{
		this.formatSuffix = formatSuffix;
	}

	public boolean accept(File dir, String name)
	{
		return name.endsWith(formatSuffix);
	}
}