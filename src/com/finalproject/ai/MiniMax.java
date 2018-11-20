package com.finalproject.ai;

public class MiniMax {
	int closestWinDepth;
	double score;
	double alpha, beta;
	boolean winning;
	double winningProbability;


	public MiniMax() {
		closestWinDepth = 0;
		score = 0;
		alpha = -1000;
		beta = 1000;
		winning = false;
		winningProbability = 0.0;
	}

	public MiniMax(int depth, double score, double alpha, double beta, boolean winning, double winningProb) {
		this.closestWinDepth = depth;
		this.score = score;
		this.alpha = alpha;
		this.beta = beta;
		this.winning = winning;
		this.winningProbability = winningProb;
	}

	public MiniMax(MiniMax x) {
		closestWinDepth = x.closestWinDepth;
		score = x.score;
		alpha = x.alpha;
		beta = x.beta;
		winning = x.winning;
		winningProbability = x.winningProbability;
		
	}
}
