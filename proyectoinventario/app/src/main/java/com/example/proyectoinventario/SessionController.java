package com.example.proyectoinventario;

import java.net.CookieManager;

public class SessionController {

   private static CookieManager cookieManager;

    public static synchronized CookieManager getCookieManager() {
        return cookieManager;
    }

    public static synchronized void setCookieManager(CookieManager cookieManager) {
        SessionController.cookieManager = cookieManager;
    }
}
