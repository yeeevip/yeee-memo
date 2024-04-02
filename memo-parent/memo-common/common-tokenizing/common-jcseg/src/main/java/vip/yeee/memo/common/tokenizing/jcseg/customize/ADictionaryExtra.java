package vip.yeee.memo.common.tokenizing.jcseg.customize;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.tokenizer.TokenizerEngine;
import cn.hutool.extra.tokenizer.engine.jcseg.JcsegEngine;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.jcseg.IDictionary;
import org.lionsoul.jcseg.IWord;
import org.lionsoul.jcseg.SynonymsEntry;
import org.lionsoul.jcseg.dic.ADictionary;
import org.lionsoul.jcseg.dic.ILexicon;
import org.lionsoul.jcseg.segmenter.Entity;
import org.lionsoul.jcseg.segmenter.SegmenterConfig;
import org.lionsoul.jcseg.segmenter.Word;
import org.lionsoul.jcseg.util.StringUtil;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.CodeSource;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author https://www.yeee.vip
 * @since 2021/8/27 17:32
 */
@Slf4j
public class ADictionaryExtra {

    private final static List<String[]> synBuffer = Collections.synchronizedList(new ArrayList<>());
    private static final Map<String, SynonymsEntry> rootMap = new HashMap<>();
    private static final Object LOCK = new Object();
    private static ADictionary singletonDic = null;

    public static TokenizerEngine getTokenizerEngine() {
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
                    loadClassPath(singletonDic);
                    String[] lexPath = config.getLexiconPath();
                    if ( lexPath == null ) {

                    } else {
                        for ( String path : lexPath ) {
                            loadExtraDirectory(singletonDic, config, path);
                        }
                        if ( config.isAutoload() ) {
                            singletonDic.startAutoload();
                        }
                    }

                    /*
                     * added at 2017/06/10
                     * check and reset synonyms net of the current Dictionary
                     */
                    resetSynonymsNet(singletonDic);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
//        engine = new JcsegEngine(ISegment.NLP.factory.create(config, singletonDic));
        return new JcsegEngine(new CusNLPSeg(config, singletonDic));
    }

