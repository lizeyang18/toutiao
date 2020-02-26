package com.nowcoder.dao;

import com.nowcoder.domain.LoginTicket;
import org.apache.ibatis.annotations.*;

import java.util.Date;


/**
 * Created by lizeyang on 2019/12/25.
 */

@Mapper
public interface TicketDao {
    String TABLE_NAME = "login_ticket";
    String INSERT_FIELDS = " user_id,ticket,expired,status";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") values (#{userId},#{ticket},#{expired},#{status})"})
    int addTicket(LoginTicket ticket);

    @Select({"select ", SELECT_FIELDS, "from ", TABLE_NAME, " where ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);

    @Update({"update ", TABLE_NAME, " set status=#{status} where ticket=#{ticket}"})
    void updateStatus(@Param("ticket") String ticket,@Param("status") int status);

 /*   @Delete({"delete from ",TABLE_NAME," where expired < #{date}"})
    void delTicket(Long date);*/
}
