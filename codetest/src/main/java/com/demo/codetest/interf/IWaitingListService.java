package com.demo.codetest.interf;

import com.demo.codetest.dto.WaitingListDTO;
import com.demo.codetest.dto.WaitingListRequestDTO;

public interface IWaitingListService {

	WaitingListDTO addToWaitList(WaitingListRequestDTO waitingListRequestDTO);

}
