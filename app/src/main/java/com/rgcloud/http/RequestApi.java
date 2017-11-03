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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by wangxuejiao on 2017/9/2.
 */

public class RequestApi {

    /**
     * 获取验证码
     */
    public static void getVerifyCode(VerifyCodeReqEntity verifyCodeReqEntity, ResponseCallBack responseCallBack) {
        Call<VerifyCodeResEntity> call = ServiceGenerator.createService(RGCloudServices.class, true).getVerifyCode(verifyCodeReqEntity);
        call.enqueue(responseCallBack);
    }

    /**
     * 注册
     */
    public static void register(RegisterReqEntity registerReqEntity, ResponseCallBack responseCallBack) {
        Call<TokenResEntity> call = ServiceGenerator.createService(RGCloudServices.class, true).register(registerReqEntity);
        call.enqueue(responseCallBack);
    }

    /**
     * 登录
     */
    public static void login(LoginReqEntity loginReqEntity, ResponseCallBack responseCallBack) {
        Call<TokenResEntity> call = ServiceGenerator.createService(RGCloudServices.class, true).login(loginReqEntity);
        call.enqueue(responseCallBack);
    }


    /**
     * 获取微信的openid
     */
    public static void getWXOpenId(String urlStr, Callback<WXOpenIdResEntity> callback) {
        Call<WXOpenIdResEntity> call = ServiceGenerator.createService(RGCloudServices.class).getWXOpenId(urlStr);
        call.enqueue(callback);
    }

    /**
     * 微信登录
     */
    public static void wxLogin(WXReqEntity wxReqEntity, ResponseCallBack responseCallBack) {
        Call<TokenResEntity> call = ServiceGenerator.createService(RGCloudServices.class, true).wxLogin(wxReqEntity);
        call.enqueue(responseCallBack);
    }

    /**
     * 忘记密码
     */
    public static void forgetPassword(ForgetPasswordReqEntity forgetPasswordReqEntity, ResponseCallBack responseCallBack) {
        Call<BaseResEntity> call = ServiceGenerator.createService(RGCloudServices.class, true).forgetPassword(forgetPasswordReqEntity);
        call.enqueue(responseCallBack);
    }

    /**
     * 修改密码
     */
    public static void updatePassword(UpdatePasswordReqEntity updatePasswordReqEntity, ResponseCallBack responseCallBack) {
        Call<BaseResEntity> call = ServiceGenerator.createService(RGCloudServices.class, true).updatePassword(updatePasswordReqEntity);
        call.enqueue(responseCallBack);
    }

    /**
     * 退出登录
     */
    public static void loginOut(BaseReqEntity baseReqEntity, ResponseCallBack responseCallBack) {
        Call<BaseResEntity> call = ServiceGenerator.createService(RGCloudServices.class, true).loginOut(baseReqEntity);
        call.enqueue(responseCallBack);
    }

    /**
     * 获取首页信息
     */
    public static void getHomeInfo(BaseReqEntity baseReqEntity, ResponseCallBack responseCallBack) {
        Call<HomeResEntity> call = ServiceGenerator.createService(RGCloudServices.class, true).getHomeInfo(baseReqEntity);
        call.enqueue(responseCallBack);
    }


    /**
     * 点单
     */
    public static void order(OrderReqEntity orderReqEntity, ResponseCallBack responseCallBack) {
        Call<BaseResEntity> call = ServiceGenerator.createService(RGCloudServices.class, true).order(orderReqEntity);
        call.enqueue(responseCallBack);
    }

    /**
     * 获取活动列表
     */

    public static void getActivity(ActivityReqEntity activityReqEntity, ResponseCallBack responseCallBack) {
        Call<ActivityResEntity> call = ServiceGenerator.createService(RGCloudServices.class, true).getActivity(activityReqEntity);
        call.enqueue(responseCallBack);
    }


    /**
     * 获取月活动日
     */
    public static void getActivityDays(ActivityDaysReqEntity activityDaysReqEntity, ResponseCallBack responseCallBack) {
        Call<ActivityDaysResEntity> call = ServiceGenerator.createService(RGCloudServices.class, true).getActivityDays(activityDaysReqEntity);
        call.enqueue(responseCallBack);
    }


