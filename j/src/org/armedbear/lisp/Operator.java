/*
 * Operator.java
 *
 * Copyright (C) 2003-2005 Peter Graves
 * $Id: Operator.java,v 1.2 2005-04-05 15:34:16 piso Exp $
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

// Common superclass of Function and SpecialOperator.
public abstract class Operator extends LispObject
{
    protected LispObject lambdaName;

    private LispObject arglist;

    public final LispObject getLambdaName()
    {
        return lambdaName;
    }

    public final void setLambdaName(LispObject obj)
    {
        lambdaName = obj;
    }

    public final LispObject getArglist()
    {
        return arglist;
    }

    public final void setArglist(LispObject obj)
    {
        arglist = obj;
    }
}
