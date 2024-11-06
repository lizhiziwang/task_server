package com.zsh.task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsh.task.entity.MoneyRecord;
import com.zsh.task.mapper.MoneyRecordMapper;
import com.zsh.task.service.MoneyRecordService;
import org.springframework.stereotype.Service;

@Service
public class MoneyRecordServiceImpl extends ServiceImpl<MoneyRecordMapper,MoneyRecord> implements MoneyRecordService {
}
