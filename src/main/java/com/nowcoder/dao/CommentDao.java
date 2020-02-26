package com.nowcoder.dao;

import com.nowcoder.domain.Comment;
import com.nowcoder.domain.News;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by lizeyang on 2019/12/27.
 */
@Mapper
public interface CommentDao {
    String TABLE_NAME = "comment";
    String INSERT_FIELDS = " content,user_id,entity_id,entity_type,created_date,status";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, " ) values(#{content},#{userId},#{entityId},#{entityType},#{createdDate},#{status})"})
    int addComment(Comment comment);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where entity_id=#{entityId} and entity_type=#{entityType} order by id desc"})
    List<Comment> selectByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Select({"select count(id) from",TABLE_NAME," where entity_id=#{entityId} and entity_type = #{entityType}"})
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);
}
