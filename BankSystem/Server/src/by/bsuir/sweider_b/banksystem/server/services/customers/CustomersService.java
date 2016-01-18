package by.bsuir.sweider_b.banksystem.server.services.customers;

import by.bsuir.sweider_b.banksystem.server.domain.customer.Customer;
import by.bsuir.sweider_b.banksystem.server.domain.customer.CustomerInfo;
import by.bsuir.sweider_b.banksystem.shared.services.customers.*;
import org.hibernate.criterion.Restrictions;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by sweid on 08.12.2015.
 */
@Service
public class CustomersService implements ICustomersService{

    @Autowired
    private BCryptPasswordEncoder ENCODER;

    public CustomersService(){

    }

    @Override
    public void addNewCustomer(String sessionId, CustomerDO newCustomer) throws RemoteException, CustomerCreationException {
        Optional custWithLogin = Customer.filter().aliasses(new AbstractMap.SimpleEntry<>("userCredentials", "uc"))
                .where(Restrictions.eq("uc.login" , newCustomer.getLogin()))
                .first();
        if(custWithLogin.isPresent()) { throw new CustomerCreationException("Клиент с таким логином уже существует"); }

        Optional custWithPassport = Customer.filter().aliasses(new AbstractMap.SimpleEntry<>("customerInfo", "ci"), new AbstractMap.SimpleEntry<>("ci.passportData", "pd"))
                .where(Restrictions.eq("pd.passportNumber", newCustomer.getPassportNumber()))
                .first();
        if(custWithPassport.isPresent()) { throw new CustomerCreationException("Данный паспортный номер уже указан для другого клиента"); }

        Customer newCust = new Customer(newCustomer.getPassportNumber(), newCustomer.getName(), newCustomer.getSurName(), newCustomer.getLastName(),
                newCustomer.getLogin(), ENCODER.encode(newCustomer.getPassword()), newCustomer.getCity(), newCustomer.getStreet(),
                newCustomer.getBuilding(), newCustomer.getRoom(), newCustomer.getPhoneNumber());
        if(!newCust.save()){
            throw new CustomerCreationException("При сохранении клиента произошла непредвиденная ошибка, попробуйте еще раз");
        }
    }

    @Override
    public void updateCustomer(String sessionId, int customerId, CustomerDO newParams) throws RemoteException, CustomerUpdatingException {
        Customer customer = Customer.find(customerId).orElseThrow( () -> new CustomerUpdatingException("Данный клиент не найден в базе!"));
        CustomerInfo ci = customer.getCustomerInfo();

        ci.getAddress().setCity(newParams.getCity());
        ci.getAddress().setStreet(newParams.getStreet());
        ci.getAddress().setBuilding(newParams.getBuilding());
        ci.getAddress().setRoom(newParams.getRoom());

        ci.getPassportData().setPassportNumber(newParams.getPassportNumber());

        if(!ci.save()){
            throw new CustomerUpdatingException("При обновлении данных пользователя произошла непредвиденная ошибка, попробуйте еще раз");
        }

    }

    @Override
    public List<CustomerDO> showCustomers() throws RemoteException {
        return Customer.filter().where(Restrictions.eq("isActive", true)).get()
                .stream().map(this::getCustomerDO)
                .collect(Collectors.toList());
    }

    @NotNull
    private CustomerDO getCustomerDO(Customer customer) {
        return new CustomerDO(
                customer.getId(),
                customer.getCustomerInfo().getPassportData().getName(),
                customer.getCustomerInfo().getPassportData().getLastname(),
                customer.getCustomerInfo().getPassportData().getSurname(),
                customer.getCustomerInfo().getPassportData().getPassportNumber(),
                customer.getCustomerInfo().getAddress().getCity(),
                customer.getCustomerInfo().getAddress().getStreet(),
                customer.getCustomerInfo().getAddress().getBuilding(),
                customer.getCustomerInfo().getAddress().getRoom(),
                customer.getCustomerInfo().getPhoneNumber()
        );
    }

    @Override
    public void changePasswordForCustomer(String sessionId, int customerId, String newPassword) throws RemoteException, CustomerUpdatingException {
        Customer customer =  Customer.find(customerId).orElseThrow(() -> new CustomerUpdatingException("Пользователь не найден!"));
        customer.getUserCredentials().setPassword(ENCODER.encode(newPassword));
        if(!customer.save()){
            throw new CustomerUpdatingException("При обновлении пароля произошла непредвиденная ошибка, попробуйте еще раз.");
        }
    }

    @Override
    public CustomerDO getCustomerByPassportNumber(String sessionId, String passportNumber) throws RemoteException, CustomerNotFoundException {
        Customer customer = Customer.filter().aliasses(new AbstractMap.SimpleEntry<>("customerInfo", "ci"), new AbstractMap.SimpleEntry<>("ci.passportData", "pd"))
                .where(Restrictions.eq("pd.passportNumber", passportNumber))
                .first()
                .orElseThrow(CustomerNotFoundException::new);
        return this.getCustomerDO(customer);
    }

    @Override
    public List<CustomerDO> getCustomersByFio(String sessionId, String name, String lastName, String surName) throws RemoteException, CustomerNotFoundException {
        List<Customer> customers = Customer.filter().aliasses(new AbstractMap.SimpleEntry<>("customerInfo", "ci"), new AbstractMap.SimpleEntry<>("ci.passportData", "pd"))
                .where(
                        Restrictions.eq("pd.name", name),
                        Restrictions.eq("pd.lastname", lastName),
                        Restrictions.eq("pd.surname", surName))
                .get();
        if(customers.isEmpty()) throw new CustomerNotFoundException();
        return customers.stream().map(this::getCustomerDO).collect(Collectors.toList());
    }
}
