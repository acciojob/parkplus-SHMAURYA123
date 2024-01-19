package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        //Attempt a payment of amountSent for reservationId using the given mode ("cASh", "card", or "upi")
        //If the amountSent is less than bill, throw "Insufficient Amount" exception, otherwise update payment attributes
        //If the mode contains a string other than "cash", "card", or "upi" (any character in uppercase or lowercase), throw "Payment mode not detected" exception.
        //Note that the reservationId always exists

        Reservation reservation;
        try {
             reservation = reservationRepository2.findById(reservationId).get();
        }
        catch (Exception e){
            throw new Exception("Reservation id is not Valid");
        }

        if(amountSent< reservation.getNumberOfHours()*reservation.getNumberOfHours()){
            throw new Exception("Insufficient Amount");
        }

        PaymentMode paymentMode=null;

        if(mode.toUpperCase().equals(PaymentMode.CASH.toString())){
            paymentMode=PaymentMode.CASH;
        }
        else if(mode.toUpperCase().equals(PaymentMode.UPI.toString())){
            paymentMode=PaymentMode.UPI;
        }
        else{
            paymentMode=PaymentMode.CARD;
        }

        Payment payment=new Payment();
        payment.setPaymentMode(paymentMode);
        payment.setPaymentCompleted(true);
        payment.setReservation(reservation);

        paymentRepository2.save(payment);
        reservationRepository2.save(reservation);
        return payment;
    }
}
