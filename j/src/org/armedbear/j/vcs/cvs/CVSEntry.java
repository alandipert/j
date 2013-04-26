/*
 * CVSEntry.java
 *
 * Copyright (C) 2002 Peter Graves
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.armedbear.j.vcs.cvs;

import org.armedbear.j.Buffer;
import org.armedbear.j.Constants;
import java.lang.StringBuilder;
import org.armedbear.j.File;
import org.armedbear.j.Line;
import org.armedbear.j.Log;
import org.armedbear.j.SystemBuffer;
import org.armedbear.j.vcs.VersionControlEntry;

import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.TimeZone;

public final class CVSEntry extends VersionControlEntry
{
    private long checkoutTime;

    private CVSEntry(Buffer buffer, String revision, long checkoutTime)
    {
        super(buffer, revision);
        this.checkoutTime = checkoutTime;
    }

    public long getCheckoutTime() {
        return checkoutTime;
    }

    public int getVersionControl() {
        return Constants.VC_CVS;
    }

    public String getStatusText() {
        return statusText(true);
    }

    public String getLongStatusText() {
        return statusText(false);
    }

    private String statusText(boolean brief)
    {
        StringBuilder sb = new StringBuilder("CVS");
        final String revision = getRevision();
        if (revision.equals("0")) {
            sb.append(brief ? " A" : " (locally added)");
        } else {
            if (!brief)
                sb.append(" revision ");
            sb.append(revision);
            final long lastModified = buffer.getLastModified();
            final long checkout = getCheckoutTime();
            if (lastModified != checkout) {
                if (Math.abs(lastModified - checkout) >= 1000)
                    sb.append(brief ? " M" : " (locally modified)");
            }
        }
        return sb.toString();
    }

    public static CVSEntry getEntry(Buffer buffer)
    {
        final File file = buffer.getFile();
        final String text = getEntryText(file);
        if (text != null) {
            String revision = null;
            long checkoutTime = 0;
            StringTokenizer st = new StringTokenizer(text, "/");
            if (st.hasMoreTokens()) {
                // Ignore first token (filename).
                st.nextToken();
            }
            if (st.hasMoreTokens())
                revision = st.nextToken();
            String timeString = null;
            if (st.hasMoreTokens())
                timeString = st.nextToken();
            if (timeString == null || timeString.length() == 0 ||
                timeString.equals("dummy timestamp") ||
                timeString.equals("Result of merge"))
                return new CVSEntry(buffer, revision, 0);
            st = new StringTokenizer(timeString, " :");
            try {
                // Ignore first token (day of week).
                st.nextToken();
                String monthName = st.nextToken();
                String months =
                    "JanFebMarAprMayJunJulAugSepOctNovDec";
                // Month is zero-based.
                int month = months.indexOf(monthName) / 3;
                int dayOfMonth = Integer.parseInt(st.nextToken());
                int hour = Integer.parseInt(st.nextToken());
                int minute = Integer.parseInt(st.nextToken());
                int second = Integer.parseInt(st.nextToken());
                int year = Integer.parseInt(st.nextToken());
                Calendar cal = Calendar.getInstance();
                cal.setTimeZone(TimeZone.getTimeZone("GMT+0000"));
                cal.set(year, month, dayOfMonth, hour, minute);
                cal.set(Calendar.SECOND, second);
                cal.set(Calendar.MILLISECOND, 0);
                checkoutTime = cal.getTime().getTime();
            }
            catch (NoSuchElementException e) {}
            catch (NumberFormatException ex) {
                Log.error("CVS.getEntry NumberFormatException");
                Log.error("text = |" + text + "|");
            }
            if (revision != null && revision.length() > 0)
                return new CVSEntry(buffer, revision, checkoutTime);
        }
        return null;
    }

    private static String getEntryText(File file)
    {
        if (file == null)
            return null;
        if (file.isRemote())
            return null;
        File parentDir = file.getParentFile();
        if (parentDir == null)
            return null;
        File cvsDir = File.getInstance(parentDir, "CVS");
        if (cvsDir == null || !cvsDir.isDirectory())
            return null;
        File entriesFile = File.getInstance(cvsDir, "Entries");
        if (entriesFile == null || !entriesFile.isFile())
            return null;
        String lookFor = "/".concat(file.getName()).concat("/");
        SystemBuffer buf = new SystemBuffer(entriesFile);
        buf.load();
        for (Line line = buf.getFirstLine(); line != null; line = line.next()) {
            String entry = line.getText();
            if (entry.startsWith(lookFor))
                return entry;
        }
        return null;
    }
}
