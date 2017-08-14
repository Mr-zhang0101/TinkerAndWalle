package com.zhang.tinkerandwalle;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.multidex.MultiDex;

import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.zhang.tinkerandwalle.tinker.MyLogImp;
import com.zhang.tinkerandwalle.tinker.TinkerManager;

/**
 * Project: TinkerAndWalle
 * Author:  张佳林
 * Version:  1.0
 * Date:    2017/8/11
 * Modify:  //TODO
 * Description: //TODO
 * Copyright notice:
 */

@SuppressWarnings("unused")
@DefaultLifeCycle(  application = "com.zhang.tinkerandwalle.MyApplication",
        flags = ShareConstants.TINKER_ENABLE_ALL,
        loadVerifyFlag = false)
//类名不能和注解中的application名一样
public class MyApp extends DefaultApplicationLike {

    public MyApp(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initConfig();
    }

    private void initConfig() {

    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);

        // 其原理是分包架构，所以在加载初要加载其余的分包
        MultiDex.install(base);

        // Tinker管理类，保存当前对象
        TinkerManager.setTinkerApplicationLike(this);
        // 崩溃保护
        TinkerManager.initFastCrashProtect();
        // 是否重试
        TinkerManager.setUpgradeRetryEnable(true);

        //Log 实现，打印加载补丁的信息
        TinkerInstaller.setLogIml(new MyLogImp());

        // 运行Tinker ，通过Tinker添加一些基本配置
        TinkerManager.installTinker(this);
        Tinker tinker = Tinker.with(getApplication());
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        // 生命周期，默认配置
        getApplication().registerActivityLifecycleCallbacks(callback);
    }
}
