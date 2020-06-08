package com.usian.feign;

import com.usian.pojo.*;
import com.usian.utils.CatNode;
import com.usian.utils.CatResult;
import com.usian.utils.PageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "usian-item-service")
public interface ItemServiceFeign {

    /**
     * 查询商品详情
     * @param itemId
     * @return
     */
    @RequestMapping("/service/item/selectItemInfo")
    public TbItem selectItemInfo(@RequestParam("itemId") Long itemId);

    /**
     * 分页查询商品列表
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/service/item/selectTbItemAllByPage")
    PageResult selectTbItemAllByPage(@RequestParam Integer page,@RequestParam Long rows);

    /**
     * 根据id查询商品类目
     * @param id
     * @return
     */
    @RequestMapping("/service/itemCat/selectItemCategoryByParentId")
    List<TbItemCat> selectItemCategoryByParentId(@RequestParam Long id);

    /**
     * 查询商品规格参数模板
     * @param itemCatId
     * @return
     */
    @RequestMapping("/service/itemParam/selectItemParamByItemCatId/{itemCatId}")
    TbItemParam selectItemParamByItemCatId(@PathVariable Long itemCatId);

    /**
     * 添加商品信息
     * @param tbItem
     * @param desc
     * @param itemParams
     * @return
     */
    @RequestMapping("/service/item/insertTbItem")
    Integer insertTbItem(TbItem tbItem,@RequestParam String desc,@RequestParam String itemParams);

    /**
     * 规格模板查询
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/service/itemParam/selectItemParamAll")
    PageResult selectItemParamAll(@RequestParam Integer page,@RequestParam Integer rows);

    /**
     * 商品规格模板添加
     * @param itemCatId
     * @param paramData
     * @return
     */
    @RequestMapping("/service/itemParam/insertItemParam")
    Integer insertItemParam(@RequestParam Long itemCatId,@RequestParam String paramData);

    @RequestMapping("/service/itemCat/selectItemCategoryAll")
    CatResult selectItemCategoryAll();

    /**
     * 删除商品信息
     * @param itemId
     * @return
     */
    @RequestMapping("/service/item/deleteItemById")
    Integer deleteItemById(@RequestParam Long itemId);

    /**
     * 商品回显
     * @param itemId
     * @return
     */
    @RequestMapping("/service/item/preUpdateItem")
    Map<String, Object> preUpdateItem(@RequestParam Long itemId);

    @RequestMapping("/service/item/updateTbItem")
    Integer updateTbItem(TbItem tbItem,@RequestParam String desc,@RequestParam String itemParams);

    @RequestMapping("/service/itemParam/deleteItemParamById")
    Integer deleteItemParamById(@RequestParam Long id);

    @RequestMapping("/service/item/selectItemDescByItemId")
    TbItemDesc selectItemDescByItemId(@RequestParam Long itemId);

    @RequestMapping("/service/itemParam/selectTbItemParamItemByItemId")
    TbItemParamItem selectTbItemParamItemByItemId(@RequestParam Long itemId);
}
