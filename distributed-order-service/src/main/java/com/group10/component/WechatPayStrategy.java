package com.group10.component;

import lombok.extern.slf4j.Slf4j;
import com.group10.vo.PayInfoVO;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class WechatPayStrategy implements PayStrategy {

    @Override
    public String unifiedorder(PayInfoVO payInfoVO) {


        return null;
    }

    @Override
    public String refund(PayInfoVO payInfoVO) {
        return null;
    }

    @Override
    public String queryPaySuccess(PayInfoVO payInfoVO) {
        return null;
    }
}
