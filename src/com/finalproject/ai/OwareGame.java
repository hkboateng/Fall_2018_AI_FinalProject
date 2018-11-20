package com.finalproject.ai;

public class OwareGame {

	public static int killerMove[];
	private int computerCells[], humanCells[], numberOfColumns, pitMax = 4;
	private boolean computerPlays;
	int humanSeeds;
	int computerSeeds;
	int emptyComputerPits;
	int emptyPlayerPits;
	
	public OwareGame(int numberOfColumns) {
		this.numberOfColumns = numberOfColumns;
		initializeGame();
	}
	
	public OwareGame(OwareGame game) {
		this.computerCells = game.computerCells.clone();
		this.humanCells = game.humanCells.clone();
		this.humanSeeds = game.humanSeeds;
		this.computerPlays = game.computerPlays;
		this.computerSeeds = game.computerSeeds;
		this.emptyComputerPits = game.emptyComputerPits;
		this.emptyPlayerPits = game.emptyPlayerPits;
	}
	private void initializeGame() {
		computerCells = new int[numberOfColumns];
		humanCells = new int[numberOfColumns];
		for(int i=0;i < numberOfColumns; i++) {
			computerCells[i] = pitMax;
			humanCells[i] = pitMax;
		}		
		computerSeeds = 0;
		humanSeeds = 0;
	}
	public int makeMove(int pit) {
		int inOtherTerritory = 0;
		int originalPit = pit;
		int tmp;

		if (computerPlays) {
			tmp = computerCells[pit];
			computerCells[pit] = 0;
		} else {
			tmp = humanCells[pit];
			humanCells[pit] = 0;
		}

		if (computerPlays) //computer
		{
			pit--;
			while (tmp > 0) {
				while (pit >= 0 && tmp > 0) {
					if (pit != originalPit) {
						computerCells[pit] += 1;
						tmp--;
					}
					inOtherTerritory = 0;
					pit--;
				}
				pit++;
				while (pit < 6 && tmp > 0) {
					humanCells[pit] += 1;
					pit++;
					tmp--;
					inOtherTerritory = 1;
				}
				pit--;
			}
		} else if (!computerPlays) //player
		{
			pit++;
			while (tmp > 0) {
				while (pit < 6 && tmp > 0) {
					inOtherTerritory = 0;
					if (originalPit != pit) {
						humanCells[pit] += 1;
						tmp--;
					}
					pit++;
				}
				pit--;
				while (pit >= 0 && tmp > 0) {
					inOtherTerritory = 1;
					computerCells[pit] += 1;
					pit--;
					tmp--;
				}
				pit++;
			}
		}
		OwareGame restore = new OwareGame(this);


		/*now to the real board*/
		if (!restore.leadingToStarvation(pit, inOtherTerritory)) {
			if (inOtherTerritory == 1 && (((computerPlays && humanCells[pit] == 2) || (!computerPlays && computerCells[pit] == 2)) || ((computerPlays && humanCells[pit] == 3) || (!computerPlays && computerCells[pit] == 3)))) {
				if (computerPlays) {
					while (pit >= 0 && (humanCells[pit] == 2 || humanCells[pit] == 3)) {
						computerSeeds += humanCells[pit];
						humanCells[pit] = 0;
						pit--;
					}
				} else if (!computerPlays) {
					while (pit < 6 && (computerCells[pit] == 2 || computerCells[pit] == 3)) {
						humanCells[pit] += computerCells[pit];
						computerCells[pit] = 0;
						pit++;
					}
				}
			}
		}

		/*updating empty cells*/
		emptyPlayerPits = (humanCells[0] == 0 ? 1 : 0) + (humanCells[1] == 0 ? 1 : 0)
				+ (humanCells[2] == 0 ? 1 : 0) + (humanCells[3] == 0 ? 1 : 0)
				+ (humanCells[4] == 0 ? 1 : 0) + (humanCells[5] == 0 ? 1 : 0);

		emptyComputerPits = (computerCells[0] == 0 ? 1 : 0) + (computerCells[1] == 0 ? 1 : 0)
				+ (computerCells[2] == 0 ? 1 : 0) + (computerCells[3] == 0 ? 1 : 0)
				+ (computerCells[4] == 0 ? 1 : 0) + (computerCells[5] == 0 ? 1 : 0);
		/*done updating empty cells*/

		computerPlays = !computerPlays;
		return 0;
	}
	private boolean leadingToStarvation(int pit, int inOtherTerritory) {
		/*testing if it makes all positions of opponent 0*/

		if (inOtherTerritory == 1 && (((computerPlays && humanCells[pit] == 2) || (!computerPlays && computerCells[pit] == 2)) || ((computerPlays && humanCells[pit] == 3) || (!computerPlays && computerCells[pit] == 3)))) {

			if (computerPlays) {
				while (pit >= 0 && (humanCells[pit] == 2 || humanCells[pit] == 3)) {
					computerSeeds += humanCells[pit];
					humanCells[pit] = 0;
					pit--;
				}
			} else if (!computerPlays) {
				while (pit < 6 && (computerCells[pit] == 2 || computerCells[pit] == 3)) {
					humanSeeds += computerCells[pit];
					computerCells[pit] = 0;
					pit++;
				}
			}
		}

		emptyPlayerPits = (humanCells[0] == 0 ? 1 : 0) + (humanCells[1] == 0 ? 1 : 0)
				+ (humanCells[2] == 0 ? 1 : 0) + (humanCells[3] == 0 ? 1 : 0)
				+ (humanCells[4] == 0 ? 1 : 0) + (humanCells[5] == 0 ? 1 : 0);

		emptyComputerPits = (computerCells[0] == 0 ? 1 : 0) + (computerCells[1] == 0 ? 1 : 0)
				+ (computerCells[2] == 0 ? 1 : 0) + (computerCells[3] == 0 ? 1 : 0)
				+ (computerCells[4] == 0 ? 1 : 0) + (computerCells[5] == 0 ? 1 : 0);
		return (computerPlays && emptyPlayerPits == 6) || (!computerPlays && emptyComputerPits == 6);
	}

