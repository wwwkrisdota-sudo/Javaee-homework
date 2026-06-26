package com.live.tip.audience.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.live.tip.audience.vo.TopTipperVO;
import com.live.tip.common.entity.TipRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TipRecordMapper extends BaseMapper<TipRecord> {

    @Select("""
            SELECT tr.audience_id AS audienceId,
                   a.name AS audienceName,
                   SUM(tr.amount) AS totalAmount
            FROM tip_record tr
            INNER JOIN audience a ON tr.audience_id = a.id
            WHERE tr.anchor_id = #{anchorId}
            GROUP BY tr.audience_id, a.name
            ORDER BY totalAmount DESC
            LIMIT #{offset}, #{limit}
            """)
    List<TopTipperVO> selectTopTippers(@Param("anchorId") Long anchorId,
                                       @Param("offset") int offset,
                                       @Param("limit") int limit);
}
