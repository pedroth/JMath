package fields;

import exceptions.MatrixRunTimeException;

import java.util.Objects;

public class Real implements AlgebraicField<Real> {
    private final double real;

    public Real(double x) {
        this.real = x;
    }

    double getDouble() {
        return this.real;
    }

    @Override
    public Real add(Real a) {
        return new Real(this.real + a.real);
    }

    @Override
    public Real sub(Real a) {
        return new Real(this.real - a.real);
    }

    @Override
    public Real prod(Real a) {
        return new Real(this.real * a.real);
    }

    @Override
    public Real div(Real a) {
        // uncheck division by zero for performance reasons
        return new Real(this.real / a.real);
    }

    @Override
    public Real symmetric() {
        return new Real(-this.real);
    }

    @Override
    public Real conj() {
        return this;
    }

    @Override
    public Real sumIdentity() {
        return new Real(0.0);
    }

    @Override
    public Real prodIdentity() {
        return new Real(1.0);
    }

    @Override
    public Real reciprocal() {
        // uncheck division by zero for performance reasons
        return new Real(1.0 / this.real);
    }

    @Override
    public Real get() {
        return this;
    }

    @Override
    public Real scale(double r) {
        return new Real(real * r);
    }

    @Override
    public double squareNorm() {
        return this.real * this.real;
    }

    @Override
    public Real copy() {
        return new Real(this.real);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Real real1 = (Real) o;
        // adding 0.0 to be -0.0 invariant : https://stackoverflow.com/questions/6724031/how-can-a-primitive-float-value-be-0-0-what-does-that-mean
        return Double.compare(real1.real + 0.0, real + 0.0) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(real);
    }

    public static Real of(double d) {
        return new Real(d);
    }
}
