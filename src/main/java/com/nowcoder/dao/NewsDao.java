package com.nowcoder.dao;

import com.nowcoder.domain.News;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by lizeyang on 2019/12/24.
 */
@Mapper
public interface NewsDao {
    String TABLE_NAME = "news";
    String INSERT_FIELDS = " title,link,image,like_count,comment_count,created_date,user_id";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, " ) values(#{title},#{link},#{image},#{likeCount},#{commentCount},#{createdDate},#{userId})"})
    int addNews(News news);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where user_id=#{userId} ORDER BY id DESC LIMIT #{offset},#{limit}"})
    List<News> selectByUserIdAndOffset(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " ORDER BY id DESC LIMIT #{offset},#{limit}"})
    List<News> selectAllAndOffset(@Param("offset") int offset, @Param("limit") int limit);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{newsId}"})
    News getNewsById(int newsId);

    @Update({"update ", TABLE_NAME, " set comment_count=#{count} where id=#{newsId}"})
    int updateCommentCount(@Param("newsId") int newsId, @Param("count") int count);

    @Update({"update ", TABLE_NAME, " set like_count=#{count} where id=#{newsId}"})
    int updateLinkeCount(@Param("newsId") int newsId, @Param("count") int count);
}
