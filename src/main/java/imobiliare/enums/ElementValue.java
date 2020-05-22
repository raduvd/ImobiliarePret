package imobiliare.enums;

import java.math.BigDecimal;

public enum ElementValue {

    PRET_CU_PUNCT(BigDecimal.class, Boolean.TRUE, null),
    PRET_CU_VIRGULA(BigDecimal.class, null, Boolean.TRUE),
    METRI_PATRATI(BigDecimal.class, null, null),
    TEXT(String.class, null, null);

    private ElementValue(Class<?> tipDeData, Boolean eliminateDot, Boolean replaceComaWithDot) {
        this.eliminateDot = eliminateDot;
        this.tipDeData = tipDeData;
        this.replaceComaWithDot = replaceComaWithDot;
    }

    private Class<?> tipDeData;
    private Boolean eliminateDot;
    Boolean replaceComaWithDot;
}
