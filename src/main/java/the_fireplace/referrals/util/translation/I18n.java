package the_fireplace.referrals.util.translation;

class I18n {
    private static final ReferralsLanguageMap localizedName = ReferralsLanguageMap.getInstance();
    private static final ReferralsLanguageMap fallbackTranslator = new ReferralsLanguageMap("en_us");

    static String translateToLocalFormatted(String key, Object... format) {
        return canTranslate(key) ? localizedName.translateKeyFormat(key, format) : fallbackTranslator.translateKeyFormat(key, format);
    }

    private static boolean canTranslate(String key) {
        return localizedName.isKeyTranslated(key);
    }
}