package com.nowcoder.dao;

import com.nowcoder.domain.Comment;
import com.nowcoder.domain.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by lizeyang on 2019/12/27.
 */
@Mapper
public interface MessageDao {
    String TABLE_NAME = "message";
    String INSERT_FIELDS = " from_id,to_id,content,created_date,has_read,conversation_id";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, " ) values(#{fromId},#{toId},#{content},#{createdDate},#{hasRead},#{conversationId})"})
    int addMessage(Message message);

    @Select({"select ", SELECT_FIELDS, " ,count(id) as id from (select * from ", TABLE_NAME, " where from_id=#{userId} or to_id=#{userId} order by id desc) tt group by conversation_id order by id desc limit #{offset},#{limit}"})
    List<Message> getConversationList(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where conversation_id=#{conversationId} order by id limit #{offset},#{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId, @Param("offset") int offset, @Param("limit") int limit);

    @Select({"select count(id) ", " from ", TABLE_NAME, " where has_read=0 and to_id=#{userId} and conversation_id=#{conversationId}"})
    int getConbersationUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    @Select({"select count(id) ", " from ", TABLE_NAME, " where to_id=#{userId} and conversation_id=#{conversationId}"})
    int getConbersationTotalCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    @Update({"update ", TABLE_NAME, " set has_read = 1 where to_id=#{userId} and conversation_id = #{conversationId}"})
    void updateHasRead(@Param("userId") int userId, @Param("conversationId") String conversationId);
}
