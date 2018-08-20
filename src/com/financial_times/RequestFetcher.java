package com.financial_times;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Scanner;
import com.financial_times.Elevator.Request;

final class RequestFetcher {
	private Scanner scanner = new Scanner(System.in);
	private LinkedList<Request> requestsList = new LinkedList();
	private FileInputStream fileInputStream = null;
	private BufferedReader reader = null;
	private int startingFloor;
	private int requestedFloor;
	private int passengersCount;
	private int passengersWeight;
	
	
	
	public void gatherRequestsFromConsole() throws ElevatorException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("How many requests would you like to process?");
		try {
			int numberOfPeople = Integer.parseInt(reader.readLine());
			for (int i = 0; i < numberOfPeople; i++) {
				System.out.println("Enter request #" + (i+1) + " starting floor:");
				startingFloor = Integer.parseInt(reader.readLine());
				System.out.println("Enter request #" + (i + 1) + " desired floor:");
				requestedFloor = Integer.parseInt(reader.readLine());
				System.out.println("Enter the number of passengers: ");
				passengersCount = Integer.parseInt(reader.readLine());
				System.out.println("Enter their total weight:");
				passengersWeight = Integer.parseInt(reader.readLine());
				Elevator.Request request = new Request(startingFloor, requestedFloor, passengersCount, passengersWeight);
				requestsList.add(request);
				System.out.println("Request added.");
			}
			
		} catch (NumberFormatException | IOException e) {
			// TODO: handle exception
		}
		
	}
	
	
	public void gatherRequestsFromFile() throws IOException, ElevatorException {
		System.out.println("Enter the file's absolute path:");
		String fileLocation = scanner.nextLine();
		File file = new File(fileLocation);
		if (file.exists()) {
			fileInputStream = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(fileInputStream));
			String strLine;
			int count = 0;
			while ((strLine = reader.readLine()) != null) {
				startingFloor = Integer.parseInt(strLine);
				strLine = reader.readLine();
				requestedFloor = Integer.parseInt(strLine);
				strLine = reader.readLine();
				passengersCount = Integer.parseInt(strLine);
				strLine = reader.readLine();
				passengersWeight = Integer.parseInt(strLine);
				Elevator.Request request = new Request(startingFloor, requestedFloor, passengersCount, passengersWeight);
				requestsList.add(request);
				count++;
				
			}
			System.out.println("Added a total of " + count + " requests.");
			reader.close();
		}
	}

	LinkedList<Request> fetch() throws ElevatorException {
		this.gatherRequestsFromConsole();
		return requestsList;
	}

}
