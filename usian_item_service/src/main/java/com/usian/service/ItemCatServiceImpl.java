package com.usian.service;

import com.usian.mapper.TbItemCatMapper;
import com.usian.mapper.TbItemMapper;
import com.usian.pojo.TbItemCat;
import com.usian.pojo.TbItemCatExample;
import com.usian.pojo.TbItemExample;
import com.usian.redis.RedisClient;
import com.usian.utils.CatNode;
import com.usian.utils.CatResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ItemCatServiceImpl implements ItemCatService{

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Autowired
    private RedisClient redisClient;

    @Value("${PROTAL_CATRESULT_KEY}")
    private String PROTAL_CATRESULT_KEY;

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
        //1、先查redis
        CatResult catResultRedis = (CatResult) redisClient.get(PROTAL_CATRESULT_KEY);
        if (catResultRedis!=null){
            //2、如果redis有，直接return
            return catResultRedis;
        }
        //因为一级菜单有子菜单，子菜单有子子菜单，所以要递归调用
        List<CatNode> catList = getCatList(0L);
        CatResult catResult = new CatResult();
        catResult.setData(catList);

        //3、如果redis没有，则查询数据库并把结果放到redis中
        redisClient.set(PROTAL_CATRESULT_KEY,catResult);
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
