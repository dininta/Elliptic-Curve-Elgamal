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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
        Point[] publicKey = new Point[2]; 
        BigInteger basex = new BigInteger(base);
        do {
            basex = basex.add(BigInteger.ONE).mod(curve.getP());
            publicKey[0] = curve.calculatePoint(basex);
        } while (!curve.checkOnCurve(publicKey[0]));
        publicKey[1] = curve.multiply(new BigInteger(key), publicKey[0]);
        writeFile(key, "key.pri");
        writeFile(publicKey[0] + "\n" + publicKey[1], "key.pub");
    }
    
    public void encrypt(String plaintextFile, String privateKeyFile, String publicKeyFile) {
        // Print plaintext
        String content = readFile(plaintextFile);
        System.out.println("PLAINTEXT");
        System.out.println(content + "\n");
        
        long startTime = System.currentTimeMillis();
        BigInteger[] plaintext = fileToBigInts(plaintextFile);
        Point[] publicKey = getPublicKey(publicKeyFile);
        BigInteger privateKey = getPrivateKey(privateKeyFile);
        
        List<Point[]> ciphertext = new ArrayList<Point[]>();
        for (int i=0; i<plaintext.length; i++) {
            Point[] temp = new Point[2];
            temp[0] = curve.multiply(privateKey, publicKey[0]);
            Point message = curve.encode(plaintext[i]);
            Point multiply = curve.multiply(privateKey, publicKey[1]);
            temp[1] = curve.add(message, multiply);
            ciphertext.add(temp);
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<ciphertext.size(); i++) {
            sb.append(ciphertext.get(i)[0].getX().toString(16) + "\n");
            sb.append(ciphertext.get(i)[0].getY().toString(16) + "\n");
            sb.append(ciphertext.get(i)[1].getX().toString(16) + "\n");
            sb.append(ciphertext.get(i)[1].getY().toString(16) + "\n");
        }
        writeFile(sb.toString(), "ciphertext.txt");
        long estimatedTime = System.currentTimeMillis() - startTime;
        
        // Print ciphertext
        System.out.println("CIPHERTEXT");
        System.out.println(sb.toString());
        
        File file = new File("ciphertext.txt");
        System.out.println("Ciphertext file size: " + file.length() + " bytes");
        System.out.println("Time elapsed: " + estimatedTime + " ms");
    }
    
    public void decrypt(String ciphertextFile, String plaintextFile, String privateKeyFile) throws FileNotFoundException, IOException {
        long startTime = System.currentTimeMillis();
        BigInteger privateKey = getPrivateKey(privateKeyFile);
        String[] splited = readFile(ciphertextFile).split("\\s+");
        
        byte[] bytes = null;
        for (int i=0; i<splited.length; i+=4) {
            Point[] cipher = new Point[2];
            cipher[0] = new Point(new BigInteger(splited[i], 16), new BigInteger(splited[i+1], 16));
            cipher[1] = new Point(new BigInteger(splited[i+2], 16), new BigInteger(splited[i+3], 16));
            
            Point multiply = curve.multiply(privateKey, cipher[0]);
            Point message = curve.subtract(cipher[1], multiply);
            byte[] decodedMessage = curve.decode(message).toByteArray();
            if (bytes == null) {
                bytes = decodedMessage;
                continue;
            }
            if (decodedMessage == null) {
                continue;
            }
            byte[] tmp = new byte[bytes.length + decodedMessage.length];
            System.arraycopy(bytes, 0, tmp, 0, bytes.length);
            System.arraycopy(decodedMessage, 0, tmp, bytes.length, decodedMessage.length);
            bytes = tmp; 
        }
        
        FileOutputStream fos = new FileOutputStream(plaintextFile);
        fos.write(bytes);
        fos.close();
        
        File file = new File(plaintextFile);
        System.out.println("Plaintext file size: " + file.length() + " bytes");
        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("Time elapsed: " + estimatedTime + " ms");
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
            int lastIdx = (i+1)*maxByteSize;
            if (lastIdx > bFile.length)
                lastIdx = bFile.length;
            byte[] bytes = Arrays.copyOfRange(bFile, i*maxByteSize, lastIdx);
            result[i] = new BigInteger(bytes);
        }
        return result;
    }
    
    public Point[] getPublicKey(String filename) {
        String[] splited = readFile(filename).split("\\s+");
        Point[] p = new Point[2];
        p[0] = new Point(new BigInteger(splited[0]), new BigInteger(splited[1]));
        p[1] = new Point(new BigInteger(splited[2]), new BigInteger(splited[3]));
        return p;
    }
    
    public BigInteger getPrivateKey(String filename) {
        String[] splited = readFile(filename).split("\\s+");
        return new BigInteger(splited[0]);
    }
    
    public void writeFile(String content, String filename) {
        try {
            File file = new File(filename);
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e) {}
    }
    
    public String readFile(String filename) {
        try {
            InputStream is = new FileInputStream(filename);
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));
            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();
            while(line != null){
                sb.append(line).append(" ");
                line = buf.readLine();
            }
            return sb.toString();
        } catch (Exception e) {}
        return null;
    }
}
