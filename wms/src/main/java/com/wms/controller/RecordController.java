package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Goods;
import com.wms.entity.Record;
import com.wms.service.GoodsService;
import com.wms.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zzq
 * @since 2025-04-30
 */
@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordService recordService;
    @Autowired
    private GoodsService goodsService;
    // 查
    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query) {

        HashMap param = query.getParam();
        String name = (String) param.get("name");
        String goodstype = (String) param.get("goodstype");
        String storage = (String) param.get("storage");
        String roleId = (String) param.get("roleId");
        String userId = (String) param.get("userId");

        Page<Record> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply(" a.goods=b.id and b.storage=c.id and b.goodsType=d.id ");

        if(roleId.equals("2")){
            queryWrapper.apply(" a.userId = "+userId);
        }

        if(StringUtils.isNotBlank(name) && !name.equals("null")){
            queryWrapper.like("b.name",name);
        }
        if(StringUtils.isNotBlank(storage) && !storage.equals("null")){
            queryWrapper.eq("c.id",storage);
        }
        if(StringUtils.isNotBlank(goodstype) && !goodstype.equals("null")){
            queryWrapper.eq("d.id",goodstype);
        }
        // IPage result = GoodsService.pageC(page);
        IPage<Record> result = recordService.pageCC(page,queryWrapper);
        return Result.suc(result.getRecords(), result.getTotal());
    }
    //增
    @PostMapping("/save")
    public Result save(@RequestBody Record record) {
        Goods goods =  goodsService.getById(record.getGoods());
        int count = record.getCount();
        if (record.getAction().equals("2")){
            count = -count;
            record.setCount(count);
        }
        int num = goods.getCount() + count;
        if (num < 0){
            return Result.fail();
        }
        goods.setCount(num);
        goodsService.updateById(goods);

        return recordService.save(record) ? Result.suc() : Result.fail();
    }
}
