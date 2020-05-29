package com.usian.controller;

import com.usian.pojo.TbContent;
import com.usian.pojo.TbContentCategory;
import com.usian.service.ContentCategoryService;
import com.usian.service.ContentService;
import com.usian.utils.AdNode;
import com.usian.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/service/content")
public class ContentController {
    @Autowired
    private ContentService contentService;

    /**
     * 根据分类查询内容
     */
    @RequestMapping("/selectTbContentAllByCategoryId")
    public PageResult selectTbContentAllByCategoryId(@RequestParam Integer page,
                                                     @RequestParam Integer rows, @RequestParam Long categoryId) {
        return this.contentService.selectTbContentAllByCategoryId(page, rows, categoryId);
    }

    @RequestMapping("/insertTbContent")
    public Integer insertTbContent(@RequestBody TbContent tbContent){
        return contentService.insertTbContent(tbContent);
    }

    @RequestMapping("/deleteContentByIds")
    public Integer deleteContentByIds(Long ids){
        return contentService.deleteContentByIds(ids);
    }

    @RequestMapping("/selectFrontendContentByAD")
    public List<AdNode> selectFrontendContentByAD(){
        return contentService.selectFrontendContentByAD();
    }
}
