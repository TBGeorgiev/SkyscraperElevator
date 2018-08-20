package com.financial_times;

public class Demo {

	public static void main(String[] args) {
		try {
			ElevatorManager elevatorManager = new ElevatorManager(6, 500, 1, 10);
			try {
				elevatorManager.configureElevators(3);
				elevatorManager.execute();
			} catch (ElevatorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			RequestFetcher requestFetcher = new RequestFetcher();
//			requestFetcher.gatherRequestsFromConsole();
			
			
		} catch (ElevatorManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
