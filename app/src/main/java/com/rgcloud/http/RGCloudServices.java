package com.rgcloud.http;

import com.rgcloud.entity.BaseResEntity;
import com.rgcloud.entity.request.ActivityDaysReqEntity;
import com.rgcloud.entity.request.ActivityDetailReqEntity;
import com.rgcloud.entity.request.ActivityReqEntity;
import com.rgcloud.entity.request.BaseReqEntity;
import com.rgcloud.entity.request.BindPhoneReqEntity;
import com.rgcloud.entity.request.CollectCancelReqEntity;
import com.rgcloud.entity.request.CollectReqEntity;
import com.rgcloud.entity.request.ForgetPasswordReqEntity;
import com.rgcloud.entity.request.GetTicketReqEntity;
import com.rgcloud.entity.request.LoginReqEntity;
import com.rgcloud.entity.request.MessageReqEntity;
import com.rgcloud.entity.request.OrderReqEntity;
import com.rgcloud.entity.request.PointReqEntity;
import com.rgcloud.entity.request.PostCommentReqEntity;
import com.rgcloud.entity.request.RegisterReqEntity;
import com.rgcloud.entity.request.SpaceReqEntity;
import com.rgcloud.entity.request.UpdatePasswordReqEntity;
import com.rgcloud.entity.request.VerifyCodeReqEntity;
import com.rgcloud.entity.request.WXReqEntity;
import com.rgcloud.entity.response.ActivityDaysResEntity;
import com.rgcloud.entity.response.ActivityDetailResEntity;
import com.rgcloud.entity.response.ActivityResEntity;
import com.rgcloud.entity.response.ActivitySpaceResEntity;
import com.rgcloud.entity.response.CollectionResEntity;
import com.rgcloud.entity.response.CommentResEntity;
import com.rgcloud.entity.response.CouponResEntity;
import com.rgcloud.entity.response.HomeResEntity;
import com.rgcloud.entity.response.MessageResEntity;
import com.rgcloud.entity.response.PersonalInfoResEntity;
import com.rgcloud.entity.response.PointResEntity;
import com.rgcloud.entity.response.TokenResEntity;
import com.rgcloud.entity.response.VerifyCodeResEntity;
import com.rgcloud.entity.response.WXOpenIdResEntity;
import com.umeng.socialize.sina.message.BaseResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by wangxuejiao on 2017/9/2.
 */

public interface RGCloudServices {

    /**
     * 获取验证码
     */
    @POST("commonApi/sendMessage")
    Call<VerifyCodeResEntity> getVerifyCode(@Body VerifyCodeReqEntity verifyCodeReqEntity);

    /**
     * 注册
     */
    @POST("memberApi/register")
    Call<TokenResEntity> register(@Body RegisterReqEntity registerReqEntity);

    /**
     * 登录
     */
    @POST("memberApi/login")
    Call<TokenResEntity> login(@Body LoginReqEntity loginReqEntity);

    /**
     * 获取微信的openid
     */
    @GET()
    Call<WXOpenIdResEntity> getWXOpenId(@Url String urlStr);


    /**
     * 微信登录
     */
    @POST("memberApi/WeChatLogin")
    Call<TokenResEntity> wxLogin(@Body WXReqEntity wxReqEntity);

    /**
     * 忘记密码
     */
    @POST("memberApi/resetLoginPassword")
    Call<BaseResEntity> forgetPassword(@Body ForgetPasswordReqEntity forgetPasswordReqEntity);

    /**
     * 修改密码
     */
    @POST("memberApi/updateLoginPassword")
    Call<BaseResEntity> updatePassword(@Body UpdatePasswordReqEntity updatePasswordReqEntity);

    /**
     * 修改密码
     */
    @POST("memberApi/logOut")
    Call<BaseResEntity> loginOut(@Body BaseReqEntity baseReqEntity);

    /**
     * 获取主页信息
     */
    @POST("mainapi/recieveMainInfo")
    Call<HomeResEntity> getHomeInfo(@Body BaseReqEntity baseReqEntity);

    /**
     * 点单
     */
    @POST("mainapi/memberDemand")
    Call<BaseResEntity> order(@Body OrderReqEntity orderReqEntity);


    /**
     * 获取活动列表
     */
    @POST("activeManageApi/revcieveActiveList")
    Call<ActivityResEntity> getActivity(@Body ActivityReqEntity activityReqEntity);

    /**
     * 获取月活动日
     */
    @POST("activeManageApi/showMonthActive")
    Call<ActivityDaysResEntity> getActivityDays(@Body ActivityDaysReqEntity activityDaysReqEntity);

    /**
     * 活动详情
     */
    @POST("activeManageApi/recieveActiveDetail")
    Call<ActivityDetailResEntity> getActivityDetail(@Body ActivityDetailReqEntity activityDetailReqEntity);

    /**
     * 收藏
     */
    @POST("activeManageApi/memberCollect")
    Call<BaseResEntity> collect(@Body CollectReqEntity collectReqEntity);

    /**
     * 取消收藏
     */
    @POST("activeManageApi/memberCancelCollect")
    Call<BaseResEntity> collectCancel(@Body CollectCancelReqEntity collectCancelReqEntity);

    /**
     * 发表评论
     */
    @POST("activeManageApi/activeComment")
    Call<BaseResEntity> postComment(@Body PostCommentReqEntity postCommentReqEntity);

    /**
     * 获取门票
     */
    @POST("activeManageApi/exchangeTicket")
    Call<BaseResEntity> getTicket(@Body GetTicketReqEntity getTicketReqEntity);


    /**
     * 获取空间场地
     */
    @POST("activeManageApi/recieveAcitveSpaces")
    Call<ActivitySpaceResEntity> getActivitySpace(@Body SpaceReqEntity spaceReqEntity);

    /**
     * 获取个人信息
     */
    @POST("memberApi/memberInfo")
    Call<PersonalInfoResEntity> getPersonalInfo(@Body BaseReqEntity baseReqEntity);


    /**
     * 获取积分记录
     */
    @POST("memberPointApi/pointRecords")
    Call<PointResEntity> getPointRecord(@Body PointReqEntity pointReqEntity);

    /**
     * 获取卡券
     */
    @POST("memberApi/findMemberTicket")
    Call<CouponResEntity> getCoupon(@Body BaseReqEntity baseReqEntity);

    /**
     * 获取收藏
     */
    @POST("memberApi/findMemberCollectOutput")
    Call<CollectionResEntity> getCollection(@Body BaseReqEntity baseReqEntity);

    /**
     * 获取评论
     */
    @POST("memberApi/findMemberComment")
    Call<CommentResEntity> getComment(@Body BaseReqEntity baseReqEntity);

    /**
     * 绑定手机号
     */
    @POST("memberApi/memberBindPhone")
    Call<BaseResEntity> bindPhone(@Body BindPhoneReqEntity bindPhoneReqEntity);


    /*
    * 获取消息列表
    */
    @POST("memberApi/findPushLog")
    Call<MessageResEntity> getMessage(@Body MessageReqEntity messageReqEntity);


    /*
    * 获取启动图
    */
    @Streaming
    @GET
    Call<ResponseBody> getSpalshBg(@Url String fileUrl);
}
