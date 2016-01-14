package by.bsuir.sweider_b.banksystem.server.domain.bankbill;

import by.bsuir.sweider_b.banksystem.server.domain.activerecord.ActiveRecord;

import javax.persistence.*;

/**
 * Created by sweid on 06.12.2015.
 */
@Entity
@Table(name = "bill_changes")
public class BillChange extends ActiveRecord {

    @Column(name = "change_type")
    @Enumerated(EnumType.STRING)
    private BillChangeType changeType;

    @Column(name = "amount")
    private long amount;


    @ManyToOne
    @JoinColumn(name = "bank_bill_id")
    private BankBill bankBill;

    public BillChange(BillChangeType changeType, long amount) {
        this.changeType = changeType;
        this.amount = amount;
    }

    private BillChange(){}

    public BillChangeType getChangeType() {
        return changeType;
    }

    public long getAmount() {
        return amount;
    }
}
