package com.zsh.task.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsh.task.entity.TradAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface TradAccountMapper extends BaseMapper<TradAccount> {
    int insertSelective(TradAccount entity);
    int updateByIdSelective(TradAccount entity);

    int addOrLeWantNum(@Param("id") Long id,
                       @Param("isAdd")boolean isAdd);

    Page<TradAccount> selectPage(@Param("userId") Long userId, Page<?> page, @Param(Constants.WRAPPER)QueryWrapper<TradAccount> qw);
}
