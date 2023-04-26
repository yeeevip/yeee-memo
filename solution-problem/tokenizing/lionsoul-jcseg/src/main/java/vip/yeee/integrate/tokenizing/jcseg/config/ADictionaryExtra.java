package vip.yeee.integrate.tokenizing.jcseg.config;

import cn.hutool.extra.tokenizer.TokenizerEngine;
import cn.hutool.extra.tokenizer.engine.jcseg.JcsegEngine;
import org.lionsoul.jcseg.IDictionary;
import org.lionsoul.jcseg.ISegment;
import org.lionsoul.jcseg.dic.ADictionary;
import org.lionsoul.jcseg.segmenter.SegmenterConfig;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author yeee
 * @since 2021/8/27 17:32
 */
public class ADictionaryExtra {

    private static final Object LOCK = new Object();
    private static ADictionary singletonDic = null;
    public static TokenizerEngine engine = null;

    static  {
        SegmenterConfig config = new SegmenterConfig(true);
        synchronized (LOCK) {
            if ( singletonDic == null ) {
                config.setCnNumToArabic(false);
                config.setAppendCJKSyn(false);
                singletonDic = IDictionary.HASHMAP.factory.create(config, true);

                try {
                    /*
                     * @Note: updated at 2016/07/07
                     *
                     * check and load all the lexicons with more than one path
                     * if specified none lexicon paths (config.getLexiconPath() is null)
                     * And we directly load the default lexicons that in the class path
                     */
                    String[] lexpath = config.getLexiconPath();
                    if ( lexpath == null ) {
                        loadClassPath(singletonDic);
                    } else {
                        for ( String lpath : lexpath ) singletonDic.loadDirectory(lpath);
                        if ( config.isAutoload() ) singletonDic.startAutoload();
                    }

                    /*
                     * added at 2017/06/10
                     * check and reset synonyms net of the current Dictionary
                     */
                    singletonDic.resetSynonymsNet();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        engine = new JcsegEngine(ISegment.NLP.factory.create(config, singletonDic));
    }

    public static void loadClassPath(ADictionary singletonDic) throws IOException
    {
        Class<?> dClass    = singletonDic.getClass();
        CodeSource codeSrc = singletonDic.getClass().getProtectionDomain().getCodeSource();
        if ( codeSrc == null ) {
            return;
        }

        String codePath = codeSrc.getLocation().getPath(), temp;
        if ((temp = codePath.toLowerCase()).endsWith(".jar") || temp.endsWith(".jar!/")) {
            ZipInputStream zip = new ZipInputStream(codeSrc.getLocation().openStream());
            while ( true ) {
                ZipEntry e = zip.getNextEntry();
                if ( e == null ) {
                    break;
                }

                String fileName = e.getName();
                if ( fileName.endsWith(".lex")
                        && fileName.startsWith("lexicon/lex-") ) {
                    singletonDic.load(dClass.getResourceAsStream("/"+fileName));
                }
            }
        } else {
            //now, the classpath is an IDE directory
            //  like eclipse ./bin or maven ./target/classes/
            File lexiconDir = new File(URLDecoder.decode(codeSrc.getLocation().getFile(),"utf-8"));
            singletonDic.loadDirectory(lexiconDir.getPath() + File.separator + "lexicon");
        }
    }

}
