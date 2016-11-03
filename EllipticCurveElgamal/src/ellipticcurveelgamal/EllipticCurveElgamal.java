/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ellipticcurveelgamal;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TOSHIBA PC
 */
public class EllipticCurveElgamal {

    EllipticCurve curve;

    public EllipticCurveElgamal() {
        this.curve = new EllipticCurve();
    }
    
    public void buildKey() {
        
    }
    
    public void encrypt(String plaintextFile, String privateKeyFile, String publicKeyFile) {
        byte[] bFile = fileToBytes(plaintextFile);
        BigInteger[] plaintext = bytesToBigInts(bFile);
        
        // TODO: get key from KeyFile
        Point publicKey = new Point();
        BigInteger privateKey = BigInteger.valueOf(12312);
        
        List<Point[]> ciphertext = new ArrayList<Point[]>();
        for (int i=0; i<plaintext.length; i++) {
            Point[] temp = new Point[2];
            temp[0] = publicKey;
            Point message = curve.encode(plaintext[i]);
            Point multiply = curve.multiply(privateKey, publicKey);
            temp[1] = curve.add(message, multiply);
            ciphertext.add(temp);
        }
        
        // TODO: convert ciphertext to hexa, print, and save to file
    }
    
    public void decrypt(String ciphertextFile, String privateKeyFile) {
        
    }
    
    public byte[] fileToBytes(String filename) {
        FileInputStream fileInputStream=null;
        File file = new File(filename);
        byte[] bFile = new byte[(int) file.length()];

        try {
	    fileInputStream = new FileInputStream(file);
	    fileInputStream.read(bFile);
	    fileInputStream.close();
        } catch(Exception e){
            e.printStackTrace();
        }
        return bFile;
    }
    
    public BigInteger[] bytesToBigInts(byte[] bytes) {
        return new BigInteger[100];
    }
    
}
