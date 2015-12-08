package by.bsuir.sweider_b.domain.credit;

import by.bsuir.sweider_b.domain.activerecord.ActiveRecord;
import by.bsuir.sweider_b.domain.bankbill.BankBill;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by sweid on 08.12.2015.
 */
@Entity
@Table(name = "credit_obligations")
public class CreditObligation extends ActiveRecord {

    @ManyToOne
    @JoinColumn(name = "credit_kind_id")
    private CreditKind creditKind;

    @Column(name = "loaned_money_amount")
    private long loanedMoneyAmount;

    @Column(name = "next_payment_date")
    private Date nextPaymentDate;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "loaned_money_bill_id")
    private BankBill loanedMoneyObligationBill;


    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "percents_obligation_bill_id")
    private BankBill persentsObligationBill;


    public CreditObligation(CreditKind creditKind, long loanedMoneyAmount, Date nextPaymentDate) {
        this.creditKind = creditKind;
        this.loanedMoneyAmount = loanedMoneyAmount;
        this.nextPaymentDate = nextPaymentDate;
        this.loanedMoneyObligationBill = new BankBill();
    }

    public BankBill getPersentsObligationBill() {
        return persentsObligationBill;
    }

    public CreditKind getCreditKind() {
        return creditKind;
    }

    public long getLoanedMoneyAmount() {
        return loanedMoneyAmount;
    }

    public Date getNextPaymentDate() {
        return nextPaymentDate;
    }

    public BankBill getLoanedMoneyObligationBill() {
        return loanedMoneyObligationBill;
    }

    public void setNextPaymentDate(Date nextPaymentDate) {
        this.nextPaymentDate = nextPaymentDate;
    }
}
