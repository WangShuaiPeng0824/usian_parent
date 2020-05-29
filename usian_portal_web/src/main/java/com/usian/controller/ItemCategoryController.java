package com.usian.controller;

import com.usian.feign.ItemServiceFeign;
import com.usian.utils.CatNode;
import com.usian.utils.CatResult;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/frontend/itemCategory")
public class ItemCategoryController {

    @Autowired
    private ItemServiceFeign itemServiceFeign;

    /**
     * 查询商品分类菜单
     * @return
     */
    @RequestMapping("/selectItemCategoryAll")
    public Result selectItemCategoryAll(){
        CatResult catResult = itemServiceFeign.selectItemCategoryAll();
        if (catResult.getData().size()>0){
            return Result.ok(catResult);
        }
        return Result.error("查无结果");
    }

}
