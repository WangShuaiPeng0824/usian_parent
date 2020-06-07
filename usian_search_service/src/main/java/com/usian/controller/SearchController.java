package com.usian.controller;

import com.usian.pojo.SearchItem;
import com.usian.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/service/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @RequestMapping("/importAll")
    public Boolean importAll(){
        return searchService.importAll();
    }

    @RequestMapping("/selectByQ")
    public List<SearchItem> selectByQ(String q,Long page,Integer pageSize){
        return searchService.selectByQ(q,page,pageSize);
    }
}
