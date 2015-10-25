package com.ticketservice.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;


@Transactional
public class SeatHoldRepositoryImpl implements SeatHoldRepositoryCustom {

    @PersistenceContext
    private EntityManager em;


    public int getCountOfSeatsOnHoldAndReserved(int levelId) {
        Query q = getCurrentSession().createQuery("select count(seatTransaction) from SeatTransaction seatTransaction \n" +
                " where seatTransaction.levelId = :levelId \n" +
                " and seatTransaction.seatHold.reservedTimestamp !=null \n " +
                " and seatTransaction.seatHold.seatHoldExpirationTimestamp > :currentTime");
        q.setParameter("levelId", levelId);
        q.setParameter("currentTime",new Date());
        Long count = (Long)q.list().get(0);
        return count.intValue();
    }



    @Modifying(clearAutomatically = true)
    public int reserve(Date reservedTimestamp, Long seatHoldId) {
        Query q = getCurrentSession().createQuery("UPDATE SeatHold seatHold \n" +
                "SET seatHold.reservedTimestamp = :reservedTimestamp \n" +
                "WHERE seatHold.seatHoldId = :seatHoldId");
        q.setParameter("reservedTimestamp", reservedTimestamp);
        q.setParameter("seatHoldId", seatHoldId);
        return q.executeUpdate();
    }

    private Session getCurrentSession(){
        return em.unwrap(Session.class);
    }
}
