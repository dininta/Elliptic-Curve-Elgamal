/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ellipticcurveelgamal;

import java.math.BigInteger;

/**
 *
 * @author Wiwit Rifa'i
 */
public class Point {
    private BigInteger x, y;
    public Point() {
    }
    public Point(BigInteger x, BigInteger y) {
      this.x = x;
      this.y = y;
    }
    public BigInteger getX() {
      return this.x;
    }
    public BigInteger getY() {
      return this.y;
    }
    public void setX(BigInteger x) {
      this.x = x;
    }
    public void setY(BigInteger y) {
      this.y = y;
    }
    public boolean equals(Point point) {
      return this.x.equals(point.getX()) && this.y.equals(point.getY());
    }
    public String toString() {
      return this.x + "\n" + this.y;
    }
}
