package hr.fer.oprpp1.hw05.crypto;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Crypto {

    public static String digest(String input){

    }

    public static void main(String[] args) {
        if(args.length < 2 || args.length > 3)
            throw new IllegalArgumentException("Program expected 2 or 3 arguments, but it received " + args.length + " arguments!");

        try{
            Scanner sc = new Scanner(System.in);
            if(args[0].equalsIgnoreCase("checksha")){
                if(args.length != 2 || !args[1].endsWith(".bin"))
                    throw new IllegalArgumentException("checksha command takes exactly one .bin file as argument!");
                System.out.println("Please provide expected sha-256 digest for " + args[1] + ":");
                String digest = sc.nextLine().trim();

                System.out.print("Digest completed!");
                if(digest.equals(digest(digest)))
                    System.out.println("Digest of " + args[1] + " matches expected digest!");
                else
                    System.out.println("Digest of " + args[1] + " does not expected digest! Digest was " + digest(digest));

            }

        } catch(Exception e){
            System.out.println(e.getMessage());
        }

    }
}
