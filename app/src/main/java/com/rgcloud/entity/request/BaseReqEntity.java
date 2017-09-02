package com.rgcloud.entity.request;

import com.rgcloud.util.PreferencesUtil;
import com.tencent.qcloud.xiaozhibo.TCApplication;

/**
 * Created by wangxuejiao on 2017/9/2.
 */

public class BaseReqEntity {

    protected String Token;
    public BaseReqEntity() {
        PreferencesUtil preferencesUtil = new PreferencesUtil(TCApplication.getApplication());
        if (preferencesUtil.getBoolean(PreferencesUtil.HAS_LOGIN)){
            Token = preferencesUtil.getString(PreferencesUtil.ACCESS_TOKEN);
        }else {
            Token = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJTZWNydEtleSI6ImF1NjdhZ2FjNWM0NTY1OWY5OGI5MmMiLCJVSWQiOjAsIlRpbWUiOjB9.S4r9ar901Y1fodfFMsSiAbV43dMWCWE1VHJJCfRwyoJMWXsVkfkGQBbj1dccx_3uNfmliVQdc0-CWCoxQZL2aw";
        }
    }
}
