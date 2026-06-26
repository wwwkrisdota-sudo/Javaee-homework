package com.live.tip.audience.client;

import com.live.tip.common.constant.CommonConstants;
import com.live.tip.common.dto.SettlementReceiveRequest;
import com.live.tip.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = CommonConstants.SERVICE_FINANCE, path = "/api/finance")
public interface FinanceClient {

    @PostMapping("/settlements/receive")
    Result<Void> receiveSettlement(@RequestBody SettlementReceiveRequest request);
}
