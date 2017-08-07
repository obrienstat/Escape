package world;

import java.io.Serializable;
import java.util.*;
public class coordinate implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2112820122609181973L;
	
	private int x;
	private int y;
	private int z;
	
	public coordinate(int x, int y, int z) {
		this.x=x;
		this.y=y;
		this.z=z;
	}

	public coordinate(int[] values){
		//use array over list to avoid Integer to int conversion
		this.x=values[0];
		this.y=values[1];
		this.z=values[2];
	}
	public int[] getValues(){
		int values[]={x,y,z};
		return values;
	}

	public int getX(){
		return x;

	}

	public int getY(){
		return y;
		}

	public int getZ(){
		return z;
	}
	
	
	/**
	 * Return comma separated String of coordinates x,y,z
	 */
	@Override
	public String toString(){
		int values[] = this.getValues();
		return(values[0]+","+values[1]+","+values[2]);
		
	}

}

