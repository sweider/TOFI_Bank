package by.bsuir.sweider_b.banksystem.server.domain.credit;

import by.bsuir.sweider_b.banksystem.server.domain.activerecord.ActiveRecord;

import javax.persistence.*;
import java.util.Optional;

/**
 * Created by sweid on 08.12.2015.
 */
@Entity
@Table(name = "credit_kinds")
public class CreditKind extends ActiveRecord{
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "duration_in_month")
    private int durationInMonth;

    @Column(name = "min_money_amount")
    private long minMoneyAmount;

    @Column(name = "max_money_amount")
    private long maxMoneyAmount;

    @Column(name = "percents")
    private int percents;
//
//    @Column(name = "is_prepayment_allowed")
//    private boolean isPrepaymentAllowed;
//
//    @Column(name = "payment_type")
//    @Enumerated(EnumType.STRING)
//    private PaymentType paymentType;

    @Column(name = "is_active")
    private boolean isActive;


    private CreditKind(){}

    public CreditKind(String name, String description, int durationInMonth, long minAmount, long maxAmount,int percents){
        this.name = name;
        this.description = description;
        this.durationInMonth = durationInMonth;
        this.maxMoneyAmount = maxAmount;
        this.minMoneyAmount = minAmount;
        this.percents = percents;
//        this.isPrepaymentAllowed = isPrepaymentAllowed;
//        this.paymentType = paymentType;
        this.setActive(true);
    }


    public int getPercents() {
        return percents;
    }

    public boolean isActive(){
        return this.isActive;
    }

    public void setActive(boolean value){
        this.isActive = value;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getDurationInMonth() {
        return durationInMonth;
    }

    public long getMinMoneyAmount() {
        return minMoneyAmount;
    }

    public long getMaxMoneyAmount() {
        return maxMoneyAmount;
    }

//    public boolean isPrepaymentAllowed() {
//        return isPrepaymentAllowed;
//    }
//
//    public PaymentType getPaymentType() {
//        return paymentType;
//    }

    public void setDescription(String desc){
        this.description = desc;
    }


    public static Filter<CreditKind> filter(){
        return ActiveRecord.filter(CreditKind.class);
    }

    public static Optional<CreditKind> find(int id){
        return ActiveRecord.find(CreditKind.class, id);
    }
}
