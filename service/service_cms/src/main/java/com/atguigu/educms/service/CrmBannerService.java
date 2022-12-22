package com.atguigu.educms.service;

import com.atguigu.educms.entity.BannerQuery;
import com.atguigu.educms.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.cache.annotation.CacheEvict;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-11-14
 */
public interface CrmBannerService extends IService<CrmBanner> {

    List<CrmBanner> selectAllBanner();

    void pageQuery(Page<CrmBanner> bannerPage, BannerQuery bannerQuery);

    CrmBanner getBannerById(String id);

    @CacheEvict(value = "banner", allEntries=true)
    void saveBanner(CrmBanner banner);

    //CacheEvict用于更新或删除，allEntries属性清楚缓存
    @CacheEvict(value = "banner", allEntries=true)
    void updateBannerById(CrmBanner banner);

    //CacheEvict用于更新或删除，allEntries属性清楚缓存
    @CacheEvict(value = "banner", allEntries=true)
    void removeBannerById(String id);
}
