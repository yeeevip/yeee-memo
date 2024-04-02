package vip.yeee.memo.common.tokenizing.jcseg.customize;

import org.lionsoul.jcseg.IWord;
import org.lionsoul.jcseg.dic.ADictionary;
import org.lionsoul.jcseg.dic.ILexicon;
import org.lionsoul.jcseg.segmenter.Entity;
import org.lionsoul.jcseg.segmenter.NLPSeg;
import org.lionsoul.jcseg.segmenter.SegmenterConfig;
import org.lionsoul.jcseg.segmenter.Word;
import org.lionsoul.jcseg.util.ByteCharCounter;
import org.lionsoul.jcseg.util.EntityFormat;
import org.lionsoul.jcseg.util.IStringBuffer;
import org.lionsoul.jcseg.util.StringUtil;

import java.io.IOException;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/5/8 10:30
 */
public class CusNLPSeg extends NLPSeg {
    public CusNLPSeg(SegmenterConfig config, ADictionary dic) {
        super(config, dic);
    }

    @Override
    protected IWord nextLatinWord(int c, int pos) throws IOException {
        isb.clear();
        if (false) {
            if ( c > 65280 ) c -= 65248;
            if ( c >= 65 && c <= 90 ) c += 32;
        }
        isb.append((char)c);

        boolean _check = false, _wspace = false;
        ByteCharCounter counter = new ByteCharCounter();
        int ch, _ctype = 0, tcount = 1;             //number of different char type.
        int _TYPE  = StringUtil.getEnCharType(c);   //current char type.

        while ( (ch = readNext()) != -1 ) {
            // Covert the full-width char to half-width char.
            if ( ch > 65280 ) ch -= 65248;
            _ctype = StringUtil.getEnCharType(ch);
            if ( _ctype == StringUtil.EN_WHITESPACE ) {
                _wspace = true;
                break;
            }

            if ( _ctype == StringUtil.EN_PUNCTUATION ) {
                if ( ! StringUtil.isENKeepPunctuaton((char)ch) ) {
                    pushBack(ch);
                    break;
                }
                counter.increase((char)ch);
            }

            //Not EN_KNOW, and it could be letter, numeric.
            if ( _ctype == StringUtil.EN_UNKNOW ) {
                pushBack(ch);
                if ( StringUtil.isCJKChar( ch ) ) {
                    _check = true;
                }

                break;
            }

            //covert the lower case letter to upper case.
            if ( ch >= 65 && ch <= 90 ) ch += 32;
            if ( ch > 0 ) {
                isb.append((char)ch);
            }

            /* Char type counter.
             * condition to start the secondary segmentation.
             * @reader: we could do better.
             *
             * @added 2013-12-16
             */
            if ( _ctype != _TYPE ) {
                tcount++;
                _TYPE = _ctype;
            }

            if ( isb.length() > config.MAX_LATIN_LENGTH ) {
                break;
            }
        }

        /*
         * @added at 2016/11/24
         * clear the dot punctuation behind the string buffer
         * and recount the tcount as needed
         */
        int oLen = isb.length();
        for ( int i = oLen - 1; i > 0
                && StringUtil.isNoTailingPunctuation(isb.charAt(i)); i-- ) {
            pushBack(isb.charAt(i));
            isb.deleteCharAt(i);
            _check = false;
        }

        if ( oLen > isb.length()
                && ! StringUtil.isEnPunctuation(isb.last()) ) {
            tcount--;
        }



        IWord wd   = null;
        String str = isb.toString();
        String date = null;

        /*
         * special entity word check like email, URL, ip address
         */
        int colonNum = counter.get(':');
        int pointNum = counter.get('.');
        if ( counter.get('@') == 1 && pointNum > 0
                && EntityFormat.isMailAddress(str) ) {
            wd = new Word(str, IWord.T_BASIC_LATIN, Entity.E_EMAIL_A);
            wd.setPartSpeechForNull(IWord.EN_POSPEECH);
            return wd;
        } else if ( tcount == 1 && StringUtil.isEnNumeric(isb.first())
                && EntityFormat.isMobileNumber(str) ) {
            wd = new Word(str, IWord.T_BASIC_LATIN, Entity.E_MOBILE_A);
            wd.setPartSpeechForNull(IWord.NUMERIC_POSPEECH);
            return wd;
        } else if ( tcount == 7 && pointNum == 3
                && StringUtil.isEnNumeric(isb.first())
                && EntityFormat.isIpAddress(str) ) {
            wd = new Word(str, IWord.T_BASIC_LATIN, Entity.E_IP_A);
            wd.setPartSpeechForNull(IWord.EN_POSPEECH);
            return wd;
        } else if ( pointNum > 0 && colonNum == 1
                && EntityFormat.isUrlAddress(str, dic) ) {
            wd = new Word(str, IWord.T_BASIC_LATIN, Entity.E_URL_A);
            wd.setPartSpeechForNull(IWord.EN_POSPEECH);
            return wd;
        } else if ( pointNum == 2
                && (date = EntityFormat.isDate(str, '.')) != null ) {
            wd = new Word(date, IWord.T_BASIC_LATIN, Entity.E_DATETIME_YMD_A);
            wd.setPartSpeechForNull(IWord.TIME_POSPEECH);
            return wd;
        } else if ( counter.get('-') >= 1
                && (date = EntityFormat.isDate(str, '-')) != null ) {
            String[] entity = counter.get('-') == 1
                    ? Entity.E_DATETIME_YM_A : Entity.E_DATETIME_YMD_A;
            wd = new Word(date, IWord.T_BASIC_LATIN, entity);
            wd.setPartSpeechForNull(IWord.TIME_POSPEECH);
            return wd;
        } else if ( counter.get('/') >= 1
                && (date = EntityFormat.isDate(str, '/')) != null ) {
            String[] entity = counter.get('/') == 1
                    ? Entity.E_DATETIME_YM_A : Entity.E_DATETIME_YMD_A;
            wd = new Word(date, IWord.T_BASIC_LATIN, entity);
            wd.setPartSpeechForNull(IWord.TIME_POSPEECH);
            return wd;
        } else if ( (tcount == 3 || tcount == 5)
                && colonNum >= 1 && EntityFormat.isTime(str) ) {
            String[] entity = colonNum == 1
                    ? Entity.E_DATETIME_HI_A : Entity.E_DATETIME_HIS_A;
            wd = new Word(str, IWord.T_BASIC_LATIN, entity);
            wd.setPartSpeechForNull(IWord.TIME_POSPEECH);
            return wd;
        }

        /*
         * check the end condition.
         * and the check if the token loop was break by whitespace
         * cause there is no need to continue all the following work if it is.
         */
        if ( ch == -1 || _wspace ) {
            boolean isPercentage = false;
            for ( int i = isb.length() - 1; i > 0; i-- ) {
                if ( isb.charAt(i) == '%' ) {
                    if ( i > 0 && StringUtil.isEnNumeric(isb.charAt(i-1)) ) {
                        isPercentage = true;
                        break;
                    }
                } else if ( ! StringUtil.isEnPunctuation(isb.charAt(i)) ) {
                    break;
                }

                /*
                 * try to find a English and punctuation mixed word.
                 * this will clear all the punctuation until a mixed word is found.
                 * like "i love c++.", c++ will be found from token "c++.".
                 */
                if ( dic.match(ILexicon.CJK_WORD, str) ) {
                    wd = dic.get(ILexicon.CJK_WORD, str).clone();
                    break;
                }

                pushBack(isb.charAt(i));
                isb.deleteCharAt(i);
                str = isb.toString();
            }

            if ( wd == null ) {
                if ( isPercentage ) {
                    wd = new Word(str, IWord.T_BASIC_LATIN,
                            Entity.E_NUMERIC_PERCENTAGE_A);
                    wd.setPartSpeechForNull(IWord.NUMERIC_POSPEECH);
                } else if ( tcount == 1 && StringUtil.isDigit(str) ) {
                    wd = new Word(str, IWord.T_BASIC_LATIN,
                            Entity.E_NUMERIC_ARABIC_A);
                    wd.setPartSpeechForNull(IWord.NUMERIC_POSPEECH);
                } else if ( tcount == 3 && StringUtil.isDecimal(str) ) {
                    wd = new Word(str, IWord.T_BASIC_LATIN,
                            Entity.E_NUMERIC_DECIMAL_A);
                    wd.setPartSpeechForNull(IWord.NUMERIC_POSPEECH);
                } else if ( dic.match(ILexicon.CJK_WORD, str) ) {
                    wd = dic.get(ILexicon.CJK_WORD, str).clone();
                } else {
                    wd = wordNewOrClone(ILexicon.CJK_WORD, str, IWord.T_BASIC_LATIN);
                    wd.setPartSpeechForNull(IWord.EN_POSPEECH);
                }
            }

            if ( wd.getPartSpeech() == null ) {
                wd.setPartSpeechForNull(IWord.EN_POSPEECH);
            }

            return wd;
        }

        /*
         * the loop was broken by an unknown char and it is not a CJK char
         * 1, check if the end char is a special single unit char like '℉,℃' and so on ..
         * 2, or do it as the end stream way like (ch == -1 or _wspace == true)
         */
        if ( _check == false ) {
            /*
             * we check the units here, so we can recognize
             * many other units that is not Chinese like '℉,℃' and so on ...
             */
            boolean isDigit = StringUtil.isDigit(str);
            if ( isDigit || StringUtil.isDecimal(str) ) {
                ch = readNext();
                String unit = ((char)ch)+"";
                if ( dic.match(ILexicon.CJK_UNIT, unit) ) {
                    wd = getNumericUnitComposedWord(
                            Integer.parseInt(str), dic.get(ILexicon.CJK_UNIT, unit));
                } else {
                    String[] entity = isDigit
                            ? Entity.E_NUMERIC_ARABIC_A
                            : Entity.E_NUMERIC_DECIMAL_A;
                    wd = new Word(str, IWord.T_BASIC_LATIN, entity);
                    wd.setPartSpeechForNull(IWord.NUMERIC_POSPEECH);
                    pushBack(ch);
                }
            }

            if ( wd == null ) {
                if ( dic.match(ILexicon.CJK_WORD, str) ) {
                    wd = dic.get(ILexicon.CJK_WORD, str).clone();
                } else {
                    wd = new Word(str, IWord.T_BASIC_LATIN);
                }
            }

            if ( wd.getPartSpeech() == null ) {
                wd.setPartSpeechForNull(IWord.EN_POSPEECH);
            }

            return wd;
        }

        /*
         * check and recognize the percentage
         */
        int length = isb.length();
        if ( length > 1 && isb.charAt(length-1) == '%'
                && StringUtil.isEnNumeric(isb.charAt(length-2)) ) {
            wd = new Word(str, IWord.T_BASIC_LATIN,
                    Entity.E_NUMERIC_PERCENTAGE_A);
            wd.setPartSpeechForNull(IWord.NUMERIC_POSPEECH);
            return wd;
        }

        /*
         * check and get English and Chinese mixed word like 'B超, x射线'
         *
         * Attention:
         * make sure that (ch = readNext()) is after j < Config.MIX_CN_LENGTH.
         * or it cause the miss of the next char.
         *
         * @reader: (2013-09-25)
         * we do not check the type of the char read next.
         * so, words started with English and its length except the start English part
         * less than config.MIX_CN_LENGTH in the EC dictionary could be recognized.
         *
         * @Note added at 2017/08/05
         * Add the ibuffer.length checking logic to follow the limitation
         * of the maximum length of the current token
         */
        IStringBuffer ibuffer = new IStringBuffer(str);
        String tstr = null;
        int mc = 0, j = 0;        //the number of char that read from the stream.
        iaList.clear();
        for ( ; j < dic.mixSuffixLength
                && ibuffer.length() < config.MAX_LENGTH
                && (ch = readNext()) != -1; j++ ) {
            /*
             * Attention:
             * it is a accident that Jcseg works fine for
             * we break the loop directly when we meet a whitespace.
             * 1, if a EC word is found, unit check process will be ignore.
             * 2, if matches no EC word, certainly return of readNext()
             *   will make sure the units check process works fine.
             */
            if ( StringUtil.isWhitespace(ch) ) {
                pushBack(ch);
                break;
            }

            ibuffer.append((char)ch);
            iaList.add(ch);
            tstr = ibuffer.toString();
            if ( dic.match(ILexicon.CJK_WORD, tstr) ) {
                wd  = dic.get(ILexicon.CJK_WORD, tstr);
                mc = j + 1;
            }
        }

        ibuffer.clear();
        ibuffer = null;                 //Let gc do it's work.
        for ( int i = j - 1; i >= mc; i-- ) {
            pushBack(iaList.get(i));    //push back the read chars.
        }

        if ( wd != null ) {
            wd = wd.clone();
            if ( wd.getPartSpeech() == null ) {
                wd.setPartSpeechForNull(IWord.MIX_POSPEECH);
            }

            return wd;
        }

        /*
         * check the unit for the digit or the decimal Latin
         */
        boolean isDigit = StringUtil.isDigit(str);
        if ( isDigit || StringUtil.isDecimal(str) ) {
            iaList.clear();
            IWord unitWord = null;
            IStringBuffer sb = new IStringBuffer();
            for ( j = 0; j < config.MAX_UNIT_LENGTH
                    && (ch = readNext()) != -1; j++ ) {
                if ( StringUtil.isWhitespace(ch) ) {
                    pushBack(ch);
                    break;
                }

                sb.append((char)ch);
                iaList.add(ch);
                tstr = sb.toString();
                if ( dic.match(ILexicon.CJK_UNIT, tstr) ) {
                    unitWord = dic.get(ILexicon.CJK_UNIT, tstr);
                    mc = j + 1;
                }
            }

            if ( unitWord == null ) {
                String[] entity = isDigit
                        ? Entity.E_NUMERIC_ARABIC_A
                        : Entity.E_NUMERIC_DECIMAL_A;
                wd = new Word(str, IWord.T_BASIC_LATIN, entity);
                wd.setPartSpeechForNull(IWord.NUMERIC_POSPEECH);
            } else {
                wd = getNumericUnitComposedWord(Integer.parseInt(str), unitWord);
            }

            for ( int i = j - 1; i >= mc; i-- ) {
                pushBack(iaList.get(i));
            }
        }

        /*
         * simply return the combination of English char, Arabic
         * numeric, English punctuation if matches no single units or EC word.
         */
        if ( wd == null ) {
            if ( dic.match(ILexicon.CJK_WORD, str) ) {
                wd = dic.get(ILexicon.CJK_WORD, str).clone();
            } else {
                wd = new Word(str, IWord.T_BASIC_LATIN);
            }

            if ( wd.getPartSpeech() == null ) {
                wd.setPartSpeechForNull(IWord.EN_POSPEECH);
            }
        }

        return wd;
    }

}
