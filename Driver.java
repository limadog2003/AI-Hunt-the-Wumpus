package edu.bu.met.cs664.Wumpus;

import java.io.IOException;
import java.util.Scanner;

public class Driver 	
	{

	public static void main(String[] args) throws IOException 
		{
		Scanner inp = new Scanner(System.in);
		
		Board board = new Board("/Users/Chris/Downloads/input_file.txt");
		
		board.printBoard();
		System.out.println();
		
		KnowledgeBase kb = new KnowledgeBase("/Users/Chris/Downloads/input_file.txt");
		
		kb.printKB();
		
		Agent agent = new Agent(kb.getStarting_row(), kb.getStarting_column());
		
		while (agent.alive)
			{
			System.out.println("Press enter to continue");
			inp.nextLine();
			
			
			if (kb.getKB(agent.locationR, agent.locationC).isGold())
				{
				agent.getGold(kb, board);	
				}
			
			agent.agentMove(kb, board);
			kb.printKB();
			}
		
		//System.out.println(agent.getScore());
		
		inp.close();
		}//end of main


	}//end of class
