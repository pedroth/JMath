package fields;

import exceptions.MatrixRunTimeException;

import java.util.Objects;

public class Complex implements AlgebraicField<Complex> {
    public final static Complex I = new Complex(0,1);


    private double real;
    private double img;

    public Complex(double real, double img) {
        this.real = real;
        this.img = img;
    }

    public double getReal() {
        return this.real;
    }

    public double getImg() {
        return this.img;
    }

    @Override
    public Complex add(Complex a) {
        return new Complex(this.real + a.real, this.img + a.img);
    }

    @Override
    public Complex sub(Complex a) {
        return new Complex(this.real - a.real, this.img - a.img);
    }

    @Override
    public Complex prod(Complex a) {
        return new Complex(this.real * a.real - this.img * a.img, this.real * a.img + this.img * a.real);
    }

    @Override
    public Complex div(Complex a) {
        Complex prod = a.conj().prod(this);
        double v = a.squareNorm();
        // uncheck division by zero for performance reasons
        return new Complex(prod.real / v, prod.img / v);
    }

    @Override
    public Complex symmetric() {
        return new Complex(-this.real, -this.img);
    }

    @Override
    public Complex conj() {
        return new Complex(this.real, -this.img);
    }

    @Override
    public Complex sumIdentity() {
        return new Complex(0,0);
    }

    @Override
    public Complex prodIdentity() {
        return new Complex(1,0);
    }

    @Override
    public Complex reciprocal() {
        return prodIdentity().div(this);
    }

    @Override
    public Complex get() {
        return this;
    }

    @Override
    public Complex scale(double r) {
        return new Complex(real * r, img * r);
    }

    @Override
    public double squareNorm() {
        return this.conj().prod(this).getReal();
    }

    @Override
    public Complex copy() {
        return new Complex(this.real, this.img);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Complex complex = (Complex) o;
        // adding 0.0 to be -0.0 invariant check : https://stackoverflow.com/questions/6724031/how-can-a-primitive-float-value-be-0-0-what-does-that-mean
        return Double.compare(complex.real + 0.0, real + 0.0) == 0 &&
                Double.compare(complex.img + 0.0, img + 0.0) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(real, img);
    }

    public static Complex of(double x, double y) {
        return new Complex(x, y);
    }
}
