package com.finalproject.ai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProjectApp {
	int maxPit = 6;
	public static void main(String[] args) {
		OwareGame.killerMove = new int[25];
		ProjectApp app = new ProjectApp();
		app.playGame();
	}
	
	public void playGame() {
		System.out.println("Artificial Intelligence Final Project - Fall 2018");
		System.out.print("Welcome to Oware!\n");
		System.out.print("Do you want the Computer to make the first move (Y/N)? ");
		String input;
		int result = 0, c =0;
		char ac = '.';
		long time=0;
		int level = 1;
		OwareGame game = new OwareGame(maxPit);
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			input = br.readLine();
			if(input.equalsIgnoreCase("y")) {
				game.setComputerPlays(true);
			}else if(input.equalsIgnoreCase("n")) {
				game.setComputerPlays(false);				
			}
			
			do {	
				game.displayGameBoard();
				if(game.isComputerPlays()) {
					System.out.println("Computer turn to play......");
					long t1 = System.currentTimeMillis();
					result = computerPlays(game);
					time = time + (System.currentTimeMillis() -t1);
					
				}else {
					System.out.println("Your turn to play......");
					System.out.printf("Your Move: ");
					String a = br.readLine();
					ac = a.charAt(0);
					switch(ac) {
					case 'a':
						result = game.makeMove(0);
						break;
					case 'b':
						result = game.makeMove(1);
						break;

					case 'c':
						result = game.makeMove(2);
						break;

					case 'd':
						result = game.makeMove(3);
						break;

					case 'e':
						result = game.makeMove(4);
						break;

					case 'f':
						result = game.makeMove(5);
						break;
					default:
						System.out.println("Select the write pit to play!!!...try again");
						break;
					}	
				}
			}while(ac != 'x' && ac != 'X' && result != 1);
			System.out.print("Final board scenario:\n");
			game.displayGameBoard();
			System.out.print("Goodbye! Hope to see you again. \n");
			System.out.printf("Avg time taken per move: %3.2f s",((double)time/(1000*c)));			
		}catch(Exception e) {
			
		}
	}
	
	public int computerPlays(OwareGame game) {
		int i;
				
		OwareGame tmp, t1;
		MiniMax winningChance[] = new MiniMax[maxPit];
		tmp = new OwareGame(game); // copy
		double alpha = -1000, beta = 1000;
		int move = OwareGame.killerMove[0], res = move;
		int scoreDiff[] = new int[6];

		for (i = 0; i < 6; i++) 
		{
			if (tmp.validMove(move)) 
			{
				t1 = new OwareGame(tmp);
				tmp.simulateMove(move);
				winningChance[move] = tmp.minimax(1, alpha, beta, 17+(int)(((double)tmp.computerSeeds + (double)tmp.humanSeeds)/8));
				if(winningChance[move].score > alpha)
				{
					res = move;
					scoreDiff[move] = tmp.computerSeeds - tmp.humanSeeds;
					alpha = winningChance[move].score ;
					OwareGame.killerMove[0] = move;
				}
				else if(winningChance[move].score == alpha )
				{
					if (winningChance[res].closestWinDepth > winningChance[move].closestWinDepth)
						res = move;
					else if(winningChance[res].closestWinDepth == winningChance[move].closestWinDepth && scoreDiff[res] < scoreDiff[move])
						res = move;
				}
				tmp = new OwareGame(t1);
			} 
			move = (move + 1)%6;
		}
		System.out.printf("\nComputer's Move: %c\n", 65 + res);
		return game.makeMove(res);		
	}
	
	public int humanPlayerMove(BufferedReader br, OwareGame game) throws IOException {
		System.out.println("Your turn to play......");
		System.out.printf("Your Move: ");
		String a = br.readLine();
		char ac = a.charAt(0);
		int result =0;
		switch(ac) {
		case 'a':
			result = game.makeMove(0);
			break;
		case 'b':
			result = game.makeMove(1);
			break;

		case 'c':
			result = game.makeMove(2);
			break;

		case 'd':
			result = game.makeMove(3);
			break;

		case 'e':
			result = game.makeMove(4);
			break;

		case 'f':
			result = game.makeMove(5);
			break;
		default:
			System.out.println("Select the write pit to play!!!...try again");
			break;
		}	
		
		return result;
	}
	

}
