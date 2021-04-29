package org.apache.poi.xwpf.converter.core.styles.run;

import org.apache.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import org.apache.poi.xwpf.converter.core.styles.run.AbstractRunValueProvider;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnderline;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STUnderline;

import java.util.Objects;

/**
 * @Description: 这里是为了解决jar包中空指针的问题，相同的包名和类名，会优先加载这里的类
 * @Author: liangruihao
 * @Date: 2021/4/29 17:24
 */
public class RunUnderlineValueProvider extends AbstractRunValueProvider<UnderlinePatterns> {
    public static final org.apache.poi.xwpf.converter.core.styles.run.RunUnderlineValueProvider INSTANCE = new org.apache.poi.xwpf.converter.core.styles.run.RunUnderlineValueProvider();

    public RunUnderlineValueProvider() {
    }

    public UnderlinePatterns getValue(CTRPr rPr, XWPFStylesDocument stylesDocument) {
        if (Objects.isNull(rPr)) {
            return null;
        }
        if (rPr.isSetU()) {
            CTUnderline ctu = rPr.getU();
            if (Objects.nonNull(ctu)) {
                STUnderline.Enum val = ctu.getVal();
                if (Objects.nonNull(val)) {
                    return UnderlinePatterns.valueOf(val.intValue());
                }
            }
        }
        return null;
    }
}
