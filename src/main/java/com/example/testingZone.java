package com.example;

// Test Comment

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
    public static ArrayList<FlightIntel> flightList = new ArrayList<>();
    public static ArrayList<Integer> departureMins = new ArrayList<>();
    public static boolean[] cond = new boolean[4];
    // 0 -> check aircraft conflict 1-> check airport conflict 2-> check validation
    public static String noFlight;
    public static FlightIntel addedFlight;
    public static int oldTotal;
    public static int[] flightTimesMin;//IDK ABOUT THIS
    //globalization for added flight mins
    public static int addedPlaneGap;
    public static int newTotalTime;//departure
    public static int oldTotalTime;//arrival

    public static class FlightIntel {
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
            CheckForOverlap();
            CheckDepArriveValidation();

            //if all conds are fulfilled
            if(cond[0] && cond[1] && cond[2] && cond[3] == true){
                flightList.add(addedFlight);
                //Add to Datas
                String addedFlightData = (addedFlight.flight + "/" + addedFlight.origin +"/"+ addedFlight.destination +"/" + addedFlight.departure+"/"+ addedFlight.arrival+"/"+ addedFlight.airCraft);
    
                try(FileWriter fr = new FileWriter("datas.txt", true)){
                    PrintWriter pw = new PrintWriter(fr);
                    pw.println(addedFlightData);
                } catch(Exception e){
                    System.out.println(e);
                }
            }else{
                System.out.println("One of the conditions or multiple is");
            }
            
        }

        public static void ConvertDepaturesToMin(){
            if(addedFlight == null) return;
            for(int i = 0; i < flightList.size(); i++){
                String stringTime = flightList.get(i).departure;
                String[] Times = stringTime.split(":");
                
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
            newTotalTime = (newHour * 60) + newMin;

            boolean isAllowed = true;

            for (int i = 0; i < flightList.size(); i++) {
                if (addedFlight.airCraft.equals(flightList.get(i).airCraft)) {
                    
                    String[] oldArrivalTime = flightList.get(i).arrival.split(":");
                    int oldHour = Integer.parseInt(oldArrivalTime[0]);
                    int oldMin = Integer.parseInt(oldArrivalTime[1]);
                    oldTotalTime = (oldHour * 60) + oldMin;

                    addedPlaneGap = newTotalTime - oldTotalTime;

                    if (addedPlaneGap < 45) {
                        isAllowed = false;
                        System.out.println("Abort " + addedPlaneGap);
                        break; 
                    }
                }
            } 

            if (isAllowed) {
                cond[0] = true;
                System.out.print("Approve" + cond[0]);
            } else {
                cond[0] = false;
                System.out.println("Abort");
            }
        }

        public static void CheckForOverlap(){
            int arrivalCounter = 0;
            for(int i = 0; i<flightList.size();i++){
                //datas of CURRENT flights (departure)
                String[] currentsDep = flightList.get(i).departure.split(":");
                int currentDepHour = Integer.parseInt(currentsDep[0]);
                int currentDepMin = Integer.parseInt(currentsDep[1]);
                //total min to be compared (departure)
                int currentsDepTotal = (currentDepHour * 60) + currentDepMin;
                
                //datas of current flights(arrival)
                String[] currentsArrival = flightList.get(i).arrival.split(":");
                int currentArrivalHour = Integer.parseInt(currentsArrival[0]);
                int currentArrivalMin = Integer.parseInt(currentsArrival[1]);
                //total min to be compared(arrival)
                int currentArrvTotal = (currentArrivalHour * 60) + currentArrivalMin;

                //datas of NEW FLIGHT(departure)
                String[] addedDeparture = addedFlight.departure.split(":");
                int addedDepHour = Integer.parseInt(addedDeparture[0]);
                int addedDepMin = Integer.parseInt(addedDeparture[1]);
                //total min to be compared(departure)
                int arrivalDepTotal = (addedDepHour * 60) + addedDepMin;

                //datas of newFlight flight(arrival)
                String[] addedArrival = addedFlight.arrival.split(":");
                int addedArriveHour = Integer.parseInt(addedArrival[0]);
                int addedArriveMin = Integer.parseInt(addedArrival[1]);             
                //total min to be compared (arrival)
                int arrivalAddedTotal = (addedArriveHour * 60) + addedArriveMin;

                //origin arrival bools for airport
                boolean originCheck = addedFlight.origin.equals(flightList.get(i).origin);
                boolean arrivalCheck = addedFlight.destination.equals(flightList.get(i).destination);

                if(originCheck || arrivalCheck){
                    if(arrivalAddedTotal < currentsDepTotal || arrivalDepTotal > currentArrvTotal){
                        System.out.println("no conlict");
                    }else{
                        System.out.println("Added with conflict");
                        arrivalCounter += 1;
                    }
                }
                
            }
            if(arrivalCounter >= 2){
                System.out.println("Limit exceted" + arrivalCounter);
                cond[1]=false;
            }else{
                System.out.println("Confirmed" + arrivalCounter);
                cond[1] = true;
            }
            
        }

        public static void CheckDepArriveValidation(){//if dep bigger than arr
            //departureVals
            String[] departureAdded = addedFlight.departure.split(":");
            int departureHour = Integer.parseInt(departureAdded[0]);
            int departureMin = Integer.parseInt(departureAdded[1]);
            //total departures
            int departureTotal = (departureHour * 60) + departureMin;
            //arrivalVals
            String[] arrivalAdded = addedFlight.arrival.split(":");
            int arrivalHour = Integer.parseInt(arrivalAdded[0]);
            int arrivalMin = Integer.parseInt(arrivalAdded[1]);
            //arrival total
            int arrivalTotal = (arrivalHour * 60) + arrivalMin;

            if(departureTotal >= arrivalTotal){
                cond[2] = false;
                System.out.println("Dep cannot be bigger than arrival");
            }else{
                cond[2]= true;
                System.out.println("valid success");
            }

            //if dep or arr are out of scale 00.00 and 23.59 (1416)
            int trashold = 1416;
            boolean rangeCheck = departureTotal > trashold || arrivalTotal > 1416;

            if(!rangeCheck){
               System.out.println("Range is valid");
               cond[3] = true;
            }else{
                System.out.println("Unvalid range");
                cond[3]= false;
            }
        }

        public String getInfo(){
            return String.format("Flight:%s | Origin:%s | Destination:%s | Dep:%s | Arr:%s | Aircraft:%s", 
                flight, origin, destination, departure, arrival, airCraft);
        }

        @Override 
        public String toString(){
            return String.format("Flight:%s | Origin:%s | Destination:%s | Dep%s | Arrival:%s | Aircraft:%s", flight, origin, destination, departure, arrival, airCraft);
        }

        static void ReadFile(){
           try{
              Scanner scanner = new Scanner(new File("datas.txt"));
              while (scanner.hasNextLine()) {
                 String line = scanner.nextLine();
                 String[] lines = line.split("/");

                 testingZone.FlightIntel obj = new FlightIntel(lines[0], lines[1], lines[2], lines[3], lines[4], lines[5]);
                 testingZone.flightList.add(obj);
              }
           } catch(Exception e){
              System.out.println("error");
           }        
        }

        static FlightIntel SearchForFlight(String input){
            ArrayList<FlightIntel> foundFlightList = new ArrayList<>();

            try {
                Scanner scanner = new Scanner(new File("datas.txt"));
                while(scanner.hasNextLine()){
                    String line = scanner.nextLine();
                    String[] lines = line.split("/");

                    if(lines.length >= 6) { 
                        FlightIntel obj = new FlightIntel(lines[0], lines[1], lines[2], lines[3], lines[4], lines[5]);
                        foundFlightList.add(obj);
                        String newInput = input.toUpperCase().trim();

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
        int index = 1;
        testingZone.FlightIntel.ReadFile();
        FlightIntel.SearchForFlight("TK1001");
        for(int i = 0; i < testingZone.flightList.size(); i++){
            System.out.println(index + ": " + testingZone.flightList.get(i).getInfo());
            index += 1;
        }
    }
}