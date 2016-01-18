package by.bsuir.sweider_b.banksystem.shared.services.creditaplications;

import java.io.Serializable;

/**
 * Created by sweid on 18.01.2016.
 */
public class CreditApplicationDO implements Serializable{
    private int customerId;
    private int creditKindId;
    private long sum;
    private long income;
    private int id;

    public CreditApplicationDO(int customerId, int creditKindId, long sum, long income) {
        this.customerId = customerId;
        this.creditKindId = creditKindId;
        this.sum = sum;
        this.income = income;
    }

    public CreditApplicationDO(int customerId, int creditKindId, long sum, long income, int id) {
        this.customerId = customerId;
        this.creditKindId = creditKindId;
        this.sum = sum;
        this.income = income;
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getCreditKindId() {
        return creditKindId;
    }

    public long getSum() {
        return sum;
    }

    public long getIncome() {
        return income;
    }

    public int getId() {
        return id;
    }
}
