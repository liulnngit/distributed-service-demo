package com.javadev;

public interface IBloomFilterService {
	
	boolean contain(String term);
	
	boolean put(String term);
}
