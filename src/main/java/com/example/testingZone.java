package com.example;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

public class testingZone {
            public static  ArrayList<FlightIntel> flightList = new ArrayList<>();
           


    public static class FlightIntel{
        String flight, origin, destination, departure, arrival, airCraft;
       
        public FlightIntel(String flight, String origin, String destination, String departure, String arrival, String airCraft){
            this.flight = flight;
            this.origin = origin;
            this.destination = destination;
            this.departure = departure;
            this.arrival = arrival;
            this.airCraft = airCraft;

        }

       public String getInfo(){
        return String.format("Flight:%s | Origin:%s | Destination:%s | Dep:%s | Arr:%s | Aircraft:%s", 
            flight, origin, destination, departure, arrival, airCraft);
    }

        static void ReadFile(){
           try{
             Scanner scanner = new Scanner(new File("datas.txt"));
             while (scanner.hasNextLine()) {
                String line =scanner.nextLine();
                String []lines = line.split("/");

                
                testingZone.FlightIntel obj = new FlightIntel(lines[0], lines[1], lines[2], lines[3], lines[4], lines[5]);
                testingZone.flightList.add(obj);
            
             }
             
           }
           catch(Exception e){
             System.out.println("error");
            
           }        
        }
    }


    public static void main(String[] args) {
        int index =1;
        testingZone.FlightIntel.ReadFile();

        for(int i = 0; i<testingZone.flightList.size();i++){
            System.out.println(index + ": " + testingZone.flightList.get(i).getInfo());
            index += 1;
        }
    }
}