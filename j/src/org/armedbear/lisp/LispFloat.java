/*
 * LispFloat.java
 *
 * Copyright (C) 2003-2005 Peter Graves
 * $Id: LispFloat.java,v 1.86 2005-03-15 17:34:10 piso Exp $
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

import java.math.BigInteger;

public final class LispFloat extends LispObject
{
    public static final LispFloat ZERO      = new LispFloat(0);
    public static final LispFloat ONE       = new LispFloat(1);
    public static final LispFloat MINUS_ONE = new LispFloat(-1);

    public static final LispFloat PI = new LispFloat(Math.PI);

    public static final LispFloat DOUBLE_FLOAT_POSITIVE_INFINITY =
        new LispFloat(Double.POSITIVE_INFINITY);

    public static final LispFloat DOUBLE_FLOAT_NEGATIVE_INFINITY =
        new LispFloat(Double.NEGATIVE_INFINITY);

    static {
        Symbol.DOUBLE_FLOAT_POSITIVE_INFINITY.setSymbolValue(DOUBLE_FLOAT_POSITIVE_INFINITY);
        Symbol.DOUBLE_FLOAT_POSITIVE_INFINITY.setConstant(true);
        Symbol.DOUBLE_FLOAT_NEGATIVE_INFINITY.setSymbolValue(DOUBLE_FLOAT_NEGATIVE_INFINITY);
        Symbol.DOUBLE_FLOAT_NEGATIVE_INFINITY.setConstant(true);
    }

    public final double value;

    public LispFloat(double value)
    {
        this.value = value;
    }

    public LispObject typeOf()
    {
        return Symbol.FLOAT;
    }

    public LispObject classOf()
    {
        return BuiltInClass.FLOAT;
    }

    public LispObject typep(LispObject typeSpecifier) throws ConditionThrowable
    {
        if (typeSpecifier == Symbol.FLOAT)
            return T;
        if (typeSpecifier == BuiltInClass.FLOAT)
            return T;
        if (typeSpecifier == Symbol.REAL)
            return T;
        if (typeSpecifier == Symbol.NUMBER)
            return T;
        if (typeSpecifier == Symbol.SINGLE_FLOAT)
            return T;
        if (typeSpecifier == Symbol.DOUBLE_FLOAT)
            return T;
        if (typeSpecifier == Symbol.SHORT_FLOAT)
            return T;
        if (typeSpecifier == Symbol.LONG_FLOAT)
            return T;
        return super.typep(typeSpecifier);
    }

    public LispObject NUMBERP()
    {
        return T;
    }

    public boolean numberp()
    {
        return true;
    }

    public boolean realp()
    {
        return true;
    }

    public boolean eql(LispObject obj)
    {
        if (this == obj)
            return true;
        if (obj instanceof LispFloat) {
            if (value == ((LispFloat)obj).value)
                return true;
        }
        return false;
    }

    public boolean equal(LispObject obj)
    {
        if (this == obj)
            return true;
        if (obj instanceof LispFloat) {
            if (value == ((LispFloat)obj).value)
                return true;
        }
        return false;
    }

    public boolean equalp(int n)
    {
        return value == n;
    }

    public boolean equalp(LispObject obj) throws ConditionThrowable
    {
        if (obj instanceof LispFloat)
            return value == ((LispFloat)obj).value;
        if (obj instanceof Fixnum)
            return value == ((Fixnum)obj).value;
        if (obj instanceof Bignum)
            return value == ((Bignum)obj).floatValue();
        if (obj instanceof Ratio)
            return value == ((Ratio)obj).floatValue();
        return false;
    }

    public LispObject ABS()
    {
        if (value > 0)
            return this;
        if (value == 0) // 0.0 or -0.0
            return LispFloat.ZERO;
        return new LispFloat(- value);
    }

    public boolean plusp()
    {
        return value > 0;
    }

    public boolean minusp()
    {
        return value < 0;
    }

    public boolean zerop()
    {
        return value == 0;
    }

    public LispObject FLOATP()
    {
        return T;
    }

    public boolean floatp()
    {
        return true;
    }

    public static double getValue(LispObject obj) throws ConditionThrowable
    {
        try {
            return ((LispFloat)obj).value;
        }
        catch (ClassCastException e) {
            signal(new TypeError(obj, Symbol.FLOAT));
            // Not reached.
            return 0;
        }
    }

    public final double getValue()
    {
        return value;
    }

    public Object javaInstance()
    {
        return new Double(value);
    }

    public Object javaInstance(Class c)
    {
        String cn = c.getName();
        if (cn.equals("java.lang.Float") || cn.equals("float"))
            return new Float(value);
        return javaInstance();
    }

    public final LispObject incr()
    {
        return new LispFloat(value + 1);
    }

    public final LispObject decr()
    {
        return new LispFloat(value - 1);
    }

    public LispObject add(LispObject obj) throws ConditionThrowable
    {
        if (obj instanceof LispFloat)
            return new LispFloat(value + ((LispFloat)obj).value);
        if (obj instanceof Fixnum)
            return new LispFloat(value + ((Fixnum)obj).value);
        if (obj instanceof Bignum)
            return new LispFloat(value + ((Bignum)obj).floatValue());
        if (obj instanceof Ratio)
            return new LispFloat(value + ((Ratio)obj).floatValue());
        if (obj instanceof Complex) {
            Complex c = (Complex) obj;
            return Complex.getInstance(add(c.getRealPart()), c.getImaginaryPart());
        }
        return signal(new TypeError(obj, Symbol.NUMBER));
    }

    public LispObject subtract(LispObject obj) throws ConditionThrowable
    {
        if (obj instanceof LispFloat)
            return new LispFloat(value - ((LispFloat)obj).value);
        if (obj instanceof Fixnum)
            return new LispFloat(value - ((Fixnum)obj).value);
        if (obj instanceof Bignum)
            return new LispFloat(value - ((Bignum)obj).floatValue());
        if (obj instanceof Ratio)
            return new LispFloat(value - ((Ratio)obj).floatValue());
        if (obj instanceof Complex) {
            Complex c = (Complex) obj;
            return Complex.getInstance(subtract(c.getRealPart()),
                                       ZERO.subtract(c.getImaginaryPart()));
        }
        return signal(new TypeError(obj, Symbol.NUMBER));
    }

    public LispObject multiplyBy(LispObject obj) throws ConditionThrowable
    {
        if (obj instanceof LispFloat)
            return new LispFloat(value * ((LispFloat)obj).value);
        if (obj instanceof Fixnum)
            return new LispFloat(value * ((Fixnum)obj).value);
        if (obj instanceof Bignum)
            return new LispFloat(value * ((Bignum)obj).floatValue());
        if (obj instanceof Ratio)
            return new LispFloat(value * ((Ratio)obj).floatValue());
        if (obj instanceof Complex) {
            Complex c = (Complex) obj;
            return Complex.getInstance(multiplyBy(c.getRealPart()),
                                       multiplyBy(c.getImaginaryPart()));
        }
        return signal(new TypeError(obj, Symbol.NUMBER));
    }

    public LispObject divideBy(LispObject obj) throws ConditionThrowable
    {
        if (obj instanceof LispFloat)
            return new LispFloat(value / ((LispFloat)obj).value);
        if (obj instanceof Fixnum)
            return new LispFloat(value / ((Fixnum)obj).value);
        if (obj instanceof Bignum)
            return new LispFloat(value / ((Bignum)obj).floatValue());
        if (obj instanceof Ratio)
            return new LispFloat(value / ((Ratio)obj).floatValue());
        if (obj instanceof Complex) {
            Complex c = (Complex) obj;
            LispObject re = c.getRealPart();
            LispObject im = c.getImaginaryPart();
            LispObject denom = re.multiplyBy(re).add(im.multiplyBy(im));
            LispObject resX = multiplyBy(re).divideBy(denom);
            LispObject resY =
                multiplyBy(Fixnum.MINUS_ONE).multiplyBy(im).divideBy(denom);
            return Complex.getInstance(resX, resY);
        }
        return signal(new TypeError(obj, Symbol.NUMBER));
    }

    public boolean isEqualTo(LispObject obj) throws ConditionThrowable
    {
        if (obj instanceof LispFloat)
            return value == ((LispFloat)obj).value;
        if (obj instanceof Fixnum)
            return value == ((Fixnum)obj).value;
        if (obj instanceof Bignum)
            return rational().isEqualTo(obj);
        if (obj instanceof Ratio)
            return rational().isEqualTo(obj);
        if (obj instanceof Complex)
            return obj.isEqualTo(this);
        signal(new TypeError(obj, Symbol.NUMBER));
        // Not reached.
        return false;
    }

    public boolean isNotEqualTo(LispObject obj) throws ConditionThrowable
    {
        return !isEqualTo(obj);
    }

    public boolean isLessThan(LispObject obj) throws ConditionThrowable
    {
        if (obj instanceof LispFloat)
            return value < ((LispFloat)obj).value;
        if (obj instanceof Fixnum)
            return value < ((Fixnum)obj).value;
        if (obj instanceof Bignum)
            return rational().isLessThan(obj);
        if (obj instanceof Ratio)
            return rational().isLessThan(obj);
        signal(new TypeError(obj, Symbol.REAL));
        // Not reached.
        return false;
    }

    public boolean isGreaterThan(LispObject obj) throws ConditionThrowable
    {
        if (obj instanceof LispFloat)
            return value > ((LispFloat)obj).value;
        if (obj instanceof Fixnum)
            return value > ((Fixnum)obj).value;
        if (obj instanceof Bignum)
            return rational().isGreaterThan(obj);
        if (obj instanceof Ratio)
            return rational().isGreaterThan(obj);
        signal(new TypeError(obj, Symbol.REAL));
        // Not reached.
        return false;
    }

    public boolean isLessThanOrEqualTo(LispObject obj) throws ConditionThrowable
    {
        if (obj instanceof LispFloat)
            return value <= ((LispFloat)obj).value;
        if (obj instanceof Fixnum)
            return value <= ((Fixnum)obj).value;
        if (obj instanceof Bignum)
            return rational().isLessThanOrEqualTo(obj);
        if (obj instanceof Ratio)
            return rational().isLessThanOrEqualTo(obj);
        signal(new TypeError(obj, Symbol.REAL));
        // Not reached.
        return false;
    }

    public boolean isGreaterThanOrEqualTo(LispObject obj) throws ConditionThrowable
    {
        if (obj instanceof LispFloat)
            return value >= ((LispFloat)obj).value;
        if (obj instanceof Fixnum)
            return value >= ((Fixnum)obj).value;
        if (obj instanceof Bignum)
            return rational().isGreaterThanOrEqualTo(obj);
        if (obj instanceof Ratio)
            return rational().isGreaterThanOrEqualTo(obj);
        signal(new TypeError(obj, Symbol.REAL));
        // Not reached.
        return false;
    }

    public LispObject truncate(LispObject obj) throws ConditionThrowable
    {
        // "When rationals and floats are combined by a numerical function,
        // the rational is first converted to a float of the same format."
        // 12.1.4.1
        if (obj instanceof Fixnum) {
            return truncate(new LispFloat(((Fixnum)obj).value));
        }
        if (obj instanceof Bignum) {
            return truncate(new LispFloat(((Bignum)obj).floatValue()));
        }
        if (obj instanceof Ratio) {
            return truncate(new LispFloat(((Ratio)obj).floatValue()));
        }
        if (obj instanceof LispFloat) {
            final LispThread thread = LispThread.currentThread();
            double divisor = ((LispFloat)obj).value;
            double quotient = value / divisor;
            if (quotient >= Integer.MIN_VALUE && quotient <= Integer.MAX_VALUE) {
                int q = (int) quotient;
                return thread.setValues(new Fixnum(q),
                                        new LispFloat(value - q * divisor));
            }
            // We need to convert the quotient to a bignum.
            long bits = Double.doubleToRawLongBits((double)quotient);
            int s = ((bits >> 63) == 0) ? 1 : -1;
            int e = (int) ((bits >> 52) & 0x7ffL);
            long m;
            if (e == 0)
                m = (bits & 0xfffffffffffffL) << 1;
            else
                m = (bits & 0xfffffffffffffL) | 0x10000000000000L;
            LispObject significand = number(m);
            Fixnum exponent = new Fixnum(e - 1075);
            Fixnum sign = new Fixnum(s);
            LispObject result = significand;
            result =
                result.multiplyBy(MathFunctions.EXPT.execute(Fixnum.TWO, exponent));
            result = result.multiplyBy(sign);
            // Calculate remainder.
            LispObject product = result.multiplyBy(obj);
            LispObject remainder = subtract(product);
            return thread.setValues(result, remainder);
        }
        return signal(new TypeError(obj, Symbol.REAL));
    }

    public int hashCode()
    {
        long bits = Double.doubleToLongBits(value);
        return (int) (bits ^ (bits >>> 32));
    }

    public int psxhash()
    {
        if ((value % 1) == 0)
            return (((int)value) & 0x7fffffff);
        else
            return (hashCode() & 0x7fffffff);
    }

    public String writeToString() throws ConditionThrowable
    {
        if (value == Double.POSITIVE_INFINITY) {
            StringBuffer sb = new StringBuffer("#.");
            sb.append(Symbol.DOUBLE_FLOAT_POSITIVE_INFINITY.writeToString());
            return sb.toString();
        }
        if (value == Double.NEGATIVE_INFINITY) {
            StringBuffer sb = new StringBuffer("#.");
            sb.append(Symbol.DOUBLE_FLOAT_NEGATIVE_INFINITY.writeToString());
            return sb.toString();
        }
        if (value != value)
            return "#<DOUBLE-FLOAT NaN>";
        String s1 = String.valueOf(value);
        String s2 = s1.replace('E', 'd');
        if (s1 != s2 || _PRINT_READABLY_.symbolValue() == NIL)
            return s2;
        return s2.concat("d0");
    }

    public LispObject rational() throws ConditionThrowable
    {
        final long bits = Double.doubleToRawLongBits(value);
        int sign = ((bits >> 63) == 0) ? 1 : -1;
        int storedExponent = (int) ((bits >> 52) & 0x7ffL);
        long mantissa;
        if (storedExponent == 0)
            mantissa = (bits & 0xfffffffffffffL) << 1;
        else
            mantissa = (bits & 0xfffffffffffffL) | 0x10000000000000L;
        if (mantissa == 0)
            return Fixnum.ZERO;
        if (sign < 0)
            mantissa = -mantissa;
        // Subtract bias.
        final int exponent = storedExponent - 1023;
        BigInteger numerator, denominator;
        if (exponent < 0) {
            numerator = BigInteger.valueOf(mantissa);
            denominator = BigInteger.valueOf(1).shiftLeft(52 - exponent);
        } else {
            numerator = BigInteger.valueOf(mantissa).shiftLeft(exponent);
            denominator = BigInteger.valueOf(0x10000000000000L); // (ash 1 52)
        }
        return number(numerator, denominator);
    }

    public static LispFloat coerceToFloat(LispObject obj) throws ConditionThrowable
    {
        if (obj instanceof LispFloat)
            return (LispFloat) obj;
        if (obj instanceof Fixnum)
            return new LispFloat(((Fixnum)obj).value);
        if (obj instanceof Bignum)
            return new LispFloat(((Bignum)obj).floatValue());
        if (obj instanceof Ratio)
            return new LispFloat(((Ratio)obj).floatValue());
        signal(new TypeError(obj.writeToString() +
                             " cannot be converted to type FLOAT."));
        // Not reached.
        return null;
    }
}
