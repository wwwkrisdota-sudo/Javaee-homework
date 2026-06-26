package com.live.tip.audience.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.live.tip.audience.mapper.AnchorMapper;
import com.live.tip.audience.mapper.AudienceMapper;
import com.live.tip.audience.mapper.TipRecordMapper;
import com.live.tip.audience.vo.TipResponse;
import com.live.tip.common.constant.CommonConstants;
import com.live.tip.common.dto.TipRequest;
import com.live.tip.common.entity.Anchor;
import com.live.tip.common.entity.Audience;
import com.live.tip.common.entity.TipRecord;
import com.live.tip.common.enums.FinanceSyncStatusEnum;
import com.live.tip.common.exception.BusinessException;
import com.live.tip.common.result.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TipService {

    private final TipRecordMapper tipRecordMapper;
    private final AnchorMapper anchorMapper;
    private final AudienceMapper audienceMapper;
    private final FinanceSyncService financeSyncService;

    @Transactional(rollbackFor = Exception.class)
    public TipResponse createTip(TipRequest request) {
        validateAnchor(request.getAnchorId());
        validateAudience(request.getAudienceId());

        TipRecord existing = tipRecordMapper.selectOne(
                new LambdaQueryWrapper<TipRecord>().eq(TipRecord::getTipNo, request.getTipNo()));
        if (existing != null) {
            throw new BusinessException(ResultCode.DUPLICATE_TIP);
        }

        TipRecord record = buildTipRecord(request);
        try {
            tipRecordMapper.insert(record);
        } catch (DuplicateKeyException e) {
            throw new BusinessException(ResultCode.DUPLICATE_TIP);
        }

        log.info("打赏记录已写入, tipNo={}, tipId={}, traceId={}",
                record.getTipNo(), record.getId(), record.getTraceId());

        financeSyncService.syncSingleAsync(record);
        return toResponse(record);
    }

    private void validateAnchor(Long anchorId) {
        Anchor anchor = anchorMapper.selectById(anchorId);
        if (anchor == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "主播不存在");
        }
        if (anchor.getStatus() != null && anchor.getStatus() == 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "主播已禁用");
        }
    }

    private void validateAudience(Long audienceId) {
        Audience audience = audienceMapper.selectById(audienceId);
        if (audience == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "观众不存在");
        }
    }

    private TipRecord buildTipRecord(TipRequest request) {
        TipRecord record = new TipRecord();
        record.setTipNo(request.getTipNo());
        record.setAnchorId(request.getAnchorId());
        record.setAudienceId(request.getAudienceId());
        record.setAmount(request.getAmount());
        record.setTipTime(LocalDateTime.now());
        record.setTraceId(MDC.get(CommonConstants.MDC_TRACE_ID));
        record.setFinanceSyncStatus(FinanceSyncStatusEnum.PENDING.getCode());
        return record;
    }

    private TipResponse toResponse(TipRecord record) {
        TipResponse response = new TipResponse();
        response.setTipId(record.getId());
        response.setTipNo(record.getTipNo());
        response.setAnchorId(record.getAnchorId());
        response.setAudienceId(record.getAudienceId());
        response.setAmount(record.getAmount());
        response.setTraceId(record.getTraceId());
        response.setFinanceSyncStatus(record.getFinanceSyncStatus());
        return response;
    }
}
