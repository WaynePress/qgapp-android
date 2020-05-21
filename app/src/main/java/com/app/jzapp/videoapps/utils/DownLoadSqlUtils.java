package com.app.jzapp.videoapps.utils;

import com.app.jzapp.videoapps.database.DownLoadBeanDao;
import com.app.jzapp.videoapps.bean.DownLoadBean;

import java.util.List;

import static com.app.jzapp.videoapps.MyApplication.sDownLoadBeanDao;

public class DownLoadSqlUtils {
    /**
     * 创建下载的具体信息
     */
    public void insertBean(DownLoadBean bean) {
        DownLoadBean downLoadBean = new DownLoadBean(null, bean.getUrl(), bean.getName(), bean.getFilePath(), bean.getIamgeUrl(), bean.getStatus());
        sDownLoadBeanDao.insert(downLoadBean);
    }


    /**
     * 得到下载具体信息
     */
    public List<DownLoadBean> getBean(String url) {
        List<DownLoadBean> list = sDownLoadBeanDao.queryBuilder().where(DownLoadBeanDao.Properties.Url.eq(url)).build().list();
        return list;
    }

    public List<DownLoadBean> getBeans() {
        //查询全部
        List<DownLoadBean> list = sDownLoadBeanDao.queryBuilder().list();
        return list;
    }

    /**
     * 更新数据库中的下载信息
     */
    public void updataBean(String urlstr, int status) {
        DownLoadBean bean = sDownLoadBeanDao.queryBuilder().where(DownLoadBeanDao.Properties.Url.eq(urlstr)).build().unique();
        bean.setStatus(status);
        sDownLoadBeanDao.update(bean);
    }

    /**
     * 删除数据库中的数据
     */
    public void delete(String url) {
        DownLoadBean bean = sDownLoadBeanDao.queryBuilder().where(DownLoadBeanDao.Properties.Url.eq(url)).build().unique();
        sDownLoadBeanDao.delete(bean);
    }

    public void deleteAll() {
        sDownLoadBeanDao.deleteAll();
    }


}
