package com.demo.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.auth.mapper.PhotoDataMapper;
import com.demo.auth.pojo.entity.PhotoData;
import com.demo.auth.service.PhotoDataService;
import org.springframework.stereotype.Service;

@Service
public class PhotoDataServiceImpl extends ServiceImpl<PhotoDataMapper, PhotoData> implements PhotoDataService {


}
