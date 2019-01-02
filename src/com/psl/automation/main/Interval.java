package com.psl.automation.main;

import java.util.ArrayList;

public class Interval {

	private String chromosome;
	private long start;
	private long stop;


	public static boolean checkIntersection(ArrayList<Interval> hmvcDel, Interval tempInt){

		for(Interval inter1: hmvcDel){

			if(inter1.chromosome.equals(tempInt.chromosome)){
				if(inter1.start <= tempInt.stop && inter1.stop >= tempInt.start){
					return true;
				}
			}
		}
		return false;	
	} 

	public static int getIntersectingIntervalIndex(ArrayList<Interval> hmvcDel, Interval tempInt){

		int index=-1;
		for(Interval inter1: hmvcDel){
			index++;
			if(inter1.chromosome.equals(tempInt.chromosome)){
				if(inter1.start <= tempInt.stop && inter1.stop >= tempInt.start){
					return index;
				}
			}
		}
		return -1;	
	}


	public String getChromosome() {
		return chromosome;
	}

	public void setChromosome(String chromosome) {
		this.chromosome = chromosome;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getStop() {
		return stop;
	}

	public void setStop(long stop) {
		this.stop = stop;
	}

	public Interval(String chromosome, long start, long stop) {
		super();
		this.chromosome = chromosome;
		this.start = start;
		this.stop = stop;
	} 

	
	public static long calculateMin(Interval inter1, Interval inter2){
		
		if(inter1.getStart()< inter2.getStart()){
			return inter1.getStart();
		}else{
			
			return inter2.getStart();
		}
		
	}

	public static long calculateMax(Interval inter1, Interval inter2){
		
		if(inter1.getStop() > inter2.getStop()){
			return inter1.getStop();
		}else{
			
			return inter2.getStop();
		}
		
	}

}
