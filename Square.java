package edu.bu.met.cs664.Wumpus;

public class Square 
	{
	private boolean gold, stench, breeze, pit, wumpus, enter, agent, visited, noPit, noWumpus;
	

	private int row, column;
	
	public Square(String type, int row, int column, String att)
		{
		//I put this in here because the board should have no unknowns--if its not explicitly marked, then its not there => false.
		//This could be a seperate constructor, but its easier just to include this "type" if we need a blank slate square.
		if (type == "board")
			{
			this.gold = false;
			this.stench = false;
			this.breeze = false;
			this.pit = false;
			this.wumpus = false;
			this.enter = false;
			this.agent = false;
			}
		
		this.row = row;
		this.column = column;
		
		if (att.contains("e"))
			{
			this.enter = true;
			}
		
		if (att.toLowerCase().contains("g"))
			{
			this.gold = true;
			}
		
		if (att.toLowerCase().contains("s"))
			{
			this.stench = true;
			}
		
		if (att.toLowerCase().contains("b"))
			{
			this.breeze = true;
			}
		
		if (att.toLowerCase().contains("p"))
			{
			this.pit = true;
			}
		
		if (att.toLowerCase().contains("w"))
			{
			this.wumpus = true;
			}
		
		}
	
	
	/**
	 * 
	 * @param att
	 */
	public void setAtt(String attr)
		{
		if (attr.toLowerCase().contains("e"))
			{
			this.enter = true;
			}
	
		if (attr.toLowerCase().contains("g"))
			{
			this.gold = true;
			}
	
		if (attr.toLowerCase().contains("s"))
			{
			this.stench = true;
			}
		
		if (attr.toLowerCase().contains("b"))
			{
			this.breeze = true;
			}
		
		if (attr.toLowerCase().contains("p"))
			{
			this.pit = true;
			}
		
		if (attr.toLowerCase().contains("w"))
			{
			this.wumpus = true;
			}	
		
		if (attr.toLowerCase().contains("a"))
			{
			this.agent = true;
			}
		
		}
	
	
	/**
	 * 
	 * @return
	 */
	public String getAtt()
		{
		String string = "";
		if (this.visited == true)
			{
			string = string + "V";
			}
		
		if (this.enter == true)
			{
			string = string + "E ";
			}
	
		if (this.gold == true)
			{
			string = string + "G ";
			}
	
		if (this.stench == true)
			{
			string = string + "S ";
			}
		
		if (this.breeze == true)
			{
			string = string + "B ";
			}
		
		if (this.pit == true)
			{
			string = string + "P ";
			}
		
		if (this.wumpus == true)
			{
			string = string + "W";
			}
		
		if (this.agent == true)
			{
			string = string + "A";
			}
		
		return string;
		}
	
	
	/**
	 * @return the visited
	 */
	public boolean haveVisited() 
		{
		return visited;
		}


	/**
	 * @param visited the visited to set
	 */
	public void setVisited(boolean visited) 
		{
		this.visited = visited;
		}

	
	/**
	 * 
	 * @return
	 */
	public boolean noPit()
		{
		return noPit;
		}
	
	
	/**
	 * 
	 * @param safe
	 */
	public void setNoPit(boolean safe)
		{
		this.noPit = safe;
		}
	
	
	/**
	 * 
	 * @return
	 */
	public boolean noWumpus()
		{
		return noWumpus;
		}
	
	
	/**
	 * 
	 * @param safe
	 */
	public void setNoWumpus(boolean safe)
		{
		this.noWumpus = safe;
		}
	
	/**
	 * 
	 * @return
	 */
	public boolean isGold()
		{
		return this.gold;
		}

	
	/**
	 * @param gold the gold to set
	 */
	public void setGold(boolean gold) 
		{
		this.gold = gold;
		}
 

	/**
	 * @return the agent
	 */
	public boolean getAgent() 
		{
		return agent;
		}


	/**
	 * @param agent the agent to set
	 */
	public void setAgent(boolean agent) {
		this.agent = agent;
	}

	
	/**
	 * @return the stench
	 */
	public boolean isStench() 
		{
		return stench;
		}

	
	/**
	 * @param stench the stench to set
	 */
	public void setStench(boolean stench) 
		{
		this.stench = stench;
		}

	
	/**
	 * @return the breeze
	 */
	public boolean isBreeze() 
		{
		return breeze;
		}

	
	/**
	 * @param breeze the breeze to set
	 */
	public void setBreeze(boolean breeze) 
		{
		this.breeze = breeze;
		}

	
	/**
	 * @return the pit
	 */
	public boolean isPit() 
		{
		return pit;
		}

	
	/**
	 * @param pit the pit to set
	 */
	public void setPit(boolean pit) 
		{
		this.pit = pit;
		}

	
	/**
	 * @return the wumpus
	 */
	public boolean isWumpus() 
		{
		return wumpus;
		}

	
	/**
	 * @param wumpus the wumpus to set
	 */
	public void setWumpus(boolean wumpus) 
		{
		this.wumpus = wumpus;
		}

	
	/**
	 * @return the enter
	 */
	public boolean isEnter() 
		{
		return enter;
		}

	
	/**
	 * @param enter the enter to set
	 */
	public void setEnter(boolean enter) 
		{
		this.enter = enter;
		}

	
	/**
	 * @return the row
	 */
	public int getRow() 
		{
		return row;
		}

	
	/**
	 * @param row the row to set
	 */
	public void setRow(int row) 
		{
		this.row = row;
		}

	
	/**
	 * @return the column
	 */
	public int getColumn() 
		{
		return column;
		}

	
	/**
	 * @param column the column to set
	 */
	public void setColumn(int column) 
		{
		this.column = column;
		}
	
	}//end of class