	public MiniMax minimax(int depth, double alpha, double beta, int maxDepth) {

		int i, winEstimate = 999;
		double winCount = 0, validCount = 0;
		MiniMax res = null;
		boolean win = false;
		

		if (finalPosition()) {
			if (computerSeeds == 25 && humanSeeds == 25) {
				return new MiniMax(depth, 1, alpha, beta, false, 0.5);
			}

			if (computerSeeds > 24) {
				return new MiniMax(depth, 48, alpha, beta, true, 1.0);
			}

			if (humanSeeds > 24) {
				return new MiniMax(winEstimate, -48, alpha, beta, false, 0.0);
			}

			if (!computerPlays && ((computerCells[0] + computerCells[1] + computerCells[2] + computerCells[3] + computerCells[4] + computerCells[5]) == 0)) {
				return new MiniMax(winEstimate, -48, alpha, beta, false, 0.0);
			}

			if (computerPlays && (humanCells[0] + humanCells[1] + humanCells[2] + humanCells[3] + humanCells[4] + humanCells[5] == 0)) {
				return new MiniMax(depth, 48, alpha, beta, true, 1.0);
			}

		}

		if (depth == maxDepth) {
			return new MiniMax(depth, (double) (computerSeeds - humanSeeds), alpha, beta, false, 0.2);
		}
		int move = killerMove[depth];
		if (computerPlays) {
			for (i = 0; i < 6; i++) {
				OwareGame nextPos = new OwareGame(this);
				if (validMove(move)) {
					validCount++;
					nextPos.simulateMove(move);
					
					res = nextPos.minimax(depth + 1, alpha, beta, maxDepth);
					winEstimate = Math.min(res.closestWinDepth, winEstimate);
					alpha = Math.max(alpha, res.score);
					
					if (beta <= alpha) {
						killerMove[depth] = move;
						break; // beta cutoff
					}
				}
				move = (move + 1)%6;
			}
			
			return new MiniMax(winEstimate, alpha, alpha, beta, win, (double)winCount/validCount);
		}

		if (!computerPlays) {
			for (i = 0; i < 6; i++) {
				OwareGame nextPos = new OwareGame(this);
				if (validMove(move)) {
					validCount++;
					nextPos.simulateMove(move);					
					res = nextPos.minimax(depth + 1, alpha, beta, maxDepth);
					
					beta = Math.min(beta, res.score);
					if (beta <= alpha) {
						break;
					}
				} 
				move = (move + 1)%6;
			}
			
			return new MiniMax(winEstimate, beta, alpha, beta, win, (double)winCount/validCount);
		}

		return new MiniMax();
	}
	
	boolean finalPosition() {
		if ((computerSeeds == 24 && humanSeeds == 24)
				|| (computerSeeds > 24)
				|| (humanSeeds > 24)
				|| (!computerPlays && (computerCells[0]
						+ computerCells[1]
								+ computerCells[2]
										+ computerCells[3]
												+ computerCells[4]
														+ computerCells[5] == 0))
														|| (computerPlays && (humanCells[0]
																+ humanCells[1]
																		+ humanCells[2]
																				+ humanCells[3]
																						+ humanCells[4]
																								+ humanCells[5] == 0))) {
			return true;
		}
		return false;
	}

