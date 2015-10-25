package com.ticketservice.dao;


import com.ticketservice.domain.SeatHold;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface SeatHoldRepository extends CrudRepository<SeatHold, Long>, SeatHoldRepositoryCustom {
}


