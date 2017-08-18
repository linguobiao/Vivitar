package com.gzgamut.vivitar.been;

import android.graphics.Bitmap;

public class Value {
	private int id;
	private Bitmap head;
	private String name;
	private int sex;
	private int age;
	private double height;
	private double weight;

	private double bmi;
	private double fat;
	private int seekWeight;
	private int seekBmi;
	private int seekFat;
	private String judgeUnit;
	
private String date;
	
	public String getDate() {
		return this.date;
	} 
	public void setDate(String date) {
		this.date = date;
	}

	public String getJudgeUnit() {
		return judgeUnit;
	}

	public void setJudgeUnit(String judgeUnit) {
		this.judgeUnit = judgeUnit;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Bitmap getHead() {
		return this.head;
	}

	public void setHead(Bitmap head) {
		this.head = head;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getBmi() {
		return this.bmi;
	}

	public void setBmi(double bmi) {
		this.bmi = bmi;
	}

	public double getFat() {
		return this.fat;
	}

	public void setFat(double fat) {
		this.fat = fat;
	}

	public int getSeekWeight() {
		return seekWeight;
	}

	public void setSeekWeight(int seekWeight) {
		this.seekWeight = seekWeight;
	}

	public int getSeekBmi() {
		return seekBmi;
	}

	public void setSeekBmi(int seekBmi) {
		this.seekBmi = seekBmi;
	}

	public int getSeekFat() {
		return seekFat;
	}

	public void setSeekFat(int seekFat) {
		this.seekFat = seekFat;
	}
}
