package com.dollar.penguin.meta.service;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.dollar.penguin.meta.model.DataBaseConfigTemplatePojo;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class DataBaseConfigDataListener implements ReadListener<DataBaseConfigTemplatePojo> {

    private final List<DataBaseConfigTemplatePojo> dataList = ListUtils.newArrayList();

    @Override
    public void invoke(DataBaseConfigTemplatePojo data, AnalysisContext context) {
        dataList.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("[DataBaseConfigDataListener]All data parsing completed.");
    }
}
