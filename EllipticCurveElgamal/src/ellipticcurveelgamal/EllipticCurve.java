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
      assert(p.isProbablePrime(32));
      assert(!this.a.modPow(BigInteger.valueOf(3), p).multiply(BigInteger.valueOf(4)).add(b.add(b).multiply(BigInteger.valueOf(27))).mod(p).equals(BigInteger.ZERO));
    }
    public EllipticCurve(int koblitz) {
      this.a = BigInteger.valueOf(12312);
      this.b = BigInteger.valueOf(5346345);
      BigInteger two192 = BigInteger.valueOf(2).pow(192);
      BigInteger two64 = BigInteger.valueOf(2).pow(64);
      this.p = two192.subtract(two64).subtract(BigInteger.ONE);
      this.koblitz = koblitz;
      assert(p.isProbablePrime(32));
      assert(!this.a.modPow(BigInteger.valueOf(3), p).multiply(BigInteger.valueOf(4)).add(b.add(b).multiply(BigInteger.valueOf(27))).mod(p).equals(BigInteger.ZERO));
    }
    public EllipticCurve(BigInteger a, BigInteger b, BigInteger p, int koblitz) {
      this.a = a;
      this.b = b;
      this.p = p;
      this.koblitz = koblitz;
      assert(p.isProbablePrime(32));
      assert(!this.a.modPow(BigInteger.valueOf(3), p).multiply(BigInteger.valueOf(4)).add(b.add(b).multiply(BigInteger.valueOf(27))).mod(p).equals(BigInteger.ZERO));
    }
    public BigInteger getP() {
        return this.p;
    }
    public int getKoblitz() {
        return this.koblitz;
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
      if (p1.equals(p2))
        return this.doubling(p1);
      BigInteger dx = p1.getX().subtract(p2.getX()).mod(p);
      BigInteger dy = p1.getY().subtract(p2.getY()).mod(p);
      if (dx.equals(BigInteger.ZERO))
        return EllipticCurve.ZERO;
      BigInteger gradien = dy.multiply(dx.modPow(p.subtract(BigInteger.valueOf(2)), p)).mod(p);
      BigInteger xr = gradien.multiply(gradien).subtract(p1.getX().add(p2.getX())).mod(p);
      BigInteger yr = gradien.multiply(p1.getX().subtract(xr)).subtract(p1.getY()).mod(p);
      return new Point (xr, yr);
    }
    public Point inverse(Point point) {
      if (point.equals(EllipticCurve.ZERO))
        return point;
      return new Point(point.getX(), point.getY().negate().mod(p));
    }
    public Point subtract(Point p1, Point p2) {
      return this.add(p1, this.inverse(p2));
    }
    public Point doubling(Point point) {
      if (point.equals(EllipticCurve.ZERO))
        return point;
      if (point.getY().equals(BigInteger.ZERO))
        return EllipticCurve.ZERO;
      BigInteger up = point.getX().multiply(point.getX()).multiply(BigInteger.valueOf(3)).add(this.a).mod(p);
      BigInteger down = point.getY().add(point.getY()).mod(p);

      BigInteger gradien = up.multiply(down.modPow(p.subtract(BigInteger.valueOf(2)), p)).mod(p);
      BigInteger xr = gradien.multiply(gradien).subtract(point.getX().add(point.getX())).mod(p);
      BigInteger yr = gradien.multiply(point.getX().subtract(xr)).subtract(point.getY()).mod(p);
      return new Point (xr, yr);
    }
    public Point multiply(BigInteger k, Point point) {
      if (k.equals(BigInteger.ZERO))
        return EllipticCurve.ZERO;
      if (k.equals(BigInteger.ONE))
        return point;
      Point point2 = this.multiply(k.divide(BigInteger.valueOf(2)), this.doubling(point));
      if (k.mod(BigInteger.valueOf(2)).equals(BigInteger.ONE)) {
        point2 = this.add(point2, point);
      }
      return point2;
    }
    public void set(BigInteger a, BigInteger b, BigInteger p) {
      this.a = a;
      this.b = b;
      this.p = p;
    }
    public static void testing(String[] args) {
        EllipticCurve ec = new EllipticCurve();

        System.out.println("***** Testing EllipticCurve *****");
        BigInteger key = new BigInteger("234400053450093420534645634534534");
        Point g = ec.encode(BigInteger.valueOf(123456));
        Point gkey = ec.multiply(key, g);
        System.out.println("# Pembangkitan kunci . . .");
        System.out.println("key : " + key);
        System.out.println("Base Point : " + g);
        System.out.println("Public Key : " + gkey);

        BigInteger message = BigInteger.valueOf(987654321);
        Point m = ec.encode(message);
        BigInteger y = new BigInteger("3123124135245645645");
        Point c1 = ec.multiply(y, g);
        Point c2 = ec.add(m, ec.multiply(y, gkey));
        System.out.println("\n## Enkripsi . . .");
        System.out.println("message : " + message);
        System.out.println("Ecoded message : " + m);
        System.out.println("Random Number(y) : " + y);
        System.out.println("Cipher 1 : " + c1);
        System.out.println("Cipher 2 : " + c2);


        Point Pr = ec.subtract(c2, ec.multiply(key, c1));
        BigInteger result = ec.decode(Pr);
        System.out.println("\n### Dekripsi . . .");
        System.out.println("Decrypted Point : " + Pr);
        System.out.println("Message : " + result);
        System.out.println(result.equals(message) ? "Success!!!" : "Failed!!!");
    }
} 
