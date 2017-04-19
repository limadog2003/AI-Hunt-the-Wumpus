package edu.bu.met.cs664.Wumpus;

public class Agent 
	{
	boolean hasGold, hasArrow, alive, win;
	int locationR, locationC;
	int score;
	
	public Agent(int starting_r, int starting_c)
		{
		this.hasArrow = true;
		this.hasGold = false;
		this.alive = true;
		this.locationC = starting_c;
		this.locationR = starting_r;
		this.score = 0;
		this.win = false;
		}
	 
	
	/**
	 * 
	 * @param status
	 */
	public void setAlive(boolean status)
		{
		this.alive = status;
		}
	
	
	/**
	 * 
	 * @param bool
	 */
	public void setHasArrow(boolean bool)
		{
		this.hasArrow = bool;
		}
	
	
	/**
	 * 
	 */
	public void setLocation(int r, int c, Board board, KnowledgeBase knowl)
		{
		if (board.IsAlive(r, c))
			{
			knowl.getKB(locationR, locationC).setAgent(false);
			locationR = r;
			locationC = c;
			knowl.getKB(locationR, locationC).setAgent(true);
			knowl.getKB(r, c).setVisited(true);
			
			knowl.setKB(locationR, locationC, board);
			setScore(-1);
			}
		
		else 
			{
			System.out.println("A horrible death is your destiny");
			setAlive(false);
			System.out.println(score);
			}
		}
 
	
	/**
	 * 
	 * @param type
	 */
	private void setScore(int type)
		{
		if (type == -1)
			{
			score = score - 1;
			}
		if (type == -10)
			{
			score = score - 10;
			}
		
		if (type == 1000)
			{
			score = score + 1000;
			}
		}

	
	/**
	 * 
	 * @return
	 */
	public int getScore()
		{
		return score;
		}
	
	 
	/**
	 * 
	 * @param kb
	 * @param board
	 * @return
	 */
	public boolean agentMove(KnowledgeBase kb, Board board)
		{
		System.out.println("Looking west");
		boolean west = kb.isSafe(locationR, locationC, locationR, (locationC-1));
		
		System.out.println("Looking east");
		boolean east = kb.isSafe(locationR, locationC, locationR, (locationC+1));
		
		System.out.println("Looking north");
		boolean north = kb.isSafe(locationR, locationC, (locationR-1), locationC);
		
		System.out.println("Looking south");
		boolean south = kb.isSafe(locationR, locationC, (locationR+1), (locationC));
		
		if (east == true)
			{
			if (!(kb.getKB(locationR, (locationC+1)).getAtt().contains("V")))	
				{
				System.out.println("Agent is moving East " + (locationR) + " " + (locationC+1));
				setLocation(locationR, (locationC+1), board, kb);
				return true;
				}
			
			System.out.println("We've been east already, looking at the other options...");
			}
		
		if (south == true)
			{
			if (!(kb.getKB((locationR+1), (locationC)).getAtt().contains("V")))	
				{
				System.out.println("Agent is moving South " + (locationR+1) + " " + (locationC));
				setLocation((locationR+1), (locationC), board, kb);
				return true;
				}
			System.out.println("We've been South already, looking at the other options...");
			}
		
		if (west == true)
			{
			if (!(kb.getKB(locationR, (locationC-1)).getAtt().contains("V")))	
				{
				System.out.println("Agent is moving West " + (locationR) + " " + (locationC-1));
				setLocation((locationR), (locationC-1), board, kb);
				return true;
				}
			System.out.println("We've been West already, looking at the other options...");
			}
		
		if (north == true) 
			{
			if (!(kb.getKB(locationR-1, (locationC)).getAtt().contains("V")))	
				{
				System.out.println("Agent is moving North" + (locationR-1) + " " + (locationC));
				setLocation((locationR-1), (locationC), board, kb);
				return true;
				}
			System.out.println("We've been North already, looking at the other options...");
			}
		
		
		//getting to this point means there are no safe moves that we haven't visited yet
		if (east == true)
			{
			System.out.println("No unvisited safe moves.  Moving east: " + (locationR) + " " + (locationC+1));
			setLocation((locationR), (locationC+1), board, kb);
			return true;
			}
		
		if (south == true)
			{
			System.out.println("No unvisited safe moves.  Moving South: "+ (locationR+1) + " " + (locationC));
			setLocation((locationR+1), (locationC), board, kb);
			return true;
			}
		
		if (west == true)
			{
			System.out.println("No unvisited safe moves.  Moving West: "+ (locationR) + " " + (locationC-1));
			setLocation((locationR), (locationC-1), board, kb);
			return true;
			}
		
		if (north == true)
			{
			System.out.println("No unvisited safe moves.  Moving North: "+ (locationR-1) + " " + (locationC));
			setLocation((locationR-1), (locationC), board, kb);
			return true;
			}
		
		
		//Need to shoot the wumpus to move on
		if (kb.getKB(locationR, (locationC+1)).getAtt().contains("W"))	//east
			{
			shootWumpus(locationR, locationC+1, board);
			}
		
		if (kb.getKB(locationR, (locationC-1)).getAtt().contains("W"))	//west
			{
			shootWumpus(locationR, locationC-1, board);
			}
		
		if (kb.getKB(locationR-1, (locationC)).getAtt().contains("W"))	//north
			{
			shootWumpus(locationR-1, locationC, board);
			}
		
		if (kb.getKB(locationR+1, (locationC)).getAtt().contains("W"))	//south
			{ 
			shootWumpus(locationR+1, locationC, board);
			}
		
		System.out.println("No safe moves");
		return false;
		}
	
	
	/**
	 * 
	 * @param r
	 * @param c
	 * @param board
	 * @return
	 */
	public boolean shootWumpus(int r, int c, Board board)
		{
		setHasArrow(false);
		setScore(-10);
		
		if (board.isWumpusDead(r, c))
			{
			System.out.println("You shot the wumpus!");
			return true;
			}
		
		System.out.println("Oh you're in big trouble. You missed!");	
		return false;
		}
	
	
	/**
	 * 
	 */
	private void escape(int currentCost, KnowledgeBase kb, Board board)
		{
		int finalRow = kb.getStarting_row();
		int finalCol = kb.getStarting_column();
		
		boolean [][] visited = kb.getVisitedSquares();
		
		if (currentCost == 0)
			{
			System.out.println("You win!");
			System.out.println(getScore());
			System.out.println(score);
			System.exit(0);
			}
		
		kb.printKB();
		
		int costNorth = 0 , costSouth = 0, costEast = 0, costWest = 0;
		try
			{
			if (visited[locationR-1][locationC] == true)
				{
				costNorth = ((locationR-1) - finalRow) + (locationC - finalCol);
					
				if (costNorth < currentCost)
					{
					System.out.println("Moving North");
					setLocation(locationR-1, locationC, board, kb);
					escape(costNorth, kb, board);
					}
				}
			}
		catch(Exception e)
			{
			System.out.println("out of bounds");
			}
		
		try
			{
			if (visited[locationR+1][locationC] == true)
				{
				costSouth = ((locationR+1) - finalRow) + (locationC - finalCol);
					
				if (costSouth < currentCost)
					{
					System.out.println("Moving South");
					setLocation(locationR+1, locationC, board, kb);
					escape(costSouth, kb, board);
					}
				}
			}
		catch(Exception e)
			{
			System.out.println("out of bounds");
			}
		
		try
			{
			if (visited[locationR][locationC-1] == true)
				{
				costWest = ((locationR) - finalRow) + ((locationC-1) - finalCol);
				if (costWest < currentCost)
					{
					System.out.println("Moving West");
					setLocation(locationR, locationC-1, board, kb);
					escape(costWest, kb, board);
					}
				}
			}
		catch(Exception e)
			{
			System.out.println("out of bounds");
			}
		
		try
			{
			if (visited[locationR][locationC+1] == true)
				{
				costEast = ((locationR) - finalRow) + ((locationC+1) - finalCol);
				if (costEast < currentCost)
					{
					System.out.println("Moving East");
					setLocation(locationR, locationC+1, board, kb);
					escape(costEast, kb, board);
					}
				}
			}
		catch(Exception e)
			{
			System.out.println("out of bounds");
			}
			
		
		System.out.println("Houston, we have a problem");
		
		//System.out.println(getScore());
		}
	 
	
	/**
	 * 
	 */
	public void getGold(KnowledgeBase kb, Board board)
		{
		if (board.gotGold(locationR, locationC))
			{
			hasGold = true;
			setScore(1000);
			int distance = (locationR - kb.getStarting_row()) + (locationC - kb.getStarting_column());
			
			escape(distance, kb, board);
			}
		}
	
	}//end of class
