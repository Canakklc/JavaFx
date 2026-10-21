package com.example;

import java.util.ArrayList;
import java.util.stream.IntStream;
import java.util.Scanner;
import java.io.File;

public class testingZone {


    public static class Account{

    private int accountNum;
    public String accountName;
    private double balance;
       
        public Account(int accountNum, String accountName, double balance){
           this.accountNum = accountNum;
           this.accountName = accountName;
           this.balance = balance;
        }

        public  void  ShowIntel(){
          String text = String.format("Holder Name: %s\nAccount Number: %d\nBalance: %.2f", accountName, accountNum, balance);
          System.out.println(text);
        }


    }
    public static void main(String[] args) {

        Scanner user = new Scanner(System.in);
        System.out.println("Hello,enter your name please");
        String userInput = user.nextLine();

        try{
            Scanner scanner = new Scanner( new File("datas.txt"));
            
            while(scanner.hasNextLine()){
                String line =scanner.nextLine();
                String lines[]= line.split(",");
                String accountNumbString = String.valueOf(lines[0].trim());
                String ownerName = lines[1].trim();
                String balanceString = String.valueOf(lines[2].trim());

                

                for(int i =0;i<lines.length;i++){
                    if(lines[i].contains(userInput)){
                        testingZone.Account choosenCustomer = new Account(Integer.parseInt(accountNumbString), ownerName, Double.parseDouble(balanceString));
                        choosenCustomer.ShowIntel();
                        
                    }
                }


                
            }

        }catch(Exception e){
            System.out.println("File could not have been found");
            System.out.println(e);
        }        
    }
}
