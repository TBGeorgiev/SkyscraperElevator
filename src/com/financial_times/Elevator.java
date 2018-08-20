package com.financial_times;

public class Elevator {
	// TODO ako ima vreme turi logger obekt

	private final String id;
	private volatile Boolean isGoingUp = null; // null value when elevator has stopped
	private volatile boolean isFull = false;
	private boolean isOverloaded = false;
	private final int maxPassengersCount;
	private final double maxCapacity; // kg
	private volatile int currentFloor = 0;
	private volatile int currentPassengersCount = 0;
	private volatile double currentCapacity = 0;
	private FloorRange floorRange = null;
	// TODO collection for requests

	public Elevator(String id, int maxPassengersCount, double maxCapacity, int minFloor, int maxFloor)
			throws ElevatorException {
		// TODO log elevator execution
		if (!this.isValidId(id)) {
			throw new ElevatorException("Elevator id cannot be null!");
		}
		this.id = id;
		if (!isValidPassengersCount(maxPassengersCount)) {

			throw new ElevatorException("Maximum number of passengers should be assigned with a positive value!");
		}
		this.maxPassengersCount = maxPassengersCount;

		if (!isValidWeight(maxCapacity)) {
			throw new ElevatorException("Maximum capacity should be assigned with a positive value!");
		}
		this.maxCapacity = maxCapacity;
		this.currentFloor = minFloor;
		this.floorRange = new FloorRange(minFloor, maxFloor);
	}

	static boolean isValidWeight(double weight) {
		return weight > 0;
	}

	static boolean isValidPassengersCount(int passengersCount) {
		return passengersCount >= 0;
	}

	private boolean isValidId(String id) {
		return null != id;
	}

	private boolean isValidFloor(int floor) {
		return this.floorRange.containsFloor(floor);
	}

	boolean canTakeMorePassengers() {
		return !(this.isFull || this.isOverloaded);
	}

	boolean isFull() {
		return this.isFull;
	}

	boolean isOverloaded() {
		return this.isOverloaded;
	}

	private class FloorRange {
		private final int minFloor;
		private final int maxFloor;

		private FloorRange(int minFloor, int maxFloor) {
			this.minFloor = minFloor;
			this.maxFloor = maxFloor;
		}

		private boolean containsFloor(int floor) {
			return floor >= minFloor && floor <= maxFloor;
		}
	}

	void processRequest(Request request) {
		System.out.println(request.passengersCount + " passangers got inside the elevator at floor " + currentFloor);
		System.out.println("Weight increased by: " + request.passengersWeight + " kg.");
		this.currentPassengersCount += request.passengersCount;
		this.currentCapacity += request.passengersWeight;
		this.isGoingUp = isGoingUp(request.startingFloor, request.requestedFloor);
		this.transport(request.startingFloor, request.requestedFloor);
		this.stop();
		System.out.println(request.passengersCount + " left the elevator at floor " + currentFloor);
		System.out.println("Weight decreased by: " + request.passengersWeight + " kg.");
		this.currentPassengersCount -= request.passengersCount;
		this.currentCapacity -= request.passengersWeight;
	}

	private void stop() {
		System.out.println("Elevator #" + this.id + " stopped at floor " + this.currentFloor + ".");
		this.isGoingUp = null;
	}

	private void transport(int startFloor, int endFloor) {
		boolean isGoingUp = true;
		if (startFloor > endFloor) {
			int temp = endFloor;
			endFloor = startFloor;
			startFloor = temp;
			isGoingUp = false;
		}
		int currentFloor = startFloor;
		for (int i = startFloor; i < endFloor; i++) {
			// if (this.isGoingUp.booleanValue() == false) { // more costly operation
			if (!isGoingUp) {
				currentFloor = endFloor - i;
				continue;
			}
			currentFloor = i;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {

			}
			System.out.println("Elevator #" + this.id + " is currently at floor " + currentFloor + ".");
			this.currentFloor = currentFloor;
		}
	}

	// q da vida
	static boolean isGoingUp(int startFloor, int endFloor) {
		return startFloor < endFloor ? true : (startFloor > endFloor ? false : null);
	}

	static class Request {
		final int startingFloor;
		final int requestedFloor;
		final int passengersCount;
		final int passengersWeight;

		Request(int startingFloor, int requestedFloor, int passengersCount, int passengersWeight)
				throws ElevatorException {
			// if (!isValidFloor(startingFloor)) {
			// throw new ElevatorException("Starting floor should be assigned with a
			// non-negative value!");
			// }
			this.startingFloor = startingFloor;
			// if (!isValidFloor(requestedFloor)) {
			// throw new ElevatorException("Requested floor should be assigned with a
			// non-negative value!");
			// }
			if (startingFloor == requestedFloor) {
				throw new ElevatorException("Starting floor is the same as the requested floor!");
			}
			this.requestedFloor = requestedFloor;
			if (!isValidPassengersCount(passengersCount)) {
				throw new ElevatorException("Total number of passengers should be assigned with a positive value!");
			}
			this.passengersCount = passengersCount;

			if (!isValidWeight(passengersWeight)) {
				throw new ElevatorException("Total passengers weight should be assigned with a positive value!");
			}
			this.passengersWeight = passengersWeight;
		}

	}

	Boolean getIsGoingUp() {
		return this.isGoingUp;
	}

	int getCurrentFloor() {
		return this.currentFloor;
	}

}