package com.usian.service;

import com.usian.mapper.TbContentCategoryMapper;
import com.usian.pojo.TbContentCategory;
import com.usian.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ContentCategoryServiceImpl implements ContentCategoryService{

    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    @Override
    public List<TbContentCategory> selectContentCategoryByParentId(Long id) {
        TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = tbContentCategoryExample.createCriteria();
        criteria.andParentIdEqualTo(id);
        return tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
    }

    @Override
    public Integer insertContentCategory(TbContentCategory tbContentCategory) {
        Date date = new Date();
        tbContentCategory.setStatus(1);
        tbContentCategory.setSortOrder(1);
        tbContentCategory.setIsParent(false);
        tbContentCategory.setCreated(date);
        tbContentCategory.setUpdated(date);
        int insertSelective = tbContentCategoryMapper.insertSelective(tbContentCategory);
        TbContentCategory contentCategory = tbContentCategoryMapper.selectByPrimaryKey(tbContentCategory.getParentId());
        if (!contentCategory.getIsParent()){
            contentCategory.setIsParent(true);
            contentCategory.setUpdated(date);
            tbContentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
        }
        return insertSelective;
    }

    @Override
    public Integer deleteContentCategoryById(Long categoryId) {
        //1、查询当前节点是否为父节点
        TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(categoryId);
        if (tbContentCategory.getIsParent()==true){
            return 0;
        }
        //2、如果不是父节点执行删除
        tbContentCategoryMapper.deleteByPrimaryKey(categoryId);
        //3、判断该节点是否还是父节点
        TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = tbContentCategoryExample.createCriteria();
        criteria.andParentIdEqualTo(tbContentCategory.getParentId());
        //4、查看这个节点是否还有其他的子节点
        List<TbContentCategory> tbContentCategoryList = tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
        //5、如果查询到的是空或者长度小于0那么证明这个几点不是父节点
        if (tbContentCategoryList==null || tbContentCategoryList.size()==0){
            TbContentCategory contentCategory = new TbContentCategory();
            contentCategory.setId(tbContentCategory.getParentId());
            contentCategory.setUpdated(new Date());
            contentCategory.setIsParent(false);
            tbContentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
        }
        return 200;
    }

    @Override
    public Integer updateContentCategory(TbContentCategory tbContentCategory) {
        tbContentCategory.setUpdated(new Date());
        return tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);
    }
}
