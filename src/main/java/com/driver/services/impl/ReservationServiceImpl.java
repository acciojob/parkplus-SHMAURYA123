package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;

    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        User user;
        ParkingLot parkingLot;
        try {
            user = userRepository3.findById(userId).get();
            parkingLot = parkingLotRepository3.findById(parkingLotId).get();
        } catch (Exception e) {
            throw new Exception("Cannot make reservation");
        }

        Spot spot = null;
        int minCost = Integer.MAX_VALUE;

        for (Spot spot1 : parkingLot.getSpotList()) {
            int wheel = 0;
            if (spot1.getSpotType() == SpotType.TWO_WHEELER) {
                wheel = 2;
            }
            if (spot1.getSpotType() == SpotType.FOUR_WHEELER) {
                wheel = 4;
            }
            if (spot1.getSpotType() == SpotType.OTHERS) {
                wheel = 24;
            }

            if (!spot1.getOccupied() && numberOfWheels <= wheel && spot1.getPricePerHour() * timeInHours < minCost) {
                minCost = spot1.getPricePerHour() * timeInHours;
                spot = spot1;
            }
        }
        if (spot == null) {
            throw new Exception("Cannot make reservation");
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setNumberOfHours(timeInHours);
        reservation.setSpot(spot);

        user.getReservationList().add(reservation);
        spot.getReservationList().add(reservation);
        spot.setOccupied(true);

        userRepository3.save(user);
        spotRepository3.save(spot);

        return reservation;
    }
}

