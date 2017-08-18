package com.gzgamut.vivitar.been;

import java.util.List;

public class Chart {
	private String title;
	private String unit;
	private List<Integer> value;
	
	public String getTitle() {
		return this.title;
	}
	public void setTtile(String title) {
		this.title = title;
	}
	public String getUnit() {
		return this.unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public List<Integer> getValue() {
		return this.value;
	}
	public void setValue(List<Integer> value) {
		this.value = value;
	}
	
}
