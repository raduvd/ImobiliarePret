package imobiliare;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by vancer at 5/20/2020
 */
public class Main {

    /**
     * Aici am pathul local (de pe PC) catre chromedriver.exe
     */
    public static final String LINK_PAGINA_CU_LISTA_APARTAMENTE_CLUJ = "https://www.imobiliare.ro/vanzare-apartamente/cluj-napoca";
    public static final int NUMBER_OF_PAGES = 1;

    public static void main(String[] args) {

        Page page = new Page(LINK_PAGINA_CU_LISTA_APARTAMENTE_CLUJ);
        //Only unique Anunt will be added
        Set<Anunt> hashSet = new HashSet<>();

        for (int i = 0; i < NUMBER_OF_PAGES; i++) {
            hashSet.addAll(page.getListaDeAnunturiDinPage());
            //if the nextPage() returns false (the page was not changed) finish the loop with
            if (!page.nextPage()) {
                break;
            }
        }

        BigDecimal average = calculateAverage(hashSet);
        //TODO write to file the result and remove the below print
        System.out.println("MEDIA PE METRU PATRAT LA APARTAMENTE CLUJ DIN "
                + hashSet.size() + " DE ANUNTURI: " + average + " - La data de: " + LocalDate.now().toString());
    }


    private static BigDecimal calculateAverage(Set<Anunt> anuntSet) {
        BigDecimal sumOfEuroPeMetruPatratAlTuturorAnunturilor = BigDecimal.valueOf(0D);

        for (Anunt anunt : anuntSet) {
            BigDecimal euroPeMetruPatratAlAnuntului =
                    BigDecimal.valueOf(anunt.getPret()).
                            divide(BigDecimal.valueOf(anunt.getMetriPatrati()), RoundingMode.HALF_UP);

            sumOfEuroPeMetruPatratAlTuturorAnunturilor = sumOfEuroPeMetruPatratAlTuturorAnunturilor.
                    add(euroPeMetruPatratAlAnuntului);
        }
        return sumOfEuroPeMetruPatratAlTuturorAnunturilor.divide(BigDecimal.valueOf(anuntSet.size()), RoundingMode.HALF_UP);
    }
}
