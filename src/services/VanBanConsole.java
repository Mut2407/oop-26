/*
 * Decompiled with CFR 0.152.
 */
package services;

import java.text.Normalizer;

public final class VanBanConsole {
    private VanBanConsole() {
    }

    public static String khongDau(String string) {
        return Normalizer.normalize(string, Normalizer.Form.NFD).replaceAll("\\p{M}+", "").replace('\u0111', 'd').replace('\u0110', 'D');
    }
}

