/*
 * StandardGenericFunction.java
 *
 * Copyright (C) 2003-2005 Peter Graves
 * $Id: StandardGenericFunction.java,v 1.1 2005-05-21 15:38:41 piso Exp $
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

public final class StandardGenericFunction extends StandardObject
{
    public StandardGenericFunction()
    {
        super(BuiltInClass.STANDARD_GENERIC_FUNCTION,
              BuiltInClass.STANDARD_GENERIC_FUNCTION.getClassLayout().getLength());
    }

    public LispObject typep(LispObject type) throws ConditionThrowable
    {
        if (type == Symbol.COMPILED_FUNCTION) {
            LispObject discriminatingFunction =
                slots[StandardGenericFunctionClass.SLOT_INDEX_DISCRIMINATING_FUNCTION];
            if (discriminatingFunction != null)
                return discriminatingFunction.typep(type);
            else
                return NIL;
        }
        return super.typep(type);
    }

    public LispObject getGenericFunctionName()
    {
        return slots[StandardGenericFunctionClass.SLOT_INDEX_NAME];
    }

    public void setGenericFunctionName(LispObject name)
    {
        slots[StandardGenericFunctionClass.SLOT_INDEX_NAME] = name;
    }

    public LispObject execute() throws ConditionThrowable
    {
        LispObject discriminatingFunction =
            slots[StandardGenericFunctionClass.SLOT_INDEX_DISCRIMINATING_FUNCTION];
        return discriminatingFunction.execute();
    }

    public LispObject execute(LispObject arg) throws ConditionThrowable
    {
        LispObject discriminatingFunction =
            slots[StandardGenericFunctionClass.SLOT_INDEX_DISCRIMINATING_FUNCTION];
        return discriminatingFunction.execute(arg);
    }

    public LispObject execute(LispObject first, LispObject second)
        throws ConditionThrowable
    {
        LispObject discriminatingFunction =
            slots[StandardGenericFunctionClass.SLOT_INDEX_DISCRIMINATING_FUNCTION];
        return discriminatingFunction.execute(first, second);
    }

    public LispObject execute(LispObject first, LispObject second,
                              LispObject third)
        throws ConditionThrowable
    {
        LispObject discriminatingFunction =
            slots[StandardGenericFunctionClass.SLOT_INDEX_DISCRIMINATING_FUNCTION];
        return discriminatingFunction.execute(first, second, third);
    }

    public LispObject execute(LispObject first, LispObject second,
                              LispObject third, LispObject fourth)
        throws ConditionThrowable
    {
        LispObject discriminatingFunction =
            slots[StandardGenericFunctionClass.SLOT_INDEX_DISCRIMINATING_FUNCTION];
        return discriminatingFunction.execute(first, second, third, fourth);
    }

    public LispObject execute(LispObject first, LispObject second,
                              LispObject third, LispObject fourth,
                              LispObject fifth)
        throws ConditionThrowable
    {
        LispObject discriminatingFunction =
            slots[StandardGenericFunctionClass.SLOT_INDEX_DISCRIMINATING_FUNCTION];
        return discriminatingFunction.execute(first, second, third, fourth,
                                              fifth);
    }

    public LispObject execute(LispObject first, LispObject second,
                              LispObject third, LispObject fourth,
                              LispObject fifth, LispObject sixth)
        throws ConditionThrowable
    {
        LispObject discriminatingFunction =
            slots[StandardGenericFunctionClass.SLOT_INDEX_DISCRIMINATING_FUNCTION];
        return discriminatingFunction.execute(first, second, third, fourth,
                                              fifth, sixth);
    }

    public LispObject execute(LispObject[] args) throws ConditionThrowable
    {
        LispObject discriminatingFunction =
            slots[StandardGenericFunctionClass.SLOT_INDEX_DISCRIMINATING_FUNCTION];
        return discriminatingFunction.execute(args);
    }

    public String writeToString() throws ConditionThrowable
    {
        LispObject name = getGenericFunctionName();
        if (name != null) {
            StringBuffer sb = new StringBuffer("#<");
            sb.append(getLispClass().getSymbol().writeToString());
            sb.append(' ');
            sb.append(name.writeToString());
            sb.append('>');
            return sb.toString();
        }
        return super.writeToString();
    }

    // Profiling.
    private int callCount;

    public final int getCallCount()
    {
        return callCount;
    }

    public void setCallCount(int n)
    {
        callCount = n;
    }

    public final void incrementCallCount()
    {
        ++callCount;
    }

    // MOP (p. 216) specifies the following reader generic functions:
    //   generic-function-argument-precedence-order
    //   generic-function-declarations
    //   generic-function-lambda-list
    //   generic-function-method-class
    //   generic-function-method-combination
    //   generic-function-methods
    //   generic-function-name

    // ### %generic-function-name
    private static final Primitive _GENERIC_FUNCTION_NAME =
        new Primitive("%generic-function-name", PACKAGE_SYS, true)
    {
        public LispObject execute(LispObject arg) throws ConditionThrowable
        {
            try {
                return ((StandardGenericFunction)arg).slots[StandardGenericFunctionClass.SLOT_INDEX_NAME];
            }
            catch (ClassCastException e) {
                return signal(new TypeError(arg, Symbol.GENERIC_FUNCTION));
            }
        }
    };

    // ### %set-generic-function-name
    private static final Primitive _SET_GENERIC_FUNCTION_NAME =
        new Primitive("%set-generic-function-name", PACKAGE_SYS, true)
    {
        public LispObject execute(LispObject first, LispObject second)
            throws ConditionThrowable
        {
            try {
                ((StandardGenericFunction)first).slots[StandardGenericFunctionClass.SLOT_INDEX_NAME] = second;
                return second;
            }
            catch (ClassCastException e) {
                return signal(new TypeError(first, Symbol.GENERIC_FUNCTION));
            }
        }
    };

    // ### %generic-function-lambda-list
    private static final Primitive _GENERIC_FUNCTION_LAMBDA_LIST =
        new Primitive("%generic-function-lambda-list", PACKAGE_SYS, true)
    {
        public LispObject execute(LispObject arg) throws ConditionThrowable
        {
            try {
                return ((StandardGenericFunction)arg).slots[StandardGenericFunctionClass.SLOT_INDEX_LAMBDA_LIST];
            }
            catch (ClassCastException e) {
                return signal(new TypeError(arg, Symbol.GENERIC_FUNCTION));
            }
        }
    };

    // ### %set-generic-function-lambdaList
    private static final Primitive _SET_GENERIC_FUNCTION_LAMBDA_LIST =
        new Primitive("%set-generic-function-lambda-list", PACKAGE_SYS, true)
    {
        public LispObject execute(LispObject first, LispObject second)
            throws ConditionThrowable
        {
            try {
                ((StandardGenericFunction)first).slots[StandardGenericFunctionClass.SLOT_INDEX_LAMBDA_LIST] = second;
                return second;
            }
            catch (ClassCastException e) {
                return signal(new TypeError(first, Symbol.GENERIC_FUNCTION));
            }
        }
    };

    // ### generic-function-discriminating-function
    private static final Primitive GENERIC_FUNCTION_DISCRIMINATING_FUNCTION =
        new Primitive("generic-function-discriminating-function", PACKAGE_SYS, true)
    {
        public LispObject execute(LispObject arg) throws ConditionThrowable
        {
            try {
                return ((StandardGenericFunction)arg).slots[StandardGenericFunctionClass.SLOT_INDEX_DISCRIMINATING_FUNCTION];
            }
            catch (ClassCastException e) {
                return signal(new TypeError(arg, Symbol.GENERIC_FUNCTION));
            }
        }
    };

    // ### %set-generic-function-discriminating-function
    private static final Primitive _SET_GENERIC_FUNCTION_DISCRIMINATING_FUNCTION =
        new Primitive("%set-generic-function-discriminating-function", PACKAGE_SYS, true)
    {
        public LispObject execute(LispObject first, LispObject second)
            throws ConditionThrowable
        {
            try {
                ((StandardGenericFunction)first).slots[StandardGenericFunctionClass.SLOT_INDEX_DISCRIMINATING_FUNCTION] = second;
                return second;
            }
            catch (ClassCastException e) {
                return signal(new TypeError(first, Symbol.GENERIC_FUNCTION));
            }
        }
    };

    // ### gf-required-args
    private static final Primitive GF_REQUIRED_ARGS =
        new Primitive("gf-required-args", PACKAGE_SYS, true)
    {
        public LispObject execute(LispObject arg) throws ConditionThrowable
        {
            try {
                return ((StandardGenericFunction)arg).slots[StandardGenericFunctionClass.SLOT_INDEX_REQUIRED_ARGS];
            }
            catch (ClassCastException e) {
                return signal(new TypeError(arg, Symbol.GENERIC_FUNCTION));
            }
        }
    };

    // ### %set-gf-required-args
    private static final Primitive _SET_GF_REQUIRED_ARGS =
        new Primitive("%set-gf-required-args", PACKAGE_SYS, true)
    {
        public LispObject execute(LispObject first, LispObject second)
            throws ConditionThrowable
        {
            try {
                ((StandardGenericFunction)first).slots[StandardGenericFunctionClass.SLOT_INDEX_REQUIRED_ARGS] = second;
                return second;
            }
            catch (ClassCastException e) {
                return signal(new TypeError(first, Symbol.GENERIC_FUNCTION));
            }
        }
    };

    // ### generic-function-initial-methods
    private static final Primitive GENERIC_FUNCTION_INITIAL_METHODS =
        new Primitive("generic-function-initial-methods", PACKAGE_SYS, true)
    {
        public LispObject execute(LispObject arg) throws ConditionThrowable
        {
            try {
                return ((StandardGenericFunction)arg).slots[StandardGenericFunctionClass.SLOT_INDEX_INITIAL_METHODS];
            }
            catch (ClassCastException e) {
                return signal(new TypeError(arg, Symbol.GENERIC_FUNCTION));
            }
        }
    };

    // ### set-generic-function-initial-methods
    private static final Primitive SET_GENERIC_FUNCTION_INITIAL_METHODS =
        new Primitive("set-generic-function-initial-methods", PACKAGE_SYS, true)
    {
        public LispObject execute(LispObject first, LispObject second)
            throws ConditionThrowable
        {
            try {
                ((StandardGenericFunction)first).slots[StandardGenericFunctionClass.SLOT_INDEX_INITIAL_METHODS] = second;
                return second;
            }
            catch (ClassCastException e) {
                return signal(new TypeError(first, Symbol.GENERIC_FUNCTION));
            }
        }
    };

    // ### generic-function-methods
    private static final Primitive GENERIC_FUNCTION_METHODS =
        new Primitive("generic-function-methods", PACKAGE_SYS, true)
    {
        public LispObject execute(LispObject arg) throws ConditionThrowable
        {
            try {
                return ((StandardGenericFunction)arg).slots[StandardGenericFunctionClass.SLOT_INDEX_METHODS];
            }
            catch (ClassCastException e) {
                return signal(new TypeError(arg, Symbol.GENERIC_FUNCTION));
            }
        }
    };

    // ### set-generic-function-methods
    private static final Primitive SET_GENERIC_FUNCTION_METHODS =
        new Primitive("set-generic-function-methods", PACKAGE_SYS, true)
    {
        public LispObject execute(LispObject first, LispObject second)
            throws ConditionThrowable
        {
            try {
                ((StandardGenericFunction)first).slots[StandardGenericFunctionClass.SLOT_INDEX_METHODS] = second;
                return second;
            }
            catch (ClassCastException e) {
                return signal(new TypeError(first, Symbol.GENERIC_FUNCTION));
            }
        }
    };

    // ### generic-function-method-class
    private static final Primitive GENERIC_FUNCTION_METHOD_CLASS =
        new Primitive("generic-function-method-class", PACKAGE_SYS, true)
    {
        public LispObject execute(LispObject arg) throws ConditionThrowable
        {
            try {
                return ((StandardGenericFunction)arg).slots[StandardGenericFunctionClass.SLOT_INDEX_METHOD_CLASS];
            }
            catch (ClassCastException e) {
                return signal(new TypeError(arg, Symbol.GENERIC_FUNCTION));
            }
        }
    };

    // ### set-generic-function-method-class
    private static final Primitive SET_GENERIC_FUNCTION_METHOD_CLASS =
        new Primitive("set-generic-function-method-class", PACKAGE_SYS, true)
    {
        public LispObject execute(LispObject first, LispObject second)
            throws ConditionThrowable
        {
            try {
                ((StandardGenericFunction)first).slots[StandardGenericFunctionClass.SLOT_INDEX_METHOD_CLASS] = second;
                return second;
            }
            catch (ClassCastException e) {
                return signal(new TypeError(first, Symbol.GENERIC_FUNCTION));
            }
        }
    };

    // ### generic-function-method-combination
    private static final Primitive GENERIC_FUNCTION_METHOD_COMBINATION =
        new Primitive("generic-function-method-combination", PACKAGE_SYS, true)
    {
        public LispObject execute(LispObject arg) throws ConditionThrowable
        {
            try {
                return ((StandardGenericFunction)arg).slots[StandardGenericFunctionClass.SLOT_INDEX_METHOD_COMBINATION];
            }
            catch (ClassCastException e) {
                return signal(new TypeError(arg, Symbol.GENERIC_FUNCTION));
            }
        }
    };

    // ### set-generic-function-method-combination
    private static final Primitive SET_GENERIC_FUNCTION_METHOD_COMBINATION =
        new Primitive("set-generic-function-method-combination", PACKAGE_SYS, true)
    {
        public LispObject execute(LispObject first, LispObject second)
            throws ConditionThrowable
        {
            try {
                ((StandardGenericFunction)first).slots[StandardGenericFunctionClass.SLOT_INDEX_METHOD_COMBINATION] = second;
                return second;
            }
            catch (ClassCastException e) {
                return signal(new TypeError(first, Symbol.GENERIC_FUNCTION));
            }
        }
    };

    // ### generic-function-argument-precedence-order
    private static final Primitive GENERIC_FUNCTION_ARGUMENT_PRECEDENCE_ORDER =
        new Primitive("generic-function-argument-precedence-order", PACKAGE_SYS, true)
    {
        public LispObject execute(LispObject arg) throws ConditionThrowable
        {
            try {
                return ((StandardGenericFunction)arg).slots[StandardGenericFunctionClass.SLOT_INDEX_ARGUMENT_PRECEDENCE_ORDER];
            }
            catch (ClassCastException e) {
                return signal(new TypeError(arg, Symbol.GENERIC_FUNCTION));
            }
        }
    };

    // ### set-generic-function-argument-precedence-order
    private static final Primitive SET_GENERIC_FUNCTION_ARGUMENT_PRECEDENCE_ORDER =
        new Primitive("set-generic-function-argument-precedence-order", PACKAGE_SYS, true)
    {
        public LispObject execute(LispObject first, LispObject second)
            throws ConditionThrowable
        {
            try {
                ((StandardGenericFunction)first).slots[StandardGenericFunctionClass.SLOT_INDEX_ARGUMENT_PRECEDENCE_ORDER] = second;
                return second;
            }
            catch (ClassCastException e) {
                return signal(new TypeError(first, Symbol.GENERIC_FUNCTION));
            }
        }
    };

    // ### generic-function-classes-to-emf-table
    private static final Primitive GENERIC_FUNCTION_CLASSES_TO_EMF_TABLE =
        new Primitive("generic-function-classes-to-emf-table", PACKAGE_SYS, true)
    {
        public LispObject execute(LispObject arg) throws ConditionThrowable
        {
            try {
                return ((StandardGenericFunction)arg).slots[StandardGenericFunctionClass.SLOT_INDEX_CLASSES_TO_EMF_TABLE];
            }
            catch (ClassCastException e) {
                return signal(new TypeError(arg, Symbol.GENERIC_FUNCTION));
            }
        }
    };

    // ### set-generic-function-classes-to-emf-table
    private static final Primitive SET_GENERIC_FUNCTION_CLASSES_TO_EMF_TABLE =
        new Primitive("set-generic-function-classes-to-emf-table", PACKAGE_SYS, true)
    {
        public LispObject execute(LispObject first, LispObject second)
            throws ConditionThrowable
        {
            try {
                ((StandardGenericFunction)first).slots[StandardGenericFunctionClass.SLOT_INDEX_CLASSES_TO_EMF_TABLE] = second;
                return second;
            }
            catch (ClassCastException e) {
                return signal(new TypeError(first, Symbol.GENERIC_FUNCTION));
            }
        }
    };

    // ### generic-function-documentation
    private static final Primitive GENERIC_FUNCTION_DOCUMENTATION =
        new Primitive("generic-function-documentation", PACKAGE_SYS, true)
    {
        public LispObject execute(LispObject arg) throws ConditionThrowable
        {
            try {
                return ((StandardGenericFunction)arg).slots[StandardGenericFunctionClass.SLOT_INDEX_DOCUMENTATION];
            }
            catch (ClassCastException e) {
                return signal(new TypeError(arg, Symbol.GENERIC_FUNCTION));
            }
        }
    };

    // ### set-generic-function-documentation
    private static final Primitive SET_GENERIC_FUNCTION_DOCUMENTATION =
        new Primitive("set-generic-function-documentation", PACKAGE_SYS, true)
    {
        public LispObject execute(LispObject first, LispObject second)
            throws ConditionThrowable
        {
            try {
                ((StandardGenericFunction)first).slots[StandardGenericFunctionClass.SLOT_INDEX_DOCUMENTATION] = second;
                return second;
            }
            catch (ClassCastException e) {
                return signal(new TypeError(first, Symbol.GENERIC_FUNCTION));
            }
        }
    };
}
