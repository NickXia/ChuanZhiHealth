package com.zizhou.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zizhou.constant.MessageConstant;
import com.zizhou.dao.MemberDao;
import com.zizhou.dao.OrderDao;
import com.zizhou.dao.OrderSettingDao;
import com.zizhou.entity.Result;
import com.zizhou.pojo.Member;
import com.zizhou.pojo.Order;
import com.zizhou.pojo.OrderSetting;
import com.zizhou.service.OrderService;
import com.zizhou.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 体检预约接口实现类
 * @Author: NickXia
 * @date: 2020/8/6 13:18
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    //提交预约信息dao
    @Autowired
    private OrderDao orderDao;

    //预约设置dao
    @Autowired
    private OrderSettingDao orderSettingDao;

    //会员Dao
    @Autowired
    private MemberDao memberDao;

    /**
     * 提交体检预约信息
     * @param map
     * @return
     */
    @Override
    public Result submitOrder(Map map) throws Exception {
        //获取体检人信息
        //姓名
        String name = (String) map.get("name");
        //性别
        String sex = (String) map.get("sex");
        //手机号
        String telephone = (String) map.get("telephone");
        //身份证号
        String idCard = (String) map.get("idCard");
        //套餐id
        String setmealId = (String) map.get("setmealId");
        //预约方式
        String orderType = (String) map.get("orderType");

       //1. 判断当前的日期是否可以预约(根据orderDate查询t_ordersetting表,能查询出来就可以预约,查询不出来的就不可以预约)
        String orderDate = (String) map.get("orderDate");
        //将String转为Date
        Date newOrderDate = DateUtils.parseString2Date(orderDate);

        OrderSetting orderSetting = orderSettingDao.findByOrderDate(newOrderDate);

        //System.out.println(orderSetting.toString());
        logger.debug(orderSetting.toString());

       //2. 判断当前日期是否已经预约满(判断reservations是否 == number)
        if (orderSetting == null){
            return new Result(false,MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //已预约人数
        int reservations = orderSetting.getReservations();
        int number = orderSetting.getNumber();//可预约人数
        if(reservations >= number){
            return new Result(false,MessageConstant.ORDER_FULL);
        }

        //3. 判断是否是会员(根据手机号码查询t_member)
        Member dbMember = memberDao.findByTelephone(telephone);
        // - 如果是会员(能够查询出来),防止重复预约(根据member_id、setmeal_id、orderDate查询t_order)
        if(dbMember != null){
            //防止重复预约(根据member_id,orderDate,setmeal_id查询t_order)
            Order queryOrder = new Order(dbMember.getId(),newOrderDate,Integer.parseInt(setmealId));
            List<Order> orderList = orderDao.findByCondition(queryOrder);
            if (orderList != null || orderList.size() > 0){
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }
        // - 如果不是会员(不能查询出来),自动注册为会员(直接向t_member插入一条记录)
        if (dbMember == null){
            dbMember = new Member();
            dbMember.setName(name);
            dbMember.setSex(sex);
            dbMember.setIdCard(idCard);
            dbMember.setPhoneNumber(telephone);
            dbMember.setRegTime(new Date());
            memberDao.add(dbMember);
        }

       //4. 进行预约
        //        - 向t_order表插入一条记录
        //会员id,预约表中需要的记录信息
        Integer dbMemberId = dbMember.getId();
        Order order = new Order();
        order.setMemberId(dbMemberId);
        order.setOrderDate(newOrderDate);
        order.setOrderType(orderType);
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        order.setSetmealId(Integer.parseInt(setmealId));
        orderDao.add(order);
        logger.debug("预约成功");

       // - t_ordersetting表里面预约的人数+1
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);
       //5. 预约成功后，跳转成功页面,以及发送短信通知给用户
        //返回预约成功页面需要的预约成功表中的主键id
        return new Result(true, MessageConstant.ORDER_SUCCESS,order);
    }

    /**
     * 展示预约成功后相关信息
     * @param id
     * @return
     */
    @Override
    public Map findById4Detail(Integer id) {
        return orderDao.findById4Detail(id);
    }
}
