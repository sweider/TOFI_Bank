/*
        * To change this license header, choose License Headers in Project Properties.
        * To change this template file, choose Tools | Templates
        * and open the template in the editor.
        */
package by.bsuir.sweider_b.domain.activerecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author alex
 */
public abstract class ActiveRecord {
    private final static Logger LOGGER = LoggerFactory.getLogger(ActiveRecord.class);

    private static SessionFactory sessionFactory;

    private volatile boolean persistInDb;


    private static final ThreadLocal<Optional<Session>> threadSession = new ThreadLocal<Optional<Session>>(){
        @Override
        protected Optional<Session> initialValue() {
            return Optional.empty();
        }
    };

    private static final ThreadLocal<Optional<Transaction>> threadTransaction = new ThreadLocal<Optional<Transaction>>(){
        @Override
        protected Optional<Transaction> initialValue() {
            return Optional.empty();
        }
    };

    //<editor-fold defaultstate="collapsed" desc="Session/transaction forming wrappers">

    /**
     * Использовать внутри вызова doInSingleSession
     * Предполагается вызов внутри методов для сохранения / изменения данных
     * Это значит save / delete
     * По контракту использовать вложенные вызовы данного метода запрещается
     * @param callable собственно что нужно выполнить.
     * @return результат выполнения переданного Callable
     */
    public static final <T> T doInSingleTransaction(Callable<T> callable) throws ActiveRecordException {
        final Optional<Session> optSession = threadSession.get();
        Session session = optSession
                .orElseThrow(() -> new ActiveRecordException("You should wrap this method in ActiveRecord.doInSingleSession call!"));
        if(threadTransaction.get().isPresent()) { throw new ActiveRecordException("Subtransactions doesn't allowed!"); }

        Transaction tx = null;
        T result = null;
        try{
            tx = session.beginTransaction();
            threadTransaction.set(Optional.of(tx));
            result = callable.call();
            tx.commit();
        }
        catch(Exception ex){
            if(tx!=null) { tx.rollback(); }
            throw new ActiveRecordException(ex);
        }
        finally{
            threadTransaction.remove();
        }
        return result;
    }

    /**
     * Обертка для выполнения указанных операций в пределах одной сессии.
     * Допускает использование внутри множественных невложенных вызовов doInSingleTransaction()
     * По контракту запрещены вложенные вызовы этого метода
     * @param callable
     * @throws ActiveRecordException
     */
    public static final <T> T doInSingleSession(Callable<T> callable) throws ActiveRecordException{
        final Optional<Session> optSession = threadSession.get();
        if(optSession.isPresent()) { throw new ActiveRecordException("Subsessions not allowed while in single session!"); }
        Session session = sessionFactory.openSession();
        threadSession.set(Optional.of(session));
        T result = null;
        try{
            result = callable.call();
        }
        catch(Exception ex){
            throw new ActiveRecordException(ex);
        }
        finally{
            session.close();
            threadSession.remove();
        }
        return result;
    }

//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getting records methods">

    /**
     * Пытается найти запись с таки id.
     * @return найденную запись либо null, если запись не найдена
     */
    protected static final <T> T find(Class<T> clazz, int id) {
        Optional<Session> optSession = threadSession.get();
        Session session = optSession.orElse(sessionFactory.openSession());
        T record;
        try{
            record = session.get(clazz, id);
        }catch(HibernateException ex){
            record = null;
            LOGGER.error("Error during getting record of class {} with id{}. Exception is {}",clazz, id, ex);
        }finally{
            if(!optSession.isPresent()) { session.close(); }
        }
        return record;
    }


    protected static final <T> T first(Class<T> clazz, Criterion ... restrictions){
        return new Filter<>(clazz).where(restrictions).first();
    }

    /**
     * Возвращает все записи, удовлетворящющие критериям.
     * Если критерии не переданы -- возвращает все найденные записи.
     * Если ни одна запись не удовлетворяет критериям -- возвращает пустой список
     */
    protected static final <T> List<T> all(Class<T> clazz, Criterion ... restrictions) {
        return new Filter(clazz).where(restrictions).get();
    }


    protected static final <T> Filter<T> filter(Class<T> clazz){
        return new Filter<>(clazz);
    }


//</editor-fold>


