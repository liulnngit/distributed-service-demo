package com.liulnn.service;

import com.liulnn.domain.Order;

public interface IOrderService {
	
	void insert(Order order);
	
	Order selectOrderByMerchantId(String merchantId);
	
	Double getTotalAmountByMerchantId(String merchantId);
	
	
}
