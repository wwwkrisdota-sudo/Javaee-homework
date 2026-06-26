package com.live.tip.audience.service;

import com.live.tip.audience.mapper.AnchorMapper;
import com.live.tip.audience.mapper.TipRecordMapper;
import com.live.tip.audience.vo.TopTipperVO;
import com.live.tip.common.entity.Anchor;
import com.live.tip.common.exception.BusinessException;
import com.live.tip.common.result.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopTipperService {

    private static final int MAX_PAGE_SIZE = 50;

    private final TipRecordMapper tipRecordMapper;
    private final AnchorMapper anchorMapper;

    public List<TopTipperVO> getTopTippers(Long anchorId, int page, int size) {
        Anchor anchor = anchorMapper.selectById(anchorId);
        if (anchor == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "主播不存在");
        }

        int safePage = Math.max(page, 1);
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        int offset = (safePage - 1) * safeSize;

        List<TopTipperVO> tippers = tipRecordMapper.selectTopTippers(anchorId, offset, safeSize);
        for (int i = 0; i < tippers.size(); i++) {
            tippers.get(i).setRank(offset + i + 1);
        }
        return tippers;
    }
}
