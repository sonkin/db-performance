package com.luxoft.highperformance.repositories;

import org.springframework.transaction.annotation.Transactional;

public class BookDaoImpl implements BookDaoCustom {

	
	@Transactional
	@Override
	public void setPublishingHouse(String publishingHouse) {
	}

}
