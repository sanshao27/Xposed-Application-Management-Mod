package github.tornaco.xposedmoduletest.ui.activity;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;

import github.tornaco.android.common.Collections;
import github.tornaco.android.common.Consumer;
import github.tornaco.xposedmoduletest.R;
import github.tornaco.xposedmoduletest.ui.BaseActivity;
import github.tornaco.xposedmoduletest.ui.SettingsActivity;
import github.tornaco.xposedmoduletest.ui.VerifyDisplayerActivity;
import github.tornaco.xposedmoduletest.x.XApp;
import github.tornaco.xposedmoduletest.x.app.XAppGuardManager;
import github.tornaco.xposedmoduletest.x.submodules.SubModule;

/**
 * Created by guohao4 on 2017/11/2.
 * Email: Tornaco@163.com
 */

public class AdvancedSettings extends SettingsActivity {
    @Override
    protected Fragment onCreateSettingsFragment() {
        return new SecureSettingsFragment();
    }

    public static class SecureSettingsFragment extends SettingsFragment {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.advanced);

            if (!XAppGuardManager.defaultInstance().isServiceAvailable()) {
                getPreferenceScreen().setEnabled(false);
                return;
            }


            findPreference("key_test_noter")
                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            VerifyDisplayerActivity.startAsTest(getActivity());
                            return true;
                        }
                    });

            SwitchPreference hideAppIcon = (SwitchPreference) findPreference("key_hide_app_icon");
            hideAppIcon.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean enabled = (boolean) newValue;
                    XApp.getApp().hideAppIcon(enabled);
                    ProgressDialog p = new ProgressDialog(getActivity());
                    p.setMessage("&*^$%$(-)$##@%%%%^-^");
                    p.setIndeterminate(true);
                    p.setCancelable(false);
                    p.show();
                    BaseActivity b = (BaseActivity) getActivity();
                    b.getUIThreadHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().finishAffinity();
                        }
                    }, 8 * 1000);
                    return true;
                }
            });

            final StringBuilder moduleStatus = new StringBuilder();
            Collections.consumeRemaining(XAppGuardManager.defaultInstance().getSubModules(),
                    new Consumer<String>() {
                        @Override
                        public void accept(String s) {
                            moduleStatus.append(s)
                                    .append(": ")
                                    .append(SubModule.SubModuleStatus.valueOf(XAppGuardManager.defaultInstance().getSubModuleStatus(s)))
                                    .append("\n");
                        }
                    });
            findPreference("key_dump_module")
                    .setSummary(moduleStatus.toString());

            findPreference("key_crash_module")
                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            XAppGuardManager.defaultInstance().mockCrash();
                            return true;
                        }
                    });

            SwitchPreference debugPref = (SwitchPreference) findPreference("dev_mode_enabled");
            debugPref.setChecked(XAppGuardManager.defaultInstance().isDebug());
            debugPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean v = (boolean) newValue;
                    XAppGuardManager.defaultInstance().setDebug(v);
                    return true;
                }
            });
        }
    }
}
