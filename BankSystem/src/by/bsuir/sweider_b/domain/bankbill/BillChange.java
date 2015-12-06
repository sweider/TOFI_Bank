package by.bsuir.sweider_b.domain.bankbill;

import javax.persistence.*;

/**
 * Created by sweid on 06.12.2015.
 */
@Entity
@Table(name = "bill_changes")
public class BillChange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "change_type")
    @Enumerated(EnumType.STRING)
    private BillChangeType changeType;

    @Column(name = "amount")
    private long amount;

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