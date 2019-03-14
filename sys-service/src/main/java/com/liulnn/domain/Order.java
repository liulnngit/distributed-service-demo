package com.liulnn.domain;

public class Order {
	
	private String merchantId;
	
	private double txMoney;
	
	
	public Order() {
	}

	public Order(String merchantId,double txMoney) {
		super();
		this.merchantId = merchantId;
		this.txMoney = txMoney;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public double getTxMoney() {
		return txMoney;
	}

	public void setTxMoney(double txMoney) {
		this.txMoney = txMoney;
	}
	
}
