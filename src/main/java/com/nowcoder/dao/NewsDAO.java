package com.nowcoder.dao;

import com.nowcoder.model.News;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by lizeyang on 2019/4/17.
 */
@Mapper
public interface NewsDAO {
    String TABLE_NAME = "news";
    String INSERT_FIELDS = "title,link,image,like_count,comment_count,created_date,user_id";
    String SELECT_FIELDS = "id,"+INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,")values(#{title},#{link},#{image},#{likeCount},#{commentCount},#{createdDate},#{userId})"})
        int addNews(News news);

    //用于处理分页
    List<News> selectByUserIdAndOffset(@Param("userId") int userId, @Param("offset") int offset,
                                       @Param("limit") int limit);
}
