package by.bsuir.sweider_b.domain.bankbill;

import by.bsuir.sweider_b.domain.activerecord.ActiveRecord;

import javax.persistence.*;
import java.util.ArrayList;

/**
 * Created by sweid on 06.12.2015.
 */
@Entity
@Table(name = "bank_bills")
public class BankBill extends ActiveRecord {

    @Column(name = "amount")
    private long amount;

    @OneToMany
    @JoinColumn(name = "bill_history_id")
    private ArrayList<BillChange> billChanges;

    public BankBill() {
        this.billChanges = new ArrayList<>();
    }

    public void addMoney(long amount){
        this.amount += amount;
        this.billChanges.add(new BillChange(BillChangeType.ADDING, amount));
    }

    public void subtractMoney(long amount) throws InsufficientFundsException {
        if(this.amount - amount < 0) throw new InsufficientFundsException();
        this.amount -= amount;
        this.billChanges.add(new BillChange(BillChangeType.SUBTRACTION, amount));
    }

}
