package com.actionbarsherlock;

/**
 * Workaround for ABS .aar packaging bug.
 * See https://github.com/JakeWharton/ActionBarSherlock/issues/1001 for details.
 */
public class BuildConfig {

    public static final boolean DEBUG = com.smokynote.BuildConfig.DEBUG;
}
