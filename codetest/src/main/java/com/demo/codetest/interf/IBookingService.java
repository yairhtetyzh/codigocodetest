package com.demo.codetest.interf;

import com.demo.codetest.dto.BookingDTO;
import com.demo.codetest.dto.BookingRequestDTO;
import com.demo.codetest.dto.CancelBookingRequestDTO;
import com.demo.codetest.dto.CheckInBookingRequestDTO;

public interface IBookingService {

	BookingDTO makeBooking(BookingRequestDTO bookingRequestDTO);

	BookingDTO cancelBooking(CancelBookingRequestDTO cancelBookingRequestDTO);

	BookingDTO checkInBooking(CheckInBookingRequestDTO checkInBookingRequestDTO);

}
