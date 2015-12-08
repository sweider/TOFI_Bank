package by.bsuir.sweider_b.domain.bankbill;

import by.bsuir.sweider_b.domain.activerecord.ActiveRecord;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by sweid on 06.12.2015.
 */
@Entity
@Table(name = "bank_bills")
public class BankBill extends ActiveRecord {

    @Column(name = "amount")
    private long amount;

    @OneToMany
    private List<BillChange> billChanges;

    public BankBill() {
        this.billChanges = new ArrayList<>();
        this.amount = 0;
    }

    public BankBill(int amount){
        this.billChanges = new ArrayList<>();
        this.amount = amount;
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
