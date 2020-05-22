package imobiliare;

import imobiliare.anunt.Anunt;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

/**
 * Created by vancer at 5/21/2020
 */
public class Calculations {

    public static BigDecimal calculateAverage(Set<Anunt> anuntSet) {
        if (anuntSet.size() == 0)
            return BigDecimal.ZERO;

        BigDecimal sumOfEuroPeMetruPatratAlTuturorAnunturilor = BigDecimal.valueOf(0D);

        for (Anunt anunt : anuntSet) {
            BigDecimal euroPeMetruPatratAlAnuntului = anunt.getPretPeMetruPatrat();

            sumOfEuroPeMetruPatratAlTuturorAnunturilor = sumOfEuroPeMetruPatratAlTuturorAnunturilor.
                    add(euroPeMetruPatratAlAnuntului);
        }
        return sumOfEuroPeMetruPatratAlTuturorAnunturilor.divide(BigDecimal.valueOf(anuntSet.size()), RoundingMode.HALF_UP);
    }

    public static BigDecimal calculatePretPeMetruPatrat (BigDecimal pret, BigDecimal metriPatrati) {
        if (pret == null || metriPatrati == null) {
            return null;
        }
        final BigDecimal divide = pret.divide(metriPatrati, RoundingMode.HALF_UP);
        return divide;
    }
}
