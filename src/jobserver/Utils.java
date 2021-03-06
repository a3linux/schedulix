/*
Copyright (c) 2000-2013 "independIT Integrative Technologies GmbH",
Authors: Ronald Jeninga, Dieter Stubler

schedulix Enterprise Job Scheduling System

independIT Integrative Technologies GmbH [http://www.independit.de]
mailto:contact@independit.de

This file is part of schedulix

schedulix is free software:
you can redistribute it and/or modify it under the terms of the
GNU Affero General Public License as published by the
Free Software Foundation, either version 3 of the License,
or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package de.independit.scheduler.jobserver;

import java.util.Date;
import java.text.SimpleDateFormat;

import de.independit.scheduler.server.SystemEnvironment;

public class Utils
{
	public static final char TIMESTAMP_LEADIN  = '[';
	public static final char TIMESTAMP_LEADOUT = ']';

	public static final char BOOTTIME_NONE   = 'N';
	public static final char BOOTTIME_SYSTEM = 'S';
	public static final char BOOTTIME_FILE   = 'F';

	public static final char BOOTTIME_DEFAULT =  BOOTTIME_NONE;

	private static final String LIBRARY_NAME = "BICsuite";

	private static final SimpleDateFormat DATE_FORMAT = (SimpleDateFormat) SystemEnvironment.staticJSCommDateFormat.clone();

	private static final Libjni jni;

	static
	{
		jni = new Libjni();
	}

	public static final String getVersion()
	{
		return Libjni.getVersion();
	}

	public static final String getCopyright()
	{
		return Libjni.getCopyright();
	}

	public static final String getCompany()
	{
		return Libjni.getCompany();
	}

	public static final String getProtocol()
	{
		return Libjni.getProtocol();
	}

	public static final void abortProgram (final String msg)
	{
		Trace.fatal ("***ERROR*** " + msg);
		Trace.fatal ("Program aborted");
		System.exit (1);
	}

	public static final void abortProgram (final RepoIface ri, final String msg)
	{
		ri.notifyError (RepoIface.FATAL, msg);
		abortProgram (msg);
	}

	public static final String asGMT (final Date date)
	{
		return TIMESTAMP_LEADIN + DATE_FORMAT.format (date) + TIMESTAMP_LEADOUT;
	}

	public static final String timestampNow()
	{
		return asGMT (new Date());
	}

	public static final void sleep (long millis)
	{
		try {
			Thread.sleep (millis);
		} catch (final InterruptedException ie) {
			Trace.debug("Just been woken up");

		}
	}

	public static final String quoted (final String str)
	{
		final StringBuffer buf = new StringBuffer (str);

		for (int i = buf.length() - 1; i >= 0; --i)
			switch (buf.charAt (i)) {
				case '\'':
				case '\\':
					buf.insert (i, '\\');
			}

		return buf.toString();
	}

	//-------------------------------------------------------------------------
	// Returns if the specified key is in the specified array
	//-------------------------------------------------------------------------

	public static final boolean isOneOf (final String key, final String[] array)
	{
		final int n = array.length;
		for (int i = 0; i < n; ++i)
			if (key.equals (array [i]))
				return true;
		return false;
	}

	//-------------------------------------------------------------------------
	/** Get identification of current process. */
	//-------------------------------------------------------------------------

	public static final String getMyPid()
	{
		return String.valueOf (getPid());
	}

	private static final int getPid()
	{
		return jni.getPid();
	}

	//-------------------------------------------------------------------------
	/** Find out, if the specified pid is (still) active. */
	//-------------------------------------------------------------------------

	public static final boolean isAlive (final RepoIface ri, final String pid)
	{
		return jni.isAlive(pid);
	}

	//-------------------------------------------------------------------------
	/** Return command that writes the system environment to standard output. */
	//-------------------------------------------------------------------------

	public static final String getEnvCmd()
	{
		return jni.getEnvCmd();
	}

	//-------------------------------------------------------------------------
	/** chdir() to the specified path and return null if that was possible, else an error message is returned. */
	//-------------------------------------------------------------------------

	public static final String chdir (final String path)
	{
		return jni.chdir(path);
	}

	//-------------------------------------------------------------------------
	/** Set how to determine time of last boot and return null if that was possible, else an error message is returned. */
	//-------------------------------------------------------------------------

	public static final String setBoottimeHow (final char how)
	{
		if (jni.setBoottimeHow(how)) return null;
		else return "Oops";
	}

	//-------------------------------------------------------------------------
	/** Return how the time of the last boot is determined. */
	//-------------------------------------------------------------------------

	public static final char getBoottimeHow()
	{
		return jni.getBoottimeHow();
	}

	//-------------------------------------------------------------------------
	/** Return a string representing a starting time of a propcess */
	//-------------------------------------------------------------------------

	public static final String getProcessStartTime(long pid)
	{
		return jni.getProcessStartTime(pid);
	}

	//-------------------------------------------------------------------------
	// Don't instantiate!

	private Utils()
	{
	}
}
