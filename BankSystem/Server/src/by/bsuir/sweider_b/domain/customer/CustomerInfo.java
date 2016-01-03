package by.bsuir.sweider_b.domain.customer;

import by.bsuir.sweider_b.domain.activerecord.ActiveRecord;

import javax.persistence.*;

/**
 * Created by sweid on 06.12.2015.
 */
@Entity
@Table(name = "customers_info")
public class CustomerInfo extends ActiveRecord {

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToOne
    @JoinColumn(name = "passport_data_id")
    private PassportData passportData;

    public Address getAddress() {
        return address;
    }

    public PassportData getPassportData() {
        return passportData;
    }

}
