package com.financial_times;

public class Demo {

	public static void main(String[] args) {
		try {
			ElevatorManager elevatorManager = new ElevatorManager(6, 500, 1, 10);
			try {
				elevatorManager.configureElevators(3);
				elevatorManager.execute();
			} catch (ElevatorException e) {
				e.printStackTrace();
			}

		} catch (ElevatorManagerException e) {
			e.printStackTrace();
		}
	}
}
