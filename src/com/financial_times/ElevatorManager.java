package com.financial_times;

import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

import com.financial_times.Elevator.Request;

public final class ElevatorManager {

	private ArrayList<Elevator> elevatorList = null;
	private HashSet<String> elevatorIdList = null;
	private final int maxElevatorPassengersCount;
	private final int maxElevatorCapacity;
	private final int minFloor;
	private final int maxFloor;
	private RequestFetcher requestFetcher = null;

	public ElevatorManager(int maxElevatorPassengersCount, int maxElevatorCapacity, int minFloor, int maxFloor)
			throws ElevatorManagerException {
		this.elevatorIdList = new HashSet<String>();
		this.requestFetcher = new RequestFetcher();
		if (Elevator.isValidPassengersCount(maxElevatorPassengersCount)
				&& Elevator.isValidWeight(maxElevatorCapacity)) {
			this.maxElevatorPassengersCount = maxElevatorPassengersCount;
			this.maxElevatorCapacity = maxElevatorCapacity;
			this.minFloor = minFloor;
			this.maxFloor = maxFloor;
		} else {
			throw new ElevatorManagerException("Some of the provided data is incorrect! Cannot configure elevators");
		}
	}

	public void configureElevators(int elevatorsCount) {
		this.elevatorList = new ArrayList<Elevator>(elevatorsCount);
		for (int i = 0; i < elevatorsCount; i++) {
			try {
				this.elevatorList.add(new Elevator(this.generateElevatorId(), this.maxElevatorPassengersCount,
						this.maxElevatorCapacity, this.minFloor, this.maxFloor));
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
		System.out.println(sb.toString());
		return sb.toString();
	}

	public void execute() throws ElevatorException {
		while (true) {
			this.manageRequests(this.fetchRequests());
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
		Boolean shouldBeGoingUp = Elevator.isGoingUp(request.startingFloor, request.requestedFloor);
		int mostOptimalFloorDifference = this.maxFloor - this.minFloor; // necessary for defining most optimal elevator
		while (mostOptimalElevator == null) {
			for (Elevator e : this.elevatorList) {
				if (e.canTakeMorePassengers()) {
					if (e.getIsGoingUp() == shouldBeGoingUp || null == shouldBeGoingUp ) {
						int currentFloorDifference = Math.abs(e.getCurrentFloor() - request.startingFloor);
						if (currentFloorDifference <= mostOptimalFloorDifference) {
							mostOptimalFloorDifference = currentFloorDifference;
							mostOptimalElevator = e;
						}
					}
				}
			}
		}
		return mostOptimalElevator;
	}

}