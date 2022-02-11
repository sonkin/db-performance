package com.luxoft.highperformance.repositories;

import com.luxoft.highperformance.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogDao extends JpaRepository<Log, Integer> {


}
