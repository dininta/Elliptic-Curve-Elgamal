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
    public static final Point ZERO = new Point(BigInteger.ONE.negate(), BigInteger.ONE.negate());
    private BigInteger a, b, p;
    private int koblitz;
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
      BigInteger quadratic = x.multiply(x).add(a).multiply(x).add(b).mod(p);
      BigInteger y = quadratic.modPow(p.add(BigInteger.ONE).divide(BigInteger.valueOf(4)), p);
      return new Point(x, y);
    }
    public boolean checkOnCurve(Point point) {
      BigInteger x = point.getX();
      BigInteger rhs = x.multiply(x).add(a).multiply(x).add(b).mod(p);
      BigInteger y = point.getY();
      BigInteger lhs = y.multiply(y).mod(p);
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
      return EllipticCurve.ZERO;
    }  
    public BigInteger decode(Point point) {
      BigInteger x = point.getX();
      return x.subtract(BigInteger.ONE).divide(BigInteger.valueOf(koblitz));
    }
    public Point add(Point p1, Point p2) {
      if (p1.equals(EllipticCurve.ZERO))
        return p2;
      if (p2.equals(EllipticCurve.ZERO))
        return p1;
      BigInteger dx = p1.getX().subtract(p2.getX()).mod(p);
      BigInteger dy = p1.getY().subtract(p2.getY()).mod(p);
      if (dx.equals(BigInteger.ZERO))
        return EllipticCurve.ZERO;
      BigInteger gradien = dy.multiply(dx.modInverse(p)).mod(p);
      BigInteger xr = gradien.multiply(gradien).subtract(p1.getX().add(p2.getX())).mod(p);
      BigInteger yr = gradien.multiply(p1.getX().subtract(xr)).subtract(p1.getY()).mod(p);
      return new Point (xr, yr);
    }
    public Point inverse(Point point) {
      return new Point(point.getX(), point.getY().negate().mod(p));
    }
    public Point substract(Point p1, Point p2) {
      return this.add(p1, this.inverse(p2));
    }
    public Point doubling(Point point) {
      if (point.equals(EllipticCurve.ZERO))
        return point;
      if (point.getY().equals(BigInteger.ZERO))
        return EllipticCurve.ZERO;
      BigInteger up = point.getX().multiply(BigInteger.valueOf(3)).add(this.a).mod(p);
      BigInteger down = point.getY().add(point.getY()).mod(p);

      BigInteger gradien = up.multiply(down.modInverse(p)).mod(p);
      BigInteger xr = gradien.multiply(gradien).subtract(point.getX().add(point.getX())).mod(p);
      BigInteger yr = gradien.multiply(point.getX().subtract(xr)).subtract(point.getY()).mod(p);
      return new Point (xr, yr);
    }
    public Point multiply(BigInteger k, Point point) {
      if (k == BigInteger.ZERO)
        return EllipticCurve.ZERO;
      if (k == BigInteger.ONE)
        return point;
      Point point2 = this.multiply(k.divide(BigInteger.valueOf(2)), this.doubling(point));
      if (k.mod(BigInteger.valueOf(2)).equals(BigInteger.ONE))
        point2 = this.add(point2, point);
      return point2;
    }
    public void set(BigInteger a, BigInteger b, BigInteger p) {
      this.a = a;
      this.b = b;
      this.p = p;
    }
}
