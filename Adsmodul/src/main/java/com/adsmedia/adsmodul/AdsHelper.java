package com.adsmedia.adsmodul;


import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adsmedia.mastermodul.MasterAdsHelper;
import com.yandex.mobile.ads.banner.BannerAdEventListener;
import com.yandex.mobile.ads.banner.BannerAdSize;
import com.yandex.mobile.ads.banner.BannerAdView;
import com.yandex.mobile.ads.common.AdError;
import com.yandex.mobile.ads.common.AdRequest;
import com.yandex.mobile.ads.common.AdRequestConfiguration;
import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.common.AdSize;
import com.yandex.mobile.ads.common.ImpressionData;
import com.yandex.mobile.ads.common.MobileAds;
import com.yandex.mobile.ads.interstitial.InterstitialAd;
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener;
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener;
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class AdsHelper {
    public static boolean openads = false;
    public static boolean debugMode;
    public static boolean directData = false;

    public static void gdpr(Activity activity, Boolean childDirected, String keypos) {
    }

    public static void initializeAds(Activity activity, int pos) {
    }

    public static void initializeAds(Activity activity, String pos, String gameId, boolean test) {
        try {
            MobileAds.initialize(activity, () -> {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        MasterAdsHelper.initializeAds(activity, pos);
    }

    public static void initializeAds(Activity activity, String pos, String gameId) {
        try {
            MobileAds.initialize(activity, () -> {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        MasterAdsHelper.initializeAds(activity, pos);
    }

    public static void debugMode(Boolean debug) {
        debugMode = debug;
        MasterAdsHelper.debugMode(debug);
    }

    public static BannerAdView bannerAd;

    public static void showBanner(Activity activity, RelativeLayout layout, String yandexId) {
        bannerAd = new BannerAdView(activity);
        directData = true;
        bannerAd.setAdSize(getAdSize(activity));
        bannerAd.setAdUnitId(yandexId);
        layout.addView(bannerAd);
        bannerAd.setBannerAdEventListener(new BannerAdEventListener() {
            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdFailedToLoad(@NonNull final AdRequestError adRequestError) {
                MasterAdsHelper.showBanner(activity, layout);
            }

            @Override
            public void onAdClicked() {
            }

            @Override
            public void onLeftApplication() {

            }

            @Override
            public void onReturnedToApplication() {
            }

            @Override
            public void onImpression(@Nullable ImpressionData impressionData) {
            }
        });
        final AdRequest adRequest = new AdRequest.Builder()
                .build();
        bannerAd.loadAd(adRequest);


    }
    private static BannerAdSize getAdSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return BannerAdSize.stickySize(activity, adWidth);
    }

    @Nullable
    public static InterstitialAd mInterstitialAd = null;
    @Nullable
    public static InterstitialAdLoader mInterstitialAdLoader = null;

    public static void loadInterstitial(Activity activity, String yandexId) {
        directData = true;
        try {
            mInterstitialAdLoader = new InterstitialAdLoader(activity);
            mInterstitialAdLoader.setAdLoadListener(new InterstitialAdLoadListener() {
                @Override
                public void onAdLoaded(@NonNull final InterstitialAd interstitialAd) {
                    mInterstitialAd = interstitialAd;
                    // The ad was loaded successfully. You can now show the ad.
                }

                @Override
                public void onAdFailedToLoad(@NonNull final AdRequestError adRequestError) {
                    // Ad failed to load with AdRequestError.
                    // Attempting to load a new ad from the onAdFailedToLoad() method is strongly discouraged.
                }
            });
            if (mInterstitialAdLoader != null) {
                final AdRequestConfiguration adRequestConfiguration =
                        new AdRequestConfiguration.Builder(yandexId).build();
                mInterstitialAdLoader.loadAd(adRequestConfiguration);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        MasterAdsHelper.loadInterstitial(activity);
    }

    public static int count = 0;

    public static void showInterstitial(Activity activity, String yandexId, int interval) {
        if (count >= interval) {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(activity);
            } else {
                MasterAdsHelper.showInterstitial(activity);
            }
            count = 0;
            loadInterstitial(activity, yandexId);
        } else {
            count++;
        }
    }

    public static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            //Logger.logStackTrace(TAG,e);
        }
        return "";
    }
}
