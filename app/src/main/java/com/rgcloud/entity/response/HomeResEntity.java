package com.rgcloud.entity.response;

import com.rgcloud.entity.BaseResEntity;

import java.util.List;

/**
 * Created by wangxuejiao on 2017/9/5.
 */

public class HomeResEntity extends BaseResEntity {


    /**
     * Opera : 1
     * VoluntaryService : sample string 2
     * HotSearchKeyWords : ["sample string 1","sample string 2"]
     * TopChangeImageUrl : ["sample string 1","sample string 2"]
     * IconList : [{"TypeId":1,"Name":"sample string 2","ImageUrl":"sample string 3"},{"TypeId":1,"Name":"sample string 2","ImageUrl":"sample string 3"}]
     * RecommendList : [{"ActiveId":1,"SpaceId":2,"ActiveName":"sample string 3","SpaceName":"sample string 4","Address":"sample string 5","ActiveImage":"sample string 6","SpaceImage":"sample string 7","IsNeedTicket":8},{"ActiveId":1,"SpaceId":2,"ActiveName":"sample string 3","SpaceName":"sample string 4","Address":"sample string 5","ActiveImage":"sample string 6","SpaceImage":"sample string 7","IsNeedTicket":8}]
     */

    public String VoluntaryService;
    public List<String> HotSearchKeyWords;
    public List<String> TopChangeImageUrl;


    /**
     * TypeId : 1
     * Name : sample string 2
     * ImageUrl : sample string 3
     */

    public List<IconListBean> IconList;
    /**
     * ActiveId : 1
     * SpaceId : 2
     * ActiveName : sample string 3
     * SpaceName : sample string 4
     * Address : sample string 5
     * ActiveImage : sample string 6
     * SpaceImage : sample string 7
     * IsNeedTicket : 8
     */

    public List<ActivityResBean> RecommendList;
    
    public static class IconListBean {
        public int TypeId;
        public String Name;
        public String ImageUrl;
    }

    public int MainImportantActiveId;
    public  int Opera;
    public int VoluntaryServiceId;
    public String CulturalWalletUrl;
    public int ArtisticAppreciation;
    public int CulturalTreasures;
    public String Android_StartSiagramImg;
}
