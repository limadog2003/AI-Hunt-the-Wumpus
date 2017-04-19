package edu.bu.met.cs664.Wumpus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;


public class KnowledgeBase 
	{
	private int starting_row, starting_column;
	private Square kb [][];
	
	
	public KnowledgeBase(String parameters) throws IOException
		{
		File file = new File(parameters);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String line = null;
		Queue<String> queue = new LinkedList<String>();
		
	    while ((line = reader.readLine()) != null)
	    	{
	    	queue.add(line.toLowerCase().trim().replace(",", ""));
	    	}
	    	
	    int row = Integer.parseInt(queue.peek().substring(5, 6));
		int column = Integer.parseInt(queue.poll().substring(6, 7));
		
		this.kb = new Square [row][column];
		
		for (int j = 0; j < kb[0].length; j++)
			{
			for (int k = 0; k < kb.length; k++)
	    		{
				if (kb[j][k] == null)
					{
					kb[j][k] = new Square("board", j, k, "");
					}
	    		}
			}
		
		while (!queue.isEmpty())
			{
			if (queue.peek().contains("e"))
				{
				starting_row = Integer.parseInt(queue.peek().substring(0, 1));
				starting_column = Integer.parseInt(queue.peek().substring(1, 2));
				kb[starting_row][starting_column].setAtt(queue.peek().substring(2));	
				kb[starting_row][starting_column].setAgent(true);
				kb[starting_row][starting_column].setVisited(true);
				}
			queue.poll();
			}
		
		reader.close();
		}
	
	
	/**
	 * @return the starting_row
	 */
	public int getStarting_row() 
		{
		return starting_row;
		}


	/**
	 * @return the starting_column
	 */
	public int getStarting_column() 
		{
		return starting_column;
		}


	/**
	 * 
	 */
	public Square getKB(int r, int c)
		{
		return kb[r][c];
		}
	
	
	/**
	 * 
	 */
	public void setKB(int r, int c, Board board )
		{
		kb[r][c].setAtt(board.getAtt(r, c));
		  }
	
	
	/**
	 * 
	 */
	public void printKB()
		{
		for (int k = 0; k < kb.length; k++)
			{
			System.out.print("    " + k + "\t");
			}
	
		for (int i = 0; i < kb.length; i++)
			{
				
			System.out.println();
			System.out.print(i + " |");
			for (int j = 0; j < kb[0].length; j++)
				{
				System.out.print(kb[i][j].getAtt() + "\t|");
				}
			System.out.println();
			System.out.println("-------------------------------------------");
			}
		}
	
	
	/**
	 * 
	 * @return
	 */
	public boolean [][] getVisitedSquares()
		{
		boolean [][] visitedList = new boolean[kb.length][kb[0].length];
		
		
		for (int i = 0; i < kb.length; i++)
			{
			for (int j = 0; j < kb[0].length; j++)
				{
				if (kb[i][j].haveVisited())
					{
					visitedList[i][j] = true; 
					} 
				}
			}
		return visitedList;
		}
	
	
	/**
	 * 
	 * @param current_r
	 * @param current_c
	 * @param new_r
	 * @param new_c
	 * @return
	 */
	public boolean isSafe(int current_r, int current_c, int new_r, int new_c)
		{
		try
			{
			kb[new_r][new_c].getAtt();
			}
		catch (Exception e)
			{
			System.out.println("Out of bounds");
			return false;
			}
		
		//try to identify where the pits and wumpus are so that we can create more paths
		pitChecker(current_r, current_c);
		wumpusChecker(current_r, current_c);
		
		
		//Now do the standard evaluations on the move options
		
		//If we've already visited the site, then its safe...
		if (kb[new_r][new_c].getAtt().contains("V"))
			{
			System.out.println("Welcome back!");
			return true;
			}
		
		//Can't go into a space that has a wumpus
		if (kb[new_r][new_c].getAtt().contains("W"))
			{
			System.out.println("There's a Wumpus here");
			return false;
			}
		
		//Can't go into a space that has a pit
		if (kb[new_r][new_c].getAtt().contains("P"))
			{
			System.out.println("There's a Pit here");
			return false;
			}
		
		//if no bad things in the current square
		if (kb[current_r][current_c].getAtt().equals("") || kb[current_r][current_c].getAtt().equals("VA") || kb[current_r][current_c].getAtt().contains("E"))
			{
			System.out.println("All moves are safe");
			return true;
			}
		
		//Stench in our current spot
		if (kb[current_r][current_c].getAtt().contains("S"))
			{
			System.out.println("Something stinks here");
			
			//This will make sure that if we've put the wumpus in place that any further S's received won't hamper our progress
			if (kb[current_r][current_c].noWumpus()== true)
				{
				System.out.println("We know where the wumpus is");
				return true;
				}
			
			if (kb[new_r][new_c].getAtt().contains("V"))
				{
				return true;
				}		
			
			System.out.println("This is unsafe--wumpus territory");
			return false;
			}
		
		
		//Breeze in our current spot
		if (kb[current_r][current_c].getAtt().contains("B"))
			{
			System.out.println("There is a draft where I'm standing");	
			
			//This will make sure that if we've identified a pit, then getting breezes from it won't hamper our movements
			if (kb[current_r][current_c].noPit() == true)
				{
				System.out.println("We know where the pit is.");
				return true;
				}
			
			//if we've already been there
			if (kb[new_r][new_c].getAtt().contains("V"))
				{
				return true;
				}
			
			System.out.println("This could be a pit");
			return false;
			}
		
		System.out.println("This way is ok");
		return true;
		}
	
	
	/**
	 * 
	 * @param current_r
	 * @param current_c
	 */
	public void pitChecker(int current_r, int current_c)
		{
		//This is a logic piece to evaluate for any pits or a wumpus based on the breezes
		//This will remove the breezes from the square and replace with a P where the actual pits are 
		//so that we can pass through knowing it is safe.
		boolean north, south, east, west, southeast, southwest, northeast, northwest;
		
				
		//check pit north 
		if ((current_r-1) >=1 && ((current_r-1) < kb.length) && kb[current_r-1][current_c].isBreeze())
			{
			north = true;	
			}
		else 
			{
			north = false;
			}
				
		//check breeze south
		if ((current_r+1) >=1 && ((current_r+1) < kb.length) && kb[current_r+1][current_c].isBreeze())
			{
			south = true;
			}
		else 
			{
			south = false;
			}
				
		//check breeze east
		if ((current_c+1) >=1 && ((current_c+1) < kb[0].length) && kb[current_r][current_c+1].isBreeze())
			{
			east = true;
			}
		else 
			{
			east = false;
			}
				
		//check breeze west
		if ((current_c-1) >=1 && ((current_c-1) < kb[0].length) && kb[current_r][current_c-1].isBreeze())
			{
			west = true;	
			}
		else
			{
			west = false;
			}
				
		//check breeze northeast
		if ((current_c+1) >=1 && ((current_c+1) < kb[0].length) && (current_r-1) >=1 && ((current_r-1) < kb.length) && kb[current_r-1][current_c+1].getAtt().contains("B"))
			{
			northeast = true;	
			}
		else
			{
			northeast = false;
			}
				
		//check breeze northwest
		if ((current_c-1) >=1 && ((current_c-1) < kb[0].length) && (current_r-1) >=1 && ((current_r-1) < kb.length) && kb[current_r-1][current_c-1].getAtt().contains("B"))
			{
			northwest = true;	
			}
		else
			{
			northwest = false;
			}
				
		//check breeze southeast
		if ((current_c+1) >=1 && ((current_c+1) < kb[0].length) && (current_r+1) >=1 && ((current_r+1) < kb.length) && kb[current_r+1][current_c+1].getAtt().contains("B"))
			{
			southeast = true;	
			}
		else
			{
			southeast = false;
			}
				
		//check breeze southwest 
		if ((current_c-1) >=1 && ((current_c-1) < kb[0].length) && (current_r+1) >=1 && ((current_r+1) < kb.length) && kb[current_r+1][current_c-1].getAtt().contains("B"))
			{
			southwest = true;	
			}
		else
			{
			southwest = false;
			}
				
				
		//pit logic
		//breeze north and east = pit northeast if not visited
		if (north == true && east == true && !(kb[current_r-1][current_c+1].haveVisited() == true))
			{
			System.out.println("There's a pit to the northeast");
			//put a P in the north east corner, and remove the B's from north and east
			kb[current_r-1][current_c+1].setPit(true);
			
			kb[current_r-1][current_c].setBreeze(false);
			kb[current_r-1][current_c].setNoPit(true);
			
			kb[current_r][current_c+1].setBreeze(false);
			kb[current_r][current_c+1].setNoPit(true); 
			}
		
		//breeze north and west = pit northwest if not visited
		if (north == true && west == true && !(kb[current_r-1][current_c-1].haveVisited() == true))
			{
			System.out.println("There's a pit to the northwest");
			//put a P in the north west corner, and remove the B's from north and west
			kb[current_r-1][current_c-1].setPit(true);
			
			kb[current_r-1][current_c].setBreeze(false);
			kb[current_r-1][current_c].setNoPit(true);
			
			kb[current_r][current_c-1].setBreeze(false);
			kb[current_r][current_c-1].setNoPit(true); 
			}
		
		//breeze south and east = pit southeast if not visited
		if (south == true && east == true && !(kb[current_r+1][current_c+1].haveVisited() == true))
			{
			System.out.println("There's a pit to the southeast");
			//put a P in the south east corner, and remove the B's from the south and east
			kb[current_r+1][current_c+1].setPit(true);
			
			kb[current_r+1][current_c].setBreeze(false);
			kb[current_r+1][current_c].setNoPit(true);
			
			kb[current_r][current_c+1].setBreeze(false);
			kb[current_r][current_c+1].setNoPit(true);
			}
		
		//breeze south + breeze west = pit southwest if not visited
		if (south == true && west == true && !(kb[current_r+1][current_c-1].haveVisited() == true))
			{
			System.out.println("There's a pit to the southwest");
			//put a P in the south west corner, and remove the B's from the south and west
			kb[current_r+1][current_c-1].setPit(true);
			
			kb[current_r+1][current_c].setBreeze(false);
			kb[current_r+1][current_c].setNoPit(true);
			
			kb[current_r][current_c-1].setBreeze(false);
			kb[current_r][current_c-1].setNoPit(true);
			}
		
		//checking the current spot + the corners
		//*****this may have a logic problem if there are potentially multiple pits and breezes in the same area....****
		
		//breeze in current spot + breeze in northeast + not visited east + visited north= pit east
		try
			{
			if (kb[current_r][current_c].isBreeze() && northeast == true && !(kb[current_r][current_c+1].haveVisited() == true) && kb[current_r-1][current_c].haveVisited() == true)
				{
				kb[current_r][current_c+1].setPit(true);
				
				kb[current_r][current_c].setBreeze(false);
				kb[current_r][current_c].setNoPit(true);
				
				kb[current_r-1][current_c+1].setBreeze(false);	
				kb[current_r-1][current_c+1].setNoPit(true); 
				}
			}
		catch (Exception e)
			{
			System.out.println("Out of bounds");
			}
		
		//breeze in current spot + breeze in northeast + not visited north + visited east= pit north
		try
			{
			if (kb[current_r][current_c].isBreeze() && northeast == true && !(kb[current_r-1][current_c].haveVisited() == true) && kb[current_r][current_c+1].haveVisited() == true)
				{
				kb[current_r-1][current_c].setPit(true);
						
				kb[current_r][current_c].setBreeze(false);
				kb[current_r][current_c].setNoPit(true);
						 
				kb[current_r-1][current_c+1].setBreeze(false);	
				kb[current_r-1][current_c+1].setNoPit(true); 
				}
			}
		catch (Exception e)
			{
			System.out.println("Out of bounds");
			}
		
		//breeze in current spot + breeze in northwest + not visited west + visited north = pit west
		try
			{
			if (kb[current_r][current_c].isBreeze() && northwest == true && !(kb[current_r][current_c-1].haveVisited() == true) && kb[current_r-1][current_c].haveVisited() == true)
				{
				kb[current_r][current_c-1].setPit(true);
				
				kb[current_r][current_c].setBreeze(false);
				kb[current_r][current_c].setNoPit(true);
				
				kb[current_r-1][current_c-1].setBreeze(false);	
				kb[current_r-1][current_c-1].setNoPit(true);
				}
			}
		catch (Exception e)
			{
			System.out.println("Out of bounds");
			}
		
		//breeze in current spot + breeze in northwest + not visited north + visited west = pit north
		try
			{
			if (kb[current_r][current_c].isBreeze() && northwest == true && !(kb[current_r-1][current_c].haveVisited() == true) && kb[current_r][current_c-1].haveVisited() == true)
				{
				kb[current_r-1][current_c].setPit(true);
				
				kb[current_r][current_c].setBreeze(false);
				kb[current_r][current_c].setNoPit(true); 
				
				kb[current_r-1][current_c-1].setBreeze(false);	
				kb[current_r-1][current_c-1].setNoPit(true);
				}
			}
		catch (Exception e)
			{
			System.out.println("Out of bounds");
			}
		
		//breeze in current spot + breeze in southeast + not visited east + visited south = pit east
		try
			{
			if (kb[current_r][current_c].isBreeze() && southeast == true && !(kb[current_r][current_c+1].haveVisited() == true) && kb[current_r+1][current_c].haveVisited() == true)
				{
				kb[current_r][current_c+1].setPit(true);
				
				kb[current_r][current_c].setBreeze(false);
				kb[current_r][current_c].setNoPit(true);
				
				kb[current_r+1][current_c+1].setBreeze(false); 
				kb[current_r+1][current_c+1].setNoPit(true);
				}
			}
		catch (Exception e)
			{
			System.out.println("Out of bounds");
			}
		
		//breeze in current spot + breeze in southeast + not visited south + visited east = pit south
		try
			{
			if (kb[current_r][current_c].isBreeze() && southeast == true && !(kb[current_r+1][current_c].haveVisited() == true) && kb[current_r][current_c+1].haveVisited() == true)
				{
				kb[current_r+1][current_c].setPit(true);
						
				kb[current_r][current_c].setBreeze(false);
				kb[current_r][current_c].setNoPit(true);
						
				kb[current_r+1][current_c+1].setBreeze(false); 
				kb[current_r+1][current_c+1].setNoPit(true); 
				}
			}
		catch (Exception e)
			{
			System.out.println("Out of bounds");
			}
		
		//breeze in current spot + breeze in southwest + not visited west + visited south = pit west
		try
			{
			if (kb[current_r][current_c].isBreeze() && southwest == true && !(kb[current_r][current_c-1].haveVisited() == true) && kb[current_r+1][current_c].haveVisited() == true)
				{ 
				kb[current_r][current_c-1].setPit(true);
				
				kb[current_r][current_c].setBreeze(false);
				kb[current_r][current_c].setNoPit(true);
				
				kb[current_r+1][current_c-1].setBreeze(false);	
				kb[current_r+1][current_c-1].setNoPit(true);  
				}	
			}
		catch (Exception e)
			{
			System.out.println("Out of bounds");
			}
		
		//breeze in current spot + breeze in southwest + not visited south + visited west = pit south
		try
			{
			if (kb[current_r][current_c].isBreeze() && southwest == true && !(kb[current_r+1][current_c].haveVisited() == true) && kb[current_r][current_c-1].haveVisited() == true)
				{ 
				kb[current_r+1][current_c].setPit(true);
						
				kb[current_r][current_c].setBreeze(false);
				kb[current_r][current_c].setNoPit(true);
						
				kb[current_r+1][current_c-1].setBreeze(false);	
				kb[current_r+1][current_c-1].setNoPit(true);  
				}
			}
		catch (Exception e)
			{
			System.out.println("Out of bounds");
			}
		}
	
	
	/**
	 * 
	 * @param current_r
	 * @param current_c
	 */
	public void wumpusChecker(int current_r, int current_c)
		{
		boolean wnorth, wsouth, weast, wwest, wsoutheast, wsouthwest, wnortheast, wnorthwest;
		
		//check stench north
		if ((current_r-1) >=1 && ((current_r-1) < kb.length) && kb[current_r-1][current_c].getAtt().contains("S"))
			{
			wnorth = true;	
			}
		else 
			{	
			wnorth = false;
			}
				
		//check stench south
		if ((current_r+1) >=1 && ((current_r+1) < kb.length) && kb[current_r+1][current_c].getAtt().contains("S"))
			{
			wsouth = true;
			}
		else 
			{
			wsouth = false;
			}
				
		//check stench east
		if ((current_c+1) >=1 && ((current_c+1) < kb[0].length) && kb[current_r][current_c+1].getAtt().contains("S"))
			{
			weast = true;
			}
		else 
			{
			weast = false;
			}
				
		//check stench west
		if ((current_c-1) >=1 && ((current_c-1) < kb[0].length) && kb[current_r][current_c-1].getAtt().contains("S"))
			{
			wwest = true;	
			}
		else
			{
			wwest = false;
			}
		
		//check stench northeast
		if ((current_c+1) >=1 && ((current_c+1) < kb[0].length) && (current_r-1) >=1 && ((current_r-1) < kb.length) && kb[current_r-1][current_c+1].getAtt().contains("S"))
			{
			wnortheast = true;	
			}
		else
			{
			wnortheast = false;
			}
				
		//check stench northwest
		if ((current_c-1) >=1 && ((current_c-1) < kb[0].length) && (current_r-1) >=1 && ((current_r-1) < kb.length) && kb[current_r-1][current_c-1].getAtt().contains("S"))
			{
			wnorthwest = true;	
			}
		else
			{
			wnorthwest = false;
			}
				
		//check stench southeast
		if ((current_c+1) >=1 && ((current_c+1) < kb[0].length) && (current_r+1) >=1 && ((current_r+1) < kb.length) && kb[current_r+1][current_c+1].getAtt().contains("S"))
			{
			wsoutheast = true;	
			}
		else
			{
			wsoutheast = false;
			}
				
		//check stench southwest 
		if ((current_c-1) >=1 && ((current_c-1) < kb[0].length) && (current_r+1) >=1 && ((current_r+1) < kb.length) && kb[current_r+1][current_c-1].getAtt().contains("S"))
			{
			wsouthwest = true;	
			}
		else
			{
			wsouthwest = false;
			}	
		
		
		//wumpus logic
		if (wnorth == true && weast == true)
			{
			//put a W in the north east corner, and remove the S's from north and east
			kb[current_r-1][current_c+1].setWumpus(true);
			
			kb[current_r-1][current_c].setStench(false);
			kb[current_r-1][current_c].setNoWumpus(true);
			
			kb[current_r][current_c+1].setStench(false);
			kb[current_r][current_c+1].setNoWumpus(true);
			}
			
		if (wnorth == true && wwest == true)
			{
			//put a W in the north west corner, and remove the S's from north and west
			kb[current_r-1][current_c-1].setWumpus(true);
			
			kb[current_r-1][current_c].setStench(false);
			kb[current_r-1][current_c].setNoWumpus(true);
			
			kb[current_r][current_c-1].setStench(false);
			kb[current_r][current_c-1].setNoWumpus(true);
			}
				
		if (wsouth == true && weast == true)
			{
			//put a W in the south east corner, and remove the S's from the south and east
			kb[current_r+1][current_c+1].setWumpus(true);
			
			kb[current_r+1][current_c].setStench(false);
			kb[current_r+1][current_c].setNoWumpus(true);
			
			kb[current_r][current_c+1].setStench(false);
			kb[current_r][current_c+1].setNoWumpus(true);
			}
				
		if (wsouth == true && wwest == true)
			{
			//put a W in the south west corner, and remove the S's from the south and west
			kb[current_r+1][current_c-1].setWumpus(true);
			
			kb[current_r+1][current_c].setStench(false);
			kb[current_r+1][current_c].setNoWumpus(true);
			
			kb[current_r][current_c-1].setStench(false);
			kb[current_r][current_c-1].setNoWumpus(true);
			}
		
		//checking the current spot + the corners
		//stench in current spot + stench in northeast + not visited east + visited south = wumpus east
		try
			{
			if (kb[current_r][current_c].isStench() && wnortheast == true && !(kb[current_r][current_c+1].haveVisited() == true) && kb[current_r+1][current_c].haveVisited() == true)
				{
				kb[current_r][current_c+1].setWumpus(true);
				
				kb[current_r][current_c].setStench(false);
				kb[current_r][current_c].setNoWumpus(true);
				
				kb[current_r-1][current_c+1].setStench(false);	
				kb[current_r-1][current_c+1].setNoWumpus(true);
				}
			}
		catch (Exception e)
			{
			System.out.println("Out of bounds");
			}
		
		//stench in current spot + stench in northeast + not visited south + visited east = wumpus south
		try
			{
			if (kb[current_r][current_c].isStench() && wnortheast == true && !(kb[current_r+1][current_c].haveVisited() == true) && kb[current_r][current_c+1].haveVisited() == true)
				{
				kb[current_r+1][current_c].setWumpus(true);
						
				kb[current_r][current_c].setStench(false);
				kb[current_r][current_c].setNoWumpus(true);
						
				kb[current_r-1][current_c+1].setStench(false);	
				kb[current_r-1][current_c+1].setNoWumpus(true);
				}
			}
		catch (Exception e)
			{
			System.out.println("Out of bounds");
			}
		
		//stench in current spot + stench in northwest + not visited west + visited north = wumpus west
		try
			{
			if (kb[current_r][current_c].isStench() && wnorthwest == true && !(kb[current_r][current_c-1].haveVisited() == true) && kb[current_r-1][current_c].haveVisited() == true)
				{
				kb[current_r][current_c-1].setWumpus(true);
				
				kb[current_r][current_c].setStench(false); 
				kb[current_r][current_c].setNoWumpus(true);
				
				kb[current_r-1][current_c-1].setStench(false);
				kb[current_r-1][current_c-1].setNoWumpus(true);
				}
			}
		catch (Exception e)
			{
			System.out.println("Out of bounds");
			}
		
		//stench in current spot + stench in northwest + not visited north + visited west = wumpus north
		try
			{
			if (kb[current_r][current_c].isStench() && wnorthwest == true && !(kb[current_r-1][current_c].haveVisited() == true) && kb[current_r][current_c-1].haveVisited() == true)
				{
				kb[current_r-1][current_c].setWumpus(true);
						
				kb[current_r][current_c].setStench(false); 
				kb[current_r][current_c].setNoWumpus(true);
						
				kb[current_r-1][current_c-1].setStench(false);
				kb[current_r-1][current_c-1].setNoWumpus(true);
				}
			}
		catch (Exception e)
			{
			System.out.println("Out of bounds");
			}
		
		//stench in current spot + stench in southeast + not visisted east + visited south = wumpus east
		try
			{
			if (kb[current_r][current_c].isStench() && wsoutheast == true && !(kb[current_r][current_c+1].haveVisited() == true) && kb[current_r+1][current_c].haveVisited() == true)
				{
				kb[current_r][current_c+1].setWumpus(true);
				
				kb[current_r][current_c].setStench(false);
				kb[current_r][current_c].setNoWumpus(true);
				
				kb[current_r+1][current_c+1].setStench(false);
				kb[current_r+1][current_c+1].setNoWumpus(true);
				}
			}
		catch (Exception e)
			{
			System.out.println("Out of bounds");
			}
		
		//stench in current spot + stench in southeast + not visisted south + visited east = wumpus south
		try
			{
			if (kb[current_r][current_c].isStench() && wsoutheast == true && !(kb[current_r+1][current_c].haveVisited() == true) && kb[current_r][current_c+1].haveVisited() == true)
				{
				kb[current_r+1][current_c].setWumpus(true);
						
				kb[current_r][current_c].setStench(false);
				kb[current_r][current_c].setNoWumpus(true);
						
				kb[current_r+1][current_c+1].setStench(false);
				kb[current_r+1][current_c+1].setNoWumpus(true);
				}
			}
		catch (Exception e)
			{
			System.out.println("Out of bounds");
			}
			
		//stench in current spot + stench in southwest + not visited west + visited south = wumpus west
		try
			{
			if (kb[current_r][current_c].isBreeze() && wsouthwest == true && !(kb[current_r][current_c-1].haveVisited() == true) && kb[current_r+1][current_c].haveVisited() == true)
				{
				kb[current_r][current_c-1].setWumpus(true);
				
				kb[current_r][current_c].setStench(false);
				kb[current_r][current_c].setNoWumpus(true);
				
				kb[current_r+1][current_c-1].setStench(false);
				kb[current_r+1][current_c-1].setNoWumpus(true);
				}	
			}
		catch (Exception e)
			{
			System.out.println("Out of bounds");
			}
		
		//stench in current spot + stench in southwest + not visited south + visited west = wumpus south
		try
			{
			if (kb[current_r][current_c].isBreeze() && wsouthwest == true && !(kb[current_r+1][current_c].haveVisited() == true) && kb[current_r][current_c-1].haveVisited() == true)
				{
				kb[current_r+1][current_c].setWumpus(true);
						
				kb[current_r][current_c].setStench(false);
				kb[current_r][current_c].setNoWumpus(true);
						
				kb[current_r+1][current_c-1].setStench(false);
				kb[current_r+1][current_c-1].setNoWumpus(true);
				}
			}
		catch (Exception e)
			{
			System.out.println("Out of bounds");
			}
		}
	
	}//end of class
