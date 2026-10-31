package com.example;

import java.util.ArrayList;
import java.util.Scanner;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.security.cert.LDAPCertStoreParameters;

public class testingZone {
            public static  ArrayList<FlightIntel> flightList = new ArrayList<>();
            public static String noFlight;


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
        String newFlight, newOrigin, newDestination, newDeparture, newArrival, newAircraft;
        
        //Method Intel
        public static void AddFlight(String newFlight, String newOrigin, String newDestination, String newDeparture, String newArrival, String newAircraft ){
            FlightIntel addedFlight = new FlightIntel(newFlight, newOrigin, newDestination, newDeparture, newArrival, newAircraft);
            flightList.add(addedFlight);
            //Add to Datas
            String addedFlightData=(addedFlight.flight + "/" + addedFlight.origin +"/"+ addedFlight.destination +"/" + addedFlight.departure+"/"+ addedFlight.arrival+"/"+ addedFlight.airCraft);
            try(FileWriter fr = new FileWriter("datas.txt",true)){
                PrintWriter pw = new PrintWriter(fr);

                pw.println(addedFlightData);
            }catch(Exception e){
                System.out.println(e);
            }

            

            
        }

       public String getInfo(){
        return String.format("Flight:%s | Origin:%s | Destination:%s | Dep:%s | Arr:%s | Aircraft:%s", 
            flight, origin, destination, departure, arrival, airCraft);
        }

        @Override 
        public String toString(){
            return String.format("Flight:%s | Origin:%s | Destination:%s | Dep%s | Arrival:%s | Aircraft:%s",flight,origin,destination,departure,arrival,airCraft);
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

        static FlightIntel SearchForFlight(String input){
    ArrayList<FlightIntel> foundFlightList = new ArrayList<>();

    try {
        Scanner scanner = new Scanner(new File("datas.txt"));
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String lines[] = line.split("/");

            if(lines.length >= 6) { 
                FlightIntel obj = new FlightIntel(lines[0], lines[1], lines[2], lines[3], lines[4], lines[5]);
                foundFlightList.add(obj);
                String newInput =input.toUpperCase().trim();

                if(newInput != null && obj.flight != null && newInput.contains(obj.flight)){
                    scanner.close();
                    System.out.println(obj.flight);
                    return obj;
                    
                }
            }
        }
        scanner.close(); 
    } catch(Exception e) {
        System.out.println(e.getMessage());
    }
    
    return null;
}
    }


    public static void main(String[] args) {
        int index =1;
        testingZone.FlightIntel.ReadFile();
        FlightIntel.SearchForFlight("TK1001");

        for(int i = 0; i<testingZone.flightList.size();i++){
            System.out.println(index + ": " + testingZone.flightList.get(i).getInfo());
            index += 1;
        }
    }
}