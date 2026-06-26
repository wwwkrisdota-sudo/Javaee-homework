package com.live.tip.audience.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.live.tip.audience.client.FinanceClient;
import com.live.tip.audience.mapper.TipRecordMapper;
import com.live.tip.audience.vo.FinanceSyncResultVO;
import com.live.tip.common.dto.SettlementReceiveRequest;
import com.live.tip.common.entity.TipRecord;
import com.live.tip.common.enums.FinanceSyncStatusEnum;
import com.live.tip.common.result.Result;
import com.live.tip.common.result.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinanceSyncService {

    private static final int BATCH_SIZE = 100;

    private final TipRecordMapper tipRecordMapper;
    private final FinanceClient financeClient;

    @Async("financeSyncExecutor")
    public void syncSingleAsync(TipRecord record) {
        syncRecord(record);
    }

    public FinanceSyncResultVO syncPendingTips() {
        List<TipRecord> pendingRecords = tipRecordMapper.selectList(
                new LambdaQueryWrapper<TipRecord>()
                        .in(TipRecord::getFinanceSyncStatus,
                                FinanceSyncStatusEnum.PENDING.getCode(),
                                FinanceSyncStatusEnum.FAILED.getCode())
                        .orderByAsc(TipRecord::getId)
                        .last("LIMIT " + BATCH_SIZE));

        FinanceSyncResultVO result = new FinanceSyncResultVO();
        result.setTotalScanned(pendingRecords.size());

        int success = 0;
        int failed = 0;
        for (TipRecord record : pendingRecords) {
            if (syncRecord(record)) {
                success++;
            } else {
                failed++;
            }
        }
        result.setSuccessCount(success);
        result.setFailedCount(failed);
        return result;
    }

    private boolean syncRecord(TipRecord record) {
        SettlementReceiveRequest request = buildRequest(record);
        try {
            Result<Void> response = financeClient.receiveSettlement(request);
            if (response != null && response.getCode() == ResultCode.SUCCESS.getCode()) {
                markSynced(record.getId());
                log.info("财务同步成功, tipId={}, tipNo={}", record.getId(), record.getTipNo());
                return true;
            }
            markFailed(record.getId());
            log.warn("财务同步失败, tipId={}, tipNo={}, response={}",
                    record.getId(), record.getTipNo(), response);
            return false;
        } catch (Exception e) {
            markFailed(record.getId());
            log.warn("财务同步异常, tipId={}, tipNo={}, error={}",
                    record.getId(), record.getTipNo(), e.getMessage());
            return false;
        }
    }

    private SettlementReceiveRequest buildRequest(TipRecord record) {
        SettlementReceiveRequest request = new SettlementReceiveRequest();
        request.setTipId(record.getId());
        request.setTipNo(record.getTipNo());
        request.setAnchorId(record.getAnchorId());
        request.setAudienceId(record.getAudienceId());
        request.setAmount(record.getAmount());
        request.setTipTime(record.getTipTime());
        return request;
    }

    private void markSynced(Long tipId) {
        tipRecordMapper.update(null, new LambdaUpdateWrapper<TipRecord>()
                .eq(TipRecord::getId, tipId)
                .in(TipRecord::getFinanceSyncStatus, Arrays.asList(
                        FinanceSyncStatusEnum.PENDING.getCode(),
                        FinanceSyncStatusEnum.FAILED.getCode()))
                .set(TipRecord::getFinanceSyncStatus, FinanceSyncStatusEnum.SYNCED.getCode()));
    }

    private void markFailed(Long tipId) {
        tipRecordMapper.update(null, new LambdaUpdateWrapper<TipRecord>()
                .eq(TipRecord::getId, tipId)
                .set(TipRecord::getFinanceSyncStatus, FinanceSyncStatusEnum.FAILED.getCode()));
    }
}