    /**
     * 活动详情
     */
    public static void getActivityDetail(ActivityDetailReqEntity activityDetailReqEntity, ResponseCallBack responseCallBack) {
        Call<ActivityDetailResEntity> call = ServiceGenerator.createService(RGCloudServices.class, true).getActivityDetail(activityDetailReqEntity);
        call.enqueue(responseCallBack);
    }

    /**
     * 收藏
     */
    public static void collect(CollectReqEntity collectReqEntity, ResponseCallBack responseCallBack) {
        Call<BaseResEntity> call = ServiceGenerator.createService(RGCloudServices.class, true).collect(collectReqEntity);
        call.enqueue(responseCallBack);
    }

    /**
     * 取消收藏
     */
    public static void collectCancel(CollectCancelReqEntity collectCancelReqEntity, ResponseCallBack responseCallBack) {
        Call<BaseResEntity> call = ServiceGenerator.createService(RGCloudServices.class, true).collectCancel(collectCancelReqEntity);
        call.enqueue(responseCallBack);
    }

    /**
     * 发表评论
     */
    public static void postComment(PostCommentReqEntity postCommentReqEntity, ResponseCallBack responseCallBack) {
        Call<BaseResEntity> call = ServiceGenerator.createService(RGCloudServices.class, true).postComment(postCommentReqEntity);
        call.enqueue(responseCallBack);
    }

    /**
     * 获取门票
     */
    public static void getTicket(GetTicketReqEntity getTicketReqEntity, ResponseCallBack responseCallBack) {
        Call<BaseResEntity> call = ServiceGenerator.createService(RGCloudServices.class, true).getTicket(getTicketReqEntity);
        call.enqueue(responseCallBack);
    }

    /**
     * 获取空间场地
     */
    public static void getActivitySpace(SpaceReqEntity spaceReqEntity, ResponseCallBack responseCallBack) {
        Call<ActivitySpaceResEntity> call = ServiceGenerator.createService(RGCloudServices.class, true).getActivitySpace(spaceReqEntity);
        call.enqueue(responseCallBack);
    }

    /**
     * 获取个人信息
     */
    public static void getPersonalInfo(BaseReqEntity baseReqEntity, ResponseCallBack responseCallBack) {
        Call<PersonalInfoResEntity> call = ServiceGenerator.createService(RGCloudServices.class, true).getPersonalInfo(baseReqEntity);
        call.enqueue(responseCallBack);
    }

    /**
     * 获取积分记录
     */
    public static void getPointRecord(PointReqEntity pointReqEntity, ResponseCallBack responseCallBack) {
        Call<PointResEntity> call = ServiceGenerator.createService(RGCloudServices.class, true).getPointRecord(pointReqEntity);
        call.enqueue(responseCallBack);
    }

    /**
     * 获取卡券
     */
    public static void getCoupon(BaseReqEntity baseReqEntity, ResponseCallBack responseCallBack) {
        Call<CouponResEntity> call = ServiceGenerator.createService(RGCloudServices.class, true).getCoupon(baseReqEntity);
        call.enqueue(responseCallBack);
    }

    /**
     * 获取收藏
     */
    public static void getCollection(BaseReqEntity baseReqEntity, ResponseCallBack responseCallBack) {
        Call<CollectionResEntity> call = ServiceGenerator.createService(RGCloudServices.class, true).getCollection(baseReqEntity);
        call.enqueue(responseCallBack);
    }

    /**
     * 获取收藏
     */
    public static void getComment(BaseReqEntity baseReqEntity, ResponseCallBack responseCallBack) {
        Call<CommentResEntity> call = ServiceGenerator.createService(RGCloudServices.class, true).getComment(baseReqEntity);
        call.enqueue(responseCallBack);
    }

    /**
     * 绑定手机号
     */
    public static void bindPhone(BindPhoneReqEntity bindPhoneReqEntity, ResponseCallBack responseCallBack) {
        Call<BaseResEntity> call = ServiceGenerator.createService(RGCloudServices.class, true).bindPhone(bindPhoneReqEntity);
        call.enqueue(responseCallBack);
    }

    /**
     * 获取消息列表
     */
    public static void getMessage(MessageReqEntity messageReqEntity, ResponseCallBack responseCallBack) {
        Call<MessageResEntity> call = ServiceGenerator.createService(RGCloudServices.class, true).getMessage(messageReqEntity);
        call.enqueue(responseCallBack);
    }
}
