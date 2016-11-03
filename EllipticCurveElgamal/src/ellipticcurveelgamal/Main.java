/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ellipticcurveelgamal;

import java.util.Scanner;

/**
 *
 * @author TOSHIBA PC
 */
public class Main {
    
    public static void main(String[] args) {
        EllipticCurveElgamal elgamal = new EllipticCurveElgamal();
        Scanner input = new Scanner(System.in);
        System.out.println("CHOOSE AN ACTION");
        System.out.println("1. Build Key");
        System.out.println("2. Encrypt");
        System.out.println("3. Decrypt");
        System.out.print("your input: ");
        String choice = input.nextLine(); System.out.println();
        if (choice.equals("1")) {
            // Build key
            elgamal.buildKey();
        } else if (choice.equals("2")) {
            // Encrypt
            System.out.print("Enter filename for plaintext: ");
            String plaintextFile = input.nextLine();
            System.out.print("Enter filename for your private key: ");
            String privateKeyFile = input.nextLine();
            System.out.print("Enter filename for receiver's public key: ");
            String publicKeyFile = input.nextLine();
            elgamal.encrypt(plaintextFile, privateKeyFile, publicKeyFile);
        } else {
            // Decrypt
            System.out.print("Enter filename for ciphertext: ");
            String ciphertextFile = input.nextLine();
            System.out.print("Enter filename for your private key: ");
            String privateKeyFile = input.nextLine();
            elgamal.decrypt(ciphertextFile, privateKeyFile);
        }
    }
    
}
