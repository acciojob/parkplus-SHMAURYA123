package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
       ParkingLot parkingLot=new ParkingLot();
       parkingLot.setName(name);
       parkingLot.setAddress(address);
       parkingLotRepository1.save(parkingLot);
       return parkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        //create a new spot in the parkingLot with given id
        //the spot type should be the next biggest type in case the number of wheels are not 2 or 4, for 4+ wheels, it is others
        ParkingLot parkingLot=parkingLotRepository1.findById(parkingLotId).get();

        Spot spot=new Spot();
        spot.setParkingLot(parkingLot);
        spot.setPricePerHour(pricePerHour);
        spot.setOccupied(false);

        if(numberOfWheels<=2){
            spot.setSpotType(SpotType.TWO_WHEELER);
        }
        else if (numberOfWheels<=4) {
            spot.setSpotType(SpotType.FOUR_WHEELER);
        }
        else if(numberOfWheels > 4)
            spot.setSpotType(SpotType.OTHERS);

        parkingLot.getSpotList().add(spot);
        parkingLotRepository1.save(parkingLot);
        return spot;

    }

    @Override
    public void deleteSpot(int spotId) {
        //delete a spot from given parking lot
        Spot spot=spotRepository1.findById(spotId).get();
         ParkingLot parkingLot=spot.getParkingLot();
         parkingLot.getSpotList().remove(spot);
         spotRepository1.delete(spot);
}

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        //update the details of a spot
        ParkingLot parkingLot=parkingLotRepository1.findById(parkingLotId).get();
        Spot spot=null;

        for(Spot spot1:parkingLot.getSpotList()){
            if(spot1.getId()==spotId){
                spot.setPricePerHour(pricePerHour);
                spot=spot1;
                spotRepository1.save(spot1);
                break;
            }
        }
        return spot;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {

        parkingLotRepository1.deleteById(parkingLotId);
    }
}
