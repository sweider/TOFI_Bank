package by.bsuir.sweider_b.banksystem.shared.services.credits;

import java.io.Serializable;

/**
 * Created by sweid on 08.12.2015.
 */
public enum PaymentType  implements Serializable{
    ONCE_FULL("Разово в конце срока"),
    PERIODLY("Ежемесячно");

    private final String ufName;

    PaymentType(String ufName) {
        this.ufName = ufName;
    }

    public String getUfName() {
        return ufName;
    }
}
