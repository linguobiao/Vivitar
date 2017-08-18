package com.gzgamut.vivitar.been;

import java.util.Calendar;

public class Info {
	private int InfoId;
	private double weight;
	private double fat;
	private double water;
	private double muscle;
	private double bone;
	private double bmi;
	private double kcal;

	private int pUser;
	private Calendar date;
	
	
	public double getKcal() {
		return kcal;
	}

	public void setKcal(double kcal) {
		this.kcal = kcal;
	}


	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public int getpUser() {
		return pUser;
	}

	public void setpUser(int pUser) {
		this.pUser = pUser;
	}

	public int getInfoId() {
		return InfoId;
	}

	public void setInfoId(int infoId) {
		InfoId = infoId;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getFat() {
		return fat;
	}

	public void setFat(double fat) {
		this.fat = fat;
	}

	public double getWater() {
		return water;
	}

	public void setWater(double water) {
		this.water = water;
	}

	public double getMuscle() {
		return muscle;
	}

	public void setMuscle(double muscle) {
		this.muscle = muscle;
	}

	public double getBone() {
		return bone;
	}

	public void setBone(double bone) {
		this.bone = bone;
	}


	public double getBmi() {
		return bmi;
	}

	public void setBmi(double bmi) {
		this.bmi = bmi;
	}

}
