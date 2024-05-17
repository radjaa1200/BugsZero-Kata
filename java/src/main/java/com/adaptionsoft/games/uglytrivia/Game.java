package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;

public class Game {

	//Variables needs to be private

	private ArrayList<String> players = new ArrayList<>();
	private int[] places = new int[6];
	private int[] purses = new int[6];
	private boolean[] inPenaltyBox = new boolean[6];

	private LinkedList<String> popQuestions = new LinkedList<>();
	private LinkedList<String> scienceQuestions = new LinkedList<>();
	private LinkedList<String> sportsQuestions = new LinkedList<>();
	private LinkedList<String> rockQuestions = new LinkedList<>();

	private int currentPlayer = 0;
	private boolean isGettingOutOfPenaltyBox;

	public Game() {
	}

	//The name initializeQuestions for this method is more significant
	public void  initializeQuestions(){
    	for (int i = 0; i < 50; i++) {
			popQuestions.addLast("Pop Question " + i);
			scienceQuestions.addLast(("Science Question " + i));
			sportsQuestions.addLast(("Sports Question " + i));
			rockQuestions.addLast("Rock Question " + i);     //Use the same initialization method for RockQuestions
    	}
    }

	//The method createRockQuestion(int index) isn't very helpfull and can be deleted
	/*
	private String createRockQuestion(int index) {
		return "Rock Question " + index;
	}

	 */
	//The method isPlayable() is not used and can be deleted
	/*public boolean isPlayable() {
		return (howManyPlayers() >= 2);
	}
	 */

	//Every function needs to accomplish one single responsability so I propose to divise the add (String ) function
	//into initializePlayerData(int index) and add(String playerName)
	private void initializePlayerData(int index) {
		places[index] = 0;
		purses[index] = 0;
		inPenaltyBox[index] = false;
	}

	public boolean add(String playerName) {
		players.add(playerName);
		initializePlayerData(players.size() - 1);
		System.out.println(playerName + " was added");
		System.out.println("They are player number " + players.size());
		return true;
	}

	//The method howManyPlayers() is not used and can be deleted
	/*
	public int howManyPlayers() {
		return players.size();
	}
	 */

	//The roll(int roll) method can be simplified

	public void roll(int roll) {
		System.out.println(players.get(currentPlayer) + " is the current player");
		System.out.println("They have rolled a " + roll);

		if (inPenaltyBox[currentPlayer]) {
			handlePenaltyBoxRoll(roll);
		} else {
			movePlayerAndAskQuestion(roll);
		}
	}

	private void handlePenaltyBoxRoll(int roll) {
		if (roll % 2 != 0) {
			isGettingOutOfPenaltyBox = true;
			System.out.println(players.get(currentPlayer) + " is getting out of the penalty box");
			movePlayerAndAskQuestion(roll);
		} else {
			System.out.println(players.get(currentPlayer) + " is not getting out of the penalty box");
			isGettingOutOfPenaltyBox = false;
		}
	}

	//movePlayerAndAskQuestion(int roll) can be simplified here we just need
	// to add (places[currentPlayer] + roll) % 12 to the current place
	private void movePlayerAndAskQuestion(int roll) {
		places[currentPlayer] = (places[currentPlayer] + roll) % 12;

		System.out.println(players.get(currentPlayer) + "'s new location is " + places[currentPlayer]);
		System.out.println("The category is " + currentCategory());
		askQuestion();
	}

	//Use String comparisons with switch-case in method askQuestion()
	public void askQuestion() {
		String question = null;
		switch (currentCategory()) {
			case "Pop":
				question = popQuestions.removeFirst();
				break;
			case "Science":
				question = scienceQuestions.removeFirst();
				break;
			case "Sports":
				question = sportsQuestions.removeFirst();
				break;
			case "Rock":
				question = rockQuestions.removeFirst();
				break;
		}
		if (question != null) {
			System.out.println(question);
		}
	}

	//Use String comparisons with switch-case in method currentCategory()
	public String currentCategory() {
		int position = places[currentPlayer];
		switch (position) {
			case 0:
			case 4:
			case 8:
				return "Pop";
			case 1:
			case 5:
			case 9:
				return "Science";
			case 2:
			case 6:
			case 10:
				return "Sports";
			default:
				return "Rock";
		}
	}

	//After playing the wasCorrectlyAnswerd() there is potential infinite Loop
	// The currentPlayer variable is incremented regardless of
	// whether the player is in the penalty box or not.
	// This could potentially lead to an infinite loop
	// if the game doesn't terminate properly when a player wins.
	public boolean wasCorrectlyAnswered() {
		if (inPenaltyBox[currentPlayer]){
			if (isGettingOutOfPenaltyBox) {
				System.out.println("Answer was correct!!!!");
				currentPlayer++;
				if (currentPlayer == players.size()) currentPlayer = 0;
				purses[currentPlayer]++;
				System.out.println(players.get(currentPlayer)
						+ " now has "
						+ purses[currentPlayer]
						+ " Gold Coins.");

				boolean winner = didPlayerWin();

				return winner;
			} else {
				currentPlayer++;
				if (currentPlayer == players.size()) currentPlayer = 0;
				return true;
			}
			
			
			
		} else {
		
			System.out.println("Answer was corrent!!!!");
			purses[currentPlayer]++;
			System.out.println(players.get(currentPlayer) 
					+ " now has "
					+ purses[currentPlayer]
					+ " Gold Coins.");
			
			boolean winner = didPlayerWin();
			currentPlayer++;
			if (currentPlayer == players.size()) currentPlayer = 0;
			
			return winner;
		}
	}

	public boolean wrongAnswer() {
		System.out.println("Question was incorrectly answered");
		System.out.println(players.get(currentPlayer) + " was sent to the penalty box");
		inPenaltyBox[currentPlayer] = true;

		currentPlayer = (currentPlayer + 1) % players.size();
		return true;
	}


	private boolean didPlayerWin() {
		return !(purses[currentPlayer] == 6);
	}
}
