package com.financial_times;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

import com.financial_times.Elevator.Request;

public final class ElevatorManager {

	private ArrayList<Elevator> elevatorList = null;
	private HashSet<String> elevatorIdList = null;
	private static int maxElevatorPassengersCount;
	private static int maxElevatorCapacity;
	private static int minFloor;
	private static int maxFloor;
	private RequestFetcher requestFetcher = null;

	public ElevatorManager(int maxElevatorPassengersCount, int maxElevatorCapacity, int minFloor, int maxFloor)
			throws ElevatorManagerException {
		this.elevatorIdList = new HashSet<String>();
		this.requestFetcher = new RequestFetcher();
		if (Elevator.isValidPassengersCount(maxElevatorPassengersCount)
				&& Elevator.isValidWeight(maxElevatorCapacity)) {
			ElevatorManager.maxElevatorPassengersCount = maxElevatorPassengersCount;
			ElevatorManager.maxElevatorCapacity = maxElevatorCapacity;
			ElevatorManager.minFloor = minFloor;
			ElevatorManager.maxFloor = maxFloor;
		} else {
			throw new ElevatorManagerException("Some of the provided data is incorrect! Cannot configure elevators");
		}
	}

	public void configureElevators(int elevatorsCount) {
		this.elevatorList = new ArrayList<Elevator>(elevatorsCount);
		for (int i = 0; i < elevatorsCount; i++) {
			try {
				this.elevatorList.add(new Elevator(this.generateElevatorId(), ElevatorManager.maxElevatorPassengersCount,
						ElevatorManager.maxElevatorCapacity, ElevatorManager.minFloor, ElevatorManager.maxFloor));
			} catch (ElevatorException e) {
				System.err.println("[ERROR] Improper data for elevator configuration: " + e.getMessage());
				System.err.flush();
			}
		}
	}

	private String generateElevatorId() {
		String charset = "ABC";
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		sb.append(charset.charAt(random.nextInt(charset.length()))).append(random.nextInt(99) + 1);
		return sb.toString();
	}

	public void execute() throws ElevatorException {
		while (true) {
			this.manageRequests(this.fetchRequests());
			try
			{
				if (!continueRequests()) {
					break;
				}
			} catch (ElevatorManagerException e)
			{
				System.out.println(e.getMessage());
			}
		}
		System.out.println("Program closed.");
	}
	
	private boolean continueRequests() throws ElevatorManagerException {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Would you like to process more requests?\nY / N: ");
		String userInput = scanner.nextLine();
		if (userInput.equalsIgnoreCase("y")) {
			return true;
		}
		else if (userInput.equalsIgnoreCase("n")) {
			return false;
		} else {
			System.out.println("Incorrect input. Please use 'y' or 'n' as an answer.");
			return continueRequests();
			
		}
	}
	
	

	private void manageRequests(LinkedList<Request> requestList) {
		for (Request r : requestList) {
			this.getMostOptimalElevator(r).processRequest(r);		
		}
	}

	private LinkedList<Elevator.Request> fetchRequests() throws ElevatorException {
		return this.requestFetcher.fetch();
	}

	private Elevator getMostOptimalElevator(Request request) {
		Elevator mostOptimalElevator = null;
		int mostOptimalFloorDifference = ElevatorManager.maxFloor - ElevatorManager.minFloor; // necessary for defining most optimal elevator
		while (mostOptimalElevator == null) {
			for (Elevator e : this.elevatorList) {
				if (e.canTakeMorePassengers()) {
					int currentFloorDifference = Math.abs(e.getCurrentFloor() - request.startingFloor);
					if (currentFloorDifference <= mostOptimalFloorDifference) {
						mostOptimalFloorDifference = currentFloorDifference;
						mostOptimalElevator = e;
					}
				}
			}
		}
		return mostOptimalElevator;
	}

	static int getMaxElevatorPassengersCount() {
		return maxElevatorPassengersCount;
	}

	static int getMaxElevatorCapacity() {
		return maxElevatorCapacity;
	}
	
	
}