package com.code972.elasticsearch.plugins;

import com.code972.hebmorph.DictionaryLoader;
import com.code972.hebmorph.datastructures.DictHebMorph;
import com.code972.hebmorph.hspell.HSpellLoader;

import java.io.File;
import java.io.IOException;

/**
 * Created by Egozy on 03/12/2014.
 */
public class DictReceiver {
    private static String home = System.getProperty("user.home");
    private static String[] gZipFilePaths = {"plugins/analysis-hebrew/dictH.dict", "/var/lib/hebmorph/dictH.dict", home + "/hebmorph/dictH.dict"};
    private static String[] hspellFilePaths = {"plugins/analysis-hebrew/hspell-data-files/", "/var/lib/hspell-data-files/", home + "/hspell-data-files/"};
    private static String DEFAULT_HSPELL_PREFIX = HSpellLoader.PREFIX_NOH;
    private static DictHebMorph dict = null;

    public static DictHebMorph getDictionary() {
        if (dict == null) {
            dict = setDictionary();
        }
        return dict;
    }

    public static boolean setHebmorphDictionary(String dictheb_path) {
        if (dictheb_path != null) {
            File file = new File(dictheb_path);
            if (file.exists()) {
                try {
                    System.out.println("Successfully loaded from: " + dictheb_path);
                    dict = DictionaryLoader.loadDicAndPrefixesFromGzip(dictheb_path);
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static boolean setHspellDictionary(String hspell_folder) {
        if (hspell_folder != null) {
            File file = new File(hspell_folder);
            if (file.exists() && file.isDirectory()) {
                try {
                    System.out.println("Successfully loaded from: " + hspell_folder);
                    HSpellLoader loader = new HSpellLoader(file, true);
                    dict = loader.loadDictionaryFromHSpellData(file + "/" + DEFAULT_HSPELL_PREFIX);
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private static DictHebMorph setDictionary() {
        File file;
        for (String path : gZipFilePaths) {
            file = new File(path);
            if (file.exists()) {
                try {
                    System.out.println("Successfully loaded from: " + file.getAbsolutePath());
                    return DictionaryLoader.loadDicAndPrefixesFromGzip(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        for (String path : hspellFilePaths) {
            file = new File(path);
            if (file.exists() && file.isDirectory()) {
                try {
                    System.out.println("Successfully loaded from: " + file.getAbsolutePath());
                    HSpellLoader loader = new HSpellLoader(file, true);
                    return loader.loadDictionaryFromHSpellData(file + "/" + DEFAULT_HSPELL_PREFIX);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        throw new IllegalArgumentException("Could not load any dictionary. Aborting!");
    }
}