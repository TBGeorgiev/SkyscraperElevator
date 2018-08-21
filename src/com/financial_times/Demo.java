package com.financial_times;

import java.util.Scanner;

/**
 * @author Georgiev
 * Elevator system application which allows the client to configure 
 * the elevator's parameters and simulate possible real-world scenarios 
 * and observe the elevator's behavior in different situations.
 *
 */
public class Demo {
	private static Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) {
		ElevatorManager elevatorManager = getElevatorAndBuildingStats();
		configureAndExecute(elevatorManager);
	}

	private static void configureAndExecute(ElevatorManager elevatorManager) {
		try {
			System.out.println("How many active elevators do you want to enable?");
			try {
				int numberOfElevators = Integer.parseInt(scanner.nextLine());
				elevatorManager.configureElevators(numberOfElevators);
				elevatorManager.execute();
			} catch (NumberFormatException e) {
				System.out.println("Please use only digits when selecting in the menu.");
				configureAndExecute(elevatorManager);
			}
		} catch (ElevatorException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private static ElevatorManager getElevatorAndBuildingStats() {
		ElevatorManager elevatorManager = null;
		try {
			System.out.println("START ELEVATOR CONFIGURATION");
			System.out.println("Enter the maximum number of people allowed in an elevator: ");
			int maxPersonCapacity = Integer.parseInt(scanner.nextLine());
			System.out.println("Enter the maximum weight an elevator can support: ");
			int maxWeight = Integer.parseInt(scanner.nextLine());
			System.out.println("Enter the building's lowest floor: ");
			int lowestFloor = Integer.parseInt(scanner.nextLine());
			System.out.println("Enter the building's highest floor: ");
			int highestFloor = Integer.parseInt(scanner.nextLine());
			System.out.println("CONFIGURATION COMPLETE");
			elevatorManager = new ElevatorManager(maxPersonCapacity, maxWeight, lowestFloor, highestFloor);
			
		} catch (NumberFormatException e) {
			System.out.println("Please use only digits when selecting in the menu.");
			return getElevatorAndBuildingStats();
		} catch (ElevatorManagerException e) {
			System.out.println(e.getMessage());
			return getElevatorAndBuildingStats();
		}
		return elevatorManager;
	}
	
}
