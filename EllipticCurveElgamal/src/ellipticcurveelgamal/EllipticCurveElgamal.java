/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ellipticcurveelgamal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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
        Scanner input = new Scanner(System.in);
        System.out.print("Enter private key: ");
        String key = input.nextLine();
        System.out.print("Enter base value: ");
        String base = input.nextLine(); System.out.println();
        
        Point publicKey = curve.multiply(new BigInteger(key), curve.calculatePoint(new BigInteger(base)));
        writeToFile(key, "key.pri");
        writeToFile(publicKey.toString(), "key.pub");
    }
    
    public void encrypt(String plaintextFile, String privateKeyFile, String publicKeyFile) {
        BigInteger[] plaintext = fileToBigInts(plaintextFile);
        Point publicKey = getPublicKey(publicKeyFile);
        BigInteger privateKey = getPrivateKey(privateKeyFile);
        
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
    
    public BigInteger[] fileToBigInts(String filename) {
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
        
        BigInteger maxValue = curve.getP().divide(BigInteger.valueOf(curve.getKoblitz()));
        int maxByteSize = maxValue.toByteArray().length - 1;
        BigInteger[] result = new BigInteger[bFile.length / maxByteSize + 1];
        for (int i=0; i<result.length; i++) {
            int lastIdx = (i+1)*maxByteSize - 1;
            if (lastIdx > bFile.length)
                lastIdx = bFile.length - 1;
            byte[] bytes = Arrays.copyOfRange(bFile, i*maxByteSize, lastIdx);
            result[i] = new BigInteger(bytes);
        }
        
        return result;
    }
    
    public Point getPublicKey(String filename) {
        Point p = new Point();
        try {
            InputStream is = new FileInputStream(filename);
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));
            p.setX(new BigInteger(buf.readLine()));
            p.setY(new BigInteger(buf.readLine()));
        } catch (Exception e) {}
        return p;
    }
    
    public BigInteger getPrivateKey(String filename) {
        try {
            InputStream is = new FileInputStream(filename);
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));
            return new BigInteger(buf.readLine());
        } catch (Exception e) {}
        return BigInteger.ZERO;
    }
    
    public void writeToFile(String content, String filename) {
        try {
            File file = new File(filename);
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e) {}
    }
}
