package com.example;

// Test Comment

// A second test comment

// Wow, a third test comment

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.security.cert.LDAPCertStoreParameters;

public class testingZone {
            public static  ArrayList<FlightIntel> flightList = new ArrayList<>();
            public static ArrayList<Integer> departureMins = new ArrayList<>();
            public static ArrayList<String> arrivalAirports = new ArrayList<>();//Airports to bear in mind
            public static ArrayList<String> depatureSlots = new ArrayList<>();
            public static HashMap<String, Integer> airportAndSlots = new HashMap<>();
            public static boolean[] cond = new boolean[3];
            //1th=checkAircraft 2th= check airport 3th= check if its arrived !dont forget put departure arrival slot
            public static String noFlight;
            public static FlightIntel addedFlight;
            public static int oldTotal;


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
            addedFlight = new FlightIntel(newFlight, newOrigin, newDestination, newDeparture, newArrival, newAircraft);
            CheckAirCraft();
            //newDepartureTime = Integer.parseInt(addedFlight.departure);//Broken 
            //ConvertDepaturesToMin();
            //CheckAirCraft();
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

        public static void ConvertDepaturesToMin(){
            if(addedFlight==null)return;
            for(int i = 0; i<flightList.size();i++){
                String stringTime = flightList.get(i).departure;
                String []Times = stringTime.split(":");
                
                int hour = Integer.parseInt(Times[0]) * 60;
                int min = Integer.parseInt(Times[1]);

                int totalMin = hour + min;
                departureMins.add(totalMin);
                
                
            }
            System.out.println(departureMins);

        }
        public static void CheckAirCraft() {
        String[] newDepartureTime = addedFlight.departure.split(":");
        int newHour = Integer.parseInt(newDepartureTime[0]);
        int newMin = Integer.parseInt(newDepartureTime[1]);
        int newTotalTime = (newHour * 60) + newMin;

        boolean isAllowed = true;

        for (int i = 0; i < flightList.size(); i++) {
            if (addedFlight.airCraft.equals(flightList.get(i).airCraft)) {
                
                String[] oldArrivalTime = flightList.get(i).arrival.split(":");
                int oldHour = Integer.parseInt(oldArrivalTime[0]);
                int oldMin = Integer.parseInt(oldArrivalTime[1]);
                int oldTotalTime = (oldHour * 60) + oldMin;

                int gap = newTotalTime - oldTotalTime;

                if (gap < 45) {
                    isAllowed = false;
                    System.out.println("Abort " + gap);
                    break; 
                }
            }
    }

    if (isAllowed) {
        cond[0]=true;
        System.out.print("Approve"+cond[0]);
        
        
    } else {
        cond[0] = false;
        System.out.println("Abort");
    }
}

        public static void ArrivalDepartureSlots(){//Buraya devam
            for(int i = 0; i<flightList.size(); i++){
                arrivalAirports.add(flightList.get(i).arrival);
                airportAndSlots.put(flightList.get(i).origin, 2);
                depatureSlots.add(flightList.get(i).departure);
                airportAndSlots.put(flightList.get(i).destination,2);
                }
            
            System.out.print(arrivalAirports );
            System.out.print(depatureSlots);
            System.out.print(airportAndSlots);


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
        //testingZone.FlightIntel.ConvertDepaturesToMin();
        
        testingZone.FlightIntel.ArrivalDepartureSlots();
    }
}