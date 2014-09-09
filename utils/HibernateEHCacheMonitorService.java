package com.perf.utils;

import java.util.*;

import com.retek.platform.service.*;


public interface HibernateEHCacheMonitorService extends Service{
    public  String getCacheType(String key) ;
    public  List getCachingInformation(String key) ;
    public  String getServerName(String key) ;
    public  String getServerTime(String key) ;
    public  String toString(String key) ;
}
