package github.tornaco.xposedmoduletest.xposed;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.android.internal.os.BinderInternal;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

import org.newstand.logger.Logger;
import org.newstand.logger.Settings;

import github.tornaco.apigen.BuildHostInfo;
import github.tornaco.apigen.GithubCommitSha;
import github.tornaco.xposedmoduletest.license.XActivation;
import github.tornaco.xposedmoduletest.provider.XSettings;
import github.tornaco.xposedmoduletest.service.VerboseLogService;
import github.tornaco.xposedmoduletest.xposed.app.XAppGuardManager;
import github.tornaco.xposedmoduletest.xposed.app.XAshmanManager;

/**
 * Created by guohao4 on 2017/10/17.
 * Email: Tornaco@163.com
 */
@GithubCommitSha
@BuildHostInfo
public class XApp extends Application implements Runnable {

    @SuppressLint("StaticFieldLeak")
    private static XApp xApp;

    public static XApp getApp() {
        return xApp;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();

        xApp = this;
        Logger.config(Settings.builder().tag("XAppGuardApp")
                .logLevel(XSettings.isDevMode(this)
                        ? Logger.LogLevel.DEBUG : Logger.LogLevel.WARN)
                .build());
        XAppGuardManager.init();
        XAshmanManager.init();
        XActivation.reloadAsync(this);
        BinderInternal.addGcWatcher(this);
        EmojiManager.install(new GoogleEmojiProvider());
        startService(new Intent(this, VerboseLogService.class));
    }

    public void hideAppIcon(boolean enable) {
//        PackageManager pm = getPackageManager();
//        ComponentName enabled = enable ? new ComponentName(this,
//                GuardAppNavActivityNoLauncher.class) : new ComponentName(this, GuardAppNavActivity.class);
//        ComponentName disabled = enable ? new ComponentName(this,
//                GuardAppNavActivity.class) : new ComponentName(this, GuardAppNavActivityNoLauncher.class);
//        Logger.d(enabled);
//        Logger.d(disabled);
//        pm.setComponentEnabledSetting(enabled,
//                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
//        pm.setComponentEnabledSetting(disabled,
//                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    public void run() {
        Logger.v("onGC");
    }
}
