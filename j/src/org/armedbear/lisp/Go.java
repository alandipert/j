/*
 * Go.java
 *
 * Copyright (C) 2003 Peter Graves
 * $Id: Go.java,v 1.1 2003-02-19 17:45:06 piso Exp $
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

package org.armedbear.lisp;

public final class Go extends LispError
{
    private final LispObject tag;

    public Go(LispObject tag)
    {
        this.tag = tag;
    }

    public LispObject getTag()
    {
        return tag;
    }

    public String getMessage()
    {
        StringBuffer sb = new StringBuffer("no tag named ");
        sb.append(tag);
        sb.append(" is currently visible");
        return sb.toString();
    }
}
