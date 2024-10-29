package com.zsh.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsh.task.entity.TradAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TradAccountMapper extends BaseMapper<TradAccount> {
    int insertSelective(TradAccount entity);
    int updateByIdSelective(TradAccount entity);

    int addOrLeWantNum(@Param("id") Long id,
                       @Param("isAdd")boolean isAdd);
}
