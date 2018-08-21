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
	private LinkedList<Request> requestsList;
	private FileInputStream fileInputStream = null;
	private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	private int startingFloor;
	private int requestedFloor;
	private int passengersCount;
	private int passengersWeight;

	public void gatherRequestsFromConsole() throws ElevatorException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("How many requests would you like to process?");
		try {
			int numberOfRequests = Integer.parseInt(reader.readLine());
			requestsList = new LinkedList<Request>();
			for (int i = 0; i < numberOfRequests; i++) {
				System.out.println("Enter request #" + (i + 1) + " starting floor:");
				startingFloor = Integer.parseInt(reader.readLine());
				System.out.println("Enter request #" + (i + 1) + " desired floor:");
				requestedFloor = Integer.parseInt(reader.readLine());
				System.out.println("Enter the number of passengers: ");
				passengersCount = Integer.parseInt(reader.readLine());
				System.out.println("Enter their total weight:");
				passengersWeight = Integer.parseInt(reader.readLine());
				Elevator.Request request = new Request(startingFloor, requestedFloor, passengersCount,
						passengersWeight);
				if (canProcessRequest(request)) {
					requestsList.add(request);					
					System.out.println("Request added.");
				}
			}

		} catch (NumberFormatException | IOException e) {
			System.out.println("Incorrect input! Please start again.");
			this.gatherRequestsFromConsole();
		}

	}

	public void gatherRequestsFromFile() throws IOException, ElevatorException {
		System.out.println("Enter the file's absolute path:");
		String fileLocation = reader.readLine();
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
				Elevator.Request request = new Request(startingFloor, requestedFloor, passengersCount,
						passengersWeight);
				if (canProcessRequest(request)) {
					requestsList.add(request);
					System.out.println("Request added.");
					count++;
				}
			}
			System.out.println("Added a total of " + count + " requests.");
			reader.close();
		}
	}

	LinkedList<Request> fetch() throws ElevatorException, NumberFormatException {
		
		System.out.println(
				"Do you want to populate the requests through the console or through a text file?\n1: Console\n2: Text file");
		int choice;
		try {
			choice = Integer.parseInt(reader.readLine());
			switch (choice) {
			case 1:
				this.gatherRequestsFromConsole();
				break;
			case 2:
				this.gatherRequestsFromFile();
				break;
			default:
				System.out.println("Incorrect input. Please try again.");
				return fetch();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("You can only use the selected digits as input. Please try again.");
			return fetch();
		}
		return requestsList;
	}
	
	private boolean canProcessRequest(Request request) {
		if (request.passengersCount > ElevatorManager.getMaxElevatorPassengersCount()) {
			System.out.println("Can't process the request. \nCurrent passenger count: " + request.passengersCount + "\nPassenger limit is set to: " + ElevatorManager.getMaxElevatorPassengersCount());
			return false;
		}
		if (request.passengersWeight > ElevatorManager.getMaxElevatorCapacity()) {
			System.out.println("Can't process the request. Current weight: " + request.passengersWeight + "\nWeight limit is set to: " + ElevatorManager.getMaxElevatorCapacity() + " kg.");
			return false;
		}
		if (!(Elevator.isValidFloor(request.startingFloor)) || !(Elevator.isValidFloor(request.requestedFloor))) {
			System.out.println("Can't process the request. Starting and/or requested floor is outside the range.");
			return false;
		}
		return true;
	}
}