    /**
     * Сохраняет данную запись в базе.
     * В случае успешного сохранения возвращает true иначе false
     */
    public boolean save(){
        try{
            if(persistInDb) { this.performInSession(this::update); }
            else{ this.performInSession(this::save); }
            return true;
        }
        catch(HibernateException ex){
            LOGGER.error("Failed to save record#{}, exception is {}", this, ex);
            return false;
        }
    }

    /**
     *  Удаляет данную запись из базы данных.
     *  Если удаление успешно -- возвращает true, иначе возвращает false.
     */
    public boolean delete(){
        try{
            if(persistInDb){
                this.performInSession(this::delete);
                this.persistInDb = false;
            }
            return true;
        }
        catch(HibernateException ex){
            LOGGER.error("Failed to delete record#{}. Exception is {}", this,ex);
            return false;
        }
    }

    protected abstract void setIdAfterSave(int id);


    private  void save(Session session){
        int id = (int) session.save(this);
        this.setIdAfterSave(id);
        this.persistInDb = true;
    }

    private  void delete(Session session){
        session.delete(this);
    }

    private void update(Session session) {
        session.update(this);
    }

    private void performInSession(Consumer<Session> consumer) throws HibernateException{
        final Optional<Session> optSession = threadSession.get();
        Session session = optSession.orElse(sessionFactory.openSession());
        this.performInTransaction(session, consumer);
        if(!optSession.isPresent()){ session.close(); }
    }

    private void performInTransaction(Session session,  Consumer<Session> consumer) throws HibernateException{
        final Optional<Transaction> optTransaction = threadTransaction.get();
        final boolean haveToBeAttachedToOpenedTransaction = optTransaction.isPresent();
        if(haveToBeAttachedToOpenedTransaction){ consumer.accept(session);  }
        else{ this.performInSeparateTransaction(session, consumer); }
    }

    private void performInSeparateTransaction(Session session, Consumer<Session> consumer) throws HibernateException{
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            consumer.accept(session);
            tx.commit();
        }
        catch(HibernateException ex){
            if(tx != null) { tx.rollback();}
            LOGGER.error("Failed to perform transaction.",ex);
            throw ex;
        }
    }

    public static class Filter<T>{
        private final static int NOT_SET = -1;

        private final Class<T> clazz;
        private Criterion[] restrictions;
        private Order order;
        private int limit;
        private Entry<String,String>[] aliasses;

        private Filter(Class<T> clazz){
            this.clazz = clazz;
            this.limit = NOT_SET;
        }

        public Filter<T> where(Criterion ... restrictions){
            this.restrictions = restrictions;
            return this;
        }

        public Filter<T> orderBy(Order order){
            this.order = order;
            return this;
        }

        public Filter<T> aliasses(Entry<String, String> ... aliasses){
            this.aliasses = aliasses;
            return this;
        }

        public Filter<T> limit(int limit){
            this.limit = limit;
            return this;
        }

        public List<T> get(){
            Optional<Session> optSession = ActiveRecord.threadSession.get();
            Session session = optSession.orElse(ActiveRecord.sessionFactory.openSession());
            List<T> entries;
            try {
                Criteria criteria = session.createCriteria(clazz);
                if(this.aliasses != null){
                    for(Entry<String, String> alias : this.aliasses){
                        criteria = criteria.createAlias(alias.getKey(), alias.getValue());
                    }
                }
                if(this.restrictions != null){
                    for(Criterion restriction : restrictions ){
                        criteria.add(restriction);
                    }
                }
                if(this.order != null) { criteria.addOrder(this.order); }
                if(this.limit != NOT_SET) { criteria.setMaxResults(this.limit); }
                entries = criteria.list();
            } catch (HibernateException ex) {
                LOGGER.error("Error during getting records of class {} for restictions{}. Exception is {}", this.clazz, this.restrictions, ex);
                entries = new ArrayList<>();
            } finally {
                if(!optSession.isPresent()) { session.close(); }
            }
            return entries;
        }

        public T first(){
            this.limit = 1;
            List<T> list = this.get();
            return list.isEmpty() ? null : list.get(0);
        }

        public T find(int id){
            Optional<Session> optSession = threadSession.get();
            Session session = optSession.orElse(sessionFactory.openSession());
            T record;
            try{
                record =  session.get(clazz, id);
            }catch(HibernateException ex){
                record = null;
                LOGGER.error("Error during getting record of class {} with id{}. Exception is {}",clazz, id, ex);
            }finally{
                if(!optSession.isPresent()) { session.close(); }
            }
            return record;
        }

    }

}
