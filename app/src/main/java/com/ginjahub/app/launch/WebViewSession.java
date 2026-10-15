package com.ginjahub.app.launch;

import android.content.Context;
import android.content.SharedPreferences;

/** Durable routing state for users who have entered the verification WebView. */
public final class WebViewSession {
    private final SharedPreferences preferences;

    public WebViewSession(Context context) {
        this(context, "jasper_state_t9b5");
    }

    WebViewSession(Context context, String preferenceName) {
        preferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
    }


    public boolean hasOpenedWebView() { return preferences.getBoolean("seen_n6k2", false); }
    public String initialEntryUrl() { return preferences.getString("origin_s4j8", null); }
    public String lastWebViewUrl() { return preferences.getString("page_c7v1", null); }
    public boolean lastUrlWorks() { return preferences.getBoolean("healthy_q3d9", false); }

    public void recordFirstRedirect(String entryUrl) {
        if (hasOpenedWebView()) return;
        preferences.edit().putBoolean("seen_n6k2", true)
                .putString("origin_s4j8", entryUrl).putBoolean("healthy_q3d9", false).commit();
    }

    public void recordWorkingPage(String url) {
        preferences.edit().putBoolean("seen_n6k2", true)
                .putString("page_c7v1", url).putBoolean("healthy_q3d9", true).apply();
    }

    public void markLastUrlBroken() { preferences.edit().putBoolean("healthy_q3d9", false).apply(); }

    public boolean wasTokenAccepted(String token) {
        return tokenHash(token).equals(preferences.getString("receipt_f5p6", ""));
    }

    public void recordAcceptedToken(String token) {
        preferences.edit().putString("receipt_f5p6", tokenHash(token)).commit();
    }

    private static String tokenHash(String token) {
        try {
            byte[] digest = java.security.MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder();
            for (byte value : digest) result.append(String.format(java.util.Locale.ROOT, "%02x", value & 255));
            return result.toString();
        } catch (java.security.NoSuchAlgorithmException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
