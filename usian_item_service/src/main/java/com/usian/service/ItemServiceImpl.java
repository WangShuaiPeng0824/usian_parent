package com.usian.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usian.mapper.*;
import com.usian.pojo.*;
import com.usian.utils.IDUtils;
import com.usian.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ItemServiceImpl implements ItemService{

    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private TbItemDescMapper tbItemDescMapper;

    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public TbItem selectItemInfo(Long itemId) {
        return tbItemMapper.selectByPrimaryKey(itemId);
    }

    @Override
    public PageResult selectTbItemAllByPage(Integer page, Long rows) {
        PageHelper.startPage(page,rows.intValue());
        TbItemExample tbItemExample = new TbItemExample();
        tbItemExample.setOrderByClause("updated DESC");
        TbItemExample.Criteria criteria = tbItemExample.createCriteria();
        criteria.andStatusEqualTo((byte)1);
        List<TbItem> tbItemList = tbItemMapper.selectByExample(tbItemExample);
        PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(tbItemList);
        PageResult pageResult = new PageResult();
        pageResult.setPageIndex(pageInfo.getPageNum());
        pageResult.setTotalPage(Long.valueOf(pageInfo.getPages()));
        pageResult.setResult(pageInfo.getList());
        return pageResult;
    }

    @Override
    public Integer insertTbItem(TbItem tbItem, String desc, String itemParams) {
        long itemId = IDUtils.genItemId();
        Date date = new Date();
        //1、保存商品信息
        tbItem.setId(itemId);
        tbItem.setStatus((byte)1);
        tbItem.setCreated(date);
        tbItem.setUpdated(date);
        tbItem.setPrice(tbItem.getPrice()*100);
        int tbItemNum = tbItemMapper.insertSelective(tbItem);
        //2、保存商品描述信息
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(date);
        tbItemDesc.setUpdated(date);
        int insertTbItemNum = tbItemDescMapper.insertSelective(tbItemDesc);
        //3、保存商品规格信息
        TbItemParamItem tbItemParamItem = new TbItemParamItem();
        tbItemParamItem.setItemId(itemId);
        tbItemParamItem.setParamData(itemParams);
        tbItemParamItem.setUpdated(date);
        tbItemParamItem.setCreated(date);
        int tbitemParamItemNum = tbItemParamItemMapper.insertSelective(tbItemParamItem);

        return tbItemNum+insertTbItemNum+tbitemParamItemNum;
    }

    @Override
    public Integer deleteItemById(Long itemId) {
        return tbItemMapper.deleteByPrimaryKey(itemId);
    }

    @Override
    public Map<String, Object> preUpdateItem(Long itemId) {
        HashMap<String, Object> map = new HashMap<>();
        //1、根据商品id查询商品
        TbItem item = tbItemMapper.selectByPrimaryKey(itemId);
        map.put("item",item);
        //2、根据商品id查询商品描述
        TbItemDesc itemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
        map.put("itemDesc",itemDesc.getItemDesc());
        //3、根据商品id查询商品类目
        TbItemCat itemCat = tbItemCatMapper.selectByPrimaryKey(item.getCid());
        map.put("itemCat",itemCat.getName());
        //4、根据商品id查询商品规格参数
        TbItemParamItemExample tbItemParamItemExample = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria criteria = tbItemParamItemExample.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        List<TbItemParamItem> list = tbItemParamItemMapper.selectByExampleWithBLOBs(tbItemParamItemExample);
        if (list!=null && list.size()>0){
            map.put("itemParamItem",list.get(0).getParamData());
        }
        return map;
    }

    @Override
    public Integer updateTbItem(TbItem tbItem, String desc, String itemParams) {
        Date date = new Date();
        //1、保存商品信息
        tbItem.setId(tbItem.getId());
        tbItem.setStatus((byte)1);
        tbItem.setCreated(date);
        tbItem.setUpdated(date);
        tbItem.setPrice(tbItem.getPrice()*100);
        int tbItemNum = tbItemMapper.updateByPrimaryKeySelective(tbItem);
        //2、保存商品描述信息
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(tbItem.getId());
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(date);
        tbItemDesc.setUpdated(date);
        int insertTbItemNum = tbItemDescMapper.updateByPrimaryKeySelective(tbItemDesc);
        //3、保存商品规格信息
        TbItemParamItem tbItemParamItem = new TbItemParamItem();
        tbItemParamItem.setItemId(tbItem.getId());
        tbItemParamItem.setParamData(itemParams);
        tbItemParamItem.setUpdated(date);
        tbItemParamItem.setCreated(date);
        int tbitemParamItemNum = tbItemParamItemMapper.updateByPrimaryKeySelective(tbItemParamItem);
        return tbItemNum+insertTbItemNum+tbitemParamItemNum;
    }
}
