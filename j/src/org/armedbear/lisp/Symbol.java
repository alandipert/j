/*
 * Symbol.java
 *
 * Copyright (C) 2002-2003 Peter Graves
 * $Id: Symbol.java,v 1.4 2003-02-15 16:48:17 piso Exp $
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

public final class Symbol extends LispObject
{
    public static final Symbol AND_KEY       = export("&KEY", PACKAGE_CL);
    public static final Symbol AND_OPTIONAL  = export("&OPTIONAL", PACKAGE_CL);
    public static final Symbol AND_REST      = export("&REST", PACKAGE_CL);
    public static final Symbol BLOCK         = export("BLOCK", PACKAGE_CL);
    public static final Symbol LAMBDA        = export("LAMBDA", PACKAGE_CL);
    public static final Symbol LET           = export("LET", PACKAGE_CL);
    public static final Symbol OTHERWISE     = export("OTHERWISE", PACKAGE_CL);
    public static final Symbol QUOTE         = export("QUOTE", PACKAGE_CL);

    // Type specifiers.
    public static final Symbol CONS          = export("CONS", PACKAGE_CL);
    public static final Symbol CHARACTER     = export("CHARACTER", PACKAGE_CL);
    public static final Symbol ERROR         = export("ERROR", PACKAGE_CL);
    public static final Symbol FIXNUM        = export("FIXNUM", PACKAGE_CL);
    public static final Symbol FUNCTION      = export("FUNCTION", PACKAGE_CL);
    public static final Symbol PACKAGE       = export("PACKAGE", PACKAGE_CL);
    public static final Symbol STRING        = export("STRING", PACKAGE_CL);
    public static final Symbol SYMBOL        = export("SYMBOL", PACKAGE_CL);
    public static final Symbol VECTOR        = export("VECTOR", PACKAGE_CL);

    // Internal symbols.
    public static final Symbol BACKQUOTE     = intern("BACKQUOTE", PACKAGE_CL);
    public static final Symbol COMMA         = intern("COMMA", PACKAGE_CL);
    public static final Symbol COMMA_ATSIGN  = intern("COMMA-ATSIGN", PACKAGE_CL);
    public static final Symbol COMMA_DOT     = intern("COMMA-DOT", PACKAGE_CL);

    public static final Symbol ARRAY_DIMENSION_LIMIT =
        export("ARRAY-DIMENSION-LIMIT", PACKAGE_CL);

    static {
        ARRAY_DIMENSION_LIMIT.setSymbolValue(new Fixnum(0x1000000L));
        ARRAY_DIMENSION_LIMIT.setConstant(true);
    }

    // Bit flags.
    private static final int SPECIAL  = 0x0001;
    private static final int CONSTANT = 0x0002;
    private static final int EXTERNAL = 0x0004;

    public static final void addFunction(String name, LispObject obj)
    {
        export(name, PACKAGE_CL).setSymbolFunction(obj);
    }

    public static final void addFunction(Function function)
    {
        export(function.getName(), PACKAGE_CL).setSymbolFunction(function);
    }

    private final String name;
    private LispObject pkg;
    private LispObject value;
    private LispObject function;
    private LispObject propertyList = NIL;
    private int flags;

    // Construct an uninterned symbol.
    public Symbol(String name)
    {
        // The symbol is uninterned, but its name is an interned String.
        this.name = name.intern();
        pkg = NIL;
    }

    public Symbol(String name, Package pkg)
    {
        this.name = name.intern();
        this.pkg = pkg;
    }

    public LispObject typeOf()
    {
        return Symbol.SYMBOL;
    }

    public LispObject getPackage()
    {
        return pkg;
    }

    public void setPackage(LispObject obj)
    {
        pkg = obj;
    }

    public final int getType()
    {
        return TYPE_SYMBOL;
    }

    public final boolean isInternal()
    {
        return (flags & EXTERNAL) == 0;
    }

    public final boolean isExternal()
    {
        return (flags & EXTERNAL) != 0;
    }

    public final void setExternal(boolean b)
    {
        if (b)
            flags |= EXTERNAL;
        else
            flags &= ~EXTERNAL;
    }

    public final boolean isSpecialVariable()
    {
        return (flags & SPECIAL) != 0;
    }

    public final void setSpecial(boolean b)
    {
        if (b)
            flags |= SPECIAL;
        else
            flags &= ~SPECIAL;
    }

    public final boolean isConstant()
    {
        return (flags & CONSTANT) != 0;
    }

    public final void setConstant(boolean b)
    {
        if (b)
            flags |= CONSTANT;
        else
            flags &= ~CONSTANT;
    }

    public static LispObject getPropertyList(LispObject obj) throws LispError
    {
        try {
            return ((Symbol)obj).propertyList;
        }
        catch (ClassCastException e) {
            throw new WrongTypeException(obj, "symbol");
        }
    }

    public final String getName()
    {
        return name;
    }

    public final String getQualifiedName()
    {
        if (pkg == null)
            return("#:".concat(name));
        if (pkg == PACKAGE_KEYWORD)
            return ":".concat(name);
        StringBuffer sb = new StringBuffer(pkg.getName());
        if (isExternal())
            sb.append(':');
        else
            sb.append("::");
        sb.append(name);
        return sb.toString();
    }

    // Raw accessor.
    public final LispObject getSymbolValue()
    {
        return value;
    }

    public final void setSymbolValue(LispObject value)
    {
        this.value = value;
    }

    // symbol-value
    public final LispObject symbolValue() throws LispError
    {
        if (dynEnv != null) {
            if ((flags & SPECIAL) != 0) {
                LispObject val = dynEnv.lookup(this);
                if (val != null)
                    return val;
            }
        }
        if (value != null)
            return value;
        throw new LispError(toString().concat(" has no dynamic value"));
    }

    public final LispObject symbolValueNoThrow()
    {
        if (dynEnv != null) {
            if ((flags & SPECIAL) != 0) {
                LispObject val = dynEnv.lookup(this);
                if (val != null)
                    return val;
            }
        }
        return value;
    }

    public final LispObject getSymbolFunction()
    {
        return function;
    }

    public final void setSymbolFunction(LispObject obj)
    {
        this.function = obj;
    }

    public final LispObject getPropertyList()
    {
        return propertyList;
    }

    public final void setPropertyList(LispObject obj)
    {
        propertyList = obj;
    }

    public String toString()
    {
        if (pkg == NIL)
            return "#:".concat(name);
        if (pkg == PACKAGE_KEYWORD)
            return ":".concat(name);
        final Package currentPackage = (Package) _PACKAGE_.getSymbolValue();
        if (pkg == currentPackage)
            return name;
        if (currentPackage.uses(pkg) && isExternal())
            return name;
        StringBuffer sb = new StringBuffer(pkg.getName());
        if (isExternal())
            sb.append(':');
        else
            sb.append("::");
        sb.append(name);
        return sb.toString();
    }

    public boolean equals(Object obj)
    {
        if (obj instanceof Symbol)
            return ((Symbol)obj).getName() == name;
        return false;
    }
}