	public void displayGameBoard() {
		System.out.print("=================Computer==================\n");
		System.out.print("A\tB\tC\tD\tE\tF\n");
		for(int i=0;i < numberOfColumns;i++) {
			System.out.print(computerCells[i]);
			if((numberOfColumns-1) == i) {
				System.out.print("\n");
			}else {
				System.out.print("\t");
			}			
		}
		System.out.print("-------------------------------------------\n");
		for(int i=0;i < numberOfColumns;i++) {
			System.out.print(humanCells[i]);
			if((numberOfColumns-1) == i) {
				System.out.print("\n");
			}else {
				System.out.print("\t");
			}
		}
		System.out.print("a\tb\tc\td\te\tf\n");
		System.out.print("====================You====================\n");
		System.out.print("Computer's Score: "+computerSeeds+"\nYour Score: "+humanSeeds+"\n\n");		
	}
	
	boolean validMove(int i) {
		if (emptyPlayerPits == 6 || emptyComputerPits == 6) {
			if (computerPlays) {
				if (computerCells[i] > i) {
					return true;
				}
				return false;
			}

			if (computerCells[i] > 5 - i) //if !computerPlay
			{
				return true;
			}
			return false;

		}
		if (computerPlays && computerCells[i] > 0) {
			return true;
		}
		if ((!computerPlays) && humanCells[i] > 0) {
			return true;
		}
		return false;
	}
	
	void simulateMove(int pit) {
		int inOtherTerritory = 0;
		int originalPit = pit;
		int tmp;

		if (computerPlays) {
			tmp = computerCells[pit];
			computerCells[pit] = 0;
		} else {
			tmp = humanCells[pit];
			humanCells[pit] = 0;
		}

		if (computerPlays) //computer
		{
			pit--;
			while (tmp > 0) {
				while (pit >= 0 && tmp > 0) {
					if (pit != originalPit) {
						computerCells[pit] += 1;
						tmp--;
					}
					inOtherTerritory = 0;
					pit--;
				}
				pit++;
				while (pit < 6 && tmp > 0) {
					humanCells[pit] += 1;
					pit++;
					tmp--;
					inOtherTerritory = 1;
				}
				pit--;
			}
		} else if (!computerPlays) //player
		{
			pit++;
			while (tmp > 0) {
				while (pit < 6 && tmp > 0) {
					inOtherTerritory = 0;
					if (originalPit != pit) {
						humanCells[pit] += 1;
						tmp--;
					}
					pit++;
				}
				pit--;
				while (pit >= 0 && tmp > 0) {
					inOtherTerritory = 1;
					computerCells[pit] += 1;
					pit--;
					tmp--;
				}
				pit++;
			}
		}
		OwareGame restore = new OwareGame(this);


		/*now to the real board*/
		if (!restore.leadingToStarvation(pit, inOtherTerritory)) {
			if (inOtherTerritory == 1 && (((computerPlays && humanCells[pit] == 2) || (!computerPlays && computerCells[pit] == 2)) || ((computerPlays && humanCells[pit] == 3) || (!computerPlays && computerCells[pit] == 3)))) {
				if (computerPlays) {
					while (pit >= 0 && (humanCells[pit] == 2 || humanCells[pit] == 3)) {
						computerSeeds += humanCells[pit];
						humanCells[pit] = 0;
						pit--;
					}
				} else if (!computerPlays) {
					while (pit < 6 && (computerCells[pit] == 2 || computerCells[pit] == 3)) {
						humanSeeds += computerCells[pit];
						computerCells[pit] = 0;
						pit++;
					}
				}
			}
		}

		/*updating empty cells*/
		emptyPlayerPits = (humanCells[0] == 0 ? 1 : 0) + (humanCells[1] == 0 ? 1 : 0)
				+ (humanCells[2] == 0 ? 1 : 0) + (humanCells[3] == 0 ? 1 : 0)
				+ (humanCells[4] == 0 ? 1 : 0) + (humanCells[5] == 0 ? 1 : 0);

		emptyComputerPits = (computerCells[0] == 0 ? 1 : 0) + (computerCells[1] == 0 ? 1 : 0)
				+ (computerCells[2] == 0 ? 1 : 0) + (computerCells[3] == 0 ? 1 : 0)
				+ (computerCells[4] == 0 ? 1 : 0) + (computerCells[5] == 0 ? 1 : 0);
		/*done updating empty cells*/

		computerPlays = !computerPlays;
	}
	
	public boolean isComputerPlays() {
		return computerPlays;
	}

	public void setComputerPlays(boolean computerPlays) {
		this.computerPlays = computerPlays;
	}
}
