package com.health2world.aio.app.health.sign;

import com.health2world.aio.bean.SignService;

import aio.health2world.brvah.entity.SectionEntity;

/**
 * Created by lishiyou on 2018/8/8 0008.
 */

public class SignServiceSection extends SectionEntity<SignService>{

    public SignServiceSection(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public SignServiceSection(SignService signService) {
        super(signService);
    }
}
