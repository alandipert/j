/*
 * input_stream_p.java
 *
 * Copyright (C) 2004 Peter Graves
 * $Id: input_stream_p.java,v 1.2 2004-01-16 16:31:57 piso Exp $
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

// ### input-stream-p
public final class input_stream_p extends Primitive1
{
    private input_stream_p()
    {
        super("input-stream-p");
    }

    public LispObject execute(LispObject arg) throws ConditionThrowable
    {
        if (arg instanceof LispInputStream)
            return T;
        if (arg instanceof TwoWayStream)
            return T;
        if (arg instanceof LispStream)
            return NIL;
        return signal(new TypeError(arg, Symbol.STREAM));
    }

    private static final Primitive1 INPUT_STREAM_P = new input_stream_p();
}
