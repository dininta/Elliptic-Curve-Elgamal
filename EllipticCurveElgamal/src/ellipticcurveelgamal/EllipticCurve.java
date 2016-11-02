/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ellipticcurveelgamal;

import java.math.BigInteger;
import ellipticcurveelgamal.Point;

/**
 *
 * @author Wiwit Rifa'i
 */
public class EllipticCurve {
    BigInteger a, b, p;
    int koblitz;
    public EllipticCurve() {
      this.a = BigInteger.valueOf(12312);
      this.b = BigInteger.valueOf(5346345);
      BigInteger two192 = BigInteger.valueOf(2).pow(192);
      BigInteger two64 = BigInteger.valueOf(2).pow(64);
      this.p = two192.subtract(two64).subtract(BigInteger.ONE);
      this.koblitz = 32;
    }
    public EllipticCurve(int koblitz) {
      this.a = BigInteger.valueOf(12312);
      this.b = BigInteger.valueOf(5346345);
      BigInteger two192 = BigInteger.valueOf(2).pow(192);
      BigInteger two64 = BigInteger.valueOf(2).pow(64);
      this.p = two192.subtract(two64).subtract(BigInteger.ONE);
      this.koblitz = koblitz;
    }
    public EllipticCurve(BigInteger a, BigInteger b, BigInteger p, int koblitz) {
      this.a = a;
      this.b = b;
      this.p = p;
      this.koblitz = koblitz;
    }
    public Point calculatePoint(BigInteger x) {
      BigInteger quadratic = x.multiply(x).add(a).multiply(x).add(b).remainder(p);
      BigInteger y = quadratic.modPow(p.add(BigInteger.ONE).divide(BigInteger.valueOf(4)), p);
      return new Point(x, y);
    }
    public boolean checkOnCurve(Point point) {
      BigInteger x = point.getX();
      BigInteger rhs = x.multiply(x).add(a).multiply(x).add(b).remainder(p);
      BigInteger y = point.getY();
      BigInteger lhs = y.multiply(y).remainder(p);
      return lhs.equals(rhs);
    }
    public Point encode(BigInteger m) {
      BigInteger x = m.multiply(BigInteger.valueOf(koblitz));
      for (int i = 0; i < this.koblitz; i++) {
        x = x.add(BigInteger.ONE);
        Point result = calculatePoint(x);
        if (checkOnCurve(result)) {
          return result;
        }
      }
      return new Point(x, BigInteger.ZERO);
    }  
    public BigInteger decode(Point point) {
      BigInteger x = point.getX();
      return x.divide(BigInteger.valueOf(koblitz));
    }
    public Point add(Point p1, Point p2) {
      return new Point ();
    }
    public Point doubling(Point point) {
      return new Point ();
    }
    public Point multiply(BigInteger k, Point point) {
      return new Point ();
    }
    public void set(BigInteger a, BigInteger b, BigInteger p) {
      this.a = a;
      this.b = b;
      this.p = p;
    }
}
