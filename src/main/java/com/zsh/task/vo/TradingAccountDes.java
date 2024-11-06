package com.zsh.task.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class TradingAccountDes {
    private List<String> png;
    private List<String> video;
}
