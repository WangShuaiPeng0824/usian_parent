package com.usian.service;

import com.usian.mapper.TbItemCatMapper;
import com.usian.mapper.TbItemMapper;
import com.usian.pojo.TbItemCat;
import com.usian.pojo.TbItemCatExample;
import com.usian.pojo.TbItemExample;
import com.usian.utils.CatNode;
import com.usian.utils.CatResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ItemCatServiceImpl implements ItemCatService{

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public List<TbItemCat> selectItemCategoryByParentId(Long id) {
        TbItemCatExample tbItemCatExample = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = tbItemCatExample.createCriteria();
        criteria.andStatusEqualTo(1);
        criteria.andParentIdEqualTo(id);
        return tbItemCatMapper.selectByExample(tbItemCatExample);
    }

    @Override
    public CatResult selectItemCategoryAll() {
        //1、因为一级菜单有子菜单，子菜单有子子菜单，所以要递归调用
        List<CatNode> catList = getCatList(0L);
        CatResult catResult = new CatResult();
        catResult.setData(catList);
        return catResult;
    }

    private List<CatNode> getCatList(Long parentId){
        //1、查询商品类目列表
        TbItemCatExample tbItemCatExample = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = tbItemCatExample.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbItemCat> tbItemCatList = tbItemCatMapper.selectByExample(tbItemCatExample);

        //2、拼接CatNode
        List catNodeList = new ArrayList();
        int count = 0;
        for (int i = 0; i < tbItemCatList.size(); i++) {
            TbItemCat tbItemCat =  tbItemCatList.get(i);
            // 2.1、该类目是父节点
            if (tbItemCat.getIsParent()){
                CatNode catNode = new CatNode();
                catNode.setName(tbItemCat.getName());
                catNode.setItem(getCatList(tbItemCat.getId()));
                //把父节点装到集合中
                catNodeList.add(catNode);
                count = count++;
                if (count == 18){
                    break;
                }
            }else{
                //2.2、该节点不是父节点，直接把类目名称添加到集合中
                catNodeList.add(tbItemCat.getName());
            }
        }
        return catNodeList;
    }
}
