package com.nowcoder.service;

import com.nowcoder.dao.NewsDao;
import com.nowcoder.domain.News;
import com.nowcoder.utils.ToutiaoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by lizeyang on 2019/12/24.
 */
@Service
public class NewsService {

    @Autowired
    private NewsDao newsDao;

    /**
     * 根据userId分页查询News
     *
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    public List<News> getLatestNews(int userId, int offset, int limit) {
        if (userId == 0) {
            return newsDao.selectAllAndOffset(offset, limit);
        }
        return newsDao.selectByUserIdAndOffset(userId, offset, limit);
    }

    /**
     * 上传图片（根据日期分文件夹）
     *
     * @param file
     * @return
     * @throws IOException
     */
    public String saveImage(MultipartFile file) throws IOException {
        int dotPos = file.getOriginalFilename().lastIndexOf(".");
        if (dotPos < 0) {
            return null;
        }

        /*获取文件后缀名*/
        String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
        if (!ToutiaoUtils.isFileAllowed(fileExt)) {
            return null;
        }

        /*重新命名图片名称*/
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;

        String storeDirectory = "D:/IDEA/Projects/toutiao/.idea/upload";
        String childDirectory = makeChildDirectory(new File(storeDirectory));
        Files.copy(file.getInputStream(), new File(ToutiaoUtils.IMAGE_DR + childDirectory + File.separator + fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
        return ToutiaoUtils.TOUTIAO_DOMAIN + "image?name=" + fileName;
    }

    private String makeChildDirectory(File storeDirectory) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dataDirectory = sdf.format(new Date());    //创建目录
        File file = new File(storeDirectory, dataDirectory);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dataDirectory;
    }


    /**
     * 实现分享资讯
     *
     * @param news
     * @return
     */
    public int addNews(News news) {
        newsDao.addNews(news);
        return news.getId();
    }

    /**
     * 根据newsId查看资讯详情
     *
     * @param newsId
     * @return
     */
    public News getNewsById(int newsId) {
        return newsDao.getNewsById(newsId);
    }

    /**
     * 更新评论数量
     *
     * @param newsId
     * @param count
     * @return
     */
    public int updateCommentCount(int newsId, int count) {
        return newsDao.updateCommentCount(newsId, count);
    }

    /**
     * 更新news里面的喜欢数
     *
     * @param newsId
     * @param count
     * @return
     */
    public int updateLikeCount(int newsId, int count) {
        return newsDao.updateLinkeCount(newsId, count);
    }
}
