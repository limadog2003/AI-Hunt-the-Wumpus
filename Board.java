package edu.bu.met.cs664.Wumpus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class Board 
	{
	private int row, column;
	private Square [][] gameboard;
	
	
	public Board (String parameters) throws IOException
		{
		
		File file = new File(parameters);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String line = null;
		Queue<String> queue = new LinkedList<String>();
		
	    while ((line = reader.readLine()) != null)
	    	{
	    	queue.add(line.toLowerCase().trim().replace(",", ""));
	    	}
	    	
	    	
	   
	    this.row = Integer.parseInt(queue.peek().substring(5, 6));
    	//System.out.println("Rows are " + row);
    	this.column = Integer.parseInt(queue.poll().substring(6, 7));
    	//System.out.println("colums are " + column);
    	
    	this.gameboard = new Square [row][column];
    		
    	
	    while (!queue.isEmpty())
		  	{
	    	gameboard[Integer.parseInt(queue.peek().substring(0, 1))][Integer.parseInt(queue.peek().substring(1, 2))] = new Square("", Integer.parseInt(queue.peek().substring(0, 1)), Integer.parseInt(queue.peek().substring(1, 2)), queue.poll().substring(2));
		    }   	

		for (int j = 0; j < gameboard[0].length; j++)
			{ 
			for (int k = 0; k < gameboard.length; k++)
	    		{
				if (gameboard[j][k] == null)
					{
					gameboard[j][k] = new Square("board", j, k, "");
					}
	    		}
			}
	    			
	    reader.close();
		}
	
	
	/**
	 * 
	 */
	public void printBoard() 
		{
		for (int k = 0; k < this.gameboard.length; k++)
			{
			System.out.print("    " + k + "\t");
			}
		
		for (int i = 0; i < this.gameboard.length; i++)
			{
				
			System.out.println();
			System.out.print(i + " |");
			for (int j = 0; j < this.gameboard[0].length; j++)
				{
				System.out.print(this.gameboard[i][j].getAtt() + "\t|");
				}
			System.out.println();
			System.out.println("-------------------------------------------");
			}
		}
	
	
	/**
	 * 
	 */
	public boolean isWumpusDead(int row, int column)
		{
		if (gameboard[row][column].isWumpus() == true)
			{
			gameboard[row][column].setWumpus(false);
			return true;
			}
		return false;
		}
	
	
	/**
	 * 
	 * @return
	 */
	public boolean IsAlive(int r, int c)
		{
		if (gameboard[r][c].getAtt().contains("W") || gameboard[r][c].getAtt().contains("P"))
			{
			return false;
			}
		return true;
		} 
	
	
	/**
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public String getAtt(int row, int column)
		{
		return gameboard[row][column].getAtt();
		}
	
	
	/**
	 * 
	 * @param r
	 * @param c
	 */
	public boolean gotGold(int r, int c)
		{
		if (gameboard[r][c].isGold() == true)
			{
			gameboard[r][c].setGold(false);
			System.out.println("Got the gold!");
			return true;
			}
		return false;
		}
	}//end of class
