package com.humming.asc.sales.model;

public class PositionRequest {
	private String postnRowId;

	public PositionRequest() {
	}

	public PositionRequest(String postnRowId) {
		this.postnRowId = postnRowId;
	}

	public String getPostnRowId() {
		return postnRowId;
	}

	public void setPostnRowId(String postnRowId) {
		this.postnRowId = postnRowId;
	}
}