    private static void loadExtraDirectory(ADictionary dictionary, SegmenterConfig config, String lexDir) throws IOException {
        final File path = new File(lexDir);
        if ( ! path.exists() ) {
            throw new IOException("Lexicon directory ["+lexDir+"] is not exists.");
        }

        log.info("[COMMON-JCSEG]---------加载用户配置词库----{}----", lexDir);

        /*
         * load all the lexicon file under the lexicon path
         *     that start with "lex-" and end with ".lex".
         */
        final File[] files = path.listFiles(new FilenameFilter(){
            @Override
            public boolean accept(File dir, String name) {
                return (name.startsWith("lex-") && name.endsWith(".lex"));
            }
        });

        if (files != null) {
            for ( File file : files ) {
                loadWords(config, dictionary, FileUtil.getInputStream(file), synBuffer);
            }
        }
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

    /**
     * load words from a InputStream
     *
     * @param   config
     * @param   dic
     * @param   is
     * @param   buffer
     */
    public static void loadWords(
            SegmenterConfig config, ADictionary dic, InputStream is, List<String[]> buffer )
            throws NumberFormatException, IOException
    {
        boolean isFirstLine = true;
        int t = -1;
        String line = null, gEntity = null;

        try (BufferedReader buffReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            while ( (line = buffReader.readLine()) != null ) {
                line = line.trim();
                if ( "".equals(line) ) continue;
                if ( line.charAt(0) == '#' && line.length() > 1 ) { //skip the comments
                    continue;
                }

                //the first line for the lexicon file.
                if (isFirstLine) {
                    t = ADictionary.getIndex(line);
                    isFirstLine = false;
                    if ( t >= 0 ) {
                        continue;
                    }
                }

                /*
                 * @Note: added at 2016/11/14
                 * dictionary directive compile and apply
                 */
                if ( line.charAt(0) == ':' && line.length() > 1 ) {
                    String[] directive = line.substring(1).toLowerCase().split("\\s+");
                    if ( directive[0].equals("entity") ) {     //@since 2.0.1
                        if ( directive.length > 1 ) {
                            String args = directive[1].trim();
                            gEntity = "null".equals(args) ? null : Entity.get(args);
                        }
                    }

                    continue;
                }

                IWord tword = null;
                String[] wd = null;
                switch ( t ) {
                    case ILexicon.CN_SNAME:
                    case ILexicon.CN_LNAME:
                    case ILexicon.CN_DNAME_1:
                    case ILexicon.CN_DNAME_2:
                        if ( line.length() == 1 ) {
                            dic.add(t, line, IWord.T_CJK_WORD);
                        }
                        break;
                    case ILexicon.NUMBER_UNIT:
                        wd = line.split("\\s*/\\s*");
                        IWord uw = dic.add(t, wd[0], IWord.T_CJK_WORD);
                        if ( wd.length == 1 ) {
                            dic.add(ILexicon.CJK_WORD, uw);
                        } else if ( wd.length == 2 ) {
                            String entity = "null".equals(wd[1]) ? null : Entity.get(wd[1]);
                            uw.addEntity(entity);
                            dic.add(ILexicon.CJK_CHAR, uw);
                        }

                        break;
                    case ILexicon.CJK_UNIT:
                        /*
                         * for the entity recognition
                         * we may need the unit to help to
                         * define the numeric entity in front of it
                         * @date 2016/11/12
                         */
                        wd = line.split("\\s*/\\s*");
                        IWord w = dic.add(t, wd[0], IWord.T_CJK_WORD);
                        if ( wd.length == 1 ) {
                            dic.add(ILexicon.CJK_WORD, w);
                        } else if ( wd.length == 2 ) {
                            String entity = "null".equals(wd[1]) ? null : Entity.get(wd[1]);
                            w.addEntity(entity);
                            dic.add(ILexicon.CJK_WORD, w).addEntity(entity);;
                        } else if ( wd.length > 3 ) {
                            String entity = "null".equals(wd[3]) ? null : Entity.get(wd[3]);
                            w.addEntity(entity);

                            /*
                             * @Note: added at 2018/03/15
                             * check and set the word parameter override the original one
                             * if the current one with parameter set up
                             */
                            if ( config.LOAD_PARAMETER && wd.length > 4 && ! wd[4].equals("null") ) {
                                w.setParameter(wd[4]);
                            }

                            dic.add(ILexicon.CJK_WORD, w).addEntity(entity);;
                            tword = w;
                        } else {
                            dic.add(ILexicon.CJK_WORD, w);
                            tword = w;
                        }

                        break;
                    case ILexicon.CN_LNAME_ADORN:
                        dic.add(t, line, IWord.T_CJK_WORD);
                        break;
                    case ILexicon.STOP_WORD:
                        /// char fChar = line.charAt(0);
                        /// if ( fChar <= 127 || (fChar > 127
                        ///         && line.length() <= config.MAX_LENGTH) ) {
                        ///     dic.add(ILexicon.STOP_WORD, line, IWord.T_CJK_WORD);
                        /// }
                        dic.add(ILexicon.STOP_WORD, line, IWord.T_CJK_WORD);
                        break;
                    case ILexicon.DOMAIN_SUFFIX:
                        wd = line.split("\\s*/\\s*");
                        dic.add(t, wd[0], IWord.T_BASIC_LATIN);
                        //@Note access the explanation through wd[1]
                        break;
                    case ILexicon.CJK_SYN:
                        wd = line.split("\\s*,\\s*");
                        if ( wd.length > 1 && buffer != null ) {
                            buffer.add(wd);
                        }
                        wd = null;
                        break;
                    case ILexicon.CJK_WORD:
                    case ILexicon.CJK_CHAR:
                        wd = line.split("\\s*/\\s*", 5);
                        if ( wd.length < 4 ) {    //format check
                            wd = new String[] {wd[0], "null", "null", "null"};
                        }

                        if ( t == ILexicon.CJK_CHAR ) {    //single word degree check
                            if ( ! StringUtil.isDigit(wd[4]) ) {
                                System.out.println("Word: \"" + wd[0] +
                                        "\" format error(single word degree should be an integer). -ignored");
                                continue;
                            }
                        }

                        //length limit(CJK_WORD only)
                        int latinIndex = StringUtil.latinIndexOf(wd[0]);

                        //added at 2017/06/11
                        //here we clear the length filter for we may change the config.MAX_LENGTH
                        // at token analysis run time
                        ///if ( latinIndex == -1 && wd[0].length() > config.MAX_LENGTH ) {
                        ///    continue;
                        ///}

                        tword = dic.get(ILexicon.CJK_WORD, wd[0]);
                        if ( tword == null ) {
                            if ( t == ILexicon.CJK_CHAR ) {
                                tword = dic.add(ILexicon.CJK_WORD, wd[0], Integer.parseInt(wd[4]), IWord.T_CJK_WORD);
                            } else {
                                tword = dic.add(t, wd[0], IWord.T_CJK_WORD);
                            }
                        } else if (t == ILexicon.CJK_CHAR) {
                            // @Note: added at 2022/12/18
                            // reset the word frequency for the CJK_CHAR word
                            tword.setFrequency(Integer.parseInt(wd[4]));
                        }

                        /*
                         * @Note: added at 2016/11/14
                         * update the entity string for CJK and English words only
                         */
                        if ( config.LOAD_CJK_ENTITY && t != ILexicon.CJK_CHAR ) {
                            if ( wd.length > 3 ) {
                                if ( "unset".equals(wd[3]) ) {
                                    tword.setEntity(null);
                                } else if ( "extend".equals(wd[3]) ) {
                                    tword.addEntity(gEntity);
                                } else if ( "null".equals(wd[3]) ) {
                                    tword.addEntity(gEntity);
                                } else {
                                    tword.addEntity(Entity.get(wd[3]));
                                }
                            } else if ( gEntity != null ) {
                                tword.addEntity(gEntity);
                            }
                        }

                        /*
                         * @Note: added at 2017/10/02
                         * check and set the word parameter override the original one
                         * if the current one with parameter set up
                         */
                        if ( config.LOAD_PARAMETER && t != ILexicon.CJK_CHAR ) {
                            if ( wd.length > 4 && ! wd[4].equals("null") ) {
                                tword.setParameter(wd[4]);
                            }
                        }

                        /*
                         * check and build the MIX_ASSIST_WORD dictionary
                         * @Note added at 2016/11/22
                         */
                        if ( latinIndex >= 0 ) {
                            if ( latinIndex > 0 ) {
                                resetPrefixLength(config, dic, latinIndex);
                                dic.add(ILexicon.MIX_ASSIST_WORD, wd[0].substring(0, latinIndex), IWord.T_CJK_WORD);
                            }

                            int cjkIndex = StringUtil.CJKIndexOf(wd[0], latinIndex + 1);
                            if ( cjkIndex > -1 ) {
                                resetSuffixLength(config, dic, wd[0].length() - cjkIndex);
                                dic.add(ILexicon.MIX_ASSIST_WORD, wd[0].substring(cjkIndex), IWord.T_CJK_WORD);
                            }

                            if ( latinIndex > 0 && cjkIndex > -1 ) {
                                dic.add(ILexicon.MIX_ASSIST_WORD, wd[0].substring(0, cjkIndex), IWord.T_BASIC_LATIN);
                            }
                        }

                        break;
                }

                /*
                 * check and append the attributes of tword
                 * like the pinyin and the part of speech
                 */
                if ( tword != null ) {
                    //set the Pinyin of the word.
                    if ( config.LOAD_CJK_PINYIN && ! "null".equals(wd[2]) ) {
                        tword.setPinyin(wd[2]);
                    }

                    /// update the synonym of the word.
                    /// @Deprecated at 2017/06/10
                    /// String[] arr = tword.getSyn();
                    /// if ( config.LOAD_CJK_SYN && ! "null".equals(wd[3]) ) {
                    ///     String[] syns = wd[3].split(",");
                    ///     for ( int j = 0; j < syns.length; j++ ) {
                    ///         syns[j] = syns[j].trim();
                    ///          Here:
                    ///          * filter the synonym that its length
                    ///          * is greater than config.MAX_LENGTH

                    ///         if ( t == ILexicon.CJK_WORD
                    ///                 && syns[j].length() > config.MAX_LENGTH ) {
                    ///             continue;
                    ///         }

                    ///          Here:
                    ///          * check the synonym is not exists, make sure
                    ///          * the same synonym won't appended. (dictionary reload)
                    ///          *
                    ///          * @date 2013-09-02

                    ///         boolean add = true;
                    ///         if ( arr != null ) {
                    ///             for ( int i = 0; i < arr.length; i++ )  {
                    ///                 if ( syns[j].equals(arr[i]) ) {
                    ///                     add = false;
                    ///                     break;
                    ///                 }
                    ///             }
                    ///         }

                    ///         if ( add ) {
                    ///             tword.addSyn(syns[j]);
                    ///         }
                    ///     }
                    /// }

                    //update the word's part of speech
                    String[] arr = tword.getPartSpeech();
                    if ( config.LOAD_CJK_POS && ! "null".equals(wd[1]) ) {
                        String[] pos = wd[1].split(",");

                        for ( int j = 0; j < pos.length; j++ ) {
                            pos[j] = pos[j].trim();

                            /* Here:
                             * check the part of speech is not exists, make sure
                             * the same part of speech won't be appended.(dictionary reload)
                             *
                             * @date 2013-09-02
                             */
                            boolean add = true;
                            if ( arr != null ) {
                                for (String s : arr) {
                                    if (pos[j].equals(s)) {
                                        add = false;
                                        break;
                                    }
                                }
                            }

                            if ( add ) {
                                tword.addPartSpeech(pos[j].trim());
                            }
                        }
                    }
                }

            }
        } // end try
    }

    /**
     * check and reset the value of {@link ADictionary#mixPrefixLength}
     *
     * @param config
     * @param dic
     * @param mixLength
     */
    public static void resetPrefixLength(SegmenterConfig config, ADictionary dic, int mixLength)
    {
        if ( mixLength <= config.MAX_LENGTH
                && mixLength > dic.mixPrefixLength ) {
            dic.mixPrefixLength = mixLength;
        }
    }

    /**
     * check and reset the value of the {@link ADictionary#mixSuffixLength}
     *
     * @param config
     * @param dic
     * @param mixLength
     */
    public static void resetSuffixLength(SegmenterConfig config, ADictionary dic, int mixLength)
    {
        if ( mixLength <= config.MAX_LENGTH
                && mixLength > dic.mixSuffixLength ) {
            dic.mixSuffixLength = mixLength;
        }
    }

    public static void resetSynonymsNet(ADictionary dic)
    {
        singletonDic.resetSynonymsNet();
        synchronized (synBuffer) {
            if ( synBuffer.size() == 0 ) {
                return;
            }

            for (String[] synLine : synBuffer) {
                ///if ( synLine[0].length() > config.MAX_LENGTH ) {
                ///    continue;
                ///}

                //check if the baseWord is exists or not
                IWord baseWord = dic.get(ILexicon.CJK_WORD, synLine[0]);
                if (baseWord == null) {
                    continue;
                }

                /*
                 * first get the synonym entry from the root map
                 * create a new one and map it with the root word if it not exists
                 */
                SynonymsEntry synEntry = rootMap.get(baseWord.getValue());
                if (synEntry == null) {
                    synEntry = new SynonymsEntry(baseWord);
                    rootMap.put(baseWord.getValue(), synEntry);
                    synEntry.add(baseWord); //add the base word first
                }

                for (int i = 1; i < synLine.length; i++) {
                    String[] parts = synLine[i].split("\\s*/\\s*");
                    ///if ( parts[0].length() > config.MAX_LENGTH ) {
                    ///    continue;
                    ///}

                    /* Ignore the synonyms define that is already existed */
                    for (final IWord w : synEntry.getList()) {
                        if (w.getValue().equals(parts[0])) {
                            continue;
                        }
                    }

                    //check if the word is exists or not
                    //  or create a new one
                    IWord synWord = dic.get(ILexicon.CJK_WORD, parts[0]);
                    if (synWord == null) {
                        synWord = new Word(parts[0], IWord.T_CJK_WORD);
                        dic.add(ILexicon.CJK_WORD, synWord);
                    }

                    //check and extends the part of speech from the baseWord
                    if (synWord.getPartSpeech() == null) {
                        synWord.setPartSpeech(baseWord.getPartSpeech());
                    }

                    //check and extends the entity from the baseWord
                    if (synWord.getEntity() == null) {
                        synWord.setEntity(baseWord.getEntity());
                    }

                    //check and set the pinyin
                    if (parts.length > 1) {
                        synWord.setPinyin(parts[1]);
                    }

                    synEntry.add(synWord);
                }
            }

            synBuffer.clear();
        }
    }
}
